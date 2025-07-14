USE `projeto`;

SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM `limpeza`;
DELETE FROM `pedido_has_produto`;
DELETE FROM `reserva`;
DELETE FROM `pedido`;
DELETE FROM `produto`;
DELETE FROM `quarto`;
DELETE FROM `equipe_limpeza_has_funcionario`;
DELETE FROM `equipe_limpeza`;
DELETE FROM `papel`;
DELETE FROM `funcionario`;
DELETE FROM `hospede`;
DELETE FROM `user`;

ALTER TABLE `pedido` AUTO_INCREMENT = 1;
ALTER TABLE `produto` AUTO_INCREMENT = 1;
ALTER TABLE `reserva` AUTO_INCREMENT = 1;
ALTER TABLE `equipe_limpeza` AUTO_INCREMENT = 1;
ALTER TABLE `limpeza` AUTO_INCREMENT = 1;
ALTER TABLE `user` AUTO_INCREMENT = 1;

INSERT INTO `quarto` (`numero`, `nao_perturbe`, `ocupado`, `marcado_para_limpeza`, `tipo`) VALUES
(101, 0, 1, 0, 'SIMPLES'),
(102, 0, 0, 1, 'SIMPLES'),
(103, 1, 1, 0, 'SIMPLES'),
(104, 0, 0, 0, 'SIMPLES'),
(105, 0, 1, 0, 'SIMPLES'),
(106, 0, 0, 1, 'SIMPLES'),
(201, 0, 1, 0, 'SUITE'),
(202, 1, 0, 0, 'SUITE'),
(203, 0, 1, 0, 'SUITE'),
(204, 0, 0, 1, 'SUITE'),
(205, 0, 1, 0, 'SUITE'),
(206, 0, 0, 0, 'SUITE'),
(301, 0, 1, 0, 'COBERTURA'),
(302, 1, 1, 0, 'COBERTURA'),
(303, 0, 0, 1, 'COBERTURA'),
(304, 0, 1, 0, 'COBERTURA'),
(305, 0, 0, 0, 'COBERTURA'),
(306, 0, 1, 0, 'COBERTURA');

INSERT INTO `hospede` (`cpf`, `data_nasc`, `nome_sobrenome`, `desativado`) VALUES
('123.456.789-01', '1985-03-15', 'João Silva Santos', DEFAULT),
('234.567.890-12', '1990-07-22', 'Maria Oliveira Costa', DEFAULT),
('345.678.901-23', '1978-11-08', 'Carlos Pereira Lima', DEFAULT),
('456.789.012-34', '1992-05-30', 'Ana Souza Ferreira', DEFAULT),
('567.890.123-45', '1988-09-12', 'Pedro Rodrigues Alves', DEFAULT),
('678.901.234-56', '1995-01-18', 'Lucia Fernandes Gomes', DEFAULT),
('789.012.345-67', '1983-12-05', 'Roberto Martins Rocha', DEFAULT),
('890.123.456-78', '1991-04-27', 'Fernanda Santos Moreira', DEFAULT),
('901.234.567-89', '1987-08-14', 'Marcelo Costa Barbosa', DEFAULT),
('012.345.678-90', '1993-10-03', 'Juliana Lima Cardoso', DEFAULT),
('112.345.678-91', '1980-02-20', 'Ricardo Alves Nascimento', DEFAULT),
('213.456.789-02', '1989-06-16', 'Camila Gomes Ribeiro', DEFAULT),
('314.567.890-13', '1975-12-25', 'Eduardo Moreira Campos', 1),
('415.678.901-24', '1996-03-08', 'Patrícia Rocha Dias', DEFAULT),
('516.789.012-35', '1984-07-11', 'Gustavo Barbosa Teixeira', DEFAULT);

INSERT INTO `funcionario` (`cpf`, `num_funcionario`, `administrador`, `data_nasc`, `nome_sobrenome`) VALUES
('111.222.333-44', 1001, 1, '1975-05-10', 'José Carlos Manager'),
('222.333.444-55', 1002, 1, '1980-08-20', 'Sandra Admin Silva'),
('333.444.555-66', 1003, 0, '1990-03-15', 'Paulo Recepção Santos'),
('444.555.666-77', 1004, 0, '1985-11-25', 'Carla Recep Oliveira'),
('555.666.777-88', 1005, 0, '1992-07-08', 'Maria Limpeza Costa'),
('666.777.888-99', 1006, 0, '1988-12-30', 'João Limpeza Pereira'),
('777.888.999-00', 1007, 0, '1986-09-18', 'Ana Limpeza Ferreira'),
('888.999.000-11', 1008, 0, '1991-02-14', 'Pedro Almox Rodrigues'),
('999.000.111-22', 1009, 0, '1987-06-05', 'Lucia Almox Fernandes'),
('000.111.222-33', 1010, 0, '1989-04-12', 'Roberto Limpeza Martins');

INSERT INTO `papel` (`cpf`, `papel`) VALUES
('111.222.333-44', 'GERENTE'),
('222.333.444-55', 'ADMINISTRADOR'),
('333.444.555-66', 'RECEPCIONISTA'),
('444.555.666-77', 'RECEPCIONISTA'),
('555.666.777-88', 'LIMPEZA'),
('666.777.888-99', 'LIMPEZA'),
('777.888.999-00', 'LIMPEZA'),
('888.999.000-11', 'ALMOXARIFADO'),
('999.000.111-22', 'ALMOXARIFADO'),
('000.111.222-33', 'LIMPEZA');

INSERT INTO `equipe_limpeza` (`id_equipe`) VALUES
(1),
(2),
(3);

INSERT INTO `equipe_limpeza_has_funcionario` (`id_equipe`, `cpf`) VALUES
(1, '555.666.777-88'),
(1, '666.777.888-99'),
(2, '777.888.999-00'),
(2, '000.111.222-33'),
(3, '555.666.777-88'),
(3, '777.888.999-00');

INSERT INTO `produto` (`nome`, `preco_atual`, `quantidade`) VALUES
('Água Mineral 500ml', 3.50, 100),
('Refrigerante Coca-Cola', 5.00, 50),
('Suco de Laranja', 4.50, 30),
('Sanduíche Natural', 8.00, 20),
('Biscoito Chocolate', 2.50, 80),
('Café Espresso', 3.00, 40),
('Cerveja Heineken', 8.50, 25),
('Vinho Tinto', 45.00, 15),
('Amendoim Salgado', 4.00, 60),
('Chocolate Trufado', 6.50, 35),
('Água com Gás', 4.00, 45),
('Energético Red Bull', 7.50, 30),
('Pizza Margherita', 25.00, 10),
('Salada Caesar', 15.00, 12),
('Torrada Integral', 5.50, 25);


INSERT INTO `reserva` (`cpf`, `data_inicio`, `data_fim`, `data_entrada`, `data_saida`, `numero`) VALUES
('123.456.789-01', '2024-01-15', '2024-01-20', '2024-01-15', '2024-01-20', 101),
('234.567.890-12', '2024-02-10', '2024-02-15', '2024-02-10', NULL, 201),
('345.678.901-23', '2024-03-05', '2024-03-12', '2024-03-05', '2024-03-12', 301),
('456.789.012-34', '2024-04-20', '2024-04-25', '2024-04-20', NULL, 103),
('567.890.123-45', '2024-05-08', '2024-05-15', '2024-05-08', '2024-05-15', 203),
('678.901.234-56', '2024-06-12', '2024-06-18', '2024-06-12', NULL, 302),
('789.012.345-67', '2024-07-01', '2024-07-07', '2024-07-01', '2024-07-07', 105),
('890.123.456-78', '2024-08-15', '2024-08-22', '2024-08-15', NULL, 205),
('901.234.567-89', '2024-09-10', '2024-09-17', '2024-09-10', '2024-09-17', 304),
('012.345.678-90', '2024-10-05', '2024-10-12', '2024-10-05', NULL, 306),
('112.345.678-91', '2024-11-20', '2024-11-25', NULL, NULL, 102),
('213.456.789-02', '2024-12-15', '2024-12-22', NULL, NULL, 204);


INSERT INTO `pedido` (`data_pedido`, `data_entrega`, `numero_quarto`) VALUES
('2025-07-10 09:15:00', '2025-07-14 18:30:00', 101),
('2025-07-11 14:20:00', '2025-07-15 15:05:00', 201),
('2025-07-12 08:45:00', '2025-07-16 09:30:00', 301),
('2025-07-13 12:00:00', NULL, 103),
('2025-07-14 16:10:00', '2025-07-18 17:00:00', 203),
('2025-07-15 11:25:00', '2025-07-19 12:15:00', 302),
('2025-07-16 07:50:00', '2025-07-20 08:45:00', 105),
('2025-07-17 13:35:00', NULL, 205),
('2025-07-18 17:00:00', '2025-07-21 17:55:00', 304),
('2025-07-19 10:10:00', '2025-07-22 11:00:00', 306),
('2025-07-20 15:30:00', '2025-07-23 16:20:00', 102),
('2025-07-21 18:40:00', NULL, 204),
('2025-07-22 09:10:00', '2025-07-24 10:00:00', 106),
('2025-07-23 14:55:00', NULL, 202);



INSERT INTO `pedido_has_produto` (`id_pedido`, `id_produto`, `preco_item`, `quantidade_item`) VALUES
(1, 1, 3.50, 2),
(1, 5, 2.50, 1),
(1, 6, 3.00, 2),
(2, 2, 5.00, 1),
(2, 4, 8.00, 1),
(2, 10, 6.50, 1),
(3, 7, 8.50, 2),
(3, 9, 4.00, 1),
(3, 8, 45.00, 1),
(4, 3, 4.50, 1),
(4, 12, 7.50, 1),
(5, 13, 25.00, 1),
(5, 14, 15.00, 1),
(5, 1, 3.50, 3),
(6, 11, 4.00, 2),
(6, 15, 5.50, 1),
(7, 6, 3.00, 3),
(7, 5, 2.50, 2),
(8, 2, 5.00, 2),
(8, 7, 8.50, 1),
(9, 8, 45.00, 1),
(9, 10, 6.50, 2),
(10, 13, 25.00, 1),
(10, 1, 3.50, 4),
(11, 4, 8.00, 2),
(11, 6, 3.00, 1),
(12, 12, 7.50, 2),
(12, 3, 4.50, 3),
(13, 14, 15.00, 1),
(13, 2, 5.00, 1),
(14, 8, 45.00, 1),
(14, 11, 4.00, 1);

INSERT INTO `limpeza` (`id_equipe`, `numero`, `data_limpeza`) VALUES
(1, 102, '2024-01-10'),
(1, 104, '2024-01-11'),
(1, 106, '2024-01-12'),
(2, 202, '2024-01-13'),
(2, 204, '2024-01-14'),
(2, 212, '2024-01-15'),
(3, 303, '2024-01-16'),
(3, 305, '2024-01-17'),
(1, 101, '2024-01-21'),
(1, 103, '2024-01-22'),
(2, 201, '2024-01-23'),
(2, 203, '2024-01-24'),
(3, 301, '2024-01-25'),
(3, 302, '2024-01-26'),
(1, 105, '2024-01-27'),
(2, 205, '2024-01-28'),
(3, 304, '2024-01-29'),
(3, 306, '2024-01-30');

INSERT INTO `user` (`nome`, `email`, `senha`) VALUES
('Admin Sistema', 'admin@hotel.com', 'admin123'),
('José Manager', 'jose.manager@hotel.com', 'manager123'),
('Sandra Admin', 'sandra.admin@hotel.com', 'admin456'),
('Paulo Recepção', 'paulo.recepcao@hotel.com', 'recepcao123'),
('Carla Recepção', 'carla.recepcao@hotel.com', 'recepcao456'),
('Sistema Backup', 'backup@hotel.com', 'backup789');

SET FOREIGN_KEY_CHECKS = 1;
