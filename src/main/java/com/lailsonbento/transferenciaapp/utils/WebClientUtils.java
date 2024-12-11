package com.lailsonbento.transferenciaapp.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
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
                .onStatus(HttpStatusCode::is5xxServerError, _ -> {
                    log.error("Error when performing http request");
                    log.error("{} {}", httpMethod.name(), url);
                    body.ifPresent(b -> log.error("Body: {}", b));
                    return Mono.error(new RuntimeException("Error when performing http request"));
                })
                .bodyToMono(clazz)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(5))
                .filter(throwable -> throwable instanceof RuntimeException))
                .subscribeOn(Schedulers.single())
                .toFuture();
    }

    public static <R> CompletableFuture<R> doAsync(String url, HttpMethod httpMethod, Class<R> clazz) {
        return doAsync(url, httpMethod, Optional.empty(), clazz);
    }

}
