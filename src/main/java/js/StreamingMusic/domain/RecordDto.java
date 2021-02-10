package js.StreamingMusic.domain;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RecordDto {

    private Long id;

    private String title;
    private String artist;

    private int count;
}
