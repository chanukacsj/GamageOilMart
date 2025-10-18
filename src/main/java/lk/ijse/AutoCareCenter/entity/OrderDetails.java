package lk.ijse.AutoCareCenter.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class OrderDetails {
    private String OrderId;
    private String code;
    private int qty;
    private double unitPrice;
    private double service_charge;
    private double Total;
}
