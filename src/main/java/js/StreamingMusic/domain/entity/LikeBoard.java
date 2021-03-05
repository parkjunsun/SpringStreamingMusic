package js.StreamingMusic.domain.entity;

import js.StreamingMusic.domain.LikeBoardStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class LikeBoard {

    @Id @GeneratedValue
    @Column(name = "LIKE_BOARD_ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    private LikeBoardStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    private Long board_id;


    public void setMember(Member member) {
        this.member = member;
        member.getLikeBoards().add(this);
    }



}
