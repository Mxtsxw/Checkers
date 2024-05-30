package agents;

import cherckers.Board;
import cherckers.Game;

import java.util.Map;

public interface AI {
    public Board run(Game game);

    public void update();

    public void setCriterias(Map<String, Integer> criterias);

    public Map<String, Integer> getCriterias();
}
