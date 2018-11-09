package com.test.ishop.domain;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //private boolean active; // true = active

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name="client_id", nullable = false)
    private Client client;

    @ManyToMany
    @JoinTable(name="cart_product",
            joinColumns = @JoinColumn(name="cart_id", referencedColumnName="id"),
            inverseJoinColumns = @JoinColumn(name="product_id", referencedColumnName="id"))
    private Set<Product> products;

    @OneToOne(optional = true, mappedBy="cart")
    private Order order;

    public Cart() {
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", client=" + client +
                ", products=" + products +
                ", order=" + order +
                '}';
    }

    public Double getCartSum() {
        Double sum=0d;
        if (products==null) return 0d;
        for(Product p:getProducts())
            sum+=p.getPrice();
        return sum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
