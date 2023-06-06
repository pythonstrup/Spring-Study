package hello.entity;

import hello.entity.embed.Address;
import hello.entity.embed.AddressEntity;
import hello.entity.embed.Period;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

  // 값타입 컬렉션? 값타입을 하나 이상 사용할 때 사용
  // 데이터베이스는 컬렉션을 저장할 수 없기 때문에 별도의 테이블이 필요하다.
  @ElementCollection
  @CollectionTable(name = "FAVORITE_FOOD", joinColumns = @JoinColumn(name = "MEMBER_ID"))
  @Column(name = "FOOD_NAME")
  private Set<String> favoriteFoods = new HashSet<>();

//  @OrderColumn(name = "address_history_order") // 의도하지 않게 동작하는 경우가 많다!! 웬만하면 사용하지 말자..
//  @ElementCollection
//  @CollectionTable(name = "ADDRESS", joinColumns = @JoinColumn(name = "MEMBER_ID"))
//  private List<Address> addressHistory = new ArrayList<>();

  // 결론: 값타입 컬렉션은 사용하지 않는 것이 좋다?!?! => 실무에서는 일대다 관계를 위한 엔티티를 만들고, 여기에서 값타입을 사용하는 경우가 많다.
  // 값타입 컬렉션을 매핑하는 테이블은 모든 컬럼을 묶어서 기본키를 구성해야한다. => null 입력 X, 중복저장 X
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true) // !!영속성 전이와 고아 객체 제거를 사용해 값타입 컬렉션처럼 사용한다!!
  @JoinColumn(name = "MEMBER_ID")
  private List<AddressEntity> addressHistory = new ArrayList<>();

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

  public Set<String> getFavoriteFoods() {
    return favoriteFoods;
  }

  public void setFavoriteFoods(Set<String> favoriteFoods) {
    this.favoriteFoods = favoriteFoods;
  }

  public List<AddressEntity> getAddressHistory() {
    return addressHistory;
  }

  public void setAddressHistory(List<AddressEntity> addressHistory) {
    this.addressHistory = addressHistory;
  }

  public Address getWorkAddress() {
    return workAddress;
  }

  public void setWorkAddress(Address workAddress) {
    this.workAddress = workAddress;
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
