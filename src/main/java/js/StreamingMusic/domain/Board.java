package js.StreamingMusic.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Getter @Setter
public class Board extends TimeEntity{

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String comment;
    private String traceId;

    private String title;
    private String artist;
    private String img;

    public void setMember(Member member) {
        this.member = member;
        member.getBoards().add(this);
    }
}
