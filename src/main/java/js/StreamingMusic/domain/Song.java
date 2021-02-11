package js.StreamingMusic.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Song {

    @Id @GeneratedValue
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
//    @JsonBackReference
    private Member member;

    public void setMember(Member member) {
        this.member = member;
        member.getSongs().add(this);
    }
}
