CREATE OR REPLACE TYPE address_t AS OBJECT (
    country        VARCHAR2(100),
    city           VARCHAR2(100),
    street_address VARCHAR2(200),
    postal_code    VARCHAR2(20)
);
/

CREATE TABLE Company (
    company_id NUMBER PRIMARY KEY,
    name       VARCHAR2(200) NOT NULL,
    eik        VARCHAR2(20) UNIQUE NOT NULL,
    address    address_t,
    phone      VARCHAR2(50)
);

CREATE TABLE Manufacturer (
    company_id NUMBER PRIMARY KEY 
        REFERENCES Company(company_id)
);

CREATE TABLE Customer (
    company_id     NUMBER PRIMARY KEY
        REFERENCES Company(company_id),
    pharmacy_code  VARCHAR2(50)
);

CREATE TABLE Supplier (
    company_id NUMBER PRIMARY KEY
        REFERENCES Company(company_id)
);

CREATE TABLE Ingredient (
    ingredient_id NUMBER PRIMARY KEY,
    name          VARCHAR2(200) NOT NULL
);

CREATE TABLE Product (
    product_id      NUMBER PRIMARY KEY,
    manufacturer_id NUMBER REFERENCES Manufacturer(company_id),
    name            VARCHAR2(200),
    form            VARCHAR2(50),
    gtin            VARCHAR2(20)
);

CREATE TABLE Product_Ingredient (
    product_id     NUMBER REFERENCES Product(product_id),
    ingredient_id  NUMBER REFERENCES Ingredient(ingredient_id),
    amount         NUMBER,
    unit           VARCHAR2(20),
    PRIMARY KEY (product_id, ingredient_id)
);

CREATE TABLE Product_Price (
    price_id    NUMBER PRIMARY KEY,
    product_id  NUMBER REFERENCES Product(product_id),
    valid_from  DATE,
    valid_to    DATE,
    price       NUMBER(10,2)
);

CREATE TABLE Medicine (
    product_id NUMBER PRIMARY KEY
        REFERENCES Product(product_id),
    rx_flag CHAR(1)
);

CREATE TABLE Device (
    product_id NUMBER PRIMARY KEY
        REFERENCES Product(product_id),
    risk_class VARCHAR2(20)
);

CREATE TABLE Batch (
    batch_id     NUMBER PRIMARY KEY,
    product_id   NUMBER REFERENCES Product(product_id),
    batch_no     VARCHAR2(50),
    expiry_date  DATE
);

CREATE TABLE Inventory (
    batch_id NUMBER PRIMARY KEY
        REFERENCES Batch(batch_id),
    qty      NUMBER,
    min_qty  NUMBER
);

CREATE TABLE Stock_Receipt (
    receipt_id   NUMBER PRIMARY KEY,
    supplier_id  NUMBER REFERENCES Supplier(company_id),
    doc_no       VARCHAR2(100),
    received_at  DATE,
    note         VARCHAR2(500)
);


CREATE TABLE Delivery (
    delivery_id   NUMBER PRIMARY KEY,
    customer_id   NUMBER REFERENCES Customer(company_id),
    doc_no        VARCHAR2(100),
    delivered_at  DATE,
    vehicle_plate VARCHAR2(50),
    carrier       VARCHAR2(200),
    note          VARCHAR2(500)
);

CREATE TABLE Delivery_Product (
    delivery_product_id NUMBER PRIMARY KEY,
    delivery_id         NUMBER REFERENCES Delivery(delivery_id),
    product_id          NUMBER REFERENCES Product(product_id),
    batch_id            NUMBER REFERENCES Batch(batch_id),
    qty                 NUMBER,
    unit_price          NUMBER(10,2)
);
