CREATE TABLE turn (
	id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	game_match BIGINT NOT NULL,
	player BIGINT NOT NULL,
	turn_data BLOB NULL,
	created_date DATETIME NOT NULL,
	FOREIGN KEY(player) REFERENCES player(id),
	FOREIGN KEY(game_match) REFERENCES game_match(id)
);