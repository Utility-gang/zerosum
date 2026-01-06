package com.utilitygang.zerosum.service;

import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseOutputItem;
import com.openai.models.responses.ResponseOutputMessage;
import com.openai.models.responses.ResponseOutputText;

import java.util.List;

public final class OpenAiResponse {

    private OpenAiResponse() {}

    /**
     * Extract assistant output text from openai-java Response.
     */
    public static String extractOutputText(Response response) {
        if (response == null) return "";

        try {
        
            var m = response.getClass().getMethod("outputText");
            Object v = m.invoke(response);
            if (v != null) {
                String s = v.toString();
                if (!s.isBlank()) return s.trim();
            }
        } catch (Exception ignored) {}

        try {
            List<ResponseOutputItem> items = response.output();
            if (items == null || items.isEmpty()) return "";

            StringBuilder sb = new StringBuilder();

            for (ResponseOutputItem item : items) {
                if (item == null || !item.isMessage()) continue;

                ResponseOutputMessage message = item.message().orElse(null);

                if (message == null) continue;

                var contentList = message.content();
                if (contentList == null || contentList.isEmpty()) continue;

                for (ResponseOutputMessage.Content content : contentList) {
                    if (content == null) continue;
                    if (content.isOutputText()) {
                        ResponseOutputText ot = content.asOutputText();
                        if (ot != null && ot.text() != null && !ot.text().isBlank()) {
                            if (sb.length() > 0) sb.append("\n");
                            sb.append(ot.text().trim());
                        }
                    } else {
                        // Fallback path: if SDK shape changes, try outputText() directly
                        try {
                            Object ot = content.outputText();
                            if (ot instanceof ResponseOutputText rot) {
                                String t = rot.text();
                                if (t != null && !t.isBlank()) {
                                    if (sb.length() > 0) sb.append("\n");
                                    sb.append(t.trim());
                                }
                            }
                        } catch (Exception ignored) {}
                    }
                }
            }

            return sb.toString().trim();
        } catch (Exception e) {
            return "";
        }
    }
}
