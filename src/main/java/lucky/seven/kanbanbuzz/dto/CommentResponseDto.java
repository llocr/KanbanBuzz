package lucky.seven.kanbanbuzz.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;

import java.time.LocalDateTime;

public class CommentResponseDto {
    @JsonProperty
    private String contents;
    @JsonProperty
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @Builder
    public CommentResponseDto(String contents,LocalDateTime createdAt) {
        this.contents = contents;
        this.createdAt = createdAt;
    }
}
