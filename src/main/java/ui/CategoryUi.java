package ui;

import entity.Category;
import lib.InputHelper;
import lib.errors.DataNotFoundException;
import service.CategoryService;
import validator.CategoryValidator;

import java.util.List;

public class CategoryUi {
    private static final CategoryService categoryService = CategoryService.getInstance();

    public static void showMainMenu(){
        int choice;
        do {
            System.out.println("====== Category Manager Menu =======");
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
        System.out.println("===== Add Category ======");
        long id = System.currentTimeMillis() / 1000L;
        System.out.println("Enter category name: ");
        String categoryName = CategoryValidator.inputCategoryName();
        Category category = new Category(id, categoryName);
        categoryService.add(category);
    }

    public static void editUI(){
        System.out.println("===== Edit Category ======");
        System.out.println("Enter category ID: ");
        long id = InputHelper.inputLong();
        try {
            Category category = categoryService.findById(id);
            System.out.println("Enter category name(old name: " + category.getName() + "): ");
            String categoryName = CategoryValidator.inputCategoryName();
            category.setName(categoryName);
            categoryService.update(id, category);
            System.out.println("Category updated successfully");
        }catch (DataNotFoundException e){
            System.out.println("Invalid category ID");
        }
    }

    public static void deleteUI(){
        System.out.println("===== Delete Category ======");
        System.out.println("Enter category ID: ");
        long id = InputHelper.inputLong();
        try {
            Category category = categoryService.findById(id);
            System.out.println("Do you want to delete this category: " + category.getName() + "? (y/n)");
            String answer = InputHelper.inputString();
            if (answer.equalsIgnoreCase("y")) {
                categoryService.delete(id);
                System.out.println("Category Deleted");
            }
        }catch (DataNotFoundException e){
            System.out.println("Category not found");
        }

    }

    public static void getAllUI(){
        System.out.println("===== List Categories ======");
        List<Category> list = categoryService.getAll();
        int count = 1;
        for (Category category : list) {
            System.out.println(count + ". Id: " + category.getId() + " Name: " + category.getName());
            count++;
        }
    }
}
