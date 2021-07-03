package js.StreamingMusic.repository;

import js.StreamingMusic.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public void remove(Member member) {
        em.remove(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findByUserName(String username) {
        return em.createQuery("select m From Member m where m.username = :username", Member.class)
                .setParameter("username", username)
                .getResultList();
    }

    public List<Member> findByUsernameViaIdAndEmail(String email, String realname) {
        return em.createQuery("SELECT m FROM Member m WHERE m.email = :email AND m.realname = :realname", Member.class)
                .setParameter("email", email)
                .setParameter("realname", realname)
                .getResultList();
    }

    public List<Member> findByUsernameViaAllInfo(String username, String email, String realname) {
        return em.createQuery("SELECT m FROM Member m WHERE m.username = :username AND m.email = :email AND m.realname = :realname", Member.class)
                .setParameter("username", username)
                .setParameter("email", email)
                .setParameter("realname", realname)
                .getResultList();
    }

    public List<Member> findByUsernameContaining(String username) {
        return em.createQuery("SELECT m FROM Member m WHERE m.username LIKE :username", Member.class)
                .setParameter("username","%" + username + "%")
                .getResultList();
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
        return em.createQuery("select m From Member m where m.username = :username", Member.class)
                .setParameter("username", username)
                .getResultList();
    }

    public List<Member> findAll() {
        return em.createQuery("SELECT m FROM Member m", Member.class)
                .getResultList();
    }



}
