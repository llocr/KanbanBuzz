package lucky.seven.kanbanbuzz.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {
	//jwt
	
	//user
	DUPLICATE_ACCOUNT_ID(HttpStatus.LOCKED, "이미 아이디가 존재합니다."),
	INVALID_ACCOUNT_ID(HttpStatus.UNAUTHORIZED, "아이디가 일치하지 않습니다."),
	INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),

	//board
	NOT_FOUND_USER(HttpStatus.NOT_FOUND, "존재하지 하지 않는 사용자입니다."),
	NOT_FOUND_BOARD(HttpStatus.NOT_FOUND, "해당 게시글은 존재하지 않습니다."),
	NOT_AUTHORIZED_BOARD(HttpStatus.UNAUTHORIZED, "권한이 없습니다.");
	//column
	
	//card
	
	//comment
	
	private final HttpStatus httpStatus;
	private final String message;
}
