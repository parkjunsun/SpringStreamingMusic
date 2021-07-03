package js.StreamingMusic.controller.mail;

import js.StreamingMusic.controller.RecordController;
import js.StreamingMusic.domain.dto.MailDto;
import js.StreamingMusic.domain.dto.MemberForm;
import js.StreamingMusic.domain.entity.Member;
import js.StreamingMusic.exception.NotExistUserNameException;
import js.StreamingMusic.service.MemberService;
import js.StreamingMusic.service.SendEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;


@Controller
@RequestMapping("/forget")
@RequiredArgsConstructor
public class MailController {

    private final MemberService memberService;
    private final SendEmailService sendEmailService;

    @GetMapping("/id")
    public String findIdForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "find/findIdForm";
    }


    @PostMapping("/id")
    public String findId(@Valid MemberForm memberForm, BindingResult result, RedirectAttributes redirectAttributes){

        if (result.hasErrors()) {
            return "find/findIdForm";
        }

        List<Member> usernames = memberService.findByUsernameViaNameAndEmail(memberForm.getEmail(), memberForm.getRealname());
        Member member = usernames.get(0);
        String msg = String.format("회원님의 ID는 %s 입니다.", member.getUsername());

        redirectAttributes.addFlashAttribute("successMsg", msg);
        return "redirect:/login";
    }


    @GetMapping("/pw")
    public String findPwForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "find/findPwForm";
    }


    @PostMapping("/pw")
    public String findPw(@Valid MemberForm memberForm, BindingResult result, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "find/findPwForm";
        }

        List<Member> usernames = memberService.findByUserCheckViaAllInfo(memberForm.getUsername(), memberForm.getEmail(), memberForm.getRealname());
        Member member = usernames.get(0);
        String msg = String.format("본인인증에 성공했습니다.\n " +
                "%s 으로 임시비밀번호가 발급되었습니다.\n " +
                "로그인 후 새 비밀번호로 변경하세요.", member.getEmail());

        MailDto dto = sendEmailService.createMailAndChangePassword(memberForm.getUsername(), memberForm.getEmail(), memberForm.getRealname());
        sendEmailService.mailSend(dto);

        redirectAttributes.addFlashAttribute("successMsg", msg);
        return "redirect:/login";
    }

    @PostMapping("/authEmail")
    @ResponseBody
    public Message auth(@RequestParam("email") String email, @RequestParam("key") String key) {


        MailDto mailDto = sendEmailService.createMail(email, key);
        sendEmailService.mailSend(mailDto);

        Message message = new Message();
        message.setMsg("정상적으로 데이터 전송이 완료되었습니다");
        return message;
    }

    static class Message {
        private String msg;

        public String getMsg() { return msg; }
        public void setMsg(String msg) {this.msg = msg;}
    }

    @ExceptionHandler
    public String NotExistUserNameExceptionHandler(NotExistUserNameException e, RedirectAttributes redirectAttributes,
                                                   HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        return "redirect:" + request.getHeader("Referer");
    }
}
