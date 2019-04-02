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

import javax.swing.JButton;

public class MyButton extends JButton
{

	private int xPos;
    private int yPos;
    private boolean isPreset = false;	//if true, do not allow deletion
    									//automatically assumed not preset
    
    private int box;					//is number 0 - 8 indicating which box the button is in
    									//0||1||2
    									//=======
										//3||4||5
    									//=======
    									//6||7||8
    
    //value = sudoku number AKA index+1
    //when false @index, then value is NOT in cannidate list AKA NOT in c_list
    //all cannidates are at first assumed true bc empty board aka all options are possible
    public boolean[] c_list = {true, true, true, true, true, true, true, true, true};
    
    
    
    

    public void setXPos(int x) {
        xPos = x;
    }

    public void setYPos(int y) {
    	yPos = y;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    
    public void setPreset()
    {
    	isPreset = true;
    }
    
    public boolean getPreset() {
        return isPreset;
    }
    
    public void setBox(int b)
    {
    	box = b;
    }
    
    public int getBox() {
        return box;
    }
 
	
}
