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

    public String funnyStockDescription(String symbol, String toneTag) {
        String instructions =
                "You write short funny stock blurbs for a parody investing game.\n" +
                        "Keep it playful and non-defamatory:\n" +
                        "- Do NOT accuse companies of crimes or fraud\n" +
                        "- Do NOT claim insider knowledge\n" +
                        "- Do NOT give financial advice\n\n" +
                        toneInstructions(toneTag);

        String input =
                "Symbol: " + symbol + "\n" +
                        "Tone: " + toneTag + "\n" +
                        "Context: This is a lose-money simulator.\n" +
                        "Write 1–2 short, funny sentences explaining why this stock hurts emotionally.";

        try {
            ResponseCreateParams params = ResponseCreateParams.builder()
                    .model(ChatModel.GPT_4_1)
                    .instructions(instructions)
                    .input(input)
                    .store(false)
                    .build();

            Response response = openai.responses().create(params);

            String text = OpenAiResponse.extractOutputText(response);

            return (text == null || text.isBlank())
                    ? fallback()
                    : text.trim();

        } catch (Exception e) {
            return fallback();
        }
    }

    private String fallback() {
        return "This stock is doing interpretive dance… directly into your portfolio.";
    }

    private String toneInstructions(String toneTag) {
        if (toneTag == null) return "Use playful but neutral financial humor.";

        return switch (toneTag) {
            case "meme_crash" ->
                    "Use ironic, meme-aware humor about hype, bag-holders, and retail investor regret.";

            case "slow_bleed" ->
                    "Use dry, resigned humor about long-term disappointment and underperformance.";

            case "biotech_grinder" ->
                    "Use dark humor about endless clinical trials, dilution, and false hope.";

            case "hype_ev_burn" ->
                    "Use sarcastic humor about futuristic promises failing to arrive on schedule.";

            default ->
                    "Use playful but neutral financial humor.";
        };
    }
}