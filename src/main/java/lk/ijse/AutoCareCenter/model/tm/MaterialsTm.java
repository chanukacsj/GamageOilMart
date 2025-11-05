package lk.ijse.AutoCareCenter.model.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class MaterialsTm {
    private String code;
    private String supId;
    private String description;
    private double unitPrice;
    private int qtyOnHand;
    private String category;
    private String brand;
    private String addedDate;
    private String status;

}
