/**
 * Abstract class for graphs used by different scheduling algorithms
 *
 * Graphs are weighted by difference of skill between players, or 0
 * if matching is done by random instead of skill.
 *
 * @since 2022-03-21
 */

package com.zoomers.GameSetMatch.scheduler.graphs;

import com.zoomers.GameSetMatch.scheduler.domain.Match;
import com.zoomers.GameSetMatch.scheduler.domain.Registrant;
import com.zoomers.GameSetMatch.scheduler.domain.Timeslot;

import java.util.Set;

public abstract class MatchGraph {

    protected final Set<Registrant> registrants;
    protected final Set<Timeslot> timeslots;
    protected final Set<Match> matches;

    public MatchGraph(Set<Registrant> registrants, Set<Timeslot> timeslots, Set<Match> matches) {

        this.registrants = registrants;
        this.timeslots = timeslots;
        this.matches = matches;
    }

    public void removeMatch(Match m) {

        matches.remove(m);
    }

    public void addMatch(Match m) {
        matches.add(m);
    }

    public void removeAll(Set<Match> matchesToRemove) {
        matches.removeAll(matchesToRemove);
    }

    public Set<Match> getMatches() {
        return this.matches;
    }

    public Set<Timeslot> getTimeslots() {
        return timeslots;
    }

    public Set<Registrant> getRegistrants() { return registrants; }

    public abstract void decrementDegree(Match m);

    public abstract void setMatchDegrees();
}
