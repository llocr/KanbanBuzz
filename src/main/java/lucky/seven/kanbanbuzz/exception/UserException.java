package lucky.seven.kanbanbuzz.exception;

import lombok.Getter;

@Getter
public class UserException extends CustomException{
	public UserException(ErrorType errorType) {
		super(errorType);
	}
}
