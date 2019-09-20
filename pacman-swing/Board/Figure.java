package Board;
 
import javax.swing.*;

/**
 * a figure is a "cell" in the board holding its properties such as place and an image
 */
public class Figure extends JButton {

	private final int _SOL_CELL;
	private int _currentCell;

    /**
     * Constructor
     * @param index
     * @param figure
     */
	public Figure(int index ,ImageIcon figure) 
	{
		_currentCell = index;
		_SOL_CELL = index;
		this.setBorderPainted(false);
		this.setIcon(figure);
	}
	
	/**
	 * changes the current figure position
	 */
	public void setCurrentIndex(int index)
	{
		_currentCell = index;
	}

	// -------------------------- GETTERS -------------------------- //
	public int getCurrentIndex()
	{
		return _currentCell;
	}

}