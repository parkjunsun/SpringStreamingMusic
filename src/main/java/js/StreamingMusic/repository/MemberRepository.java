package js.StreamingMusic.repository;

import js.StreamingMusic.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findByUserName(String username) {
        Member member = em.createQuery("select m From Member m where m.username = :username", Member.class)
                .setParameter("username", username)
                .getSingleResult();

        return member;
    }
}
