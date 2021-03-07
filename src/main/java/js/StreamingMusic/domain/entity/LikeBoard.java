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


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    @Enumerated(EnumType.STRING)
    private LikeBoardStatus status;


    public void setMember(Member member) {
        this.member = member;
        member.getLikeBoards().add(this);
    }

    public void setBoard(Board board) {
        this.board = board;
        board.getLikeBoards().add(this);
    }



}
