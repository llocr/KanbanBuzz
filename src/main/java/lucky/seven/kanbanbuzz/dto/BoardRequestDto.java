package lucky.seven.kanbanbuzz.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lucky.seven.kanbanbuzz.entity.Board;

@Getter
@NoArgsConstructor
public class BoardRequestDto {

    @NotNull(message = "제목을 입력해주세요.")
    private String name;

    @NotNull(message = "소개글을 간단하게 작성하여주세요!")
    private String bio;
    
    @Builder
    public BoardRequestDto(String name, String bio) {
        this.name = name;
        this.bio = bio;
    }
    
    public static BoardResponseDto from(Board board) {
        return BoardResponseDto.builder()
                .name(board.getName())
                .bio(board.getBio())
                .build();
    }
}
