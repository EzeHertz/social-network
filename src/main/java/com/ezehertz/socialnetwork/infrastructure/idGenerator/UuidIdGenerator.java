package com.ezehertz.socialnetwork.infrastructure.idGenerator;

import com.ezehertz.socialnetwork.domain.common.id.IdGenerator;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidIdGenerator implements IdGenerator {
    @Override
    public String newId() {
        return UUID.randomUUID().toString();
    }
}
