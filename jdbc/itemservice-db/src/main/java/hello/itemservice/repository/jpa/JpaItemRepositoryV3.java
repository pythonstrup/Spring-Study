package hello.itemservice.repository.jpa;

import static hello.itemservice.domain.QItem.item;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Repository
@Transactional
public class JpaItemRepositoryV3 implements ItemRepository {

  private final EntityManager em;
  private final JPAQueryFactory queryFactory;

  public JpaItemRepositoryV3(EntityManager em) {
    this.em = em;
    this.queryFactory = new JPAQueryFactory(em);
  }

  @Override
  public Item save(Item item) {
    em.persist(item);
    return item;
  }

  @Override
  public void update(Long itemId, ItemUpdateDto updateParam) {
    Item findItem = em.find(Item.class, itemId);
    findItem.setItemName(updateParam.getItemName());
    findItem.setPrice(updateParam.getPrice());
    findItem.setQuantity(updateParam.getQuantity());
  }

  @Override
  public Optional<Item> findById(Long id) {
    Item item = em.find(Item.class, id);
    return Optional.ofNullable(item);
  }

  public List<Item> findAllOld(ItemSearchCond cond) {

    String itemName = cond.getItemName();
    Integer maxPrice = cond.getMaxPrice();

    BooleanBuilder builder = new BooleanBuilder();
    if (StringUtils.hasText(itemName)) {
      builder.and(item.itemName.like("%" + itemName + "%"));
    }
    if (maxPrice != null) {
      builder.and(item.price.loe(maxPrice));
    }

    List<Item> result = queryFactory.select(item)
        .from(item)
        .where(builder)
        .fetch();

    return result;
  }

  @Override
  public List<Item> findAll(ItemSearchCond cond) {

    String itemName = cond.getItemName();
    Integer maxPrice = cond.getMaxPrice();

    return queryFactory.select(item)
        .from(item)
        .where(likeItemName(itemName), maxPrice(maxPrice))
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