package js.StreamingMusic.controller;

import js.StreamingMusic.domain.entity.Member;
import js.StreamingMusic.domain.entity.Song;
import js.StreamingMusic.domain.dto.SongDto;
import js.StreamingMusic.exception.DuplicateSongException;
import js.StreamingMusic.security.MemberContext;
import js.StreamingMusic.service.MemberService;
import js.StreamingMusic.service.SongService;
import js.StreamingMusic.service.data.DataApi;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("/playlist")
    public String showPlaylist(Model model, @AuthenticationPrincipal MemberContext member) {

        String username = member.getUsername();
        model.addAttribute("name", username);
        List<SongDto> songs = songService.findAllSongsByName(username);   //컨트롤러에서는 엔티티를 절대 직접 반환하지 말자. dto로 받아야한다 entity를 직접보내면 json 생성 라이브러리 문제로 무한루프에 빠진다
        model.addAttribute("songs", songs);
        return "playlist";
    }

    @PostMapping("/playlist")
    public String SavePlaylist(Model model, @AuthenticationPrincipal MemberContext member,
                                            @RequestParam(value = "add", required = false) List<String> param,
                                            RedirectAttributes redirectAttributes,
                                            HttpServletRequest request) throws IOException, ParseException {
        String username = member.getUsername();
        Member m = memberService.findByUsername(username).get(0);

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
        return "redirect:" + request.getHeader("Referer");
    }


    @ExceptionHandler
    public String duplicateSongExceptionHandler(DuplicateSongException e, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        return "redirect:" + request.getHeader("Referer");
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
    public String reorderSong(Model model, @RequestParam(value = "relocation") String jsonStr, @AuthenticationPrincipal MemberContext memberContext) throws ParseException {

        List<HashMap<String, String>> songs = dataApi.parsingAndRelocate(jsonStr);

        model.addAttribute("songs", songs);
        model.addAttribute("name", memberContext.getUsername());

        return "playlist";
    }

    @GetMapping("/playlist/category")
    public String showByGenre(Model model, @AuthenticationPrincipal MemberContext memberContext, @RequestParam("genre") String genre) {
        String refactoringGenreName = dataApi.refactoringName(genre);
        String username = memberContext.getUsername();

        List<SongDto> songs = songService.findAllSongsByCategory(username, refactoringGenreName);
        model.addAttribute("songs", songs);
        model.addAttribute("name", username);
        model.addAttribute("genrename", genre);

        return "genreplaylist";
    }

}
