package js.StreamingMusic.controller;

import js.StreamingMusic.service.crawling.GetHomeNewAlbums;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class AjaxController {

    private final GetHomeNewAlbums getHomeNewAlbums;

    @RequestMapping("/ajax/firstmenu")
    public String firstMenu(Model model) throws Exception{
        model.addAttribute("albums", getHomeNewAlbums.getHomeNewAlbumPg1());
        return "ajax/firstmenu";
    }

    @RequestMapping("/ajax/secondmenu")
    public String secondMenu(Model model) throws Exception{
        model.addAttribute("albums", getHomeNewAlbums.getHomeNewAlbumPg2());
        return "ajax/secondmenu";
    }

    @RequestMapping("/ajax/thirdmenu")
    public String thirdMenu(Model model) throws Exception{
        model.addAttribute("albums", getHomeNewAlbums.getHomeNewAlbumPg3());
        return "ajax/thirdmenu";
    }

    @RequestMapping("/ajax/fourthmenu")
    public String fourthMenu(Model model) throws Exception{
        model.addAttribute("albums", getHomeNewAlbums.getHomeNewAlbumPg4());
        return "ajax/fourthmenu";
    }

    @RequestMapping("/ajax/fifthmenu")
    public String fifthMenu(Model model) throws Exception{
        model.addAttribute("albums", getHomeNewAlbums.getHomeNewAlbumPg5());
        return "ajax/fifthmenu";
    }
}
