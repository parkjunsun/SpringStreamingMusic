package js.StreamingMusic.controller;

import js.StreamingMusic.domain.entity.Member;
import js.StreamingMusic.domain.entity.Record;
import js.StreamingMusic.security.MemberContext;
import js.StreamingMusic.service.MemberService;
import js.StreamingMusic.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;
    private final MemberService memberService;

    @PostMapping("/data")
    @ResponseBody
    public Message test(HttpServletRequest request, @AuthenticationPrincipal MemberContext memberContext) {

        String username = memberContext.getUsername();
        Member member = memberService.findByUsername(username).get(0);

        String title = request.getParameter("title");
        String artist = request.getParameter("artist");
        String img = request.getParameter("img");
        String songid = request.getParameter("songid");

        if (recordService.findDuplicateRecord(username, title, artist)) {
            Record record = new Record();
            record.setTitle(title);
            record.setArtist(artist);
            record.setImg(img);
            record.setSongid(songid);
            record.setPlayCount(1);
            record.setMember(member);

            recordService.addRecord(record);
        } else {
            recordService.addRecordCount(username, title, artist);
        }

        Message message = new Message();
        message.setMsg("정상적으로 데이터 전송이 완료되었습니다");
        return message;

    }

    static class Message {
        private String msg;

        public String getMsg() { return msg; }
        public void setMsg(String msg) {this.msg = msg;}
    }

}
