package Game;
import Board.Board;
import IHandler.FilesHandler;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * this is a graphical menue for the user to choose which board he wants from a series of boards
 */
public class ChooseWindow extends JFrame implements ActionListener{

	private JButton _backButton,_sushiButton,_catButton,_cyberButton;
	private ImageIcon[] _iconsPack;
	private JLabel _chooseWindowsLabel,_backLabel;
	private ImagePanel _panel;
	private final Insets _INSETS = new Insets(5, 5, 5,5);
	private final Dimension _BUTTON_DIMENSION = new Dimension(160, 150);
	private int _puzzleSize;
	private StartPuzzleWindow _backWindow;
	private FilesHandler _filesHandler;
	private boolean _useCSV;

    /**
     * Constructor
     * @param filesHandler
     */
	public ChooseWindow(FilesHandler filesHandler) {
		super("Sliding Puzzle");
		_filesHandler = filesHandler;
		setSize(600	,400);
		setResizable(false);
		_iconsPack = _filesHandler.getChooseIconsPack();
		initiateWindow();
		add(_panel);
		pack();
	}

    /**
     * initiating Java Swing preferences
     */
	private void initiateWindow() {
		
		if (_filesHandler.getIcon() != null)
			setIconImage(_filesHandler.getIcon());
		_panel = new ImagePanel("Images/Background.jpg");
		GridBagConstraints gbco = new GridBagConstraints();
		gbco.insets = _INSETS;
		
		// ---- Labels ----
		_chooseWindowsLabel = new JLabel();
		_chooseWindowsLabel.setText("Choose a picture");
		_chooseWindowsLabel.setFont(new Font ("Arial",Font.BOLD, 20));
		_chooseWindowsLabel.setOpaque(true);
		_chooseWindowsLabel.setBackground(new Color(1,196,252,70));
		gbco.gridx = 1;
		gbco.gridy = 0;
		_panel.add(_chooseWindowsLabel, gbco);
		
		_backLabel = new JLabel();
		_backLabel.setText("Back to menu");
		_backLabel.setFont(new Font ("Arial",Font.BOLD, 20));
		_backLabel.setOpaque(true);
		_backLabel.setBackground(new Color(1,196,252,70));
		gbco.gridx = 1;
		gbco.gridy = 2;
		_panel.add(_backLabel, gbco);
		
		// ---- Buttons ----
		_catButton = new JButton();
		_catButton.setName("Cat");
		_catButton.setIcon(_iconsPack[0]);
		_catButton.addActionListener(this);
		_catButton.setPreferredSize(_BUTTON_DIMENSION);
		gbco.gridx = 0;
		gbco.gridy = 1;
		_panel.add(_catButton,gbco);

		_sushiButton = new JButton();
		_sushiButton.setName("Sushi");
		_sushiButton.setIcon(_iconsPack[1]);
		_sushiButton.addActionListener(this);
		_sushiButton.setPreferredSize(_BUTTON_DIMENSION);
		gbco.gridx = 1;
		gbco.gridy = 1;
		_panel.add(_sushiButton,gbco);

		_cyberButton = new JButton();
		_cyberButton.setName("Cyber");
		_cyberButton.setIcon(_iconsPack[2]);
		_cyberButton.addActionListener(this);
		_cyberButton.setPreferredSize(_BUTTON_DIMENSION);
		gbco.gridx = 2;
		gbco.gridy = 1;
		_panel.add(_cyberButton,gbco);		
		
		_backButton = new JButton();
		_backButton.setName("Back");
		_backButton.setIcon(_iconsPack[3]);
		_backButton.addActionListener(this);
		_backButton.setPreferredSize(new Dimension (80,80));
		gbco.gridx = 2;
		gbco.gridy = 2;
		_panel.add(_backButton,gbco);
	}

    /**
     * enabling visibility of this window
     * @param pS
     * @param useCSV
     * @param backWindow
     */
	public void openWindow(int pS, boolean useCSV, StartPuzzleWindow backWindow)
	{
		_useCSV = useCSV;
		_backWindow = backWindow;
		_puzzleSize = pS;
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton)e.getSource();
		String sender = button.getName();
		switch (sender)
		{
		case "Cyber":
		{
			_filesHandler.setPuzzleImage("Cyber");
			start(_puzzleSize,_useCSV);
			break;
		}
		case "Sushi":
		{
			_filesHandler.setPuzzleImage("Sushi");
			start(_puzzleSize,_useCSV);
			break;
		}
		case "Cat":
		{
			_filesHandler.setPuzzleImage("Cat");
			start(_puzzleSize,_useCSV);
			break;
		}
		case "Back":
		{
			setVisible(false);
			_backWindow.setVisible(true);
		}
		}
		
	}

	/**
	 * creates a random game
	 * choosing a picture of the sample pictures given in the assignment
	 * randomizes a puzzle size ( 3 , 4 or 5 sized)
	 */
	public void randomGame(boolean csvSelected) 
	{
		Random rand = new Random();
		int randomNum = rand.nextInt(3) + 3;		//find the next random able board on file
		while(csvSelected && !_filesHandler.legalCsvSize(randomNum)) 
			randomNum = rand.nextInt(3) + 3;
		Board board;
		_filesHandler.setPuzzleImage(rand.nextInt(3));
		if (csvSelected)
		{
			int[] firstBoard = _filesHandler.getBoardFromCSV(randomNum);
			board = new Board (randomNum, firstBoard);
		}
		else
			board = new Board(randomNum);
		play(board);
	}

    /**
     * initiating a game according to specific conditions
     * @param puzSize
     * @param useCSV
     */
	public void start(int puzSize, boolean useCSV)
	{
		_puzzleSize = puzSize;
		Board board;
		if (useCSV)
		{
			int[] firstBoard = _filesHandler.getBoardFromCSV(puzSize);
			board = new Board (puzSize, firstBoard);
		}
		else
			board = new Board (puzSize);
		play(board);
	}

	/**
	 * creates a new sliding puzzle game
	 */
	private void play(Board board) {
		
		PuzzleWindow p = new PuzzleWindow (board,_filesHandler);
		p.setLocationRelativeTo(this);
		dispose();
		if (_backWindow != null)
			_backWindow.dispose();
	}
	

}
