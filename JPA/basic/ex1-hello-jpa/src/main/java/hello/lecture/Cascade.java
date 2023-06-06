package hello.lecture;

import hello.entity.cascadeEx.Child;
import hello.entity.cascadeEx.Parent;
import javax.persistence.EntityManager;

// 영속성 전이 - 특정 Entity를 영속 상태로 만들 때 연관된 Entity도 함께 영속 상태로 만들고 싶을 때
// ALL: 모두 적용
// PERSIST: 영속
// REMOVE: 삭제
// 도메인 주도 설계의 Aggregate Root 개념을 구현할 때 유용한 기능이다.
public class Cascade {

  // 영속성 전이를 사용하지 않았을 때, entity마다 persist를 호출해야한다...
  private void noCascade(EntityManager em) {
    Child child1 = new Child();
    Child child2 = new Child();

    Parent parent = new Parent();
    parent.addChild(child1);
    parent.addChild(child2);

    em.persist(parent);
    em.persist(child1);
    em.persist(child2);
  }

  // 옵션 ALL
  // 영속성 전이를 사용했을 때는 그 중점이 되는 entity만 영속성 컨텍스트에 넣어줘도 그 자식들이 알아서 딸려 들어간다.
  // 릴레이션 어노테이션에 cascade = CascadeType.ALL 옵션
  private void cascade(EntityManager em) {
    Child child1 = new Child();
    Child child2 = new Child();

    Parent parent = new Parent();
    parent.addChild(child1);
    parent.addChild(child2);

    em.persist(parent);
  }

  // orphanRemoval = true // 고아 객체 => 특정 엔티티가 개인 소유할 때만 사용해야한다.
  // 부모 엔티티와 연관관계가 끊어진 자식 엔티티를 자동으로 삭제한다.
  private void orphanRemoval(EntityManager em) {
    Child child1 = new Child();
    Child child2 = new Child();

    Parent parent = new Parent();
    parent.addChild(child1);
    parent.addChild(child2);

    em.persist(parent);

    em.flush();
    em.clear();

    Parent findParent = em.find(Parent.class, parent.getId());
    findParent.getChildList().remove(0);
    // 연관을 끊어버리면 아래 쿼리가 실행된다.
    // /* delete hello.entity.cascadeEx.Child */
    // delete from Child where id=?
  }

  // orphanRemoval이 false면 상관없다. => 외래키 제약 조건때문에 parent가 지워지지 않고 예외가 터질 것이다.
  private void orphanRemovalWithRemoveParent(EntityManager em) {
    Child child1 = new Child();
    Child child2 = new Child();

    Parent parent = new Parent();
    parent.addChild(child1);
    parent.addChild(child2);

    em.persist(parent);

    em.flush();
    em.clear();

    Parent findParent = em.find(Parent.class, parent.getId());
    em.remove(findParent);
    // parent를 지우면 아래 쿼리가 실행된다.
    // /* delete hello.entity.cascadeEx.Child */ delete from Child where id=?
    // /* delete hello.entity.cascadeEx.Child */ delete from Child where id=?
    // /* delete hello.entity.cascadeEx.Parent */ delete from Parent where id=?
  }
}
