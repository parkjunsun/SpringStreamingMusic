package js.StreamingMusic.service;

import js.StreamingMusic.domain.dto.MailDto;
import js.StreamingMusic.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendEmailService {

    private final MemberService memberService;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;

    private static final String FROM_ADDRESS = "qkrwnstns52@gamil.com";

    public MailDto createMailAndChangePassword(String username, String userEmail, String realname) {
        String str = getTempPassword();
        MailDto mailDto = new MailDto();
        mailDto.setAddress(userEmail);
        mailDto.setTitle(String.format("%s님의 JSMUSIC 임시비밀번호 안내 이메일 입니다.", username));
        mailDto.setMessage(String.format("안녕하세요. JSMUSIC 임시비밀번호 안내 관련 이메일 입니다. [ %s ]님의 임시 비밀번호는 %s 입니다", username, str));
        updatePassword(str, username, userEmail, realname);
        return mailDto;
    }

    public MailDto createMail(String userEmail, String key) {
        MailDto mailDto = new MailDto();
        mailDto.setToken(key);
        mailDto.setAddress(userEmail);
        mailDto.setTitle(String.format("JSMUSIC 인증번호 발급 메일입니다."));
        mailDto.setMessage(String.format("안녕하세요. JSMUSIC 인증번호 안내 관련 이메일 입니다. 인증번호는 %s 입니다", key));
        return mailDto;
    }

    private void updatePassword(String str, String username, String userEmail, String realname) {
        String pw = passwordEncoder.encode(str);
        memberService.updateMemberPassword(pw, username, userEmail, realname);
    }

    private String getTempPassword() {
        char[] charSet = new char[] {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
        };

        String str = "";
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }

        return str;
    }

    public void mailSend(MailDto mailDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDto.getAddress());
        message.setFrom(SendEmailService.FROM_ADDRESS);
        message.setSubject(mailDto.getTitle());
        message.setText(mailDto.getMessage());

        javaMailSender.send(message);
    }

}
