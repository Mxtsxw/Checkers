import javax.swing.*;
import java.awt.*;
import agents.AI;
import agents.Minimax;
import cherckers.Board;
import cherckers.Constants;
import cherckers.Game;

public class CheckersApplication {
    private JFrame frame;
    private Game game;
    private AI blackAI = new Minimax(Constants.BLACK, 5);
    private AI redAI = new Minimax(Constants.RED, 4);
    private final JPanel panel = new JPanel(new GridLayout(Constants.ROWS, Constants.COLS));

    public CheckersApplication() {
        // Create the main frame
        frame = new JFrame("Checkers Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Constants.WIDTH, Constants.HEIGHT);

        // Initialize Game
        this.game = new Game();

        // Initialize the scene
        frame.add(panel);
        frame.setVisible(true);

        // Display the game
        game.update();
        updateBoardPanel();

        // Start AI turns in a separate thread
        new Thread(this::startAIPlay).start();
    }

    /**
     * Update the board panel according to the game board state
     */
    private void updateBoardPanel() {
        this.panel.removeAll();
        for (int row = 0; row < Constants.ROWS; row++) {
            for (int col = 0; col < Constants.COLS; col++) {
                this.panel.add(this.game.getBoard().getBoard()[row][col]);
            }
        }
        this.panel.revalidate();
        this.panel.repaint();
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
                Thread.sleep(500); // Optional: Add a delay to make AI moves visible
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CheckersApplication::new);
    }
}
