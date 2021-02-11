package js.StreamingMusic.repository;

import js.StreamingMusic.domain.Record;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;
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

}
