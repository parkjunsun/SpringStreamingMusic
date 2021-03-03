package js.StreamingMusic.repository;

import js.StreamingMusic.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findByUserName(String username) {
        List<Member> members = em.createQuery("select m From Member m where m.username = :username", Member.class)
                .setParameter("username", username)
                .getResultList();

        return members.get(0);
    }

    /**
     *
     * 임시로 만든 함수
     *
     */

    public List<Member> findByAdminName(String username) {
        return em.createQuery("select m From Member m where m.username = :username", Member.class)
                .setParameter("username", username)
                .getResultList();
    }

    public List<Member> checkByUserName(String username) {
        List<Member> members = em.createQuery("select m From Member m where m.username = :username", Member.class)
                .setParameter("username", username)
                .getResultList();
        return members;
    }
}
