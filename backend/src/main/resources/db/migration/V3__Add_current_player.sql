ALTER TABLE game_match ADD COLUMN current_player BIGINT NULL;
ALTER TABLE game_match ADD FOREIGN KEY(current_player) REFERENCES player(id);

ALTER TABLE game_match ADD COLUMN winner BIGINT NULL;
ALTER TABLE game_match ADD FOREIGN KEY(winner) REFERENCES player(id);