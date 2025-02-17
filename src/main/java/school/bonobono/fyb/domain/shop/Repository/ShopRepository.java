package school.bonobono.fyb.domain.shop.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import school.bonobono.fyb.domain.shop.Entity.Shop;

import java.util.List;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    @Query("SELECT s FROM Shop s WHERE s.id IN :ids ORDER BY FIELD(s.id, :ids)")
    List<Shop> findByIdsInSpecifiedOrder(@Param("ids") List<Long> ids);
    List<Shop> findByShopNameContaining(String shopName);
    List<Shop> findByIdIn(List<Long> sortedViewsId);
}
