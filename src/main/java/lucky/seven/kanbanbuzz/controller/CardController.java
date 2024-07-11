package lucky.seven.kanbanbuzz.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lucky.seven.kanbanbuzz.dto.ResponseMessage;
import lucky.seven.kanbanbuzz.dto.SortWithCardDto;
import lucky.seven.kanbanbuzz.security.UserDetailsImpl;
import lucky.seven.kanbanbuzz.service.CardService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards/{boardId}/cards")
public class CardController {
	private final CardService cardService;

	@GetMapping
	public ResponseEntity<ResponseMessage<List<SortWithCardDto>>> getCards(
		@PathVariable Long boardId,
		@RequestParam(value = "sort", required = false, defaultValue = "column") String sort,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		
		List<SortWithCardDto> responseDto = cardService.findAllCards(boardId, sort, userDetails.getUser());
		
		ResponseMessage<List<SortWithCardDto>> responseMessage = ResponseMessage.<List<SortWithCardDto>>builder()
			.statusCode(HttpStatus.OK.value())
			.message("전체 카드 조회가 완료되었습니다.")
			.data(responseDto)
			.build();
		
		return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
	}
}
