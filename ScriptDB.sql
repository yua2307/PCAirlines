drop database if exists PCAirlinesLan5;
create database PCAirlinesLan5;
use PCAirlinesLan5;
create table AirBrand(
airBrandId int not null auto_increment,
nameAirBrand varchar(45),
primary key (airBrandId)
);

create table TicketType(
ticketTypeId int not null auto_increment,
ticketTypeName varchar(45),
rate double,
primary key (ticketTypeId)
);

create table TicketClass(
ticketClassId int not null auto_increment,
ticketClassName varchar(45),
rate double,
primary key (ticketClassId));

create table CreditCard(
creditCardId int not null auto_increment,
type varchar(45),
creditCardNumber varchar(45),
balance double,
nameOnCard varchar(45),
primary key (creditCardId)
);


create table Customer(
customerId int not null auto_increment,
name varchar(45),
phoneNumber varchar(45),
identifyNumber varchar(45),
email varchar(45),
username varchar(45) not null,
password varchar(45) not null,
address varchar(45),
enabled TINYINT NOT NULL DEFAULT 1 ,
primary key (customerId)
);

CREATE TABLE user_roles (
  user_role_id int(11) NOT NULL AUTO_INCREMENT,
  customerId int not null,
  username varchar(45) 	not null,	
  role varchar(45) NOT NULL,
  PRIMARY KEY (user_role_id),
 UNIQUE KEY uni_username_role (role,username),
  KEY fk_customerId_idx (customerId),
  CONSTRAINT fk_customerId FOREIGN KEY (customerId) REFERENCES Customer(customerId)
  );


create table AirCraft(
airCraftId int not null auto_increment,
airBrandId int,
airCraftName varchar(45),
model varchar(45),
seatNumber int,
primary key (airCraftId),
foreign key (airBrandId) references AirBrand(airBrandId)
);

create table Booking(
bookingId int not null auto_increment,
numberTicketBuy int,
totalMoney double,
creditCardId int,
customerId int,
paymentDate DATETIME,
primary key (bookingId),
foreign key (creditCardId) references CreditCard(creditCardId),
foreign key (customerId) references Customer(customerId)
);
-- doan ni phai tao 3 table airport, route, flight nua moi tao dc table Ticket
create table Airport(
	airportId int not null auto_increment,
    airportName varchar(45),
    cityName varchar(45),
    primary key (airportId)
);
create table FlightRoute(
	routeId int not null auto_increment,
    departurePlaceId int ,
	arrivalPlaceId int ,
    standardPrice double,
    primary key (routeId),
	foreign key (departurePlaceId) references Airport(airportId),
	foreign key (arrivalPlaceId) references Airport(airportId)
);
create table Flight(
	flightId int not null auto_increment,
    departureTime DATETIME,
    arrivalTime DATETIME,
    totalSeat int ,
    availableSeat int ,
    orderedSeat int ,
    duration double,
    routeId int,
    airCraftId int ,
    primary key (flightId),
    foreign key (routeId) references FlightRoute(routeId),
    foreign key (airCraftId) references AirCraft(airCraftId)
);
 create table Ticket(
 ticketId int not null auto_increment,
 price double,
 dateBuyTicket datetime,
 seat int,
  namePersonBuyTicket varchar(45),
 identify varchar(45),
 bookingId int,
 flightId int,
 ticketClassId int,
 ticketTypeId int,
 primary key (ticketId),
 foreign key (bookingId) references Booking(bookingId),
 foreign key (flightId) references Flight(flightId),
 foreign key (ticketClassId) references TicketClass(ticketClassId),
 foreign key (ticketTypeId) references TicketType(ticketTypeId)
 );
 
 INSERT INTO `pcairlineslan5`.`airbrand` (`nameAirBrand`) VALUES ('Vietnamairlines');
INSERT INTO `pcairlineslan5`.`airbrand` (`nameAirBrand`) VALUES ('Vietjet');
INSERT INTO `pcairlineslan5`.`airbrand` (`nameAirBrand`) VALUES ('BambooAirway');

INSERT INTO `pcairlineslan5`.`aircraft` (`airBrandId`, `airCraftName`, `model`, `seatNumber`) VALUES ('1', 'VN001', 'B52', '200');
INSERT INTO `pcairlineslan5`.`aircraft` (`airBrandId`, `airCraftName`, `model`, `seatNumber`) VALUES ('1', 'VN002', 'B52', '200');
INSERT INTO `pcairlineslan5`.`aircraft` (`airBrandId`, `airCraftName`, `model`, `seatNumber`) VALUES ('1', 'VN003', 'B52', '200');
INSERT INTO `pcairlineslan5`.`aircraft` (`airBrandId`, `airCraftName`, `model`, `seatNumber`) VALUES ('2', 'VJ001', 'B52', '200');
INSERT INTO `pcairlineslan5`.`aircraft` (`airBrandId`, `airCraftName`, `model`, `seatNumber`) VALUES ('2', 'VJ002', 'B52', '200');
INSERT INTO `pcairlineslan5`.`aircraft` (`airBrandId`, `airCraftName`, `model`, `seatNumber`) VALUES ('2', 'VJ003', 'B52', '200');
INSERT INTO `pcairlineslan5`.`aircraft` (`airBrandId`, `airCraftName`, `model`, `seatNumber`) VALUES ('3', 'QH001', 'B52', '200');
INSERT INTO `pcairlineslan5`.`aircraft` (`airBrandId`, `airCraftName`, `model`, `seatNumber`) VALUES ('3', 'QH002', 'B52', '200');
INSERT INTO `pcairlineslan5`.`aircraft` (`airBrandId`, `airCraftName`, `model`, `seatNumber`) VALUES ('3', 'QH003', 'B52', '200');

INSERT INTO `pcairlineslan5`.`airport` (`airportName`, `cityName`) VALUES ('Tan Son Nhat', 'Thanh pho Ho Chi Minh');
INSERT INTO `pcairlineslan5`.`airport` (`airportName`, `cityName`) VALUES ('Noi Bai', 'Thu do Ha Noi');
INSERT INTO `pcairlineslan5`.`airport` (`airportName`, `cityName`) VALUES ('Da Nang', 'Thanh pho Da Nang');

INSERT INTO `pcairlineslan5`.`creditcard` (`type`, `creditCardNumber`, `balance`, `nameOnCard`) VALUES ('VISA', '123456789', '10000000', 'Tran Diep Phuong');
INSERT INTO `pcairlineslan5`.`creditcard` (`type`, `creditCardNumber`, `balance`, `nameOnCard`) VALUES ('VISA', '987654321', '10000000', 'Duong Van Chinh');

INSERT INTO `pcairlineslan5`.`customer` (`name`, `phoneNumber`, `identifyNumber`, `email`, `username`, `password`, `address`, `enabled`) VALUES ('Tran Diep Phuong', '0354892727', '201835650', 'dphuong15032000@gmail.com', 'phuong15032000', '15032000', 'Da Nang', true);
INSERT INTO `pcairlineslan5`.`customer` (`name`, `phoneNumber`, `identifyNumber`, `email`, `username`, `password`, `address`, `enabled`) VALUES ('Duong Van Chinh', '0935204877', '201817047', 'yua2307@gmail.com', 'yua2307', 'chinhdeptrai', 'Da Nang', true);


INSERT INTO user_roles (customerId, username, role)
VALUES ('2', 'yua2307','ROLE_USER');
INSERT INTO user_roles (customerId, username, role)
VALUES ('1', 'phuong15032000','ROLE_ADMIN');

INSERT INTO `pcairlineslan5`.`ticketclass` (`ticketClassName`, `rate`) VALUES ('First Class', '1.5');
INSERT INTO `pcairlineslan5`.`ticketclass` (`ticketClassName`, `rate`) VALUES ('Business Class', '1.2');
INSERT INTO `pcairlineslan5`.`ticketclass` (`ticketClassName`, `rate`) VALUES ('Economy Class', '1.0');

INSERT INTO `pcairlineslan5`.`tickettype` (`ticketTypeName`, `rate`) VALUES ('Children', '0.8');
INSERT INTO `pcairlineslan5`.`tickettype` (`ticketTypeName`, `rate`) VALUES ('Adult', '1.3');

INSERT INTO `pcairlineslan5`.`flightroute` (`departurePlaceId`, `arrivalPlaceId`, `standardPrice`) VALUES ('1', '2', '2000000');
INSERT INTO `pcairlineslan5`.`flightroute` (`departurePlaceId`, `arrivalPlaceId`, `standardPrice`) VALUES ('1', '3', '1500000');
INSERT INTO `pcairlineslan5`.`flightroute` (`departurePlaceId`, `arrivalPlaceId`, `standardPrice`) VALUES ('2', '1', '200000');
INSERT INTO `pcairlineslan5`.`flightroute` (`departurePlaceId`, `arrivalPlaceId`, `standardPrice`) VALUES ('2', '3', '1600000');
INSERT INTO `pcairlineslan5`.`flightroute` (`departurePlaceId`, `arrivalPlaceId`, `standardPrice`) VALUES ('3', '1', '1500000');
INSERT INTO `pcairlineslan5`.`flightroute` (`departurePlaceId`, `arrivalPlaceId`, `standardPrice`) VALUES ('3', '2', '1600000');

INSERT INTO `pcairlineslan5`.`flight` (`departureTime`, `arrivalTime`, `totalSeat`, `availableSeat`, `orderedSeat`, `duration`, `routeId`, `airCraftId`) VALUES ('2020-09-29 19:07:00', '2020-09-30 07:07:00', '200', '200', '0', '120', '1', '1');
INSERT INTO `pcairlineslan5`.`flight` (`departureTime`, `arrivalTime`, `totalSeat`, `availableSeat`, `orderedSeat`, `duration`, `routeId`, `airCraftId`) VALUES ('2020-09-30 20:00:00', '2020-09-30 21:30:00', '200', '200', '0', '120', '1', '1');
INSERT INTO `pcairlineslan5`.`flight` (`departureTime`, `arrivalTime`, `totalSeat`, `availableSeat`, `orderedSeat`, `duration`, `routeId`, `airCraftId`) VALUES ('2020-10-01 10:00:00', '2020-10-01 21:30:00', '200', '200', '0', '120', '2', '1');
INSERT INTO `pcairlineslan5`.`flight` (`departureTime`, `arrivalTime`, `totalSeat`, `availableSeat`, `orderedSeat`, `duration`, `routeId`, `airCraftId`) VALUES ('2020-09-29 05:00:00', '2020-09-30 5:30:00', '200', '200', '0', '120', '2', '1');
INSERT INTO `pcairlineslan5`.`flight` (`departureTime`, `arrivalTime`, `totalSeat`, `availableSeat`, `orderedSeat`, `duration`, `routeId`, `airCraftId`) VALUES ('2020-09-29 10:00:00', '2020-09-30 10:30:00', '200', '200', '0', '120', '2', '1');
INSERT INTO `pcairlineslan5`.`flight` (`departureTime`, `arrivalTime`, `totalSeat`, `availableSeat`, `orderedSeat`, `duration`, `routeId`, `airCraftId`) VALUES ('2020-09-29 21:00:00', '2020-09-30 21:30:00', '200', '200', '0', '120', '2', '1');



INSERT INTO `pcairlineslan5`.`booking` (`numberTicketBuy`, `totalMoney`, `creditCardId`, `customerId`, `paymentDate`) VALUES ('1', '1000000', '1', '1', '2020-09-29 19:16:16');

INSERT INTO `pcairlineslan5`.`ticket` (`price`, `dateBuyTicket`, `seat`, `namePersonBuyTicket`, `identify`, `bookingId`, `flightId`, `ticketClassId`, `ticketTypeId`) VALUES ('1500000', '2020-09-29 19:19:00', '01', 'Tran Diep Phuong', '201835650', '1', '1', '2', '2');
ALTER TABLE CreditCard
   ADD CCV int;
   
ALTER TABLE CreditCard
ADD CONSTRAINT UC_CreditCard UNIQUE (creditCardNumber,nameOnCard);
UPDATE `PCAirlinesLan5`.`CreditCard` SET `CCV` = '123' WHERE (`creditCardId` = '1');
UPDATE `PCAirlinesLan5`.`CreditCard` SET `CCV` = '456' WHERE (`creditCardId` = '2');
