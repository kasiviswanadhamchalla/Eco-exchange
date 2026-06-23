package com.industry_connect.order_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.industry_connect.order_service.entity.IdempotencyKey;
import com.industry_connect.order_service.repository.IdempotencyKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.Optional;

@Service
public class IdempotencyService {

    @Autowired
    private IdempotencyKeyRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    public Optional<IdempotencyKey> get(String key) {
        return repository.findByIdempotencyKey(key);
    }

    public String calculateHash(Object requestBody) {
        try {
            if (requestBody == null) return "";
            String json = objectMapper.writeValueAsString(requestBody);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(json.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            return "";
        }
    }

    public <T> ResponseEntity<T> toResponse(IdempotencyKey key, Class<T> responseType) {
        try {
            T body = objectMapper.readValue(key.getResponse(), responseType);
            return ResponseEntity.status(HttpStatus.valueOf(key.getStatusCode())).body(body);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public void save(String key, Object requestBody, Object responseBody, HttpStatus status) {
        try {
            String hash = calculateHash(requestBody);
            String responseJson = objectMapper.writeValueAsString(responseBody);
            IdempotencyKey entity = new IdempotencyKey(key, hash, responseJson, status.value());
            repository.save(entity);
        } catch (Exception e) {
            // Log error
        }
    }
}
