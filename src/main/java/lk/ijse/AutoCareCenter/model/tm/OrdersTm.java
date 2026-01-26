package lk.ijse.AutoCareCenter.model.tm;

import com.jfoenix.controls.JFXButton;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrdersTm {
    private String code;
    private String description;
    private int qty;
    private double unitPrice;
    private double total;
    private JFXButton btnRemove;
    private double service_charge;

    public OrdersTm(String code, String description, int qty, double unitPrice, double total, JFXButton btnRemove) {
        this.code = code;
        this.description = description;
        this.qty = qty;
        this.unitPrice = unitPrice;
        this.total = total;
        this.btnRemove = btnRemove;
    }

    public OrdersTm(String code, String itemName, int qty) {
        this.code = code;
        this.description = itemName;
        this.qty = qty;
    }
}
