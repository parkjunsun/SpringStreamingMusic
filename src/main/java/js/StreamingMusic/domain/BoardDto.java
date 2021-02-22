package js.StreamingMusic.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class BoardDto {

    private Long id;
    private String writer;
    private String comment;
    private String traceId;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

}
