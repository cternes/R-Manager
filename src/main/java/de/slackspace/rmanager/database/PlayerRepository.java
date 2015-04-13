package de.slackspace.rmanager.database;

import org.springframework.data.repository.CrudRepository;

import de.slackspace.rmanager.domain.Player;

public interface PlayerRepository extends CrudRepository<Player, Long> {

	Player findByName(String name);
	Player findByToken(String token);
}
