package lk.ijse.AutoCareCenter.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class ChequePayment {
    private String chequeId;
    private String paymentId;
    private String customerName;
    private String customerPhone;
    private String chequeNo;
    private String bankName;
    private String branch;
    private String chequeDate;
    private double amount;
    private String status;
}
