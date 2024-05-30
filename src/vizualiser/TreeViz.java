package vizualiser;

import agents.Node;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static guru.nidi.graphviz.model.Factory.mutGraph;
import static guru.nidi.graphviz.model.Factory.mutNode;

public class TreeViz {
    private Node root;

    public TreeViz(Node root) {
        this.root = root;
    }

    public void renderGraph(String filename) throws IOException {
        MutableGraph g = mutGraph("MCTS Tree").setDirected(true);
        addNodes(g, root, 0);
        Graphviz.fromGraph(g).width(1920).height(1080).render(Format.PNG).toFile(new File("./images/rendering/" + filename));
    }

    private void addNodesWithLabels(MutableGraph g, Node node, int index) {
        MutableNode currentNode = mutNode("node" + index).add(Label.of("Visits: " + node.getVisits() + "\nValue: " + node.getValue()));
        g.add(currentNode);
        List<Node> children = node.getChildren();
        for (int i = 0; i < children.size(); i++) {
            int childIndex = index * 10 + i + 1; // Ensure unique indexes
            MutableNode childNode = mutNode("node" + childIndex).add(Label.of("Visits: " + children.get(i).getVisits() + "\nValue: " + children.get(i).getValue()));
            currentNode.addLink(childNode);
            addNodes(g, children.get(i), childIndex);
        }
    }

    /**
     * addNodes without labels
     */
    private void addNodes(MutableGraph g, Node node, int index) {
        MutableNode currentNode = mutNode("node" + index);
        g.add(currentNode);
        List<Node> children = node.getChildren();
        for (int i = 0; i < children.size(); i++) {
            int childIndex = index * 10 + i + 1; // Ensure unique indexes
            MutableNode childNode = mutNode("node" + childIndex);
            currentNode.addLink(childNode);
            addNodes(g, children.get(i), childIndex);
        }
    }

}
