package com.intellias.testmarketplace.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "products")
public class Product {
    private UUID id;
    private String name;
    private BigDecimal price;
    private Set<User> users;

    @Id
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    @Column(name = "id", columnDefinition = "uuid")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Column(name = "name")
    @NotEmpty
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "price")
    @DecimalMin(message = "{price.min.value}", value = "0.0", inclusive = false)
    @Digits(message = "{price.wrong.number}", integer = 10, fraction = 2)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @ManyToMany(mappedBy = "products", cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}