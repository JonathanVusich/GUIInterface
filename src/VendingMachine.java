import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/************************************
 * This class simulates a real life
 * vending machine. It tracks stock,
 * and can receive and dispense items
 * and change.
 * @author Jonathan Vusich
 * @version 2.0
 *************************************/

public class VendingMachine {
  private BigDecimal money;
  private BigDecimal revenue;
  private ArrayList<Item> items;
  private static final Map<Character, Integer> letterMap = createMap();
  private static Map<Character, Integer> createMap() {
      Map<Character, Integer> myMap = new HashMap<Character, Integer>();
      char letter = 'A';
      int integer = 0;
      for (int i = 0; i < 26; i++) {
    	  myMap.put(letter, integer);
    	  letter++;
    	  integer+=6;
      }
      return myMap;
  }
  
  /**********************************
   * This is the constructor for the
   * vending machine.
   * @param ArrayList<Item>
   **********************************/
  
  public VendingMachine(ArrayList<Item> items) {
    this.items = items;
    this.money = new BigDecimal(0);
    this.revenue = new BigDecimal(0);
  }
  
  /**********************************
   * This method is a getter method 
   * that returns the transaction 
   * result of a given item.
   * @return TransactionResult
   **********************************/
  
  public TransactionResult vend(String itemId) {
    
	  if (!this.containsID(items, itemId)) {   	
		  return TransactionResult.INVALID_ITEM;
	  }
    
	  final Item item = this.items.get(idToIndex(itemId));
    
	  if ((item.price()).compareTo(this.money) > 0) {
		  return TransactionResult.INSUFFICIENT_FUNDS;
	  }
    
	  if (item.quantity() < 1) {
		  return TransactionResult.OUT_OF_STOCK;
	  }
    
	  item.updateQuantity(-1);
	  this.money = this.money.subtract(item.price());
	  this.revenue = this.revenue.add((item.price()));
	  return TransactionResult.SUCCESS;
  	}

  /**********************************
   * This method is a setter method 
   * that inserts money into the 
   * vending machine.
   * @param BigDecimal
   **********************************/
  
  public void insertMoney(BigDecimal money) {
    this.money = this.money.add(money);
  }
  
  /**********************************
   * This method is a getter method 
   * that returns the current 
   * money of the vending machine.
   * @return BigDecimal
   **********************************/
  
  public BigDecimal getBalance() {
    return this.money;
  }
  
  /**********************************
   * This method is a method 
   * that returns and clears the
   * money in the vending machine.
   * @return BigDecimal
   **********************************/  
  
  public BigDecimal dispenseChange() {
    BigDecimal money = this.money;
    this.money = new BigDecimal(0);
    return money;
  }
  
  /**********************************
   * This method is a search method 
   * that returns true or false
   * depending on whether or not
   * an item is presently in stock.
   * @return boolean
   * @param ArrayList<Item>, String
   **********************************/
  
  public boolean containsID (final ArrayList<Item> list, final String id) {
	  return list.stream().filter(o -> o.getID().equals(id)).findFirst().isPresent();
  }
  
  /**********************************
   * This method is a method that 
   * takes an item's identifier and 
   * turns it into a valid index where
   * that item may be found.
   * @return int
   * @param String
   **********************************/
  
  public int idToIndex (String id) {
	  
	  int valueOfLetter = letterMap.get(id.charAt(0));
	  int value = Integer.parseInt(id.substring(1, 2));
	  return valueOfLetter + value - 1;
  }
  
  /**********************************
   * This method is a getter method 
   * that returns the current 
   * inventory of the vending machine.
   * @return ArrayList<Item>
   **********************************/
  
  public ArrayList<Item> getItems() {
	  return this.items;
  }
  
  /**********************************
   * This is a getter method that 
   * returns the item at the 
   * proper index.
   * @param int
   * @return Item
   */
  
  public Item getItem(int index) {
	  return this.items.get(index);
  }
}