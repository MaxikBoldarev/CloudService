package ru.netology.exeption;

public class RequestErrors extends RuntimeException {
    public RequestErrors(String message){super(message);}
}
