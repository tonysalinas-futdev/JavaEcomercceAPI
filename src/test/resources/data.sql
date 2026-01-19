INSERT INTO Categories(id,name, description) VALUES(5,'Otros','Otros productos');

INSERT INTO Products (name, description, price, pic, stock, available, category_id) VALUES ('Laptop Lenovo ThinkPad', 'Ultrabook empresarial con 16GB RAM y SSD 512GB', 1200.00, 'thinkpad.jpg', 15, TRUE, 5);
INSERT INTO Products (name, description, price, pic, stock, available, category_id) VALUES ('Smartphone Samsung Galaxy S23', 'Teléfono Android gama alta con cámara triple', 950.00, 'galaxy_s23.jpg', 30, TRUE, 5);
INSERT INTO Products (name, description, price, pic, stock, available, category_id) VALUES ('Auriculares Sony WH-1000XM5', 'Auriculares inalámbricos con cancelación de ruido', 350.00, 'sony_wh1000xm5.jpg', 25, TRUE, 5);
INSERT INTO Products (name, description, price, pic, stock, available, category_id) VALUES ('Monitor LG UltraWide 34', 'Monitor 34 pulgadas ultrawide para productividad', 600.00, 'lg_ultrawide.jpg', 10, TRUE, 5);
INSERT INTO Products (name, description, price, pic, stock, available, category_id) VALUES ('Teclado Mecánico Logitech G Pro', 'Teclado gamer con switches GX Blue', 120.00, 'logitech_gpro.jpg', 40, TRUE, 5);
INSERT INTO Products (name, description, price, pic, stock, available, category_id) VALUES ('Mouse Razer DeathAdder V2', 'Mouse gamer ergonómico con 20K DPI', 70.00, 'razer_deathadder.jpg', 50, TRUE, 1);
INSERT INTO Products (name, description, price, pic, stock, available, category_id) VALUES ('Silla Gamer Secretlab Titan', 'Silla ergonómica con soporte lumbar ajustable', 450.00, 'secretlab_titan.jpg', 12, TRUE, 5);
INSERT INTO Products (name, description, price, pic, stock, available, category_id) VALUES ('Disco Duro Externo Seagate 2TB', 'Almacenamiento portátil USB 3.0', 85.00, 'seagate_2tb.jpg', 60, TRUE, 5);
INSERT INTO Products (name, description, price, pic, stock, available, category_id) VALUES ('Tablet Apple iPad Air', 'Pantalla Liquid Retina de 10.9 pulgadas', 650.00, 'ipad_air.jpg', 20, TRUE, 5);
INSERT INTO Products (name, description, price, pic, stock, available, category_id) VALUES ('Impresora HP LaserJet Pro', 'Impresora láser monocromo rápida y eficiente', 300.00, 'hp_laserjet.jpg', 18, TRUE, 5);
INSERT INTO Products (name, description, price, pic, stock, available, category_id) VALUES ('Laptop HP', 'Laptop rapida y potente', 400.00, 'pc_hp.jpg', 18, TRUE, 5);


INSERT INTO Roles(id, role_name) VALUES(1 ,'USER'), (2 ,'ADMIN'), (3,'MANAGER');
INSERT INTO Permissions(id, permission_name) VALUES(1, 'EDIT_CATALOGUE'), (2, 'CREATE_USER'),(3,'DELETE_USER'), (4,'UPDATE_USER');
INSERT INTO role_permission(role_id,permission_id)VALUES(2,1),(3,1), (2,2), (2,3),(2,4);