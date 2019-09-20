package Board;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

/**
 * representing a board of puzzle game, holds the pieces of the puzzle and possible moves around itself
 */
public class Board {
	
	private final int _DIMENSIONS;
	private int[] _positions;
	private final int _TOTAL_FIGURES;
	private Stack _boardsStack;

    /**
     * Constructor setting up the basic prefferencess of the board
     * @param puzzleSize
     */
	public Board(int puzzleSize) 
	{
		_DIMENSIONS = puzzleSize;
		_TOTAL_FIGURES = _DIMENSIONS *_DIMENSIONS;
		_positions = new int [_TOTAL_FIGURES];
		_boardsStack = new Stack();
		boardShuffle();

	}

    /**
     *   additional constructor for creating a specific board of given array
     */
	public Board (int _puzzleSize, int[] board)
	{
		_positions = board;
		_DIMENSIONS = _puzzleSize;
		_TOTAL_FIGURES = _DIMENSIONS*_DIMENSIONS;
		_boardsStack = new Stack();
	}

	/**
	 * duplicates positions board
	 * @return a duplicated board
	 */
	private int[] duplicateBoard() 
	{
		int tN = _TOTAL_FIGURES;
		int[] tBoard = new int [tN];
		for (int i=0; i<tN ; i++)
			tBoard[i] = _positions[i];
		return tBoard;
	}

	/**
	 * Shuffling the board itself and adding it to the JPanel
	 */
    private void boardShuffle() 
    {
        Random randomGenerator = new Random();
        int randomNumber = randomGenerator.nextInt(_DIMENSIONS * 500 + 1) + 50;    //making sure randomNumber is not zero
        orderInSolution();
        while (randomNumber != 0) {
            int zeroIdx = findZero();
            int[] neigh = findNeighboursOfZero(zeroIdx);
            int movingNeigh = findLegalNeighbourOfZero(neigh, zeroIdx);
            switchFig(zeroIdx, movingNeigh);
            randomNumber--;
        }
        clearStack();
    }

    /**
     * finds zero's legal neighbors
     * @param neigh array of neighbors
     * @param zeroIdx zero's index
     * @return
     */
    private int findLegalNeighbourOfZero(int[] neigh, int zeroIdx) {
        ArrayList<Integer> legalNeigh = new ArrayList<Integer>();
        for (int i = 0; i < neigh.length; i++)
            if (isLegalMove(neigh[i], zeroIdx))
                legalNeigh.add(neigh[i]);
        Random randomGenerator = new Random();
        return legalNeigh.get(randomGenerator.nextInt(legalNeigh.size()));
    }

    /**
     * finds zero's neighbors
     * @param zeroIdx
     * @return
     */
    private int[] findNeighboursOfZero(int zeroIdx) {
        int[] neigh = new int[4];
        neigh[0] = zeroIdx - 1;
        neigh[1] = zeroIdx + 1;
        neigh[2] = zeroIdx - _DIMENSIONS;
        neigh[3] = zeroIdx + _DIMENSIONS;
        return neigh;
    }

    /**
     * checks whether a certain move of two index is legal
     * @param i
     * @param zero
     * @return
     */
    private boolean isLegalMove(int i, int zero) {
        int tBig, tSmall;
        if (i > zero) {
            tBig = i;
            tSmall = zero;
        } else {
            tSmall = i;
            tBig = zero;
        }
        if (((tBig - tSmall == 1) && (tBig % _DIMENSIONS == 0) && 
        		(tSmall%_DIMENSIONS == _DIMENSIONS - 1)) 
        		|| (i < 0) || (i >=_TOTAL_FIGURES)){
            return false;
        }
        return true;
    }

	/**
	 * places figures in solution places
	 */
    private void orderInSolution() {
        for (int i = 0; i < _TOTAL_FIGURES - 1; i++)
            _positions[i] = i + 1;
        _positions[_TOTAL_FIGURES - 1] = 0;
	}

	/**
	 * switch indexes of given figure's indexes
	 * @param a is an index of figure
	 * @param b is an index of figure
	 */
	public void switchFig(int a, int b) 
	{
		pushToStack();
		int temp = _positions[a];
		_positions[a] = _positions[b];
		_positions[b] = temp;
	}

	/**
	 * finds the index which holds the empty figure
	 * @return
	 */
	public int findZero() {
		for (int i = 0; i < _TOTAL_FIGURES; i++)
			if (_positions[i] == 0)
				return i;
		return 0;
	}

	/**
	 * returns to the previous board
	 * @param figures is the figures array
	 * @return true whether undo move is done, else returns false 
	 */
	public boolean undoMove(ArrayList<Figure> figures) {
		if (_boardsStack.isEmpty())
			return false;
		_positions = (int[]) _boardsStack.pop();
		applyBoard(figures);
		return true;
	}

	/**
	 * sets the figures in their right place on board
	 * @param figures
	 */
    public void applyBoard(ArrayList<Figure> figures) {
        for (int i = 0; i < _TOTAL_FIGURES; i++) {
            int x = _positions[i];
            if (x != 0) {
                Figure tempFig = figures.get(x - 1);
                tempFig.setCurrentIndex(i + 1);
            }
        }
    }

	/**
	 * checks if the game is done
	 */
	public boolean checkAnswer() {
		for (int i = 0; i < _TOTAL_FIGURES - 1; i++) {
			if (_positions[i] != i + 1) {
				return false;
			}
		}
		return true;
	}

	/**
	 * clears the boards stack
	 */
	public void clearStack() {
		_boardsStack.clear();
	}

	/**
	 * pushes the current board onto the stack
	 */
	public void pushToStack() {
		_boardsStack.push(duplicateBoard());
	}

	/**
	 * creates a new game
	 * @param bFigures
	 * @return
	 */
	public void playAgain(ArrayList<Figure> bFigures)
	{
		_positions = new int[_TOTAL_FIGURES];
		boardShuffle();
		applyBoard(bFigures);
	}
	

	// --- GETTERS ---
	/**
	 * returns the number of total figures needed to create the board
	 * @return
	 */
	public int getTotalFigures()
	{
		return _TOTAL_FIGURES;
	}

	/**
	 * return the board's dimension
	 * @return
	 */
	public int getDimension()
	{
		return _DIMENSIONS;
	}

	/**
	 * return the value of index i in positions array
	 * @param i
	 * @return
	 */
	public int get(int i) {
		return _positions[i];
	}
}