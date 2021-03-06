package js.StreamingMusic.repository;

import js.StreamingMusic.domain.entity.Song;
import js.StreamingMusic.domain.dto.SongDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SongRepository {

    private final EntityManager em;

    public void save(Song song) {
        em.persist(song);
    }


    public void remove(Song song) {em.remove(song);}

    public Song findOne(Long songid) {
        return em.find(Song.class, songid);
    }


    public List<SongDto> findAllByName(String name) {
        String jpql = "select new js.StreamingMusic.domain.dto.SongDto(s.id, s.title, s.artist, s.videoId, s.videoId2, s.videoId3, s.img, s.genre, s.duration, s.songid) " +
                      "From Song s join s.member m " +
                      "where m.username like :name";
        List<SongDto> data = em.createQuery(jpql, SongDto.class).setParameter("name", name).getResultList();

        return data;
    }



    public List<SongDto> findAllByCategory(String name, String genre) {
        String jpql = "select new js.StreamingMusic.domain.dto.SongDto(s.id, s.title, s.artist, s.videoId, s.videoId2, s.videoId3, s.img, s.genre, s.duration, s.songid) ";
        if (genre.equals("가요 / 발라드;가요 / 블루스/포크;가요 / R&B/소울")){
            String[] columns = genre.split(";");
            String ballard = columns[0];
            String blues = columns[1];
            String rnb = columns[2];
            String condition = "From Song s join s.member m " +
                               "where m.username like :name " +
                               "and s.genre IN :genreList";
            return em.createQuery(jpql+condition, SongDto.class)
                    .setParameter("name", name)
                    .setParameter("genreList", Arrays.asList(ballard, blues, rnb))
                    .getResultList();
        } else if (genre.equals("기타")) {
            String ballardColumns = "가요 / 발라드";
            String bluesColumns = "가요 / 블루스/포크 ";
            String rnbColumns = "가요 / R&B/소울";
            String danceColumns = "가요 / 댄스";
            String rockColumns = "가요 / 락";
            String hiphopColumns = "가요 / 랩/힙합";
            String elecColumns = "가요 / 일렉트로니카";
            String indeColumns = "가요 / 인디";
            String trotColumns = "가요 / 트로트";
            String ytColumns = "youtube";

            String condition = "From Song s join s.member m " +
                               "where m.username like :name " +
                               "and s.genre NOT IN :genreList";

            return em.createQuery(jpql+condition, SongDto.class)
                    .setParameter("name", name)
                    .setParameter("genreList", Arrays.asList(ballardColumns, bluesColumns, rnbColumns, danceColumns, rockColumns, hiphopColumns, elecColumns, indeColumns, trotColumns, ytColumns))
                    .getResultList();
        } else {
            String condition = "From Song s join s.member m " +
                    "where m.username like :name " +
                    "and s.genre like :genre";
            return em.createQuery(jpql+condition, SongDto.class).setParameter("name", name).setParameter("genre", genre).getResultList();
        }

    }



    public List<Song> findAllByTitle(String title) {
        return em.createQuery("select s From Song s where s.title = :title", Song.class)
                .setParameter("title", title)
                .getResultList();
    }

    public List<Song> findAllByArtist(String artist) {
        return em.createQuery("select s From Song s where s.artist = :artist", Song.class)
                .setParameter("artist", artist)
                .getResultList();
    }

    public List<Song> findDuplicateSong(Song song, String name, String title, String artist) {
        String jpql = "select s From Song s join s.member m where m.username like :name and s.title like :title and s.artist like :artist";
        return em.createQuery(jpql, Song.class)
                .setParameter("name", name)
                .setParameter("title", title)
                .setParameter("artist", artist)
                .getResultList();
    }



}
