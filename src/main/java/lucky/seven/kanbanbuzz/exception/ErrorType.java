package lucky.seven.kanbanbuzz.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorType {
	//jwt
	NOT_FOUND_TOKEN(HttpStatus.NOT_FOUND,"토큰을 찾을 수 없습니다"),
	NOT_FOUND_AUTHENTICATION_INFO(HttpStatus.NOT_FOUND,"인증정보를 찾을 수 없습니다."),
	INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED,"유효하지 않은 리프레시 토큰 입니다."),
	//user
	DUPLICATE_ACCOUNT_ID(HttpStatus.LOCKED, "이미 아이디가 존재합니다."),
	INVALID_ACCOUNT_ID(HttpStatus.UNAUTHORIZED, "아이디가 일치하지 않습니다."),
	INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
	USER_ALREADY_EXISTS(HttpStatus.ALREADY_REPORTED,"이미 존재하는 이메일 입니다.")
	;
	INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),

	//board
	
	//column
	DUPLICATE_STATUS_NAME(HttpStatus.LOCKED, "이미 존재하는 컬럼명입니다."),
	INVALID_COLUMN(HttpStatus.NOT_FOUND, "해당 칼럼이 존재하지 않습니다.");

	//card
	
	//comment
	
	private final HttpStatus httpStatus;
	private final String message;
}
