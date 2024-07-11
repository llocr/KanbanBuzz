package lucky.seven.kanbanbuzz.exception;

public class BoardException extends CustomException{
    public BoardException(ErrorType errorType){
        super(errorType);
    }
}
