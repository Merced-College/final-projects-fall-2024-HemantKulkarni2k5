import java.util.*;

public class TicTacToe {
    // Board and move history
    private static char[][] board = {
        {' ', ' ', ' '},
        {' ', ' ', ' '},
        {' ', ' ', ' '}
    };

    // Stack for move history (to undo last move)
    private static Stack<String> moveStack = new Stack<>();

    // Undo counters for each player
    private static int undoCounterX = 0;  // Player X's undo counter
    private static int undoCounterO = 0;  // Player O's undo counter

    // Player symbols and current player
    private static char currentPlayer = 'X';

    // Queue to store game history results
    private static Queue<String> gameHistory = new LinkedList<>();

    // Main game loop
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Game loop for play-again feature
        boolean playAgain = true;
        while (playAgain) {
            resetBoard();  // Reset board for a new game

            // Ask for the game mode
            System.out.println("Welcome to TicTacToe!");
            System.out.println("Choose game mode:");
            System.out.println("1. 1v1 Mode");
            System.out.println("2. Play against Computer");

            int mode = getValidModeInput(scanner);

            // Game loop for each round
            while (true) {
                printBoard();

                if (mode == 1) {
                    // 1v1 Mode - Human vs Human
                    int row = -1, col = -1;
                    boolean validMove = false;
                    while (!validMove) {
                        try {
                            System.out.println("Player " + currentPlayer + ", enter your move (row and column): ");
                            row = Integer.parseInt(scanner.nextLine());
                            col = Integer.parseInt(scanner.nextLine());

                            if (isValidMove(row, col)) {
                                validMove = true;
                            } else {
                                System.out.println("Invalid move, try again.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter two integers for the row and column (0, 1, or 2).");
                        }
                    }

                    // Make the move
                    board[row][col] = currentPlayer;

                    // Record the move in the move stack
                    moveStack.push("Player " + currentPlayer + " moved to (" + row + "," + col + ")");

                    // After the move, check for undo
                    handleUndo(scanner);

                    // Check for winner
                    if (checkWinner(currentPlayer)) {
                        printBoard();
                        System.out.println("Player " + currentPlayer + " wins!");
                        // Store the result in the game history
                        gameHistory.add("Player " + currentPlayer + " wins!");
                        break;
                    }

                    // Check for draw
                    if (isBoardFull()) {
                        printBoard();
                        System.out.println("The game is a draw!");
                        // Store the result in the game history
                        gameHistory.add("The game is a draw!");
                        break;
                    }

                    // Switch player
                    currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
                } else if (mode == 2) {
                    // Play against Computer
                    if (currentPlayer == 'X') {
                        // Human Player (X) move
                        int row = -1, col = -1;
                        boolean validMove = false;
                        while (!validMove) {
                            try {
                                System.out.println("Player X, enter your move (row and column): ");
                                row = Integer.parseInt(scanner.nextLine());
                                col = Integer.parseInt(scanner.nextLine());

                                if (isValidMove(row, col)) {
                                    validMove = true;
                                } else {
                                    System.out.println("Invalid move, try again.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid input. Please enter two integers for the row and column (0, 1, or 2).");
                            }
                        }

                        // Make the move
                        board[row][col] = currentPlayer;

                        // Record the move in the move stack
                        moveStack.push("Player " + currentPlayer + " moved to (" + row + "," + col + ")");

                        // After the move, check for undo
                        handleUndo(scanner);

                        // Check for winner
                        if (checkWinner(currentPlayer)) {
                            printBoard();
                            System.out.println("Player " + currentPlayer + " wins!");
                            // Store the result in the game history
                            gameHistory.add("Player " + currentPlayer + " wins!");
                            break;
                        }

                        // Switch player
                        currentPlayer = 'O';
                    } else {
                        // Computer's (O) move
                        System.out.println("Computer's turn (O)...");

                        // Let the computer make a random valid move
                        makeComputerMove();

                        // Check for winner
                        if (checkWinner(currentPlayer)) {
                            printBoard();
                            System.out.println("Player " + currentPlayer + " wins!");
                            // Store the result in the game history
                            gameHistory.add("Player " + currentPlayer + " wins!");
                            break;
                        }

                        // Switch player
                        currentPlayer = 'X';
                    }
                }
            }

            // Ask if the user wants to play again
            System.out.println("Do you want to play again? (yes/no): ");
            String response = scanner.nextLine();
            if (!response.equalsIgnoreCase("yes")) {
                playAgain = false;
            }

            // Display game history
            System.out.println("\nGame History:");
            for (String game : gameHistory) {
                System.out.println(game);
            }
        }

        scanner.close();
        System.out.println("Thank you for playing!");
    }

    // Get valid game mode input
    private static int getValidModeInput(Scanner scanner) {
        int mode = 0;
        boolean validMode = false;
        while (!validMode) {
            try {
                mode = Integer.parseInt(scanner.nextLine());
                if (mode == 1 || mode == 2) {
                    validMode = true;
                } else {
                    System.out.println("Invalid choice. Please enter 1 for 1v1 Mode or 2 for Play against Computer:");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter 1 for 1v1 Mode or 2 for Play against Computer:");
            }
        }
        return mode;
    }

    // Reset the board for a new game
    private static void resetBoard() {
        board = new char[][] {
            {' ', ' ', ' '},
            {' ', ' ', ' '},
            {' ', ' ', ' '}
        };
        moveStack.clear();
        undoCounterX = 0;
        undoCounterO = 0;
        currentPlayer = 'X';
    }

    // Print the board with coordinates for empty spots
    private static void printBoard() {
        System.out.println("Current Board:");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // If the cell is empty, show the coordinates, otherwise show the player symbol
                if (board[i][j] == ' ') {
                    System.out.print("(" + i + "," + j + ")");
                } else {
                    System.out.print(board[i][j]);
                }

                if (j < 2) {
                    System.out.print(" | ");  // Separator between columns
                }
            }
            System.out.println();
            if (i < 2) {
                System.out.println("--+---+--");  // Separator between rows
            }
        }
    }

    // Validation of the move
    private static boolean isValidMove(int row, int col) {
        return row >= 0 && row < 3 && col >= 0 && col < 3 && board[row][col] == ' ';
    }

    // Check if the current player has won
    private static boolean checkWinner(char player) {
        // Check rows, columns, and diagonals
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
                return true; // Row win
            }
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) {
                return true; // Column win
            }
        }
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true; // Diagonal win top left to bottom right
        }
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            return true; // Diagonal win top right to bottom left
        }
        return false;
    }

    // Handle undo for players X and O
    private static void handleUndo(Scanner scanner) {
        if (currentPlayer == 'X' && undoCounterX == 0) { // Allow undo for Player X
            System.out.println("Player X, would you like to undo your last move? (yes/no): ");
            String response = scanner.nextLine();

            // Undo last move if the player wants it
            if (response.equalsIgnoreCase("yes")) {
                undoLastMove();
                undoCounterX++;  // Increment Player X's undo counter
            }
        } else if (currentPlayer == 'O' && undoCounterO == 0) { // Allow undo for Player O
            System.out.println("Player O, would you like to undo your last move? (yes/no): ");
            String response = scanner.nextLine();

            // Undo last move if the player wants it
            if (response.equalsIgnoreCase("yes")) {
                undoLastMove();
                undoCounterO++;  // Increment Player O's undo counter
            }
        }
    }

    // Undo the last move (if there is any)
    private static void undoLastMove() {
        if (!moveStack.isEmpty()) {
            // Pop the most recent move
            String lastMove = moveStack.pop();
            String[] coords = lastMove.split(",");
            int row = Integer.parseInt(coords[0]);
            int col = Integer.parseInt(coords[1]);

            // Reset the corresponding cell
            board[row][col] = ' ';
            System.out.println("Undo successful. The last move has been undone.");
        } else {
            System.out.println("No moves to undo.");
        }
    }

    // Make a random move for the computer
    private static void makeComputerMove() {
        Random rand = new Random();
        int row, col;

        // Find a random valid move
        do {
            row = rand.nextInt(3);
            col = rand.nextInt(3);
        } while (!isValidMove(row, col));

        // Make the move
        board[row][col] = 'O';
        System.out.println("Computer placed 'O' at (" + row + ", " + col + ")");
    }

    // Check if the board is full (to check for draw)
    private static boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }
}
