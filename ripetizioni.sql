-- phpMyAdmin SQL Dump
-- version 4.9.0.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 21-11-2019 a las 19:23:48
-- Versión del servidor: 5.7.27-0ubuntu0.18.04.1
-- Versión de PHP: 7.3.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `ripetizioni`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `CORSO`
--

CREATE TABLE `CORSO` (
  `titulo` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `DOCENTE`
--

CREATE TABLE `DOCENTE` (
  `nome` varchar(50) NOT NULL,
  `cognome` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ORARIO`
--

CREATE TABLE `ORARIO` (
  `giorno` int(2) NOT NULL,
  `inic` int(11) NOT NULL,
  `final` int(11) NOT NULL,
  `nome` varchar(50) NOT NULL,
  `cognome` varchar(50) NOT NULL,
  `corso` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `RIPETIZIONE`
--

CREATE TABLE `RIPETIZIONE` (
  `giorno` int(2) NOT NULL,
  `inic` int(11) NOT NULL,
  `final` int(11) NOT NULL,
  `nome` varchar(50) NOT NULL,
  `cognome` varchar(50) NOT NULL,
  `account` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `UTENTE`
--

CREATE TABLE `UTENTE` (
  `account` varchar(50) NOT NULL,
  `password` varchar(50) DEFAULT NULL,
  `ruolo` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `CORSO`
--
ALTER TABLE `CORSO`
  ADD PRIMARY KEY (`titulo`);

--
-- Indices de la tabla `DOCENTE`
--
ALTER TABLE `DOCENTE`
  ADD PRIMARY KEY (`nome`,`cognome`);

--
-- Indices de la tabla `ORARIO`
--
ALTER TABLE `ORARIO`
  ADD PRIMARY KEY (`giorno`,`inic`,`final`,`nome`,`cognome`) USING BTREE,
  ADD KEY `doc_k` (`nome`,`cognome`),
  ADD KEY `corso_k` (`corso`);

--
-- Indices de la tabla `RIPETIZIONE`
--
ALTER TABLE `RIPETIZIONE`
  ADD PRIMARY KEY (`giorno`,`inic`,`final`,`nome`,`cognome`,`account`) USING BTREE,
  ADD KEY `acc_k` (`account`);

--
-- Indices de la tabla `UTENTE`
--
ALTER TABLE `UTENTE`
  ADD PRIMARY KEY (`account`);

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `ORARIO`
--
ALTER TABLE `ORARIO`
  ADD CONSTRAINT `corso_k` FOREIGN KEY (`corso`) REFERENCES `CORSO` (`titulo`),
  ADD CONSTRAINT `doc_k` FOREIGN KEY (`nome`,`cognome`) REFERENCES `DOCENTE` (`nome`, `cognome`);

--
-- Filtros para la tabla `RIPETIZIONE`
--
ALTER TABLE `RIPETIZIONE`
  ADD CONSTRAINT `acc_k` FOREIGN KEY (`account`) REFERENCES `UTENTE` (`account`),
  ADD CONSTRAINT `ora_k` FOREIGN KEY (`giorno`,`inic`,`final`,`nome`,`cognome`) REFERENCES `ORARIO` (`giorno`, `inic`, `final`, `nome`, `cognome`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
