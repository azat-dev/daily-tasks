package com.azatdev.dailytasks.domain.models;

import java.util.UUID;

public record AppUser(
    UUID id,
    String username,
    String password
) {
}
