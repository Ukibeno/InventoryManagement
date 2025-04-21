package InventoryManagement.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Supplier extends User{

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user; // Link to the User

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "supplier")
    private List<Order> orders;

}
