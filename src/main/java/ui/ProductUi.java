package ui;

import entity.Category;
import entity.Product;
import lib.InputHelper;
import lib.errors.DataNotFoundException;
import service.CategoryService;
import service.ProductService;
import validator.ProductValidator;

import java.util.List;


public class ProductUi {
    private static final ProductService productService = ProductService.getInstance();
    private static final CategoryService categoryService = CategoryService.getInstance();

    public static void showMainMenu()
    {
        int choice;
        do {
            System.out.println("====== Product Manager");
            System.out.println("1. Add");
            System.out.println("2. Edit");
            System.out.println("3. Delete");
            System.out.println("4. Get All");
            System.out.println("5. Search By Name");
            System.out.println("6. Sort By Price");
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
                case 5:
                    searchByName();
                    break;
                case 6:
                    sortByPriceUI();
                    break;
                case 0:
                    System.out.println("Back to main menu");
                    break;
                default:
                    System.out.println("Invalid choice");

            }

        }while (choice != 0);
    }

    public static void searchByName()
    {
        System.out.println("==== Search By Name ====");
        System.out.println("Enter Name: ");
        String searchName = InputHelper.inputString();
        List<Product> list = productService.getAll();
        int count = 1;
        for (Product item : list) {
            String categoryName = "(Unknown)";
            try {
                categoryName = categoryService.findById(item.getCategoryId()).getName();
            }catch (DataNotFoundException e){
            }
            if (item.getName().toLowerCase().contains(searchName.trim().toLowerCase())) {
                System.out.println(count + ". Id: " + item.getId() + " Name: " + item.getName() + " Price: " + item.getPrice() + " stock: " + item.getStock() + " category: " + categoryName);
                count++;
            }
        }
        if (count == 1) {
            System.out.println("No product found");
        }
    }

    public static void sortByPriceUI()
    {
        System.out.println("==== Sort By Price (ascending) ====");
        List<Product> sortedList = productService.sortByPrice();
        int count = 1;
        for (Product item : sortedList) {
            String categoryName = "(Unknown)";
            try {
                categoryName = categoryService.findById(item.getCategoryId()).getName();
            }catch (DataNotFoundException e){
            }
            System.out.println(count + ". Id: " + item.getId() + " Name: " + item.getName() + " Price: " + item.getPrice() + " Stock: " + item.getStock() + " Category: " + categoryName);
            count++;
        }
    }

    public static void addUI()
    {
        System.out.println("===== Add Product ======");
        long id = System.currentTimeMillis() / 1000L;
        System.out.println("Enter Product name: ");
        String productName = ProductValidator.inputProductName();
        System.out.println("Enter Product price: ");
        double price = ProductValidator.inputProductPrice();
        System.out.println("Enter Product stock: ");
        int stock = ProductValidator.inputProductStock();
        long categoryId = inputCategoryId();
        Product product = new Product(id, productName, price, stock, categoryId);
        productService.add(product);
    }

    public static long inputCategoryId()
    {
        System.out.println("===== List Category ======");
        List<Category> categories = categoryService.getAll();
        int count = 1;
        for (Category category : categories) {
            System.out.println(count + ". Id: " + category.getId() + " Name: " + category.getName());
            count++;
        }
        System.out.println("Enter category Id: ");
        while (true){
            long categoryId = InputHelper.inputLong();
            for(Category item : categories){
                if(item.getId() == categoryId){
                    return categoryId;
                }
            }
            System.out.println("Invalid category id, please try again");
        }


    }

    public static void deleteUI(){
        System.out.println("===== Delete Product ======");
        System.out.println("Enter Product Id Delete: ");
        long id = InputHelper.inputLong();
        try {
            Product product = productService.findById(id);
            System.out.println("Do you want to delete this product ?" +product.getName() + " (y/n?)");
            String answer = InputHelper.inputString();
            if (answer.equalsIgnoreCase("y")) {
                productService.delete(id);
                System.out.println("Product Deleted");
            }
        }catch (DataNotFoundException e){
            System.out.println("Product not found");
        }
    }

    public static void editUI(){
        System.out.println("===== Edit Product ======");
        System.out.println("Enter Product Id: ");
        long id = InputHelper.inputLong();
        try {
            Product product = productService.findById(id);
            System.out.println("Enter New Product name(old name: "+product.getName()+"): ");
            String productName = ProductValidator.inputProductName();
            product.setName(productName);
            System.out.println("Enter New Product price(old price: "+product.getPrice()+"): ");
            double productPrice = ProductValidator.inputProductPrice();
            product.setPrice(productPrice);
            System.out.println("Enter New Product stock(old stock: "+product.getStock()+"): ");
            int productStock = ProductValidator.inputProductStock();
            product.setStock(productStock);
            long categoryId = inputCategoryId();
            product.setCategoryId(categoryId);
            productService.update(id, product);
            System.out.println("Product Updated");
        }catch (DataNotFoundException e){
            System.out.println("Product not found");
        }
    }

    public static void getAllUI()
    {
        System.out.println("==== List of Products ===");
        List<Product> list = productService.getAll();
        int count = 1;
        for (Product product : list) {
            String categoryName = "(Unknown)";
            try {
                categoryName = categoryService.findById(product.getCategoryId()).getName();
            }catch (DataNotFoundException e){
            }
            System.out.println(count + ". ID: " + product.getId()+ " Name: " + product.getName() + " Price: " + product.getPrice() + " Stock: " + product.getStock() + " Category: " + categoryName);
            count++;
        }
    }
}
