package InventoryManagement.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Supplier extends User{
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


}
