package study.datajpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"}) // 여기서 team 하면 큰일난다. => 무한루프될 수도!! 연관관계 매핑에서는 ToString 안하는 게 좋다.
public class Member {

  @Id @GeneratedValue
  @Column(name = "member_id")
  private Long id;
  private String username;
  private int age;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "team_id")
  private Team team;

  public Member(String username) {
    this.username = username;
  }

  public Member(String username, int age, Team team) {
    this.username = username;
    this.age = age;
    if (team != null) {
      changeTeam(team);
    }
  }

  public Member(String username, int age) {
    this.username = username;
    this.age = age;
  }

  public void changeTeam(Team team) {
    this.team = team;
    team.getMembers().add(this);
  }
}
