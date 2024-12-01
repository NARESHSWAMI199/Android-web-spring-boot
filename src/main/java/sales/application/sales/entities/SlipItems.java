package sales.application.sales.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Column(name = "slip_id")
    Integer slipId;
    @Column(name = "item_order_id")
    Integer itemOrderId;
}