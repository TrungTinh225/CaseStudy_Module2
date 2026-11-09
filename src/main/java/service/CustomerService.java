package service;

import entity.Customer;
import lib.errors.DataNotFoundException;
import repository.CustomerRepository;

import java.util.List;

public class CustomerService implements IService<Customer> {
    private CustomerRepository customerRepository = new CustomerRepository();
    private List<Customer> customerList;
    private static final CustomerService customerService = new CustomerService();

    private CustomerService() {
        customerList = customerRepository.read();
    }

    public static CustomerService getInstance() {
        return customerService;
    }

    @Override
    public void add(Customer customer) {
        this.customerList.add(customer);
        this.customerRepository.write(customerList);
    }

    @Override
    public void delete(long id) {
        int index = findIndexById(id);
        this.customerList.remove(index);
        this.customerRepository.write(customerList);
    }

    @Override
    public void update(long id, Customer customer) {
        int index = findIndexById(id);
        this.customerList.set(index, customer);
        this.customerRepository.write(customerList);
    }

    @Override
    public List<Customer> getAll() {
        return this.customerList;
    }

    @Override
    public int findIndexById(long id) {
        for (int i = 0; i < customerList.size(); i++) {
            if (customerList.get(i).getId() == id) {
                return i;
            }
        }
        throw new DataNotFoundException();
    }

    @Override
    public Customer findById(long id) {
        int index = findIndexById(id);
        return customerList.get(index);
    }
}
