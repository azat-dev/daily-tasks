package com.azatdev.dailytasks.data.dao.data.user;

import java.util.UUID;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor
@Getter
@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = "username") })
public class UserData {

    private @Id UUID id;
    private @Nonnull String username;
    private @Nonnull String password;

    public UserData(
        UUID id,
        @Nonnull String username,
        @Nonnull String password
    ) {
        this.id = id;
        this.username = username;
        this.password = password;
    }
}
