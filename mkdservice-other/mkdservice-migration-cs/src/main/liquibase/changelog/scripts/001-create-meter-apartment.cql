CREATE TABLE IF NOT EXISTS mkdservice_meter_apartments
(
    apartment_id text,
    meter_id     text,
    PRIMARY KEY (apartment_id, meter_id)
);

INSERT INTO mkdservice_meter_apartments (apartment_id, meter_id)
VALUES ('1', '428398');

INSERT INTO mkdservice_meter_apartments (apartment_id, meter_id)
VALUES ('1', '109283');

INSERT INTO mkdservice_meter_apartments (apartment_id, meter_id)
VALUES ('2', '342893');

INSERT INTO mkdservice_meter_apartments (apartment_id, meter_id)
VALUES ('2', '019283');

INSERT INTO mkdservice_meter_apartments (apartment_id, meter_id)
VALUES ('3', '128932');

INSERT INTO mkdservice_meter_apartments (apartment_id, meter_id)
VALUES ('3', '523523');