package hello.itemservice.repository.v2;

import static hello.itemservice.domain.QItem.item;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemSearchCond;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class ItemQueryRepositoryV2 {

  private final JPAQueryFactory queryFactory;

  public ItemQueryRepositoryV2(EntityManager em) {
    this.queryFactory = new JPAQueryFactory(em);
  }

  public List<Item> findAll(ItemSearchCond cond) {
    return queryFactory.select(item)
        .from(item)
        .where(
            likeItemName(cond.getItemName()),
            maxPrice(cond.getMaxPrice()))
        .fetch();
  }

  private BooleanExpression likeItemName(String itemName) {
    if (StringUtils.hasText(itemName)) {
      return item.itemName.like("%" + itemName + "%");
    }
    return null;
  }

  private BooleanExpression maxPrice(Integer price) {
    if (price != null) {
      return item.price.loe(price);
    }
    return null;
  }
}
