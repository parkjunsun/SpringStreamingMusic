package js.StreamingMusic.domain.dto;

import js.StreamingMusic.domain.LikeBoardStatus;
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

    private String title;
    private String artist;
    private String img;
    private String type;

    private int LikeCount;


}
