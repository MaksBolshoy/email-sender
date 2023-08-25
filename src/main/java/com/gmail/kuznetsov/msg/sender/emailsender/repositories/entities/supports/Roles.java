package com.gmail.kuznetsov.msg.sender.emailsender.repositories.entities.supports;

/**
 * Имена ролей
 */
public enum Roles {
    ADMIN("ROLE_ADMIN"),  USER("ROLE_USER");

    public final String name;

    Roles(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
