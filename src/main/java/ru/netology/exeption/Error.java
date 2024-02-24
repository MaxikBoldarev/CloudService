package ru.netology.exeption;

public enum Error {
    UNAUTHORIZED_REQUEST("Unauthorized request "),

    COULD_NOT_CREATE_USER_DIRECTORIES("Could not create user directories ");

    private final String description;

    Error(String description) {
        this.description = description;
    }

    public String value() {
        return description;
    }
}