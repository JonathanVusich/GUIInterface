/********************************
 * This is an enum type that can
 * represent all of the different 
 * transaction results.
 * @author Jonathan Vusich
 * @version 1.0
 ********************************/

public enum TransactionResult {
    // Transaction was successful
    SUCCESS,
    
    // Item is valid but empty
    OUT_OF_STOCK,
    
    // Item ID is invalid (not assigned to any item)
    INVALID_ITEM,
    
    // Customer did not insert enough money
    INSUFFICIENT_FUNDS,
  }