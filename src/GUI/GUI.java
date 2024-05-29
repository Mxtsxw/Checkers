package GUI;

import agents.AI;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GUI extends JPanel {

    // Elementary components
    public JTextField depthField;
    public JTextField iterationsField;
    public JComboBox<String> playerSelector;
    public JButton resetButton;
    public JButton validateButton;

    // Panels
    public JPanel selectionPanel;
    public JPanel criteriaPanel;
    public JPanel playerParameterPanel;

    public AI ai;

    Map<String, Integer> criterias;

    public GUI(AI ai) {
        this.ai = ai;
        this.iterationsField = new JTextField(); iterationsField.setPreferredSize(new Dimension(50, 20));
        this.depthField = new JTextField(); depthField.setPreferredSize(new Dimension(50, 20));
        this.playerSelector = new JComboBox<>(new String[]{"Human", "Minimax", "MinimaxAlphaBeta", "MCTS"});
        this.resetButton = new JButton("Reset");
        this.validateButton = new JButton("Validate");

        this.criterias = Map.of(
                "Material", 2,
                "King", 5,
                "Eatable", -2,
                "Movable", 1,
                "Win", 1000
        );

        init();
    }

    public void init() {
        this.setLayout(new BorderLayout());

        this.selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        this.criteriaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        this.criteriaPanel.setBorder(BorderFactory.createTitledBorder("Evaluation Criteria"));

        // -- Player selection panel

        this.playerParameterPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        updatePlayerParameterPanel("Human");

        this.selectionPanel.add(playerSelector);
        this.selectionPanel.add(playerParameterPanel);

        this.add(selectionPanel, BorderLayout.NORTH);



        // -- Criteria HashMap
        this.add(criteriaPanel, BorderLayout.CENTER);
        updateCriteriasPanel("Human");

        // -- Reset and Validate buttons
        JPanel validationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        validationPanel.add(resetButton);
        validationPanel.add(validateButton);
        this.add(validationPanel, BorderLayout.SOUTH);

    }

    public void updatePlayerParameterPanel(String playerType) {
        playerParameterPanel.removeAll();

        switch (playerType) {
            case "Human":
                break;
            case "Minimax", "MinimaxAlphaBeta":
                playerParameterPanel.setLayout(new FlowLayout());
                playerParameterPanel.add(new JLabel("Depth"));
                playerParameterPanel.add(depthField);
                break;
            case "MCTS":
                playerParameterPanel.setLayout(new FlowLayout());
                playerParameterPanel.add(new JLabel("Iterations"));
                playerParameterPanel.add(iterationsField);
                break;
        }
        playerParameterPanel.revalidate();
        playerParameterPanel.repaint();
    }

    public void updateCriteriasPanel(String playerType){
        criteriaPanel.removeAll();
        criteriaPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        for (String criteria : criterias.keySet()) {
            JPanel div = new JPanel(new GridLayout(1, 2, 10, 10));
            div.add(new JLabel(criteria));
            JTextField field = new JTextField();
            field.setPreferredSize(new Dimension(50, 20));
            field.setText(String.valueOf(criterias.get(criteria)));
            div.add(field);
            this.criteriaPanel.add(div);
        }
        criteriaPanel.setVisible(playerType.equals("Minimax") || playerType.equals("MinimaxAlphaBeta"));
        this.revalidate();
        this.repaint();
    }


}
