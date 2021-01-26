package js.StreamingMusic.domain;

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

    @OneToMany(mappedBy = "member")
    private List<Song> songs = new ArrayList<>();



}
