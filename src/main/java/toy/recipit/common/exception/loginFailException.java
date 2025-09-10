package toy.recipit.common.exception;

public class loginFailException extends RuntimeException {
    public loginFailException(String s) {
        super(s);
    }
}