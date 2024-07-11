package lucky.seven.kanbanbuzz.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lucky.seven.kanbanbuzz.dto.CardDetailResponseDto;
import lucky.seven.kanbanbuzz.dto.CardRequestDto;
import lucky.seven.kanbanbuzz.dto.CardSimpleResponseDto;
import lucky.seven.kanbanbuzz.dto.SortWithCardDto;
import lucky.seven.kanbanbuzz.entity.Board;
import lucky.seven.kanbanbuzz.entity.Card;
import lucky.seven.kanbanbuzz.entity.Column;
import lucky.seven.kanbanbuzz.entity.User;
import lucky.seven.kanbanbuzz.entity.UserBoard;
import lucky.seven.kanbanbuzz.exception.CardException;
import lucky.seven.kanbanbuzz.exception.ColumnException;
import lucky.seven.kanbanbuzz.exception.ErrorType;
import lucky.seven.kanbanbuzz.exception.UserException;
import lucky.seven.kanbanbuzz.repository.CardRepository;
import lucky.seven.kanbanbuzz.repository.ColumnRepository;
import lucky.seven.kanbanbuzz.repository.UserBoardRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CardService {
	private final CardRepository cardRepository;
	private final UserBoardRepository userBoardRepository;
	private final ColumnRepository columnRepository;

	//모든 카드 조회
	public List<SortWithCardDto> findAllCards(Long boardId, String sort, User user) {
		//user가 board에 속한 게 맞는지 확인
		checkUserBoardValidation(boardId, user);

		//카드 목록 조회
		List<Card> cardList = getCards(boardId);

		switch (sort) {
			case "column":
				return groupCardsByColumn(boardId, cardList);
			case "user":
				return groupCardsByUser(boardId, cardList);
			case "all":
				List<CardSimpleResponseDto> all = cardList.stream().map(CardSimpleResponseDto::new).collect(Collectors.toList());
				return List.of(new SortWithCardDto(0L, sort, all));
			default:
				throw new CardException(ErrorType.INVALID_SORT);
		}
	}

	//유저별로 카드 그룹화
	private List<SortWithCardDto> groupCardsByUser(Long boardId, List<Card> cardList) {
		Set<UserBoard> userBoards = userBoardRepository.findByBoardId(boardId);

		// 카드 목록을 유저별로 그룹화
		Map<String, List<CardSimpleResponseDto>> cardsByUser = cardList.stream()
			.map(CardSimpleResponseDto::new)
			.collect(Collectors.groupingBy(CardSimpleResponseDto::getUser));

		// 유저별로 그룹화된 카드 목록을 ColumnWithCardsDto 객체로 변환하여 반환
		return userBoards.stream()
			.map(userBoard -> new SortWithCardDto(
				userBoard.getUser().getId(),
				userBoard.getUser().getName(),
				cardsByUser.getOrDefault(userBoard.getUser().getName(), List.of())
			))
			.collect(Collectors.toList());
	}

	//컬럼별로 카드 그룹화
	private List<SortWithCardDto> groupCardsByColumn(Long boardId, List<Card> cardList) {
		Set<Column> columns = columnRepository.findByBoardId(boardId);

		// 카드 목록을 컬럼별로 그룹화
		Map<Long, List<CardSimpleResponseDto>> cardsByColumn = cardList.stream()
			.map(CardSimpleResponseDto::new)
			.collect(Collectors.groupingBy(CardSimpleResponseDto::getColumnId));

		// 컬럼별로 그룹화된 카드 목록을 ColumnWithCardsDto 객체로 변환하여 반환
		return columns.stream()
			.map(column -> new SortWithCardDto(
				column.getId(),
				column.getStatusName(),
				cardsByColumn.getOrDefault(column.getId(), List.of())
			))
			.collect(Collectors.toList());
	}
	
	//카드 단일 조회
	public CardDetailResponseDto findSingleCard(Long boardId, Long cardId, User user) {
		checkUserBoardValidation(boardId, user);
		
		Card card = getSingleCard(boardId, cardId);
		
		return new CardDetailResponseDto(card);
	}
	
	//카드 저장
	@Transactional
	public Long saveCard(Long boardId, CardRequestDto requestDto, User user) {
		Board board = checkUserBoardValidation(boardId, user);
		Column column = getColumn(boardId, requestDto.getColumnId());
		
		Card.CardBuilder cardBuilder = Card.builder()
			.title(requestDto.getTitle())
			.board(board)
			.column(column)
			.user(user);
		
		if (requestDto.getContents() != null) {
			cardBuilder.contents(requestDto.getContents());
		}
		
		if (requestDto.getEndDate() != null) {
			cardBuilder.endDate(LocalDate.parse(requestDto.getEndDate()));
		}
		
		Card buildCard = cardBuilder.build();
		Card saveCard = cardRepository.save(buildCard);
		
		return saveCard.getId();
	}
	
	
	//** UTIL **//

	//유저가 보드에 속한 것인지 확인
	private Board checkUserBoardValidation(Long boardId, User user) {
		Optional<UserBoard> userBoard = userBoardRepository.findByBoardIdAndUserId(boardId, user.getId());
		if (userBoard.isEmpty()) {
			throw new UserException(ErrorType.NOT_FOUND_BOARD);
		}

		return userBoard.get().getBoard();
	}
	
	//카드 존재하는지 확인
	private List<Card> getCards(Long boardId) {
		List<Card> cardList = cardRepository.findByBoardIdWithColumnAndUser(boardId);
		if (cardList.isEmpty()) {
			throw new CardException(ErrorType.NOT_FOUND_CARD);
		}
		
		return cardList;
	}
	
	//cardId, boardId가 일치하는 Card
	private Card getSingleCard(Long boardId, Long cardId) {
		Optional<Card> card = cardRepository.findByIdWithUserBoardAndColumn(cardId, boardId);
		if (card.isEmpty()) {
			throw new CardException(ErrorType.NOT_FOUND_CARD);
		}
		
		return card.get();
	}
	
	//컬럼 존재하는지 확인
	private Column getColumn(Long boardId, Long columnId) {
		Optional<Column> column = columnRepository.findColumnByIdAndBoard(boardId, columnId);
		if (column.isEmpty()) {
			throw new ColumnException(ErrorType.NOT_FOUND_COLUMN);
		}
		
		return column.get();
	}
}
