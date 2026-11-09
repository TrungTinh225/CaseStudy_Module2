package repository;

import entity.Customer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository implements IRepository<Customer> {

    private final static String FILE_NAME = "data/customers.csv";

    @Override
    public void write(List<Customer> list) {
        String dataStr = "";
        for (Customer item : list) {
            dataStr += item.getId() + ","+item.getName()+ ","+ item.getPhone() +"\n";
        }
        try {
            FileWriter fileWriter = new FileWriter(FILE_NAME);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(dataStr);
            bufferedWriter.close();
        }catch (IOException e){
            System.out.println("[CustomerRepository.write.error]" + e.getMessage());
        }
    }

    @Override
    public List<Customer> read() {
        List<Customer> list = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(FILE_NAME);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                String[] data = line.split(",");
                long id = Long.parseLong(data[0]);
                String name = data[1];
                String phone = data[2];
                Customer customer = new Customer(id, name, phone);
                list.add(customer);
            }
            bufferedReader.close();
            return list;
        }catch (IOException e){
            System.out.println("[CustomerRepository.read.error]" + e.getMessage());
        }
        return list;
    }
}
