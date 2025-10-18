package lk.ijse.AutoCareCenter.model.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookingTm {
    private String id;
    private String date;
    private String description;
    private String contact;
    private String cusId;
    private String vId;
    private String time;
}
