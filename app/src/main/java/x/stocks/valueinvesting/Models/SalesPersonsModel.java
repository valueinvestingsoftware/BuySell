package x.stocks.valueinvesting.Models;

public class SalesPersonsModel {
    private int id;
    private String name;

    @Override
    public String toString() {
        return "SalesPersonsModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public SalesPersonsModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
