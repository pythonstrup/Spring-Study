package study.datajpa.entity;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.repository.MemberRepository;

@SpringBootTest
@Transactional
class MemberTest {

  @PersistenceContext
  EntityManager em;

  @Autowired
  MemberRepository memberRepository;

  @Test
  public void testEntity() {
    Team teamA = new Team("teamA");
    Team teamB = new Team("teamB");
    em.persist(teamA);
    em.persist(teamB);

    Member member1 = new Member("member1", 25, teamA);
    Member member2 = new Member("member2", 26, teamA);
    Member member3 = new Member("member3", 27, teamB);
    Member member4 = new Member("member4", 28, teamB);
    em.persist(member1);
    em.persist(member2);
    em.persist(member3);
    em.persist(member4);
    
    // 초기화
    em.flush();
    em.clear();
    
    // 확인
    List<Member> members = em.createQuery("select m from Member m", Member.class)
        .getResultList();
    for (Member member : members) {
      System.out.println("member = " + member);
      System.out.println("member.getTeam() = " + member.getTeam());
    }
  }


  @Test
  void JpaEventBaseEntity() throws Exception {
    // given
    Member member = new Member("member1");
    memberRepository.save(member);

    Thread.sleep(100);
    member.setUsername("member2");
    em.flush(); // @PreUdate
    em.clear();
    
    // when
    Member findMember = memberRepository.findById(member.getId()).get();
    
    // then
    System.out.println("findMember.getCreatedDate() = " + findMember.getCreatedDate());
    System.out.println("findMember.getLastModifiedDate() = " + findMember.getLastModifiedDate());
    System.out.println("findMember.getCreateBy() = " + findMember.getCreateBy());
    System.out.println("findMember.getLastModifiedBy() = " + findMember.getLastModifiedBy());
  }
}