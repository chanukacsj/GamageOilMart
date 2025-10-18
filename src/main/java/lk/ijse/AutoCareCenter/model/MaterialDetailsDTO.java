package lk.ijse.AutoCareCenter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MaterialDetailsDTO {
    private String code;
    private String supId;
    private String description;
    private double unitPrice;
    private int qtyOnHand;

}