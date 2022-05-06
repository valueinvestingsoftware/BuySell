package x.stocks.valueinvesting.Models;

public class PriceListsModel {
    int id;
    String Category;
    String Tipo;

    public PriceListsModel(Integer id, String category, String tipo) {
        this.id = id;
        Category = category;
        Tipo = tipo;
    }

    @Override
    public String toString() {
        return "PriceLists{" +
                "id='" + id + '\'' +
                ", Category='" + Category + '\'' +
                ", Tipo='" + Tipo + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }
}
