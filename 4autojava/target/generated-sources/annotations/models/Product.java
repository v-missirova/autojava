package models;

public class Product {
@annotations.JsonKey("product_id")
public int productId;

@annotations.JsonKey("product_name")
public java.lang.String productName;

@annotations.JsonKey("price")
public double price;

public Product() {}

public Product(int productId, java.lang.String productName, double price) {
this.productId = productId;
this.productName = productName;
this.price = price;
}
}
