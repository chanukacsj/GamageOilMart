package lk.ijse.AutoCareCenter.entity;

import com.jfoenix.controls.JFXButton;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Loan {
    private String loanId;
    private String orderId;
    private String customerName;
    private String phone;
    private double total;
    private double paid;
    private double remaining;
    private String nextDue;
    private String status;
    private String date;

}
