package service;

import entity.Product;
import lib.errors.DataNotFoundException;
import repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;

public class ProductService implements IService<Product> {
    private ProductRepository productRepository = new ProductRepository();
    private List<Product> productList;
    private static final ProductService productService = new ProductService();

    private ProductService() {
        productList = productRepository.read();
    }

    public static ProductService getInstance(){
        return productService;
    }

    @Override
    public void add(Product product) {
        productList.add(product);
        productRepository.write(productList);
    }

    @Override
    public void delete(long id) {
        int index = findIndexById(id);
        this.productList.remove(index);
        productRepository.write(productList);
    }

    @Override
    public void update(long id, Product product) {
        int index = findIndexById(id);
        this.productList.set(index, product);
        productRepository.write(productList);
    }

    @Override
        public List<Product> getAll() {
        return productList;
    }

    @Override
    public int findIndexById(long id) {
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getId() == id) {
                return i;
            }
        }
        throw new DataNotFoundException();
    }

    public List<Product> sortByPrice() {
        List<Product> sortedList = new ArrayList<>(productList);
        for (int i = 0; i < sortedList.size() - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < sortedList.size(); j++) {
                if (sortedList.get(j).getPrice() < sortedList.get(minIndex).getPrice()) {
                    minIndex = j;
                }
            }
            Product temp = sortedList.get(i);
            sortedList.set(i, sortedList.get(minIndex));
            sortedList.set(minIndex, temp);
        }
        return sortedList;
    }

    @Override
    public Product findById(long id) {
        int index = findIndexById(id);
        return productList.get(index);
    }
}
