package ru.netology.exeption;

import javax.naming.AuthenticationException;

public class StorageException extends AuthenticationException {
    public StorageException(String message) {
        super(message);
    }
}