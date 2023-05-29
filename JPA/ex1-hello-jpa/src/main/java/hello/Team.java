package hello;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Team extends BaseEntity {

  @Id @GeneratedValue
  @Column(name = "TEAM_ID")
  private Long id;

  private String name;

  @OneToMany(mappedBy = "team")
  private List<Member> members = new ArrayList<>();

  // 연관관계 편의메소드 - 둘 중 한쪽에만 있으면 된다.
  public void addMember(Member member) {
    member.setTeam(this);
    this.members.add(member);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Member> getMembers() {
    return members;
  }

  public void setMembers(List<Member> members) {
    this.members = members;
  }

  // Lombok의 toString 웬만하면 쓰지 말자. - 무한 루프
//  @Override
//  public String toString() {
//    return "Team{" +
//        "id=" + id +
//        ", name='" + name + '\'' +
//        ", members=" + members +
//        '}';
//  }
}
