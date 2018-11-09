package com.test.ishop.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "attribute")
public class Attribute {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private char type;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name="category_id", nullable = false)
    private Category category;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinColumn(name="attribute_id", nullable = false)
    private Set<ProductAttribute> values = new HashSet<ProductAttribute>();

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name="attribute_id", nullable = true)
    private Set<AttributeList> list = new HashSet<AttributeList>();

    public Attribute() {
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", category=" + category +
                ", values=" + values +
                ", list=" + list +
                '}';
    }

    public Set<AttributeList> getList() {
        return list;
    }

    public void setList(Set<AttributeList> list) {
        this.list = list;
    }

    public Long getId() {
        return id;
    }

    public Set<ProductAttribute> getValues() {
        return values;
    }

    public void setValues(Set<ProductAttribute> values) {
        this.values = values;
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

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
