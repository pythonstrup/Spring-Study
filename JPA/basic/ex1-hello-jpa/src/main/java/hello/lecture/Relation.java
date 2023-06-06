package hello.lecture;

import hello.entity.Locker;
import hello.entity.Member;
import hello.entity.Team;
import java.util.List;
import javax.persistence.EntityManager;

public class Relation {

  private void pk(EntityManager em) {
    Member member1 = new Member();
    member1.setUsername("A");

    Member member2 = new Member();
    member2.setUsername("B");

    Member member3 = new Member();
    member3.setUsername("C");

    System.out.println("===========================");
    em.persist(member1);
    em.persist(member2);
    em.persist(member3);
    System.out.println("member.id = " + member1.getId());
    System.out.println("member.id = " + member2.getId());
    System.out.println("member.id = " + member3.getId());
    System.out.println("===========================");
  }

  private void manyToOne(EntityManager em) {
    Team team = new Team();
    team.setName("TeamA");
    em.persist(team);

    Member member = new Member();
    member.setUsername("member1");
    member.setTeam(team);
    em.persist(member);

    em.flush();
    em.clear();

    Member findMember = em.find(Member.class, member.getId());
    Team findTeam = member.getTeam();
    System.out.println("findTeam = " + findTeam.getName());
  }

  private void relation(EntityManager em) {
    Team team = new Team();
    team.setName("TeamA");
    em.persist(team);

    Locker locker = new Locker();
    locker.setName("Locker1");
    em.persist(locker);

    Member member = new Member();
    member.setUsername("member1");
    member.addLocker(locker);
//      member.changeTeam(team);
    team.addMember(member);
    em.persist(member);

//      em.flush();
//      em.clear();

    Team findTeam = em.find(Team.class, team.getId());
    List<Member> members = findTeam.getMembers();

    System.out.println("======================");
//      System.out.println(findTeam); // toString으로 인한 순환참조 문제
    for (Member m: members) {
      System.out.println("m = " + m.getUsername());
    }
    System.out.println("======================");
  }
}
