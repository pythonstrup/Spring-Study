package hello.itemservice.repository.mybatis;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ItemMapper {

  void save(Item item);

  void update(@Param("id") Long id, @Param("updateParam") ItemUpdateDto updateParam);

  // XML 대신 아래와 같이 어노테이션을 사용할 수 있지만 거의 사용하지 않는다고 함.
  @Select("select id, item_name, price, quantity from item where id=#{id}")
  Optional<Item> findById(Long id);

  List<Item> findAll(ItemSearchCond itemSearch);
  // 동적 쿼리를 작성할 때 부등호는 그냥 적을 수 없다. 아래와 같이 적어줘야한다.
  // and price &lt;= #{maxPrice}
  // and price <![CDATA[<=]]> #{maxPrice}
}
