package validator;

import lib.InputHelper;

import java.util.regex.Pattern;

public class CustomerValidator {
    public static String inputCustomerName(){
        String nameRegex = "^(?=.*\\p{L})[\\p{L} ]{5,30}$";
        while (true){
            String customerName = InputHelper.inputString().trim();
            if (Pattern.matches(nameRegex, customerName)){
                return customerName;
            }
            System.out.println("Invalid name (5-30 letters, no digits or special characters)");
        }
    }

    public static String inputPhoneNumber(){
        String phoneRegex = "^[0-9]{10}$";
        while (true){
            String phoneNumber = InputHelper.inputString();
            if (Pattern.matches(phoneRegex, phoneNumber)){
                return phoneNumber;
            }
            System.out.println("Phone is invalid.");
        }
    }
}
