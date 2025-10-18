package lk.ijse.AutoCareCenter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class BookingDTO {
    private String id;
    private String date;
    private String description;
    private String contact;
    private String cusId;
    private String vId;
    private String time;
}
