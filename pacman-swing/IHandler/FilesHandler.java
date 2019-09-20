package IHandler;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * file handler is an object handling all file related missions such as reading etc
 */
public class FilesHandler {

	private Image _icon;
	private boolean _csvFound,_csvLoaded;
	private ImageIcon[] _startPuzzleImageIconPack, _mainImagePack, _puzzleWindowPack,_chooseWindowIconPack;
	private BufferedImage _puzzleImage,_sushiImage,_catImage,_cyberImage;
	private ArrayList<int[]> _boardsHolder3,_boardsHolder4,_boardsHolder5;
	private ArrayList<int[]>[] _boardSizes;
	private ImageLoader _imageLoader;
	private ImageResizer _imageResizer;
	private boolean _start,_main,_choose,_puzzle,_ico, _given;
	private ArrayList<Integer> _csvBoardsPossibleSizes;

    /**
     * Constructor setting up the basic preferences of the File Handler
     */
	public FilesHandler() {
		_start = true;
		_given = true;
		_main = true;
		_choose = true;
		_puzzle = true; 
		_ico = true;;
		_icon = null;
		_imageResizer = new ImageResizer();
		_imageLoader = new ImageLoader();
		_csvLoaded = false;
		loadImages();
		loadCSV();
	}

    /**
     * initiating the process of reading the csv file from data storage
     * uses get getcsvBoards, csvToString
     */
	private void loadCSV() {
		_boardSizes = (ArrayList<int[]>[]) new ArrayList[3];
		_boardsHolder3 = new ArrayList<>();
		_boardsHolder4 = new ArrayList<>();
		_boardsHolder5 = new ArrayList<>();
		_boardSizes[0] = _boardsHolder3;
		_boardSizes[1] = _boardsHolder4;
		_boardSizes[2] = _boardsHolder5;
		_csvBoardsPossibleSizes = new ArrayList<>();
		getCsvBoards();
		if(_csvLoaded && (_boardsHolder3 != null || _boardsHolder4 != null || _boardsHolder5 != null)) {
            _csvBoardsPossibleSizes.sort(Integer::compareTo);
        }
	}

    /**
     * creating a DS that stores the csv file as puzzle boards
     */
	private void getCsvBoards() {

		String tCsv = csvToString();
		if (tCsv.isEmpty())
		{
			_csvLoaded = false;	
			if (!_csvFound)
				return;
			_csvFound = false;
			alert("empty csv");
			return;
		}
		_csvLoaded = true;		
		String[] tCsvBoards = tCsv.split("\\r?\\n");
		int i = 0;
		while (i < tCsvBoards.length) { //Cycling all String from csv
			if (tCsvBoards[i].length() == 1) { //if its board size , backup check
				int tJump = Integer.parseInt(tCsvBoards[i]); //get size
                if(!_csvBoardsPossibleSizes.contains(tJump)) {
                    _csvBoardsPossibleSizes.add(tJump);
                }
				int[] tPositions = new int[tJump * tJump];
				int k = 0;
				for (int j = i + 1; j <= (i + tJump); j++) { //Cycling Rows of current board
				    int counter = 0;
					String[] tRow = tCsvBoards[j].split(",");
					while (counter < tRow.length) { // Cycling each row
						tPositions[k] = Integer.parseInt(tRow[counter]);
						counter++;
						k++;
					}
				}
				_boardSizes[tJump - 3].add(tPositions);
				i += (tJump + 1);
			}
		}
	}

    /**
     * transferring the csv file to string in order to create boards from it
     * @return
     */
	private String csvToString() {
		try {
			Scanner reader;
 			File tBoardCSV = new File("boards.csv");

			reader = new Scanner(tBoardCSV);
			_csvFound = true;
//			reader.useDelimiter(",");
			String tNewBoard = "";
			tNewBoard = reader.nextLine();
			if (tNewBoard.isEmpty())
				return tNewBoard;
			while (reader.hasNext()) {
				tNewBoard += '\n';
				tNewBoard += reader.nextLine();
			}
			reader.close();

			return tNewBoard;
		} catch (Exception e) {
			_csvFound = false;
			alert("not found csv");

		}
		return "";
	}

    /**
     * loading images to relevant places
     */
	private void loadImages() {
		try {
			loadStartPuzzleImages();
			loadMainWindowImages();
			loadPuzzleWindowImages();
			loadChooseWindowImages();
			loadGivenImages();
			_icon = ImageIO.read(new File("Images/icon.png"));
		} catch (IOException e1) {
			System.out.println("error: could not find icon");
			_ico = false;
		}

	}

	private void loadStartPuzzleImages()
	{
		_startPuzzleImageIconPack = new ImageIcon [6];
		_startPuzzleImageIconPack[0] = new ImageIcon ("Images/StartPuzzle/changeImageIcon.png");
		_startPuzzleImageIconPack[1] = new ImageIcon("Images/StartPuzzle/randomIcon.png");
		_startPuzzleImageIconPack[2] = new ImageIcon("Images/StartPuzzle/openIcon.png");
		_startPuzzleImageIconPack[3] = new ImageIcon ("Images/StartPuzzle/backIcon.png");
		_startPuzzleImageIconPack[4] = new ImageIcon ("Images/StartPuzzle/csvOff.png");
		_startPuzzleImageIconPack[5] = new ImageIcon ("Images/StartPuzzle/csvOn.png");
		String[] files = {"changeImageIcon.png","randomIcon.png","openIcon.png","backIcon.png","csvOff.png","csvOn.png"};
		for (int i=0; i<6 ; i++)
		{
			if( _startPuzzleImageIconPack[i].getIconHeight() == -1)
			{
				_start = false;
				System.out.println("error: could not find " + files[i]);
			}
			
		}
	}

	private void loadMainWindowImages()
	{
		_mainImagePack = new ImageIcon[2];
		_mainImagePack[0] = new ImageIcon("Images/Main/playMainIcon.png");
		_mainImagePack[1] = new ImageIcon("Images/Main/exitIcon.png");	
		String[] files = {"playMainIcon.png","exitIcon.png"};
		for (int i=0; i<2 ; i++)
		{
			if( _mainImagePack[i].getIconHeight() == -1)
			{
				_main = false;
				System.out.println("error: could not find " + files[i]);
			}
			
		}
	}

	private void loadPuzzleWindowImages()
	{
		_puzzleWindowPack = new ImageIcon[9];
		_puzzleWindowPack[0] = new ImageIcon("Images/PuzzleWindow/stopIcon.png"); 
		_puzzleWindowPack[1] = new ImageIcon("Images/PuzzleWindow/startIcon.png"); 
		_puzzleWindowPack[2] = new ImageIcon("Images/PuzzleWindow/undoIcon.png"); 
		_puzzleWindowPack[3] = new ImageIcon("Images/PuzzleWindow/changeImageIcon.png"); 
		_puzzleWindowPack[4] = new ImageIcon("Images/PuzzleWindow/menuIcon.png"); 
		_puzzleWindowPack[5] = new ImageIcon ("Images/PuzzleWindow/playAgainIcon.png");
		_puzzleWindowPack[6] = new ImageIcon ("Images/PuzzleWindow/movesIcon.png");
		_puzzleWindowPack[7] = new ImageIcon ("Images/PuzzleWindow/timerIcon.png");
		_puzzleWindowPack[8] = new ImageIcon ("Images/PuzzleWindow/hintIcon.png");
		String[] files = {"stopIcon.png","startIcon.png","undoIcon.png","changeImageIcon.png","menuIcon.png"
				,"playAgainIcon.png","movesIcon.png","timerIcon.png","hintIcon.png"};
		for (int i=0; i<9 ; i++)
		{
			if( _puzzleWindowPack[i].getIconHeight() == -1)
			{
				System.out.println("error: could not find " + files[i]);
				_puzzle = false;
			}
			
		}
	}

	private void loadChooseWindowImages()
	{
		_chooseWindowIconPack = new ImageIcon[4];
		_chooseWindowIconPack[0] = new ImageIcon ("Images/ChooseWindow/cat.jpeg");
		_chooseWindowIconPack[1] = new ImageIcon ("Images/ChooseWindow/sushi.jpg");
		_chooseWindowIconPack[2] = new ImageIcon ("Images/ChooseWindow/cyber.jpeg");
		_chooseWindowIconPack[3] = new ImageIcon ("Images/ChooseWindow/backIcon.png");
		String[] files = {"cat.jpeg","sushi.jpg","cyber.jpeg","backIcon.png"};
		for (int i=0; i<4 ; i++)
		{
			if( _chooseWindowIconPack[i].getIconHeight() == -1)
			{
				_choose = false;
				System.out.println("error: could not find " + files[i]);
			}
		}
	}

	private void loadGivenImages() {
		try {
			_sushiImage = ImageIO.read(new File("Images/ChooseWindow/sushi.jpg"));
			_catImage =  ImageIO.read(new File("Images/ChooseWindow/cat.jpeg"));
			_cyberImage = ImageIO.read(new File("Images/ChooseWindow/cyber.jpeg"));
			//resize
			_sushiImage = resizePictures(_sushiImage);
			_catImage = resizePictures(_catImage);
			_cyberImage = resizePictures(_cyberImage);
		} catch(IOException e) {
			_given = false;
			System.out.println("error: one of the files missing: sushi.jpg, cat.jpeg, cyber.jpeg");
		}

	}	

	public boolean loadPuzzleImage()
	{
		BufferedImage bf = _imageLoader.loadImage();
		if (bf != null)
		{
			_puzzleImage = _imageResizer.resizeImage(bf, 700, 700);
			return true;
		}
		return false;
	}

    /**
     * resizing the images to fit to the the game
     * @param pic
     * @return
     */
	public BufferedImage resizePictures(BufferedImage pic)
	{
		return _imageResizer.resizeImage(pic, 700, 700);
	}

	public BufferedImage resizeFinish(BufferedImage pic) 
	{
		return _imageResizer.resizeImage(pic, 250, 250);
	}

    /**
     * warnings presenting method
     * @param alert
     */
	private void alert(String alert)
	{
		switch (alert)
		{
		case "empty csv":
		{
			JOptionPane.showMessageDialog(null,"error: empty csv file, please insert a legal file and try again."
					+ '\n' + "You can still play, you will get the options of a none csv file game.", 
					"EMPTY CSV FILE", JOptionPane.CANCEL_OPTION);
			break;
		}
		case "not found csv":
		{
			if (getStatus()) 
			{
				JOptionPane.showMessageDialog(null,"error: csv fild not found."
						+ '\n' + "You can still play, you will get the options of a none csv file game.", 
						"NO CSV FILE", JOptionPane.CANCEL_OPTION);
			}
			else
			{
				JOptionPane.showMessageDialog(null,"error: csv fild not found.", 
						"NO CSV FILE", JOptionPane.CANCEL_OPTION);
			}

			break;
		}
		case "not found size":
			JOptionPane.showMessageDialog(null,"error: size request was not found on csv file", 
					"SIZE NOT FOUND", JOptionPane.CANCEL_OPTION);
			break;
		}
	}

    /**
     * setting up the puzzle image by choice or randomly
     * @param pic
     */
	public void setPuzzleImage(String pic)
	{
		switch (pic)
		{
		case "Cyber":
		{
			_puzzleImage = _cyberImage;
			break;
		}
		case "Cat":
		{
			_puzzleImage = _catImage;
			break;
		}
		case "Sushi":
		{
			_puzzleImage = _sushiImage;
			break;
		}
		}
	}

	public void setPuzzleImage(int random)
	{
		switch (random)
		{
		case 0:
		{
			_puzzleImage = _cyberImage;
			break;
		}
		case 1:
		{
			_puzzleImage = _catImage;
			break;
		}
		case 2:
		{
			_puzzleImage = _sushiImage;
			break;
		}
		}
	}

    /**
     * getters
     * @return
     */
	public ImageIcon[] getStartPuzzlePack()
	{
		return _startPuzzleImageIconPack;
	}

	public ImageIcon[] getMainPack()
	{
		return _mainImagePack;
	}

	public ImageIcon[] getPuzzlePack()
	{
		return _puzzleWindowPack;
	}

	public ImageIcon[] getChooseIconsPack()
	{
		return _chooseWindowIconPack;
	}

	public boolean getCSVStatus()
	{
		return _csvLoaded;
	}

	public Image getIcon() {
		return _icon;
	}

	public int[] getBoardFromCSV(int size)
	{
		Random rnd = new Random();
		int index = rnd.nextInt(_boardSizes[size - 3].size());
		return _boardSizes[size - 3].get(index);
	}

	public BufferedImage getPuzzleImage()
	{
		return _puzzleImage;
	}

    /**
     * checks if the chosen csv size is legal bu checking if it exists in the legal sizes DS
     * @param pSize
     * @return
     */
	public boolean legalCsvSize(int pSize) {
		return _csvBoardsPossibleSizes.contains(pSize);
	}

	public String getBoardSizes() {
		int size = _csvBoardsPossibleSizes.size();
		String ans = "{";
		for (int i=0;i<size; i++)
		{
			String s = _csvBoardsPossibleSizes.get(i).toString();
			ans += s;
			if (i != size-1)
				ans+= ",";
		}
		return ans + "}";
	}

	public boolean getStatus() {
		if (!_given || !_start || !_main || !_choose || !_puzzle || !_ico)
			return false;
		return true;
	}

}
