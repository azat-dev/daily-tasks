package com.azatdev.dailytasks.domain.models;

import java.util.UUID;

public record User(
    UUID id,
    String username,
    String password
) {
}
