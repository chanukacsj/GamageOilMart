package lk.ijse.AutoCareCenter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrdersDTO {
    private String orderId;
    private String cusId;
    private Date date;
    private String bId;
    List<OrderDetailsDTO> orderDetails;



}
