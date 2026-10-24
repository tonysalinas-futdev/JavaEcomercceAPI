
INSERT INTO categories (name, description, pic)
VALUES
    ('Ordenadores', 'Portátiles y ordenadores de sobremesa', 'https://placehold.co/600x400?text=Ordenadores'),
    ('Móviles', 'Smartphones y teléfonos móviles', 'https://placehold.co/600x400?text=Moviles'),
    ('Periféricos', 'Teclados, ratones y accesorios informáticos', 'https://placehold.co/600x400?text=Perifericos'),
    ('Monitores', 'Monitores para trabajo y gaming', 'https://placehold.co/600x400?text=Monitores'),
    ('Audio', 'Auriculares, altavoces y micrófonos', 'https://placehold.co/600x400?text=Audio'),
    ('Accesorios', 'Cables, cargadores y complementos', 'https://placehold.co/600x400?text=Accesorios')
    ON CONFLICT (name) DO NOTHING;



INSERT INTO products (name, description, price, pic, stock, is_available, created_at, update_at, category_id)
VALUES
    ('Portátil Lenovo IdeaPad', 'Portátil de 15 pulgadas con 16 GB de RAM y SSD de 512 GB', 649.99, 'https://placehold.co/600x400?text=Lenovo', 15, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM categories WHERE name = 'Ordenadores')),
    ('Portátil ASUS VivoBook', 'Portátil ligero para estudiar y trabajar', 729.99, 'https://placehold.co/600x400?text=ASUS', 10, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM categories WHERE name = 'Ordenadores')),
    ('PC Gaming Ryzen 7', 'Ordenador gaming con 32 GB de RAM y SSD de 1 TB', 1299.99, 'https://placehold.co/600x400?text=PC+Gaming', 5, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM categories WHERE name = 'Ordenadores')),
    ('MacBook Air', 'Portátil ultraligero para productividad', 1199.00, 'https://placehold.co/600x400?text=MacBook', 0, FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM categories WHERE name = 'Ordenadores'));



INSERT INTO products (name, description, price, pic, stock, is_available, created_at, update_at, category_id)
VALUES
    ('Samsung Galaxy A55', 'Smartphone con pantalla AMOLED', 399.99, 'https://placehold.co/600x400?text=Samsung', 20, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM categories WHERE name = 'Móviles')),
    ('iPhone 16', 'Smartphone Apple con pantalla Super Retina', 899.00, 'https://placehold.co/600x400?text=iPhone', 8, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM categories WHERE name = 'Móviles')),
    ('Xiaomi Redmi Note', 'Smartphone con batería de larga duración', 249.99, 'https://placehold.co/600x400?text=Xiaomi', 25, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM categories WHERE name = 'Móviles')),
    ('Google Pixel', 'Smartphone Android con fotografía avanzada', 599.99, 'https://placehold.co/600x400?text=Pixel', 0, FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM categories WHERE name = 'Móviles'));



INSERT INTO products (name, description, price, pic, stock, is_available, created_at, update_at, category_id)
VALUES
    ('Teclado mecánico RGB', 'Teclado mecánico con iluminación RGB', 89.99, 'https://placehold.co/600x400?text=Teclado', 30, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM categories WHERE name = 'Periféricos')),
    ('Ratón inalámbrico Logitech', 'Ratón inalámbrico ergonómico', 34.99, 'https://placehold.co/600x400?text=Raton', 40, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM categories WHERE name = 'Periféricos')),
    ('Ratón Gaming Pro', 'Ratón gaming de alta precisión', 59.99, 'https://placehold.co/600x400?text=Gaming', 12, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM categories WHERE name = 'Periféricos')),
    ('Teclado Bluetooth', 'Teclado inalámbrico compacto', 44.99, 'https://placehold.co/600x400?text=Bluetooth', 18, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM categories WHERE name = 'Periféricos'));



INSERT INTO products (name, description, price, pic, stock, is_available, created_at, update_at, category_id)
VALUES
    ('Monitor LG 24 pulgadas', 'Monitor Full HD para oficina', 149.99, 'https://placehold.co/600x400?text=Monitor+LG', 14, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM categories WHERE name = 'Monitores')),
    ('Monitor Samsung 27 pulgadas', 'Monitor QHD para productividad', 279.99, 'https://placehold.co/600x400?text=Monitor+Samsung', 9, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM categories WHERE name = 'Monitores')),
    ('Monitor Gaming 144 Hz', 'Monitor gaming Full HD de 144 Hz', 229.99, 'https://placehold.co/600x400?text=Gaming+Monitor', 7, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM categories WHERE name = 'Monitores')),
    ('Monitor UltraWide', 'Monitor panorámico para multitarea', 449.99, 'https://placehold.co/600x400?text=UltraWide', 0, FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM categories WHERE name = 'Monitores'));


INSERT INTO products (name, description, price, pic, stock, is_available, created_at, update_at, category_id)
VALUES
    ('Auriculares Bluetooth', 'Auriculares inalámbricos con cancelación de ruido', 79.99, 'https://placehold.co/600x400?text=Auriculares', 22, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM categories WHERE name = 'Audio')),
    ('Auriculares Gaming', 'Auriculares gaming con micrófono integrado', 69.99, 'https://placehold.co/600x400?text=Auriculares+Gaming', 16, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM categories WHERE name = 'Audio')),
    ('Altavoz Bluetooth', 'Altavoz portátil inalámbrico', 49.99, 'https://placehold.co/600x400?text=Altavoz', 19, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM categories WHERE name = 'Audio')),
    ('Micrófono USB', 'Micrófono USB para streaming y videollamadas', 99.99, 'https://placehold.co/600x400?text=Microfono', 11, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM categories WHERE name = 'Audio'));



INSERT INTO products (name, description, price, pic, stock, is_available, created_at, update_at, category_id)
VALUES
    ('Cargador USB-C 65W', 'Cargador rápido compatible con USB-C', 29.99, 'https://placehold.co/600x400?text=Cargador', 35, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM categories WHERE name = 'Accesorios')),
    ('Cable USB-C', 'Cable USB-C de 2 metros', 12.99, 'https://placehold.co/600x400?text=Cable', 50, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM categories WHERE name = 'Accesorios')),
    ('Hub USB de 4 puertos', 'Concentrador USB para conectar dispositivos', 24.99, 'https://placehold.co/600x400?text=Hub+USB', 20, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM categories WHERE name = 'Accesorios')),
    ('Alfombrilla Gaming XL', 'Alfombrilla extragrande para teclado y ratón', 19.99, 'https://placehold.co/600x400?text=Alfombrilla', 28, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, (SELECT id FROM categories WHERE name = 'Accesorios'));


