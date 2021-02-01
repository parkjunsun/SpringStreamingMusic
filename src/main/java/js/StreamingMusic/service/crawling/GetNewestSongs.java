package js.StreamingMusic.service.crawling;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class GetNewestSongs {

    public List<HashMap<String, String >> getSongs(String page) throws IOException {
        int cnt = 1;
        List<HashMap<String, String>> songs = new ArrayList<>();
        String url = String.format("https://www.genie.co.kr/newest/song?pg=%s", page);

        Document doc = Jsoup.connect(url).get();
        Elements songList = doc.select("div.music-list-wrap > table.list-wrap > tbody > tr.list");

        for (Element song : songList) {
            String song_id = song.attr("songid");
            Elements tds = song.select("td");
            String img_src = tds.get(2).selectFirst("a.cover > img").attr("src");
            String img = "https:" + img_src;
            String title = song.selectFirst("td.info > a.title").text().strip();
            String artist = song.selectFirst("td.info > a.artist").text();

            HashMap<String, String> map = new HashMap<>();
            map.put("img", img);
            map.put("title", title);
            map.put("artist", artist);
            map.put("song_id", song_id);

//            if (page.equals("2")) {
//               map.put("index", Integer.toString(cnt+30));
//            } else if (page.equals("3")) {
//                map.put("index", Integer.toString(cnt+60));
//            }
            switch (page) {
                case "2": map.put("index", Integer.toString(cnt+30));
                    break;
                case "3": map.put("index", Integer.toString(cnt+60));
                    break;
                case "4": map.put("index", Integer.toString(cnt+90));
                    break;
                case "5": map.put("index", Integer.toString(cnt+120));
                    break;
            }

            songs.add(map);
            cnt += 1;
        }

        return songs;
    }
}
