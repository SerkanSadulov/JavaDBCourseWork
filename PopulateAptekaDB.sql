INSERT INTO Company VALUES (1, 'PharmaCorp Ltd', 'BG12345678',
    address_t('Bulgaria', 'Sofia', 'Tsarigradsko Shose 12', '1000'),
    '+359 2 999 1111');

INSERT INTO Company VALUES (2, 'MediLife Manufacturer', 'BG87654321',
    address_t('Bulgaria', 'Plovdiv', 'Kapitan Raicho 55', '4000'),
    '+359 32 555 7777');

INSERT INTO Company VALUES (3, 'GreenPharm Pharmacy', 'BG55577700',
    address_t('Bulgaria', 'Varna', 'Slivnitsa 100', '9000'),
    '+359 52 222 4444');

INSERT INTO Company VALUES (4, 'HealSupply Ltd', 'BG22233344',
    address_t('Bulgaria', 'Burgas', 'Transportna 45', '8000'),
    '+359 56 880 990');

INSERT INTO Manufacturer VALUES (2);     
INSERT INTO Customer VALUES (3, 'PH123');
INSERT INTO Supplier VALUES (4);      

INSERT INTO Ingredient VALUES (1, 'Paracetamol');
INSERT INTO Ingredient VALUES (2, 'Ibuprofen');
INSERT INTO Ingredient VALUES (3, 'Glycerin');

INSERT INTO Product VALUES (10, 2, 'Paracetamol 500mg', 'Tablet', '3800000000011');
INSERT INTO Product VALUES (11, 2, 'Ibuprofen 200mg', 'Tablet', '3800000000028');
INSERT INTO Product VALUES (12, 2, 'Glycerin Nasal Spray', 'Spray', '3800000000035');

INSERT INTO Product_Ingredient VALUES (10, 1, 500, 'mg');
INSERT INTO Product_Ingredient VALUES (11, 2, 200, 'mg');
INSERT INTO Product_Ingredient VALUES (12, 3, 10, 'ml');

INSERT INTO Product_Price VALUES (101, 10, DATE '2024-01-01', NULL, 3.50);
INSERT INTO Product_Price VALUES (102, 11, DATE '2024-01-01', NULL, 4.20);
INSERT INTO Product_Price VALUES (103, 12, DATE '2024-01-01', NULL, 7.90);

INSERT INTO Medicine VALUES (10, 'Y'); 
INSERT INTO Medicine VALUES (11, 'N');  

INSERT INTO Device VALUES (12, 'Class IIa');

INSERT INTO Batch VALUES (1001, 10, 'PCH500A', DATE '2026-05-01');
INSERT INTO Batch VALUES (1002, 11, 'IBU200B', DATE '2025-11-15');
INSERT INTO Batch VALUES (1003, 12, 'GLYSPR01', DATE '2027-02-01');

INSERT INTO Inventory VALUES (1001, 500, 50);
INSERT INTO Inventory VALUES (1002, 300, 30);
INSERT INTO Inventory VALUES (1003, 150, 20);

INSERT INTO Stock_Receipt VALUES (501, 4, 'SR-2024-001', DATE '2024-01-20', 'Initial supply shipment');

INSERT INTO Delivery VALUES (600, 3, 'DL-2024-050', DATE '2024-01-25', 'CA5550AC', 'TransCorp', 'Routine delivery');

INSERT INTO Delivery_Product VALUES (7001, 600, 10, 1001, 50, 3.50); 
INSERT INTO Delivery_Product VALUES (7002, 600, 11, 1002, 30, 4.20); 
INSERT INTO Delivery_Product VALUES (7003, 600, 12, 1003, 10, 7.90);
