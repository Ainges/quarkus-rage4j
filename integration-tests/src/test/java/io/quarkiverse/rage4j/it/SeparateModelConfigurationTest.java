package io.quarkiverse.rage4j.it;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;

import io.quarkiverse.rage4j.runtime.holder.ApiKeyHolder;
import io.quarkus.test.junit.QuarkusTest;

/**
 * Test to verify that the Judge Model and Executing Model can be configured separately.
 *
 * The Judge Model (configured via quarkus.rage4j.*) is used by Rage4j to evaluate
 * the quality of responses from your AI service.
 *
 * The Executing Model (configured via quarkus.langchain4j.*) is your AI service
 * that generates responses and is being tested.
 *
 * This separation allows you to:
 * - Use different providers (e.g., OpenAI for judging, Ollama for your service)
 * - Use different models (e.g., GPT-4 for judging, GPT-3.5 for your service)
 * - Use different API keys or endpoints
 */
@QuarkusTest
class SeparateModelConfigurationTest {

    @Inject
    ApiKeyHolder apiKeyHolder;

    @Test
    void shouldConfigureJudgeModelIndependently() {
        // Verify that the judge model is configured via quarkus.rage4j.* properties
        assertNotNull(apiKeyHolder.getApiKey(), "Judge model API key should be configured");
        assertNotNull(apiKeyHolder.getProvider(), "Judge model provider should be configured");

        // The executing model (AI service being tested) is configured separately
        // via quarkus.langchain4j.* properties and doesn't affect the judge configuration
        assertEquals("openai", apiKeyHolder.getProvider(), "Default judge provider should be OpenAI");
    }

    @Test
    void shouldSupportOptionalJudgeModelCustomization() {
        // Custom chat and embedding models can be specified for the judge
        // These are optional and provider-specific defaults are used if not specified

        // In this test setup, no custom models are specified, so these should be empty
        assertTrue(apiKeyHolder.getChatModel().isEmpty(),
                "Chat model should not be configured when not specified in application.properties");
        assertTrue(apiKeyHolder.getEmbeddingModel().isEmpty(),
                "Embedding model should not be configured when not specified in application.properties");
    }

    @Test
    void shouldConfigureOllamaBaseUrlForJudge() {
        // When using Ollama as the judge provider, the base URL can be configured
        assertNotNull(apiKeyHolder.getOllamaBaseUrl(), "Ollama base URL should have a default value");
        assertEquals("http://localhost:11434", apiKeyHolder.getOllamaBaseUrl(),
                "Default Ollama base URL should be localhost:11434");
    }
}
