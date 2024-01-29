package org.dyu5thdorm.dyu5thdormdiscordbot.line;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Component
@PropertySource("classpath:line.properties")
@RequiredArgsConstructor
public class LineNotify {
    final RepairTokenSet repairTokenSet;

    @Value("${api}")
    String api;

    String[] headers(RepairTokenSet.RepairType type) {
        return new String[] {
                "Content-Type", "application/x-www-form-urlencoded; charset=utf-8",
                "Authorization", "Bearer " + repairTokenSet.getTokenMap().get(type)
        };
    }

    HttpRequest getRequest(String message, String imageUrl, RepairTokenSet.RepairType type) {
        String parameters = String.format(
                "message=%s", URLEncoder.encode(message, StandardCharsets.UTF_8)
        );

        if (imageUrl != null) {
            parameters += String.format(
                    "&imageThumbnail=%s&imageFullsize=%s",
                    imageUrl,
                    imageUrl
            );
        }

        return HttpRequest
                .newBuilder()
                .headers(headers(type))
                .uri(URI.create(api))
                .POST(HttpRequest.BodyPublishers.ofString(
                        parameters
                )).build();
    }

    HttpRequest getRequest(String message, RepairTokenSet.RepairType type) {
        return getRequest(message, null, type);
    }


    public HttpResponse<String> sendMessage(String message, RepairTokenSet.RepairType type) throws IOException, InterruptedException {
        return send(message, null, type);
    }

    public HttpResponse<String> sendMessageWithImage(String message, String imageUrl, RepairTokenSet.RepairType type) throws IOException, InterruptedException {
        return send(message, imageUrl, type);
    }

    HttpResponse<String> send(String message, String imageUrl, RepairTokenSet.RepairType type) throws IOException, InterruptedException {
        if (imageUrl == null) {
            return HttpClient.newHttpClient().send(
                    getRequest(message, type), HttpResponse.BodyHandlers.ofString()
            );
        } else {
            return HttpClient.newHttpClient().send(
                    getRequest(message, imageUrl, type), HttpResponse.BodyHandlers.ofString()
            );
        }
    }
}
