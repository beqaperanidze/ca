package prjnightsky.service;

import prjnightsky.entity.Order;
import prjnightsky.entity.StarMap;
import prjnightsky.entity.User;
import prjnightsky.exception.OrderNotFoundException;
import prjnightsky.exception.StarMapNotFoundException;
import prjnightsky.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import prjnightsky.repository.OrderRepository;
import prjnightsky.repository.StarMapRepository;
import prjnightsky.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final StarMapRepository starMapRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository, StarMapRepository starMapRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.starMapRepository = starMapRepository;
    }

    public Order createOrder(Long userId, Long starMapId, Order orderData) throws UserNotFoundException, StarMapNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        StarMap starMap = starMapRepository.findById(starMapId)
                .orElseThrow(StarMapNotFoundException::new);

        orderData.setUser(user);
        orderData.setStarMap(starMap);

        return orderRepository.save(orderData);
    }

    public List<Order> getAllOrders() { return orderRepository.findAll(); }

    public Optional<Order> getOrder(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUser(userId);
    }

    public Order updateOrder(Long id, Order updatedOrder) throws OrderNotFoundException, StarMapNotFoundException, UserNotFoundException {
        Optional<Order> existingOrderOpt = orderRepository.findById(id);
        if (existingOrderOpt.isEmpty()) {
            throw new OrderNotFoundException();
        }

        Order existingOrder = existingOrderOpt.get();

        if (updatedOrder.getUser() != null) {
            User user = userRepository.findById(updatedOrder.getUser().getId())
                    .orElseThrow(UserNotFoundException::new);
            existingOrder.setUser(user);
        }

        if (updatedOrder.getStarMap() != null) {
            StarMap starMap = starMapRepository.findById(updatedOrder.getStarMap().getId())
                    .orElseThrow(StarMapNotFoundException::new);
            existingOrder.setStarMap(starMap);
        }

        if (updatedOrder.getOrderDate() != null) {
            existingOrder.setOrderDate(updatedOrder.getOrderDate());
        }

        if (updatedOrder.getPaymentStatus() != null) {
            existingOrder.setPaymentStatus(updatedOrder.getPaymentStatus());
        }

        return orderRepository.save(existingOrder);
    }

    public void deleteOrder(Long id) throws OrderNotFoundException {
        Optional<Order> existingOrderOpt = orderRepository.findById(id);
        if (existingOrderOpt.isEmpty()) {
            throw new OrderNotFoundException();
        }
        orderRepository.deleteById(id);
    }
}
