-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 05, 2025 at 11:28 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `e_perpus`
--

-- --------------------------------------------------------

--
-- Table structure for table `agama`
--
-- Error reading structure for table e_perpus.agama: #1932 - Table &#039;e_perpus.agama&#039; doesn&#039;t exist in engine
-- Error reading data for table e_perpus.agama: #1064 - You have an error in your SQL syntax; check the manual that corresponds to your MariaDB server version for the right syntax to use near &#039;FROM `e_perpus`.`agama`&#039; at line 1

-- --------------------------------------------------------

--
-- Table structure for table `anggota`
--

CREATE TABLE `anggota` (
  `tanggal_masuk` date NOT NULL DEFAULT current_timestamp(),
  `id_anggota` varchar(11) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `no_induk` int(11) NOT NULL,
  `tempat_lahir` varchar(100) NOT NULL,
  `tangal_lahir` date NOT NULL,
  `alamat` varchar(100) NOT NULL,
  `no_telp` int(11) NOT NULL,
  `email` varchar(100) NOT NULL,
  `pendidikan` varchar(50) NOT NULL,
  `status` varchar(50) NOT NULL,
  `nama_ibu` varchar(30) NOT NULL,
  `no_telp_darurat` int(11) NOT NULL,
  `status_anggota` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Triggers `anggota`
--
DELIMITER $$
CREATE TRIGGER `before_insert_anggota` BEFORE INSERT ON `anggota` FOR EACH ROW BEGIN
    DECLARE year_part CHAR(2);
    DECLARE month_part CHAR(2);
    DECLARE day_part CHAR(2);
    DECLARE sequence_part CHAR(5);

    -- Mengambil dua digit terakhir dari tahun
    SET year_part = DATE_FORMAT(NEW.tanggal_masuk, '%y');
    -- Mengambil dua digit bulan
    SET month_part = DATE_FORMAT(NEW.tanggal_masuk, '%m');
    -- Mengambil dua digit tanggal
    SET day_part = DATE_FORMAT(NEW.tanggal_masuk, '%d');

    -- Mendapatkan urutan nomor berdasarkan data yang sudah ada
    SET sequence_part = LPAD(
        IFNULL(
            (SELECT COUNT(*) + 1 
             FROM anggota 
             WHERE DATE(tanggal_masuk) = DATE(NEW.tanggal_masuk)), 
            1), 
        5, '0'
    );

    -- Membentuk id_anggota
    SET NEW.id_anggota = CONCAT(year_part, month_part, day_part, sequence_part);
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `buku`
--

CREATE TABLE `buku` (
  `id_buku` int(11) NOT NULL,
  `judul` varchar(100) NOT NULL,
  `pengarang` varchar(100) NOT NULL,
  `penerbit` varchar(100) NOT NULL,
  `tahun` varchar(10) NOT NULL,
  `sinopsis` text NOT NULL,
  `gambar` varchar(300) NOT NULL,
  `status_peminjaman` varchar(10) NOT NULL DEFAULT '''Tersedia'''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `buku`
--

INSERT INTO `buku` (`id_buku`, `judul`, `pengarang`, `penerbit`, `tahun`, `sinopsis`, `gambar`, `status_peminjaman`) VALUES
(1, 'testing', 'Andrea Hirata', 'Penerbit Bentang', '2006', 'Buku Kedua Andrea Hirata ini bercerita tentang masa SMA tiga orang pemuda, yaitu Ikal, Arai dan Jimbron. Mereka bertiga adalah remaja yang berasal dari Belitong dan melanjutkan sekolah di Manggar, SMA Negeri pertama di Manggar. Untuk mencukupi kebutuhan sekolahnya Arai, Ikal dan Jimbron bekerja paruh waktu sebagai kuli di pasar ikan.\r\n\r\n\r\nArai adalah yang paling cerdas diantara mereka bertiga, selalu mengutip kata-kata inspiratif dari berbagai sumber “tak semua yang dihitung bisa diperhitungkan dan tak semua yang diperhitungkan bisa dihitung”, sedangkan Ikal yang sangat mengidolakan H. Roma Irama akan mengutip kalimat dari lirik lagu raja dangdut tersebut “Darah muda adalah darahnya para remaja” sedangkan Jibron yang sangat menyukai kuda akan mengeluarkan kalimat yang tidak jauh-jauh dari bahasan tentang kuda.\r\n\r\n\r\nKehidupan SMA adalah perjalanan mencari jati diri. Arai, saat itu jatuh cinta pada teman sekelasnya, Zakia Nurmala, sedangkan Ikal jatuh cinta pada putri seorang cina, A Ling, dan Jimbron jatuh cinta padaku.', 'http://192.168.1.9/mYAPP_API/img/Sang_Pemimpi_sampul.jpg', 'Terpinjam'),
(2, 'The Pioneer', 'James Fenimore Cooper', 'Charles Wiley', '1823', 'Kisah ini terjadi di perbatasan Negara Bagian New York yang berkembang pesat dan menampilkan seorang Leatherstocking tua ( Natty Bumppo ), Hakim Marmaduke Temple of Templeton (yang hidupnya sejajar dengan ayah penulis, Hakim William Cooper ), dan Elizabeth Temple (berdasarkan cerita penulis). saudara perempuan, Hannah Cooper), putri dari Templeton fiksi. Cerita dimulai dengan perdebatan antara hakim dan Leatherstocking mengenai siapa yang membunuh uang.\r\n\r\nMelalui diskusi mereka, Cooper meninjau banyak perubahan pada Danau Otsego di New York dan wilayahnya: pertanyaan mengenai pengelolaan lingkungan, konservasi, dan pemanfaatan masih menjadi perhatian utama. Leatherstocking dan teman terdekatnya, Chingachgook Indian Mohican , mulai bersaing dengan Kuil untuk mendapatkan kesetiaan pengunjung muda misterius, \"pemburu muda\" yang dikenal sebagai Oliver Edwards. Yang terakhir akhirnya menikahi Elizabeth Temple. Chingachgook meninggal, mewakili ketakutan Eropa-Amerika terhadap ras \"orang Indian yang sekarat\", yang tampaknya digantikan oleh para pemukim. Natty menghilang saat matahari terbenam.', 'http://192.168.1.5/myapp_api/img/the_pioneers.jpg', 'Tersedia');

-- --------------------------------------------------------

--
-- Table structure for table `jenis_anggota`
--
-- Error reading structure for table e_perpus.jenis_anggota: #1932 - Table &#039;e_perpus.jenis_anggota&#039; doesn&#039;t exist in engine
-- Error reading data for table e_perpus.jenis_anggota: #1064 - You have an error in your SQL syntax; check the manual that corresponds to your MariaDB server version for the right syntax to use near &#039;FROM `e_perpus`.`jenis_anggota`&#039; at line 1

-- --------------------------------------------------------

--
-- Table structure for table `jenis_kelamin`
--
-- Error reading structure for table e_perpus.jenis_kelamin: #1932 - Table &#039;e_perpus.jenis_kelamin&#039; doesn&#039;t exist in engine
-- Error reading data for table e_perpus.jenis_kelamin: #1064 - You have an error in your SQL syntax; check the manual that corresponds to your MariaDB server version for the right syntax to use near &#039;FROM `e_perpus`.`jenis_kelamin`&#039; at line 1

-- --------------------------------------------------------

--
-- Table structure for table `pdf_files`
--

CREATE TABLE `pdf_files` (
  `id` int(11) NOT NULL,
  `id_buku` int(11) NOT NULL,
  `nama_file` varchar(255) DEFAULT NULL,
  `lokasi_file` varchar(255) DEFAULT NULL,
  `tanggal_upload` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pdf_files`
--

INSERT INTO `pdf_files` (`id`, `id_buku`, `nama_file`, `lokasi_file`, `tanggal_upload`) VALUES
(1, 1, 'sangpemimpi', 'http://192.168.1.7/mYAPP_API/buku/Sang%20Pemimpi.pdf', '2024-02-29 10:19:27'),
(2, 2, 'thepioneer', 'http://192.168.1.2/myapp_api/buku/The%20Pioneers%20by%20James%20Cooper.pdf', '2024-06-14 09:10:09');

-- --------------------------------------------------------

--
-- Table structure for table `peminjaman`
--

CREATE TABLE `peminjaman` (
  `id_peminjaman` int(11) NOT NULL,
  `id_user` int(11) DEFAULT NULL,
  `id_buku` int(11) DEFAULT NULL,
  `tanggal_peminjaman` date DEFAULT current_timestamp(),
  `tanggal_pengembalian` date DEFAULT NULL,
  `status` enum('Pinjam','Kembali') DEFAULT 'Pinjam'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `peminjaman`
--

INSERT INTO `peminjaman` (`id_peminjaman`, `id_user`, `id_buku`, `tanggal_peminjaman`, `tanggal_pengembalian`, `status`) VALUES
(134, 5, 1, '2024-06-24', '2024-06-24', 'Pinjam');

--
-- Triggers `peminjaman`
--
DELIMITER $$
CREATE TRIGGER `set_tanggal_pengembalian` BEFORE INSERT ON `peminjaman` FOR EACH ROW BEGIN
    SET NEW.tanggal_pengembalian = DATE_ADD(NEW.tanggal_peminjaman, INTERVAL 0 DAY);
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `status_anggota`
--
-- Error reading structure for table e_perpus.status_anggota: #1932 - Table &#039;e_perpus.status_anggota&#039; doesn&#039;t exist in engine
-- Error reading data for table e_perpus.status_anggota: #1064 - You have an error in your SQL syntax; check the manual that corresponds to your MariaDB server version for the right syntax to use near &#039;FROM `e_perpus`.`status_anggota`&#039; at line 1

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id_user` int(11) NOT NULL,
  `no_anggota` varchar(100) NOT NULL,
  `nama` varchar(32) NOT NULL,
  `password` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id_user`, `no_anggota`, `nama`, `password`) VALUES
(5, '23083000081', 'adam', '02ba762dad6e9e0b8c8cdb5181fbffef'),
(6, '23083000082', 'yuzan', '02ba762dad6e9e0b8c8cdb5181fbffef');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `anggota`
--
ALTER TABLE `anggota`
  ADD PRIMARY KEY (`id_anggota`);

--
-- Indexes for table `buku`
--
ALTER TABLE `buku`
  ADD PRIMARY KEY (`id_buku`);

--
-- Indexes for table `pdf_files`
--
ALTER TABLE `pdf_files`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id_buku` (`id_buku`);

--
-- Indexes for table `peminjaman`
--
ALTER TABLE `peminjaman`
  ADD PRIMARY KEY (`id_peminjaman`),
  ADD KEY `iduser` (`id_user`),
  ADD KEY `idbuku` (`id_buku`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id_user`),
  ADD UNIQUE KEY `no_anggota` (`no_anggota`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `pdf_files`
--
ALTER TABLE `pdf_files`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `peminjaman`
--
ALTER TABLE `peminjaman`
  MODIFY `id_peminjaman` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=135;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id_user` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `pdf_files`
--
ALTER TABLE `pdf_files`
  ADD CONSTRAINT `pdf_files_ibfk_1` FOREIGN KEY (`id_buku`) REFERENCES `buku` (`id_buku`);

--
-- Constraints for table `peminjaman`
--
ALTER TABLE `peminjaman`
  ADD CONSTRAINT `idbuku` FOREIGN KEY (`id_buku`) REFERENCES `buku` (`id_buku`),
  ADD CONSTRAINT `iduser` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
