package io.quarkiverse.rage4j.runtime.recorder;

import java.util.Optional;

import io.quarkiverse.rage4j.runtime.holder.ApiKeyHolder;
import io.quarkus.arc.Arc;
import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class ApiKeyHolderRecorder {
    public void initApiKey(String apiKey, String provider, String ollamaBaseUrl, Optional<String> chatModel,
            Optional<String> embeddingModel) {
        ApiKeyHolder apiKeyHolder = Arc.container().instance(ApiKeyHolder.class).get();
        apiKeyHolder.setApiKey(apiKey);
        apiKeyHolder.setProvider(provider);
        apiKeyHolder.setOllamaBaseUrl(ollamaBaseUrl);
        apiKeyHolder.setChatModel(chatModel);
        apiKeyHolder.setEmbeddingModel(embeddingModel);
    }
}
