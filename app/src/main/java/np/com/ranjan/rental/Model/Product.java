package np.com.ranjan.rental.Model;

import java.io.Serializable;

public class Product implements Serializable {
    private String id;
    private String owner;
    private String name;
    private String cost;
    private String category;
    private String description;
    private String photo;

    /**
     * Parameterized constructor
     * @param id id of product
     * @param owner id of user who uploaded the product
     * @param name name of product
     * @param cost cost of product
     * @param category product category
     * @param description product description
     * @param photo product photo
     */
    public Product(String id, String owner, String name, String cost, String category, String description, String photo) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.cost = cost;
        this.category = category;
        this.description = description;
        this.photo = photo;
    }


    /**
     * Non parameterized constructor
     */
    public Product() {
    }

    public String getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public String getCost() {
        return cost;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
