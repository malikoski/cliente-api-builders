package br.com.builders.cliente.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CacheService {

    private final CacheManager cacheManager;

    @Scheduled(fixedRate = 120000)
    public void evictAllcachesAtIntervals() {
        log.info("Limpando cache...");
        evictAllCaches();
        log.info("Limpeza de cache finalizado.");
    }

    private void evictAllCaches() {
        cacheManager.getCacheNames().stream()
                .forEach(cacheName -> {
                    log.info("Cache: {}", cacheName);
                    cacheManager.getCache(cacheName).clear();
                });
    }

}
