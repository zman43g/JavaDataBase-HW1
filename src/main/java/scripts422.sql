CREATE TABLE car (carId int PRIMARY KEY,carBrand text, carModel text, cost int);
CREATE TABLE human ( name text PRIMARY KEY, age int, driverLicense boolean, carId int,  CONSTRAINT fk_human_car FOREIGN KEY (carId) REFERENCES car(carId));
