package x.stocks.valueinvesting.Models;

import java.util.Date;

public class BuySellModel {
    private int id;
    private int ItemId;
    private String Cod;
    private String PurchaseDate;
    private String SaleDate;
    private Double PurchasePrice;
    private int PurchaseQuantity;
    private Double SalePrice;
    private int SaleQuantity;
    private int AvailableQuantity;
    private String Clase;
    private String Categoria;
    private String Subcategoria;
    private String Tipo;
    private String SubTipo;
    private Double Profit;

    public BuySellModel(int id, int itemId, String cod, String purchaseDate, String saleDate, Double purchasePrice, int purchaseQuantity, Double salePrice, int saleQuantity, int availableQuantity, String clase, String categoria, String subcategoria, String tipo, String subTipo, Double profit) {
        this.id = id;
        ItemId = itemId;
        Cod = cod;
        PurchaseDate = purchaseDate;
        SaleDate = saleDate;
        PurchasePrice = purchasePrice;
        PurchaseQuantity = purchaseQuantity;
        SalePrice = salePrice;
        SaleQuantity = saleQuantity;
        AvailableQuantity = availableQuantity;
        Clase = clase;
        Categoria = categoria;
        Subcategoria = subcategoria;
        Tipo = tipo;
        SubTipo = subTipo;
        Profit = profit;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItemId() {
        return ItemId;
    }

    public void setItemId(int itemId) {
        ItemId = itemId;
    }

    public String getCod() {
        return Cod;
    }

    public void setCod(String cod) {
        Cod = cod;
    }

    public String getPurchaseDate() {
        return PurchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        PurchaseDate = purchaseDate;
    }

    public String getSaleDate() {
        return SaleDate;
    }

    public void setSaleDate(String saleDate) {
        SaleDate = saleDate;
    }

    public Double getPurchasePrice() {
        return PurchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        PurchasePrice = purchasePrice;
    }

    public int getPurchaseQuantity() {
        return PurchaseQuantity;
    }

    public void setPurchaseQuantity(int purchaseQuantity) {
        PurchaseQuantity = purchaseQuantity;
    }

    public Double getSalePrice() {
        return SalePrice;
    }

    public void setSalePrice(Double salePrice) {
        SalePrice = salePrice;
    }

    public int getSaleQuantity() {
        return SaleQuantity;
    }

    public void setSaleQuantity(int saleQuantity) {
        SaleQuantity = saleQuantity;
    }

    public int getAvailableQuantity() {
        return AvailableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        AvailableQuantity = availableQuantity;
    }

    public String getClase() {
        return Clase;
    }

    public void setClase(String clase) {
        Clase = clase;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public String getSubcategoria() {
        return Subcategoria;
    }

    public void setSubcategoria(String subcategoria) {
        Subcategoria = subcategoria;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    public String getSubTipo() {
        return SubTipo;
    }

    public void setSubTipo(String subTipo) {
        SubTipo = subTipo;
    }

    public Double getProfit() {
        return Profit;
    }

    public void setProfit(Double profit) {
        Profit = profit;
    }

}
