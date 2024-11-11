package prjnightsky.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import prjnightsky.entity.Order;
import prjnightsky.exception.OrderNotFoundException;
import prjnightsky.exception.StarMapNotFoundException;
import prjnightsky.exception.UserNotFoundException;
import prjnightsky.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/user/{userId}/starmap/{starmapId}")
    public ResponseEntity<?> createOrder(@PathVariable("userId") Long userId,
                                         @PathVariable("starmapId") Long starmapId,
                                         @RequestBody Order orderData) {
        try {
            Order order = orderService.createOrder(userId, starmapId, orderData);
            return new ResponseEntity<>(order, HttpStatus.CREATED);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (StarMapNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Starmap not found");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Order> getById(@PathVariable("id") Long id) {
        return orderService.getOrder(id)
                .map(order -> new ResponseEntity<>(order, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getByUser(@PathVariable("userId") Long userId) {
        List<Order> orders = orderService.getOrdersByUser(userId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody Order order) {
        try {
            Order updatedOrder = orderService.updateOrder(id, order);
            return ResponseEntity.ok(updatedOrder);
        } catch (OrderNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        } catch (StarMapNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Associated StarMap not found");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Associated User not found");
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable("id") Long id) {
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        } catch (OrderNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
