package lucky.seven.kanbanbuzz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lucky.seven.kanbanbuzz.entity.Column;

@Getter
@Builder
@AllArgsConstructor
public class ColumnResponseDto {
    private String statusName;

    public static ColumnResponseDto of(Column column) {
        return ColumnResponseDto.builder()
                .statusName(column.getStatusName())
                .build();
    }
}
