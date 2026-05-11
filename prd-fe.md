# Frontend Product Requirements Document (PRD) & UI/UX Design System
## AI Medical Assistant — Premium Frontend Experience

**Versione:** 1.0
**Data:** Maggio 2026
**Framework:** Next.js 14 (App Router) + React Server Components

---

## 1. Visione del Design e UX (Aestethics & Feel)

### 1.1 Concetto Base
L'interfaccia non deve sembrare la classica dashboard medica noiosa, ma un'esperienza "Premium, Dinamica e Rassicurante". L'utente deve percepire immediatamente un alto livello tecnologico (AI) combinato con estrema affidabilità (Medicale). 

### 1.2 Principi di Design
- **Glassmorphism & Profondità:** Uso di sfondi traslucidi, blur effect e ombre morbide per creare gerarchia visiva senza appesantire l'interfaccia.
- **Dynamic Micro-Animations:** L'interfaccia deve "respirare". Animazioni fluide durante il caricamento (es. quando l'AI "pensa" o "cerca nei database"), hover states reattivi e transizioni di pagina morbide (via Framer Motion).
- **Rassicurante ma Tecnologico:** Sfruttare temi scuri moderni (Dark Mode nativa) con accenti luminosi e vibranti (es. Neon Teal, Soft Blue) per indicare l'attività dell'AI, senza risultare aggressivi.

---

## 2. Design System & Tokens

### 2.1 Tipografia
- **Primary Font (Headings):** `Outfit` o `Clash Display` — Moderno, geometrico e d'impatto.
- **Secondary/Body Font:** `Inter` o `Geist` — Massima leggibilità per i testi medici prolungati e i messaggi di chat.

### 2.2 Palette Colori (Modern Dark Theme)
- **Background:** `#0A0A0F` (Deep Midnight Blue/Black)
- **Surface (Cards/Chat Bubbles):** `#1A1A24` (Slightly elevated dark) con bordi `#2A2A35`
- **Primary Accent (AI Core):** `#00D2FF` to `#3A7BD5` (Soft gradient per bottoni primari e indicatori AI)
- **Success/Health:** `#00E676` (Neon Green per conferme e slot disponibili)
- **Warning/Triage:** `#FF3D00` (Vibrant Red per urgenze e allarmi triage)
- **Text:** `#E2E8F0` (Slate 200 per body), `#FFFFFF` per Headings.

### 2.3 Librerie e Strumenti UI
- **Styling:** TailwindCSS (Strictly used for utility, no generic palettes)
- **Componenti base:** Radix UI primitives o shadcn/ui (estremamente customizzati per rimuovere il look "standard")
- **Animazioni:** Framer Motion (per layout transitions, chat bubbles in-out)
- **Iconografia:** Lucide Icons o Phosphor Icons (variante Duotone)

---

## 3. Core User Flows & Viste Principali

### 3.1 Vista Principale: "The AI Consultation Room" (Chat)
La pagina core dell'applicazione. Non una semplice chat WhatsApp-style, ma una console di consultazione spaziale.
- **Layout:** Chat fluida al centro con un pannello laterale (collapsible) a destra per il "Medical Context" (riassunto sintomi attuali estratti in tempo reale dall'agente).
- **AI Tool Execution Visuals:** Quando LangGraph invoca un tool (es. *get_available_doctors*), la chat non deve bloccarsi. Mostrare un indicatore dinamico animato (es. icona radar rotante con testo `"Analisi database specialisti in corso..."`).
- **Streaming Response:** Il testo del Claude LLM deve apparire in modo fluido (Typewriter effect fluido), formattato in Markdown ricco (bold, liste, tabelle).
- **Message Bubbles:**
  - Utente: Glassy blur effect, allineato a destra.
  - AI: Sfondo scuro pulito, allineato a sinistra, con un leggero border-left del colore Primario (Teal).

### 3.2 Triage Alert System (Emergenza)
Se il *triage-service* Kafka event scatta con "HIGH urgency":
- **UI Override:** L'interfaccia deve interrompere visivamente le altre azioni. Un banner rosso traslucido con glow effect scende dall'alto (Framer Motion `y: -100` to `y: 0`).
- **Action Buttons:** Pulsante pulsante e immediato per "Contatta 118" o "Avvia protocollo Emergenza", molto visibile.

### 3.3 Dashboard: Smart Doctor Booking
Quando l'AI consiglia un medico, la UI non deve solo generare testo, ma renderizzare una **Interactive Component Card** direttamente nella chat (UI generativa / Vercel AI SDK style).
- **Card Medico:** Foto (se disponibile), specializzazione, punteggio matching, e slider orizzontale degli slot orari disponibili (recuperati da Redis).
- **Interazione:** L'utente può cliccare su uno slot e confermare l'appuntamento senza lasciare la conversazione. Un'animazione di "Morphing" trasforma la card in una "Conferma Prenotazione" (ticket style).

---

## 4. Architettura Tecnica Frontend

### 4.1 Struttura Next.js (App Router)
```
src/
├── app/
│   ├── (auth)/login/page.tsx
│   ├── (dashboard)/chat/page.tsx
│   └── (dashboard)/appointments/page.tsx
├── components/
│   ├── ui/          # Core atoms (buttons, inputs) - shadcn based
│   ├── chat/        # MessageBubbles, AgentActivityIndicators
│   └── booking/     # DoctorCards, TimeSlots
├── lib/
│   ├── hooks/       # useWebsocket, useChatStream
│   └── store/       # Zustand store per local state
└── styles/
    └── globals.css  # CSS Variables (HSL) per Tailwind
```

### 4.2 State Management & Dati
- **Global UI State:** `Zustand` (es. sidebar aperta/chiusa, tema).
- **Server State & Streaming:** Fetch API standard con `ReadableStream` per catturare lo streaming token-by-token dell'LLM (via `ai` package di Vercel, o custom SSE).
- **Real-time Eventi (Kafka -> Frontend):** WebSocket (o SSE) gestiti dal `chat-service` (NestJS) per aggiornamenti asincroni come notifiche di appuntamento confermato da altri sistemi.

### 4.3 SEO & Performance (Best Practices)
- **Server Components:** Utilizzati pesantemente per pagine statiche e layout (riduzione bundle Javascript).
- **Unique IDs & Semantic HTML:** Ogni elemento iterativo (messaggio, medico) usa `aria-labels` e semantic tags (`<article>`, `<section>`, `<h1>`).
- **Lazy Loading:** Componenti pesanti (animazioni Lottie/Framer) caricati dinamicamente.

---

## 5. Requisiti di Sviluppo (Step Successivi)
1. Inizializzare il progetto Next.js (`npx create-next-app@latest`).
2. Configurare `tailwind.config.ts` con i token di colore e i font menzionati.
3. Creare i componenti UI di base (bottone primario gradient, layout base).
4. Sviluppare la `ChatConsole` con supporto per messaggi mockati e animazioni Framer Motion.
