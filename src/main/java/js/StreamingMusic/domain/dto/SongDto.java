package js.StreamingMusic.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SongDto {
    private Long id;
    private String title;
    private String artist;
    private String videoId;
    private String videoId2;
    private String videoId3;
    private String img;
    private String genre;
    private String duration;
    private String songid;

    public SongDto(Long id, String title, String artist, String videoId, String videoId2, String videoId3, String img, String genre, String duration, String songid) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.videoId = videoId;
        this.videoId2 = videoId2;
        this.videoId3 = videoId3;
        this.img = img;
        this.genre = genre;
        this.duration = duration;
        this.songid = songid;
    }


}
