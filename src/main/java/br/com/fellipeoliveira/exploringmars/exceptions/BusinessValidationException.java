package br.com.fellipeoliveira.exploringmars.exceptions;

public class BusinessValidationException extends RuntimeException {

  public BusinessValidationException(String message) {
    super(message);
  }

}
