package lk.ijse.AutoCareCenter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class RepairDTO {
    private String rId;
    private String startTime;
    private String endTime;
    private String description;
    private String empId;
    private String vId;
}
