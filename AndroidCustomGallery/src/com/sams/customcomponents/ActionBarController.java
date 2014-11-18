
package com.sams.customcomponents;

/**
 * ActionBarController - custom C.A.B type menu provider<br>
 * provides contextual action bar type items on gallery item selection
 * @author Sam Rajan
 *
 */
public interface ActionBarController {
		
	/**
	 * @param toogleAction boolean value for showing or hiding 
	 * actionbar items..<br>
	 * if false, hides the item otherwise shows it 
	 */
	public void toggleActionBarItems(boolean toogleAction);
	
	/**
	 * function for updating counter value in actionbar
	 * @param value boolean value for incrementing
	 * or decrementing the counter..
	 * if false, counter decrements otherwise increments
	 */
	public void updateCounter(boolean value);
	

}
