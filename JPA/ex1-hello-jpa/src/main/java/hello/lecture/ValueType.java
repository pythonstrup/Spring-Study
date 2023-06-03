package hello.lecture;

import hello.entity.Member;
import hello.entity.embed.Address;
import hello.entity.embed.Period;
import javax.persistence.EntityManager;

public class ValueType {

  private void embeddedType(EntityManager em) {
    Member member = new Member();
    member.setUsername("user1");
    member.setHomeAddress(new Address("city", "street", "10000"));
    member.setWorkPeriod(new Period());

    em.persist(member);
  }

  // 값타입을 사용할 때 좋지 않은 예시
  private void valueTypeSideEffect(EntityManager em) {
    Address homeAddress = new Address("city", "street", "10000");

    Member member1 = new Member();
    member1.setUsername("user1");
    member1.setHomeAddress(homeAddress);
    member1.setWorkPeriod(new Period());
    em.persist(member1);

    Member member2 = new Member();
    member2.setUsername("user1");
    member2.setHomeAddress(homeAddress); // address를 member1과 공유
    member2.setWorkPeriod(new Period());
    em.persist(member2);

//    member1.getHomeAddress().setCity("newCity"); // member1만 바꾸려고 했지만, member2에도 영향을 줘버린다.
    // update 쿼리도 2번이나 나가버린다. (side effect - 이런 버그는 진짜 잡기 어렵다.)
  }

  // 값은 아래와 같이 복사해서 사용해야 하는 번거로움이 있다.
  private void copyValueType(EntityManager em) {
    Address homeAddress = new Address("city", "street", "10000");

    Member member1 = new Member();
    member1.setUsername("user1");
    member1.setHomeAddress(homeAddress);
    member1.setWorkPeriod(new Period());
    em.persist(member1);

    Address copy = new Address(homeAddress.getCity(), homeAddress.getStreet(), homeAddress.getZipcode());
    Member member2 = new Member();
    member2.setUsername("user1");
    member2.setHomeAddress(copy);
    member2.setWorkPeriod(new Period());
    em.persist(member2);

//    member1.getHomeAddress().setCity("newCity"); // 새롭게 객체를 만들어 할당했기 때문에 member2에 영향을 주지 않는다.
  }

  // 객체의 값을 변경할 수 있는 도구가 있으면 다른 개발자들이 이 객체를 바꾸는 것을 컴파일이나 런타임에서 막을 수 있는 방법도 없다.
  // 따라서 값타입은 불변객체로 만들어 setter를 아예 사용하지 않거나, private로 두는 것이 좋다.
  // 그리고 값타입을 변경하고 싶다면 새로운 객체를 만들어 할당해야한다.
  private void immutableObject(EntityManager em) {
    Address homeAddress = new Address("city", "street", "10000");

    Member member1 = new Member();
    member1.setUsername("user1");
    member1.setHomeAddress(homeAddress);
    member1.setWorkPeriod(new Period());
    em.persist(member1);

    Address newHomeAddress = new Address("newCity", "newStreet", homeAddress.getZipcode());
    member1.setHomeAddress(newHomeAddress);
  }
}
