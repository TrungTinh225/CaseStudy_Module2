package validator;

import lib.InputHelper;

import java.util.regex.Pattern;

public class CategoryValidator {
    public static String inputCategoryName(){
        String nameRegex = "^(?=.*\\p{L})[\\p{L} ]{2,30}$";
        while (true){
            String categoryName = InputHelper.inputString().trim();
            if (Pattern.matches(nameRegex, categoryName)){
                return categoryName;
            }
            System.out.println("Invalid name (2-30 letters, no digits or special characters)");
        }
    }
}
