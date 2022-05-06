package x.stocks.valueinvesting.Models;

public class ItemSalesDataModel {
    String itemname;
    Integer itemvalue;

    public ItemSalesDataModel(String itemname, Integer itemvalue) {
        this.itemname = itemname;
        this.itemvalue = itemvalue;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public Integer getItemvalue() {
        return itemvalue;
    }

    public void setItemvalue(Integer itemvalue) {
        this.itemvalue = itemvalue;
    }
}
