package js.StreamingMusic.validate;

import js.StreamingMusic.domain.dto.MemberForm;
import js.StreamingMusic.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class MemberFormValidator implements Validator {

    private final MemberService memberService;

    @Override
    public boolean supports(Class<?> aclass) {
        return aclass.isAssignableFrom(MemberForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MemberForm memberForm = (MemberForm)target;
        if(memberService.validateDuplicateMember(memberForm.getUsername())) {
            errors.rejectValue("username", "invalid.username",
                    new Object[]{memberForm.getUsername()}, "이미 사용중인 아이디 입니다");
        }
    }
}
