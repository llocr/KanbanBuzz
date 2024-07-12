package lucky.seven.kanbanbuzz.exception;

public class CardException extends CustomException{
	public CardException(ErrorType errorType) {
		super(errorType);
	}
}
