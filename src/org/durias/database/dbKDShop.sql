drop database if exists dbKDShop;

create database dbKDShop;

use dbKDShop;

create table Clientes(
    codigoCliente int,
    nitCliente varchar (45),
    nombreCliente varchar (45),
    apellidoCliente varchar (128),
    direccionCliente varchar (128),
    telefonoCliente varchar (13),
    correoCliente varchar (128),
    primary key PK_codigoCliente(codigoCliente)
);


create table TipoProducto(
	codigoTipoProducto int ,
    descripcionProducto varchar (45),
    primary key PK_TipoProducto(codigoTipoProducto)
);

create table Compras(
	numeroDocumento int,
    fechaDocumento date,
    descripcionCompra varchar(60),
    totalDocumento decimal(20,2),
    primary key PK_NumeroDocumento(numeroDocumento)
);

create table proveedores(
	codigoProveedor int,
    NITProveedor varchar(13),
    nombresProveedor varchar(60),
    apellidosProveedor varchar (60),
    direccionProveedor varchar (150),
    razonSocial varchar (60),
    contactoPrincipal varchar (100),
    paginaWeb varchar(50),
    telefonoProveedor varchar (13),
    emailProveedor varchar(50),
    primary key PK_codigoProveedor(codigoProveedor)
);

create table CargoEmpleado (
	codigoCargoEmpleado int,
    nombreCargo varchar (45),
    descripcionCargo varchar (82),
    primary key PK_codigoCargoEmpleado (codigoCargoEmpleado)
);

create table Productos(
	codigoProducto varchar(15),
	descripcionProducto varchar(15),
	precioUnitario decimal(10,2),
	precioDocena decimal(10,2),
	precioMayor decimal(10,2),
	imagenProducto varchar(45),
	existencia int,
	codigoTipoProducto int,
	codigoProveedor int,
	primary key  PK_codigoProducto (codigoProducto),
	foreign key (codigoTipoProducto) references TipoProducto(codigoTipoProducto),
	foreign key (codigoProveedor) references Proveedores(codigoProveedor)
);

CREATE TABLE DetalleCompra(

codigoDetalleCompra int,
costoUnitario decimal(10,2),
cantidad int,
codigoProducto varchar(15),
numeroDocumento int,
PRIMARY KEY PK_codigoDetalleCompra (codigoDetalleCompra),
FOREIGN KEY (codigoProducto) REFERENCES Productos(codigoProducto),
FOREIGN KEY (numeroDocumento) REFERENCES Compras(numeroDocumento)
    
);

CREATE TABLE Empleados(

codigoEmpleado int,
nombresEmpleado varchar(50),
apellidosEmpleado varchar(50),
sueldo decimal(10,2),
direccion varchar(150),
turno varchar(15),
codigoCargoEmpleado int,
PRIMARY KEY PK_codigoEmpleado (codigoEmpleado),
FOREIGN KEY (codigoCargoEmpleado) REFERENCES CargoEmpleado(codigoCargoEmpleado)

);


CREATE TABLE Factura(

numeroDeFactura int,
estado varchar(50),
totalFactura decimal(10,2),
fechaFactura varchar(45),
codigoCliente int,
codigoEmpleado int,
PRIMARY KEY PK_numeroDeFactura (numeroDeFactura),
FOREIGN KEY (codigoCliente) REFERENCES Clientes(codigoCliente),
FOREIGN KEY (codigoEmpleado) REFERENCES Empleados(codigoEmpleado)

);


CREATE TABLE DetalleFactura(

codigoDetalleFactura int,
precioUnitario decimal(10,2),
cantidad int,
numeroDeFactura int,
codigoProducto varchar(15),
PRIMARY KEY PK_codigoDetalleFactura (codigoDetalleFactura),
FOREIGN KEY (numeroDeFactura) REFERENCES Factura(numeroDeFactura),
FOREIGN KEY (codigoProducto) REFERENCES Productos(codigoProducto)

);

delimiter $$
create procedure sp_AgregarCliente(in codigoCliente int, in nitCliente varchar (45), in nombreCliente varchar(45), in apellidoCliente varchar(128) , in direccionCliente varchar(128), in telefonoCliente varchar(13),in correoCliente varchar(128))
begin
	insert into Clientes(codigoCliente,nitCliente,nombreCliente,apellidoCliente,direccionCliente,telefonoCliente,correoCliente)
		values(codigoCliente,nitCliente,nombreCliente,apellidoCliente,direccionCliente,telefonoCliente,correoCliente);
end$$
delimiter ;

CALL sp_AgregarCliente('5', '12345678', 'Juan', 'López', 'Colonia Centro', '1234567890', 'juan@example.com');
CALL sp_AgregarCliente(20, '123123123', 'Pedro', 'García', 'Colonia Reformita', '555987654', 'pedro@example.com');
CALL sp_AgregarCliente(30, '456456456', 'Ana', 'Martínez', 'Zona 10', '555456789', 'ana@example.com');


delimiter $$
create procedure sp_MostrarClientes()
begin
	select * from Clientes;
end$$
delimiter ;

call sp_MostrarClientes();

delimiter $$
create procedure sp_actualizarclientes(
    in codigocliente int,
    in nuevonombrecliente varchar(128),
    in nuevoapellidocliente varchar(128),
    in nuevadireccioncliente varchar(128),
    in nuevotelefonocliente varchar(13),
    in nuevocorreocliente varchar(128)
)
begin
    update clientes
    set nombrecliente = nuevonombrecliente,
        apellidocliente = nuevoapellidocliente,
        direccioncliente = nuevadireccioncliente,
        telefonocliente = nuevotelefonocliente,
        correocliente = nuevocorreocliente
    where codigocliente = codigocliente;
end$$
delimiter ;


call sp_actualizarClientes('5', 'Pedro', 'López', 'Colonia Centro', '1234567890', 'juan@example.com');

delimiter $$
create procedure sp_EliminarClientes(in codigoCliente int)
begin
	delete from Clientes
    where codigoCliente = codigoCliente;
end$$
delimiter ;

call sp_EliminarClientes(30);

delimiter $$
create procedure sp_agregartipoproducto(
    in codigotipoproducto int,
    in descripcionproducto varchar(45)
)
begin
    insert into tipoproducto (codigotipoproducto, descripcionproducto) values (codigotipoproducto, descripcionproducto);
end$$
delimiter ;

call sp_agregartipoproducto(1, 'Frutas');


delimiter $$
create procedure sp_mostrartiposproducto()
begin
    select * from tipoproducto;
end$$
delimiter ;

call sp_mostrartiposproducto();

delimiter $$
create procedure sp_actualizartipoproducto(
    in codigotipoproducto int,
    in _descripcion varchar(45)
)
begin
    update TipoProducto
    set descripcionProducto = _descripcion
    where codigoTipoProducto = codigotipoproducto;
end$$
delimiter ;


delimiter $$
create procedure sp_eliminartipoproducto(
    in codigotipoproducto int
)
begin
    delete from tipoproducto where codigotipoproducto = codigotipoproducto;
end$$
delimiter ;

call sp_eliminartipoproducto(1);

delimiter $$
create procedure sp_agregarcompra(
    in numerodocumento int,
    in fechadocumento date,
    in descripcioncompra varchar(60),
    in totaldocumento decimal(20,2)
)
begin
    insert into compras (numerodocumento, fechadocumento, descripcioncompra, totaldocumento) values (numerodocumento, fechadocumento, descripcioncompra, totaldocumento);
end$$
delimiter ;

call sp_agregarcompra(1, '2024-05-10', 'Compra de frutas', 150.50);

delimiter $$
create procedure sp_mostrarcompras()
begin
    select * from compras;
end$$
delimiter ;

call sp_mostrarcompras();

delimiter $$
create procedure sp_actualizarcompra(
    in numerodocumento int,
    in fechadocumento date,
    in descripcioncompra varchar(60),
    in totaldocumento decimal(20,2)
)
begin
    update Compras
    set fechaDocumento = fechadocumento,
        descripcionCompra = descripcioncompra,
        totalDocumento = totaldocumento
    where numeroDocumento = numerodocumento;
end$$
delimiter ;


delimiter $$
create procedure sp_eliminarcompra(
    in numerodocumento int
)
begin
    delete from compras where numerodocumento = numerodocumento;
end$$
delimiter ;

call sp_eliminarcompra(1);

delimiter $$
create procedure sp_agregarproveedor(
    in codigoproveedor int,
    in nitproveedor varchar(13),
    in nombresproveedor varchar(60),
    in apellidosproveedor varchar(60),
    in direccionproveedor varchar(150),
    in razonsocial varchar(60),
    in contactoprincipal varchar(100),
    in paginaweb varchar(50),
    in telefonoproveedor varchar(13),
    in emailproveedor varchar(50)
)
begin
    insert into proveedores (codigoproveedor, nitproveedor, nombresproveedor, apellidosproveedor, direccionproveedor, razonsocial, contactoprincipal, paginaweb, telefonoproveedor, emailproveedor) values (codigoproveedor, nitproveedor, nombresproveedor, apellidosproveedor, direccionproveedor, razonsocial, contactoprincipal, paginaweb, telefonoproveedor, emailproveedor);
end$$
delimiter ;

call sp_agregarproveedor(1, '987654321', 'Distribuidora ABC', 'S.A.', 'Avenida Principal', 'Distribuidora ABC S.A.', 'Juan Pérez', 'www.distribuidoraabc.com', '555-123456', 'info@distribuidoraabc.com');

delimiter $$
create procedure sp_mostrarproveedores()
begin
    select * from proveedores;
end$$
delimiter ;

delimiter $$
create procedure sp_actualizarproveedor(
    in codigoproveedor int,
    in _nit varchar(13),
    in _nombres varchar(60),
    in _apellidos varchar(60),
    in _direccion varchar(150),
    in _razonsocial varchar(60),
    in _contactoprincipal varchar(100),
    in _paginaweb varchar(50),
    in _telefono varchar(13),
    in _email varchar(50)
)
begin
    update proveedores
    set NITProveedor = _nit,
        nombresProveedor = _nombres,
        apellidosProveedor = _apellidos,
        direccionProveedor = _direccion,
        razonSocial = _razonsocial,
        contactoPrincipal = _contactoprincipal,
        paginaWeb = _paginaweb,
        telefonoProveedor = _telefono,
        emailProveedor = _email
    where codigoProveedor = codigoproveedor;
end$$
delimiter ;


call sp_mostrarproveedores();

delimiter $$
create procedure sp_eliminarproveedor(
    in codigoproveedor int
)
begin
    delete from proveedores where codigoproveedor = codigoproveedor;
end$$
delimiter ;

call sp_eliminarproveedor(1);


delimiter $$
create procedure sp_agregarcargoempleado(
    in codigo int,
    in nombre varchar(45),
    in descripcion varchar(82)
)
begin
    insert into cargoempleado (codigocargoempleado, nombrecargo, descripcioncargo) 
		values (codigo, nombre, descripcion);
end$$
delimiter ;

call sp_agregarcargoempleado(1,"administrado","mandar");


delimiter $$
create procedure sp_mostrarcargoempleados()
begin
    select * from cargoempleado;
end$$
delimiter ;

call sp_mostrarcargoempleados();

delimiter $$
create procedure sp_actualizarcargoempleado(
    in codigo int,
    in nombre varchar(45),
    in descripcion varchar(82)
)
begin
    update cargoempleado
    set nombrecargo = nombre, descripcioncargo = descripcion
    where codigocargoempleado = codigo;
end$$
delimiter ;

call sp_actualizarcargoempleado(1,"jefe","dueño");

delimiter $$
create procedure sp_eliminarcargoempleado(
    in codigo int
)
begin
    delete from cargoempleado 
    where codigocargoempleado = codigo;
end$$
delimiter ;

call sp_eliminarcargoempleado(1);

    