package lucky.seven.kanbanbuzz.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lucky.seven.kanbanbuzz.dto.ColumnRequestDto;
import lucky.seven.kanbanbuzz.dto.ColumnResponseDto;
import lucky.seven.kanbanbuzz.dto.ResponseMessage;
import lucky.seven.kanbanbuzz.security.UserDetailsImpl;
import lucky.seven.kanbanbuzz.service.ColumnService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class ColumnController {

    private final ColumnService columnService;

    /**
     * 컬럼 생성
     * @param boardId
     * @param requestDto
     * @return
     */
    @PostMapping("/{boardId}/columns")
    public ResponseEntity<ResponseMessage<ColumnResponseDto>> columnAdd(
            @PathVariable Long boardId, @Valid @RequestBody ColumnRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        ColumnResponseDto responseDto = columnService.addColumn(boardId, requestDto, userDetails.getUser());

        ResponseMessage<ColumnResponseDto> responseMessage = ResponseMessage
                .<ColumnResponseDto>builder().statusCode(HttpStatus.CREATED.value())
                .message("컬럼이 추가되었습니다.").data(responseDto).build();

        return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
    }

    /**
     * 컬럼 삭제
     * @param boardId
     * @param columnId
     * @return
     */
    @DeleteMapping("/{boardId}/columns/{columnId}")
    public ResponseEntity<ResponseMessage<Void>> columnDelete(
            @PathVariable Long boardId, @PathVariable Long columnId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        columnService.deleteColumn(boardId, columnId, userDetails.getUser());

        ResponseMessage<Void> responseMessage = ResponseMessage
                .<Void>builder().statusCode(HttpStatus.OK.value())
                .message("컬럼이 삭제되었습니다.").data(null).build();

        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    /**
     * 컬럼 정렬
     * @param boardId
     * @param id
     * @param userDetails
     * @return
     */
    @PostMapping("/{boardId}/columns/reorder")
    public ResponseEntity<ResponseMessage<Void>> columnsReorder(
            @PathVariable Long boardId, @RequestParam("id[]") Long[] id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        columnService.reorderColumns(boardId, id, userDetails.getUser());

        ResponseMessage<Void> responseMessage = ResponseMessage
                .<Void>builder().statusCode(HttpStatus.OK.value())
                .message("컬럼이 정렬되었습니다.").data(null).build();

        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }
}
