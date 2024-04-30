CREATE TABLE customer (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    contact_number VARCHAR(10) UNIQUE NOT NULL,
    CONSTRAINT check_age_positive CHECK (age >= 0)
);

CREATE TABLE room (
    room_number INT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(255) NOT NULL,
    occupancy INT NOT NULL,
    price_per_day INT NOT NULL,
    availability BOOLEAN DEFAULT TRUE,
    is_checked_in BOOLEAN DEFAULT FALSE,
    is_checked_out BOOLEAN DEFAULT TRUE
);

CREATE TABLE customer_room (
    room_number INT,
    customer_id INT,
    PRIMARY KEY (room_number, customer_id),
    FOREIGN KEY (room_number) REFERENCES room(room_number),
    FOREIGN KEY (customer_id) REFERENCES Customer(customer_id)
);

CREATE TABLE booking_details (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    duration INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    mode_of_booking ENUM('online', 'offline') NOT NULL,
    mode_of_payment ENUM('cash', 'prepaid', 'online') NOT NULL,
    bill_amount INT DEFAULT 0,
    paid_amount INT DEFAULT 0
);

CREATE TABLE booking_customer (
    booking_id INT,
    customer_id INT,
    PRIMARY KEY (booking_id, customer_id),
    FOREIGN KEY (booking_id) REFERENCES booking_details(booking_id),
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);

CREATE TABLE booked_room_list (
    booking_id INT,
    room_number INT,
    PRIMARY KEY (booking_id, room_number),
    FOREIGN KEY (booking_id) REFERENCES booking_details(booking_id),
    FOREIGN KEY (room_number) REFERENCES room(room_number)
);
