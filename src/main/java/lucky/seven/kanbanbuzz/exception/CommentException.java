package lucky.seven.kanbanbuzz.exception;

public class CommentException extends CustomException{
    public CommentException(ErrorType errorType) {
        super(errorType);
    }
}
