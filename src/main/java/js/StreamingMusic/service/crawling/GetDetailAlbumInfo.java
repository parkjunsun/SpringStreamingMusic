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
public class GetDetailAlbumInfo {

    public HashMap<String, String> getData(String album_id) throws IOException {
        String url = "https://www.genie.co.kr/detail/albumInfo?axnm=" + album_id;
        HashMap<String, String> detailAlbumInfo = new HashMap<>();

        Document doc = Jsoup.connect(url).get();
        Element infozone = doc.selectFirst("div.info-zone");
        Elements infodata = doc.select("ul.info-data > li");
        String img_src = doc.selectFirst("div.photo-zone > a").attr("href");

        String img = "https:" + img_src;
        String title = infozone.selectFirst("h2.name").text();
        String artist = infodata.get(0).selectFirst("span.value > a").text();
        String genre = infodata.get(1).selectFirst("span.value").text();
        String publisher = infodata.get(2).selectFirst("span.value").text();
        String agency = infodata.get(3).selectFirst("span.value").text();
        String releaseDate = infodata.get(4).selectFirst("span.value").text().strip();

        detailAlbumInfo.put("img", img);
        detailAlbumInfo.put("title", title);
        detailAlbumInfo.put("artist", artist);
        detailAlbumInfo.put("genre", genre);
        detailAlbumInfo.put("publisher", publisher);
        detailAlbumInfo.put("agency", agency);
        detailAlbumInfo.put("release_date", releaseDate);

        return detailAlbumInfo;
    }


    public List<HashMap<String, String>> getSongs(String album_id) throws IOException{
        String url = "https://www.genie.co.kr/detail/albumInfo?axnm=" + album_id;
        List<HashMap<String, String>> getList = new ArrayList<>();

        Document doc = Jsoup.connect(url).get();
        Elements songList = doc.select("div.music-list-wrap.none-album-list2 > table.list-wrap > tbody > tr.list");

        for (Element song : songList) {
            String title = song.selectFirst("td.info > a.title").text().strip();
            title = title.replace("TITLE", "").strip();
            String artist = song.selectFirst("td.info > a.artist").text();
            String song_id = song.attr("songid");

            HashMap<String, String> map = new HashMap<>();
            map.put("title", title);
            map.put("artist", artist);
            map.put("song_id", song_id);

            getList.add(map);
        }

        return getList;
    }

}
