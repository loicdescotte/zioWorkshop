CREATE TABLE IF NOT EXISTS stock
(
    id INT NOT NULL AUTO_INCREMENT,
    value INT NOT NULL,
    PRIMARY KEY(id)
);

REPLACE INTO stock VALUES(1, 100);
REPLACE INTO stock VALUES(2, 50);
REPLACE INTO stock VALUES(3, 0);
