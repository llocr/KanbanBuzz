package lucky.seven.kanbanbuzz.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lucky.seven.kanbanbuzz.entity.Board;

@Getter
@Builder
public class BoardResponseDto {

    private Long Id;

    @NotNull(message = "제목을 입력해주세요.")
    private String name;

    @NotNull(message = "소개글을 간단하게 작성하여주세요!")
    private String bio;
    
    private Long id;

    // Builder 패턴 사용
    public static BoardResponseDto from(Board board) {
        return BoardResponseDto.builder()
                .Id(board.getId())
                .name(board.getName())
                .bio(board.getBio())
                .id(board.getId())
                .build();
    }
}
