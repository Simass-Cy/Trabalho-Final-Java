package Exceptions;

public class RepositoryException extends Exception{

    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepositoryException(String message) {
        super(message);
    }

}
