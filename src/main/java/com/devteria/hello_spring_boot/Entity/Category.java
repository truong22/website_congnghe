package com.devteria.hello_spring_boot.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Category {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    private String nameCategory;

    @JsonIgnore
    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Products> products;
    public Category(String nameCategory){
        this.nameCategory=nameCategory;
    }

}
