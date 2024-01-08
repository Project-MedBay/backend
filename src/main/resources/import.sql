INSERT INTO _user (status, id, email, first_name, last_name, password, role) VALUES ('ACTIVE', 1, 'admin@gmail.com', 'John', 'Doe', '$2a$12$AgOr5O5nmLlIgsBgHrP57eouuZ9XRgOX.evYytR7oZ.XRBrgCS65W', 'ADMIN');
--employees
--INSERT INTO _user (id, email, first_name, last_name, password, role, status) VALUES (1, 'employee1@example.com', 'Employee', 'One', '$2a$12$vt1ex4tEN.mRrg1w.43CuejrCJ4qVnRJWcBNE2TewGlR7BYjRTfMG', 'STAFF', 'ACTIVE');
--INSERT INTO employee (specialization, id)  VALUES ('PHYSICAL_THERAPIST', 1);
--INSERT INTO _user(status,id,  email, first_name, last_name, password, role) VALUES ('ACTIVE',2,  'ivo.ivic@gmail.com', 'Ivo', 'Ivic', 'testPass', 'STAFF');
INSERT INTO _user (id, email, first_name, last_name, password, role, status) VALUES (2, 'patient1@example.com', 'Patient', 'One', '$2a$12$9eweklLdaOLp.VVZDQBiz.LFR4xO1RMqKHD2ZKCVu1a3sYyP/kXAm', 'PATIENT', 'ACTIVE');

INSERT INTO _user (id, email, first_name, last_name, password, role, status) VALUES (3, 'patient2@example.com', 'Patient', 'Two', '$2a$12$Zd8PdG0ypHVmQKPj34159OZ8GGaR1761mziLDZ76LWf528vAovboK', 'PATIENT', 'PENDING');

INSERT INTO _user (id, email, first_name, last_name, password, role, status) VALUES (4, 'patient3@example.com', 'Patient', 'Three', '$2a$12$KEB.lcTkLt2ymUvUVaycfOYVcVzIq/n9Qd4z2TZMgRbpedzwx/sYq', 'PATIENT', 'PENDING');

INSERT INTO _user (id, email, first_name, last_name, password, role, status) VALUES (5, 'patient4@example.com', 'Patient', 'Four', '$2a$12$hBBBlx.IKKe0mv4n43Vp8eRbblKaVXZjTXoRVrAn/BRsoO7Q81Sei', 'PATIENT', 'ACTIVE');

INSERT INTO _user (id, email, first_name, last_name, password, role, status) VALUES (6, 'patient5@example.com', 'Patient', 'Five', '$2a$12$4Bceack6KQ/KzjMja4ZaoepC1/t8V/KFNYukUjJ.RfrXZMsBshy2C', 'PATIENT', 'ACTIVE');


INSERT INTO equipment (capacity, id, name, specialization, room_name) VALUES (20, 1, 'Gym', 'PHYSICAL_THERAPIST', 'Physiotherapy Suite'); --physical therapy
INSERT INTO equipment (capacity, id, name, specialization, room_name) VALUES (2, 2, 'Electrical Muscle Stimulation unit', 'ELECTROTHERAPIST', 'Electrotherapy Zone');
INSERT INTO equipment (capacity, id, name, specialization, room_name) VALUES (2, 3, 'Cryotherapy machine', 'CRYOTHERAPIST', 'CryoRecovery Room');
INSERT INTO equipment (capacity, id, name, specialization, room_name) VALUES (4, 4, 'Hydrotherapy pool', 'HYDROTHERAPIST', 'Hydro Rehab Pool');
INSERT INTO equipment (capacity, id, name, specialization, room_name) VALUES (4, 5, 'Paraffin wax baths and infrared lamp', 'THERMOTHERAPIST', 'Thermal Wellness Space');


--shoulder
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions, body_part) VALUES (1, 'Rotator cuff tear', 'Physical therapy focuses on strengthening the shoulder muscles, improving range of motion, and reducing pain through targeted exercises.', '#5G3Y4', 12, 'shoulder');
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions, body_part) VALUES (1, 'Frozen shoulder', 'Therapy aims to restore shoulder mobility through stretching and range of motion exercises.', '#7Z8V8', 10, 'shoulder');

--arm
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions, body_part) VALUES (1, 'Tennis elbow', 'Treatment includes strengthening forearm muscles.', '#1V2V2', 8, 'arm');
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions, body_part) VALUES (1, 'Golfers elbow', 'Physical therapy involves strengthening and stretching exercises for the forearm muscles and may include modalities for pain relief.', '#8I5F5', 8, 'arm');
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions, body_part) VALUES (1, 'Biceps tendinitis', 'Therapy focuses on strengthening the biceps and shoulder muscles while reducing inflammation through modalities.', '#4C7X3', 10, 'arm');

--hand
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions, body_part) VALUES (1, 'Carpal tunnel', 'Exercises to improve wrist and hand flexibility, along with techniques to relieve pressure on the median nerve.', '#1R2Y1', 8, 'hand');
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions, body_part) VALUES (3, 'Arthritis pain management', 'Cryotherapy helps alleviate arthritis pain by numbing the affected joints, reducing inflammation, and temporarily easing discomfort and stiffness.', '#6F6P9', 8, 'hand');
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions, body_part) VALUES (3, 'Tendinitis', 'Cryotherapy can be employed to reduce inflammation and alleviate pain associated with tendinitis by constricting blood vessels and diminishing tissue swelling in the affected area.', '#1G8Z3', 10, 'hand');
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions, body_part) VALUES (4, 'Osteoarthritis management', 'Hydrotherapy provides a low-impact environment for joint mobility exercises and pain relief, helping individuals with osteoarthritis improve joint function and reduce discomfort.', '#4D5C2', 8, 'hand');
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions, body_part) VALUES (5, 'Bursitis', 'Thermotherapy may be used to alleviate pain and inflammation associated with bursitis by increasing blood flow and promoting the relaxation of affected tissues, often applied through warm compresses or therapeutic ultrasound.', '#4N8P5' , 8, 'hand');
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions, body_part) VALUES (5, 'Rheumatoid Arthritis', 'Thermotherapy, such as paraffin wax baths, can help ease joint stiffness and reduce pain in individuals with rheumatoid arthritis by enhancing joint flexibility and reducing inflammation.', '#7R3N6', 8, 'hand');

--leg
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions, body_part) VALUES (1, 'ACL injury', 'Rehabilitation includes exercises to strengthen the knee and improve stability, often following surgical repair.', '#3H4O2', 20, 'leg');
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions, body_part) VALUES (1, 'Meniscus tear', 'Therapy emphasizes range of motion exercises and strengthening of the muscles around the knee.', '#6F2G1', 8, 'leg');
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions, body_part) VALUES (1, 'IT band syndrome', 'Treatment involves stretching and strengthening the IT band and hip muscles.', '#8L2Q4', 8, 'leg');
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions, body_part) VALUES (1, 'Shin splints', 'Rehabilitation focuses on reducing inflammation and improving lower leg strength and flexibility.', '#7P3Y5', 6, 'leg');
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions, body_part) VALUES (1, 'Stress fractures', 'Rest and gradual return to weight-bearing activities are essential, along with exercises to maintain bone density.', '#9M8D2', 10, 'leg');
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions, body_part) VALUES (1, 'Hip bursitis', 'Exercises to improve hip muscle strength and flexibility, along with modalities for pain management.', '#2T8L4', 10, 'leg');

--foot
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions, body_part) VALUES (1, 'Ankle sprain', 'Therapy includes balance exercises, strengthening the ankle, and improving flexibility.', '#6W1E6', 8, 'foot');
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions, body_part) VALUES (1, 'Plantar fasciitis ', 'Treatment involves stretching and strengthening exercises for the calf and foot muscles, along with modalities for pain relief.', '#6B5Q3', 8, 'foot');
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions, body_part) VALUES (1, 'Achilles tendinitis ', 'Therapy aims to strengthen the calf muscles and Achilles tendon while reducing inflammation.', '#5C3G3', 12, 'foot');

--lower torso
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions, body_part) VALUES (1, 'Lumbar herniated disc ', 'Physical therapy focuses on relieving pain and improving spinal stability through exercises.', '#3N4P6', 14, 'lower torso');
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions, body_part) VALUES (1, 'Sciatica', 'Treatment involves exercises to alleviate sciatic nerve compression and improve lumbar spine function.', '#9P1H4', 7, 'lower torso');
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions, body_part) VALUES (4, 'Chronic low back pain management', 'Hydrotherapy involves warm water exercises and aquatic therapy techniques to improve flexibility, reduce pain, and enhance core strength in individuals with chronic low back pain.', '#4W3F2', 10, 'lower_torso');

--upper torso
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions, body_part) VALUES (1, 'Scoliosis', 'Therapy includes exercises to improve spinal alignment, flexibility, and muscle balance.', '#5R9N7', 8, 'upper torso');
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions, body_part) VALUES (1, 'Postural disorders', 'Treatment includes exercises to correct posture and improve muscle balance to relieve pain and prevent future issues.', '#8K1H7', 7, 'upper torso');

--head
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions, body_part) VALUES (1, 'Cervical radiculopathy ', 'Rehabilitation aims to reduce neck pain and nerve symptoms through neck exercises and modalities.', '#8G5H3', 8, 'head');
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions, body_part) VALUES (1, 'Whiplash', 'Physical therapy focuses on neck strengthening and range of motion exercises to alleviate pain and stiffness.', '#8H6H1', 10, 'head');

--any
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions, body_part) VALUES (2, 'Post-Operative rehabilitation', 'EMS therapy aids in muscle activation and circulation for post-operative patients, helping to prevent muscle atrophy and accelerate recovery through controlled electrical muscle contractions.', '#3J6K2', 12, 'any');
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions, body_part) VALUES (2, 'Stroke rehabilitation', 'EMS can be used to stimulate paralyzed or weakened muscles, aiding in muscle re-education and motor function recovery for stroke survivors.', '#9P3R1', 20, 'any');
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions, body_part) VALUES (2, 'Muscle atrophy prevention', 'EMS assists in maintaining muscle strength and mass in individuals with limited mobility, such as bedridden or immobilized patients, by providing passive muscle contractions', '#2J6Z7', 8, 'any');
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions, body_part) VALUES (2, 'Chronic pain management', 'EMS therapy can help manage chronic pain conditions by stimulating endorphin release, reducing pain perception, and improving blood circulation in targeted areas.', '#7X4N5', 6, 'any');
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions, body_part) VALUES (3, 'Post-Operative swelling minimization', 'Cryotherapy involves the application of cold therapy to reduce post-operative swelling and inflammation, promoting faster healing and pain relief.', '#5M8P4', 6, 'any');
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions, body_part) VALUES (3, 'Acute sports injury', 'Cryotherapy is used immediately after an acute sports injury to minimize swelling, numb the area, and provide pain relief while facilitating faster recovery.', '#5Z9B5', 8, 'any');
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions, body_part) VALUES (4, 'Post-Stroke rehabilitation', 'Hydrotherapy offers a supportive and buoyant environment for stroke survivors to work on regaining ', '#8D2H9', 20, 'any');
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions, body_part) VALUES (4, 'Post-Operative Rehabilitation', 'Hydrotherapy can aid in the recovery process after surgery by offering a gentle way to regain mobility, reduce swelling, and improve overall conditioning through aquatic exercises and controlled movements in warm water.', '#5I9K8', 12, 'any');
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions, body_part) VALUES (5, 'Muscle spasms and tension management', 'Thermotherapy in a wax bath involves immersing affected muscles in warm wax to relax and soothe muscle spasms, reduce tension, and promote blood flow for pain relief.', '#6L2M9', 6, 'any');
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions, body_part) VALUES (5, 'Chronic muscle pain management', 'Regular heat therapy, including hot baths, warm packs, or infrared heat, may be part of a comprehensive pain management strategy for chronic muscle pain, providing temporary relief and improving muscle relaxation.', '#2K6H8', 7, 'any');

INSERT INTO health_referral (health_referral_id, hlkid, mbo, therapy_code) VALUES ('DGC_167819', 'HR019618', '160378294', '#3N4P6');
INSERT INTO health_referral (health_referral_id, hlkid, mbo, therapy_code) VALUES ('DGC_167820', 'HR061864', '234567890', '#9P1H4'); --inactive
INSERT INTO health_referral (health_referral_id, hlkid, mbo, therapy_code) VALUES ('DGC_167821', 'HR271982', '345678901', '#5R9N7');
INSERT INTO health_referral (health_referral_id, hlkid, mbo, therapy_code) VALUES ('DGC_167822', 'HR282164', '456789012', '#8G5H3');
INSERT INTO health_referral (health_referral_id, hlkid, mbo, therapy_code) VALUES ('DGC_167823', 'HR005951', '567890123', '#8H6H1');
INSERT INTO health_referral (health_referral_id, hlkid, mbo, therapy_code) VALUES ('DGC_167824', 'HR211078', '678901234', '#8K1H7');
INSERT INTO health_referral (health_referral_id, hlkid, mbo, therapy_code) VALUES ('DGC_167825', 'HR026260', '789012345', '#3J6K2'); --inactive
INSERT INTO health_referral (health_referral_id, hlkid, mbo, therapy_code) VALUES ('DGC_167826', 'HR166491', '890123456', '#9P3R1');
INSERT INTO health_referral (health_referral_id, hlkid, mbo, therapy_code) VALUES ('DGC_167827', 'HR095677', '901234567', '#4W3F2');
INSERT INTO health_referral (health_referral_id, hlkid, mbo, therapy_code) VALUES ('DGC_167828', 'HR233633', '987654321', '#4D5C2');
INSERT INTO health_referral (health_referral_id, hlkid, mbo, therapy_code) VALUES ('DGC_167829', 'HR206450', '876543210', '#8D2H9'); --inactive
INSERT INTO health_referral (health_referral_id, hlkid, mbo, therapy_code) VALUES ('DGC_167830', 'HR019618', '160378294', '#1V2V2');
INSERT INTO health_referral (health_referral_id, hlkid, mbo, therapy_code) VALUES ('DGC_167831', 'HR171737', '234567890', '#8I5F5');
INSERT INTO health_referral (health_referral_id, hlkid, mbo, therapy_code) VALUES ('DGC_167832', 'HR295432', '345678901', '#4C7X3');
INSERT INTO health_referral (health_referral_id, hlkid, mbo, therapy_code) VALUES ('DGC_167833', 'HR197286', '456789012', '#1R2Y1');
INSERT INTO health_referral (health_referral_id, hlkid, mbo, therapy_code) VALUES ('DGC_167834', 'HR207887', '567890123', '#3H4O2'); --inactive
INSERT INTO health_referral (health_referral_id, hlkid, mbo, therapy_code) VALUES ('DGC_167835', 'HR224558', '678901234', '#6F2G1');
INSERT INTO health_referral (health_referral_id, hlkid, mbo, therapy_code) VALUES ('DGC_167836', 'HR233300', '789012345', '#8L2Q4');
INSERT INTO health_referral (health_referral_id, hlkid, mbo, therapy_code) VALUES ('DGC_167837', 'HR221049', '890123456', '#7P3Y5');
INSERT INTO health_referral (health_referral_id, hlkid, mbo, therapy_code) VALUES ('DGC_167838', 'HR177695', '901234567', '#9M8D2');
INSERT INTO health_referral (health_referral_id, hlkid, mbo, therapy_code) VALUES ('DGC_167839', 'HR219873', '876543210', '#6W1E6'); --inactive

-- INSERT INTO patient (id, date_of_birth, address, mbo, oib, phone_number) VALUES (1, '2000-01-01', 'tesna ulica 1', '1279525045', '531113693', '0911223456');
-- INSERT INTO patient (id, date_of_birth, address, mbo, oib, phone_number) VALUES (2, '2003-01-01', 'tesna ulica 4', '1899935409', '463243045', '0911223457');
-- INSERT INTO patient (id, date_of_birth, address, mbo, oib, phone_number) VALUES (3, '2002-04-01', 'tesna ulica 2', '1264166032', '357092999', '0911223458');
-- INSERT INTO patient (id, date_of_birth, address, mbo, oib, phone_number) VALUES (4, '2004-01-01', 'tesna ulica 1', '1487949723', '349047109', '0911223459');
-- INSERT INTO patient (id, date_of_birth, address, mbo, oib, phone_number) VALUES (5, '2001-01-05', 'tesna ulica 8', '1509125019', '620361537', '0911223460');

INSERT INTO therapy (therapy_type_id, therapy_status) VALUES (3, 'VERIFIED');
INSERT INTO therapy (therapy_type_id, therapy_status) VALUES (5, 'PENDING');
INSERT INTO therapy (therapy_type_id, therapy_status) VALUES (4, 'VERIFIED');
INSERT INTO therapy (therapy_type_id, therapy_status) VALUES (9, 'VERIFIED');
INSERT INTO therapy (therapy_type_id, therapy_status) VALUES (2, 'VERIFIED');
INSERT INTO therapy (therapy_type_id, therapy_status) VALUES (7, 'PENDING');
INSERT INTO therapy (therapy_type_id, therapy_status) VALUES (1, 'PENDING');
INSERT INTO therapy (therapy_type_id, therapy_status) VALUES (6, 'VERIFIED');