package com.azatdev.dailytasks.data.repositories.data.user;

import java.util.UUID;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = "username") })
public class UserData {

    private @Id UUID id;
    private @Nonnull String username;
    private @Nonnull String password;

    public UserData() {
    }

    public UserData(
        UUID id,
        @Nonnull String username,
        @Nonnull String password
    ) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public UUID id() {
        return id;
    }

    public String username() {
        return username;
    }

    public String password() {
        return password;
    }
}
