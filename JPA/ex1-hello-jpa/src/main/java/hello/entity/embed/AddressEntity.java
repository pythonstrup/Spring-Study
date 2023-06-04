package hello.entity.embed;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

// Value Type Collection 대체 엔티티
@Entity
@Table(name = "address")
public class AddressEntity {

  @Id @GeneratedValue
  private Long id;

  private Address address;

  public AddressEntity() {}

  public AddressEntity(Address address) {
    this.address = address;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AddressEntity that = (AddressEntity) o;
    return Objects.equals(id, that.id) && Objects.equals(address, that.address);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, address);
  }
}
