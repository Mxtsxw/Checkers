package agents;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import cherckers.Board;

public class Node {
    private Board state;
    private Node parent;
    private List<Node> children;
    private int visits;
    private double value;
    private List<Board> untriedActions;
    private Board action;

    public Node(Board state) {
        this(state, null, null);
    }

    public Node(Board state, Node parent, Board action) {
        this.state = state;
        this.parent = parent;
        this.children = new ArrayList<>();
        this.visits = 0;
        this.value = 0.0;
        this.untriedActions = new ArrayList<>(state.getLegalActions()); // Assume getLegalActions() returns List<Board>
        this.action = action;
    }

    public boolean isFullyExpanded() {
        return untriedActions.isEmpty();
    }

    public boolean isTerminalNode() {
        return state.isTerminal(); // Assume Board has isTerminal() method
    }

    public Node bestChild(double cParam) {
        double[] weights = new double[children.size()];
        for (int i = 0; i < children.size(); i++) {
            Node child = children.get(i);
            weights[i] = (child.value / child.visits) + cParam * Math.sqrt((2 * Math.log(this.visits) / child.visits));
        }

        int bestIndex = 0;
        double maxWeight = weights[0];
        for (int i = 1; i < weights.length; i++) {
            if (weights[i] > maxWeight) {
                bestIndex = i;
                maxWeight = weights[i];
            }
        }

        return children.get(bestIndex);
    }

    public Node expand() {
        int actionIndex = ThreadLocalRandom.current().nextInt(untriedActions.size());
        Board action = untriedActions.remove(actionIndex);
        Node childNode = new Node(action, this, action);
        children.add(childNode);
        return childNode;
    }

    public void update(double reward) {
        visits++;
        value += reward;
    }

    public Board getState() {
        return state;
    }

    public Node getParent() {
        return parent;
    }

    public List<Node> getChildren() {
        return children;
    }

    public int getVisits() {
        return visits;
    }

    public double getValue() {
        return value;
    }

    public List<Board> getUntriedActions() {
        return untriedActions;
    }

    public Board getAction() {
        return action;
    }
}
