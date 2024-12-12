package sales.application.sales.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Table(name = "slip_items")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SlipItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "slip_id",referencedColumnName = "id")
    Slip slip;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_order_id",referencedColumnName = "id")
    List<ItemOrder> itemOrders;
}
