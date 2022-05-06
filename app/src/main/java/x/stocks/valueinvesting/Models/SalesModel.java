package x.stocks.valueinvesting.Models;

public class SalesModel {

    private int Id;
    private int ItemId;
    private int ClientId;
    private Double SaleQuantity;
    private Double SalePrice;
    private String SaleDate;
    private String Observations;
    private Double Profit;
    private Boolean SoldInApp;
    private Boolean Sincronizado;

    public SalesModel(int id, int itemId, int clientId, Double saleQuantity, Double salePrice, String saleDate, String observations, Double profit, Boolean soldInApp, Boolean sincronizado) {
        Id = id;
        ItemId = itemId;
        ClientId = clientId;
        SaleQuantity = saleQuantity;
        SalePrice = salePrice;
        SaleDate = saleDate;
        Observations = observations;
        Profit = profit;
        SoldInApp = soldInApp;
        Sincronizado = sincronizado;
    }

    @Override
    public String toString() {
        return "SalesModel{" +
                "Id=" + Id +
                ", ItemId=" + ItemId +
                ", ClientId=" + ClientId +
                ", SaleQuantity=" + SaleQuantity +
                ", SalePrice=" + SalePrice +
                ", SaleDate='" + SaleDate + '\'' +
                ", Observations='" + Observations + '\'' +
                ", Profit=" + Profit +
                ", SoldInApp=" + SoldInApp +
                ", Sincronizado=" + Sincronizado +
                '}';
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

    public int getClientId() {
        return ClientId;
    }

    public void setClientId(int clientId) {
        ClientId = clientId;
    }

    public Double getSaleQuantity() {
        return SaleQuantity;
    }

    public void setSaleQuantity(Double saleQuantity) {
        SaleQuantity = saleQuantity;
    }

    public Double getSalePrice() {
        return SalePrice;
    }

    public void setSalePrice(Double salePrice) {
        SalePrice = salePrice;
    }

    public String getSaleDate() {
        return SaleDate;
    }

    public void setSaleDate(String saleDate) {
        SaleDate = saleDate;
    }

    public String getObservations() {
        return Observations;
    }

    public void setObservations(String observations) {
        Observations = observations;
    }

    public Double getProfit() {
        return Profit;
    }

    public void setProfit(Double profit) {
        Profit = profit;
    }

    public Boolean getSoldInApp() {
        return SoldInApp;
    }

    public void setSoldInApp(Boolean soldInApp) {
        SoldInApp = soldInApp;
    }

    public Boolean getSincronizado() {
        return Sincronizado;
    }

    public void setSincronizado(Boolean sincronizado) {
        Sincronizado = sincronizado;
    }
}
