package com.utilitygang.zerosum.service;

import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseOutputItem;
import com.openai.models.responses.ResponseOutputMessage;
import com.openai.models.responses.ResponseOutputText;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * These tests mock the OpenAI Java SDK response object graph
 * (Response → ResponseOutputItem → ResponseOutputMessage → Content → OutputText)
 * to verify that the extractor correctly:
 *  - returns assistant text when present
 *  - ignores non-message output items
 *  - handles missing / empty content safely
 *
 * No real API calls are made.
 */




class OpenAiResponseTest {

    @Test
    void extractOutputText_returnsText_whenOutputTextPresent() {
        // Mock SDK objects
        Response response = mock(Response.class);
        ResponseOutputItem item = mock(ResponseOutputItem.class);
        ResponseOutputMessage message = mock(ResponseOutputMessage.class);
        ResponseOutputMessage.Content content = mock(ResponseOutputMessage.Content.class);
        ResponseOutputText outputText = mock(ResponseOutputText.class);

        // Arrange the tree:
        // response.output() -> [item]
        when(response.output()).thenReturn(List.of(item));

        // item is a message item
        when(item.isMessage()).thenReturn(true);
        when(item.message()).thenReturn(Optional.of(message));

        // message has content list
        when(message.content()).thenReturn(List.of(content));

        // content is output text
        when(content.isOutputText()).thenReturn(true);
        when(content.asOutputText()).thenReturn(outputText);

        // output text contains the final string
        when(outputText.text()).thenReturn("Hello misery.");

        // Act
        String extracted = OpenAiResponse.extractOutputText(response);

        // Assert
        assertEquals("Hello misery.", extracted);
    }

    @Test
    void extractOutputText_returnsEmpty_whenResponseNull() {
        assertEquals("", OpenAiResponse.extractOutputText(null));
    }

    @Test
    void extractOutputText_returnsEmpty_whenNoItems() {
        Response response = mock(Response.class);
        when(response.output()).thenReturn(List.of());

        assertEquals("", OpenAiResponse.extractOutputText(response));
    }

    @Test
    void extractOutputText_returnsEmpty_whenItemNotMessage() {
        Response response = mock(Response.class);
        ResponseOutputItem item = mock(ResponseOutputItem.class);

        when(response.output()).thenReturn(List.of(item));
        when(item.isMessage()).thenReturn(false);

        assertEquals("", OpenAiResponse.extractOutputText(response));
    }

    @Test
    void extractOutputText_returnsEmpty_whenMessageMissing() {
        Response response = mock(Response.class);
        ResponseOutputItem item = mock(ResponseOutputItem.class);

        when(response.output()).thenReturn(List.of(item));
        when(item.isMessage()).thenReturn(true);
        when(item.message()).thenReturn(Optional.empty());

        assertEquals("", OpenAiResponse.extractOutputText(response));
    }

    @Test
    void extractOutputText_returnsEmpty_whenContentIsOutputTextButBlank() {
        Response response = mock(Response.class);
        ResponseOutputItem item = mock(ResponseOutputItem.class);
        ResponseOutputMessage message = mock(ResponseOutputMessage.class);
        ResponseOutputMessage.Content content = mock(ResponseOutputMessage.Content.class);
        ResponseOutputText outputText = mock(ResponseOutputText.class);

        when(response.output()).thenReturn(List.of(item));
        when(item.isMessage()).thenReturn(true);
        when(item.message()).thenReturn(Optional.of(message));
        when(message.content()).thenReturn(List.of(content));

        when(content.isOutputText()).thenReturn(true);
        when(content.asOutputText()).thenReturn(outputText);
        when(outputText.text()).thenReturn("   ");

        assertEquals("", OpenAiResponse.extractOutputText(response));
    }
}
