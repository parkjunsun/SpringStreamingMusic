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

    public List<Board> findAll() {
        String jpql = "select b from Board b join fetch b.member";
        return em.createQuery(jpql, Board.class)
                .getResultList();
    }

}
