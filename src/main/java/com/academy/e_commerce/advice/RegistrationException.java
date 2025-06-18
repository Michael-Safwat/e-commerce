package com.academy.e_commerce.advice;

public class RegistrationException extends RuntimeException {
  public RegistrationException(String message) {
    super(message);
  }
}