package js.StreamingMusic.domain.dto;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RecordDto {

    private String artist;
    private Long count;


    public RecordDto(String artist, Long count) {
        this.artist = artist;
        this.count = count;
    }
}
