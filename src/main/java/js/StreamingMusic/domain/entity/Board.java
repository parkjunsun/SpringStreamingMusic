package js.StreamingMusic.domain.entity;

import js.StreamingMusic.domain.LikeBoardStatus;
import js.StreamingMusic.domain.TimeEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter @Setter
public class Board extends TimeEntity {

    @Id @GeneratedValue
    @Column(name = "BOARD_ID")
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
    private String type;

    private int likeCount;



    @OneToMany(mappedBy = "board")
    private List<LikeBoard> likeBoards = new ArrayList<>();

    public void setMember(Member member) {
        this.member = member;
        member.getBoards().add(this);
    }

    public void addLikeCount(int cnt) {
        this.likeCount += cnt;
    }
    public void removeLikeCount(int cnt) {this.likeCount -= cnt;}


}
