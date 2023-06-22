package study.datajpa.repository.custom;

import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;

// class명을 꼭 맞춰줘야한다.
// {커스텀 레포지토리 인터페이스명} + "Impl"
// 만약 다른 문구를 쓰고 싶다면, XML이나 JavaConfig를 사용해야한다.. 웬만하면 관례를 따르자.
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberCustomRepository {

  private final EntityManager em;

  @Override
  public List<Member> findMemberCustom() {
    return em.createQuery("select m from Member m")
        .getResultList();
  }
}
