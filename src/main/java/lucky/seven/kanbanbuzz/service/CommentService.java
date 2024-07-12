package lucky.seven.kanbanbuzz.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lucky.seven.kanbanbuzz.dto.CommentRequestDto;
import lucky.seven.kanbanbuzz.dto.CommentResponseDto;
import lucky.seven.kanbanbuzz.entity.Card;
import lucky.seven.kanbanbuzz.entity.Comment;
import lucky.seven.kanbanbuzz.entity.User;
import lucky.seven.kanbanbuzz.exception.CardException;
import lucky.seven.kanbanbuzz.exception.ErrorType;
import lucky.seven.kanbanbuzz.repository.CardRepository;
import lucky.seven.kanbanbuzz.repository.CommentRepositroy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepositroy commentRepositroy;
    private final CardRepository cardRepository;
    @Transactional
    public void createComment(User user, Long cardId, CommentRequestDto requestDto){
        Card card = cardRepository.findById(cardId).orElseThrow(()-> new CardException(ErrorType.NOT_FOUND_CARD));
        Comment comment = Comment.builder()
                .contents(requestDto.getContents())
                .card(card)
                .user(user)
                .build();
        commentRepositroy.save(comment);
    }

//    @Transactional
//    public List<CommentResponseDto> getComments(){
//
//    }
}
