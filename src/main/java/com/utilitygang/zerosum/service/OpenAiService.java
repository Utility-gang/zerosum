package com.utilitygang.zerosum.service;

import com.openai.client.OpenAIClient;
import com.openai.models.ChatModel;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import org.springframework.stereotype.Service;

@Service
public class OpenAiService {

    private final OpenAIClient openai;

    public OpenAiService(OpenAIClient openai) {
        this.openai = openai;
    }

    public String funnyStockDescription(String symbol, String companyName) {
        String instructions =
                "You write short funny stock blurbs for a parody investing game. " +
                        "Keep it playful and non-defamatory: do NOT accuse companies of crimes/fraud, " +
                        "do NOT claim insider info, and do NOT give financial advice.";

        String input =
                "Symbol: " + symbol + "\n" +
                        "Company: " + companyName + "\n" +
                        "Write 1–2 funny sentences for a 'lose-money simulator' UI.";

        ResponseCreateParams params = ResponseCreateParams.builder()
                .model(ChatModel.GPT_4_1)
                .instructions(instructions)
                .input(input)
                .store(false)
                .build();

        try {
            Response response = openai.responses().create(params);


            String text = OpenAiResponse.extractOutputText(response);


            return (text == null || text.isBlank())
                    ? fallback()
                    : text;

        } catch (Exception e) {
            return fallback();
        }
    }

    private String fallback() {
        return "This stock is doing interpretive dance… directly into your portfolio.";
    }
}

