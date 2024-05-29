import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import GUI.GUI;
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
    private final int defaultDepth = 5;
    private final int defaultIterations = 100;
    private final JPanel infoPanel;
    private final Map<String, Integer> defaultCriterias = Map.of(
        "Material", 2,
        "King", 5,
        "Eatable", -2,
        "Movable", 1,
        "Win", 1000
    );

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
        sidePanel.setLayout(new FlowLayout());
        sidePanel.setPreferredSize(new Dimension(300, Constants.HEIGHT));

        // Black player GUI
        GUI blackPanel = new GUI(blackAI);
        blackPanel.setPreferredSize(new Dimension(300, (Constants.HEIGHT/2)-50));
        blackPanel.playerSelector.addActionListener(e -> {
            String playerType = (String) blackPanel.playerSelector.getSelectedItem();
            blackPanel.updatePlayerParameterPanel(playerType);
            blackPanel.updateCriteriasPanel(playerType);
        });
        blackPanel.validateButton.addActionListener(e1 -> {
            Map<String, Integer> criterias = new HashMap<>();
            for (int i = 0; i < blackPanel.criteriaPanel.getComponentCount(); i++) {
                JPanel criteria = (JPanel) blackPanel.criteriaPanel.getComponent(i);
                String name = ((JLabel) criteria.getComponent(0)).getText();
                int value = Integer.parseInt(((JTextField) criteria.getComponent(1)).getText());
                System.out.println(name + " " + value);
                criterias.put(name, value);
            }
            blackAI = switch ((String) blackPanel.playerSelector.getSelectedItem()) {
                case "Minimax" -> new Minimax(Constants.BLACK, Integer.parseInt(blackPanel.depthField.getText()));
                case "MinimaxAlphaBeta" -> new MinimaxAlphaBeta(Constants.BLACK, Integer.parseInt(blackPanel.depthField.getText()));
                case "MCTS" -> new MonteCarloTreeSearch(Constants.BLACK, Integer.parseInt(blackPanel.iterationsField.getText()));
                default -> null;
            };
            if (blackAI != null) blackAI.setCriterias(criterias);
            updateAIInfo();
        });

        blackPanel.resetButton.addActionListener(e1 -> {
            blackPanel.updateCriteriasPanel((String) blackPanel.playerSelector.getSelectedItem());
            blackAI.setCriterias(defaultCriterias);
            // Update fields
            for (int i = 0; i < blackPanel.criteriaPanel.getComponentCount(); i++) {
                JPanel criteria = (JPanel) blackPanel.criteriaPanel.getComponent(i);
                String name = ((JLabel) criteria.getComponent(0)).getText();
                int value = defaultCriterias.get(name);
                ((JTextField) criteria.getComponent(1)).setText(String.valueOf(value));
            }

            // Update depth or iteration
            switch ((String) blackPanel.playerSelector.getSelectedItem()) {
                case "Minimax", "MinimaxAlphaBeta" -> blackPanel.depthField.setText(String.valueOf(defaultDepth));
                case "MCTS" -> blackPanel.iterationsField.setText(String.valueOf(defaultIterations));
            }
        });

        sidePanel.add(blackPanel);

        // Info Panel
        infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setBackground(Color.GRAY);
        infoPanel.setPreferredSize(new Dimension(300, 75));
        JLabel infoAIBlack = new JLabel("AI Black : " + blackAI);
        JLabel infoAIRed = new JLabel("AI Red : " + redAI);
        infoPanel.add(infoAIBlack);
        infoPanel.add(infoAIRed);
        sidePanel.add(infoPanel);


        // Red player GUI
        GUI redPanel = new GUI(redAI);
        redPanel.setPreferredSize(new Dimension(300, (Constants.HEIGHT/2)-50));
        redPanel.playerSelector.addActionListener(e -> {
            String playerType = (String) redPanel.playerSelector.getSelectedItem();
            redPanel.updatePlayerParameterPanel(playerType);
            redPanel.updateCriteriasPanel(playerType);
        });
        redPanel.validateButton.addActionListener(e1 -> {
            Map<String, Integer> criterias = new HashMap<>();
            for (int i = 0; i < redPanel.criteriaPanel.getComponentCount(); i++) {
                JPanel criteria = (JPanel) redPanel.criteriaPanel.getComponent(i);
                String name = ((JLabel) criteria.getComponent(0)).getText();
                int value = Integer.parseInt(((JTextField) criteria.getComponent(1)).getText());
                System.out.println(name + " " + value);
                criterias.put(name, value);
            }
            redAI = switch ((String) redPanel.playerSelector.getSelectedItem()) {
                case "Minimax" -> new Minimax(Constants.RED, Integer.parseInt(redPanel.depthField.getText()));
                case "MinimaxAlphaBeta" -> new MinimaxAlphaBeta(Constants.RED, Integer.parseInt(redPanel.depthField.getText()));
                case "MCTS" -> new MonteCarloTreeSearch(Constants.RED, Integer.parseInt(redPanel.iterationsField.getText()));
                default -> null;
            };
            if (redAI != null) redAI.setCriterias(criterias);
            updateAIInfo();
        });

        redPanel.resetButton.addActionListener(e1 -> {
            redPanel.updateCriteriasPanel((String) redPanel.playerSelector.getSelectedItem());
            redAI.setCriterias(defaultCriterias);
            // Update fields
            for (int i = 0; i < redPanel.criteriaPanel.getComponentCount(); i++) {
                JPanel criteria = (JPanel) redPanel.criteriaPanel.getComponent(i);
                String name = ((JLabel) criteria.getComponent(0)).getText();
                int value = defaultCriterias.get(name);
                ((JTextField) criteria.getComponent(1)).setText(String.valueOf(value));
            }

            // Update depth or iteration
            switch ((String) redPanel.playerSelector.getSelectedItem()) {
                case "Minimax", "MinimaxAlphaBeta" -> redPanel.depthField.setText(String.valueOf(defaultDepth));
                case "MCTS" -> redPanel.iterationsField.setText(String.valueOf(defaultIterations));
            }
        });
        sidePanel.add(redPanel);


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

    private void updateAIInfo() {
        System.out.println("Update AI Info");
        ((JLabel) this.infoPanel.getComponent(0)).setText("AI Black : " + blackAI);
        ((JLabel) this.infoPanel.getComponent(1)).setText("AI Red : " + redAI);

        this.infoPanel.revalidate();
        this.infoPanel.repaint();
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

}
