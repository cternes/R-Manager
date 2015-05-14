ALTER TABLE game_match ADD COLUMN current_player BIGINT NULL;
ALTER TABLE game_match ADD FOREIGN KEY(current_player) REFERENCES player(id);