package lucky.seven.kanbanbuzz.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {
	//jwt
	NOT_FOUND_TOKEN(HttpStatus.NOT_FOUND,"토큰을 찾을 수 없습니다"),
	NOT_FOUND_AUTHENTICATION_INFO(HttpStatus.NOT_FOUND,"인증정보를 찾을 수 없습니다."),
	INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED,"유효하지 않은 리프레시 토큰 입니다."),

	//user
	DUPLICATE_ACCOUNT_ID(HttpStatus.LOCKED, "이미 존재하는 아이디입니다."),
	INVALID_ACCOUNT_ID(HttpStatus.UNAUTHORIZED, "아이디가 일치하지 않습니다."),
	INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
	USER_ALREADY_EXISTS(HttpStatus.ALREADY_REPORTED,"이미 존재하는 이메일 입니다."),
	NOT_FOUND_USER(HttpStatus.NOT_FOUND, "존재하지 하지 않는 사용자입니다."),
	USER_NOT_IN_BOARD(HttpStatus.BAD_REQUEST, "속하지 않은 보드입니다"),
	
	//board,
	NOT_FOUND_BOARD(HttpStatus.NOT_FOUND, "보드가 존재하지 않습니다."),
	NOT_AUTHORIZED_BOARD(HttpStatus.UNAUTHORIZED, "권한이 없습니다."),
  
	//column
	NOT_FOUND_COLUMN(HttpStatus.NOT_FOUND, "컬럼이 존재하지 않습니다."),
	DUPLICATE_STATUS_NAME(HttpStatus.LOCKED, "이미 존재하는 컬럼명입니다."),
	INVALID_COLUMN(HttpStatus.NOT_FOUND, "해당 칼럼이 존재하지 않습니다."),

	//card
	INVALID_CARD_USER(HttpStatus.UNAUTHORIZED, "작성자가 일치하지 않습니다."),
	NOT_FOUND_CARD(HttpStatus.NOT_FOUND, "카드가 존재하지 않습니다."),
	INVALID_SORT(HttpStatus.BAD_REQUEST, "정렬 방식이 올바르지 않습니다.");
	
	//comment
	
	private final HttpStatus httpStatus;
	private final String message;
}
