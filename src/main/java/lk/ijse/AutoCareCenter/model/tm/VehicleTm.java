package lk.ijse.AutoCareCenter.model.tm;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class VehicleTm {
    private String id;
    private String type;
    private String number;
    private String cusId;

}