package js.StreamingMusic.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @OneToMany(mappedBy = "member")
//    @JsonManagedReference
    private List<Song> songs = new ArrayList<>();

}
