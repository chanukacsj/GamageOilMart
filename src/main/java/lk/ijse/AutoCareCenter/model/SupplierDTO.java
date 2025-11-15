package lk.ijse.AutoCareCenter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class SupplierDTO {
    private String id;
    private String name;
    private String contact;
    private String address;

    public SupplierDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return name; // ComboBox එකේ display වෙන්න
    }
}
