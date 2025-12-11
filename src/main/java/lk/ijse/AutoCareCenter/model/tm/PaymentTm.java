package lk.ijse.AutoCareCenter.model.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaymentTm {
    private String id;
    private String OrderId;
    private String code;
    private int qty;
    private double unitPrice;
    private double service_charge;
    private double Total;
    private String description;
    private String date;
}
