package validator;

import lib.InputHelper;

import java.util.regex.Pattern;

public class ProductValidator {
    public static String inputProductName(){
        String nameRegex = "^(?=.*\\p{L})[\\p{L}0-9 ]{2,30}$";
        while (true){
            String productName = InputHelper.inputString().trim();
            if (Pattern.matches(nameRegex, productName)){
                return productName;
            }
            System.out.println("Invalid name (2-30 letters, no special characters)");
        }
    }

    public static double inputProductPrice(){
        while (true){
            double price = InputHelper.inputDouble();
            if (price > 0){
                return price;
            }
            System.out.println("Price must be a positive number");
        }
    }

    public static int inputProductStock(){
        while (true){
            int stock = InputHelper.inputInt();
            if (stock >= 0){
                return stock;
            }
            System.out.println("Stock must be a positive number");
        }
    }
}
