package budget;

public class Purchase {
    private String purchase;
    private Double price;
    private PurchaseCategory purchaseCategory;

    Purchase(String purchase, Double price, PurchaseCategory purchaseCategory) {
        this.purchase = purchase;
        this.price = price;
        this.purchaseCategory = purchaseCategory;
    }

    public String getPurchase() {
        return purchase;
    }

    public void setPurchase(String purchase) {
        this.purchase = purchase;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public PurchaseCategory getPurchaseCategory() {
        return purchaseCategory;
    }

    public void setPurchaseCategory(PurchaseCategory purchaseCategory) {
        this.purchaseCategory = purchaseCategory;
    }
}
