package js.StreamingMusic.service.crawling;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;

@Service
public class GetDetailSongInfo {

    public HashMap<String,String> getData(String song_id) throws IOException {
        String url =  "https://www.genie.co.kr/detail/songInfo?xgnm=" + song_id;
        HashMap<String, String> detailSongData = new HashMap<>();

        String songWriter = "";
        String composer = "";

        Document doc = Jsoup.connect(url).get();
        Element infozone = doc.selectFirst("div.info-zone");
        Elements infodata = doc.select("ul.info-data > li");

        String img_src = doc.selectFirst("div.photo-zone a").attr("href");
        String img = "https:" + img_src;

        String title = infozone.selectFirst("h2.name").text().strip();
        String artist = infodata.get(0).selectFirst("span.value a").text();
        String albumName = infodata.get(1).selectFirst("span.value a").text();
        String genre = infodata.get(2).selectFirst("span.value").text();
        String duration = infodata.get(3).selectFirst("span.value").text();
        String lyrics = doc.selectFirst("pre#pLyrics p").text();

        if (infodata.size() >= 5) {
            Elements fourth = infodata.get(4).select("span.value a");
            if (fourth.size() >= 2) {
                for (Element element : fourth) {
                    songWriter += element.text() + ',';
                    songWriter = songWriter.substring(0,songWriter.length()-1);
                }
            } else {
                songWriter = fourth.get(0).text();
            }

            Elements fifth = infodata.get(5).select("span.value a");
            if (fifth.size() >= 2) {
                for (Element element : fifth) {
                    composer += element.text() + ',';
                    composer = composer.substring(0, composer.length()-1);
                }
            } else {
                composer = fifth.get(0).text();
            }

            detailSongData.put("img", img);
            detailSongData.put("title", title);
            detailSongData.put("artist", artist);
            detailSongData.put("album", albumName);
            detailSongData.put("genre", genre);
            detailSongData.put("duration", duration);
            detailSongData.put("lyrics", lyrics);
            detailSongData.put("songwriter", songWriter);
            detailSongData.put("composer", composer);
        } else {
            detailSongData.put("img", img);
            detailSongData.put("title", title);
            detailSongData.put("artist", artist);
            detailSongData.put("album", albumName);
            detailSongData.put("genre", genre);
            detailSongData.put("duration", duration);
            detailSongData.put("lyrics", lyrics);
        }

        return detailSongData;
    }

}
