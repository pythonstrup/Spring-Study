package hello.lecture;

import hello.entity.Member;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.hibernate.Hibernate;

public class Proxy {

  private void proxy1(EntityManager em) {
    Member member1 = new Member();
    member1.setUsername("user1");
    em.persist(member1);

    em.flush();
    em.clear();

//      Member findMember = em.find(Member.class, member.getId());
    Member findMember1 = em.getReference(Member.class, member1.getId()); // 프록시 클래스 - 가짜 엔티티 객체 조회
    System.out.println("before findMember = " + findMember1.getClass());
    printMember(findMember1);
    System.out.println("after findMember = " + findMember1.getClass()); // 프록시가 엔티티로 대체되지 않는다.
  }

  private static void printMember(Member member) {
    String username = member.getUsername();
    System.out.println("username = " + username);
  }

  // proxy의 instance
  private void proxy2(EntityManager em) {
    Member member1 = new Member();
    member1.setUsername("user1");
    em.persist(member1);

    Member member2 = new Member();
    member2.setUsername("user2");
    em.persist(member2);

    Member findMember2 = em.getReference(Member.class, member2.getId());
    System.out.println("member1 == member2: " + (member1.getClass() == member2.getClass()));
    System.out.println("member1 is Member: " + (member1 instanceof Member));
    System.out.println("member2 is Member: " + (member2 instanceof Member));
  }

  // proxy가 entity로 대체되는 경우
  private void proxyReplacedByEntity(EntityManager em) {
    Member member1 = new Member();
    member1.setUsername("user1");
    em.persist(member1);

    em.flush();
    em.clear();

    Member m1 = em.find(Member.class, member1.getId());
    System.out.println("m1 = " + m1.getClass());

    Member reference = em.getReference(Member.class, member1.getId());
    System.out.println("reference = " + reference.getClass()); // 프록시가 엔티티로 대체된다.
    System.out.println("m1 == reference: " + (m1 == reference));
  }

  // entity가 proxy로 대체되는 경우
  private void entityReplacedByProxy(EntityManager em) {
    Member member1 = new Member();
    member1.setUsername("user1");
    em.persist(member1);

    em.flush();
    em.clear();

    Member reference = em.getReference(Member.class, member1.getId());
    System.out.println("reference = " + reference.getClass());

    Member m1 = em.find(Member.class, member1.getId()); // 오히려 find로 찾아오는 entity가 proxy로 대체되어 버린다.
    System.out.println("m1 = " + m1.getClass()); //

    System.out.println("m1 == reference: " + (m1 == reference));
  }

  // proxy는 영속성과 생명을 같이 한다.
  private void proxyDependsOnPersistence(EntityManager em) {
    Member member1 = new Member();
    member1.setUsername("user1");
    em.persist(member1);

    em.flush();
    em.clear();

    Member reference = em.getReference(Member.class, member1.getId());
    System.out.println("reference = " + reference.getClass());

//      em.detach(reference);
//      em.close();
    em.clear();

    System.out.println("reference = " + reference.getUsername());
  }

  // proxy가 loading 됐는지 확인하는 방법
  private void persistenceUnitUtilIsLoaded(EntityManagerFactory emf, EntityManager em) {
    Member member1 = new Member();
    member1.setUsername("user1");
    em.persist(member1);

    em.flush();
    em.clear();

    Member reference = em.getReference(Member.class, member1.getId());
    System.out.println("reference = " + reference.getClass());

    System.out.println("Before isLoaded = " + emf.getPersistenceUnitUtil().isLoaded(reference)); // false

    reference.getUsername(); // loading
    System.out.println("After isLoaded = " + emf.getPersistenceUnitUtil().isLoaded(reference)); // true
  }

  // getter로 proxy를 강제초기화하는 것은 뭔가 어색해보인다.
  // Hibernate의 initialize 메소드를 사용하면 강제초기화를 할 수 있다.
  private void forceToInitialize(EntityManager em) {

    Member member1 = new Member();
    member1.setUsername("user1");
    em.persist(member1);

    em.flush();
    em.clear();

    Member reference = em.getReference(Member.class, member1.getId());
    System.out.println("reference = " + reference.getClass());
    Hibernate.initialize(reference); // 강제초기화 => select query가 발생한다.
  }
}
