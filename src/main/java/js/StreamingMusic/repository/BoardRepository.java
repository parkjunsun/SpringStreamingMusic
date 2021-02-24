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

    public void remove(Board board) {
        em.remove(board);
    }

    public List<Board> findByAlbumId(Integer pageNum, String traceId, int pageCnt) {
        String jpql = "SELECT b FROM Board b JOIN FETCH b.member WHERE b.traceId = :traceId";
        return em.createQuery(jpql, Board.class)
                .setFirstResult(pageCnt * (pageNum-1))
                .setMaxResults(pageCnt)
                .setParameter("traceId", traceId)
                .getResultList();
    }

    public Board findOne(Long id) {
        return em.find(Board.class, id);
    }

    public List<Board> findByUsername(String name) {
        String jpql = "SELECT b FROM Board b JOIN FETCH b.member m WHERE m.username = :username ORDER BY b.createdDate DESC";
        return em.createQuery(jpql, Board.class)
                .setParameter("username", name)
                .setFirstResult(0)
                .setMaxResults(5)
                .getResultList();
    }


    public Long count(String traceId) {
        return em.createQuery("SELECT COUNT(b) FROM Board b WHERE b.traceId = :traceId", Long.class)
                .setParameter("traceId", traceId)
                .getSingleResult();

    }


}
