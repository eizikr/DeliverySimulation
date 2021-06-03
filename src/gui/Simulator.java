package gui;
import javax.swing.JFrame;

import components.MainOffice;

import java.awt.*;
import java.awt.event.*;

/**
 * Manage the whole simulator system
 * @version 2.0, 8/5/2021
 * @author ItzikRahamim - 312202351
 * @author GilBenHamo - 315744557
 */
public class Simulator{

	private static MainOffice main_off;	// Main office
	
	private JFrame frame;				// Simulator window
	private static SimulatorPanel sP;	// Simulator panel
	private buttonsPanel bP;			// Buttons panel
	
    public static volatile boolean suspended = false; // Check if need to suspend a simulator
    
	 /** 
	  * Constructs and initializes a the Simulator by default<br>
	  */
	public Simulator()
	{
		// WINDOW SETUP
		frame = new JFrame("Post tracking system");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1200, 700);
		
		// PANELS SETUP
		sP = new SimulatorPanel();
		bP = new buttonsPanel();
		frame.add(sP);
		frame.add(bP,BorderLayout.SOUTH);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);			// Don't resize
		frame.setVisible(true);
		
		// When user close the window
		frame.addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {
		    	if(main_off != null)
		    		main_off.printReport();
		        frame.dispose();
		        System.exit(0);
		    }
		});
	}
	
	/**
	 * @return Simulator panel
	 */
	public static SimulatorPanel getSimuPanel()
	{
		return sP;
	}
	
	/**
	 * @return Main office
	 */
	public static MainOffice getMain_off() {
		return main_off;
	}

	/**
	 * @return Frame
	 */
	public JFrame getFrame() {
		return frame;
	}

	/**
	 * @return Buttons panel
	 */
	public buttonsPanel getbP() {
		return bP;
	}

	/**
	 * Sets the main office
	 * @param main_off Main office
	 */
	public static void setMain_off(MainOffice main_off) {
		Simulator.main_off = main_off;
	}

	/**
	 * Sets the frame
	 * @param frame The main window
	 */
	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	/**
	 * Sets the simulator panel
	 * @param sP Simulator panel
	 */
	public void setsP(SimulatorPanel sP) {
		Simulator.sP = sP;
	}

	/**
	 * Sets the buttons panel
	 * @param bP Buttons panes
	 */
	public void setbP(buttonsPanel bP) {
		this.bP = bP;
	}

	/**
	 * Check if simulator is suspended
	 * @return true if simulator is suspended
	 */
	public static boolean isSuspended() {
		return suspended;
	}

	/**
	 * Suspended the simulator
	 * @param susp suspend the simulator  
	 */
	public static void setSuspended(boolean susp) {
		suspended = susp;
	}
}
