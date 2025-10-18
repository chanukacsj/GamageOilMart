package lk.ijse.AutoCareCenter.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Orders {
    private String orderId;
    private String cusId;
    private Date date;
    private String bId;
}
