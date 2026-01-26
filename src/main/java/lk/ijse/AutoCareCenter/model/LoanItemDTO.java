package lk.ijse.AutoCareCenter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanItemDTO {
    private String loanId;
    private String itemName;
    private String code;
    private int qty;
    private double unitPrice;

    public LoanItemDTO(String loanId, String itemName, int qty) {
        this.loanId = loanId;
        this.itemName = itemName;
        this.qty = qty;
    }

    public LoanItemDTO(String loanId, String itemName, String code, int qty) {
        this.loanId = loanId;
        this.itemName = itemName;
        this.code = code;
        this.qty = qty;
    }
}
