package com.desafioitau.api.transferencia.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Component
public class RetryUtils {

    @Getter
    @Value("${webclient.maxAttempts}")
    private int maxAttempts;

    @Value("${webclient.timeoutInSeconds}")
    private int timeoutInSeconds;

    @Value("${webclient.minBackoffInSeconds}")
    private int minBackoffInSeconds;

    public Duration getTimeoutDuration() {
        return Duration.ofSeconds(timeoutInSeconds);
    }

    public Duration getMinBackoffDuration() {
        return Duration.ofSeconds(minBackoffInSeconds);
    }

    private final List<Integer> retryableStatusCodes;

    public RetryUtils(@Value("${webclient.retryableStatusCodes}") String[] retryableStatusCodes) {
        this.retryableStatusCodes = Arrays.asList(Arrays.stream(retryableStatusCodes).map(Integer::parseInt).toArray(Integer[]::new));
    }

    public boolean isRetryableError(Throwable throwable) {
        if (throwable instanceof WebClientResponseException webClientException) {
            int statusCode = webClientException.getStatusCode().value();
            return retryableStatusCodes.contains(statusCode);
        }
        return false;
    }
}
