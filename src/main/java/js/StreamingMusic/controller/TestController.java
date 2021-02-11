package js.StreamingMusic.controller;

import js.StreamingMusic.domain.Member;
import js.StreamingMusic.domain.Record;
import js.StreamingMusic.domain.RecordDto;
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
public class TestController {

    private final RecordService recordService;
    private final MemberService memberService;

    @PostMapping("/test")
    @ResponseBody
    public String test(HttpServletRequest request, @AuthenticationPrincipal MemberContext memberContext) {

        String username = memberContext.getUsername();
        Member member = memberService.findByUsername(username);

        String title = request.getParameter("title");
        String artist = request.getParameter("artist");
        System.out.println("artist = " + artist);

        /**
         * 내일 할것... record객체를 생성하는 조건을 만들어야함. 즉, 먼저 username에 속한 title과 artist를 record db에서 찾아 이미 존재하면 객체를 생성하면 안돼고 찾아서 id를 반환받자
         * 만약 db에 없으면 객체를 생성하고 db에 추가한다.
         */

        if (recordService.findDuplicateRecord(username, title, artist)) {
            Record record = new Record();
            record.setTitle(title);
            record.setArtist(artist);
            record.setPlayCount(1);
            record.setMember(member);

            recordService.addRecord(record);
        } else {
            recordService.addRecordCount(username, title, artist);
        }


        return "success";

    }

}
