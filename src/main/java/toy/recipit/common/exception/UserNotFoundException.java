package toy.recipit.common.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String s) { super(s); }
}
