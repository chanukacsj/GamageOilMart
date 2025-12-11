package lk.ijse.AutoCareCenter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class LoanPaymentDTO {

    private String paymentId;
    private String loanId;
    private double amount;
    private LocalDate date;

}