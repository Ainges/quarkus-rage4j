package io.quarkiverse.rage4j.it;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;

import io.quarkiverse.rage4j.runtime.holder.ApiKeyHolder;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class ConfigurationTest {

    @Inject
    ApiKeyHolder apiKeyHolder;

    @Test
    void shouldHaveDefaultOpenAiProvider() {
        assertNotNull(apiKeyHolder.getProvider());
        assertEquals("openai", apiKeyHolder.getProvider());
    }

    @Test
    void shouldHaveDefaultOllamaBaseUrl() {
        assertNotNull(apiKeyHolder.getOllamaBaseUrl());
        assertEquals("http://localhost:11434", apiKeyHolder.getOllamaBaseUrl());
    }

    @Test
    void shouldHaveApiKey() {
        assertNotNull(apiKeyHolder.getApiKey());
        assertEquals("someApiKey", apiKeyHolder.getApiKey());
    }
}
