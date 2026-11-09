package service;

import entity.Category;
import lib.errors.DataNotFoundException;
import repository.CategoryRepository;

import java.util.List;

public class CategoryService implements IService<Category> {
    private CategoryRepository categoryRepository = new CategoryRepository();
    private List<Category> categoryList;
    private static final CategoryService categoryService = new CategoryService();

    private CategoryService() {
        categoryList = categoryRepository.read();
    }

    public static CategoryService getInstance() {
        return categoryService;
    }

    @Override
    public void add(Category category) {
        this.categoryList.add(category);
        this.categoryRepository.write(categoryList);
    }

    @Override
    public void delete(long id) {
        int index = findIndexById(id);
        this.categoryList.remove(index);
        this.categoryRepository.write(categoryList);
    }

    @Override
    public void update(long id, Category category) {
        int index = findIndexById(id);
        this.categoryList.set(index, category);
        this.categoryRepository.write(categoryList);
    }

    @Override
    public List<Category> getAll() {
        return this.categoryList;
    }

    @Override
    public int findIndexById(long id) {
        for (int i = 0; i < categoryList.size(); i++) {
            if (categoryList.get(i).getId() == id) {
                return i;
            }
        }
        throw new DataNotFoundException();
    }

    @Override
    public Category findById(long id) {
        int index = findIndexById(id);
        return categoryList.get(index);
    }
}


