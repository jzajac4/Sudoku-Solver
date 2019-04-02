/*
 * John Zajac: jzajac4
 * Nicholas Abbasi: nabbasi3
 * Liam Edelman: ledelma2
 * 
 * Project 3 - Sudoku Solver 
 * 
 * October 30th, 2017
 * 
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.swing.*;

public class Board {
	public final int boardSide = 9; //reusable int for length of each side of the board in cells
	final List<String> numbers = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9"); //used to init player choice buttons
	MyButton [][] buttons = new MyButton [9][9]; //main 81 game buttons
	JFrame frame;
	JPanel panel1;
	JPanel panel2;
	JPanel panel3;
	JPanel panel4;
	JLabel candidates; //for displaying the candidates list
	JLabel userChoice; //for displaying the player's choice
	
	String initUserChoice = "User is currently using: "; //used for userChoice JLabel
	String currentUserChoice = ""; // added onto initUserChoice when initUserChoices buttons are used
	
	boolean Allow_user_to_make_mistakes = true; //for selected thinggy under hints, if false, 
												//users can only enter c_list members
	
	public Board () 
	{
		// frame init
		frame = new JFrame ("Sudoku");
		
		
		// sudoku panel
		panel1 = new JPanel();
		panel1.setLayout(new GridLayout(boardSide, boardSide));
		initBoard(panel1); //makes all the playing buttons
		loadBoard(buttons); //enters preset buttons onto board
		frame.add(panel1, BorderLayout.CENTER);
		
		// user choice panel
		panel2 = new JPanel();
		initUserChoices (panel2, numbers); //sets up user choice buttons on the right side
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
		panel2.setBorder(BorderFactory.createLineBorder(Color.black));
		frame.add(panel2, BorderLayout.EAST);
		
		// candidate panel
		panel3 = new JPanel();
		candidates = new JLabel("Candidate list: ");
		panel3.add(candidates); //for candidates list
		frame.add(panel3,  BorderLayout.SOUTH);
		
		panel4 = new JPanel();
		userChoice = new JLabel(initUserChoice);
		panel4.add(userChoice); //for user's choices
		frame.add(panel4,  BorderLayout.NORTH);
		
		// menubar init
		JMenuBar menubar = new JMenuBar();
		addMenuBar(frame, menubar);
		
		// file menu init
		JMenu fileMenu = new JMenu("Menu");
		addMenu(menubar, fileMenu); //adds menu options onto the menu bar
		initFileMenuItems(fileMenu, buttons);
		
		// help menu init
		JMenu helpMenu = new JMenu("Help");
		addMenu(menubar, helpMenu); //adds help options onto the menu bar
		initHelpMenuItems(helpMenu);
		
		// hints menu init
		JMenu hintsMenu = new JMenu("Hints");
		addMenu(menubar, hintsMenu); //adds hint options onto the menu bar
		initHintsMenuItems(hintsMenu);
		
		initC_list(); //generates a candidate list for all blank buttons post preset buttons being added
		
		initFrame(frame);
	}
	
	public void initFrame (JFrame frameX)
	{
		frameX.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameX.setSize(530,600);
		frameX.setVisible(true);
	}
	
	//initializes the buttons for the board, giving each lots of information like what box they are in,
	// what their x and y positions are and adding their actions. That happens in here.
	public void initBoard (JPanel panelX)
	{
		int count = 0;
		int boxSetter1 = 0;
		int boxSetter2 = 3;
		int boxSetter3 = 6;
		
		for (int i = 0; i < boardSide; i++)
		{
			for (int j = 0; j < boardSide; j++)
			{
				MyButton button = new MyButton();
				button.setPreferredSize(new Dimension(50, 50));
				buttons[i][j] = button;
				//BOX SETTER OPERATIONS
				//==============================================
				if(i < 3)
				{
					buttons[i][j].setBox(boxSetter1);
					count++;
					if(count/3 == 1)
					{
						boxSetter1++;
						count = 0;
					}
				}
				if(boxSetter1 == 3)
				{
					boxSetter1 = 0;
				}
				//----------------------------------------------
				if(i >= 3 && i < 6)
				{
					buttons[i][j].setBox(boxSetter2);
					count++;
					if(count/3 == 1)
					{
						boxSetter2++;
						count = 0;
					}
				}
				if(boxSetter2 == 6)
				{
					boxSetter2 = 3;
				}
				//----------------------------------------------
				if(i >= 6 && i < 9)
				{
					buttons[i][j].setBox(boxSetter3);
					count++;
					if(count/3 == 1)
					{
						boxSetter3++;
						count = 0;
					}
				}
				if(boxSetter3 == 9)
				{
					boxSetter3 = 6;
				}
				//==============================================
				buttons[i][j].setXPos(i);
				buttons[i][j].setYPos(j);
				buttons[i][j].addActionListener(new onBoardPressAction());
				panelX.add(buttons[i][j]);
			}
		}
	}
	
	
	///places preset buttons onto board to create the puzzle
	public void loadBoard (MyButton [][] buttons, String array)
	{
		for (int i = 0; i < array.length(); i+=3)
		{
			int xVar = Character.getNumericValue((array.charAt(i)))-1;
			int yVar = Character.getNumericValue((array.charAt(i+1)))-1;
			int valVar = Character.getNumericValue(array.charAt(i+2));
			
			buttons[xVar][yVar].setPreset(); //makes it not deletable
			buttons[xVar][yVar].setText( valVar + "" );
			
		}
	}
	
	
	//places preset buttons onto board to create the puzzle
	public void loadBoard (MyButton [][] buttons)
	{
		for (int i = 0; i < 9; i++)
		{
			for (int j = 0; j < 9; j++ ) 
			{
			buttons[i][j].setPreset(); //makes it not deletable
			buttons[i][j].setText( "" );
			}
			
		}
	}
	
	//saves current bord to a text file
	public String saveBoard (MyButton [][] buttons)
	{
		String savedfile = "";
		
		int xPos;
		int yPos;
		int ch = 0;
		
		for (int i = 0; i < 9; i++)
		{
			for (int j = 0; j < 9; j++ ) 
			{
				xPos = buttons[i][j].getXPos();
				yPos = buttons[i][j].getYPos();
				if (buttons[i][j].getText() != "")
				{
				ch = Integer.parseInt(buttons[i][j].getText());
				
				savedfile += xPos;
				savedfile += ' ';
				savedfile += yPos;
				savedfile += ' ';
				savedfile += ch;
				savedfile += '\n';
				}
			}
		}
		System.out.println("What will be saved: " + savedfile);
		
		return savedfile;
	}
	
	//Sets up the player choice buttons
	public void initUserChoices (JPanel panelX, List<String> numbers)
	{
		class userChoicesAction implements ActionListener {
			public void actionPerformed (ActionEvent e) 
			{
				
				currentUserChoice = ((JButton) e.getSource()).getText();
				userChoice.setText(initUserChoice + currentUserChoice);
			}
		}
		
		
		for (int j = 0; j < numbers.size(); j++)
		{
			JButton button = new JButton(numbers.get(j));
			button.addActionListener(new userChoicesAction());
			button.setPreferredSize(new Dimension(50, 50));
			panelX.setAlignmentX(Component.CENTER_ALIGNMENT);
			panelX.add(button);
		}
		JButton buttonX = new JButton("X");
		buttonX.addActionListener(new userChoicesAction());
		JButton buttonQuestion = new JButton("?");
		buttonQuestion.addActionListener(new userChoicesAction());
		buttonX.setPreferredSize(new Dimension(50, 50));
		buttonQuestion.setPreferredSize(new Dimension(50, 50));
		panelX.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelX.add(buttonX);
		panelX.add(buttonQuestion);
	}

	//adds menubar to frame
	public void addMenuBar (JFrame frameX, JMenuBar menubarX) {
		frameX.setJMenuBar(menubarX);
	}
	
	//adds menu object into menu
	public void addMenu (JMenuBar menubarX, JMenu menuX)
	{
		menubarX.add(menuX);
	}
	
	//adds and item onto a menu
	public void addMenuItem (JMenu menuX, JMenuItem itemX)
	{
		menuX.add(itemX);
	}
	
	//adds a checkbox item to menu object
	public void addCheckBoxMenuItem (JMenu menuX, JCheckBoxMenuItem buttonX)
	{
		menuX.add(buttonX);
	}
	
	//initialized lots of menu items and actions on them
	public void initFileMenuItems (JMenu menuX, MyButton [][] buttons)
	{
		JMenuItem loadPuzzle = new JMenuItem("Load puzzle from a file");
		class loadaction implements ActionListener {
			public void actionPerformed (ActionEvent e)
			{
				loadBoard(buttons);
				LoadFile chosenfile = new LoadFile(menuX);	
				String filename = chosenfile.getFile().getName().toString();
				
				String content = null;
				try {
					content = new Scanner(new File(filename)).useDelimiter("\\Z").next();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				content = content.replaceAll("\\s+","");
				loadBoard(buttons, content);
			}
		}
		loadPuzzle.addActionListener(new loadaction());
		
		JMenuItem savePuzzle = new JMenuItem("Save puzzle into a file");
		class saveaction implements ActionListener {
			public void actionPerformed (ActionEvent e)
			{
				String savedfile = saveBoard(buttons);
				
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Specify a file to save");   
				 
				int userSelection = fileChooser.showSaveDialog(menuX);
				 
				if (userSelection == JFileChooser.APPROVE_OPTION) {
					String filename = fileChooser.getSelectedFile().getName();
					if (!filename .endsWith(".txt"))
				        filename += ".txt";
					
					File file = new File(filename);
					
					try {
						FileWriter fileWriter = new FileWriter(file);
						fileWriter.write(savedfile);
						fileWriter.flush();
						fileWriter.close();
					}
					catch (IOException e1) {
						e1.printStackTrace();
					}
					
				    System.out.println("Save as file: " + filename);
				}
			}
		}
		savePuzzle.addActionListener(new saveaction());
		
		JMenuItem quitGame = new JMenuItem("Quit the game");
		class quitaction implements ActionListener {
			public void actionPerformed (ActionEvent e) {
				System.exit(0);
			}
		}
		quitGame.addActionListener(new quitaction());
		
		addMenuItem(menuX, loadPuzzle);
		addMenuItem(menuX, savePuzzle);
		addMenuItem(menuX, quitGame);
	}
	
	//action for loading
	class loadaction implements ActionListener {
			public void actionPerformed (ActionEvent e) {
				// FileChooser
				JFileChooser fc = new JFileChooser();

				int returnValue = fc.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
		          File selectedFile = fc.getSelectedFile();
		          JOptionPane.showMessageDialog(frame.getComponent(0), "You selected: " + selectedFile.getName());
		        }
			}
		}
	
	//initializes help menu items
	public void initHelpMenuItems (JMenu menuX)
	{
		// how to play sudoku
		JMenuItem how2Sudoku = new JMenuItem("How to play Sudoku");
		class how2playaction implements ActionListener {
			public void actionPerformed (ActionEvent e) {
				JOptionPane.showMessageDialog(frame.getComponent(0), 
						"The objective is to fill a 9x9 grid so that each column,\n"
						+ " each row, and each of the nine 3x3 boxes \n"
						+ "(also called blocks or regions) contains the \n"
						+ "digits from 1 to 9. ");
			}
		}
		how2Sudoku.addActionListener(new how2playaction());
		addMenuItem(menuX, how2Sudoku);
		
		JMenuItem how2Interface = new JMenuItem("How to use the Sudoku interface");
		class how2useaction implements ActionListener {
			public void actionPerformed (ActionEvent e) {
				JOptionPane.showMessageDialog(frame.getComponent(0), 
						"To use our interface, select any of the numbers \n"
						+ "on the side to fill in the empty boxes.\n"
						+ "The 'X' at the bottom is to clear any numbers \n"
						+ "you have inputted and want to get rid of.");
			}
		}
		how2Interface.addActionListener(new how2useaction());
		addMenuItem(menuX, how2Interface);
		
		JMenuItem aboutUs = new JMenuItem("About the creators");
		class aboutusaction implements ActionListener {
			public void actionPerformed (ActionEvent e) {
				JOptionPane.showMessageDialog(frame.getComponent(0), 
						"Created by: \n"
						+ "John Zajac: jzajac4\n"
						+ "Nicholas Abbasi: nabbasi3\n"
						+ "Liam Edelman: ledelma2");
			}
		}
		aboutUs.addActionListener(new aboutusaction());
		addMenuItem(menuX, aboutUs);
	}
	
	//initilizes hint menu items
	public void initHintsMenuItems (JMenu menuX)
	{
		JCheckBoxMenuItem toggleFill = new JCheckBoxMenuItem("Check on Fill");
		toggleFill.addActionListener(new checkOnFillAction());
		JMenuItem singleAlg = new JMenuItem("Single Algorithm");
		singleAlg.addActionListener(new singleAlgAction()); 
		JMenuItem hiddenSingleAlg = new JMenuItem("Hidden Single Algorithm");
		hiddenSingleAlg.addActionListener(new hiddenSingleAlgAction());
		JMenuItem lockedCandidateAlg = new JMenuItem("Locked Candidate Algorithm");
		JMenuItem nakedPairsAlg = new JMenuItem("Naked Pairs Algorithm");
		nakedPairsAlg.addActionListener(new nakedPairsAlgAction()); 
		addCheckBoxMenuItem(menuX, toggleFill);
		addMenuItem(menuX, singleAlg);
		addMenuItem(menuX, hiddenSingleAlg);
		addMenuItem(menuX, lockedCandidateAlg);
		addMenuItem(menuX, nakedPairsAlg);
	}
	
	//goes through all buttons and updates their candidate lists
	public void initC_list()
	{
		for (int a = 0; a < boardSide; a++)
		{
			for (int b = 0; b < boardSide; b++)
			{
				MyButton current_Button = buttons[a][b];
				int currentX = a;
				int currentY = b;
				int currentBox = buttons[currentX][currentY].getBox();
				
				//=================================================
				for (int i = 0; i < boardSide; i++)
				{
					
					//ROW CHECK
					if(buttons[currentX][i].getText() != "")
					{
						current_Button.c_list[(Integer.parseInt(buttons[currentX][i].getText()))-1] = false;
					}
					
					//COL CHECK
					if(buttons[i][currentY].getText() != "")
					{
						current_Button.c_list[(Integer.parseInt(buttons[i][currentY].getText()))-1] = false;
					}
					
					for (int j = 0; j < boardSide; j++)
					{
						//BOX CHECK
						if(buttons[i][j].getBox() == currentBox)
						{
							if(buttons[i][j].getText() != "")
							{
								//this means that there IS a value here so make sure to mark it off c_list
								current_Button.c_list[(Integer.parseInt(buttons[i][j].getText()))-1] = false;
								
							}
						}
					}
				}
				//=================================================
			}
		}
		if(is_all_filled() == true)
		{
			JOptionPane.showMessageDialog(frame.getComponent(0), 
					"Congratulations on solving the puzzle!");
		}
		
	}
	
	//action for when a button is pressed in game on 9x9 grid of buttons
	class onBoardPressAction implements ActionListener {
		public void actionPerformed (ActionEvent e) 
		{
			MyButton current_Button = ((MyButton) e.getSource());
			
			int currentX = current_Button.getXPos();
			int currentY= current_Button.getYPos();
			int currentBox = current_Button.getBox();
			int int_rep_of_currentUserChoice = 0;
			
			//make a int representation of player choice
			if(currentUserChoice!= "?" && currentUserChoice!= "X")
			{
				int_rep_of_currentUserChoice = Integer.parseInt(currentUserChoice);
			}
			
			//displays message to scold user and do no action
			if(!Allow_user_to_make_mistakes && currentUserChoice!= "?" && currentUserChoice!= "X")
			{
				for(int w = 0; w < 9; w++)
				{
					if(!current_Button.c_list[w])
					{
						if(int_rep_of_currentUserChoice == w+1)
						{
							JOptionPane.showMessageDialog(frame.getComponent(0), 
									"CHECK ON FILL ERROR:\nYou have tried to enter a number not on that\n" + 
									"list's candidate list. Current choice would be result in an incorrect\n" +
									"puzzle solution.");
							return;
						}
					}
				}
			}
			
			//place user choice into cell
			if(current_Button.getText() == "" && currentUserChoice!= "?")
			{
				current_Button.setText(currentUserChoice);
				initC_list();
			}
			
			//delete
			if(current_Button.getText() != "" && currentUserChoice == "X" && !(current_Button.getPreset()))
			{
				
				current_Button.setText("");
			}
			
			//candidate list
			if(current_Button.getText() == "" && currentUserChoice == "?")
			{
				//CANNIDATE LIST IMPLEMENTATION HERE
				//can use buttons and c_list in here, score.
				
				for (int i = 0; i < boardSide; i++)
				{
					
					//ROW CHECK
					if(buttons[currentX][i].getText() != "")
					{
						current_Button.c_list[(Integer.parseInt(buttons[currentX][i].getText()))-1] = false;
					}
					
					//COL CHECK
					if(buttons[i][currentY].getText() != "")
					{
						current_Button.c_list[(Integer.parseInt(buttons[i][currentY].getText()))-1] = false;
					}
					
					for (int j = 0; j < boardSide; j++)
					{
						//BOX CHECK
						if(buttons[i][j].getBox() == currentBox)
						{
							if(buttons[i][j].getText() != "")
							{
								//this means that there IS a value here so make sure to mark it off c_list
								current_Button.c_list[(Integer.parseInt(buttons[i][j].getText()))-1] = false;
								
							}
						}
					}
				}
				
				//to update candidate loist label
				String s = "";
				for(int k = 0; k<9; k++)
				{
					
					if(current_Button.c_list[k])
					{
						s = s + ((k+1) + " ");
						
					}
					
					String good_string = "Candidate list: " + s;
					candidates.setText(good_string);
				}
		}
			
	}
		
	}

	//action for check on fill menu item
	class checkOnFillAction implements ActionListener {
		public void actionPerformed (ActionEvent e) {
		AbstractButton aButton = (AbstractButton) e.getSource();
		boolean selected = aButton.getModel().isSelected();
		if(selected)
		{
			Allow_user_to_make_mistakes = false;
		}
		else
		{
			Allow_user_to_make_mistakes = true;
		}
		
		}
	}
	
	
	//action for hidden single algorithm
	class hiddenSingleAlgAction implements ActionListener 
	{
		public void actionPerformed (ActionEvent e) 
		{
			for (int i = 0; i < boardSide; i++)
			{
				for (int j = 0; j < boardSide; j++)
				{
					if(buttons[i][j].getText() == "")
					{
						MyButton current_button = buttons[i][j];
						int currentX = current_button.getXPos();
						int currentY = current_button.getYPos();
						
						boolean[] temp_c_list = {false, false, false, false, false, false, false, false, false};
						for(int t = 0; t < boardSide; t++)
						{
							if(current_button.c_list[t])
							{
								temp_c_list[t] = true;
							}
						}
						boolean[] temp_c_list_copy = temp_c_list;
						
						//==================================
						//for "comparing button" section
						
						for(int k = 0; k < boardSide; k++)
						{
							//col
							MyButton next_button = buttons[currentX][k];
							
							if(current_button.getXPos() == next_button.getXPos() &&
						       current_button.getYPos() == next_button.getYPos())
							{
								//do nothing because its the same as the cur button
							}
							else
							{
								for(int q = 0; q < boardSide; q++)
								{
									if(next_button.c_list[q])
									{
										temp_c_list[q] = false;
									}
								}
								//CHECK HERE
								for(int u = 0; u < boardSide; u++)
								{
									int count = 0;
									if(temp_c_list[u])
									{
										count++;
										
									}
									
									if(count == 1)
									{
										current_button.setText(u + 1 + "");
										initC_list();
										return;
									}
								}
								
								temp_c_list = temp_c_list_copy;
								
								
							}
							
							//row
							next_button = buttons[k][currentY];
							if(current_button.getXPos() == next_button.getXPos() &&
							   current_button.getYPos() == next_button.getYPos())
							{
								//do nothing because its the same as the cur button
							}
							else
							{
								for(int q = 0; q < boardSide; q++)
								{
									if(next_button.c_list[q])
									{
										temp_c_list[q] = false;
									}
								}
								//CHECK HERE
								for(int u = 0; u < boardSide; u++)
								{
									int count = 0;
									if(temp_c_list[u])
									{
										count++;
										
									}
									
									if(count == 1)
									{
										current_button.setText(u + 1 + "");
										initC_list();
										return;
									}
								}
								
								temp_c_list = temp_c_list_copy;
							}
							
							for(int y = 0; y < boardSide; y++)
							{
								next_button = buttons[k][y];
								
								if(current_button.getBox() == next_button.getBox())
								{
									if(current_button.getXPos() == next_button.getXPos() &&
									   current_button.getYPos() == next_button.getYPos())
									{
										//do nothing because its the same as the cur button
									}
									else
									{
										for(int q = 0; q < boardSide; q++)
										{
											if(next_button.c_list[q])
											{
												temp_c_list[q] = false;
											}
										}
										//CHECK HERE
										for(int u = 0; u < boardSide; u++)
										{
											int count = 0;
											if(temp_c_list[u])
											{
												count++;
												
											}
											
											if(count == 1)
											{
												current_button.setText(u + 1 + "");
												initC_list();
												return;
											}
										}
										
										temp_c_list = temp_c_list_copy;
									}
								}
							}
						}
					}
				}	
			}
		}
	}

	//action for single algorithm
	class singleAlgAction implements ActionListener {
		public void actionPerformed (ActionEvent e) {
			
			
			for (int i = 0; i < boardSide; i++)
			{
				for (int j = 0; j < boardSide; j++)
				{
					int c_list_count = 0;
					int only_number_in_c_list = 0;
					
					MyButton current_button = buttons[i][j];
					
					for(int k = 0; k < 9; k++)
					{
						if(current_button.c_list[k] && !current_button.getPreset())
						{
							c_list_count++;
							only_number_in_c_list = k + 1;
						}
					}
					
					if(c_list_count == 1)
					{
						current_button.setText(only_number_in_c_list + "");
						initC_list();
						return;
						
					}
					
				}
			}
		}
	}
	
	//to check if the board is filled
	public boolean is_all_filled()
	{
		boolean is_full = true;
		
		for (int i = 0; i < boardSide; i++)
		{
			for (int j = 0; j < boardSide; j++)
			{
				if(buttons[i][j].getText() == "")
				{
					is_full = false;
				}	
			}
		}	
		return is_full;
	}	
	
	//action for naked pairs algorithm
	class nakedPairsAlgAction implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            boolean done = false;

            //rows
            for(int c = 0; c < boardSide; c++)
            {
                for(int r = 0; r < boardSide; r++)
                {
                    MyButton cur = buttons[c][r];
                    if(cur.getText() == "")
                    {
                        int length = 0;
                        for(int i = 0; i < 9; i++)
                        {
                            if(cur.c_list[i] == true)
                            {
                                length++;
                            }
                        }
                        if(length == 2)
                        {
                            for(int i = r; i < boardSide; i++)
                            {
                                if(buttons[c][i].getText() == "")
                                {
                                    MyButton next = buttons[c][i];
                                    if(Arrays.equals(cur.c_list, next.c_list))
                                    {
                                        for(int x = 0; x < boardSide; x++)
                                        {
                                            MyButton toChange = buttons[c][x];
                                            if(toChange.getText() == "" && !Arrays.equals(cur.c_list, toChange.c_list))
                                            {
                                                for(int y = 0; y < 9; y++)
                                                {
                                                    if(cur.c_list[y] == toChange.c_list[y])
                                                    {
                                                        toChange.c_list[y] = false;
                                                        done = true;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                if(done == true)
                                    return;
                            }
                        }
                    }
                }
            }

            //columns
            for(int c = 0; c < boardSide; c++)
            {
                for(int r = 0; r < boardSide; r++)
                {
                    MyButton cur = buttons[r][c];
                    if(cur.getText() == "")
                    {
                        int length = 0;
                        for(int i = 0; i < 9; i++)
                        {
                            if(cur.c_list[i] == true)
                            {
                                length++;
                            }
                        }
                        if(length == 2)
                        {
                            for(int i = r; i < boardSide; i++)
                            {
                                if(buttons[i][c].getText() == "")
                                {
                                    MyButton next = buttons[i][c];
                                    if(Arrays.equals(cur.c_list, next.c_list))
                                    {
                                        for(int x = 0; x < boardSide; x++)
                                        {
                                            MyButton toChange = buttons[x][c];
                                            if(toChange.getText() == "" && !Arrays.equals(cur.c_list, toChange.c_list))
                                            {
                                                for(int y = 0; y < 9; y++)
                                                {
                                                    if(cur.c_list[y] == toChange.c_list[y])
                                                    {
                                                        toChange.c_list[y] = false;
                                                        done = true;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                if(done == true)
                                    return;
                            }
                        }
                    }
                }
            }

            //box
            int r = 0;
            int c = 0;
            for(; r < boardSide; r++)
            {
                for(; c < boardSide; c++)
                {
                    MyButton cur  = buttons[r][c];
                    if(cur.getText() == "")
                    {
                        int length = 0;
                        for(int i = 0; i < 9; i++)
                        {
                            if(cur.c_list[i] == true)
                            {
                                length++;
                            }
                        }
                        if(length == 2)
                        {
                            for(int i = r; i < boardSide; i++)
                            {
                                for(int x = c; c < boardSide; c++)
                                {
                                    MyButton next = buttons[i][x];
                                    if(buttons[i][c].getText() == "" && cur.getBox() == next.getBox())
                                    {                                        
                                        if(Arrays.equals(cur.c_list, next.c_list))
                                        {
                                            for(int p = 0; p < boardSide; p++)
                                            {
                                                for(int q = 0; q < boardSide; q++)
                                                {
                                                    MyButton toChange = buttons[x][c];
                                                    if(toChange.getBox() == cur.getBox() && toChange.getText() == "" && !Arrays.equals(cur.c_list, toChange.c_list))
                                                    {
                                                        for(int y = 0; y < 9; y++)
                                                        {
                                                            if(cur.c_list[y] == toChange.c_list[y])
                                                            {
                                                                toChange.c_list[y] = false;
                                                                done = true;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if(done == true)
                                        return;
                                }
                            }
                        }
                    }
                    if(r == 8 && c == 8)
                        return;
                    if((c + 1) % 3 == 0)
                    {
                        c = c - 2;
                        break;
                    }
                }
                if(r == 8)
                {
                    c = c + 3;
                    r = 0;
                }
            }
        }
    }
}