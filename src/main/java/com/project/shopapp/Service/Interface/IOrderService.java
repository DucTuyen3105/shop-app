package com.project.shopapp.Service.Interface;

import com.project.shopapp.DTO.request.OrderDTO;
import com.project.shopapp.Exception.DataNotFoundException;
import com.project.shopapp.Model.Order;

import java.util.List;

public interface IOrderService {
    Order createOrder(OrderDTO orderDTO) throws Exception;
    Order getOrder(Long id) throws DataNotFoundException;
    Order updateOrder(Long id,OrderDTO orderDTO) throws DataNotFoundException;
    void deleteOrder(Long id);
    List<Order> findByUserId(Long userId);
}
