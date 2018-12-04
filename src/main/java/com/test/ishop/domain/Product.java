package com.test.ishop.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product")
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private Double price;
    private Integer quantity;
    @Enumerated(EnumType.ORDINAL)
    private ProdStatus status;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name="category_id", nullable = false)
    private Category category;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name="product_id", nullable = false)
    private Set<ProductAttribute> values = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "products")
    private Set<Cart> carts;


    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", status=" + status +
                ", category=" + category.getId() +
                ", values=" + values +
                ", carts=?" +
                '}';
    }

    public String attributesList() {
        StringBuilder result= new StringBuilder();
        for(ProductAttribute p:values) {
            result.append(p.getAttribute().getName()).append(": ").append(p.getAttribute().getType() == 'S' ? p.getValueStr() : p.getValueNum().toString()).append(", ");
        }
        if (result.length()>0) result = new StringBuilder(result.substring(0, result.length() - 2));
        return result.toString();
    }

    public Set<Cart> getCarts() {
        return carts;
    }

    public void setCarts(Set<Cart> carts) {
        this.carts = carts;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<ProductAttribute> getValues() {
        return values;
    }

    public void setValues(Set<ProductAttribute> values) {
        this.values = values;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ProdStatus getStatus() {
        return status;
    }

    public void setStatus(ProdStatus status) {
        this.status = status;
    }
}
