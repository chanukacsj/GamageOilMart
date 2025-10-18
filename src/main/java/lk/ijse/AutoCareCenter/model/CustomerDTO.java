package lk.ijse.AutoCareCenter.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class CustomerDTO implements Serializable { // this model class represent real world customer entity
    private String id;
    private String name;
    private String address;
    private String contact;


}