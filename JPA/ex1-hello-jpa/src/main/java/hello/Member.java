package hello;

import java.util.Date;
import javax.persistence.*;

@Entity
public class Member {

  @Id @GeneratedValue
  @Column(name = "MEMBER_ID")
  private Long id;

  @Column(name = "USERNAME")
  private String username;

//  @Column(name = "TEAM_ID")
//  private Long teamId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "TEAM_ID")
  private Team team;

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

  public Team getTeam() {
    return team;
  }

  public void setTeam(Team team) {
    this.team = team;
  }

  // 연관관계 편의 메소드
//  public void changeTeam(Team team) {
//    team.getMembers().add(this);
//    this.team = team;
//  }


//  @Override
//  public String toString() {
//    return "Member{" +
//        "id=" + id +
//        ", username='" + username + '\'' +
//        ", team=" + team +
//        '}';
//  }
}
