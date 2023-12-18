INSERT INTO _user (status, id, email, first_name, last_name, password, role) VALUES ('ACTIVE', 1, 'admin@gmail.com', 'John', 'Doe', '$2a$12$AgOr5O5nmLlIgsBgHrP57eouuZ9XRgOX.evYytR7oZ.XRBrgCS65W', 'ROLE_ADMIN');
--employees
INSERT INTO _user(status,id,  email, first_name, last_name, password, role) VALUES ('ACTIVE',2,  'ivo.ivic@gmail.com', 'Ivo', 'Ivic', 'testPass', 'ROLE_STAFF');

INSERT INTO equipment (capacity, id, name) VALUES (20, 1, 'Gym'); --physical therapy
INSERT INTO equipment (capacity, id, name) VALUES (2, 2, 'Electrical Muscle Stimulation unit');
INSERT INTO equipment (capacity, id, name) VALUES (2, 3, 'Cryotherapy machine');
INSERT INTO equipment (capacity, id, name) VALUES (4, 4, 'Hydrotherapy pool');

--shoulder
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions) VALUES (1, 'Rotator cuff tear', 'Physical therapy focuses on strengthening the shoulder muscles, improving range of motion, and reducing pain through targeted exercises.', '#5G3Y4', 12);
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions) VALUES (1, 'Frozen shoulder', 'Therapy aims to restore shoulder mobility through stretching and range of motion exercises.', '#7Z8V8', 10);
--elbow + biceps
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions) VALUES (1, 'Tennis elbow', 'Treatment includes strengthening forearm muscles.', '#1V2V2', 8);
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions) VALUES (1, 'Golfers elbow', 'Physical therapy involves strengthening and stretching exercises for the forearm muscles and may include modalities for pain relief.', '#8I5F5', 8);
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions) VALUES (1, 'Biceps tendinitis', 'Therapy focuses on strengthening the biceps and shoulder muscles while reducing inflammation through modalities.', '#4C7X3', 10);
--wrist
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions) VALUES (1, 'Carpal tunnel', 'Exercises to improve wrist and hand flexibility, along with techniques to relieve pressure on the median nerve.', '#1R2Y1', 8);
--knee + shin
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions) VALUES (1, 'ACL injury', 'Rehabilitation includes exercises to strengthen the knee and improve stability, often following surgical repair.', '#3H4O2', 20);
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions) VALUES (1, 'Meniscus tear', 'Therapy emphasizes range of motion exercises and strengthening of the muscles around the knee.', '#6F2G1', 8);
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions) VALUES (1, 'IT band syndrome', 'Treatment involves stretching and strengthening the IT band and hip muscles.', '#8L2Q4', 8);
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions) VALUES (1, 'Shin splints', 'Rehabilitation focuses on reducing inflammation and improving lower leg strength and flexibility.', '#7P3Y5', 6);
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions) VALUES (1, 'Stress fractures', 'Rest and gradual return to weight-bearing activities are essential, along with exercises to maintain bone density.', '#9M8D2', 10);
--ankle + foot
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions) VALUES (1, 'Ankle sprain', 'Therapy includes balance exercises, strengthening the ankle, and improving flexibility.', '#6W1E6', 8);
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions) VALUES (1, 'Plantar fasciitis ', 'Treatment involves stretching and strengthening exercises for the calf and foot muscles, along with modalities for pain relief.', '#6B5Q3', 8);
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions) VALUES (1, 'Achilles tendinitis ', 'Therapy aims to strengthen the calf muscles and Achilles tendon while reducing inflammation.', '#5C3G3', 12);
--hip
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions) VALUES (1, 'Hip bursitis', 'Exercises to improve hip muscle strength and flexibility, along with modalities for pain management.', '#2T8L4', 10);
--back
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions) VALUES (1, 'Lumbar herniated disc ', 'Physical therapy focuses on relieving pain and improving spinal stability through exercises.', '#3N4P6', 14);
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions) VALUES (1, 'Sciatica', 'Treatment involves exercises to alleviate sciatic nerve compression and improve lumbar spine function.', '#9P1H4', 7);
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions) VALUES (1, 'Scoliosis', 'Therapy includes exercises to improve spinal alignment, flexibility, and muscle balance.', '#5R9N7', 8);
--neck
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions) VALUES (1, 'Cervical radiculopathy ', 'Rehabilitation aims to reduce neck pain and nerve symptoms through neck exercises and modalities.', '#8G5H3', 8);
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions) VALUES (1, 'Whiplash', 'Physical therapy focuses on neck strengthening and range of motion exercises to alleviate pain and stiffness.', '#8H6H1', 10);
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions) VALUES (1, 'Postural disorders', 'Treatment includes exercises to correct posture and improve muscle balance to relieve pain and prevent future issues.', '#8K1H7', 7);

INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions) VALUES (2, 'Post-Operative rehabilitation', 'EMS therapy aids in muscle activation and circulation for post-operative patients, helping to prevent muscle atrophy and accelerate recovery through controlled electrical muscle contractions.', '#3J6K2', 12);
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions) VALUES (2, 'Stroke rehabilitation', 'EMS can be used to stimulate paralyzed or weakened muscles, aiding in muscle re-education and motor function recovery for stroke survivors.', '#9P3R1', 20);
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions) VALUES (2, 'Muscle atrophy prevention', 'EMS assists in maintaining muscle strength and mass in individuals with limited mobility, such as bedridden or immobilized patients, by providing passive muscle contractions', '#2J6Z7', 8);
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions) VALUES (2, 'Chronic pain management', 'EMS therapy can help manage chronic pain conditions by stimulating endorphin release, reducing pain perception, and improving blood circulation in targeted areas.', '#7X4N5', 6);

INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions) VALUES (3, 'Post-Operative swelling minimization', 'Cryotherapy involves the application of cold therapy to reduce post-operative swelling and inflammation, promoting faster healing and pain relief.', '#5M8P4', 6);
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions) VALUES (3, 'Arthritis pain management', 'Cryotherapy helps alleviate arthritis pain by numbing the affected joints, reducing inflammation, and temporarily easing discomfort and stiffness.', '#6F6P9', 8);
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions) VALUES (3, 'Acute sports injury', 'Cryotherapy is used immediately after an acute sports injury to minimize swelling, numb the area, and provide pain relief while facilitating faster recovery.', '#5Z9B5', 8);
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions) VALUES (3, 'Tendinitis', 'Cryotherapy can be employed to reduce inflammation and alleviate pain associated with tendinitis by constricting blood vessels and diminishing tissue swelling in the affected area.', '#1G8Z3', 10);

INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions) VALUES (4, 'Chronic low back pain management', 'Hydrotherapy involves warm water exercises and aquatic therapy techniques to improve flexibility, reduce pain, and enhance core strength in individuals with chronic low back pain.', '#4W3F2', 10);
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions) VALUES (4, 'Osteoarthritis management', 'Hydrotherapy provides a low-impact environment for joint mobility exercises and pain relief, helping individuals with osteoarthritis improve joint function and reduce discomfort.', '#4D5C2', 8);
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions) VALUES (4, 'Post-Stroke rehabilitation', 'Hydrotherapy offers a supportive and buoyant environment for stroke survivors to work on regaining ', '#8D2H9', 20);
INSERT INTO therapy_type (required_equipment_id, name, description, therapy_Code, num_of_sessions) VALUES (4, 'Post-Operative Rehabilitation', 'Hydrotherapy can aid in the recovery process after surgery by offering a gentle way to regain mobility, reduce swelling, and improve overall conditioning through aquatic exercises and controlled movements in warm water.', '#5I9K8', 12);


INSERT INTO health_refferal (health_refferal_id, hlkid, mbo, therapy_code) VALUES ('DGC_167819/015H2463F8T600004D7J', 'HR019618', 160378294, '#3N4P6');
INSERT INTO health_refferal (health_refferal_id, hlkid, mbo, therapy_code) VALUES ('DGC_167820/015H2463F8T600004D7K', 'HR061864', 234567890, '#9P1H4');
INSERT INTO health_refferal (health_refferal_id, hlkid, mbo, therapy_code) VALUES ('DGC_167821/015H2463F8T600004D7L', 'HR271982', 345678901, '#5R9N7');
INSERT INTO health_refferal (health_refferal_id, hlkid, mbo, therapy_code) VALUES ('DGC_167822/015H2463F8T600004D7M', 'HR282164', 456789012, '#8G5H3');
INSERT INTO health_refferal (health_refferal_id, hlkid, mbo, therapy_code) VALUES ('DGC_167823/015H2463F8T600004D7N', 'HR005951', 567890123, '#8H6H1');
INSERT INTO health_refferal (health_refferal_id, hlkid, mbo, therapy_code) VALUES ('DGC_167824/015H2463F8T600004D7O', 'HR211078', 678901234, '#8K1H7');
INSERT INTO health_refferal (health_refferal_id, hlkid, mbo, therapy_code) VALUES ('DGC_167825/015H2463F8T600004D7P', 'HR026260', 789012345, '#3J6K2');
INSERT INTO health_refferal (health_refferal_id, hlkid, mbo, therapy_code) VALUES ('DGC_167826/015H2463F8T600004D7Q', 'HR166491', 890123456, '#9P3R1');
INSERT INTO health_refferal (health_refferal_id, hlkid, mbo, therapy_code) VALUES ('DGC_167827/015H2463F8T600004D7R', 'HR095677', 901234567, '#4W3F2');
INSERT INTO health_refferal (health_refferal_id, hlkid, mbo, therapy_code) VALUES ('DGC_167828/015H2463F8T600004D7S', 'HR233633', 987654321, '#4D5C2');
INSERT INTO health_refferal (health_refferal_id, hlkid, mbo, therapy_code) VALUES ('DGC_167829/015H2463F8T600004D7T', 'HR206450', 876543210, '#8D2H9');

