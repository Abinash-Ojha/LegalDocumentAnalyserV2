Legal Document Analyzer with RAG-Based Chat System

A backend system for analyzing legal documents and enabling users to query their own documents using Large Language Models (LLMs) with a production-grade Retrieval-Augmented Generation (RAG) pipeline.

This project focuses on building a scalable, cost-efficient, and accurate LLM backend by combining structured output parsing, rate limiting, embeddings, and vector similarity search.

🚀 Features
🔹 Structured Legal Analysis Output

Uses prompt templates to control LLM response format

Parses LLM JSON responses on the backend

Stores analysis in a relational schema mapped to headings instead of raw text blobs

Sends clean, structured data directly to the frontend

🔹 Rate Limiting & Abuse Protection (Redis + Fixed Window)

Implemented Redis-based fixed window rate limiting

Limits enforced:

LLM API: 2 requests per user per day

Normal APIs: 5 requests per user per 60 minutes

Protects against abuse and controls inference cost

🔹 RAG-Based Document Query System

Allows users to query their own legal documents with high accuracy and low cost.

📄 Document Ingestion

Documents are split into chunks

Each chunk is converted into an embedding using the Gemini embedding model

Embeddings are stored alongside chunks in PostgreSQL with PgVector

Embeddings are numerical vectors that represent the semantic meaning of text.

🔍 Query Flow

User question is converted into an embedding

Vector similarity search retrieves the top-K most relevant chunks

Relevance is determined by vector distance
(smaller distance = more similar meaning)

Only these chunks are sent to the LLM with a prompt

Benefits:

✅ Reduced hallucinations

✅ Lower token usage and cost

✅ Improved answer accuracy

✅ No fine-tuning required

🏗️ Architecture Overview
🔹 High-Level Flow

User uploads a legal document

Backend chunks the document and generates embeddings

Chunks + embeddings stored in PgVector

User submits a query

Query embedding generated

Top-K relevant chunks retrieved via vector similarity search

Chunks + prompt sent to LLM

Structured response returned to frontend

🛠️ Tech Stack

Backend: Java, Spring Boot, Spring WebFlux

Database: PostgreSQL (Users & Documents), PgVector (Embeddings)

Caching / Rate Limiting: Redis (Fixed Window Algorithm)

LLM & Embeddings: Google Gemini (Chat + Embedding Models)

ORM / Querying: JPA / JPQL with Vector Distance Queries

⚡ Key Learnings

Designing real-world RAG pipelines

Understanding how embeddings capture semantic meaning

Using vector databases for efficient semantic search

Importance of rate limiting for scalability, security, and cost control

Reducing hallucinations and token cost without fine-tuning

🔮 Future Improvements

Hybrid search (keyword + vector search)

Streaming LLM responses

Document-level access control

Multi-document query support

Caching frequent embeddings and queries

🖼️ Architecture Diagrams & Flow Visualizations
🔹 System Architecture & RAG Pipeline
Detailed Flow & Component Interaction

<img width="1536" height="1024" alt="Advanced legal document analyzer flowchart" src="https://github.com/user-attachments/assets/1adddfaf-ac8a-4231-ad65-5dfb82b48e94" />
<img width="1919" height="1017" alt="Screenshot 2026-01-19 235044" src="https://github.com/user-attachments/assets/08cad215-2dd7-451d-8bbb-f76d1fb5da33" />
<img width="1915" height="989" alt="Screenshot 2026-01-20 134111" src="https://github.com/user-attachments/assets/fd5fd5b6-e4ff-4517-80f0-467358e312f9" />
<img width="1915" height="989" alt="Screenshot 2026-01-20 134111" src="https://github.com/user-attachments/assets/7e5a3981-8ba2-442d-82d5-6ccf630eed8c" />
<img width="1919" height="1020" alt="Screenshot 2026-01-20 134409" src="https://github.com/user-attachments/assets/4f44d458-9778-45ed-a800-93a12df224bb" />
<img width="1919" height="1017" alt="Screenshot 2026-01-20 134527" src="https://github.com/user-attachments/assets/d83cc973-babb-4d01-8a61-c61eb98ee830" />
<img width="1919" height="1021" alt="Screenshot 2026-01-20 134716" src="https://github.com/user-attachments/assets/a1b4f981-ccc7-4c7b-b5c3-e2a957812231" />


🤝 Contributions & Feedback

This project is part of my Build in Public journey.
Feedback, suggestions, and improvements are highly welcome!

👨‍💻 Author

Abinash Ojha
Backend Engineer | LLM Systems | RAG Enthusiast

⭐ If you found this project interesting, consider starring the repository and sharing your feedback!
