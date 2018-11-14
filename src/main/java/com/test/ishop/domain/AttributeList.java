package com.test.ishop.domain;

import javax.persistence.*;

@Entity
@Table(name = "attr_list")
public class AttributeList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String value;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="attribute_id", insertable=false, updatable=false)
    private Attribute attribute;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }
}
