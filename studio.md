# Studio — AI Medical Assistant
## Roadmap di apprendimento con risorse

> Ordine consigliato: segui le fasi in sequenza. Ogni fase costruisce sulla precedente.

---

## Fase 1 — Fondamenta Architetturali

Prima di scrivere codice, questi due testi ti danno il linguaggio per progettare sistemi complessi.
Sono la differenza tra un developer che implementa e uno che progetta.

### Designing Data-Intensive Applications — Martin Kleppmann
Il libro più importante per capire sistemi distribuiti, event sourcing, CQRS e database a scala.
Obbligatorio prima di toccare Kafka e Redis seriamente.

- **Libro (acquisto):** https://www.oreilly.com/library/view/designing-data-intensive-applications/9781491903063/
- **Note gratuite sul libro:** https://gist.github.com/bcherny/b870a60d1650973df7e400c8603ac76d
- **Capitoli prioritari:** 3 (Storage & Retrieval), 5 (Replication), 11 (Stream Processing), 12 (The Future of Data Systems)

### Microservices Patterns — Chris Richardson
Patterns concreti per microservizi: CQRS, Event Sourcing, Saga, API Gateway.

- **Sito con tutti i pattern (gratuito):** https://microservices.io/patterns/index.html
- **Pattern CQRS:** https://microservices.io/patterns/data/cqrs.html
- **Pattern Event Sourcing:** https://microservices.io/patterns/data/event-sourcing.html
- **Libro:** https://www.manning.com/books/microservices-patterns

---

## Fase 2 — CQRS / Event Sourcing

### Teoria
- **Sito di riferimento (cqrs.com):** https://www.cqrs.com/
- **CQRS Journey — Microsoft (PDF gratuito):** https://www.microsoft.com/en-us/download/details.aspx?id=34774
- **Awesome CQRS / Event Sourcing su GitHub:** https://github.com/leandrocp/awesome-cqrs-event-sourcing
- **Greg Young — CQRS & Event Sourcing (talk originale, YouTube):** https://www.youtube.com/watch?v=8JKjvY4etTY

### Hands-on
- **Workshop Event Sourcing con Python:** https://github.com/oskardudycz/EventSourcing.NetCore/tree/main/Workshops/IntroductionToEventSourcing
- **Event Sourcing con PostgreSQL (pg_eventstore):** https://awesome-architecture.com/event-sourcing/

---

## Fase 3 — Apache Kafka

### Partenza rapida (gratuita)
- **Kafka 101 — Confluent (ufficiale, hands-on, ~90 min):** https://developer.confluent.io/courses/apache-kafka/events/
- **Apache Kafka for Python Developers — Confluent:** https://developer.confluent.io/get-started/python/
- **Kafka in 6 minuti (YouTube):** https://www.youtube.com/watch?v=Ch5VhJzaoaI

### Corso completo
- **Apache Kafka for Beginners v3 — Stephane Maarek (Udemy, ~$15):** https://www.udemy.com/course/apache-kafka/
  - Aggiornato ad agosto 2025 con Kafka 4.0. Il corso di riferimento per Kafka. 130.000+ studenti.
- **Versione Coursera (gratuita con audit):** https://www.coursera.org/learn/packt-apache-kafka-series-learn-apache-kafka-for-beginners-v3-cjher

### Documentazione
- **Kafka Official Docs:** https://kafka.apache.org/documentation/
- **GitHub — Curated Kafka Learning Resources:** https://github.com/pmoskovi/kafka-learning-resources

---

## Fase 4 — Redis Avanzato

### Fondamenta
- **Redis University (gratuita, ufficiale):** https://university.redis.io/
- **Redis Data Structures — Documentazione:** https://redis.io/docs/data-types/
- **Sorted Sets (per slot medici):** https://redis.io/docs/data-types/sorted-sets/

### Use case specifici del progetto
- **Rate Limiting con Redis:** https://redis.io/glossary/rate-limiting/
- **Redis come Session Store:** https://redis.io/solutions/session-management/
- **Semantic Caching con Redis + Vector Search:** https://redis.io/blog/what-is-llm-semantic-caching/

---

## Fase 5 — AI Engineering & LLM

### RAG (Retrieval-Augmented Generation)
- **DeepLearning.AI — Building and Evaluating Advanced RAG (gratuito):** https://www.deeplearning.ai/short-courses/building-evaluating-advanced-rag/
- **pgvector + LangChain + Claude — Tutorial hands-on:** https://zilliz.com/tutorials/rag/langchain-and-pgvector-and-anthropic-claude-3-haiku-and-ollama-all-minilm
- **pgvector + Claude 3.5 — Tutorial:** https://www.tigerdata.com/blog/retrieval-augmented-generation-with-claude-sonnet-3-5-and-pgvector
- **pgvector — Documentazione ufficiale:** https://github.com/pgvector/pgvector

### LangGraph (Agenti)
- **LangGraph Academy — Corso ufficiale gratuito:** https://academy.langchain.com/courses/intro-to-langgraph
- **AI Agents in LangGraph — DeepLearning.AI (gratuito):** https://www.deeplearning.ai/short-courses/ai-agents-in-langgraph/
- **LangGraph Docs:** https://langchain-ai.github.io/langgraph/
- **Tutorial LangGraph per principianti:** https://www.analyticsvidhya.com/blog/2025/05/langgraph-tutorial-for-beginners/

### Anthropic API (Tool Use)
- **Tool Use — Documentazione ufficiale Anthropic:** https://docs.anthropic.com/en/docs/build-with-claude/tool-use
- **Anthropic API — Guida completa:** https://docs.anthropic.com/en/api/getting-started
- **Prompt Engineering Guide:** https://docs.anthropic.com/en/docs/build-with-claude/prompt-engineering/overview

### Semantic Caching
- **Cos'è il Semantic Caching (Redis blog):** https://redis.io/blog/what-is-llm-semantic-caching/
- **GPTCache — libreria Python per semantic caching:** https://github.com/zilliztech/GPTCache

### LLM Observability
- **LangSmith (tracing e monitoring LLM):** https://www.langchain.com/langsmith
- **Helicone — LLM observability open source:** https://www.helicone.ai/
- **OpenTelemetry per LLM:** https://opentelemetry.io/docs/

### Guardrails
- **NeMo Guardrails — NVIDIA:** https://github.com/NVIDIA/NeMo-Guardrails
- **Guardrails AI:** https://www.guardrailsai.com/

---

## Fase 6 — Docker & Kubernetes

### Docker
- **Play with Docker (hands-on gratuito, nel browser):** https://labs.play-with-docker.com/
- **Docker Official Getting Started:** https://docs.docker.com/get-started/
- **Docker Compose per microservizi:** https://docs.docker.com/compose/

### Kubernetes
- **Kubernetes Official Tutorials (gratuiti):** https://kubernetes.io/docs/tutorials/
- **Learn Kubernetes in Under 3 Hours — freeCodeCamp (YouTube):** https://www.youtube.com/watch?v=X48VuDVv0do
- **Kube by Example — Kubernetes Fundamentals:** https://kubebyexample.com/
- **Helm — Packaging K8s apps:** https://helm.sh/docs/intro/quickstart/

### Corso completo
- **Learn DevOps: Docker, Kubernetes, Terraform, Azure DevOps (Udemy ~$15):** https://www.udemy.com/course/devops-with-docker-kubernetes-and-azure-devops/

---

## Fase 7 — Terraform

### Partenza
- **HashiCorp Learn — Terraform Get Started (gratuito, ufficiale):** https://developer.hashicorp.com/terraform/tutorials/aws-get-started
- **Terraform + Kubernetes — Tutorial ufficiale HashiCorp:** https://developer.hashicorp.com/terraform/tutorials/kubernetes
- **Crash Course Terraform — Gruntwork Blog:** https://www.gruntwork.io/blog/a-crash-course-on-terraform

### Corso
- **Terraform per AWS da zero (Udemy):** https://www.udemy.com/course/terraform-beginner-to-advanced/
- **Terraform Associate Certification Prep:** https://developer.hashicorp.com/terraform/tutorials/certification-003

### Libro
- **Terraform: Up & Running — Yevgeniy Brikman (O'Reilly):** https://www.terraformupandrunning.com/

---

## Fase 8 — CI/CD

- **GitHub Actions — Documentazione ufficiale:** https://docs.github.com/en/actions
- **GitHub Actions per Python (tutorial):** https://docs.github.com/en/actions/use-cases-and-examples/building-and-testing/building-and-testing-python
- **CI/CD per Docker + Kubernetes con GitHub Actions:** https://docs.github.com/en/actions/use-cases-and-examples/publishing-packages/publishing-docker-images

---

## Risorse Bonus

### System Design (per colloqui senior)
- **System Design Primer — GitHub (gratuito, 280k+ stelle):** https://github.com/donnemartin/system-design-primer
- **Grokking System Design — Educative:** https://www.educative.io/courses/grokking-the-system-design-interview

### Datasets del progetto
- **MedQuAD — Medical Q&A Dataset:** https://github.com/abachaa/MedQuAD
- **MIMIC-III — Clinical Data (richiede registrazione):** https://physionet.org/content/mimiciii/1.4/

### Community e newsletter
- **The Batch — Newsletter AI (DeepLearning.AI):** https://www.deeplearning.ai/the-batch/
- **r/MachineLearning:** https://www.reddit.com/r/MachineLearning/
- **r/devops:** https://www.reddit.com/r/devops/

---

## Ordine di Studio Consigliato

```
Settimana 1-2   →  DDIA (capitoli 11-12) + Microservices Patterns (sito)
Settimana 3     →  CQRS/Event Sourcing (Greg Young talk + CQRS Journey PDF)
Settimana 4-5   →  Kafka 101 Confluent + Kafka for Python
Settimana 6     →  Redis University + use case specifici
Settimana 7-8   →  LangGraph Academy + DeepLearning.AI RAG
Settimana 9     →  Anthropic Tool Use docs + costruisci il primo agente
Settimana 10    →  Docker + Kubernetes tutorials
Settimana 11-12 →  Terraform HashiCorp Learn + GitHub Actions
```

> **Regola pratica:** per ogni risorsa teorica, scrivi subito del codice.
> Non passare alla fase successiva senza un piccolo progetto funzionante sulla fase corrente.