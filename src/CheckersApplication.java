import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import agents.AI;
import agents.MonteCarloTreeSearch;
import agents.Minimax;
import agents.MinimaxAlphaBeta;
import cherckers.Board;
import cherckers.Constants;
import cherckers.Game;

public class CheckersApplication {
    private JFrame frame;
    private Game game;
    private AI blackAI = new MonteCarloTreeSearch(Constants.BLACK, 100);
    private AI redAI = new MinimaxAlphaBeta(Constants.RED, 5);
    private final JPanel boardPanel = new JPanel(new GridLayout(Constants.ROWS, Constants.COLS));

    private Map<String, JTextField> blackCriteriaFields = new HashMap<>();
    private Map<String, JTextField> redCriteriaFields = new HashMap<>();

    public CheckersApplication() {
        // Create the main frame
        frame = new JFrame("Checkers Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Constants.WIDTH, Constants.HEIGHT);

        // Board GUI
        boardPanel.setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));

        // Initialize Game
        this.game = new Game();

        // Set the Main panel
        frame.setLayout(new BorderLayout());

        // Side panel
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new GridLayout(2, 1));
        sidePanel.setPreferredSize(new Dimension(200, Constants.HEIGHT));

        // Player 1 control panel
        JPanel player1ControlPanel = buildPlayerControlPanel("Player BLACK", blackCriteriaFields);
        sidePanel.add(player1ControlPanel);

        // Player 2 control panel
        JPanel player2ControlPanel = buildPlayerControlPanel("Player RED", redCriteriaFields);
        sidePanel.add(player2ControlPanel);

        // Initialize the scene
        frame.add(boardPanel, BorderLayout.CENTER);
        frame.add(sidePanel, BorderLayout.EAST);
        frame.setVisible(true);
        frame.pack();

        // Prevent click
        game.disableClickEvent();

        // Display the game
        game.update();
        updateBoardPanel();

        // Start AI turns in a separate thread
        new Thread(this::startAIPlay).start();
    }

    /**
     * Update the board boardPanel according to the game board state
     */
    private void updateBoardPanel() {
        this.boardPanel.removeAll();
        for (int row = 0; row < Constants.ROWS; row++) {
            for (int col = 0; col < Constants.COLS; col++) {
                this.boardPanel.add(this.game.getBoard().getBoard()[row][col]);
            }
        }
        this.boardPanel.revalidate();
        this.boardPanel.repaint();
    }

    private void startAIPlay() {
        while (!game.isTerminal()) {
            Board board;
            if (game.getTurn().equals(Constants.BLACK)) {
                board = blackAI.run(this.game);
            } else {
                board = redAI.run(this.game);
            }
            game.aiMove(board);
            SwingUtilities.invokeLater(() -> {
                game.update();
                updateBoardPanel();
            });
            try {
                Thread.sleep(1000); // Optional: Add a delay to make AI moves visible
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CheckersApplication::new);
    }

    public JPanel buildControlPanel(){
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout());
        controlPanel.setPreferredSize(new Dimension(200, Constants.HEIGHT/2));
        controlPanel.setBackground(Color.BLUE);
        return controlPanel;
    }

    /**
     * Create a panel with criteria selection for evaluation
     * @param label for criteria
     */
    public JPanel buildEvaluationCriteriaPanel(String label, Map<String, JTextField> criteriaFields) {
        JPanel panel = new JPanel();
        FlowLayout layout = new FlowLayout();
        layout.setAlignment(FlowLayout.LEFT);
        panel.setLayout(layout);

        panel.setBackground(Color.YELLOW);

        JLabel criteriaLabel = new JLabel(label);
        panel.add(criteriaLabel);

        // Input for weight
        JTextField criteriaWeight = new JTextField();
        criteriaWeight.setPreferredSize(new Dimension(50, 20));
        panel.add(criteriaWeight);

        // Store reference to the text field
        criteriaFields.put(label, criteriaWeight);

        return panel;
    }

    /**
     * Create a panel with criteria selection for evaluation
     */
    public JPanel buildEvaluationCriteriaGridPanel(Map<String, JTextField> criteriaFields) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));
        panel.setPreferredSize(new Dimension(200, 200));
        panel.setBackground(Color.YELLOW);

        JPanel criteriaPanel1 = buildEvaluationCriteriaPanel("Material", criteriaFields);
        JPanel criteriaPanel2 = buildEvaluationCriteriaPanel("King", criteriaFields);
        JPanel criteriaPanel3 = buildEvaluationCriteriaPanel("Eatable", criteriaFields);
        JPanel criteriaPanel4 = buildEvaluationCriteriaPanel("Win", criteriaFields);

        panel.add(criteriaPanel1);
        panel.add(criteriaPanel2);
        panel.add(criteriaPanel3);
        panel.add(criteriaPanel4);

        return panel;
    }

    public JPanel buildAIEvaluationPanel(Map<String, JTextField> criteriaFields){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Dropdown field
        JPanel playerDropdownPanel = buildPlayerDropdown(criteriaFields);
        panel.add(playerDropdownPanel);

        // Evaluation criteria panel
        JPanel evaluationCriteriaPanel = buildEvaluationCriteriaGridPanel(criteriaFields);
        panel.add(evaluationCriteriaPanel);

        return panel;
    }

    public JPanel buildLabelPanel(String label) {
        JPanel panel = new JPanel();
        FlowLayout layout = new FlowLayout();
        layout.setAlignment(FlowLayout.CENTER);
        panel.setLayout(layout);

        panel.setBackground(Color.GREEN);

        JLabel criteriaLabel = new JLabel(label);
        panel.add(criteriaLabel);

        return panel;
    }

    /**
     * Create a dropdown field for player selection
     */
    public JPanel buildPlayerDropdown(Map<String, JTextField> criteriaFields) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Dropdown field
        String[] players = { "Player", "Minimax", "MinimaxAlphaBeta", "MonteCarloTreeSearch"};
        JComboBox<String> playerDropdown = new JComboBox<>(players);
        playerDropdown.setPreferredSize(new Dimension(100, 20));
        panel.add(playerDropdown);

        JPanel criteriaContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(criteriaContainer);

        // How to detect the selected player type
        playerDropdown.addActionListener(e -> {
            drawPlayerControlPanel(criteriaContainer, (String) playerDropdown.getSelectedItem(), criteriaFields);
        });

        return panel;
    }

    /**
     * Draw additional components for the player control panel according to the player type
     */
    public void drawPlayerControlPanel(JPanel panel, String playerType, Map<String, JTextField> criteriaFields) {
        panel.removeAll();

        switch (playerType) {
            case "Minimax", "MinimaxAlphaBeta" -> panel.add(buildEvaluationCriteriaGridPanel(criteriaFields));
            case "MonteCarloTreeSearch" -> panel.add(buildMCTSIterationInput());
            default -> {
            }
        }

        panel.revalidate();
        panel.repaint();
    }

    public JPanel buildMCTSIterationInput(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel iterationLabel = new JLabel("Iterations");
        panel.add(iterationLabel);

        JTextField iterationField = new JTextField();
        iterationField.setPreferredSize(new Dimension(50, 20));
        panel.add(iterationField);

        return panel;
    }

    /**
     * Player Control panel
     */
    public JPanel buildPlayerControlPanel(String title, Map<String, JTextField> criteriaFields){
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.setPreferredSize(new Dimension(200, Constants.HEIGHT/2));
        controlPanel.setBackground(Color.BLUE);

        // Section Label
        JPanel titlePanel = buildLabelPanel(title);
        controlPanel.add(titlePanel, BorderLayout.NORTH);

        // Merge player dropdown and evaluation criteria panel
        JPanel aiEvaluationPanel = buildPlayerDropdown(criteriaFields);
        controlPanel.add(aiEvaluationPanel, BorderLayout.CENTER);

        // Validation panel
        JPanel validationPanel = validationPanel(criteriaFields);
        controlPanel.add(validationPanel, BorderLayout.SOUTH);

        return controlPanel;
    }

    /**
     * Two buttons to reset and validate the evaluation criteria and player selection
     */
    public JPanel validationPanel(Map<String, JTextField> criteriaFields){
        JPanel validationPanel = new JPanel();
        validationPanel.setLayout(new FlowLayout());
        validationPanel.setPreferredSize(new Dimension(200, 50));
        validationPanel.setBackground(Color.RED);

        JButton resetButton = new JButton("Reset");
        JButton validateButton = new JButton("Validate");

        validateButton.addActionListener(e -> validateCriteriaFields(criteriaFields));
        resetButton.addActionListener(e -> resetCriteriaFields(criteriaFields));

        validationPanel.add(resetButton);
        validationPanel.add(validateButton);

        return validationPanel;
    }

    private void resetCriteriaFields(Map<String, JTextField> criteriaFields) {
        for (JTextField field : criteriaFields.values()) {
            field.setText("0");
        }
    }

    private void validateCriteriaFields(Map<String, JTextField> criteriaFields) {
        Map<String, String> criteriaValues = new HashMap<>();
        for (Map.Entry<String, JTextField> entry : criteriaFields.entrySet()) {
            criteriaValues.put(entry.getKey(), entry.getValue().getText());
        }

        // Print the values to the console (or handle as needed)
        System.out.println("Criteria Values: " + criteriaValues);
    }

    private void setBlackAI(String ai){
        switch (ai) {
            case "Minimax" -> blackAI = new Minimax(Constants.BLACK, 3);
            case "MinimaxAlphaBeta" -> blackAI = new MinimaxAlphaBeta(Constants.BLACK, 5);
            case "MonteCarloTreeSearch" -> blackAI = new MonteCarloTreeSearch(Constants.BLACK, 100);
            default -> blackAI = new Minimax(Constants.BLACK, 3);
        }
    }
}
