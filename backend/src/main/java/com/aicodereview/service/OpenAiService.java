package com.aicodereview.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class OpenAiService {

    @Autowired
    private WebClient openAiWebClient;

    public String reviewCode(String code) {
        String prompt = """
                You are a Senior Java Software Engineer.
                Review the following Java code and provide:
                1. Bugs found
                2. Security vulnerabilities
                3. Code smells
                4. Performance improvements
                5. Best coding practices
                6. Suggested refactoring
                7. Better variable and method names
                8. Code quality score out of 100
                9. Complexity analysis
                10. Summary of recommendations

                Return the response in structured JSON format.

                Code:
                """ + code;

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt)
                        ))
                )
        );

        String response = openAiWebClient.post()
                .uri("/models/gemini-flash-latest:generateContent")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            return root.path("candidates").get(0)
                    .path("content").path("parts").get(0)
                    .path("text").asText();
        } catch (Exception e) {
            return "Error parsing AI response: " + e.getMessage();
        }
    }
}