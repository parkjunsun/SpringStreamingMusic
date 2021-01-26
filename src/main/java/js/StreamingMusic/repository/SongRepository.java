package js.StreamingMusic.repository;

import js.StreamingMusic.domain.Song;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;
import java.util.HashMap;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SongRepository {

    private final EntityManager em;

    public void save(Song song) {
        em.persist(song);
    }

    public List<Song> findAllByName(String name) {
        String jpql = "select s From Song s join s.member m where m.username like :name";
        List<Song> data = em.createQuery(jpql, Song.class).setParameter("name", name).getResultList();

        return data;
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

}
