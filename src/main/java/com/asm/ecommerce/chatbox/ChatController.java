package com.asm.ecommerce.chatbox;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    // Khai báo hằng số cho API và cấu hình HTTP
    private static final String GEMINI_API_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-preview-09-2025:generateContent";
    private static final Gson GSON = new Gson();
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    // Sử dụng OkHttpClient với timeout hợp lý
    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    // Lấy API Key từ application.properties/yml
    @Value("${gemini.api.key}")
    private String geminiApiKey;

    /**
     * Xử lý yêu cầu chat, gửi tin nhắn đến Gemini API và nhận phản hồi.
     * @param request Chứa tin nhắn người dùng trong trường "message".
     * @return ResponseEntity chứa câu trả lời của AI hoặc thông báo lỗi.
     */
    @PostMapping
    public ResponseEntity<?> getAiResponse(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");

        if (userMessage == null || userMessage.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Tin nhắn không được để trống.");
        }

        // 1. Xây dựng Request Body JSON
        JsonObject requestBodyJson = buildRequestBody(userMessage);
        String jsonPayload = requestBodyJson.toString();

        okhttp3.RequestBody body = okhttp3.RequestBody.create(JSON_MEDIA_TYPE, jsonPayload);

        // 2. Xây dựng Request và truyền API Key qua Header
        Request geminiRequest = new Request.Builder()
                .url(GEMINI_API_URL)
                .header("X-Goog-Api-Key", geminiApiKey) // API Key qua Header
                .post(body)
                .build();

        try (Response response = CLIENT.newCall(geminiRequest).execute()) {
            String responseBodyString = Objects.requireNonNull(response.body()).string();

            if (!response.isSuccessful()) {
                // Xử lý lỗi HTTP từ API (ví dụ: 400, 403, 500)
                return ResponseEntity.status(response.code())
                        .body("Gemini API error: " + responseBodyString);
            }

            // 3. Phân tích JSON Response để trích xuất văn bản trả lời
            String aiResponseText = extractTextFromGeminiResponse(responseBodyString);

            if (aiResponseText != null) {
                return ResponseEntity.ok(aiResponseText);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Lỗi xử lý: Không tìm thấy trường 'text' trong phản hồi của Gemini.");
            }

        } catch (IOException e) {
            // Xử lý lỗi kết nối mạng, timeout, v.v.
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Lỗi hệ thống: Không thể kết nối đến Gemini API.");
        } catch (Exception e) {
            // Xử lý các lỗi khác
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống nội bộ.");
        }
    }

    /**
     * Xây dựng JSON payload cho Gemini API.
     * @param userMessage Tin nhắn của người dùng.
     * @return JsonObject đại diện cho request body.
     */
    private JsonObject buildRequestBody(String userMessage) {
        // Cấu trúc: { "contents": [ { "parts": [ { "text": "..." } ] } ] }
        JsonObject part = new JsonObject();
        part.addProperty("text", userMessage);

        JsonArray partsArray = new JsonArray();
        partsArray.add(part);

        JsonObject content = new JsonObject();
        content.add("parts", partsArray);

        JsonArray contentsArray = new JsonArray();
        contentsArray.add(content);

        JsonObject requestBodyJson = new JsonObject();
        requestBodyJson.add("contents", contentsArray);

        // Tùy chọn: Thêm cấu hình như nhiệt độ, system instruction, tools (Google Search)
        // Ví dụ: Kích hoạt Google Search Grounding
        // JsonObject googleSearchTool = new JsonObject();
        // googleSearchTool.add("googleSearch", new JsonObject());
        // JsonArray toolsArray = new JsonArray();
        // toolsArray.add(googleSearchTool);
        // requestBodyJson.add("tools", toolsArray);

        return requestBodyJson;
    }

    /**
     * Phân tích JSON response từ Gemini API để trích xuất văn bản trả lời.
     * @param responseBodyString Chuỗi JSON nhận được từ API.
     * @return Văn bản trả lời của AI hoặc null nếu không tìm thấy.
     */
    private String extractTextFromGeminiResponse(String responseBodyString) {
        try {
            JsonObject jsonResponse = JsonParser.parseString(responseBodyString).getAsJsonObject();

            JsonArray candidates = jsonResponse.getAsJsonArray("candidates");
            if (candidates == null || candidates.size() == 0) {
                // Trường hợp API không trả về candidates (ví dụ: bị chặn, hoặc lỗi khác)
                System.err.println("Cảnh báo: Gemini response không có candidates.");
                return null;
            }

            JsonObject candidate = candidates.get(0).getAsJsonObject();
            JsonObject content = candidate.getAsJsonObject("content");
            JsonArray parts = content.getAsJsonArray("parts");

            if (parts.size() > 0) {
                return parts.get(0).getAsJsonObject().get("text").getAsString();
            }

            return null;
        } catch (Exception e) {
            System.err.println("!!! Lỗi khi phân tích cấu trúc JSON response từ Gemini: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}
