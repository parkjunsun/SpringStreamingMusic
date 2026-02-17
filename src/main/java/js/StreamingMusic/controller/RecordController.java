package js.StreamingMusic.controller;

import js.StreamingMusic.domain.entity.Member;
import js.StreamingMusic.domain.entity.Record;
//import js.StreamingMusic.security.MemberContext;
import js.StreamingMusic.security.MemberContext;
import js.StreamingMusic.service.MemberService;
import js.StreamingMusic.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;
    private final MemberService memberService;

    @PostMapping("/record")
    @ResponseBody
    public Message test(HttpServletRequest request, @AuthenticationPrincipal MemberContext memberContext) {


        String username = memberContext.getUsername();
        Member member = memberService.findById(memberContext.getMember().getId());

        // ajax로 데이터 받아옴
        Long id = Long.valueOf(request.getParameter("id")); //song테이블의 고유id값
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

            recordService.addRecord(record);    //노래기록저장 (insert)
        } else {
            recordService.addRecordCount(username, title, artist);  //노래기록저장 (update)
        }

        memberService.updateLastPlayedSongId(member.getId(), id);  //마지막으로 재생한 곡의 아이디 업데이트

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
