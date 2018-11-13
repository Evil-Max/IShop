package com.test.ishop.domain;

import javax.persistence.*;

@Entity
@Table(name = "product_attribute")
@Embeddable
public class ProductAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "value_s")
    private String valueStr;
    @Column(name = "value_n")
    private Double valueNum;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="product_id", insertable=false, updatable=false)
    private Product product;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="attribute_id", insertable=false, updatable=false)
    private Attribute attribute;

    public ProductAttribute() {
    }

    @Override
    public String toString() {
        return "ProductAttribute{" +
                "id=" + id +
                ", valueStr='" + valueStr + '\'' +
                ", valueNum=" + valueNum +
                ", product=" + product +
                ", attribute=" + attribute +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValueStr() {
        return valueStr;
    }

    public void setValueStr(String valueStr) {
        this.valueStr = valueStr;
    }

    public Double getValueNum() {
        return valueNum;
    }

    public void setValueNum(Double valueNum) {
        this.valueNum = valueNum;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }
}
