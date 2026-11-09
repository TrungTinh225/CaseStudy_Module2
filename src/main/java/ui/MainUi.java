package ui;

import lib.InputHelper;

public class MainUi {
    public static void showMainMenu() {
        int choice;
        do {
            System.out.println("====== Main Menu =======");
            System.out.println("1. Category Manager");
            System.out.println("2. Product Manager");
            System.out.println("3. Customer Manager");
            System.out.println("4. Order Manager");
            System.out.println("0. Exit");
            System.out.println("Enter your choice: ");
            choice = InputHelper.inputInt();
            switch (choice) {
                case 1:
                    CategoryUi.showMainMenu();
                    break;
                case 2:
                    ProductUi.showMainMenu();
                    break;
                case 3:
                    CustomerUi.showMainMenu();
                    break;
                case 4:
                    OrderUi.showMainMenu();
                    break;
                case 0:
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice");

            }
        }while (choice != 0);
    }
}
