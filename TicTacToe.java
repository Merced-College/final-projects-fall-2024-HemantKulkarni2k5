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
    
    // Main game loop
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Game loop
        while (true) {
            printBoard();
            
            // Get player move
            System.out.println("Player " + currentPlayer + ", enter your move (row and column): ");
            int row = scanner.nextInt();
            int col = scanner.nextInt();
            
            // Validate move
            if (isValidMove(row, col)) {
                // Make the move
                board[row][col] = currentPlayer;
                
                // Record the move in the move stack
                moveStack.push(row + "," + col);
                
                // After the move, check for undo
                if (currentPlayer == 'X' && undoCounterX == 0) { // Allow undo for Player X
                    System.out.println("Player X, would you like to undo your last move? (yes/no): ");
                    String response = scanner.next();
                    
                    // Undo last move if the player wants it
                    if (response.equalsIgnoreCase("yes")) {
                        undoLastMove();
                        undoCounterX++;  // Increment Player X's undo counter
                    }
                } else if (currentPlayer == 'O' && undoCounterO == 0) { // Allow undo for Player O
                    System.out.println("Player O, would you like to undo your last move? (yes/no): ");
                    String response = scanner.next();
                    
                    // Undo last move if the player wants it
                    if (response.equalsIgnoreCase("yes")) {
                        undoLastMove();
                        undoCounterO++;  // Increment Player O's undo counter
                    }
                }
                
                // Check for winner
                if (checkWinner(currentPlayer)) {
                    printBoard();
                    System.out.println("Player " + currentPlayer + " wins!");
                    break;
                }
                
                // Switch player
                currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
            } else {
                System.out.println("Invalid move, try again.");
            }
        }
        
        scanner.close();
    }
    
    // Print the board
    private static void printBoard() {
        System.out.println("Current Board:");
        for (int i = 0; i < 3; i++) {
            System.out.println(board[i][0] + " | " + board[i][1] + " | " + board[i][2]);
            if (i < 2) {
                System.out.println("--+---+--");
            }
        }
    }
    //slide 5 Validation of the move
    private static boolean isValidMove(int row, int col) {
        return row >= 0 && row < 3 && col >= 0 && col < 3 && board[row][col] == ' ';
    }

    // Slide #6 Check if the current player has won
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

    // Slide #7 Undo the last move (if there is any)
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
}
