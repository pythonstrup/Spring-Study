package study.datajpa.repository.custom;

import java.util.List;
import study.datajpa.entity.Member;

// 무조건 이렇게 사용할 필요는 없다!
// 틀에 갇혀있는 것은 좋지 않다.
public interface MemberCustomRepository {

  List<Member> findMemberCustom();
}
