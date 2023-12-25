package com.cstapin.support.domain;

import java.time.LocalDateTime;

public abstract class AbstractDomain {

    protected final Long id;

    protected final LocalDateTime createdAt;

    protected final LocalDateTime updatedAt;

    protected AbstractDomain(Long id, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }
}
