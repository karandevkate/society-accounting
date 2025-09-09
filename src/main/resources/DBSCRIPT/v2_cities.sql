INSERT INTO city (city_id, city, state_id) VALUES
-- Andhra Pradesh (state_id = 1)
(1, 'Visakhapatnam', 1),
(2, 'Vijayawada', 1),
(3, 'Guntur', 1),
(4, 'Tirupati', 1),
(5, 'Nellore', 1),
(6, 'Kurnool', 1),
(7, 'Rajahmundry', 1),

-- Arunachal Pradesh (state_id = 2)
(8, 'Itanagar', 2),
(9, 'Naharlagun', 2),
(10, 'Tawang', 2),

-- Assam (state_id = 3)
(11, 'Guwahati', 3),
(12, 'Dibrugarh', 3),
(13, 'Silchar', 3),
(14, 'Jorhat', 3),

-- Bihar (state_id = 4)
(15, 'Patna', 4),
(16, 'Gaya', 4),
(17, 'Bhagalpur', 4),
(18, 'Muzaffarpur', 4),
(19, 'Purnia', 4),

-- Chhattisgarh (state_id = 5)
(20, 'Raipur', 5),
(21, 'Bhilai', 5),
(22, 'Bilaspur', 5),
(23, 'Durg', 5),

-- Goa (state_id = 6)
(24, 'Panaji', 6),
(25, 'Vasco da Gama', 6),
(26, 'Margao', 6),

-- Gujarat (state_id = 7)
(27, 'Ahmedabad', 7),
(28, 'Surat', 7),
(29, 'Vadodara', 7),
(30, 'Rajkot', 7),
(31, 'Gandhinagar', 7),

-- Haryana (state_id = 8)
(32, 'Chandigarh', 8),
(33, 'Gurgaon', 8),
(34, 'Faridabad', 8),
(35, 'Panipat', 8),
(36, 'Rohtak', 8),

-- Himachal Pradesh (state_id = 9)
(37, 'Shimla', 9),
(38, 'Mandi', 9),
(39, 'Dharamshala', 9),

-- Jharkhand (state_id = 10)
(40, 'Ranchi', 10),
(41, 'Jamshedpur', 10),
(42, 'Dhanbad', 10),

-- Karnataka (state_id = 11)
(43, 'Bengaluru', 11),
(44, 'Mysuru', 11),
(45, 'Hubballi', 11),
(46, 'Belagavi', 11),

-- Kerala (state_id = 12)
(47, 'Thiruvananthapuram', 12),
(48, 'Kochi', 12),
(49, 'Kozhikode', 12),

-- Madhya Pradesh (state_id = 13)
(50, 'Bhopal', 13),
(51, 'Indore', 13),
(52, 'Jabalpur', 13),
(53, 'Gwalior', 13),

-- Maharashtra (state_id = 14)
(54, 'Mumbai', 14),
(55, 'Pune', 14),
(56, 'Nagpur', 14),
(57, 'Thane', 14),
(58, 'Nashik', 14),
(59, 'Aurangabad', 14),

-- Manipur (state_id = 15)
(60, 'Imphal', 15),

-- Meghalaya (state_id = 16)
(61, 'Shillong', 16),

-- Mizoram (state_id = 17)
(62, 'Aizawl', 17),

-- Nagaland (state_id = 18)
(63, 'Kohima', 18),
(64, 'Dimapur', 18),

-- Odisha (state_id = 19)
(65, 'Bhubaneswar', 19),
(66, 'Cuttack', 19),
(67, 'Rourkela', 19),
(68, 'Sambalpur', 19),

-- Punjab (state_id = 20)
(69, 'Amritsar', 20),
(70, 'Ludhiana', 20),
(71, 'Patiala', 20),

-- Rajasthan (state_id = 21)
(72, 'Jaipur', 21),
(73, 'Udaipur', 21),
(74, 'Jodhpur', 21),
(75, 'Kota', 21),

-- Sikkim (state_id = 22)
(76, 'Gangtok', 22),

-- Tamil Nadu (state_id = 23)
(77, 'Chennai', 23),
(78, 'Coimbatore', 23),
(79, 'Madurai', 23),
(80, 'Tiruchirappalli', 23),

-- Telangana (state_id = 24)
(81, 'Hyderabad', 24),
(82, 'Warangal', 24),

-- Tripura (state_id = 25)
(83, 'Agartala', 25),

-- Uttar Pradesh (state_id = 26)
(84, 'Lucknow', 26),
(85, 'Kanpur', 26),
(86, 'Varanasi', 26),
(87, 'Agra', 26),
(88, 'Meerut', 26),
(89, 'Allahabad', 26),

-- Uttarakhand (state_id = 27)
(90, 'Dehradun', 27),
(91, 'Haridwar', 27),

-- West Bengal (state_id = 28)
(92, 'Kolkata', 28),
(93, 'Darjeeling', 28),
(94, 'Asansol', 28),
(95, 'Durgapur', 28)
ON CONFLICT (city_id) DO NOTHING;