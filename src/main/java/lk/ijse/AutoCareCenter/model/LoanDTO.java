package lk.ijse.AutoCareCenter.model;

import com.jfoenix.controls.JFXButton;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanDTO {
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
    private List<LoanItemDTO> items;

    public LoanDTO(String loanId, String orderId, String customerName, String phone, double total, double paid, double remaining, String nextDue, String status, String date) {
        this.loanId = loanId;
        this.orderId = orderId;
        this.customerName = customerName;
        this.phone = phone;
        this.total = total;
        this.paid = paid;
        this.remaining = remaining;
        this.nextDue = nextDue;
        this.status = status;
        this.date = date;
    }
}
