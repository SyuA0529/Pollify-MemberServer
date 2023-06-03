package dku.cloudcomputing.memberserver.exception;

public class ClientOccurException extends RuntimeException{
    public ClientOccurException() {
    }

    public ClientOccurException(String message) {
        super(message);
    }
}
