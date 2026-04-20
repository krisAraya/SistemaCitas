CREATE DATABASE sistema_citas;
USE sistema_citas;
CREATE TABLE Usuarios (
  id_usuario INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  correo VARCHAR(100) UNIQUE NOT NULL,
  contraseña VARCHAR(100) NOT NULL,
  rol VARCHAR(20) NOT NULL,
  especialidad VARCHAR(100),
  telefono VARCHAR(20),
  direccion VARCHAR(200)
);

-- TABLA CITAS
CREATE TABLE Citas (
  id_cita INT AUTO_INCREMENT PRIMARY KEY,
  id_paciente INT,
  id_medico INT,
  fecha DATE,
  hora TIME,
  estado VARCHAR(20),
  FOREIGN KEY (id_paciente) REFERENCES Usuarios(id_usuario),
  FOREIGN KEY (id_medico) REFERENCES Usuarios(id_usuario)
);

-- TABLA HISTORIAL
CREATE TABLE Historial (
  id_historial INT AUTO_INCREMENT PRIMARY KEY,
  id_cita INT,
  id_paciente INT,	
  id_medico INT,
  notas TEXT,
  FOREIGN KEY (id_cita) REFERENCES Citas(id_cita)
);

-- TABLA NOTIFICACIONES
CREATE TABLE Notificaciones (
  id_notificacion INT AUTO_INCREMENT PRIMARY KEY,
  id_usuario INT,
  id_cita INT,
  accion VARCHAR(50),
  fecha_hora DATETIME
);

ALTER TABLE Usuarios 
MODIFY rol ENUM('paciente', 'medico') NOT NULL;

ALTER TABLE Citas 
MODIFY estado ENUM('pendiente', 'confirmada', 'cancelada');

INSERT INTO Usuarios (nombre, correo, contraseña, rol)
VALUES ('Kris', 'kris@gmail.com', '1234', 'paciente');