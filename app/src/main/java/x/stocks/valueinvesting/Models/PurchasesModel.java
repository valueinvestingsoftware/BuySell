package x.stocks.valueinvesting.Models;

public class PurchasesModel {
    private int Id;
    private int ItemId;
    private int SupplierId;
    private Double PurchaseQuantity;
    private Double PurchasePrice;
    private String PurchaseDate;
    private String Observations;
    private Boolean PurchaseInApp;
    private Boolean Sincronizado;

    @Override
    public String toString() {
        return "PurchasesModel{" +
                "Id=" + Id +
                ", ItemId=" + ItemId +
                ", SupplierId=" + SupplierId +
                ", PurchaseQuantity=" + PurchaseQuantity +
                ", PurchasePrice=" + PurchasePrice +
                ", PurchaseDate='" + PurchaseDate + '\'' +
                ", Observations='" + Observations + '\'' +
                ", PurchaseInApp=" + PurchaseInApp + '\'' +
                ", Sincronizado=" + Sincronizado +
                '}';
    }

    public PurchasesModel(int id, int itemId, int supplierId, Double purchaseQuantity, Double purchasePrice, String purchaseDate, String observations, Boolean purchaseInApp, Boolean sincronizado) {
        Id = id;
        ItemId = itemId;
        SupplierId = supplierId;
        PurchaseQuantity = purchaseQuantity;
        PurchasePrice = purchasePrice;
        PurchaseDate = purchaseDate;
        Observations = observations;
        PurchaseInApp = purchaseInApp;
        Sincronizado = sincronizado;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getItemId() {
        return ItemId;
    }

    public void setItemId(int itemId) {
        ItemId = itemId;
    }

    public int getSupplierId() {
        return SupplierId;
    }

    public void setSupplierId(int supplierId) {
        SupplierId = supplierId;
    }

    public Double getPurchaseQuantity() {
        return PurchaseQuantity;
    }

    public void setPurchaseQuantity(Double purchaseQuantity) {
        PurchaseQuantity = purchaseQuantity;
    }

    public Double getPurchasePrice() {
        return PurchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        PurchasePrice = purchasePrice;
    }

    public String getPurchaseDate() {
        return PurchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        PurchaseDate = purchaseDate;
    }

    public String getObservations() {
        return Observations;
    }

    public void setObservations(String observations) {
        Observations = observations;
    }

    public Boolean getPurchaseInApp() {
        return PurchaseInApp;
    }

    public void setPurchaseInApp(Boolean purchaseInApp) {
        PurchaseInApp = purchaseInApp;
    }

    public Boolean getSincronizado() {
        return Sincronizado;
    }

    public void setSincronizado(Boolean sincronizado) {
        Sincronizado = sincronizado;
    }
}
