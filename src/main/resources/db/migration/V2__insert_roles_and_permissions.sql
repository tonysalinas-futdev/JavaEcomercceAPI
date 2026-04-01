INSERT INTO Roles(id, role_name) VALUES (1,'ADMIN'),(2,'USER'),(3,'MANAGER') ON CONFLICT (id) DO NOTHING ;

INSERT INTO Permissions(id, permission_name) VALUES(1,'EDIT_CATALOGUE'), (2,'CREATE_USER'),(3,'DELETE_USER'),(4,'UPDATE_USER') ON CONFLICT(id) DO NOTHING;

INSERT INTO role_permission(role_id,permission_id) VALUES(1,1),
(1,2),(1,3),(1,4),(3,1);