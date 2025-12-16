package com.whyboutrasseyt.miniproject.domain.exception;

public class NotFoundException extends DomainException {
    public NotFoundException(String resource, Object id) {
        super(resource + " with id %s not found".formatted(id));
    }
}
