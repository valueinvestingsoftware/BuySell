package x.stocks.valueinvesting.Models;

public class PricesModel {
            int id;
            int ListId;
            int ItemId;
            Double Price;

    public PricesModel(Integer id, Integer listId, Integer itemId, Double price) {
        this.id = id;
        ListId = listId;
        ItemId = itemId;
        Price = price;
    }

    @Override
    public String toString() {
        return "PricesModel{" +
                "id=" + id +
                ", ListId=" + ListId +
                ", ItemId=" + ItemId +
                ", Price=" + Price +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getListId() {
        return ListId;
    }

    public void setListId(Integer listId) {
        ListId = listId;
    }

    public Integer getItemId() {
        return ItemId;
    }

    public void setItemId(Integer itemId) {
        ItemId = itemId;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }
}
