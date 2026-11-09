package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private long orderId;
    private long customerId;
    private LocalDate date;
    private double totalPrice;
    private List<OrderItem> orderItems = new ArrayList<>();

    public Order(long orderId, long customerId, LocalDate date, double totalPrice,  List<OrderItem> orderItems) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.date = date;
        this.totalPrice = totalPrice;
        this.orderItems = orderItems;
    }

    public Order() {
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalprice) {
        this.totalPrice = totalprice;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderitems) {
        this.orderItems = orderitems;
    }
}
