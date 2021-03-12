package js.StreamingMusic.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;
    private String username;
    private String password;
    private String email;
    private String age;
    private String role;
    private int songQuantity;
    private int boardQuantity;
    private String joinDate;

    @OneToMany(mappedBy = "member")
    private List<Song> songs = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Record> records = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<LikeBoard> likeBoards = new ArrayList<>();

    public void addSong(int quantity) {
        this.songQuantity += quantity;
    }

    public void removeSong(int quantity) {
        this.songQuantity -= quantity;
    }

    public void addBoard(int quantity) {
        this.boardQuantity += quantity;
    }

    public void removeBoard(int quantity) {
        this.boardQuantity -= quantity;
    }

}
