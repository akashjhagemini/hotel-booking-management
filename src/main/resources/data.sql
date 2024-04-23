
insert ignore into room (room_number, type, occupancy, price_per_day, availability, is_checked_in, is_checked_out) values
(1, 'single', 1, 100, true, false, false),
(2, 'double', 2, 200, true, false, false),
(3, 'deluxe', 3, 500, true, false, false),
(4, 'triple', 3, 300, true, false, false);

insert ignore into customer (customer_id, full_name, address, age, contact_number) values
(1, 'akash', 'delhi', 22, '1234567890'),
(2, 'aman', 'delhi', 23, '1234567891'),
(3, 'ankit', 'delhi', 23, '1234567892'),
(4, 'aniket', 'gurgaon', 23, '1234567893');
