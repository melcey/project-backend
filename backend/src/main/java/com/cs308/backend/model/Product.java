package com.cs308.backend.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

// The entity Product
// Corresponds to the `product` table in the database
@Entity
@Table(name = "products", schema = "public")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "model", length = 255)
    private String model;

    @Column(name = "serial_number", length = 255)
    private String serialNumber;

    @Column(name = "description")
    private String description;

    @Column(name = "quantity_in_stock", nullable = false)
    private int quantityInStock;

    // Precision: The total number of digits which can be stored in the number
    // Scale: The number of digits to the right of the decimal point
    // => In the price variable, numbers up to 99999999.99 can be stored
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    // BigDecimal is used as the data type in order to preserve the precision
    private BigDecimal price;

    @Column(name = "warranty_status", length = 255)
    private String warrantyStatus;

    @Column(name = "distributor_info")
    private String distributorInfo;

    @Column(name = "is_active")
    // The default value in the table is true
    private Boolean isActive = true;

    @Column(name = "image_url")
    private String imageUrl;

    // Many-to-one association with the Category class
    @ManyToOne
    // The foreign key category_id is joined to determine the category of the product
    @JoinColumn(name = "category_id")
    private Category category;

    // Many-to-one association with the User class (for product managers)
    @ManyToOne
    // The foreign key product_manager_id is joined to determine the manager of the product
    @JoinColumn(name = "product_manager_id")
    private User productManager;

    public Product() {}

    public Product(String name, String model, String serialNumber, String description, int quantityInStock,
            BigDecimal price, String warrantyStatus, String distributorInfo, Boolean isActive, String imageUrl) {
        this.name = name;
        this.model = model;
        this.serialNumber = serialNumber;
        this.description = description;
        this.quantityInStock = quantityInStock;
        this.price = price;
        this.warrantyStatus = warrantyStatus;
        this.distributorInfo = distributorInfo;
        this.isActive = isActive;
        this.imageUrl = imageUrl;
    }

    public Product(Long id, String name, String model, String serialNumber, String description, int quantityInStock,
            BigDecimal price, String warrantyStatus, String distributorInfo, Boolean isActive, String imageUrl,
            Category category, User productManager) {
        this.id = id;
        this.name = name;
        this.model = model;
        this.serialNumber = serialNumber;
        this.description = description;
        this.quantityInStock = quantityInStock;
        this.price = price;
        this.warrantyStatus = warrantyStatus;
        this.distributorInfo = distributorInfo;
        this.isActive = isActive;
        this.imageUrl = imageUrl;
        this.category = category;
        this.productManager = productManager;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getWarrantyStatus() {
        return warrantyStatus;
    }

    public void setWarrantyStatus(String warrantyStatus) {
        this.warrantyStatus = warrantyStatus;
    }

    public String getDistributorInfo() {
        return distributorInfo;
    }

    public void setDistributorInfo(String distributorInfo) {
        this.distributorInfo = distributorInfo;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public User getProductManager() {
        return productManager;
    }

    public void setProductManager(User productManager) {
        this.productManager = productManager;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Product [id=").append(id)
            .append(", name=").append(name)
            .append(", model=").append(model)
            .append(", serialNumber=").append(serialNumber)
            .append(", description=").append(description)
            .append(", quantityInStock=").append(quantityInStock)
            .append(", price=").append(price)
            .append(", warrantyStatus=").append(warrantyStatus)
            .append(", distributorInfo=").append(distributorInfo)
            .append(", isActive=").append(isActive)
            .append(", imageUrl=").append(imageUrl)
            .append(", category=").append(category)
            .append(", productManager=").append(productManager.toString())
            .append("]");
            
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Product other = (Product) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    
}
