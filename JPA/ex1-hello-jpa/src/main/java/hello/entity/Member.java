package hello.entity;

import hello.entity.embed.Address;
import hello.entity.embed.Period;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
public class Member extends BaseEntity {

  @Id @GeneratedValue
  @Column(name = "MEMBER_ID")
  private Long id;

  @Column(name = "USERNAME")
  private String username;

  @Embedded
  private Period workPeriod;

  @Embedded
  private Address homeAddress;

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "city", column=@Column(name="WORK_CITY")),
    @AttributeOverride(name = "street", column=@Column(name="WORK_STREET")),
    @AttributeOverride(name = "zipcode", column=@Column(name="WORK_ZIPCODE"))
  })
  private Address workAddress;

  @ManyToOne(fetch = FetchType.LAZY)
//  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "TEAM_ID")
  private Team team;

  @OneToOne
  @JoinColumn(name = "LOCKER_ID")
  private Locker locker;

  @OneToMany(mappedBy = "member")
  private List<MemberProduct> memberProducts = new ArrayList<>();

  public void addLocker(Locker locker) {
    this.locker = locker;
    locker.setMember(this);
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

  public Period getWorkPeriod() {
    return workPeriod;
  }

  public void setWorkPeriod(Period workPeriod) {
    this.workPeriod = workPeriod;
  }

  public Address getHomeAddress() {
    return homeAddress;
  }

  public void setHomeAddress(Address homeAddress) {
    this.homeAddress = homeAddress;
  }

  public Team getTeam() {
    return team;
  }

  public void setTeam(Team team) {
    this.team = team;
  }

  public Locker getLocker() {
    return locker;
  }

  public void setLocker(Locker locker) {
    this.locker = locker;
  }

  public List<MemberProduct> getMemberProducts() {
    return memberProducts;
  }

  public void setMemberProducts(List<MemberProduct> memberProducts) {
    this.memberProducts = memberProducts;
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
