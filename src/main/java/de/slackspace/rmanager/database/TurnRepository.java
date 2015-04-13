package de.slackspace.rmanager.database;

import org.springframework.data.repository.CrudRepository;

import de.slackspace.rmanager.domain.Turn;

public interface TurnRepository extends CrudRepository<Turn, Long> {

}
