package com.utilitygang.zerosum.service;

import com.openai.client.OpenAIClient;
import com.openai.models.responses.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OpenAiServiceTest {

    @Test
    void returnsFallback_whenOpenAiThrows() {
        OpenAIClient client = mock(OpenAIClient.class, RETURNS_DEEP_STUBS);
        when(client.responses().create(any(com.openai.models.responses.ResponseCreateParams.class)))

                .thenThrow(new RuntimeException("boom"));

        OpenAiService service = new OpenAiService(client);

        String result = service.funnyStockDescription("TSLA", "Tesla");

        assertNotNull(result);
        assertTrue(result.contains("interpretive dance"));
    }
}
