package js.StreamingMusic.service.crawling;

import js.StreamingMusic.exception.NotExistSearchResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class GetSearchSongs {

    public List<HashMap<String, String>> getSearch(String search) throws IOException {

        String url = "https://www.genie.co.kr/search/searchMain?query=" + search;
        List<HashMap<String, String>> songs = new ArrayList<>();

        Document doc = Jsoup.connect(url).get();
        Elements trs = doc.select("#body-content > div.search_song > div.search_result_detail > div > table > tbody > tr.list");

        for (Element tr : trs) {
            Element info = tr.selectFirst("td.info");
            if (info == null) {
                return songs;
            }
            String songid = tr.attr("songid");
//            String title = info.selectFirst("a").attr("title").strip();
            String artist = info.selectFirst("a.artist").text().strip();

            Elements td = tr.select("td");
            Element data = td.get(2);
            Element img_src = data.selectFirst("img");
            String img = "https:" + img_src.attr("src");
            String title = img_src.attr("alt");

            HashMap<String, String> map = new HashMap<>();
            map.put("title", title);
            map.put("artist", artist);
            map.put("img", img);
            map.put("song_id", songid);

            songs.add(map);
        }

        return songs;
    }


}
