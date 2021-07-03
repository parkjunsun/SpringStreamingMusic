package js.StreamingMusic.domain.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.criteria.CriteriaBuilder;

@Getter @Setter
public class MemberDto {

    private Long id;
    private String username;
    private String realname;
    private String email;
    private Integer age;
    private String role;
    private int songQuantity;
    private int boardQuantity;
    private String joinDate;

    public MemberDto(Long id, String username, String realname, String email, Integer age, String role, int songQuantity, int boardQuantity, String joinDate) {
        this.id = id;
        this.username = username;
        this.realname = realname;
        this.email = email;
        this.age = age;
        this.role = role;
        this.songQuantity = songQuantity;
        this.boardQuantity = boardQuantity;
        this.joinDate = joinDate;
    }
}
