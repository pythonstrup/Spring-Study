package hello.entity.cascadeEx;

import hello.entity.cascadeEx.Child;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Parent {

  @Id @GeneratedValue
  private Long id;

  private String name;

  // cascade는 parent와 child의 생명주기가 비슷할 때만 사용해야한다.
  // 접점이 거의 없는데 설정하면 parent를 삭제했다고 child가 다 날라가 버리고, 큰일날 수 있다.
  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Child> childList = new ArrayList<>();

  public void addChild(Child child) {
    childList.add(child);
    child.setParent(this);
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

  public List<Child> getChildList() {
    return childList;
  }

  public void setChildList(List<Child> childList) {
    this.childList = childList;
  }
}
