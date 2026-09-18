

-- CODE N BUGS - MODELO FISICO DE BASE DE DATOS

CREATE DATABASE IF NOT EXISTS codenbugs
DEFAULT CHARACTER SET utf8mb4
DEFAULT COLLATE utf8mb4_unicode_ci;

USE codenbugs;


-- SUCURSALES

CREATE TABLE Sucursal (
    id_sucursal INT AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(150) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    estado ENUM('ACTIVA', 'INACTIVA') NOT NULL DEFAULT 'ACTIVA',

    PRIMARY KEY (id_sucursal),
    UNIQUE (nombre)
);


-- CONFIGURACION DEL SISTEMA

CREATE TABLE ConfiguracionSistema (
    id_config INT AUTO_INCREMENT,
    monto_depreciacion_km DECIMAL(10,2) NOT NULL,
    fecha_actualizacion DATE NOT NULL,

    PRIMARY KEY (id_config)
);


-- USUARIOS

CREATE TABLE Usuario (
    id_usuario INT AUTO_INCREMENT,
    id_sucursal INT NULL,
    dpi VARCHAR(20) NOT NULL,
    nombre_completo VARCHAR(150) NOT NULL,
    nit VARCHAR(20) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    direccion VARCHAR(150) NOT NULL,
    rol ENUM(
        'ADMINISTRADOR',
        'ADMINISTRADOR_DE_SUCURSAL',
        'CLIENTE'
    ) NOT NULL,
    estado ENUM('ACTIVO', 'INACTIVO') NOT NULL DEFAULT 'ACTIVO',
    correo VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,

    PRIMARY KEY (id_usuario),

    UNIQUE (dpi),
    UNIQUE (nit),
    UNIQUE (correo),

    FOREIGN KEY (id_sucursal)
        REFERENCES Sucursal(id_sucursal)
);


-- BUSES

CREATE TABLE Bus (
    id_bus INT AUTO_INCREMENT,
    id_sucursal INT NOT NULL,
    foto VARCHAR(255),
    placa VARCHAR(20) NOT NULL,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    anio_fabricacion INT NOT NULL,
    capacidad INT NOT NULL,
    estado_operativo ENUM(
        'DISPONIBLE',
        'EN_VIAJE',
        'EN_MANTENIMIENTO',
        'INACTIVO'
    ) NOT NULL DEFAULT 'DISPONIBLE',
    kilometraje_actual DECIMAL(10,2) NOT NULL DEFAULT 0,

    PRIMARY KEY (id_bus),

    UNIQUE (placa),

    FOREIGN KEY (id_sucursal)
        REFERENCES Sucursal(id_sucursal)
);


-- CHOFERES

CREATE TABLE Chofer (
    id_chofer INT AUTO_INCREMENT,
    id_sucursal INT NOT NULL,
    foto VARCHAR(255),
    nombre_completo VARCHAR(150) NOT NULL,
    numero_licencia VARCHAR(50) NOT NULL,
    tipo_licencia VARCHAR(30) NOT NULL,
    fecha_vencimiento DATE NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    salario_base_viaje DECIMAL(10,2) NOT NULL,
    estado ENUM('ACTIVO', 'INACTIVO') NOT NULL DEFAULT 'ACTIVO',

    PRIMARY KEY (id_chofer),

    UNIQUE (numero_licencia),

    FOREIGN KEY (id_sucursal)
        REFERENCES Sucursal(id_sucursal)
);


-- RUTAS

CREATE TABLE Ruta (
    id_ruta INT AUTO_INCREMENT,
    id_sucursal_origen INT NOT NULL,
    id_sucursal_destino INT NOT NULL,
    distancia_km DECIMAL(10,2) NOT NULL,
    precio_boleto DECIMAL(10,2) NOT NULL,
    estado ENUM('ACTIVA', 'INACTIVA') NOT NULL DEFAULT 'ACTIVA',

    PRIMARY KEY (id_ruta),

    FOREIGN KEY (id_sucursal_origen)
        REFERENCES Sucursal(id_sucursal),

    FOREIGN KEY (id_sucursal_destino)
        REFERENCES Sucursal(id_sucursal)
);


-- VIAJES

CREATE TABLE Viaje (
    id_viaje INT AUTO_INCREMENT,
    id_bus INT NULL,
    id_chofer INT NULL,
    id_ruta INT NULL,

    tipo_viaje ENUM(
        'REGULAR',
        'PRIVADO'
    ) NOT NULL,

    fecha_salida DATETIME NOT NULL,
    fecha_llegada_estimada DATETIME NOT NULL,

    estado ENUM(
        'PROGRAMADO',
        'EN_TRANSITO',
        'FINALIZADO',
        'CANCELADO'
    ) NOT NULL DEFAULT 'PROGRAMADO',

    PRIMARY KEY (id_viaje),

    FOREIGN KEY (id_bus)
        REFERENCES Bus(id_bus),

    FOREIGN KEY (id_chofer)
        REFERENCES Chofer(id_chofer),

    FOREIGN KEY (id_ruta)
        REFERENCES Ruta(id_ruta)
);


-- REGISTRO DE SALIDA

CREATE TABLE RegistroSalida (
    id_salida INT AUTO_INCREMENT,
    id_viaje INT NOT NULL,
    id_bus INT NOT NULL,
    id_chofer INT NOT NULL,
    fecha_hora_salida_real DATETIME NOT NULL,
    kilometraje_inicial DECIMAL(10,2) NOT NULL,

    PRIMARY KEY (id_salida),

    UNIQUE (id_viaje),

    FOREIGN KEY (id_viaje)
        REFERENCES Viaje(id_viaje),

    FOREIGN KEY (id_bus)
        REFERENCES Bus(id_bus),

    FOREIGN KEY (id_chofer)
        REFERENCES Chofer(id_chofer)
);


-- REGISTRO DE LLEGADA

CREATE TABLE RegistroLlegada (
    id_llegada INT AUTO_INCREMENT,
    id_viaje INT NOT NULL,
    fecha_hora_llegada_real DATETIME NOT NULL,
    kilometraje_final DECIMAL(10,2) NOT NULL,
    gasto_combustible DECIMAL(10,2) NOT NULL,
    monto_depreciacion DECIMAL(10,2) NOT NULL,

    PRIMARY KEY (id_llegada),

    UNIQUE (id_viaje),

    FOREIGN KEY (id_viaje)
        REFERENCES Viaje(id_viaje)
);


-- CARTERA DIGITAL

CREATE TABLE Cartera (
    id_cartera INT AUTO_INCREMENT,
    id_usuario INT NOT NULL,
    saldo DECIMAL(10,2) NOT NULL DEFAULT 0,

    PRIMARY KEY (id_cartera),

    UNIQUE (id_usuario),

    FOREIGN KEY (id_usuario)
        REFERENCES Usuario(id_usuario)
);


-- MOVIMIENTOS DE CARTERA

CREATE TABLE MovimientoCartera (
    id_movimiento INT AUTO_INCREMENT,
    id_cartera INT NOT NULL,
    tipo ENUM(
        'RECARGA',
        'PAGO'
    ) NOT NULL,
    monto DECIMAL(10,2) NOT NULL,
    fecha DATE NOT NULL,
    descripcion VARCHAR(255),

    PRIMARY KEY (id_movimiento),

    FOREIGN KEY (id_cartera)
        REFERENCES Cartera(id_cartera)
);


-- BOLETOS

CREATE TABLE Boleto (
    id_boleto INT AUTO_INCREMENT,
    id_viaje INT NOT NULL,
    id_usuario INT NOT NULL,
    numero_asiento INT NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    fecha_pago DATE NOT NULL,
    estado ENUM(
        'PAGADO',
        'CANCELADO'
    ) NOT NULL DEFAULT 'PAGADO',

    PRIMARY KEY (id_boleto),

    UNIQUE (id_viaje, numero_asiento),

    FOREIGN KEY (id_viaje)
        REFERENCES Viaje(id_viaje),

    FOREIGN KEY (id_usuario)
        REFERENCES Usuario(id_usuario)
);


-- ALQUILER PRIVADO

CREATE TABLE Alquiler (
    id_alquiler INT AUTO_INCREMENT,
    id_viaje INT NOT NULL,
    id_cliente INT NOT NULL,

    origen VARCHAR(150) NOT NULL,
    destino VARCHAR(150) NOT NULL,

    fecha_salida DATE NOT NULL,
    fecha_retorno DATE NULL,

    cantidad_pasajeros INT NOT NULL,

    precio_estimado DECIMAL(10,2) NOT NULL,
    precio_confirmado DECIMAL(10,2) NULL,

    estado ENUM(
        'SOLICITADO',
        'CONFIRMADO',
        'PAGADO',
        'FINALIZADO',
        'CANCELADO'
    ) NOT NULL DEFAULT 'SOLICITADO',

    PRIMARY KEY (id_alquiler),

    UNIQUE (id_viaje),

    FOREIGN KEY (id_viaje)
        REFERENCES Viaje(id_viaje),

    FOREIGN KEY (id_cliente)
        REFERENCES Usuario(id_usuario)
);


-- GASTOS DE TALLER

CREATE TABLE Mantenimiento (
    id_mantenimiento INT AUTO_INCREMENT,
    id_bus INT NOT NULL,
    monto_mano_obra DECIMAL(10,2) NOT NULL,
    monto_repuestos DECIMAL(10,2) NOT NULL DEFAULT 0,
    fecha_mantenimiento DATE NOT NULL,
    descripcion VARCHAR(255),

    PRIMARY KEY (id_mantenimiento),

    FOREIGN KEY (id_bus)
        REFERENCES Bus(id_bus)
);

INSERT INTO Usuario (dpi, nombre_completo, nit, telefono, direccion, rol, estado, correo, password)
VALUES ('1234567890101', 'Admin Sistema', '1234567-8', '12345678', 'Sede Central', 'ADMINISTRADOR', 'ACTIVO', 'admin@buses.com', '1234');

INSERT INTO ConfiguracionSistema (monto_depreciacion_km, fecha_actualizacion) VALUES (0.50, '2026-01-01');
