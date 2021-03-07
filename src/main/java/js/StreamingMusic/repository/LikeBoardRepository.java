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

    public List<LikeBoard> findByUserName(String username) {
        String jpql = "SELECT l FROM LikeBoard l JOIN l.member m WHERE m.username = :name";
        return em.createQuery(jpql, LikeBoard.class)
                .setParameter("name", username)
                .getResultList();
    }

}
