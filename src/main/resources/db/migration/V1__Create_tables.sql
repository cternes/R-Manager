create table player (
    id varchar(255) not null PRIMARY KEY,
    name varchar(255) not null
);

create table game_match (
    id varchar(255) not null PRIMARY KEY,
    created_date datetime not null,
	status int not null,
	player1 varchar(255) not null ,
	player2 varchar(255) null,
	FOREIGN KEY(player1) REFERENCES player(id),
	FOREIGN KEY(player2) REFERENCES player(id)
);