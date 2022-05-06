package x.stocks.valueinvesting.Models;

import java.sql.Blob;

public class ItemsModel {

    private int Id;
    private String Cod;
    private String Category;
    private Integer Nivel;
    private String CreationDate;
    private String UpdateDate;
    private byte[] Image;
    private Boolean CatMapInApp;
    private Boolean ImagenAnadidaEnApp;
    private Boolean Sincronizado;

    @Override
    public String toString() {
        return "ItemsModel{" +
                "Id=" + Id +
                ", Cod='" + Cod + '\'' +
                ", Category='" + Category + '\'' +
                ", Nivel='" + Nivel + '\'' +
                ", CreationDate='" + CreationDate + '\'' +
                ", UpdateDate='" + UpdateDate + '\'' +
                ", Image=" + Image + '\'' +
                ", CreatedInApp=" + CatMapInApp + '\'' +
                ", ImagenAnadidaEnApp=" + ImagenAnadidaEnApp + '\'' +
                ", Sincronizado=" + Sincronizado +
                '}';
    }

    public ItemsModel(int id, String cod, String category, Integer nivel, String creationDate, String updateDate, byte[] image, Boolean catMapInApp, Boolean imagenAnadidaEnApp, Boolean sincronizado) {
        this.Id = id;
        this.Cod = cod;
        this.Category = category;
        this.Nivel = nivel;
        this.CreationDate = creationDate;
        this.UpdateDate = updateDate;
        this.Image = image;
        this.CatMapInApp = catMapInApp;
        this.ImagenAnadidaEnApp = imagenAnadidaEnApp;
        this.Sincronizado = sincronizado;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getCategory() {
        return Category;
    }

    public String getCod() {
        return Cod;
    }

    public void setCod(String cod) {
        Cod = cod;
    }

    public Integer getNivel() {
        return Nivel;
    }

    public void setNivel(Integer nivel) {
        Nivel = nivel;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getCreationDate() {
        return CreationDate;
    }

    public void setCreationDate(String creationDate) {
        CreationDate = creationDate;
    }

    public String getUpdateDate() {
        return UpdateDate;
    }

    public void setUpdateDate(String updateDate) {
        UpdateDate = updateDate;
    }

    public byte[] getImage() {return Image; }

    public void setImage(byte[] image) {
        Image = image;
    }

    public Boolean getCatMapInApp() {
        return CatMapInApp;
    }

    public void setCatMapInApp(Boolean catMapInApp) {
        CatMapInApp = catMapInApp;
    }

    public Boolean getImagenAnadidaEnApp() {
        return ImagenAnadidaEnApp;
    }

    public void setImagenAnadidaEnApp(Boolean imagenAnadidaEnApp) {
        ImagenAnadidaEnApp = imagenAnadidaEnApp;
    }

    public Boolean getSincronizado() {
        return Sincronizado;
    }

    public void setSincronizado(Boolean sincronizado) {
        Sincronizado = sincronizado;
    }
}
