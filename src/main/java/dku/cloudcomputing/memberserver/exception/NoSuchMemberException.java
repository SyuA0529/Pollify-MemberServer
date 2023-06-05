package dku.cloudcomputing.memberserver.exception;

public class NoSuchMemberException extends ClientOccurException {
    public NoSuchMemberException() {
        this("이메일과 일치하는 회원이 없습니다");
    }

    public NoSuchMemberException(String message) {
        super(message);
    }
}
