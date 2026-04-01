CREATE EXTENSION IF NOT EXISTS pgcrypto;



CREATE TABLE IF NOT EXISTS Permissions(
    id SERIAL PRIMARY KEY,
    permission_name VARCHAR UNIQUE

);
CREATE TABLE IF NOT EXISTS Roles(
    id SERIAL PRIMARY KEY,
    role_name VARCHAR UNIQUE
);

CREATE TABLE IF NOT EXISTS role_permission(
    role_id INT REFERENCES Roles(id),
    permission_id INT REFERENCES Permissions(id),
    PRIMARY KEY (role_id, permission_id)
);

CREATE TABLE IF NOT EXISTS Users(
    id SERIAL PRIMARY KEY,
    joined_at TIMESTAMP,
    name VARCHAR(200) NOT NULL,
    email VARCHAR UNIQUE NOT NULL,
    password VARCHAR NOT NULL,
    is_enabled BOOLEAN,
    account_no_locked BOOLEAN, 
    credentials_no_expired BOOLEAN,
    role_id INT REFERENCES Roles(id)

);



CREATE TABLE IF NOT EXISTS categories(
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(2000),
    pic VARCHAR(50)
);


CREATE TABLE IF NOT EXISTS products(
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(2000),
    price NUMERIC(10,2),
    pic VARCHAR(50),
    stock INT,
    available BOOLEAN,
    created_at TIMESTAMP,
    update_at TIMESTAMP,
    category_id INT REFERENCES categories(id)
);




CREATE TABLE IF NOT EXISTS Orders(
    id SERIAL PRIMARY KEY,
    request_id UUID ,
    created_at TIMESTAMP,
    update_at TIMESTAMP,
    status VARCHAR(30),
    user_id BIGINT REFERENCES Users(id),
    total_amount NUMERIC(10,2)

);

CREATE TABLE IF NOT EXISTS Order_details(
    id SERIAL PRIMARY KEY,
    product_id BIGINT REFERENCES products(id),
    quantity INT ,
    order_id INT REFERENCES Orders(id)
);

CREATE TABLE IF NOT EXISTS Payments(
    id SERIAL PRIMARY KEY,
    order_id INTEGER,
    user_id INTEGER,
    amount NUMERIC(10,2),
    currency VARCHAR(15),
    payment_intent_id VARCHAR,
    status VARCHAR(10)

);

CREATE TABLE IF NOT EXISTS webhook_event(
    id SERIAL PRIMARY KEY,
    webhook_id VARCHAR,
    event_type VARCHAR,
    created_at TIMESTAMP
);


CREATE TABLE IF NOT EXISTS token(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    token TEXT UNIQUE ,
    type VARCHAR(30),
    revoked BOOLEAN,
    expired BOOLEAN,
    user_id BIGINT REFERENCES Users(id)

);


CREATE TABLE IF NOT EXISTS Cart(
    id SERIAL PRIMARY KEY,
    created_at TIMESTAMP,
    user_id BIGINT REFERENCES Users(id)
    
);

CREATE TABLE IF NOT EXISTS Cart_item(
    id SERIAL PRIMARY KEY,
    added_at TIMESTAMP,
    product_id INTEGER REFERENCES products(id),
    quantity INTEGER,
    cart_id BIGINT REFERENCES Cart(id)
    
);

