package js.StreamingMusic.service.data;

import com.google.common.net.UrlEscapers;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataApi {

    private static String api_key = "AIzaSyD26KoGfRFz3p6ptxqInXSGKq3jt8_C13I";

    public List<String> getVideoId(String title, String artist) throws IOException, MalformedURLException, ParseException {

        List<String> videoIds = new ArrayList<>();

        String urlStr = "https://www.googleapis.com/youtube/v3/search?q=" + title + "+" + artist + "+" + "audio&key=" + api_key +"&part=id&maxResults=3&type=video&videoEmbeddable=true";
        String encodedUrl = UrlEscapers.urlFragmentEscaper().escape(urlStr);
        URL url = new URL(encodedUrl);

        BufferedReader bf;
        String line = "";
        String result = "";

        bf = new BufferedReader(new InputStreamReader(url.openStream()));

        while((line = bf.readLine()) != null) {
            result = result.concat(line);
        }

        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(result);

        JSONArray items = (JSONArray) obj.get("items");
        int dataCnt = items.size();

        if (dataCnt == 1) {
            JSONObject info1 = (JSONObject) items.get(0);
            JSONObject id1 = (JSONObject) info1.get("id");

            String videoId1 = (String) id1.get("videoId");
            String videoId2 = "";
            String videoId3 = "";

            videoIds.add(videoId1);
            videoIds.add(videoId2);
            videoIds.add(videoId3);
        } else if (dataCnt == 2) {
            JSONObject info1 = (JSONObject) items.get(0);
            JSONObject id1 = (JSONObject) info1.get("id");
            JSONObject info2 = (JSONObject) items.get(1);
            JSONObject id2 = (JSONObject) info2.get("id");

            String videoId1 = (String) id1.get("videoId");
            String videoId2 = (String) id2.get("videoId");
            String videoId3 = "";

            videoIds.add(videoId1);
            videoIds.add(videoId2);
            videoIds.add(videoId3);

        } else if (dataCnt == 3) {
            JSONObject info1 = (JSONObject) items.get(0);
            JSONObject id1 = (JSONObject) info1.get("id");
            JSONObject info2 = (JSONObject) items.get(1);
            JSONObject id2 = (JSONObject) info2.get("id");
            JSONObject info3 = (JSONObject) items.get(2);
            JSONObject id3 = (JSONObject) info2.get("id");

            String videoId1 = (String) id1.get("videoId");
            String videoId2 = (String) id2.get("videoId");
            String videoId3 = (String) id3.get("videoId");

            videoIds.add(videoId1);
            videoIds.add(videoId2);
            videoIds.add(videoId3);
        }

        return videoIds;

    }
}
