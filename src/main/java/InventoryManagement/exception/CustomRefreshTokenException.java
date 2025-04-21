package InventoryManagement.exception;


public class CustomRefreshTokenException extends RuntimeException {
    public CustomRefreshTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
