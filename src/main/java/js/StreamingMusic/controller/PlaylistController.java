package js.StreamingMusic.controller;

import js.StreamingMusic.domain.entity.Member;
import js.StreamingMusic.domain.entity.Song;
import js.StreamingMusic.domain.dto.SongDto;
import js.StreamingMusic.exception.DuplicateSongException;
//import js.StreamingMusic.security.MemberContext;
import js.StreamingMusic.security.MemberContext;
import js.StreamingMusic.service.MemberService;
import js.StreamingMusic.service.SongService;
import js.StreamingMusic.service.data.DataApi;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PlaylistController {

    private final MemberService memberService;
    private final DataApi dataApi;
    private final SongService songService;

    // 임시 방편 //
    private String tempKeyword;

    private static final String searchUrl = "http://localhost:9999/search";

    @GetMapping("/playlist")
    public String showPlaylist(Model model,
                               @AuthenticationPrincipal MemberContext member) {

        // DB에서 최신 Member 정보 조회 (lastPlayedSongId 갱신을 위해)
        Member freshMember = memberService.findById(member.getMember().getId());

        if (!freshMember.getProvider().equals("JSMUSIC")){
            model.addAttribute("name", freshMember.getRealname());
        }
        else {
            model.addAttribute("name", freshMember.getUsername());
        }

        // 로그인한 회원의 플레이리스트에 있는 곡들을 가져와서 모델에 담아줌
        List<SongDto> songs = songService.findAllSongsByMemberId(freshMember.getId());
        model.addAttribute("songs", songs);

        // DB에서 조회한 최신 lastPlayedSongId를 모델에 담아줌
        model.addAttribute("lastPlayedSongId", freshMember.getLastPlayedSongId());
        return "playlist";
    }

    @PostMapping("/playlist")
    public String SavePlaylist(Model model,
                               @AuthenticationPrincipal MemberContext member,
                               @RequestParam(value = "add", required = false) List<String> param,
                               @RequestParam(value = "keyword", required = false) String keyword,
                               RedirectAttributes redirectAttributes,
                               HttpServletRequest request) throws IOException, ParseException {

        tempKeyword = keyword;

        Member m = memberService.findById(member.getMember().getId());
        String username = m.getUsername();

        if (param == null) {
            redirectAttributes.addFlashAttribute("errorMsg", "곡을 선택해주세요");
            return "redirect:" + request.getHeader("Referer");
        }


        if (param.size() == 1) {

            String[] params = param.get(0).split(";");
            String title = params[0];
            String artist = params[1];

            List<String> videoIds = dataApi.getVideoId(title, artist);

            String videoId = videoIds.get(0);
            String videoId2 = videoIds.get(1);
            String videoId3 = videoIds.get(2);

            String img = dataApi.getImg(title, artist);
            img = "https:" + img;

            List<String> details = dataApi.getDetail(title, artist);

            String genre = details.get(0);
            String duration = details.get(1);
            String songid = details.get(2);

            m.addSong(param.size());

            Song song = new Song();
            song.setTitle(title);
            song.setArtist(artist);
            song.setImg(img);
            song.setVideoId(videoId);
            song.setVideoId2(videoId2);
            song.setVideoId3(videoId3);
            song.setGenre(genre);
            song.setDuration(duration);
            song.setSongid(songid);
            song.setMember(m);

            songService.addSong(song, username, title, artist);

        } else {
            m.addSong(param.size());

            for (String p : param) {
                String[] params = p.split(";");
                String title = params[0];
                String artist = params[1];

                List<String> videoIds = dataApi.getVideoId(title, artist);
                String img_src = dataApi.getImg(title, artist);
                List<String> details = dataApi.getDetail(title, artist);

                String videoId = videoIds.get(0);
                String videoId2 = videoIds.get(1);
                String videoId3 = videoIds.get(2);

                String img = "https:" + img_src;
                String genre = details.get(0);
                String duration = details.get(1);
                String songid = details.get(2);

                Song song = new Song();
                song.setTitle(title);
                song.setArtist(artist);
                song.setImg(img);
                song.setVideoId(videoId);
                song.setVideoId2(videoId2);
                song.setVideoId3(videoId3);
                song.setGenre(genre);
                song.setDuration(duration);
                song.setSongid(songid);
                song.setMember(m);

                songService.addSong(song, username, title, artist);
            }
        }

        redirectAttributes.addFlashAttribute("successMsg", "플레이리스트에 추가 되었습니다");

        if (request.getHeader("Referer").equals(searchUrl)) {
            return "redirect:/search?keyword=" + keyword;
        } else {
            return "redirect:" + request.getHeader("Referer");
        }
    }


    @ExceptionHandler
    public String duplicateSongExceptionHandler(DuplicateSongException e, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());

        if (request.getHeader("Referer").equals(searchUrl)) {
            return "redirect:/search?keyword=" + tempKeyword;
        } else {
            return "redirect:" + request.getHeader("Referer");
        }
    }

    @PostMapping(value = "/playlist/{songId}/delete")
    public String deleteSong(Model model, @PathVariable("songId") Long songId,
                             @AuthenticationPrincipal MemberContext memberContext,
                             RedirectAttributes redirectAttributes,
                             HttpServletRequest request) {

        Member member = memberService.findByUsername(memberContext.getUsername()).get(0);
        member.removeSong(1);

        Song deleteSong = songService.findSong(songId);
        songService.removeSong(deleteSong);
        redirectAttributes.addFlashAttribute("updateMsg", "플레이리스트에서 삭제 되었습니다");
        return "redirect:" + request.getHeader("Referer");
    }

    @PostMapping(value = "/playlist/relocation")
    public String reorderSong(Model model, @RequestParam(value = "relocation") String jsonStr, @AuthenticationPrincipal MemberContext memberContext
    ) throws ParseException {

        List<HashMap<String, String>> songs = dataApi.parsingAndRelocate(jsonStr); //ajax로 jsonStr을 받아오는것임

        model.addAttribute("songs", songs);
        model.addAttribute("name", memberContext.getUsername());

        return "playlist";
    }

    @GetMapping("/playlist/category")
    public String showByGenre(Model model,
                              @AuthenticationPrincipal MemberContext memberContext,
                              @RequestParam("genre") String genre) {



        String refactoringGenreName = dataApi.refactoringName(genre);
        List<SongDto> songs = songService.findAllSongsByCategory(memberContext.getMember().getId(), refactoringGenreName);

        if (!memberContext.getMember().getProvider().equals("JSMUSIC")){
            model.addAttribute("name", memberContext.getMember().getRealname());
        }
        else {
            model.addAttribute("name", memberContext.getMember().getUsername());
        }
        model.addAttribute("songs", songs);
        model.addAttribute("genrename", genre);

        return "genreplaylist";
    }

}
