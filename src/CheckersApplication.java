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
        sidePanel.setLayout(new GridLayout(2, 1));
        sidePanel.setPreferredSize(new Dimension(300, Constants.HEIGHT));

        // Black player GUI
        GUI blackPanel = new GUI(blackAI);
        blackPanel.playerSelector.addActionListener(e -> {
            String playerType = (String) blackPanel.playerSelector.getSelectedItem();
            blackPanel.updatePlayerParameterPanel(playerType);
            blackPanel.updateCriteriasPanel(playerType);
        });
        sidePanel.add(blackPanel);

        // Red player GUI
        GUI redPanel = new GUI(redAI);
        redPanel.playerSelector.addActionListener(e -> {
            String playerType = (String) redPanel.playerSelector.getSelectedItem();
            redPanel.updatePlayerParameterPanel(playerType);
            redPanel.updateCriteriasPanel(playerType);
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
