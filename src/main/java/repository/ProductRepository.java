package repository;

import entity.Product;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository implements IRepository<Product> {
    private final static String FILE_NAME = "data/products.csv";

    @Override
    public void write(List<Product> list) {
        String dataStr = "";
        for (Product product : list) {
            dataStr = dataStr + product.getId() + "," + product.getName() + "," + product.getPrice() + "," + product.getStock() + "," + product.getCategoryId() + "\n" ;
        }
        try {
            FileWriter fileWriter = new FileWriter(FILE_NAME);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(dataStr);
            bufferedWriter.close();
        }catch(IOException e) {
            System.out.println("[ProductRepository.write.erros: ]" + e.getMessage());
        }
    }

    @Override
    public List<Product> read() {
        List<Product> list = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(FILE_NAME);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {break;}
                String[] data = line.split(",");
                long id = Long.parseLong(data[0]);
                String name = data[1];
                double price = Double.parseDouble(data[2]);
                int stock = Integer.parseInt(data[3]);
                long categoryId = Long.parseLong(data[4]);
                Product product = new Product(id,name,price,stock,categoryId);
                list.add(product);
            }
            bufferedReader.close();
            return list;
        }catch(IOException e) {
            System.out.println("[ProductRepository.read.erros: ]" + e.getMessage());
        }
        return list;
    }
}
