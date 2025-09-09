CREATE TABLE IF NOT EXISTS state (
    state_id SERIAL PRIMARY KEY,
    state_name VARCHAR(255) NOT NULL UNIQUE
);

INSERT INTO state (state_id, state_name) VALUES
(1, 'Andhra Pradesh'), (2, 'Arunachal Pradesh'), (3, 'Assam'),
(4, 'Bihar'), (5, 'Chhattisgarh'), (6, 'Goa'), (7, 'Gujarat'),
(8, 'Haryana'), (9, 'Himachal Pradesh'), (10, 'Jharkhand'),
(11, 'Karnataka'), (12, 'Kerala'), (13, 'Madhya Pradesh'),
(14, 'Maharashtra'), (15, 'Manipur'), (16, 'Meghalaya'),
(17, 'Mizoram'), (18, 'Nagaland'), (19, 'Odisha'), (20, 'Punjab'),
(21, 'Rajasthan'), (22, 'Sikkim'), (23, 'Tamil Nadu'), (24, 'Telangana'),
(25, 'Tripura'), (26, 'Uttar Pradesh'), (27, 'Uttarakhand'), (28, 'West Bengal')
ON CONFLICT (state_id) DO NOTHING;
