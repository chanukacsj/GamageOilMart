package lk.ijse.AutoCareCenter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class SalaryDTO {
    private String id;
    private int amount;
    private String month;
    private String empId;
}
