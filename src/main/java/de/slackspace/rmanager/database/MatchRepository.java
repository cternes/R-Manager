package de.slackspace.rmanager.database;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import de.slackspace.rmanager.domain.GameMatch;

public interface MatchRepository extends CrudRepository<GameMatch, String> {

	Page<GameMatch> findAll(Pageable pageable);
}
