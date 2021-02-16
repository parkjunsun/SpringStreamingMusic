package js.StreamingMusic.repository;

import js.StreamingMusic.domain.Record;
import js.StreamingMusic.domain.RecordDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RecordRepository {

    private final EntityManager em;

    public void save(Record record) {
        em.persist(record);
    }

    public List<Record> findDuplicateRecord(String name, String title, String artist) {
        String jpql = "select r From Record r join r.member m where m.username like :name and r.title like :title and r.artist like :artist";
        return em.createQuery(jpql, Record.class)
                .setParameter("name", name)
                .setParameter("title", title)
                .setParameter("artist", artist)
                .getResultList();
    }

    public List<Record> findByUserName(String name) {
        String jpql = "select r From Record r join r.member m where m.username like :name ORDER BY r.playCount DESC";
        return em.createQuery(jpql, Record.class)
                .setParameter("name", name)
                .setFirstResult(0)
                .setMaxResults(10)
                .getResultList();
    }

    public List<RecordDto> findMostArtist(String name) {
        String jpql = "select new js.StreamingMusic.domain.RecordDto(r.artist, sum(r.playCount) as cnt) From Record r join r.member m where m.username like :name GROUP BY r.artist ORDER BY cnt DESC";
        return em.createQuery(jpql, RecordDto.class)
                .setParameter("name", name)
                .setFirstResult(0)
                .setMaxResults(5)
                .getResultList();

    }

}
