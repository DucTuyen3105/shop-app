package com.project.shopapp.Repository;

import com.project.shopapp.Model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long> {
    List<OrderDetail> findOrderDetailById(Long id);
    List<OrderDetail>findByOrderId(Long orderId);
}