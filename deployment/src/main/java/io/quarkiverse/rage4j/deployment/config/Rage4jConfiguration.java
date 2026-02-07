package io.quarkiverse.rage4j.deployment.config;

import java.util.Optional;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@ConfigMapping(prefix = "quarkus.rage4j")
@ConfigRoot(phase = ConfigPhase.BUILD_TIME)
public interface Rage4jConfiguration {
    /**
     * This apikey is used to call the LLM Api for the judge model, it is always required.
     * Note: This is separate from the API key used by your application's AI service
     * (configured via quarkus.langchain4j.* properties).
     */
    String apiKey();

    /**
     * The LLM provider to use for the judge model evaluation. Defaults to "openai".
     * Supported values: "openai", "ollama".
     * Note: This configures the judge model, not your application's AI service.
     * Your application's AI service is configured separately via quarkus.langchain4j.* properties.
     */
    @WithDefault("openai")
    String provider();

    /**
     * The base URL for the Ollama API when using Ollama as the judge model provider.
     * Only required when provider is "ollama".
     * Defaults to http://localhost:11434
     */
    @WithDefault("http://localhost:11434")
    String ollamaBaseUrl();

    /**
     * The chat model name to use for the judge model.
     * Optional, provider-specific defaults will be used if not specified.
     * This is the model that will evaluate your AI service's responses.
     */
    Optional<String> chatModel();

    /**
     * The embedding model name to use for the judge model.
     * Optional, provider-specific defaults will be used if not specified.
     * This is used by the judge for semantic similarity calculations.
     */
    Optional<String> embeddingModel();
}
