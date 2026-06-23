package com.industry_connect.order_service.repository;

import com.industry_connect.order_service.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    @Query("SELECT o FROM Order o WHERE o.buyerOrgId = :orgId OR o.sellerOrgId = :orgId ORDER BY o.createdAt DESC")
    List<Order> findByOrganizationId(@Param("orgId") Long orgId);
}
