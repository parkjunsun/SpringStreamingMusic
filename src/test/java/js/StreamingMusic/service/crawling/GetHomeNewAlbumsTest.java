package js.StreamingMusic.service.crawling;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GetHomeNewAlbumsTest {

    @Autowired
    private GetHomeNewAlbums getHomeNewAlbums;

    @Test
    void show() {
        try {
            List<HashMap<String, String>> homeNewAlbumPg1 = getHomeNewAlbums.getHomeNewAlbumPg1();
            System.out.println(homeNewAlbumPg1);
        } catch (Exception e) {
            e.getMessage();
        }
    }

}