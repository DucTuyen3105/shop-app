package com.project.shopapp.Controller;

import com.project.shopapp.DTO.request.OrderDetailDTO;
import com.project.shopapp.DTO.response.OrderDetailResponse;
import com.project.shopapp.Exception.DataNotFoundException;
import com.project.shopapp.Model.OrderDetail;
import com.project.shopapp.Service.Interface.IOrderDetailService;
import com.project.shopapp.Service.impl.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders-detail")
@RequiredArgsConstructor
@Slf4j
public class OrderDetailController {
    private final IOrderDetailService orderDetailService;
    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO, BindingResult result) {
        if(result.hasErrors())
        {
            List<String> error = result.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage().toString()).toList();
            return ResponseEntity.badRequest().body(error);
        }
        try{
            OrderDetail orderDetail = orderDetailService.createOrderDetail(orderDetailDTO);
            return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(orderDetail));
        }catch (Exception e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@Valid @PathVariable("id")Long id)
    {
        try {
            OrderDetail orderDetail = orderDetailService.getOrderDetail(id);
            return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(orderDetail));
        }catch (Exception e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("orderId") Long orderId)
    {
        try {
            List<OrderDetail> orderDetails = orderDetailService.findByOrderId(orderId);
            List<OrderDetailResponse> orderDetailResponses = orderDetails.stream().map(OrderDetailResponse :: fromOrderDetail).toList();
            return ResponseEntity.ok(orderDetailResponses);
        }catch (Exception e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(@Valid @PathVariable("id") Long id,@Valid @RequestBody OrderDetailDTO orderDetailDTO) {
        try{
            OrderDetail orderDetail = orderDetailService.updateOderDetail(id,orderDetailDTO);
            return ResponseEntity.ok(orderDetail);
        }catch (Exception e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(@Valid @PathVariable("id") Long id)
    {
        orderDetailService.deleteById(id);
        return ResponseEntity.ok("Delete OrderDetail Successfully with id: " + id);
    }
}
