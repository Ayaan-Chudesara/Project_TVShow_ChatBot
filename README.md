# TVBot
A fully private AI-powered web chatbot that recommends TV shows using open-source LLMs (via Ollama + Spring AI), vector search, and a fast Angular 16 frontend.
No cloud APIs required. Runs on your hardware with privacy and total control.

# 🛠️ Tech Stack
## Backend:

Java 17

Spring Boot 3.5.4

Spring Data JPA

Spring AI (Ollama integration via spring-ai-starter-model-ollama v1.0.0-M6)

PostgreSQL (hosted via Supabase, includes pgvector extension)

Ollama (for LLM & embedding models; serving locally)

## Frontend:

Angular 16 (TypeScript)

HTML5/CSS3

## Others:

Lombok

pgvector Java client

Postman/cURL (for API testing)

# 🌟 Features
Chatbot UI for friendly, contextual TV show recommendations

Open-source LLM (llama3 or similar, via Ollama) for response summarization

Embedding-based vector search (nomic-embed-text via Ollama)

PostgreSQL/pgvector for fast similarity search & storage

Lightweight, responsive Angular 16 frontend

Full privacy: No third-party or paid AI APIs, all processing happens locally

# 🚀 Quick Setup
## 1. Ollama & Models
Install Ollama

Run and pull required models:


### ollama pull llama3
### ollama pull nomic-embed-text
### ollama serve

## 2. Database (Supabase Postgres)
Create a Supabase project

Get your DB connection info (URL, user, password)

Set up pgvector extension (Supabase comes pre-loaded)

## 3. Backend (Spring Boot + Spring AI)
a. Clone and Configure
text
git clone https://github.com/your-username/tvbot.git
cd tvbot
In src/main/resources/application.properties, set:


spring.datasource.url=<your supabase db url>
spring.datasource.username=<your db user>
spring.datasource.password=<your db password>
spring.ai.ollama.embedding.options.model=nomic-embed-text
spring.ai.ollama.base-url=http://localhost:11434
spring.ai.ollama.chat.options.model=llama3
spring.ai.ollama.chat.base-url=http://localhost:11434
Pool size tip for Supabase:

spring.datasource.hikari.maximum-pool-size=5

b. Maven setup (in pom.xml):
Use the Spring AI BOM and latest Ollama model starter:

xml
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>org.springframework.ai</groupId>
      <artifactId>spring-ai-bom</artifactId>
      <version>1.0.0-M6</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>
<dependencies>
  <!-- Other dependencies -->
  <dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-model-ollama</artifactId>
  </dependency>
  <!-- ... -->
</dependencies>

 c. Build & Start

### mvn clean install
### mvn spring-boot:run Or, use your IDE's run config

## 4. Frontend (Angular 16)

### cd tvbot-frontend Or whatever your frontend dir is called
### npm install
### ng serve
Open http://localhost:4200 in your browser.

Chat UI will POST to your backend (Spring Boot, default port 8080).

Please ensure CORS is enabled on the backend for http://localhost:4200.

📝 Usage
Add TV Shows:
Use the web interface or POST endpoint to add TV shows with title, genre, and description.

Get Recommendations:
Enter a prompt like “I love thoughtful sci-fi with strong female leads.”
Get instant AI-powered recommendations with both LLM summary and a matching show list.
