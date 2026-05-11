-- Creazione utente principale (se non esiste)
-- Creazione dei database logici per i vari microservizi

CREATE DATABASE auth_db;
CREATE DATABASE booking_db;
CREATE DATABASE doctor_db;
CREATE DATABASE rag_db;

-- Connettiti a rag_db e abilita l'estensione pgvector
\c rag_db;
CREATE EXTENSION IF NOT EXISTS vector;
