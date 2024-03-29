package js.StreamingMusic.controller;

import js.StreamingMusic.domain.dto.BoardDto;
import js.StreamingMusic.domain.dto.MemberForm;
import js.StreamingMusic.domain.dto.RecordDto;
import js.StreamingMusic.domain.entity.Board;
import js.StreamingMusic.domain.entity.Member;
import js.StreamingMusic.domain.entity.Record;
import js.StreamingMusic.security.MemberContext;
import js.StreamingMusic.service.BoardService;
import js.StreamingMusic.service.MemberService;
import js.StreamingMusic.service.RecordService;
import js.StreamingMusic.service.crawling.GetDetailAlbumInfo;
import js.StreamingMusic.service.crawling.GetDetailSongInfo;
import js.StreamingMusic.service.data.DataApi;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final RecordService recordService;
    private final BoardService boardService;
    private final DataApi dataApi;
    private final GetDetailAlbumInfo getDetailAlbumInfo;
    private final GetDetailSongInfo getDetailSongInfo;


    @GetMapping("/user/{username}")
    public String showUserInfo(Model model, @AuthenticationPrincipal MemberContext memberContext) throws IOException {


        if (!memberContext.getMember().getProvider().equals("JSMUSIC"))
            model.addAttribute("name", memberContext.getMember().getRealname());
        else
            model.addAttribute("name", memberContext.getMember().getUsername());

        String username = memberContext.getMember().getUsername();

        model.addAttribute("social", memberContext.getMember().getProvider());

        List<HashMap<String, String>> datum = new ArrayList<>();

        Member member = memberService.findByUsername(username).get(0);
//        model.addAttribute("name", member.getUsername());
        model.addAttribute("count", member.getSongQuantity());
        model.addAttribute("role", member.getRole());
        model.addAttribute("joinDate", member.getJoinDate());
        model.addAttribute("boardCount", member.getBoardQuantity());

        List<Record> records = recordService.findAll(username);
        List<RecordDto> playCountByArtist = recordService.findMostPlayCountByArtist(username);
        for (RecordDto recordDto : playCountByArtist) {
            HashMap<String, String> hashMap = new HashMap<>();
            String img = dataApi.getImgByArtist(recordDto.getArtist());
            hashMap.put("artist", recordDto.getArtist());
            hashMap.put("img", img);
            hashMap.put("count", Long.toString(recordDto.getCount()));

            datum.add(hashMap);
        }

        model.addAttribute("records", records);
        model.addAttribute("datum", datum);


        List<Board> results = boardService.findBoardByUserName(username);
        List<BoardDto> boards = new ArrayList<>();
        for (Board result : results) {
            BoardDto boardDto = new BoardDto();
            boardDto.setId(result.getId());
            boardDto.setWriter(username);
            boardDto.setComment(result.getComment());
            boardDto.setTraceId(result.getTraceId());
            boardDto.setCreatedDate(result.getCreatedDate());
            boardDto.setType(result.getType());

            boards.add(boardDto);
        }

        model.addAttribute("boardList", boards);

        return "members/userInfo";
    }

    @GetMapping("/user/{username}/edit")
    public String updateMemberForm(Model model, @AuthenticationPrincipal MemberContext memberContext) {

        if (!memberContext.getMember().getProvider().equals("JSMUSIC"))
            model.addAttribute("name", memberContext.getMember().getRealname());
        else
            model.addAttribute("name", memberContext.getMember().getUsername());

        model.addAttribute("social", memberContext.getMember().getProvider());

        String username = memberContext.getMember().getUsername();

        Member member = memberService.findByUsername(username).get(0);

        MemberForm form = new MemberForm();
        form.setPassword(member.getPassword());
        form.setEmail(member.getEmail());
        form.setAge(member.getAge());

        model.addAttribute("memberForm", form);
        return "members/updateMemberForm";

    }

    @PostMapping("/user/{username}/edit")
    public String updateMember(Model model, @Valid MemberForm form, BindingResult result, HttpServletRequest request,
                               @AuthenticationPrincipal MemberContext memberContext, RedirectAttributes redirectAttributes) {

        String tmpName = "";

        if (!memberContext.getMember().getProvider().equals("JSMUSIC"))
            tmpName = memberContext.getMember().getRealname();
        else
            tmpName = memberContext.getMember().getUsername();

        if (result.hasErrors()) {
            return "members/updateMemberForm";
        }

        String username = memberContext.getMember().getUsername();
        memberService.updateMember(username, passwordEncoder.encode(form.getPassword()), form.getEmail(), form.getAge());

        redirectAttributes.addFlashAttribute("successMsg", "회원정보가 수정 되었습니다");

        String path = URLEncoder.encode("/user/" + tmpName, StandardCharsets.UTF_8);
        return "redirect:" + path;
    }

    @GetMapping("/user/{username}/myboard")
    public String showMyBoard(Model model, @PathVariable("username") String username,
                              @RequestParam(value = "page", defaultValue = "1") Integer pageNum) {


        List<BoardDto> boards = new ArrayList<>();
        List<Board> result = boardService.findAllBoardByUserName(pageNum, username);
        List<Integer> pageList = boardService.getMyPageList(pageNum, username);
        for (Board board : result) {
            BoardDto boardDto = new BoardDto();
            boardDto.setId(board.getId());
            boardDto.setWriter(username);
            boardDto.setComment(board.getComment());
            boardDto.setTraceId(board.getTraceId());
            boardDto.setCreatedDate(board.getCreatedDate());
            boardDto.setTitle(board.getTitle());
            boardDto.setArtist(board.getArtist());
            boardDto.setImg(board.getImg());
            boardDto.setType(board.getType());

            boards.add(boardDto);
        }

        Long myBoardCount = boardService.getMyBoardCount(username);

        model.addAttribute("boardList", boards);
        model.addAttribute("pageList", pageList);
        model.addAttribute("curPage", pageNum);
        model.addAttribute("name", username);
        model.addAttribute("allCount", myBoardCount);
        return "members/userBoard";
    }
}
