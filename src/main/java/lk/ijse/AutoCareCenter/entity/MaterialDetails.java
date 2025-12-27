package lk.ijse.AutoCareCenter.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MaterialDetails {
    private String code;
    private String supId;
    private String description;
    private double unitPrice;
    private int qtyOnHand;
    private String category;
    private String brand;
    private String addedDate;
    private String status;
    private String barcode;

    public MaterialDetails(String code, String supId, String description, double unitPrice, int qtyOnHand, String category, String brand, String addedDate, String status) {

        this.code = code;
        this.supId = supId;
        this.description = description;
        this.unitPrice = unitPrice;
        this.qtyOnHand = qtyOnHand;
        this.category = category;
        this.brand = brand;
        this.addedDate = addedDate;
        this.status = status;
    }
}