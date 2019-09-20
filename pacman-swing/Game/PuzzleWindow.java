package Game;
import Board.*;
import IHandler.FilesHandler;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;

/**
 * this class is responsible for the game itself and cycling it
 */
public class PuzzleWindow extends JFrame implements ActionListener, KeyListener {

	// --- TIMER ---
	private Timer _timer, _popupTimer;
	private int _seconds,_minutes,_hours;

	// --- HEADER TOOLBAR ---
	private JPanel _controlsBar;
	private JButton _hintButton,_undoButton, _stopStartButton, _changeImageButton, _menuButton;
	private ImageIcon[] _imagesPack;	

	// --- FOOTER INFO ---
	private JLabel _timerLabel,_movesCounterLabel;
	private int _movesCounter;
	private JPanel _infoBar;

	// --- GAME STATUS ---
	private boolean _isFinished,_isStopped;
	private JFrame _hintPopup;
	
	// --- BOARD ---
	private JPanel _board;
	private int _dimension, _n,_figureSize;
	private JLabel _emptyFigure;
	private ArrayList<Figure> _boardFigures; //Data Structure to hold the board.
	private Dimension _figureDimension;
	private Board _boardDS;
	private BufferedImage _puzzleImage;
	private FilesHandler _filesHandler;


	// --- CONSTRUCTOR ---
	/**
	 * Puzzle object constructor, creates a new windows and adds components using addComponents method
	 * @param board is the sliding puzzle game component
	 */
	public PuzzleWindow (Board board, FilesHandler filesHandler)
	{
		super("Sliding Puzzle");
		_filesHandler = filesHandler;
		_puzzleImage = _filesHandler.getPuzzleImage();
		_boardDS = board;
		_imagesPack = _filesHandler.getPuzzlePack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initiateWindow();
		
		addKeyListener(this);
		setFocusable(true);
		setResizable(false);
		setVisible(true);		
		
		updater();
	}

	private void initiateWindow()
	{
		if (_filesHandler.getIcon() != null)
			setIconImage(_filesHandler.getIcon());
		Color buttonColor = new Color(248,244,233);
		Color bgd = new Color (248,244,233);
		
		// initialize header toolbar items
		_isStopped = false;
		_isFinished = false;
		_stopStartButton = new JButton("Stop");
		_stopStartButton.setIcon(_imagesPack[0]);
		_stopStartButton.setBackground(buttonColor);
		_stopStartButton.addActionListener(this);

		_undoButton = new JButton("Undo");
		_undoButton.setIcon(_imagesPack[2]);
		_undoButton.addActionListener(this);
		_undoButton.setBackground(buttonColor);

		_hintButton = new JButton("Hint");
		_hintButton.setIcon(_imagesPack[8]);
		_hintButton.setBackground(buttonColor);
		_hintButton.addActionListener(this);
		_popupTimer = new Timer(700,this);
		
		//popup hind window
		_hintPopup = new JFrame();
		BufferedImage popImg = _filesHandler.resizePictures(_puzzleImage);
		JPanel hintPopup = new ImagePanel(popImg);
		_hintPopup.setLocationRelativeTo(this);
		_hintPopup.setSize(700, 700);
		_hintPopup.setResizable(false);
		_hintPopup.add(hintPopup);
		
		_changeImageButton = new JButton("Change");
		_changeImageButton.setIcon(_imagesPack[3]);
		_changeImageButton.addActionListener(this);
		_changeImageButton.setBackground(buttonColor);

		_menuButton = new JButton("Menu");
		_menuButton.setIcon(_imagesPack[4]);
		_menuButton.addActionListener(this);
		_menuButton.setBackground(buttonColor);

		// add items to toolbar
		_controlsBar = new JPanel();
		_controlsBar.setBackground(bgd);
		_controlsBar.setLayout(new FlowLayout(FlowLayout.CENTER));
        _controlsBar.add(_stopStartButton);
        _controlsBar.add(_hintButton);
        _controlsBar.add(_undoButton);
        _controlsBar.add(_changeImageButton);
        _controlsBar.add(_menuButton);
        
		// initialize timer 
        _timerLabel = new JLabel();
        _timerLabel.setIcon(_imagesPack[7]);
		resetTimer();
		_timer = new Timer(1000,this);
		_timer.start();

		//initialize counter
		_movesCounter = -1;
		_movesCounterLabel = new JLabel();
	    _movesCounterLabel.setIcon(_imagesPack[6]);
		_movesCounterLabel.setText("Total moves: "+_movesCounter);

		// add timer and moves counter to info toolbar 
		_infoBar = new JPanel();
		_infoBar.add(_timerLabel);
		_infoBar.add(_movesCounterLabel);
		_infoBar.setBackground(bgd);
		
		//initialize board
		_board = new JPanel();
		_board.setBackground(new Color(0,199,254));
		_dimension = _boardDS.getDimension();
		_figureSize = _puzzleImage.getWidth() / _dimension; //size of each button
		_figureDimension = new Dimension(_figureSize, _figureSize);
		_emptyFigure = new JLabel();
		_emptyFigure.setPreferredSize(_figureDimension);
		_emptyFigure.setOpaque(true);
		_emptyFigure.setBackground(new Color(0,199,254));
		
		int boardSize = _figureSize * _dimension;
		_n = _boardDS.getTotalFigures();
		setSize(boardSize + 15, boardSize + 130);
		_boardFigures = new ArrayList<Figure>();
		_board.setLayout(new GridLayout(_dimension, _dimension, 1, 1));
		initFigures();
		_boardDS.applyBoard(_boardFigures);

		
		// add all components to window
		add(_controlsBar, BorderLayout.NORTH);
		add(_board, BorderLayout.CENTER);
		add(_infoBar, BorderLayout.SOUTH);

	}
	/**
	 * Initiating the board data structure in order to create from it the board itself
	 *
	 *
	 */
	private void initFigures()
	{
		int x = 0, y = 0;
		for (int i = 0; i < _n - 1; i++) {
			ImageIcon imgToAdd = new ImageIcon(_puzzleImage.getSubimage(x, y, _figureSize, _figureSize));
			Figure figToAdd = new Figure(i + 1, imgToAdd);
			figToAdd.addActionListener(this);
			_boardFigures.add(figToAdd);
			if ((i + 1) % _dimension == 0) {
				x = 0;
				y += _figureSize;
			} else {
				x += _figureSize;
			}
		}
		_boardFigures.add(null);
	}

	/**
	 * updates the shown board on window through the board's methods
	 */
	private void updater() 
	{
		
		_board.removeAll();
		updateBoard();
		updateMoves();
		if (_boardDS.checkAnswer())
			finishGame();
	}
	/**
	 * Updating the board after each move by user
	 */
	private void updateBoard() {
		for (int i = 0; i < _n; i++) {
			int currPos = _boardDS.get(i);
			if (currPos != 0) {
				Figure tmp = _boardFigures.get(currPos - 1);
				_board.add(tmp);
			}
			else {
				_board.add(_emptyFigure);
			}
		}
		_isFinished = _boardDS.checkAnswer();
	}
	/**
	 * pushes a new board to the stack after a move is done and updates the moves counter
	 */
	private void updateMoves() 
	{
		_movesCounter++;
		_movesCounterLabel.setText("Total moves: "+_movesCounter);	
	}

	/**
	 * moving figure on the board if the move is legal
	 * @param movingFigure
	 * @return
	 */
	private void move(Figure movingFigure) 
	{
		int toChange = movingFigure.getCurrentIndex() - 1;
		int zero = _boardDS.findZero();
		try {
			if (zero < _n - _dimension && zero + _dimension == toChange) { // if up is empty
				_boardDS.switchFig(toChange - _dimension, toChange);
				movingFigure.setCurrentIndex(toChange - _dimension + 1);
				updater();
			}
		} catch (ArrayIndexOutOfBoundsException e) {

		}
		try {
			if (!(zero % _dimension == 0) && zero - 1 == toChange) { // if right is empty
				_boardDS.switchFig(toChange + 1, toChange);
				movingFigure.setCurrentIndex(toChange + 2);
				updater();

			}
		} catch (ArrayIndexOutOfBoundsException e) {

		}
		try {

			if (!(zero % _dimension == _dimension -1) && zero + 1 == toChange) { // if left is empty
				_boardDS.switchFig(toChange - 1, toChange);
				movingFigure.setCurrentIndex(toChange);
				updater();

			}
		} catch (ArrayIndexOutOfBoundsException e) {

		}
		try {
			if (zero >= _dimension && zero - _dimension == toChange) { // if down is empty
				_boardDS.switchFig(toChange + _dimension, toChange);
				movingFigure.setCurrentIndex(toChange + _dimension + 1);
				updater();
			}
		} catch (ArrayIndexOutOfBoundsException e) {}
	}
	/**
	 * moving figure using the keyboard keys pressed by user
	 *
	 * @param  moving,  x
	 */
	private void moveByKey(int moving, int x) {
		moving = moving + x;
		if (moving >= 0 && moving < _n)
		{
			int movingIndex = _boardDS.get(moving) -1;
			Figure movingFigure = _boardFigures.get(movingIndex);
			move(movingFigure);
		}
	}
	
	/**
	 * finishes the game whenever the user wins
	 */
	private void finishGame()
	{
		_timer.stop();
		_isFinished = true;
		_isStopped = true;
		alert("finish");
		_stopStartButton.setIcon(_imagesPack[5]);
		_stopStartButton.setText("Play Again");	
	}

	/**
	 * closes the game and goes back to image choosing window
	 */
	private void backToChooseImage() 
	{
		StartPuzzleWindow p = new StartPuzzleWindow(_filesHandler);
		p.setLocationRelativeTo(this);
		dispose();
	}
	/**
	 * closes the game and goes back to main window
	 */
	private void backToMenu() 
	{
		MainWindow m = new MainWindow(_filesHandler);
		m.setLocationRelativeTo(this);
		dispose();		
	}
	/**
	 * undo last move
	 */
	private void undo()
	{
		if (_boardDS.undoMove(_boardFigures))
		{
			_board.removeAll();
			updateBoard();
		}
		else
			alert("cant undo");
	}
	/**
	 * changes the start and stop icons whenever it is clicked, also stops the timer if the game is paused
	 */
	private void PauseStartButton()
	{
		_isStopped = !_isStopped;
		if (!_isStopped)
		{
			_stopStartButton.setIcon(_imagesPack[0]);
			_stopStartButton.setText("Stop");
			_timer.start();
		}
		else
		{
			_stopStartButton.setIcon(_imagesPack[1]);
			_stopStartButton.setText("Start");
			_timer.stop();
		}
	}
	/**
	 * if the game is over and the user wants to play again, this method recreates the puzzle.
	 */
	private void playAgain() 
	{
		_boardDS.clearStack();
		_boardDS.playAgain(_boardFigures);
		_movesCounter = -1;
		updater();

		_isFinished = false;
		_isStopped = false;
		_stopStartButton.setIcon(_imagesPack[0]);
		_stopStartButton.setText("Stop");
		resetTimer();
		_timer.start();
	}

	// --- TIMER ---
	/**
	 * updates the timer every second
	 */
	private void updateTimerLabel() 
	{
		_seconds++;
		if (_seconds == 60)
		{
			_seconds = 0;
			_minutes++;
		}
		if (_minutes == 60)
		{
			_seconds = 0; _minutes = 0;
			_hours++;
		}		

		_timerLabel.setText("Total Time: " + String.format("%02d", _hours) + ":" +
				String.format("%02d", _minutes) + ":" + String.format("%02d", _seconds));
	}
	/**
	 * resets the timer
	 */
	private void resetTimer()
	{
		_seconds = 0;
		_minutes = 0;
		_hours = 0;
		updateTimerLabel();
	}

	/**
	 * handles the alerts shows to user 
	 * @param alert is string which represent an alert type
	 */
	private void alert(String alert) 
	{
		switch (alert)
		{
		case ("paused"):
		{
			JOptionPane.showMessageDialog(null, "Can not make moves while game is paused,"
					+ '\n' +  "please press play first and then make moves ", 
					"Game is paused", JOptionPane.CANCEL_OPTION);
			break;
		}
		case ("finish"):
		{
			BufferedImage iconB = _filesHandler.resizeFinish(_puzzleImage);
			ImageIcon icon = new ImageIcon(iconB);
			JOptionPane.showMessageDialog(null, "GOOD JOB!" + '\n' 
					+ "Total moves :" + _movesCounter + '\n'
					+ _timerLabel.getText(), "Game is over", JOptionPane.INFORMATION_MESSAGE,
                    icon);
			break;
		}
		case ("play again"):
		{
			JOptionPane.showMessageDialog(null, "The game is finished,"
					+ '\n' +  "please choose one of the buttons above.", "Game is over", JOptionPane.CANCEL_OPTION);				
			break;
		}
		case ("cant undo"):
		{
			JOptionPane.showMessageDialog(null, "Cannot undo,"
					+ '\n' +  "You have reached maximum undo phases", "No more undos", JOptionPane.CANCEL_OPTION);
		}
		}
	}

	// --- OVERRIDES ---	
	@Override
	public void actionPerformed(ActionEvent e)
	{

		if (e.getSource() == _menuButton)
		{
			backToMenu();
		}
		else if (e.getSource() == _changeImageButton)
		{
			backToChooseImage();
		}
		else if (_isFinished)
		{
			if (e.getSource() == _stopStartButton)
			{
				playAgain();
			}
			else
				alert("play again");
		}		
		else if (_isStopped)
		{
			if (e.getSource() == _stopStartButton)
				PauseStartButton();
			else
				alert("paused");
		}
		else if (e.getSource() == _hintButton)
		{
			_popupTimer.start();
			_hintPopup.setLocationRelativeTo(this);
			setVisible(false);
			_hintPopup.setVisible(true);
		}
		else if (e.getSource() == _popupTimer)
		{
			_popupTimer.stop();
			_hintPopup.setVisible(false);
			setVisible(true);
		}
		else if ( e.getSource() instanceof Figure)
		{
			move((Figure)e.getSource());
			
		}
		else if (e.getSource() == _stopStartButton)
		{
			PauseStartButton();
		}
		else if (e.getSource() == _timer)
		{
			updateTimerLabel();
		}
		else if (e.getSource() == _undoButton)
		{
			undo();
		}
		requestFocusInWindow();
	}
		@Override
	public void keyPressed(KeyEvent e)
	{
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_SPACE)
		{
			if (_isFinished)
				playAgain();
			else
				PauseStartButton();
		}
		else if (_isFinished)
		{
			alert("play again");
		}
		else if (_isStopped)
		{
			alert("paused");
		}

		else {
			int x = _boardDS.findZero();
			switch( keyCode )
			{
			case KeyEvent.VK_UP:			//move up
			{
				if (x <_n- _dimension) 
					moveByKey(_dimension,x);
			}
			break;
			case KeyEvent.VK_DOWN:			//move down
			{
				if (x >= _dimension) 
					moveByKey(-1 * _dimension,x);
			}
			break;
			case KeyEvent.VK_LEFT:			//move left
			{
				if (!(x % _dimension == _dimension-1))
					moveByKey(1,x);

			}
			break;
			case KeyEvent.VK_RIGHT :		//move right
			{
				if (!(x % _dimension == 0))
					moveByKey(-1,x);
			}
			break;
			case KeyEvent.VK_Z:				//undo
			{
				if ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)   //ctrl+z
					undo();
			}
			}
		}
	}
	@Override
	public void keyReleased(KeyEvent arg0) {}
	@Override
	public void keyTyped(KeyEvent arg0) {}

}
