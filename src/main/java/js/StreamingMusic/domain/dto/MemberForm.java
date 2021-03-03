package js.StreamingMusic.domain.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;

@Getter @Setter
public class MemberForm {

    private Long id;
    private String username;

    @Length(min = 8, max = 20, message = "비밀번호는 8이상 20자리 이내로 만들어 주세요")
    private String password;

    @Email(message = "올바른 이메일 형식이 아닙니다 Ex)user@example.com")
    private String email;

    private String age;
    private String role;
}
