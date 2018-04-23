import java.math.BigDecimal;

/***********************************
 * This class represents an item
 * that could be found in a vending
 * machine.
 * @author Jonathan Vusich
 * @version 1.0
 ***********************************/

public class Item {
	private String description;
	private BigDecimal price;
	private int quantity;
	private String identifier;
	
	/*****************************
	 * This is the constructor
	 * for each item.
	 * @param String
	 * @param BigDecimal
	 * @param int
	 * @param String
	 *****************************/
	
	public Item(String itemDescription, BigDecimal itemPrice, int itemQuantity, String identifier) {
		this.description = itemDescription;
		this.price = itemPrice;
		this.quantity = itemQuantity;
		this.identifier = identifier;
	}
	
	/*******************************
	 * This is a getter method
	 * that returns the description
	 * of this item.
	 * @return String
	 ******************************/
	
	public String description() {
		return this.description;
	}
	
	/*******************************
	 * This is a getter method
	 * that returns the price
	 * of this item.
	 * @return BigDecimal
	 ******************************/
	
	public BigDecimal price() {
		return this.price;
	}
	
	/*******************************
	 * This is a getter method
	 * that returns the quantity
	 * of this item.
	 * @return int
	 ******************************/
	
	public int quantity() {
		return this.quantity;
	}
	
	/*******************************
	 * This is a setter method
	 * that updates the quantity
	 * of this item.
	 * @param int
	 ******************************/
	
	public void updateQuantity(int delta) {
		this.quantity += delta;
	}
	
	/*******************************
	 * This is a getter method
	 * that returns the unique ID
	 * of this item.
	 * @return String
	 ******************************/
	
	public String getID() {
		return this.identifier;
	}
}