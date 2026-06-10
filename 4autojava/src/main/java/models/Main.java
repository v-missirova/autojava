package models;

import annotations.JsonSerializer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
       try {
            Product product = new Product(131, "Pear syrup", 149.99);

            String jsonResult = JsonSerializer.toJson(product);

            Path filePath = Paths.get("product_data.json");

            Files.writeString(
                   filePath,
                   jsonResult,
                   StandardOpenOption.CREATE,
                   StandardOpenOption.TRUNCATE_EXISTING
           );

           System.out.println("Json created in: " + filePath.toAbsolutePath());

       } catch (Exception e) {
           e.fillInStackTrace();
       }

        List<Product> productList = new ArrayList<>();

        productList.add(new Product(101, "Apple", 72.42));
        productList.add(new Product(102, "Boots", 53.123));
        productList.add(new Product(103, "Lotion", 31.53));

        try {
            String jsonArrayResult = JsonSerializer.toJson(productList);

            Path filePath = Paths.get("products_list.json");
            Files.writeString(
                    filePath,
                    jsonArrayResult,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

            System.out.println("JSON created in: " + filePath.toAbsolutePath());

        } catch (Exception e) {
            e.fillInStackTrace();
        }

    }
}