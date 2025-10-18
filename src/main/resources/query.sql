CREATE TABLE customerDTOS (
                           id varchar(15) PRIMARY KEY,
                           name varchar (20),
                           contact varchar (15),
                           address varchar (50)
);


CREATE TABLE orders(
                       orderId varchar (15)PRIMARY KEY,
                       id varchar(15) not null,
                       date varchar(20) not null,
                       bId varchar(15),
                       constraint FOREIGN KEY (bId) REFERENCES Booking(bId)on delete cascade on update cascade,
                       constraint FOREIGN KEY (id) REFERENCES customerDTOS(id)on delete cascade on update cascade
);

CREATE TABLE materialsDTO(
    code varchar(35) primary key
);

CREATE TABLE order_details(
                              orderId varchar(15)not null,
                              code varchar(15)not null,
                              qty int(8)not null,
                              unit_price double(10,2)not null,
service_charge double(10,2),
total double(10,2),
constraint FOREIGN KEY (code) REFERENCES materialsDTO(code)on delete cascade on update cascade,
constraint FOREIGN KEY (orderId) REFERENCES orders(orderId)on delete cascade on update cascade
);


CREATE TABLE suppliers(
                          supId varchar (15) PRIMARY KEY,
                          supName varchar(20),
                          contact varchar(15),
                          address varchar(50)
);

CREATE TABLE material_details(
                                 code varchar (35),
                                 supId varchar (15),
                                 description varchar(35),
                                 unit_price double(10,2),
qty_on_hand int(8),
constraint FOREIGN KEY (code) REFERENCES materialsDTO(code)on delete  cascade on update cascade,
constraint FOREIGN KEY (supId) REFERENCES suppliers(supId)on delete  cascade on update cascade
);

CREATE TABLE vehicleDTO(
                        vId varchar (15) PRIMARY KEY,
                        type varchar(20),
                        number varchar(20),
                        id varchar(15),
                        constraint FOREIGN KEY (id) REFERENCES customerDTOS(id)on delete  cascade on update cascade
);


CREATE TABLE Employee(
                         empId varchar (15)PRIMARY KEY,
                         empName varchar(20),
                         contact varchar(15),
                         address varchar(50)
);

CREATE TABLE Booking(
                        bId varchar  (15)PRIMARY KEY,
                        date varchar(50),
                        description varchar(20),
                        contact varchar (10),
                        id varchar(15),
                        vId varchar(15),
                        time varchar(15),
                        constraint FOREIGN KEY (id) REFERENCES customerDTOS(id)on delete  cascade on update cascade,
                        constraint FOREIGN KEY (vId) REFERENCES vehicleDTO(vId)on delete  cascade on update cascade
);


CREATE TABLE salaryDTO(
                       sId varchar  (15)PRIMARY KEY,
                       amount int(20),
                       month varchar (12),
                       empId varchar(15),
                       constraint FOREIGN KEY (empId) REFERENCES Employee(empId)on delete  cascade on update cascade
);

CREATE TABLE repairAndService(
                                 rid varchar  (15)PRIMARY KEY,
                                 startTime varchar(20),
                                 endTime varchar (20),
                                 description varchar (50),
                                 empId varchar(15),
                                 vId varchar(15),
                                 constraint FOREIGN KEY (vId) REFERENCES vehicleDTO(vId)on delete  cascade on update cascade,
                                 constraint FOREIGN KEY (empId) REFERENCES Employee(empId)on delete  cascade on update cascade
);

create table users(
                      user_id varchar(35) primary key ,
                      password varchar(155) not null
);

