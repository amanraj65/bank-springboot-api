package com.bank.account_service.service;

import com.bank.account_service.model.IdempotencyKey;
import com.bank.account_service.repository.IdempotencyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class IdempotencyService {

    private final IdempotencyRepository repository;
    private final ObjectMapper objectMapper;

    public <T> T process(String key, Supplier<T> action, Class<T> clazz) {

        Optional<IdempotencyKey> existing = repository.findByIdempotencyKey(key);

        if (existing.isPresent()) {
            try {
                return objectMapper.readValue(existing.get().getResponse(), clazz);
            } catch (Exception e) {
                throw new RuntimeException("Error reading stored response");
            }
        }

        T response = action.get();

        try {
            IdempotencyKey entity = IdempotencyKey.builder()
                    .idempotencyKey(key)
                    .response(objectMapper.writeValueAsString(response))
                    .build();

            repository.save(entity);
        } catch (Exception e) {
            throw new RuntimeException("Error saving idempotency key");
        }

        return response;
    }
}