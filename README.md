# Quarkus Rage4j

[![Version](https://img.shields.io/maven-central/v/io.quarkiverse.rage4j/quarkus-rage4j?logo=apache-maven&style=flat-square)](https://central.sonatype.com/artifact/io.quarkiverse.rage4j/quarkus-rage4j)

A Quarkus extension that integrates [Rage4j](https://github.com/rage4j/rage4j) for testing AI services with RAG (Retrieval-Augmented Generation) evaluation metrics. This extension enables you to validate the quality of your LangChain4j-based AI services in Quarkus applications.

## Features

- 🎯 **Answer Correctness**: Evaluate how accurate your AI service responses are
- 🔗 **Faithfulness**: Measure if the answer is faithful to the provided context
- 📊 **Answer Relevance**: Check if responses are relevant to the question
- 🔍 **Semantic Similarity**: Compare semantic similarity between answers and ground truth
- 🔌 **Seamless Integration**: Works with Quarkus and LangChain4j AI services
- ✅ **JUnit 5 Support**: Easy integration with your existing test suite

## Installation

Add the extension to your Quarkus project:

```xml
<dependency>
    <groupId>io.quarkiverse.rage4j</groupId>
    <artifactId>quarkus-rage4j</artifactId>
    <version>VERSION</version>
    <scope>test</scope>
</dependency>
```

You'll also need a LangChain4j provider (e.g., OpenAI):

```xml
<dependency>
    <groupId>io.quarkiverse.langchain4j</groupId>
    <artifactId>quarkus-langchain4j-openai</artifactId>
    <version>1.3.1</version>
</dependency>
```

## Configuration

Quarkus Rage4j allows you to **separately configure** two different AI models:

1. **Judge Model** (`quarkus.rage4j.*`): The LLM used by Rage4j to evaluate and judge the quality of your AI service's responses
2. **Executing Model** (`quarkus.langchain4j.*`): Your application's AI service that is being tested

This separation allows you to:
- Use different providers for judging vs execution (e.g., OpenAI for judging, Ollama for your service)
- Use different models (e.g., GPT-4 for judging, GPT-3.5 for your service)
- Use different API keys or endpoints for each purpose

### Using OpenAI (Default)

Configure your API keys in `application.properties`:

```properties
# Judge Model Configuration (used by Rage4j to evaluate responses)
quarkus.rage4j.api-key=your-openai-api-key-for-judge

# Executing Model Configuration (your AI service being tested)
quarkus.langchain4j.openai.api-key=your-openai-api-key-for-service
```

**Note:** These can be the same API key if you want, but they are configured separately to give you flexibility.

### Using Ollama

To use Ollama, you can configure it for either or both the judge model and your executing model:

#### Example 1: Ollama for Judge, OpenAI for Executing Model

```properties
# Judge Model Configuration (Ollama)
quarkus.rage4j.provider=ollama
quarkus.rage4j.ollama-base-url=http://localhost:11434
quarkus.rage4j.api-key=not-used
quarkus.rage4j.chat-model=llama3.2
quarkus.rage4j.embedding-model=nomic-embed-text

# Executing Model Configuration (OpenAI)
quarkus.langchain4j.openai.api-key=your-openai-api-key
```

#### Example 2: OpenAI for Judge, Ollama for Executing Model

```properties
# Judge Model Configuration (OpenAI)
quarkus.rage4j.provider=openai
quarkus.rage4j.api-key=your-openai-api-key
quarkus.rage4j.chat-model=gpt-4

# Executing Model Configuration (Ollama)
quarkus.langchain4j.ollama.base-url=http://localhost:11434
quarkus.langchain4j.ollama.chat-model.model-id=llama3.2
```

#### Example 3: Both Using Ollama

```properties
# Judge Model Configuration (Ollama)
quarkus.rage4j.provider=ollama
quarkus.rage4j.ollama-base-url=http://localhost:11434
quarkus.rage4j.api-key=not-used
quarkus.rage4j.chat-model=llama3.2
quarkus.rage4j.embedding-model=nomic-embed-text

# Executing Model Configuration (Ollama)
quarkus.langchain4j.ollama.base-url=http://localhost:11434
quarkus.langchain4j.ollama.chat-model.model-id=llama3.2
```

To use Ollama as the judge model, you need to:

1. Add the LangChain4j Ollama dependency to your project:

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-ollama</artifactId>
    <version>1.2.0</version>
</dependency>
```

2. Make sure you have Ollama running locally with the models you want to use:

```bash
ollama pull llama3.2
ollama pull nomic-embed-text
```

## Usage

### 1. Create Your AI Service

First, define a LangChain4j AI service using `@RegisterAiService`:

```java
import dev.langchain4j.service.SystemMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.enterprise.context.ApplicationScoped;

@RegisterAiService
@ApplicationScoped
@SystemMessage("""
    You are a helpful assistant. Your task is to answer questions clearly, 
    precisely, and in a friendly manner. You support the user in understanding 
    concepts, solving problems, and creating content.
    """)
public interface MyAiService {
    String chat(String question);
}
```

### 2. Set Up Your Test Class

Create a test class with the Rage4j extension and inject required components:

```java
import io.quarkiverse.rage4j.runtime.annotations.TestAIService;
import io.quarkiverse.rage4j.runtime.junitextension.Rage4jTestExtension;
import io.quarkiverse.rage4j.runtime.wrapper.RageAssert;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@QuarkusTest
@ExtendWith(Rage4jTestExtension.class)
class MyAiServiceTest {

    @Inject
    RageAssert rageAssert;

    @Inject
    MyAiService aiService;

    @TestAIService
    public String answer(String question) {
        return aiService.chat(question);
    }

    // Your tests go here
}
```

### 3. Write Your Tests

#### Test Answer Correctness

Verify that your AI service provides correct answers:

```java
@Test
void testAnswerCorrectness() {
    String groundTruth = """
        The answer to "life, the universe, and everything" is famously 
        known to be **42**. This concept originates from Douglas Adams' 
        science fiction series "The Hitchhiker's Guide to the Galaxy."
        """;
    
    rageAssert
        .question("What is the answer to life, the universe and everything?")
        .groundTruth(groundTruth)
        .threshold(0.50)
        .assertAnswerCorrectness();
}
```

#### Test with Expected Failure

You can also test scenarios where the answer should be incorrect:

```java
@Test
void shouldFailWithIncorrectGroundTruth() {
    assertThrows(Rage4JCorrectnessException.class, () -> 
        rageAssert
            .question("What is the answer to life, the universe and everything?")
            .groundTruth("Nothing.")
            .threshold(0.50)
            .assertAnswerCorrectness()
    );
}
```

### 4. Available Assertions

The `RageAssert` API provides several assertion methods:

```java
rageAssert
    .question("Your question")
    .groundTruth("Expected answer")
    .threshold(0.7)
    .assertAnswerCorrectness();  // Evaluates overall correctness
```

Available assertion methods:
- `assertAnswerCorrectness()` - Evaluates the overall correctness of the answer
- `assertFaithfulness()` - Checks if the answer is faithful to the provided context
- `assertAnswerRelevance()` - Verifies that the answer is relevant to the question
- `assertSemanticSimilarity()` - Compares semantic similarity between the answer and ground truth

#### Chaining Multiple Assertions

You can chain multiple assertions to evaluate different aspects of the response:

```java
@Test
void testMultipleMetrics() {
    rageAssert
        .question("What is the answer to life, the universe and everything?")
        .groundTruth("The answer is 42, from The Hitchhiker's Guide to the Galaxy.")
        .threshold(0.7)
        .assertAnswerCorrectness()
        .assertAnswerRelevance()
        .assertSemanticSimilarity()
        .assertFaithfulness();
}
```

## How It Works

1. **`@TestAIService` Annotation**: Mark a method with this annotation to define how your AI service (executing model) should be called
2. **`RageAssert` API**: Use the fluent API to configure your test with question, ground truth, and threshold
3. **Separate Model Configuration**: 
   - The **Judge Model** (configured via `quarkus.rage4j.*`) is used by Rage4j to evaluate the quality of responses
   - The **Executing Model** (configured via `quarkus.langchain4j.*`) is your AI service that generates the responses being tested
4. **Evaluation**: The judge model uses LLM-based metrics to evaluate the quality of the executing model's responses
5. **Threshold**: Scores range from 0.0 to 1.0; assertions pass if the score meets or exceeds the threshold

## Example Project Structure

```
src/
├── main/
│   └── java/
│       └── com/example/
│           └── MyAiService.java
└── test/
    ├── java/
    │   └── com/example/
    │       └── MyAiServiceTest.java
    └── resources/
        └── application.properties
```

## Configuration Properties

### Judge Model Configuration

These properties configure the LLM used by Rage4j to evaluate your AI service:

| Property | Description | Default | Required |
|----------|-------------|---------|----------|
| `quarkus.rage4j.api-key` | API key for the judge model LLM provider (not used by Ollama, but required) | - | Yes |
| `quarkus.rage4j.provider` | LLM provider for the judge model: `openai` or `ollama` | `openai` | No |
| `quarkus.rage4j.ollama-base-url` | Base URL for Ollama API (only used when provider is `ollama`) | `http://localhost:11434` | No |
| `quarkus.rage4j.chat-model` | Custom chat model name for the judge (provider-specific defaults used if not specified) | Provider default | No |
| `quarkus.rage4j.embedding-model` | Custom embedding model name for the judge (provider-specific defaults used if not specified) | Provider default | No |

### Executing Model Configuration

Your AI service (the model being tested) is configured separately using LangChain4j properties:

| Property | Description | Required |
|----------|-------------|----------|
| `quarkus.langchain4j.openai.api-key` | OpenAI API key for your AI service | Yes (if using OpenAI) |
| `quarkus.langchain4j.ollama.base-url` | Ollama base URL for your AI service | Yes (if using Ollama) |
| `quarkus.langchain4j.*.chat-model.*` | Chat model configuration for your AI service | Provider-specific |

For complete LangChain4j configuration options, see the [Quarkus LangChain4j documentation](https://docs.quarkiverse.io/quarkus-langchain4j/dev/index.html).

### Provider-Specific Defaults

**OpenAI:**
- Chat Model: `gpt-5.1`
- Embedding Model: `text-embedding-3-small`

**Ollama:**
- Chat Model: `llama3.2`
- Embedding Model: `nomic-embed-text`

## Requirements

- Java 21 or higher
- Quarkus 3.x
- An OpenAI API key (if using OpenAI provider)
- Ollama instance (if using Ollama provider)

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Links

- [Rage4j GitHub](https://github.com/explore-de/rage4j)
- [Quarkus LangChain4j](https://docs.langchain4j.dev/)
