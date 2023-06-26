package study.datajpa.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import study.datajpa.entity.Member;

// Specification은 웬만하면 사용하지 말자...
public class MemberSpec {

  public static Specification<Member> teamName(final String teamName) {
    return new Specification<Member>() {
      @Override
      public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query,
          CriteriaBuilder builder) {

        if (StringUtils.isEmpty(teamName)) {
          return null;
        }

        Join<Object, Object> t = root.join("team", JoinType.INNER);
        return builder.equal(t.get("name"), teamName);
      }
    };
  }

  public static  Specification<Member> username(final String username) {
    return (Specification<Member>) (root, query, builder) ->
        builder.equal(root.get("username"), username);
  }
}
