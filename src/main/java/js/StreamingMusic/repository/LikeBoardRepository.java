package js.StreamingMusic.repository;

import js.StreamingMusic.domain.entity.LikeBoard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;

@Repository
@RequiredArgsConstructor
public class LikeBoardRepository {

    private final EntityManager em;

    public void save(LikeBoard likeBoard) {
        em.persist(likeBoard);
    }

}
