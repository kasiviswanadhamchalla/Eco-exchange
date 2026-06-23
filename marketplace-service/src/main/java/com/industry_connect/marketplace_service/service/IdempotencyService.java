package com.industry_connect.marketplace_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.industry_connect.marketplace_service.entity.IdempotencyKey;
import com.industry_connect.marketplace_service.repository.IdempotencyKeyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class IdempotencyService {

    private final IdempotencyKeyRepository repository;
    private final ObjectMapper objectMapper;

    public IdempotencyService(IdempotencyKeyRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    public Optional<IdempotencyKey> get(String idempotencyKey) {
        if (idempotencyKey == null || idempotencyKey.trim().isEmpty()) {
            return Optional.empty();
        }
        return repository.findByIdempotencyKey(idempotencyKey);
    }

    @Transactional
    public void save(String idempotencyKey, String requestHash, Object responseObj) {
        if (idempotencyKey == null || idempotencyKey.trim().isEmpty()) {
            return;
        }
        try {
            String responseJson = objectMapper.writeValueAsString(responseObj);
            IdempotencyKey keyEntry = new IdempotencyKey();
            keyEntry.setIdempotencyKey(idempotencyKey);
            keyEntry.setRequestHash(requestHash);
            keyEntry.setResponseJson(responseJson);
            repository.save(keyEntry);
        } catch (JsonProcessingException e) {
            // Log and ignore or wrap
            throw new RuntimeException("Failed to serialize idempotency response", e);
        }
    }

    public <T> T parseResponse(IdempotencyKey key, Class<T> responseType) {
        try {
            return objectMapper.readValue(key.getResponseJson(), responseType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize idempotency response", e);
        }
    }
}
