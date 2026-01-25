package lk.ijse.AutoCareCenter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class OrderDetailsDTO  implements Serializable {
    private String OrderId;
    private String code;
    private int qty;
    private double unitPrice;
    private double service_charge;
    private double Total;
    private double discount;
}
