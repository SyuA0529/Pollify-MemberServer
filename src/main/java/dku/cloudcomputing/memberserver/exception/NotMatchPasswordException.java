package dku.cloudcomputing.memberserver.exception;

public class NotMatchPasswordException extends ClientOccurException {
    public NotMatchPasswordException() {
        this("비밀번호가 일치하지 않습니다");
    }

    public NotMatchPasswordException(String message) {
        super(message);
    }
}
