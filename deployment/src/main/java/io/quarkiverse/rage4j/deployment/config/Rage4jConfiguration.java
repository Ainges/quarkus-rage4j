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
     * This apikey is used to call the LLM Api, it is always required.
     */
    String apiKey();

    /**
     * The LLM provider to use for evaluation. Defaults to "openai".
     * Supported values: "openai", "ollama"
     */
    @WithDefault("openai")
    String provider();

    /**
     * The base URL for the Ollama API. Only required when provider is "ollama".
     * Defaults to http://localhost:11434
     */
    @WithDefault("http://localhost:11434")
    String ollamaBaseUrl();

    /**
     * The chat model name to use. Optional, provider-specific defaults will be used if not specified.
     */
    Optional<String> chatModel();

    /**
     * The embedding model name to use. Optional, provider-specific defaults will be used if not specified.
     */
    Optional<String> embeddingModel();
}
