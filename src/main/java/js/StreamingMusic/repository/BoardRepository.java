package js.StreamingMusic.repository;

import js.StreamingMusic.domain.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardRepository {

    private final EntityManager em;

    public void save(Board board) {
        em.persist(board);
    }

    public List<Board> findByAlbumId(String albumId) {
        String jpql = "select b from Board b join fetch b.member where b.traceId = :albumId";
        return em.createQuery(jpql, Board.class)
                .setParameter("albumId", albumId)
                .getResultList();
    }

    public Board findOne(Long id) {
        return em.find(Board.class, id);
    }

    public void remove(Board board) {
        em.remove(board);
    }

}
