package Buffers_Product;


public class Product {
    private PTypes productType;
    public Product(PTypes productType){
        this.productType = productType;
    }
    public PTypes getType() {
        return this.productType;
    }
}
