/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package theshedcrud;

/**
 *
 * @author Brett
 */
public class Bikes {
    private int id;
    private String name;
    private String brand;
    private String type;
    private String desc;
    private double price;
    private byte[] image;

    public void setImage(byte[] image) {
        this.image = image;
    }

    public byte[] getImage() {
        return image;
    }
    
    
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }
    public String getDesc() {
        return desc;
    }
    public Bikes(int id, String name, String brand, String type,String desc, double price,byte[] image) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.type = type;
        this.desc = desc;
        this.price = price;
        this.image = image;
    }
    
    
}
