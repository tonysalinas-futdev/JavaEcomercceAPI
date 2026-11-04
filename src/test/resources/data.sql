INSERT INTO Categories(id,name, description) VALUES(5,'Otros','Otros productos');
INSERT INTO Categories(id,name, description) VALUES(3,'CategoriaPrueba','Categoría de prueba');


INSERT INTO Products (name, description, price, pic, stock, is_available, created_at, update_at, category_id)
VALUES ('Laptop Lenovo ThinkPad', 'Ultrabook empresarial con 16GB RAM y SSD 512GB', 1200.00, 'thinkpad.jpg', 15, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5);

INSERT INTO Products (name, description, price, pic, stock, is_available, created_at, update_at, category_id)
VALUES ('Smartphone Samsung Galaxy S23', 'Teléfono Android gama alta con cámara triple', 950.00, 'galaxy_s23.jpg', 30, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5);

INSERT INTO Products (name, description, price, pic, stock, is_available, created_at, update_at, category_id)
VALUES ('Auriculares Sony WH-1000XM5', 'Auriculares inalámbricos con cancelación de ruido', 350.00, 'sony_wh1000xm5.jpg', 25, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5);

INSERT INTO Products (name, description, price, pic, stock, is_available, created_at, update_at, category_id)
VALUES ('Monitor LG UltraWide 34', 'Monitor 34 pulgadas ultrawide para productividad', 600.00, 'lg_ultrawide.jpg', 10, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5);

INSERT INTO Products (name, description, price, pic, stock, is_available, created_at, update_at, category_id)
VALUES ('Teclado Mecánico Logitech G Pro', 'Teclado gamer con switches GX Blue', 120.00, 'logitech_gpro.jpg', 40, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5);

INSERT INTO Products (name, description, price, pic, stock, is_available, created_at, update_at, category_id)
VALUES ('Mouse Razer DeathAdder V2', 'Mouse gamer ergonómico con 20K DPI', 70.00, 'razer_deathadder.jpg', 50, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5);

INSERT INTO Products (name, description, price, pic, stock, is_available, created_at, update_at, category_id)
VALUES ('Silla Gamer Secretlab Titan', 'Silla ergonómica con soporte lumbar ajustable', 450.00, 'secretlab_titan.jpg', 12, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5);

INSERT INTO Products (name, description, price, pic, stock, is_available, created_at, update_at, category_id)
VALUES ('Disco Duro Externo Seagate 2TB', 'Almacenamiento portátil USB 3.0', 85.00, 'seagate_2tb.jpg', 60, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5);

INSERT INTO Products (name, description, price, pic, stock, is_available, created_at, update_at, category_id)
VALUES ('Tablet Apple iPad Air', 'Pantalla Liquid Retina de 10.9 pulgadas', 650.00, 'ipad_air.jpg', 20, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5);

INSERT INTO Products (name, description, price, pic, stock, is_available, created_at, update_at, category_id)
VALUES ('Impresora HP LaserJet Pro', 'Impresora láser monocromo rápida y eficiente', 300.00, 'hp_laserjet.jpg', 18, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5);

INSERT INTO Products (name, description, price, pic, stock, is_available, created_at, update_at, category_id)
VALUES ('Laptop HP', 'Laptop rapida y potente', 400.00, 'pc_hp.jpg', 18, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5);


INSERT INTO Products (id, name, description, price, pic, stock, is_available, created_at, update_at, category_id)
VALUES (16, 'Laptop Lenovo 2026', 'Laptop rapida', 440.00, 'pc_hp.jpg', 18, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5);

INSERT INTO Products (id, name, description, price, pic, stock, is_available, created_at, update_at, category_id)
VALUES (17, 'PC Gamer 2026', 'PC potente', 1440.00, 'pc_hp.jpg', 40, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5);

INSERT INTO Products (id, name, description, price, pic, stock, is_available, created_at, update_at, category_id)
VALUES (19, 'Mouse Gamer 2026', 'Mouse para jugar', 110.00, 'pc_hp.jpg', 18, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5);


INSERT INTO Roles(id, role_name) VALUES(1 ,'USER'), (2 ,'ADMIN'), (3,'MANAGER');

INSERT INTO Permissions(id, permission_name) VALUES(1, 'EDIT_CATALOGUE'), (2, 'CREATE_USER'),(3,'DELETE_USER'), (4,'UPDATE_USER');

INSERT INTO role_permission(role_id,permission_id)VALUES(2,1),(3,1), (2,2), (2,3),(2,4);


INSERT INTO Users(id, joined_at, name, email, password)
VALUES(10, CURRENT_TIMESTAMP, 'Eduardo Camavinga Celmi', 'camacelmi@gmail.com', '12345678Abc#');


INSERT INTO Orders (id, request_id, created_at, update_at, status, total_amount)
VALUES
    (50, '550e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'PENDING', 99.99),
    (51, '550e8400-e29b-41d4-a716-446655440002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'PAID', 149.50),
    (52, '550e8400-e29b-41d4-a716-446655440003', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'CANCELED', 250.75),
    (53, '550e8400-e29b-41d4-a716-446655440004', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'PAID', 45.00),
    (54, '550e8230-e29b-41d4-a716-446655440004', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'PAID', 45.00);