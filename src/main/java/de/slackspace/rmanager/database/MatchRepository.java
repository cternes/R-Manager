package de.slackspace.rmanager.database;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import de.slackspace.rmanager.domain.GameMatch;

public interface MatchRepository extends CrudRepository<GameMatch, String> {

	Page<GameMatch> findAll(Pageable pageable);
	
	/**
	 * Find all active matches for the given player.
	 * 
	 * Active matches are those with MatchStatus WAITINGFORPLAYERS, TURNP1, TURNP2
	 * 
	 * @param playerId
	 * @return a list of active matches
	 */
	@Query("SELECT m FROM GameMatch m WHERE (m.player1.id = :playerId OR m.player2.id = :playerId) AND m.status IN (0,1,2)")
    public List<GameMatch> findActiveMatchesByPlayer(@Param("playerId") long playerId);

	GameMatch findTop1ByPlayer2IsNull();
	
	GameMatch findByToken(String token);
}
