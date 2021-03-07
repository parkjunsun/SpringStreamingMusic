package js.StreamingMusic.repository;

import js.StreamingMusic.domain.entity.LikeBoard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LikeBoardRepository {

    private final EntityManager em;

    public void save(LikeBoard likeBoard) {
        em.persist(likeBoard);
    }

    public void remove(LikeBoard likeBoard) {
        em.remove(likeBoard);
    }

    public List<LikeBoard> findByUserName(String username) {
        String jpql = "SELECT l FROM LikeBoard l JOIN FETCH l.member m WHERE m.username = :name";
        return em.createQuery(jpql, LikeBoard.class)
                .setParameter("name", username)
                .getResultList();
    }

    public List<LikeBoard> findByMemberIdAndBoardId(Long memberId, Long boardId) {
        String jpql = "SELECT l FROM LikeBoard l JOIN FETCH l.member m JOIN FETCH l.board b WHERE m.id = :memberId AND b.id = :boardId";
        return em.createQuery(jpql, LikeBoard.class)
                .setParameter("memberId", memberId)
                .setParameter("boardId", boardId)
                .getResultList();
    }

}
