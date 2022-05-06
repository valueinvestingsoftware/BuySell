package x.stocks.valueinvesting.Models;

import android.text.TextUtils;

public class ViewPurchasesSalesModel {
    String itemNameCardP, supplierCardP, Observations;
    Integer quantityCardP, purchaseId;
    Double priceCardP;
    private byte[] Image;

    public ViewPurchasesSalesModel(Integer purchaseId, String itemNameCardP, Integer quantityCardP, Double priceCardP, String supplierCardP, byte[] image, String observations) {
        this.purchaseId = purchaseId;
        this.itemNameCardP = itemNameCardP;
        this.quantityCardP = quantityCardP;
        this.priceCardP = priceCardP;
        this.supplierCardP = supplierCardP;
        this.Image = image;
        this.Observations = observations;
    }

    public Integer getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Integer purchaseId) {
        this.purchaseId = purchaseId;
    }

    public byte[] getImage() {
        return Image;
    }

    public void setImage(byte[] image) {
        Image = image;
    }

    public String getSupplierCardP() {
        return supplierCardP;
    }

    public void setSupplierCardP(String supplierCardP) {
        this.supplierCardP = supplierCardP;
    }

    public String getItemNameCardP() {
        return itemNameCardP;
    }

    public void setItemNameCardP(String itemNameCardP) {
        this.itemNameCardP = itemNameCardP;
    }

    public Integer getQuantityCardP() {
        return quantityCardP;
    }

    public void setQuantityCardP(Integer quantityCardP) {
        this.quantityCardP = quantityCardP;
    }

    public Double getPriceCardP() {
        return priceCardP;
    }

    public void setPriceCardP(Double priceCardP) {
        this.priceCardP = priceCardP;
    }

    public String getObservations() {return Observations;}

    public void setObservations(String observations) {Observations = observations;}
}
