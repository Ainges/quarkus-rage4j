package io.quarkiverse.rage4j.runtime.llmbuilder;

import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.rage4j.asserts.LLMBuilder;
import dev.rage4j.asserts.RageAssert;

public class OllamaLLMBuilder implements LLMBuilder<OllamaLLMBuilder> {
    private static final String DEFAULT_CHAT_MODEL = "llama3.2";
    private static final String DEFAULT_EMBEDDING_MODEL = "nomic-embed-text";

    private String baseUrl;
    private String chatModelName = DEFAULT_CHAT_MODEL;
    private String embeddingModelName = DEFAULT_EMBEDDING_MODEL;

    public OllamaLLMBuilder(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public OllamaLLMBuilder withChatModel(String modelName) {
        this.chatModelName = modelName;
        return this;
    }

    @Override
    public OllamaLLMBuilder withEmbeddingModel(String modelName) {
        this.embeddingModelName = modelName;
        return this;
    }

    @Override
    public RageAssert fromApiKey(String apiKey) {
        // API key is not used by Ollama, but required by the interface
        OllamaChatModel chatModel = OllamaChatModel.builder()
                .baseUrl(baseUrl)
                .modelName(chatModelName)
                .build();

        OllamaEmbeddingModel embeddingModel = OllamaEmbeddingModel.builder()
                .baseUrl(baseUrl)
                .modelName(embeddingModelName)
                .build();

        return new RageAssert(chatModel, embeddingModel);
    }
}
