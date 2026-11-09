package repository;

import entity. Order;
import entity.OrderItem;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OrderRepository implements IRepository<Order>{
    private final static String ORDER_FILE = "data/orders.csv";
    private final static String ITEM_FILE = "data/orderItems.csv";

    @Override
    public void write(List< Order> list) {
        String orderData = "";
        String itemData = "";
        for (Order item : list) {
            orderData += item.getOrderId() + ","+item.getCustomerId()+ "," +item.getDate()+ ","+ item.getTotalPrice() +"\n";
            for (OrderItem orderItem : item.getOrderItems()) {
                itemData += orderItem.getOrderId() + ","+orderItem.getProductId()+","+orderItem.getQuantity() + "," + orderItem.getPrice() + "\n";
            }
        }
        writeFile(ORDER_FILE, orderData);
        writeFile(ITEM_FILE, itemData);
    }

    private void writeFile(String fileName, String content){
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(content);
            bufferedWriter.close();
        }catch (IOException e){
            System.out.println("[OrderRepository.write.error]" + e.getMessage());
        }

    }

    private List<String> readLines(String fileName) {
        List<String> lines = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("[OrderRepository.readLines.error]" + e.getMessage());
        }
        return lines;
    }

    @Override
    public List<Order> read() {
        Map<Long, Order> orderMap = new LinkedHashMap<>();

        for (String line : readLines(ORDER_FILE)) {
            String[] data = line.split(",");
            long id = Long.parseLong(data[0]);
            long customerId = Long.parseLong(data[1]);
            LocalDate date = LocalDate.parse(data[2]);
            double totalPrice = Double.parseDouble(data[3]);
            orderMap.put(id, new Order(id, customerId, date, totalPrice, new ArrayList<>()));
        }

        for (String line : readLines(ITEM_FILE)) {
            String[] data = line.split(",");
            long orderId = Long.parseLong(data[0]);
            long productId = Long.parseLong(data[1]);
            int quantity = Integer.parseInt(data[2]);
            double price = Double.parseDouble(data[3]);
            Order order = orderMap.get(orderId);
            if (order != null) {
                OrderItem orderItem = new OrderItem(orderId, productId, quantity, price);
                order.getOrderItems().add(orderItem);
            }
        }

        return new ArrayList<>(orderMap.values());
    }

    public static void main(String[] args) {
        OrderRepository repository = new OrderRepository();

        // 1. Tạo dữ liệu mẫu: 2 đơn, đơn 1 có 2 dòng hàng, đơn 2 có 1 dòng hàng
//        List<Order> list = new ArrayList<>();
//        Order order1 = new Order(5001, 111, LocalDate.of(2026, 11, 8), 13000, new ArrayList<>());
//        order1.getOrderItems().add(new OrderItem(5001, 1, 2, 2000));
//        order1.getOrderItems().add(new OrderItem(5001, 2, 3, 3000));
//        Order order2 = new Order(5002, 222, LocalDate.of(2026, 11, 8), 8000, new ArrayList<>());
//        order2.getOrderItems().add(new OrderItem(5002, 1, 4, 2000));
//        list.add(order1);
//        list.add(order2);
//
//        // 2. Test WRITE
//        repository.write(list);

        // 3. Test READ: đọc lại từ file và in ra
//        List<Order> readList = repository.read();
//        for (Order order : readList) {
//            System.out.println("Order " + order.getOrderId() + " | customer " + order.getCustomerId()
//                    + " | " + order.getDate() + " | total " + order.getTotalPrice());
//            for (OrderItem item : order.getOrderItems()) {
//                System.out.println("    product " + item.getProductId() + " x" + item.getQuantity()
//                        + " = " + item.getTotal());
//            }
//        }
    }
}
