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

import javax.swing.*;        
import java.io.*;

public class LoadFile extends JFrame
{
    private File f;
    
    public LoadFile(JMenu menuX)
    {
        JFileChooser fc = new JFileChooser();    
        int result = fc.showOpenDialog(menuX);    
        if(result == JFileChooser.APPROVE_OPTION){    
            f = fc.getSelectedFile();
        }
    }

    public boolean hasFile()
    {
        if(f == null)
            return false;
        else
            return true;
    }

    public File getFile()
    {
        return f;
    }
    
    public void setFileNull()
    {
        f = null;
    }
}
