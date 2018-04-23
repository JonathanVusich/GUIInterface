import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**************************************
 * This class represents a specific
 * type of JButton meant for a number
 * pad.
 * @author Jonathan Vusich
 * @version 1.0
 *************************************/

public class ItemButton extends JButton {

	// Default serialVersionUID
	
	private static final long serialVersionUID = 1L;

	/********************************
	 * This is the constructor for
	 * ItemButton that automatically
	 * sets its preferred size.
	 * @param String
	 ********************************/
	
	public ItemButton(String text) {
		this.setText(text);
		this.setPreferredSize(new Dimension(30, 30));
		this.setMinimumSize(new Dimension(15,15));
		this.setMaximumSize(this.getPreferredSize());
	}
}
