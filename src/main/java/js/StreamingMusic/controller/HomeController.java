package js.StreamingMusic.controller;

import js.StreamingMusic.security.MemberContext;
import js.StreamingMusic.service.crawling.GetHomeNewAlbums;
import js.StreamingMusic.service.crawling.GetTop10;
import js.StreamingMusic.service.data.DataApi;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final GetTop10 songs;
    private final GetHomeNewAlbums getHomeNewAlbums;
    private final DataApi dataApi;

    @RequestMapping(value = "/", method = { RequestMethod.GET, RequestMethod.POST })
    public String home(Model model, HttpServletRequest request, @AuthenticationPrincipal MemberContext member) throws Exception{
        if (request.getMethod().equals("GET")) {
            if(member != null) {
                String username = member.getUsername();
                model.addAttribute("name", username);
            }
            model.addAttribute("top10", songs.getTop10());
            model.addAttribute("albums", getHomeNewAlbums.getHomeNewAlbumPg1());
        }
        else if (request.getMethod().equals("POST")) {
            if(member != null) {
                String username = member.getUsername();
                model.addAttribute("name", username);
            }


            String param = request.getParameter("play");
            String[] data = param.split(";");
            String title = data[0];
            String artist = data[1];
            Integer index = Integer.parseInt(data[2]);


            List<String> videoIds = dataApi.getVideoId(title, artist);
            model.addAttribute("videoIds", videoIds);
            model.addAttribute("index", index);

            model.addAttribute("top10", songs.getTop10());
            model.addAttribute("albums", getHomeNewAlbums.getHomeNewAlbumPg1());

            }

        return "home";
    }




}
