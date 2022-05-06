package x.stocks.valueinvesting.Models;

public class CustomerModel {
    private int id;
    private String contacto;
    private boolean supplier;
    private String address;
    private String city;
    private String telefono;
    private String email;
    private String comment;
    private Double latitude;
    private Double longitude;
    private String creationDate;
    private String updateDate;
    private boolean createdInApp;
    private boolean Sincronizado;
    private byte[] Image;

    @Override
    public String toString() {
        return "CustomerModel{" +
                "id=" + id +
                ", contacto='" + contacto + '\'' +
                ", supplier=" + supplier +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", email='" + email + '\'' +
                ", comment='" + comment + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", creationDate='" + creationDate + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", createdInApp=" + createdInApp + '\'' +
                ", Sincronizado=" + Sincronizado +
                '}';
    }

    public CustomerModel(int id, String contacto, boolean supplier, String address, String city, String telefono, String email, String comment, Double latitude, Double longitude, String creationDate, String updateDate, boolean createdInApp, boolean sincronizado, byte[] image) {
        this.id = id;
        this.contacto = contacto;
        this.supplier = supplier;
        this.address = address;
        this.city = city;
        this.telefono = telefono;
        this.email = email;
        this.comment = comment;
        this.latitude = latitude;
        this.longitude = longitude;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
        this.createdInApp = createdInApp;
        this.Sincronizado = sincronizado;
        this.Image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSupplier() {
        return supplier;
    }

    public void setSupplier(boolean supplier) {
        this.supplier = supplier;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComment() {
        return comment;
    }

    public boolean isSincronizado() {
        return Sincronizado;
    }

    public void setSincronizado(boolean sincronizado) {
        Sincronizado = sincronizado;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public boolean isCreatedInApp() {
        return createdInApp;
    }

    public void setCreatedInApp(boolean createdInApp) {
        this.createdInApp = createdInApp;
    }

    public byte[] getImage() {
        return Image;
    }

    public void setImage(byte[] image) {
        Image = image;
    }
}
