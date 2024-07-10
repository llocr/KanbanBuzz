package lucky.seven.kanbanbuzz.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorType {
	//jwt
	NOT_FOUND_TOKEN(HttpStatus.NOT_FOUND,"토큰을 찾을 수 없습니다"),
	//user
	DUPLICATE_ACCOUNT_ID(HttpStatus.LOCKED, "이미 아이디가 존재합니다."),
	INVALID_ACCOUNT_ID(HttpStatus.UNAUTHORIZED, "아이디가 일치하지 않습니다."),
	INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
	
	//board
	
	//column
	
	//card
	
	//comment
	
	private final HttpStatus httpStatus;
	private final String message;
}
