
# Checkers Game with AI Algorithms

This project implements a Checkers game in Java, featuring AI opponents using three different algorithms: Minimax, Alpha-Beta Pruning, and Monte-Carlo Tree Search (MCTS).

## Features 

- Basic Checkers Gameplay: Play against a human or AI opponent.
- Customizable AI Algorithms:
    - Minimax: Traditional adversarial search algorithm for decision-making.
    - Alpha-Beta Pruning: Enhances Minimax by reducing the number of nodes evaluated.
    - Monte-Carlo Tree Search (MCTS): A probabilistic search algorithm known for its success in games with large search spaces.
- MCTS Tree Visualization: Visual representation of the MCTS decision tree.

## Overview

![image](https://github.com/Mxtsxw/Checkers/assets/85303770/ada4c3ba-400c-4aff-b748-de1bad29d22e)

![image](https://github.com/Mxtsxw/Checkers/assets/85303770/e52aba0a-0ea5-4417-a550-896092e1db68)

## In-depth explanation

### Minimax Algorithm

The Minimax algorithm is a foundational approach to decision-making in adversarial environments like board games. In your Checkers game project:

#### Evaluation function 

To assess board configurations, the AI uses an evaluation function that assigns a numerical value to each possible game state. Key factors considered in this evaluation function include:

- **Piece Count**: The total number of pieces on the board. More pieces generally indicate a stronger position.

- **King Pieces**: Special consideration is given to kings, as they have greater mobility and strategic importance.

- **Mobility**: The number of moveable pieces can indicate better control of the game.

- **Threats and Vulnerabilities**: The AI evaluates positions where its pieces are vulnerable to capture (e.g., isolated pieces) and identifies opportunities to capture opponent pieces.

- **Wins or loses**: Assign an infinite score for wins (resp. negative infinite for loses).

#### Depth Limitation
The algorithm explores potential moves up to a specified depth on the game tree. This depth limit balances computational resources with the desire for optimal gameplay.

### Alpha-Beta Pruning
Alpha-Beta Pruning enhances Minimax by pruning branches of the game tree that do not affect the final decision, thereby reducing the number of nodes evaluated:

#### Performance Benefits
- **Efficiency**: Alpha-Beta Pruning allows the AI to explore deeper into the game tree within a reasonable time frame. By eliminating irrelevant branches early in the search, it focuses computation on more promising moves.

  - **Comparison**: Testing conducted on a 12th Generation Intel® Core™ i9 Processor (~5GHz) demonstrates that Alpha-Beta pruning extends the effective depth of the search tree by up to two levels in polynomial time.
 
- **Nodes Created**: The number of nodes expanded for a given depth is significantly lower with pruning compared to the basic Minimax approach.

  - **Comparison**: In practical terms, this reduction can be as substantial as a factor of 20 times fewer nodes.
 

### Monte-Carlo Tree Search (MCTS)
Monte-Carlo Tree Search (MCTS) is a heuristic search algorithm that is particularly effective for decision processes in large search spaces. It uses random sampling to construct a decision tree and focuses computational effort on the most promising branches.

Unlike traditional methods relying solely on evaluation functions, MCTS emphasizes iterative refinement and a balance between exploration and exploitation, governed by parameters such as the number of iterations and the Upper Confidence Bound for Trees (UCBT) constant.

This program offers customization options for adjusting the number of iterations and setting the UCBT constant. Additionally, it supports visualizing the generated tree, providing a comprehensive overview of the search process.

### Conclusion 

The MCTS heuristic shows a higher win rate, making it well-suited for Checkers due to its extensive state complexity. In contrast, recursive algorithms like Minimax and Alpha-Beta quickly reach their limitations. It's also worth noting that our evaluation function has significant room for improvement.

-----

## 🛠 Skills

- Development Skills in Java
- GUI Development using Swing
- Deep Understanding of AI Algorithms
  - Minimax
  - Alpha-Beta Pruning
  - Monte-Carlo Tree Search (MCTS)
- Analytical Process
  - Hypothesis formulation
  - Conducting tests
  - Making observations
- Performance Optimization: Identifying performance issues and implementing optimization solutions.
