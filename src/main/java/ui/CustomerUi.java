package ui;

import entity.Customer;
import lib.InputHelper;
import lib.errors.DataNotFoundException;
import service.CustomerService;
import validator.CustomerValidator;

import java.util.List;

public class CustomerUi {
    private static final CustomerService customerService = CustomerService.getInstance();

    public static void showMainMenu(){
        int choice;
        do {
            System.out.println("====== Customer Manager Menu =======");
            System.out.println("1. Add");
            System.out.println("2. Edit");
            System.out.println("3. Delete");
            System.out.println("4. Get All");
            System.out.println("0. Back");
            System.out.println("Enter your choice: ");
            choice = InputHelper.inputInt();
            switch (choice) {
                case 1:
                    addUI();
                    break;
                case 2:
                    editUI();
                    break;
                case 3:
                    deleteUI();
                    break;
                case 4:
                    getAllUI();
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
        System.out.println("===== Add Customer ======");
        long id = System.currentTimeMillis() / 1000L;
        System.out.println("Enter Customer name: ");
        String customerName = CustomerValidator.inputCustomerName();
        System.out.println("Enter Customer phone: ");
        String phone = CustomerValidator.inputPhoneNumber();
        Customer customer = new Customer(id, customerName, phone);
        customerService.add(customer);
    }

    public static void editUI(){
        System.out.println("===== Edit Customer ======");
        System.out.println("Enter Customer ID: ");
        long id = InputHelper.inputLong();
        try {
            Customer customer = customerService.findById(id);
            System.out.println("Enter Customer name(old name: " + customer.getName() + "): ");
            String customerName = CustomerValidator.inputCustomerName();
            customer.setName(customerName);
            System.out.println("Enter Customer phone(old phone: "  + customer.getPhone() + "): ");
            String phone = CustomerValidator.inputPhoneNumber();
            customer.setPhone(phone);
            customerService.update(id, customer);
            System.out.println("Customer updated successfully");
        }catch (DataNotFoundException e){
            System.out.println("Invalid Customer ID");
        }
    }

    public static void deleteUI(){
        System.out.println("===== Delete Customer ======");
        System.out.println("Enter Customer ID: ");
        long id = InputHelper.inputLong();
        try {
            Customer customer = customerService.findById(id);
            System.out.println("Do you want to delete this Customer: " + customer.getName() + "? (y/n)");
            String answer = InputHelper.inputString();
            if (answer.equalsIgnoreCase("y")) {
                customerService.delete(id);
                System.out.println("Customer Deleted");
            }
        }catch (DataNotFoundException e){
            System.out.println("Customer not found");
        }

    }

    public static void getAllUI(){
        System.out.println("===== List Customer ======");
        List<Customer> list = customerService.getAll();
        int count = 1;
        for (Customer customer : list) {
            System.out.println(count + ". Id: " + customer.getId() + " Name: " + customer.getName() +  " Phone: " + customer.getPhone());
            count++;
        }
    }
}
