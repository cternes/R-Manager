package de.slackspace.rmanager.database;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import de.slackspace.rmanager.domain.Game;

@Transactional
public interface GameRepository extends CrudRepository<Game, Long> {

	Page<Game> findAll(Pageable pageable);
}
