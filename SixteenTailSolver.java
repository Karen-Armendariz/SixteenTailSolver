package nineTailModel;

import java.util.*;

public class SixteenTailSolver {
    private static final int GRID_SIZE = 4;  //4x4 grid, 16 cells

    public static void main(String[] args) {
        boolean solved = false;
        int attempt = 0;

        while (!solved) {     //loop until solved (all heads or all tails)
            attempt++;
            System.out.println("Attempt " + attempt);
            
            int initialState = generateRandomState();   //randomly select H or T for cells to start
            System.out.println("Initial State:");
            printState(initialState);

            int solution = solve(initialState);    //try to get all heads or tails

            if (solution == -1) {   //-1 is not found or not solved in this case. Restart with new grid.
                System.out.println("No solution found. Trying a new configuration...\n");  //shows unsolved grid
            } else {
                System.out.println("Solution found in " + attempt + " attempts!");  //shows solved grid and # of attempts
                printState(solution);
                solved = true;
            }
        }
    }

    public static int solve(int initialState) {    //BFS to find shortest solved sequence.
        Set<Integer> visited = new HashSet<>();    //tracks visited cells and results and prevents making same visits.
        Queue<int[]> queue = new LinkedList<>();   //initializes queue to keep the grid state and the # of moves to reach that state.
        queue.add(new int[]{initialState, 0});		//add integers to the end of the queue. creates array with two integers
        visited.add(initialState);					//initial state of grid is added to visited set(all grid states already processed).
        											//makes sure the same config. is not visited over and over.  BFS efficiency tool.
        while (!queue.isEmpty()) {					//runs as long as there are elements in queue(from initial state on)
            int[] current = queue.poll();			//removes/retrieves front queue element.
            int state = current[0];					//state: current grid config.
            int moves = current[1];					//moves: number of moves to reach the above state.

            if (isUniformState(state)) {			//method that checks if grid is all H or all T
                System.out.println("Solved in " + moves + " moves!");
                return state;
            }

            for (int i = 0; i < GRID_SIZE * GRID_SIZE; i++) {	//for loop that flips the cell and corresponding connecting cells
                int nextState = getFlippedNode(state, i);		//
                if (!visited.contains(nextState)) {				//If this config was not seen previously, then it is marked as now visited
                    visited.add(nextState);
                    queue.add(new int[]{nextState, moves + 1});
                }
            }
        }
        return -1; 							// Return -1 if no solution is found for the given initial state
    }

    private static int getFlippedNode(int state, int position) {	//two parameter method: state -H or T, position: cell index to flip
        char[] node = toCharArray(state);
        int row = position / GRID_SIZE;
        int column = position % GRID_SIZE;

        flipACell(node, row, column);
        flipACell(node, row - 1, column); // Above
        flipACell(node, row + 1, column); // Below
        flipACell(node, row, column - 1); // Left
        flipACell(node, row, column + 1); // Right

        return getIndex(node);
    }

    private static void flipACell(char[] node, int row, int col) {
        if (row >= 0 && row < GRID_SIZE && col >= 0 && col < GRID_SIZE) {
            int index = row * GRID_SIZE + col;
            node[index] = (node[index] == '0') ? '1' : '0';
        }
    }

    private static char[] toCharArray(int state) {
        char[] result = new char[GRID_SIZE * GRID_SIZE];
        for (int i = 0; i < GRID_SIZE * GRID_SIZE; i++) {
            result[i] = ((state >> i) & 1) == 0 ? '0' : '1';
        }
        return result;
    }

    private static int getIndex(char[] node) {
        int result = 0;
        for (int i = 0; i < node.length; i++) {
            if (node[i] == '1') {
                result |= (1 << i);
            }
        }
        return result;
    }

    private static boolean isUniformState(int state) {
        return state == 0 || state == (1 << (GRID_SIZE * GRID_SIZE)) - 1;
    }

    private static int generateRandomState() {
        Random random = new Random();
        int state = 0;
        for (int i = 0; i < GRID_SIZE * GRID_SIZE; i++) {
            state |= (random.nextInt(2) << i);
        }
        return state;
    }

    private static void printState(int state) {
        for (int i = 0; i < GRID_SIZE * GRID_SIZE; i++) {
            System.out.print(((state >> i) & 1) == 0 ? "H " : "T ");
            if ((i + 1) % GRID_SIZE == 0) System.out.println();
        }
        System.out.println();
    }
}
