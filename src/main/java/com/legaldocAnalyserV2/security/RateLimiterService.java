package com.legaldocAnalyserV2.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimiterService {

    private final StringRedisTemplate redisTemplate;

    @Value("${rate.limit.daily.max}")
    private int dailyMax;

    @Value("${rate.limit.daily.window.seconds}")
    private long dailyWindow; // 86400

    @Value("${rate.limit.minute.max}")
    private int minuteMax; // 5

    @Value("${rate.limit.minute.window.seconds}")
    private long minuteWindow; // 60

    public RateLimiterService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // EXPENSIVE — new document
    public boolean allowDaily(String ip) {
        return increment("rate:daily:" + ip, dailyMax, dailyWindow);
    }

    // CHEAP — duplicate document
    public boolean allowPerMinute(String ip) {
        return increment("rate:minute:" + ip, minuteMax, minuteWindow);
    }

    private boolean increment(String key, int max, long windowSeconds) {
        Long count = redisTemplate.opsForValue().increment(key);
        if (count != null && count == 1) {
            redisTemplate.expire(key, Duration.ofSeconds(windowSeconds));
        }
        return count != null && count <= max;
    }
}

