package ui;

import entity.Customer;
import entity.Order;
import entity.OrderItem;
import entity.Product;
import lib.InputHelper;
import lib.errors.DataNotFoundException;
import lib.errors.InsufficientStockException;
import service.CustomerService;
import service.OrderService;
import service.ProductService;


import java.util.List;

public class OrderUi {
    private static final OrderService orderService = OrderService.getInstance();
    private static final CustomerService customerService = CustomerService.getInstance();
    private static final ProductService productService = ProductService.getInstance();
    
    public static void showMainMenu(){
        int choice;
        do {
            System.out.println("====== Order Manager Menu =======");
            System.out.println("1. Add");
            System.out.println("2. Show All");
            System.out.println("3. Show Detail");
            System.out.println("0. Back");
            System.out.println("Enter your choice: ");
            choice = InputHelper.inputInt();
            switch (choice) {
                case 1:
                    addUI();
                    break;
                case 2:
                    showAllUI();
                    break;
                case 3:
                    showDetailUI();
                    break;
                case 0:
                    System.out.println("Back to main menu");
                    break;
                default:
                    System.out.println("Invalid choice");

            }
        }while (choice != 0);
    }

    public static void addUI(){
        System.out.println("====== Add Order =======");
        if (customerService.getAll().isEmpty()) {
            System.out.println("No customers yet. Please add a customer first.");
            return;
        }
        if (productService.getAll().isEmpty()) {
            System.out.println("No products yet. Please add a product first.");
            return;
        }

        long customerId = inputCustomerId();
        Order order = orderService.createOrder(customerId);

        System.out.println("===== List Product ======");
        for (Product product : productService.getAll()) {
            System.out.println("Id: " + product.getId() + " Name: " + product.getName() + " Price: " + product.getPrice() + " Stock: " + product.getStock());
        }

        while (true) {
            System.out.println("Enter product id (0 = finish): ");
            long productId = InputHelper.inputLong();
            if (productId == 0) {
                break;
            }
            Product product;
            try {
                product = productService.findById(productId);
            } catch (DataNotFoundException e) {
                System.out.println("Product not found");
                continue;
            }
            System.out.println("Enter quantity: ");
            int quantity = InputHelper.inputInt();
            if (quantity <= 0) {
                System.out.println("Quantity must be greater than 0");
                continue;
            }
            try {
                orderService.addItem(order, product, quantity);
                System.out.println("Added " + quantity + " x " + product.getName());
            } catch (InsufficientStockException e) {
                System.out.println("Not enough stock (available: " + product.getStock() + ")");
            }
        }

        if (order.getOrderItems().isEmpty()) {
            System.out.println("Order cancelled: no items");
            return;
        }
        orderService.checkout(order);
        System.out.println("Order created. Id: " + order.getOrderId() + " Total: " + order.getTotalPrice());
    }

    private static long inputCustomerId() {
        System.out.println("===== List Customer ======");
        for (Customer customer : customerService.getAll()) {
            System.out.println("Id: " + customer.getId() + " Name: " + customer.getName() + " Phone: " + customer.getPhone());
        }
        while (true) {
            System.out.println("Enter customer id: ");
            long customerId = InputHelper.inputLong();
            try {
                customerService.findById(customerId);
                return customerId;
            } catch (DataNotFoundException e) {
                System.out.println("Customer not found, please try again");
            }
        }
    }

    private static String getCustomerName(long customerId){
        String customerName = "(Unknown)";
        try {
            customerName = customerService.findById(customerId).getName();
        }catch (DataNotFoundException e){
        }
        return customerName;
    }

    private static String getProductName(long productId){
        String productName = "(Unknown)";
        try {
            productName = productService.findById(productId).getName();
        }catch (DataNotFoundException e){
        }
        return productName;
    }

    public static void showAllUI(){
        System.out.println("====== List Order =======");
        List<Order> list = orderService.getAll();
        int count = 1;
        if (list.isEmpty()) {
            System.out.println("No orders yet");
            return;
        }
        for (Order item : list) {
            System.out.println(count + ". Id: " + item.getOrderId()+ ". Customer Name: " + getCustomerName(item.getCustomerId())+ ". Date: "+item.getDate()+ ". Total Price: " + item.getTotalPrice());
            count++;
        }
    }

    public static void showDetailUI(){
        System.out.println("====== Order Detail =======");
        System.out.println("Enter Order Id: ");
        long orderId = InputHelper.inputLong();
        try {
            Order order = orderService.findById(orderId);
            System.out.println("Order: " + order.getOrderId());
            System.out.println("Customer: " + getCustomerName(order.getCustomerId()));
            System.out.println("Date: " + order.getDate());
            String line = "------------------------------------------------------------";
            System.out.println(line);
            System.out.printf("%-4s %-20s %8s %12s %12s%n", "No", "Product", "Qty", "Price", "Total");
            System.out.println(line);
            int count = 1;
            for (OrderItem item : order.getOrderItems()) {
                System.out.printf("%-4d %-20s %8d %,12.0f %,12.0f%n",
                        count, getProductName(item.getProductId()), item.getQuantity(), item.getPrice(), item.getTotal());
                count++;
            }
            System.out.println(line);
            System.out.printf("Total: %,.0f%n", order.getTotalPrice());
        }catch (DataNotFoundException e){
            System.out.println("Order not found");
        }

    }
}

