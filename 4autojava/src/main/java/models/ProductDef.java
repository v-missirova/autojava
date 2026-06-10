package models;

import annotations.GenerateJson;

@GenerateJson(className = "Product")
public class ProductDef {
    public int productId;
    public String productName;
    public double price;
}