# Product Requirements Document
## AI Medical Assistant — Architettura Enterprise

**Versione:** 2.0
**Data:** Maggio 2026
**Stato:** Draft
**Autore:** Mario Esposito

---

## 1. Visione del Prodotto

### 1.1 Obiettivo

Assistente virtuale intelligente per il settore sanitario basato su architettura a microservizi, event-driven con Kafka, CQRS/Event Sourcing e un sistema RAG agentico. Il sistema gestisce conversazioni mediche, triage automatico, raccomandazione medici e prenotazione appuntamenti — il tutto mantenendo piena tracciabilità degli eventi e scalabilità orizzontale.

### 1.2 Principi Architetturali

- **Event-driven**: ogni azione utente genera un evento immutabile su Kafka
- **CQRS**: separazione netta tra operazioni di scrittura (Command) e lettura (Query)
- **Event Sourcing**: lo stato della chat è derivato dalla sequenza di eventi, non da uno snapshot
- **Agentic AI**: il modello LLM decide autonomamente quale tool invocare ad ogni turno
- **Privacy by design**: nessun dato clinico nei log, GDPR applicato a livello infrastrutturale
- **Observability**: ogni chiamata LLM è tracciata con latenza, token, feedback e qualità della risposta

---

## 2. Architettura di Sistema

### 2.1 Panoramica

```
┌──────────────────────────────────────────────────────────┐
│                      API Gateway                          │
│          (authn/authz, rate limiting, routing)            │
└────┬─────────┬─────────┬─────────┬────────────┬──────────┘
     │         │         │         │            │
  Chat      Booking   Doctor    Auth       Notification
 Service    Service   Service   Service      Service
     │         │         │
     └────┬────┴────┬────┘
          │         │
     Kafka Event Bus (topics per dominio)
          │         │
     ┌────┴────┬────┴────┐
  RAG        Triage   Analytics
 Service     Service   Service
```

### 2.2 Microservizi

| Servizio | Responsabilità | Stack |
|----------|---------------|-------|
| **api-gateway** | Routing, auth JWT, rate limiting | Nginx / Kong |
| **auth-service** | Registrazione, login, JWT | FastAPI + PostgreSQL |
| **chat-service** | Gestione sessioni e messaggi (Command + Query) | FastAPI + Kafka + MongoDB |
| **rag-service** | Pipeline RAG + Agentic tool use | FastAPI + LangGraph + pgvector |
| **triage-service** | Sentiment analysis + urgency scoring | FastAPI + Kafka |
| **booking-service** | Slot, prenotazioni (CQRS) | FastAPI + PostgreSQL + Redis |
| **doctor-service** | Profili medici, specializzazioni | FastAPI + PostgreSQL |
| **notification-service** | Email/SMS su eventi Kafka | FastAPI + Celery |
| **analytics-service** | LLM observability, metriche qualità | FastAPI + TimescaleDB |

---

## 3. CQRS / Event Sourcing

### 3.1 Command Side

Ogni azione dell'utente è un **Command** che produce uno o più **Event** immutabili pubblicati su Kafka.

```
Command                   →  Event prodotto
────────────────────────────────────────────────────────
SendMessage               →  MessageReceived
RequestDoctorSuggestion   →  DoctorRecommendationRequested
BookAppointment           →  AppointmentBooked
CancelAppointment         →  AppointmentCancelled
TriageEscalated           →  EmergencyFlagged
AgentToolInvoked          →  ToolCallExecuted
LLMResponseGenerated      →  ResponseDelivered
```

### 3.2 Event Store

Gli eventi sono **append-only** su un event store dedicato (PostgreSQL con extension `pg_eventstore` o tabella custom). Ogni evento contiene:

```json
{
  "event_id": "uuid",
  "aggregate_id": "session_id",
  "aggregate_type": "ChatSession",
  "event_type": "MessageReceived",
  "payload": { "text": "...", "user_id": "...", "timestamp": "..." },
  "metadata": { "causation_id": "...", "correlation_id": "...", "version": 1 },
  "created_at": "2026-05-10T10:00:00Z"
}
```

### 3.3 Query Side — Viste Materializzate

I **Read Model** vengono aggiornati in modo asincrono dagli eventi:

| Read Model | Storage | Aggiornato da |
|------------|---------|---------------|
| `chat_history` | MongoDB | `MessageReceived`, `ResponseDelivered` |
| `active_sessions` | Redis | `MessageReceived`, `SessionClosed` |
| `doctor_availability` | Redis Sorted Set | `AppointmentBooked`, `AppointmentCancelled` |
| `patient_appointments` | PostgreSQL | `AppointmentBooked`, `AppointmentCancelled` |
| `triage_dashboard` | PostgreSQL | `EmergencyFlagged`, `TriageEscalated` |

---

## 4. Kafka — Topics e Consumer Groups

### 4.1 Topics

```
chat.message.received          → rag-service, triage-service
chat.response.ready            → chat-service (deliver to user)
triage.urgency.scored          → chat-service, booking-service
booking.appointment.booked     → notification-service, doctor-service
booking.appointment.cancelled  → notification-service, doctor-service
llm.call.completed             → analytics-service
agent.tool.invoked             → analytics-service
```

### 4.2 Flusso Messaggio Utente

```
1. Utente invia messaggio
2. chat-service pubblica → [chat.message.received]
3. triage-service consuma → analizza urgenza → pubblica [triage.urgency.scored]
4. rag-service consuma → invoca agente LLM → pubblica [chat.response.ready]
5. chat-service consuma → invia risposta all'utente
6. analytics-service consuma entrambi → logga latenza, token, urgency score
```

### 4.3 Gestione Emergenze

Se `urgency = HIGH`:
```
triage-service pubblica → [triage.emergency.flagged]
chat-service → mostra banner 118 immediatamente (non aspetta RAG)
notification-service → notifica operatore sanitario di guardia
```

---

## 5. Redis — Utilizzi

| Uso | Struttura | TTL |
|-----|-----------|-----|
| Cache risposte LLM frequenti | Hash (semantic key → risposta) | 24h |
| Slot disponibilità medici | Sorted Set (timestamp → doctor_id) | Real-time |
| Session working memory | Hash (session_id → context window) | 2h |
| Rate limiting per utente | Counter con sliding window | 1 min |
| Semantic cache lookup | Hash (embedding hash → risposta) | 48h |

### 5.1 Semantic Caching

Prima di ogni chiamata LLM:
```
1. Calcola embedding della query utente
2. Cerca in Redis se esiste un embedding con cosine similarity > 0.92
3. Se sì → restituisci la risposta cachata (< 100ms)
4. Se no → chiama LLM → salva risposta in cache
```
Risparmio stimato: 30-40% delle chiamate API su domande mediche frequenti.

---

## 6. RAG Agentico

### 6.1 Architettura LangGraph

Invece di un pipeline RAG sequenziale, l'agente decide autonomamente quale tool invocare:

```python
tools = [
    search_medical_knowledge,   # MedQuAD + MIMIC-III su pgvector
    get_available_doctors,       # doctor-service via REST
    book_appointment,            # booking-service via REST
    get_patient_history,         # chat-service read model
    analyze_symptoms,            # chiamata specializzata al LLM
    check_urgency_level,         # triage-service via REST
]
```

**Flusso agentico**:
```
User: "Ho dolore al petto da stamattina, chi posso vedere?"

Agent step 1: check_urgency_level("dolore al petto") → HIGH
Agent step 2: risposta immediata con allerta 118
Agent step 3: search_medical_knowledge("dolore toracico differenziale")
Agent step 4: get_available_doctors(specialty="cardiologia", urgency="alta")
Agent step 5: presenta risultati + opzione prenotazione
```

### 6.2 Knowledge Base

| Dataset | Contenuto | Indicizzazione |
|---------|-----------|----------------|
| MedQuAD | 47.000 Q&A mediche | pgvector, chunking 512 token |
| MIMIC-III (subset anonimizzato) | Note cliniche strutturate | pgvector, chunking 256 token |
| Linee guida ISS | Protocolli sanitari italiani | pgvector, chunking 512 token |

### 6.3 Guardrails

Layer di validazione prima di ogni risposta:
- Blocco automatico di consigli farmacologici diretti (dosaggi, prescrizioni)
- Disclaimer obbligatorio su ogni risposta medica
- Rilevamento prompt injection
- Escalation automatica se l'agente non è sicuro (confidence < 0.7)

---

## 7. AI Observability

Ogni chiamata LLM viene tracciata nell'`analytics-service`:

```json
{
  "call_id": "uuid",
  "session_id": "...",
  "model": "claude-sonnet-4-20250514",
  "prompt_tokens": 1240,
  "completion_tokens": 380,
  "latency_ms": 1840,
  "tools_invoked": ["search_medical_knowledge", "get_available_doctors"],
  "urgency_score": 0.3,
  "cache_hit": false,
  "user_feedback": null,
  "guardrail_triggered": false,
  "timestamp": "2026-05-10T10:00:00Z"
}
```

Dashboard (Grafana):
- Latenza media per servizio
- Token/giorno e costi stimati
- Cache hit rate
- Distribuzione urgency score
- Guardrail triggers per categoria

---

## 8. Infrastructure as Code

### 8.1 Stack

```
Terraform     → provisioning cloud (AWS)
Kubernetes    → orchestrazione container
Helm          → packaging K8s manifests
Docker        → containerizzazione servizi
GitHub Actions → CI/CD pipeline
```

### 8.2 Terraform — Risorse AWS

```hcl
# Cluster K8s
module "eks" { ... }

# Database
resource "aws_rds_instance" "postgres" { ... }        # auth, booking, doctor, events
resource "aws_elasticache_cluster" "redis" { ... }    # cache, sessions, slots
resource "aws_msk_cluster" "kafka" { ... }            # event bus
resource "aws_opensearch_domain" "analytics" { ... }  # metriche LLM

# Storage
resource "aws_s3_bucket" "uploads" { ... }            # immagini diagnostiche

# Networking
resource "aws_vpc" { ... }
resource "aws_security_group" { ... }
```

### 8.3 Kubernetes — Per Servizio

```yaml
# Ogni microservizio ha:
- Deployment       (replica set, rolling update)
- Service          (ClusterIP interno)
- HorizontalPodAutoscaler  (scaling su CPU/memory)
- ConfigMap        (variabili d'ambiente non sensibili)
- Secret           (API key LLM, DB passwords)
- PodDisruptionBudget  (alta disponibilità)

# Infrastruttura condivisa:
- Ingress (NGINX)  → routing esterno
- CertManager      → TLS automatico
- Kafka (Strimzi operator)
- Redis (Bitnami Helm chart)
```

### 8.4 CI/CD Pipeline (GitHub Actions)

```
push → branch
  └── lint + test unitari
  └── build Docker image
  └── push su ECR

merge → main
  └── integration tests
  └── terraform plan (review)
  └── deploy su K8s staging
  └── smoke tests
  └── deploy su K8s production (manual approval)
```

---

## 9. Stack Tecnologico Completo

| Layer | Tecnologia |
|-------|------------|
| **API Gateway** | Kong / NGINX |
| **Backend** | FastAPI (Python 3.12) |
| **Agentic AI** | LangGraph + Anthropic API (tool use) |
| **LLM** | Claude Sonnet (Anthropic API) |
| **Vector DB** | pgvector su PostgreSQL |
| **Event Bus** | Apache Kafka (Strimzi su K8s) |
| **Cache / Sessions** | Redis 7 |
| **Database primario** | PostgreSQL 16 |
| **Event Store** | PostgreSQL (append-only) |
| **Document Store** | MongoDB (chat history read model) |
| **Frontend** | Next.js 14 (App Router) |
| **Observability** | Grafana + Prometheus + LangSmith |
| **Infrastructure** | Terraform + AWS EKS |
| **Orchestrazione** | Kubernetes + Helm |
| **CI/CD** | GitHub Actions |
| **Guardrails** | Custom validation layer |

---

## 10. Roadmap di Sviluppo

### Fase 1 — Fondamenta (Settimane 1-2)
- [x] Infrastruttura locale: docker-compose con tutti i servizi
- [x] auth-service: registrazione + JWT
- [ ] api-gateway: routing base
- [ ] Schema Kafka: topics e producer/consumer base

### Fase 2 — Core AI (Settimane 3-4)
- [ ] rag-service: pipeline RAG con pgvector + MedQuAD
- [ ] triage-service: sentiment analysis + urgency scoring
- [ ] chat-service: CQRS + event store + Kafka integration
- [ ] Semantic caching su Redis

### Fase 3 — Agentic (Settimane 5-6)
- [ ] Integrazione LangGraph con tool use
- [ ] Tutti i tool implementati e testati
- [ ] Guardrails layer
- [ ] Analytics service + Grafana dashboard

### Fase 4 — Booking & Notifications (Settimana 7)
- [ ] booking-service: CQRS + Redis slot management
- [ ] doctor-service: profili e disponibilità
- [ ] notification-service: email su eventi Kafka

### Fase 5 — Frontend (Settimana 8)
- [ ] Chat UI con streaming delle risposte
- [ ] Dashboard prenotazioni
- [ ] Visualizzazione triage alert

### Fase 6 — Infrastructure (Settimane 9-10)
- [ ] Dockerfile per ogni servizio
- [ ] Helm charts per K8s
- [ ] Terraform per AWS
- [ ] CI/CD pipeline completa
- [ ] Deploy su staging

---

## 11. Competenze Acquisite al Completamento

| Area | Competenza |
|------|-----------|
| **AI Engineering** | Agentic RAG, LangGraph, tool use, semantic caching, guardrails, LLM observability |
| **Backend** | Event-driven architecture, CQRS, Event Sourcing, Kafka, microservizi |
| **Infrastructure** | Terraform, Kubernetes, Helm, GitHub Actions CI/CD |
| **Database** | pgvector, MongoDB, Redis avanzato (Sorted Set, sliding window) |
| **Security** | JWT, rate limiting, GDPR by design, guardrails AI |