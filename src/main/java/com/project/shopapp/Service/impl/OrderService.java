package com.project.shopapp.Service.impl;

import com.project.shopapp.DTO.request.OrderDTO;
import com.project.shopapp.DTO.response.OrderResponse;
import com.project.shopapp.Exception.DataNotFoundException;
import com.project.shopapp.Model.Order;
import com.project.shopapp.Model.OrderStatus;
import com.project.shopapp.Model.User;
import com.project.shopapp.Repository.OrderRepository;
import com.project.shopapp.Repository.UserRepository;
import com.project.shopapp.Service.Interface.IOrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    @Override
    public Order createOrder(OrderDTO orderDTO) throws Exception{
        User user = userRepository.findById(orderDTO.getUserId()).orElseThrow(()-> new DataNotFoundException("Cannot find user with id: " + orderDTO.getUserId()));
        modelMapper.typeMap(OrderDTO.class,Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        Order order = new Order();
        modelMapper.map(orderDTO,order);
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now(): orderDTO.getShippingDate();
        if( shippingDate.isBefore(LocalDate.now()))
        {
            throw new DataNotFoundException("Date must be at least today");
        }
        order.setActive(true);
        order.setShippingDate(shippingDate);
        System.out.println(order);
        System.out.println(order.getOrderDate());
        orderRepository.save(order);
        modelMapper.typeMap(Order.class,OrderResponse.class);
        OrderResponse orderResponse = new OrderResponse();
        modelMapper.map(order,orderResponse);
        return order;
    }

    @Override
    public Order getOrder(Long id) throws DataNotFoundException {
        return orderRepository.findById(id).orElseThrow(null);
    }

    @Override
    public Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException {
        Order order = orderRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Không tìm thấy order"));
        User existingUser = userRepository.findById(orderDTO.getUserId()).orElseThrow(() -> new DataNotFoundException("Không tìm thấy user"));
        modelMapper.typeMap(OrderDTO.class,Order.class).addMappings(mapper -> mapper.skip(Order::setId));
        modelMapper.map(orderDTO,order);
        order.setUser(existingUser);
        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long id) {
         Order order = orderRepository.findById(id).orElse(null);
         if(order != null){
             order.setActive(false);
             orderRepository.save(order);
         }
    }

    @Override
    public List<Order> findByUserId(Long userId) {

        return orderRepository.findByUserId(userId);
    }
}
