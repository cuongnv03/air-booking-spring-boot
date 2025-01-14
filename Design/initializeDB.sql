-- -- Tạo cơ sở dữ liệu (nếu cần)
-- CREATE DATABASE air_booking;
-- \c air_booking;

-- Bảng Users
CREATE TABLE "user" (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    is_admin BOOLEAN NOT NULL,
    full_name VARCHAR(255),
    date_of_birth DATE,
    gender VARCHAR(10),
    contact_phone VARCHAR(15),
    email VARCHAR(255),
    identity_card_id VARCHAR(50)
);

-- Bảng Airports
CREATE TABLE Airport (
    id SERIAL PRIMARY KEY,
    airport_code VARCHAR(10) NOT NULL UNIQUE,
    airport_name VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL
);

-- Bảng Flights
CREATE TABLE Flight (
    id SERIAL PRIMARY KEY,
    flight_id VARCHAR(10),
    company VARCHAR(255) NOT NULL,
    departure_point VARCHAR(255) NOT NULL,
    destination_point VARCHAR(255) NOT NULL,
    scheduled_time TIMESTAMP NOT NULL,
    price_E BIGINT NOT NULL,
    price_B BIGINT NOT NULL,
    baggage_allowance_E INT NOT NULL,
    baggage_allowance_B INT NOT NULL,
    num_seats_E INT DEFAULT 0,
    num_seats_B INT DEFAULT 0,
    FOREIGN KEY (departure_point) REFERENCES Airport(airport_code),
    FOREIGN KEY (destination_point) REFERENCES Airport(airport_code),
    UNIQUE (flight_id)
);

-- Trigger: Tự động tạo flight_id
CREATE OR REPLACE FUNCTION generate_flight_id()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.flight_id IS NULL THEN
        NEW.flight_id = CONCAT('FL', LPAD(NEW.id::TEXT, 3, '0'));
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER generate_flight_id_trigger
BEFORE INSERT ON Flight
FOR EACH ROW
EXECUTE FUNCTION generate_flight_id();

-- Bảng Seats
CREATE TABLE Seat (
    id INT NOT NULL,
    flight_id VARCHAR(10) NOT NULL,
    seat_class VARCHAR(10) CHECK (seat_class IN ('economy', 'business')) NOT NULL,
    price BIGINT NOT NULL,
    baggage_allowance INT NOT NULL,
    seat_status BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id, flight_id),
    FOREIGN KEY (flight_id) REFERENCES Flight(flight_id)
);

-- Bảng Bookings
CREATE TABLE Booking (
    id SERIAL PRIMARY KEY,
    booking_id VARCHAR(255) NOT NULL UNIQUE,
    flight_id VARCHAR(10) NOT NULL,
    seat_id INT NOT NULL,
    user_id INT NOT NULL,
    payment_status BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (flight_id) REFERENCES Flight(flight_id),
    FOREIGN KEY (seat_id, flight_id) REFERENCES Seat(id, flight_id),
    FOREIGN KEY (user_id) REFERENCES "user"(user_id)
);

-- Bảng Cards
CREATE TABLE Card (
    id SERIAL PRIMARY KEY,
    card_number CHAR(16) NOT NULL UNIQUE,
    cardholder_name VARCHAR(255) NOT NULL,
    cvv_code CHAR(3) NOT NULL,
    balance_amount NUMERIC(15, 2) NOT NULL,
    expiry_date DATE NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES "user"(user_id)
);

-- Trigger: Cập nhật số ghế còn lại trong bảng Flights
CREATE OR REPLACE FUNCTION update_seat_count()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE Flight
    SET num_seats_E = (SELECT COUNT(*) FROM Seat WHERE flight_id = NEW.flight_id AND seat_class = 'economy' AND seat_status = FALSE),
        num_seats_B = (SELECT COUNT(*) FROM Seat WHERE flight_id = NEW.flight_id AND seat_class = 'business' AND seat_status = FALSE)
    WHERE flight_id = NEW.flight_id;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_seat_count_trigger
AFTER INSERT OR UPDATE OR DELETE ON Seat
FOR EACH ROW
EXECUTE FUNCTION update_seat_count();

-- Trigger: Tự động cập nhật giá và hành lý của ghế dựa trên loại ghế
CREATE OR REPLACE FUNCTION set_seat_price_baggage()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.seat_class = 'economy' THEN
        NEW.price = (SELECT price_E FROM Flight WHERE flight_id = NEW.flight_id);
        NEW.baggage_allowance = (SELECT baggage_allowance_E FROM Flight WHERE flight_id = NEW.flight_id);
    ELSIF NEW.seat_class = 'business' THEN
        NEW.price = (SELECT price_B FROM Flight WHERE flight_id = NEW.flight_id);
        NEW.baggage_allowance = (SELECT baggage_allowance_B FROM Flight WHERE flight_id = NEW.flight_id);
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER set_seat_price_baggage_trigger
BEFORE INSERT ON Seat
FOR EACH ROW
EXECUTE FUNCTION set_seat_price_baggage();

-- Function để xử lý logic cập nhật thời gian
CREATE OR REPLACE FUNCTION set_booking_timestamps()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'INSERT') THEN
        NEW.created_at = CURRENT_TIMESTAMP; -- Gán thời gian hiện tại cho created_at
        NEW.updated_at = CURRENT_TIMESTAMP; -- updated_at ban đầu trùng với created_at
    ELSIF (TG_OP = 'UPDATE') THEN
        NEW.updated_at = CURRENT_TIMESTAMP; -- Chỉ cập nhật updated_at khi bản ghi được chỉnh sửa
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger gọi hàm trên khi INSERT hoặc UPDATE bảng Booking
CREATE TRIGGER booking_timestamps_trigger
BEFORE INSERT OR UPDATE ON Booking
FOR EACH ROW
EXECUTE FUNCTION set_booking_timestamps();

CREATE OR REPLACE FUNCTION generate_booking_id()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.booking_id IS NULL THEN
        -- Format: BKG-YYYYMMDD-XXXX
        NEW.booking_id = CONCAT(
            'BKG-', 
            TO_CHAR(CURRENT_TIMESTAMP, 'YYYYMMDD'), 
            '-', 
            LPAD((NEW.id::TEXT), 4, '0') -- Padding số ID với 4 chữ số
        );
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER generate_booking_id_trigger
BEFORE INSERT ON Booking
FOR EACH ROW
EXECUTE FUNCTION generate_booking_id();

CREATE OR REPLACE FUNCTION set_seat_id()
RETURNS TRIGGER AS $$
DECLARE
    max_id INT;
BEGIN
    -- Lấy giá trị id lớn nhất hiện có cho flight_id
    SELECT COALESCE(MAX(id), 0) INTO max_id
    FROM public.seat
    WHERE flight_id = NEW.flight_id;

    -- Gán id mới là max_id + 1
    NEW.id = max_id + 1;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER set_seat_id_trigger
BEFORE INSERT ON public.seat
FOR EACH ROW
EXECUTE FUNCTION set_seat_id();

-- Thêm dữ liệu mẫu
-- Users
INSERT INTO "user" (username, password, is_admin, full_name, date_of_birth, gender, contact_phone, email, identity_card_id) VALUES
('admin', '1', TRUE, 'Admin User', '1980-01-01', 'male', '1234567890', 'admin@example.com', 'ID001'),
('user1', '123', FALSE, 'User One', '1990-02-01', 'female', '1234567891', 'user1@example.com', 'ID002'),
('user2', '123', FALSE, 'User Two', '1992-03-01', 'male', '1234567892', 'user2@example.com', 'ID003'),
('user3', '123', FALSE, 'User Three', '1993-04-01', 'female', '1234567893', 'user3@example.com', 'ID004'),
('user4', '123', FALSE, 'User Four', '1994-05-01', 'male', '1234567894', 'user4@example.com', 'ID005'),
('user5', '123', FALSE, 'User Five', '1995-06-01', 'female', '1234567895', 'user5@example.com', 'ID006'),
('user6', '123', FALSE, 'User Six', '1996-07-01', 'male', '1234567896', 'user6@example.com', 'ID007'),
('user7', '123', FALSE, 'User Seven', '1997-08-01', 'female', '1234567897', 'user7@example.com', 'ID008'),
('user8', '123', FALSE, 'User Eight', '1998-09-01', 'male', '1234567898', 'user8@example.com', 'ID009'),
('user9', '123', FALSE, 'User Nine', '1999-10-01', 'female', '1234567899', 'user9@example.com', 'ID010');

-- Airports
INSERT INTO Airport (airport_code, airport_name, city, country) VALUES
('JFK', 'John F. Kennedy International', 'New York', 'USA'),
('LAX', 'Los Angeles International', 'Los Angeles', 'USA'),
('ORD', 'O''Hare International', 'Chicago', 'USA'),
('DFW', 'Dallas/Fort Worth International', 'Dallas', 'USA'),
('DEN', 'Denver International', 'Denver', 'USA'),
('SFO', 'San Francisco International', 'San Francisco', 'USA'),
('SEA', 'Seattle-Tacoma International', 'Seattle', 'USA'),
('ATL', 'Hartsfield-Jackson Atlanta International', 'Atlanta', 'USA'),
('MIA', 'Miami International', 'Miami', 'USA'),
('BOS', 'Logan International', 'Boston', 'USA');

-- Flights
INSERT INTO Flight (company, departure_point, destination_point, scheduled_time, price_E, price_B, baggage_allowance_E, baggage_allowance_B) VALUES
('Airline A', 'JFK', 'LAX', '2025-01-20 10:00:00', 300, 800, 15, 30),
('Airline B', 'LAX', 'ORD', '2025-01-21 11:00:00', 250, 700, 15, 30),
('Airline C', 'ORD', 'DFW', '2025-01-22 12:00:00', 200, 600, 15, 30),
('Airline D', 'DFW', 'DEN', '2025-01-23 13:00:00', 180, 550, 15, 30),
('Airline E', 'DEN', 'SFO', '2025-01-24 14:00:00', 220, 650, 15, 30),
('Airline F', 'SFO', 'SEA', '2025-01-25 15:00:00', 240, 700, 15, 30),
('Airline G', 'SEA', 'ATL', '2025-01-26 16:00:00', 280, 750, 15, 30),
('Airline H', 'ATL', 'MIA', '2025-01-27 17:00:00', 260, 720, 15, 30),
('Airline I', 'MIA', 'BOS', '2025-01-28 18:00:00', 300, 800, 15, 30),
('Airline J', 'BOS', 'JFK', '2025-01-29 19:00:00', 320, 850, 15, 30);

-- Cards
INSERT INTO Card (card_number, cardholder_name, cvv_code, balance_amount, expiry_date, user_id) VALUES
('1234567812345678', 'User One', '123', 1000.00, '2025-12-01', 2),
('2345678923456789', 'User Two', '234', 1500.00, '2026-11-01', 3),
('3456789034567890', 'User Three', '345', 2000.00, '2027-10-01', 4),
('4567890145678901', 'User Four', '456', 2500.00, '2028-09-01', 5),
('5678901256789012', 'User Five', '567', 3000.00, '2029-08-01', 6),
('6789012367890123', 'User Six', '678', 3500.00, '2030-07-01', 7),
('7890123478901234', 'User Seven', '789', 4000.00, '2031-06-01', 8),
('8901234589012345', 'User Eight', '890', 4500.00, '2032-05-01', 9),
('9012345690123456', 'User Nine', '901', 5000.00, '2033-04-01', 10),
('0123456701234567', 'Admin User', '012', 10000.00, '2034-03-01', 1);

-- Seats
DO $$
DECLARE
    flight_rec RECORD;
    seat_class TEXT;
BEGIN
    FOR flight_rec IN (SELECT flight_id, price_E, price_B, baggage_allowance_E, baggage_allowance_B FROM Flight) LOOP
        FOR i IN 1..5 LOOP
            IF i <= 3 THEN
                seat_class := 'economy';
                INSERT INTO Seat (id, flight_id, seat_class, price, baggage_allowance) VALUES (i, flight_rec.flight_id, seat_class, flight_rec.price_E, flight_rec.baggage_allowance_E);
            ELSE
                seat_class := 'business';
                INSERT INTO Seat (id, flight_id, seat_class, price, baggage_allowance) VALUES (i, flight_rec.flight_id, seat_class, flight_rec.price_B, flight_rec.baggage_allowance_B);
            END IF;
        END LOOP;
    END LOOP;
END $$;