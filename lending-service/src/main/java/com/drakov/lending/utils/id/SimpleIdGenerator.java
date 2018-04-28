package com.drakov.lending.utils.id;

import org.springframework.stereotype.Component;

@Component
public class SimpleIdGenerator implements IdGenerator {
    private Long id = 1L;

    @Override
    public Long generateId() {
        return id++;
    }
}
