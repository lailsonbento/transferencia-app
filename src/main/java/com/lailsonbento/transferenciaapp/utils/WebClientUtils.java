package com.lailsonbento.transferenciaapp.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class WebClientUtils {

    public static <R> CompletableFuture<R> doAsync(String url, HttpMethod httpMethod, Optional<String> body, Class<R> clazz) {
        return WebClient.create(url)
                .method(httpMethod)
                .bodyValue(body)
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, response -> {
                    logResponse(url, httpMethod, body, response);
                    return Mono.error(new RuntimeException("Error when performing http request"));
                })
                .bodyToMono(clazz)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(5))
                .filter(throwable -> throwable instanceof RuntimeException))
                .subscribeOn(Schedulers.single())
                .toFuture();
    }

    private static void logResponse(String url, HttpMethod httpMethod, Optional<String> body, ClientResponse response) {
        log.error("{} {}", httpMethod.name(), url);
        body.ifPresent(b -> log.error("Body: {}", b));
        log.error("Status: {}", response.statusCode());
        response.bodyToMono(String.class)
                .publishOn(Schedulers.boundedElastic())
                .subscribe(responseBody -> log.error("Response body: {}", responseBody));
    }

    public static <R> CompletableFuture<R> doAsync(String url, HttpMethod httpMethod, Class<R> clazz) {
        return doAsync(url, httpMethod, Optional.empty(), clazz);
    }

}
