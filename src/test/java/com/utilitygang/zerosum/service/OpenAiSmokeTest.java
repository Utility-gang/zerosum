package com.utilitygang.zerosum.service;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Tag;
import static org.junit.jupiter.api.Assertions.*;

@Tag("smoke")
@SpringBootTest
@ActiveProfiles("test")
class OpenAiSmokeTest {

    @Autowired
    private OpenAiService openAiService;

    @Test
    void realApi_returnsNonEmptyText_whenKeyIsPresent() {
        String key = System.getenv("OPENAI_API_KEY");
        Assumptions.assumeTrue(key != null && !key.isBlank(), "OPENAI_API_KEY not set");

        String text = openAiService.funnyStockDescription("TSCO", "Tractor Supply");

        System.out.println("SMOKE RESULT >>> " + text);
        assertNotNull(text);
        assertFalse(text.isBlank());
    }
}
