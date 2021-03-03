package js.StreamingMusic.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberDto {

    private Long id;
    private String username;
    private String email;
    private String age;
    private String role;
    private int songQuantity;
    private int boardQuantity;
    private String joinDate;

    public MemberDto(Long id, String username, String email, String age, String role, int songQuantity, int boardQuantity, String joinDate) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.age = age;
        this.role = role;
        this.songQuantity = songQuantity;
        this.boardQuantity = boardQuantity;
        this.joinDate = joinDate;
    }
}
