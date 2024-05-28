-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 47.236.10.165:32307:3306
-- Generation Time: May 21, 2024 at 12:05 PM
-- Server version: 8.0.30
-- PHP Version: 8.2.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `wms`
--

-- --------------------------------------------------------

--
-- Table structure for table `supos_user`
--

CREATE TABLE `supos_user` (
  `id` bigint UNSIGNED NOT NULL,
  `username` varchar(100) NOT NULL,
  `accountType` int DEFAULT NULL,
  `lockStatus` int DEFAULT NULL,
  `valid` tinyint DEFAULT NULL,
  `personCode` varchar(100) NOT NULL,
  `personName` varchar(100) NOT NULL,
  `delFlag` tinyint DEFAULT NULL,
  `createTime` timestamp NOT NULL,
  `modifyTime` timestamp NOT NULL,
  `syncTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `supos_user`
--

INSERT INTO `supos_user` (`id`, `username`, `accountType`, `lockStatus`, `valid`, `personCode`, `personName`, `delFlag`, `createTime`, `modifyTime`, `syncTime`) VALUES
(1, 'Rajasekaran.R', 0, 0, 1, 'IMI004', 'Rajasekaran.R', NULL, '2023-12-11 03:38:53', '2024-05-06 08:04:12', '2024-05-15 07:43:13'),
(2, 'Jana.Shivarama.Krishna', 0, 0, 1, 'IMI015', 'Jana.Shivarama.Krishna', NULL, '2023-12-11 03:38:53', '2024-02-26 06:37:53', '2024-05-15 07:43:13'),
(3, 'Ravi.Kumar.Pasupuleti_1', 0, 0, 1, 'IMI014', 'Ravi.Kumar.Pasupuleti', NULL, '2023-12-11 03:38:53', '2024-02-26 06:37:46', '2024-05-15 07:43:13'),
(4, 'Umesh.Hegde', 0, 0, 1, 'IMI013', 'Umesh.Hegde', NULL, '2023-12-11 03:38:53', '2024-02-26 06:37:42', '2024-05-15 07:43:13'),
(5, 'Bhaskara.Rao.Ravva', 0, 0, 1, 'IMI012', 'Bhaskara.Rao.Ravva', NULL, '2023-12-11 03:38:53', '2024-02-26 06:37:36', '2024-05-15 07:43:13'),
(6, 'Mohammed.Noorulla.Sheriff', 0, 0, 1, 'IMI011', 'Mohammed.Noorulla.Sheriff', NULL, '2023-12-11 03:38:53', '2024-02-26 06:37:30', '2024-05-15 07:43:13'),
(7, 'Ravi.Kumar.Pasupuleti', 0, 0, 1, 'IMI010', 'Ravi.Kumar.Pasupuleti', NULL, '2023-12-11 03:38:53', '2024-02-26 06:37:25', '2024-05-15 07:43:13'),
(8, 'Majid.Husain.Choudhary', 0, 0, 1, 'IMI009', 'Majid.Husain.Choudhary', NULL, '2023-12-11 03:38:53', '2024-02-26 06:37:20', '2024-05-15 07:43:13'),
(9, 'Ravi.Soni', 0, 0, 1, 'IMI008', 'Ravi.Soni', NULL, '2023-12-11 03:38:53', '2024-02-26 06:37:15', '2024-05-15 07:43:13'),
(10, 'Sravan.Murarishetti', 0, 0, 1, 'IMI007', 'Sravan.Murarishetti', NULL, '2023-12-11 03:38:53', '2024-02-26 06:37:09', '2024-05-15 07:43:13'),
(11, 'Nitin.Bali', 0, 0, 1, 'IMI006', 'Nitin.Bali', NULL, '2023-12-11 03:38:53', '2024-02-26 06:37:03', '2024-05-15 07:43:13'),
(12, 'Prakash.M', 0, 0, 1, 'IMI005', 'Prakash.M', NULL, '2023-12-11 03:38:53', '2024-02-26 06:36:59', '2024-05-15 07:43:13'),
(13, 'Arjun.Baidya.Ray', 0, 0, 1, 'IMI019', 'Arjun.Baidya.Ray', NULL, '2023-12-11 03:38:53', '2024-02-26 06:36:53', '2024-05-15 07:43:13'),
(14, 'Agatha.Nair', 0, 0, 1, 'IMI018', 'Agatha.Nair', NULL, '2023-12-11 03:38:53', '2024-02-26 06:36:42', '2024-05-15 07:43:13'),
(15, 'Arif.Hussain', 0, 0, 1, 'IMI003', 'Arif.Hussain', NULL, '2023-12-11 03:38:53', '2024-02-26 06:36:36', '2024-05-15 07:43:13'),
(16, 'Vijaya.Kumari.Miryala', 0, 0, 1, 'IMI017', 'Vijaya.Kumari.Miryala', NULL, '2023-12-11 03:38:53', '2024-02-26 06:36:32', '2024-05-15 07:43:13'),
(17, 'Pradipta.Mukherjee', 0, 0, 1, 'IMI002', 'Pradipta.Mukherjee', NULL, '2023-12-11 03:38:53', '2024-02-26 06:36:25', '2024-05-15 07:43:13'),
(18, 'Saubhagyalaxmi.Samantaray', 0, 0, 1, 'IMI016', 'Saubhagyalaxmi.Samantaray', NULL, '2023-12-11 03:38:53', '2024-05-15 07:44:24', '2024-05-15 07:48:03'),
(19, 'Venu.Gopal.Valiveti_1', 0, 0, 1, 'IMI001', 'Venu.Gopal.Valiveti', NULL, '2023-12-15 08:56:10', '2024-02-26 06:36:10', '2024-05-15 07:43:13'),
(20, 'ShiMu', 1, 0, 1, 'ShiMu', 'ShiMu', NULL, '2023-12-11 01:38:21', '2024-01-10 03:46:23', '2024-05-15 07:43:13'),
(21, 'admin8', 0, 0, 1, 'admin8', 'admin8', NULL, '2023-12-15 06:49:59', '2023-12-15 06:49:59', '2024-05-15 07:43:13'),
(22, 'admin7', 0, 0, 1, 'IMI20', 'renzhanx', NULL, '2023-12-14 06:23:32', '2023-12-14 06:24:52', '2024-05-15 07:43:13'),
(23, 'admin5', 0, 0, 1, 'admin5', 'admin5', NULL, '2023-12-11 02:51:12', '2023-12-11 02:51:59', '2024-05-15 07:43:13'),
(24, 'IMI_Yara', 0, 0, 1, 'IMI_Yara', 'IMI_Yara', NULL, '2023-11-27 14:22:29', '2023-11-27 14:37:59', '2024-05-15 07:43:13'),
(25, 'IMI_Rayan', 0, 0, 1, 'IMI_Rayan', 'IMI_Rayan', NULL, '2023-11-27 14:22:57', '2023-11-27 14:26:50', '2024-05-15 07:43:13'),
(26, 'IMI_user', 0, 0, 1, 'IMI_user', 'IMI_user', NULL, '2023-11-27 11:13:02', '2023-11-27 12:42:30', '2024-05-15 07:43:13'),
(27, 'admin', 1, 0, 1, 'default_person', 'virtual_person', NULL, '2023-10-30 01:46:18', '2023-10-30 01:51:13', '2024-05-15 07:43:13');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `supos_user`
--
ALTER TABLE `supos_user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `personCode` (`personCode`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `supos_user`
--
ALTER TABLE `supos_user`
  MODIFY `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
