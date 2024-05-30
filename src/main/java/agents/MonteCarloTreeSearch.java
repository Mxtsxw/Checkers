package agents;

import cherckers.Board;
import cherckers.Game;
import vizualiser.TreeViz;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class MonteCarloTreeSearch implements AI {
    private final String color;
    private final int computationalBudget;
    private double explorationConstant;
    public static boolean GENERATE_TREE_RENDER = false;

    public MonteCarloTreeSearch(String color, int computationalBudget, double explorationConstant) {
        this.color = color;
        this.computationalBudget = computationalBudget;
        this.explorationConstant = explorationConstant;
    }

    public MonteCarloTreeSearch(String color, int computationalBudget) {
        this.color = color;
        this.computationalBudget = computationalBudget;
        this.explorationConstant = Math.sqrt(2.0); // Use sqrt(2) as the exploration constant
    }

    public int evaluate(Board state) {
        String winner = state.getWinner();
        if (winner == null) {
            return 0;
        }
        if (winner.equals(this.color)) {
            return 1;
        }
        return -1;
    }

    public Board uctSearch(Board rootState) {
        Node rootNode = new Node(rootState);

        for (int i = 0; i < this.computationalBudget; i++) {
            Node node = treePolicy(rootNode);
            int reward = defaultPolicy(node.getState());
            backup(node, reward);
        }

        if (MonteCarloTreeSearch.GENERATE_TREE_RENDER) {
            try {
                TreeViz treeViz = new TreeViz(rootNode);
                treeViz.renderGraph("tree_" + color + "_" + System.currentTimeMillis() + ".png");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return rootNode.bestChild(0).getState();
    }

    public Node treePolicy(Node node) {
        while (!node.isTerminalNode()) {
            if (!node.isFullyExpanded()) {
                return node.expand();
            } else {
                node = node.bestChild(explorationConstant);
            }
        }
        return node;
    }

    public int defaultPolicy(Board state) {
        while (!state.isTerminal()) {
            List<Board> legalActions = state.getLegalActions();
            Board action = legalActions.get(ThreadLocalRandom.current().nextInt(legalActions.size()));
            state = action; // Current state is updated to the next state
        }
//        System.out.println("Terminal Node hit");
        return evaluate(state); // Return the reward
    }

    public void backup(Node node, int reward) {
        while (node != null) {
            node.update(reward);
            node = node.getParent();
        }
    }

    public Board run(Game game) {
        System.out.println("Running MCTS (" + computationalBudget + ")" + "(c=" + explorationConstant + ")" + " for " + color + " player");
        return uctSearch(game.getBoard());
    }

    @Override
    public void update() {
        // Do nothing
    }

    @Override
    public void setCriterias (Map<String, Integer> criterias) {
        // Do nothing
    }

    @Override
    public Map<String, Integer> getCriterias() {
        return null;
    }

    @Override
    public String toString() {
        return "MCTS (" + computationalBudget + ")" + " (c=" + String.format("%.2f", explorationConstant) + ")";
    }
}

