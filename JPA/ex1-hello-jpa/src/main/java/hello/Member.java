package hello;

import java.util.Date;
import javax.persistence.*;

@Entity
// @Table(name = "MBR")
// call next value를 얼마나 한 번에 가져올지 설정이 allocationSize이다.
@SequenceGenerator(name = "member_seq_generator", sequenceName = "member_seq", initialValue = 1, allocationSize = 50) // SEQ 이름 지정
//@TableGenerator(name = "MEMBER_SEQ_GENERATOR", table = "MY_SEQUENCES", pkColumnName = "MEMBER_SEQ", allocationSize = 1)
public class Member {

//  @GeneratedValue(strategy = GenerationType.IDENTITY) // ex) MySQL autoincrement
  // `call next value for {sequence명}` 으로 마지막 SEQ를 가져와 그 다음으로 업데이트쳐줌 => Oracle에서 많이 사용
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_seq_generator")
  // 테이블을 확인 => SEQ 전략과 비슷 그러나 성능이 안 좋다. 운영에서 사용하기엔 부담스럽다.
//  @GeneratedValue(strategy = GenerationType.TABLE, generator = "MEMBER_SEQ_GENERATOR")
  @Id
  private Long id;

  @Column(name = "name",
      nullable = false,
      columnDefinition = "varchar(100) default 'EMPTY'")
  private String username;

  public Member() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}
