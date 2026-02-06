package io.quarkiverse.rage4j.runtime.holder;

import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ApiKeyHolder {
    private String apiKey;
    private String provider;
    private String ollamaBaseUrl;
    private Optional<String> chatModel;
    private Optional<String> embeddingModel;

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getProvider() {
        return provider;
    }

    public void setOllamaBaseUrl(String ollamaBaseUrl) {
        this.ollamaBaseUrl = ollamaBaseUrl;
    }

    public String getOllamaBaseUrl() {
        return ollamaBaseUrl;
    }

    public void setChatModel(Optional<String> chatModel) {
        this.chatModel = chatModel;
    }

    public Optional<String> getChatModel() {
        return chatModel;
    }

    public void setEmbeddingModel(Optional<String> embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    public Optional<String> getEmbeddingModel() {
        return embeddingModel;
    }
}
