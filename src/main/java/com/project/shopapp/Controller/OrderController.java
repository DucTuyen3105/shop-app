package com.project.shopapp.Controller;

import com.project.shopapp.DTO.request.OrderDTO;
import com.project.shopapp.DTO.response.OrderResponse;
import com.project.shopapp.Model.Order;
import com.project.shopapp.Service.Interface.IOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final IOrderService orderService;
    @PostMapping("")
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderDTO orderDTO, BindingResult result)
    {
        try{
            if(result.hasErrors())
            {
                var errorMessage = result.getFieldErrors().stream().map(error-> error.getDefaultMessage()).toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            Order orderResponse = orderService.createOrder(orderDTO);
            return ResponseEntity.ok(OrderResponse.fromOrder(orderResponse));
        }catch (Exception e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getOrders(@Valid @PathVariable("userId")Long userId)
    {
        try{
            List<Order> orders = orderService.findByUserId(userId);
            return ResponseEntity.ok(orders);
        }catch (Exception e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@Valid @PathVariable("id")Long orderId)
    {
        try{
            Order existingOrder = orderService.getOrder(orderId);
            return ResponseEntity.ok(existingOrder);
        }catch (Exception e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateOrder(@Valid @PathVariable("userId") long id,@Valid @RequestBody OrderDTO orderDTO)
    {
        try{
            Order order = orderService.updateOrder(id,orderDTO);
            return ResponseEntity.ok(OrderResponse.fromOrder(order));
        }catch (Exception e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?>deleteOrder(@Valid @PathVariable("id") long id)
    {
        try{
            orderService.deleteOrder(id);
            return ResponseEntity.ok("delete success");
        }catch (Exception e)
        {
            return null;
        }
    }
}
