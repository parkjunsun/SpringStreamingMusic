package js.StreamingMusic.listener;


import js.StreamingMusic.domain.entity.Member;
import js.StreamingMusic.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }
        createAdminIfNotFound("admin", "1164425ab^^", "qkrwnstns52@naver.com", 1, "ROLE_ADMIN", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        alreadySetup = true;
    }


    public void createAdminIfNotFound(String name, String password, String email, Integer age, String role, String joinDate ) {

        List<Member> findAdmin = memberService.findByAdminName(name);
        if (findAdmin.isEmpty()) {
            Member admin = new Member();
            admin.setUsername(name);
            admin.setPassword(passwordEncoder.encode(password));
            admin.setEmail(email);
            admin.setAge(age);
            admin.setRole(role);
            admin.setJoinDate(joinDate);

            memberService.join(admin);
        }

    }
}
