package Game;
import IHandler.FilesHandler;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * Graphical menu for the user to choose its game preferences
 */
public class StartPuzzleWindow extends JFrame implements ActionListener
{
	private JButton _backButton,_openButton,_choose,_random;
	private JTextField _nxn;
	private ImageIcon[] _iconsPack;
	private int _puzzleSize = -1;
	private JLabel _backLabel,_sizeLabel,_openLabel,_chooseLabel,_randomLabel;
	private ImagePanel _panel;
	private ChooseWindow _chooseWindow;
	private JCheckBox _csvCheckBox;
	private FilesHandler _filesHandler;
	private boolean _csvLoaded;
	private final Insets _INSETS = new Insets(5, 5, 5,5);
	private final Dimension BUTTON_DIMENSION = new Dimension(90, 60);

    /**
     * Constructor
     * @param filesHandler
     */
	public StartPuzzleWindow(FilesHandler filesHandler) {

		super("Sliding Puzzle");
		_filesHandler = filesHandler;
		_csvLoaded = _filesHandler.getCSVStatus();
		setSize(600	,400);		
		initiateWindow();
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		_chooseWindow = new ChooseWindow(_filesHandler);
		_chooseWindow.setLocationRelativeTo(this);

		setResizable(false);
		setVisible(true);
		requestFocusInWindow();
	}

    /**
     * setting up Java Swing preferences
     */
	private void initiateWindow() 
	{
		if (_filesHandler.getIcon() != null)
			setIconImage(_filesHandler.getIcon());
		_iconsPack = _filesHandler.getStartPuzzlePack();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = _INSETS;
		_panel = new ImagePanel("Images/Background.jpg");
		Color color = new Color(248,244,233);
		// ------ Labels ------
		_sizeLabel = new JLabel();
		_sizeLabel.setText("Choose puzzle size");
		_sizeLabel.setFont(new Font ("Arial",Font.BOLD, 20));
		_sizeLabel.setOpaque(true);
		_sizeLabel.setBackground(new Color(1,196,252,70));
		gbc.gridx = 0;
		gbc.gridy = 0;
		_panel.add(_sizeLabel, gbc);

		_chooseLabel = new JLabel();
		_chooseLabel.setText("Choose a picture");
		_chooseLabel.setFont(new Font ("Arial",Font.BOLD, 20));
		_chooseLabel.setOpaque(true);
		_chooseLabel.setBackground(new Color(1,196,252,70));
		gbc.gridx = 0;
		gbc.gridy = 1;
		_panel.add(_chooseLabel, gbc);

		_openLabel = new JLabel();
		_openLabel.setText("Load a picture");
		_openLabel.setFont(new Font ("Arial",Font.BOLD, 20));
		_openLabel.setOpaque(true);
		_openLabel.setBackground(new Color(1,196,252,70));
		gbc.gridx = 0;
		gbc.gridy = 2;
		_panel.add(_openLabel, gbc);

		_randomLabel = new JLabel();
		_randomLabel.setText("Random game");
		_randomLabel.setFont(new Font ("Arial",Font.BOLD, 20));
		_randomLabel.setOpaque(true);
		_randomLabel.setBackground(new Color(1,196,252,70));
		gbc.gridx = 0;
		gbc.gridy = 3;
		_panel.add(_randomLabel, gbc);

		_backLabel = new JLabel();
		_backLabel.setText("Back to main menu");
		_backLabel.setFont(new Font ("Arial",Font.BOLD, 20));
		_backLabel.setOpaque(true);
		_backLabel.setBackground(new Color(1,196,252,70));
		gbc.gridx = 0;
		gbc.gridy = 4;
		_panel.add(_backLabel, gbc);

		// ------ Buttons ------
		_nxn = new JTextField("Enter size");
		_nxn.setSize(BUTTON_DIMENSION);
		_nxn.addActionListener(this);
		//mouse listener
		_nxn.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e)
			{
				_nxn.setText("");
			}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}

		});
		gbc.gridx = 1;
		gbc.gridy = 0;
		_panel.add(_nxn, gbc);

		_choose = new JButton();
		_choose.setName("Choose");
		_choose.setIcon(_iconsPack[0]);
		_choose.addActionListener(this);
		_choose.setPreferredSize(BUTTON_DIMENSION);
		_choose.setBackground(color);
		gbc.gridx = 1;
		gbc.gridy = 1;
		_panel.add(_choose,gbc);

		_openButton = new JButton();
		_openButton.setName("Open");
		_openButton.setIcon(_iconsPack[2]);
		_openButton.addActionListener(this);
		_openButton.setPreferredSize(BUTTON_DIMENSION);
		_openButton.setBackground(color);
		gbc.gridx = 1;
		gbc.gridy = 2;
		_panel.add(_openButton, gbc);

		_random = new JButton();
		_random.setName("Random");
		_random.setIcon(_iconsPack[1]);
		_random.addActionListener(this);
		_random.setPreferredSize(BUTTON_DIMENSION);
		_random.setBackground(color);
		gbc.gridx = 1;
		gbc.gridy = 3;
		_panel.add(_random,gbc);

		_backButton = new JButton();
		_backButton.setName("Back");
		_backButton.setIcon(_iconsPack[3]);
		_backButton.addActionListener(this);
		_backButton.setPreferredSize(BUTTON_DIMENSION);
		_backButton.setBackground(color);
		gbc.gridx = 1;
		gbc.gridy = 4;
		_panel.add(_backButton,gbc);

		// csv checkbox
		if (_csvLoaded)
		{
			_csvCheckBox = new JCheckBox();
			_csvCheckBox.setIcon(_iconsPack[4]);
			_csvCheckBox.setSelectedIcon(_iconsPack[5]);
			_csvCheckBox.setDisabledIcon(_iconsPack[4]);
			gbc.gridx = 2;
			gbc.gridy = 1;
			_panel.add(_csvCheckBox,gbc);
		}

		add(_panel);			//add JPanel to window
	}

	//============= logics
	/**
	 * creates an integer of given user's input
	 * @return
	 */
	private boolean parseTextField(boolean useAlerts) {
		try {
			String input = _nxn.getText();
			_puzzleSize = Integer.parseInt(input);
			if (_puzzleSize > 1 )
				return true;
			else if (_csvLoaded && _csvCheckBox.isSelected())
				return true;
		}
		catch (NumberFormatException e) {}
		if ((!_csvLoaded && useAlerts) || (!_csvCheckBox.isSelected() && useAlerts)) {
            alert("invalid size");
        }
		else if (_csvCheckBox.isSelected() && useAlerts) {
            alert("invalid csv size");
        }
		return false;
	}

	/**
	 * handles the alerts shown to user 
	 * @param alert is string which represent an alert type
	 */
	private void alert(String alert) 
	{
		switch (alert)
		{
		case "invalid size":
		{
			JOptionPane.showMessageDialog(null, "Please choose a valid puzzle size and then try again."
					+ '\n' + "A valid size is an integer bigger than 1.", 
					"Size is not chosen", JOptionPane.CANCEL_OPTION);
			break;
		}
		case "none selected": {
            JOptionPane.showMessageDialog(null, "Could not start game"
                            + '\n' + "Please type a correct board size or use CSV" + '\n'
                            + "You can play a random game using CSV by clicking the CSV button (turns into green) and click the random button.",
                    "No board selected", JOptionPane.CANCEL_OPTION);
            break;
        }
            case "invalid csv size":{
            	String sizes = _filesHandler.getBoardSizes();
                JOptionPane.showMessageDialog(null, "Your csv board size choice is invalid, "
                		+ '\n' + "Please choose a csv board size from our default sizes: " +sizes,
                        "No board selected", JOptionPane.CANCEL_OPTION);
            }
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		JButton button = new JButton();
		if (e.getSource() instanceof JButton)
			button = (JButton)e.getSource();			
		switch (button.getName())
		{
		case "Back":					//back to main menu
		{
			new MainWindow(_filesHandler);
			dispose();
			break;
		}
		case "Choose":					//choose from game's library
		{
			parseChooseOption();
			break;
		}
		case "Random":					//play a random game
		{
			parseRandom();
			break;
		}
		case "Open":					//load a picture
		{
			parseOpen();
			break;

		}
		}
	}

    /**
     * handles the case in which the user pressed open
     */
	private void parseOpen() {
		boolean useCSV;
		if (_csvLoaded && (useCSV = _csvCheckBox.isSelected()))
		{
			if (!parseTextField(true))
				return;
			if (isValidCsvSize(true) && _filesHandler.loadPuzzleImage())
			{
				_chooseWindow.start(_puzzleSize,true);
				setVisible(false);
			}
		}
		else if (parseTextField(true) && _filesHandler.loadPuzzleImage())
		{
			_chooseWindow.start(_puzzleSize,false);
			setVisible(false);	
		}
	}

    /**
     * handles the case in which the user pressed Random
     */
	private void parseRandom() {
		if (_csvLoaded)
		{	
			boolean csvSelected = _csvCheckBox.isSelected();
			if (csvSelected) //random game using random csv
				_chooseWindow.randomGame(true);
			else if (!csvSelected)							//random game using size and no csv
				_chooseWindow.randomGame(false);
		}
		else 
			_chooseWindow.randomGame(false);
		dispose();
	}

    /**
     * handles the case in which the user pressed choose
     */
	private void parseChooseOption() 
	{
		if (_csvLoaded) 
		{
			if (!parseTextField(true))
				return;
			boolean useCSV = _csvCheckBox.isSelected();
			if (useCSV && isValidCsvSize(false))		//csv and size has chosen
			{
				_chooseWindow.openWindow(_puzzleSize,true,this);
				setVisible(false);
			}
			else if (!useCSV) 					//csv has not been chosen
			{
				_chooseWindow.openWindow(_puzzleSize,false,this);
				setVisible(false);
			}
			else if(useCSV && !isValidCsvSize(false)){
			    alert("invalid csv size");
            }
			return;
		}
		if (parseTextField(true))	//csv not loaded and size chosen
		{
			_chooseWindow.openWindow(_puzzleSize,false,this);
			setVisible(false);
		}
	}

    /**
     * return if a board size is valid
     * @return
     */
	private boolean isValidCsvSize(boolean alert) {
		boolean ans = _filesHandler.legalCsvSize(_puzzleSize);
		if (alert && !ans)
			alert("invalid csv size");
		return ans;
	}


}

