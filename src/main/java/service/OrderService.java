package service;

import entity.Order;
import entity.OrderItem;
import entity.Product;
import lib.errors.DataNotFoundException;
import lib.errors.InsufficientStockException;
import repository.OrderRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private OrderRepository orderRepository = new OrderRepository();
    private ProductService productService = ProductService.getInstance();
    private List<Order> orderList;
    private static OrderService orderService = new OrderService();

    private OrderService() {
        orderList = orderRepository.read();
    }

    public static OrderService getInstance() {
        return orderService;
    }

    public void addItem(Order order, Product product, int quantity) {
        for (OrderItem item : order.getOrderItems()) {
            if (item.getProductId() == product.getId()) {
                int newQuantity = item.getQuantity() + quantity;
                if (newQuantity > product.getStock()) {
                    throw new InsufficientStockException();
                } else {
                    item.setQuantity(newQuantity);
                    return;
                }
            }
        }
        if (quantity > product.getStock()) {
            throw new InsufficientStockException();
        } else {
            order.getOrderItems().add(new OrderItem(order.getOrderId(), product.getId(), quantity, product.getPrice()));
        }
    }

    public Order createOrder(long customerId) {
        long id = System.currentTimeMillis() / 1000L;
        return new Order(id, customerId, LocalDate.now(), 0, new ArrayList<>());
    }

    public void checkout(Order order) {
        if (order.getOrderItems().isEmpty()) {
            throw new IllegalArgumentException("Order has no items");
        }
        double totalPrice = 0;
        for (OrderItem item : order.getOrderItems()) {
            Product product = productService.findById(item.getProductId());
            product.setStock(product.getStock() - item.getQuantity());
            productService.update(product.getId(), product);
            totalPrice += item.getTotal();
        }
        order.setTotalPrice(totalPrice);
        orderList.add(order);
        orderRepository.write(orderList);
    }

    public List<Order> getAll() {
        return this.orderList;
    }

    public Order findById(long id) {
        for (Order order : orderList) {
            if (order.getOrderId() == id) {
                return order;
            }
        }
        throw new DataNotFoundException();
    }
}
