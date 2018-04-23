import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

/*******************************************
 * This class is a GUI selection interface
 * that allows a user to interact with the
 * vending machine of their choice.
 * @author Jonathan Vusich
 * @version 1.0
 *******************************************/

public class GUIInterface implements ActionListener {
	
	// Assorted variable declarations
	
	String[] filenames = {"data/snacks.txt", "data/drinks.txt"};
	ArrayList<String> options = new ArrayList<String>();
	Map<String, ArrayList<Item>> categories = getCategories(filenames);
	String currentSelection = "";
	BigDecimal revenue = new BigDecimal(0);
	String categoryName;
	String itemId;
	BigDecimal moneyEntered;
	boolean running = true;
	private static final long serialVersionUID = 1L;
	private static final int FRAME_WIDTH = 600;
	private static final int FRAME_HEIGHT = 450;
	private BigDecimal money;
	VendingMachine vm;
		
	// ItemButton declaration
	
	private ItemButton buttonA;
	private ItemButton buttonB;
	private ItemButton buttonC;
	private ItemButton buttonD;
	private ItemButton buttonE;
	private ItemButton buttonF;
	private ItemButton button1;
	private ItemButton button2;
	private ItemButton button3;
	private ItemButton button4;
	private ItemButton button5;
	private ItemButton button6;
	
	// JButton declaration
	
	private JButton getChange;
	private JButton addMoney;
	private JButton vend;
	
	// JScrollPane declaration
	
	private JScrollPane scrollBar;
	
	// JLabel declaration
	
	private JLabel itemSelection;
	private JLabel moneyLabel;
	private JLabel itemSearch;
	private JLabel itemResults;
	private JLabel makeSelection;
	
	// JTextField declaration
	
	private JTextArea menu;	
	private JTextField moneyRemaining = null;
	private JTextField itemSelect = null;
	private JTextField itemSearchBox = null;
	
	// JPanel declaration
	
	private JPanel abcPad;
	private JPanel numPad;
	private JFrame mainFrame;
	private JPanel rightPane;
	private JPanel leftPane;
	private JPanel searchPane;
	private JPanel moneyRemainingPane;
	private JPanel itemSelectPane;
	private JPanel padPane;
	private JPanel assortedElements;
	private JPanel mainPanel;
	private JPanel buttonPane;
	
	/**********************************
	 * This method initializes the GUI.
	 * @param String[], args
	 **********************************/
	
	public static void main(String[] args) {
		
		GUIInterface gui = new GUIInterface();
		gui.initialize();
	}
	
	/**********************************
	 * This method initializes the GUI.
	 * @param ActionEvent
	 **********************************/
	
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() instanceof ItemButton) {
			ItemButton button = (ItemButton)event.getSource();
			addToSelection(button.getText());
			itemSelect.setText(currentSelection);
			mainFrame.validate();
		} else if (event.getSource() instanceof JButton) {
			JButton button = (JButton)event.getSource();
			if (button.getText().equalsIgnoreCase("Vend!")) {
				vm.insertMoney(money);
				if (!vm.containsID(vm.getItems(), currentSelection)) {
					displayResult(vm.vend(currentSelection));
					vm.dispenseChange();
				} else {
					displayResult(vm.vend(currentSelection), vm.getItem(vm.idToIndex(currentSelection)));
					money = vm.dispenseChange();
					populateMenu();
					moneyRemaining.setText(money.toString());
					mainFrame.validate();
				}
			} else if (button.getText().equalsIgnoreCase("Add Money")) {
				BigDecimal addMoney = waitForMoney();
				money = money.add(addMoney);
				moneyRemaining.setText(money + "");
				mainFrame.validate();
			} else {
				JOptionPane.showMessageDialog(mainFrame, "Your change is $" + money, "Thank you!", JOptionPane.PLAIN_MESSAGE);
				mainFrame.dispose();
				this.initialize();
				
			}
			
		} else {
			JTextField field = (JTextField)event.getSource();
			int result = searchForItem(field.getText());
			if (result == -1) {
				itemResults.setText("No such item exists!");
				itemResults.setAlignmentX(assortedElements.CENTER_ALIGNMENT);
				mainFrame.validate();
			} else {
				StringBuilder sb = new StringBuilder();
				Formatter formatter = new Formatter(sb, Locale.US);
				Item item = vm.getItems().get(result);
				formatter.format("%s: %s - $%.2f (%d)\n", item.getID(), item.description(), item.price(), item.quantity());
				itemResults.setText(sb.toString());
				itemResults.setAlignmentX(assortedElements.CENTER_ALIGNMENT);
				mainFrame.validate();
			}
		}
	}
	
	/*****************************************
	 * This method changes the item selection
	 * string based on the users input.
	 * @param String
	 ******************************************/
	
	private void addToSelection(String arg) {
		if (currentSelection.length() < 2) {
			currentSelection += arg;
		} else {
			currentSelection = currentSelection.substring(1) + arg;
		}
	}
	
	/**********************************
	 * This method asks the user which
	 * vending machine they would like
	 * to use.
	 * @param Set<String>
	 * @return String
	 **********************************/

	public String waitForCategorySelection(Set<String> categories) {
		String[] category = categories.stream().toArray(String[]::new);
		JFrame frame = new JFrame();
			String selection = (String)JOptionPane.showInputDialog(frame, "Please select a Vending Machine:", "Vending Machine Selection", JOptionPane.PLAIN_MESSAGE, null, getNames(category), category[0]);
			if (selection == null) {
				System.exit(0);
			}
			return setName(selection);
		
	}
	
	/**********************************
	 * This method asks the user for
	 * a monetary amount.
	 * @return BigDecimal
	 **********************************/

	public BigDecimal waitForMoney() {
		JFrame frame = new JFrame();
		boolean input = false;
		BigDecimal money = new BigDecimal(0);
		while (!input) {
			String text = (String)JOptionPane.showInputDialog(frame, "Please enter the amount of money that you would like to spend", "Input Money", JOptionPane.PLAIN_MESSAGE);
			if (text == null) {
				return money;
			}
			try {
				money = new BigDecimal(text);
				input = true;
			} catch (NumberFormatException nfe) {
				JOptionPane.showMessageDialog(frame, "Error! Invalid input!", "Error!", JOptionPane.ERROR_MESSAGE);
			} catch (NullPointerException npe) {
				JOptionPane.showMessageDialog(frame, "Error! Invalid input!", "Error!", JOptionPane.ERROR_MESSAGE);
	
			}
			if (money.compareTo(new BigDecimal(0)) < 0) {
				JOptionPane.showMessageDialog(frame, "Error! Not a valid amount of money!", "Error!", JOptionPane.ERROR_MESSAGE);
				input = false;
			}
		}	
		money = money.setScale(2, RoundingMode.DOWN);
		return money;
	}

	/***********************************
	 * This method updates the JLabel
	 * that displays the amount of money
	 * that the user has in the machine.
	 * @param BigDecimal
	 ***********************************/
	
	public void displayBalance(BigDecimal money) {
		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter(sb, Locale.US);
		formatter.format("$.2f", money);
		moneyRemaining.setText(sb.toString());
	}

	/***********************************
	 * This method displays a message
	 * dialog with different information
	 * depending on the result of the
	 * transaction.
	 * @param TransactionResult
	 ***********************************/
	
	public void displayResult(TransactionResult result, Item item) {
		if (result == TransactionResult.SUCCESS) {
		JOptionPane.showMessageDialog(mainFrame, "Transaction completed successfully! You bought " + item.description() + " for $" + item.price() + ".", "Success!", JOptionPane.PLAIN_MESSAGE);
		} else if (result == TransactionResult.INVALID_ITEM) {
			JOptionPane.showMessageDialog(mainFrame, "Error! Invalid item!", "Error!", JOptionPane.ERROR_MESSAGE);
		} else if (result == TransactionResult.INSUFFICIENT_FUNDS) {
			JOptionPane.showMessageDialog(mainFrame, "Error! You have insufficient funds to purchase this item!\nPlease select another item or add more money!", "Error!", JOptionPane.ERROR_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(mainFrame, "Error! This item is out of stock!", "Error!", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/***********************************
	 * This method displays a message
	 * dialog with different information
	 * depending on the result of the
	 * transaction.
	 * @param TransactionResult
	 ***********************************/
	
	public void displayResult(TransactionResult result) {
		if (result == TransactionResult.INVALID_ITEM) {
			JOptionPane.showMessageDialog(mainFrame, "Error! Invalid item!", "Error!", JOptionPane.ERROR_MESSAGE);
		} else if (result == TransactionResult.INSUFFICIENT_FUNDS) {
			JOptionPane.showMessageDialog(mainFrame, "Error! You have insufficient funds to purchase this item!\nPlease select another item or add more money!", "Error!", JOptionPane.ERROR_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(mainFrame, "Error! This item is out of stock!", "Error!", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**********************************
	 * This method changes the names of
	 * the data files into a more human
	 * readable format.
	 * @param String[]
	 * @return String[]
	 **********************************/
	
	public String[] getNames(String[] machineChoice) {
		String[] results = new String[machineChoice.length];
		for (int i = 0; i < machineChoice.length; i++) {
			String name = machineChoice[i].substring(5);
			String cap = name.substring(0, 1).toUpperCase() + name.substring(1);
			results[i] = cap;
		}
		return results;
	}
	
	/**********************************
	 * This method changes the name of
	 * the data files back into a 
	 * machine readable format.
	 * @param String
	 * @return String
	 **********************************/
	
	public String setName(String text) {
		String cap = text.substring(0, 1).toLowerCase() + text.substring(1);
		return "Data/" + cap;
	}
	
	/**********************************
	 * This method gets the title name
	 * for the vending machine from 
	 * its data file.
	 * @param String
	 * @return String
	 **********************************/
	
	public String getName(String text) {
		return text.substring(5).substring(0, 1).toUpperCase() + text.substring(6);
	}
	
	/**********************************
	 * This method populates the menu
	 * field with the current item info
	 * of the vending machine.
	 **********************************/
	
	public void populateMenu() {
		menu.setText("\nMachine Menu:\n\n");
		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter(sb, Locale.US);
		for (Item item : vm.getItems()) {
			formatter.format("%s: %s - $%.2f (%d)\n", item.getID(), item.description(), item.price(), item.quantity());
		}
		menu.append(sb.toString());
		menu.validate();
	}
	
	/**********************************
	 * This method parses and returns
	 * an arraylist of items from a 
	 * data file.
	 * @param String
	 * @return ArrayList<Item>
	 **********************************/
	
	private static ArrayList<Item> parseFile(String filename) {

		ArrayList<Item> stock = new ArrayList<Item>();
		String[] tokens;


		char letter = 'A';
		String identifier;
		int value = 0;
		int line = 0;
		String data;
		BufferedReader br = null;
		FileReader fr = null;
		// This is still broken, skips every other line


		try {
			fr = new FileReader(filename);
			br = new BufferedReader(fr);
			while ((data = br.readLine()) != null) {
				if (data.length() == 0) {
					continue;
				}
				value++;
				if (value == 7) {
					value = 1;
				}
				if (line != 0 && line%6 == 0) {
					letter++;
				}

				tokens = data.split(",");
				identifier = Character.toString(letter) + value;
				try {
					stock.add(new Item(tokens[0], new BigDecimal((tokens[1])).setScale(2, RoundingMode.DOWN), Integer.parseInt(tokens[2]), identifier));
					line++;
				} catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
					System.err.println("Bad item in file " + filename + 
							" on line " + (line+1) + " of " + filename);
				}
			}
		} catch (IOException e) {
			System.out.println("Error! File not found!");
		}
		return stock;
	}
	
	/*******************************************
	 * This method returns a hashmap of
	 * vending machine categories and 
	 * its data.
	 * @param String[]
	 * @return HashMap<String, ArrayList<Item>>
	 ******************************************/
	
	private HashMap<String, ArrayList<Item>> getCategories(String[] filenames) {
		HashMap<String, ArrayList<Item>> categories = new HashMap<String, ArrayList<Item>>();

		for (String f : filenames) {
			String basename = f.split("\\.(?=[^\\.]+$)")[0];
			String category = basename.substring(0, 1).toUpperCase() + basename.substring(1);
			options.add(category);
			categories.put(category, parseFile(f));
		}

		return categories;
	}
	
	
	/**********************************
	 * This method searches the items
	 * of the current vending machine
	 * for a specific item name.
	 * @param String
	 * @return int
	 **********************************/
	
	private int searchForItem(String text) {
		for (Item item : vm.getItems()) {
			if (text.equalsIgnoreCase(item.description())) {
				return vm.idToIndex(item.getID());
			}
		}
		return -1;
			
	}
	
	/**********************************
	 * This method initializes the GUI.
	 **********************************/
	
	private void initialize() {
		mainFrame = new JFrame();
		mainPanel = new JPanel(new GridLayout(1,2,15,15));
		mainPanel.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		rightPane = new JPanel();
		rightPane.setLayout(new BoxLayout(rightPane, BoxLayout.Y_AXIS));
		searchPane = new JPanel();
		searchPane.setLayout(new FlowLayout());
		moneyRemainingPane = new JPanel();
		moneyRemainingPane.setLayout(new GridLayout(1,2,15,15));
		itemSelectPane = new JPanel();
		itemSelectPane.setLayout(new GridLayout(1,2,15,15));
		buttonPane = new JPanel();
		buttonPane.setLayout(new GridLayout(1,2,15,15));
		padPane = new JPanel(new GridLayout(1,2,40,20));
		leftPane = new JPanel();
		leftPane.setLayout(new BoxLayout(leftPane, BoxLayout.Y_AXIS));
		assortedElements = new JPanel();
		assortedElements.setLayout(new BoxLayout(assortedElements, BoxLayout.Y_AXIS));
		
		// Set up abcPad and numPad
		
		abcPad = new JPanel(new GridLayout(3,2,15,15));
		numPad = new JPanel(new GridLayout(3,2,15,15));
		
		buttonA = new ItemButton("A");
		buttonA.addActionListener(this);
		buttonB = new ItemButton("B");
		buttonB.addActionListener(this);
		buttonC = new ItemButton("C");
		buttonC.addActionListener(this);
		buttonD = new ItemButton("D");
		buttonD.addActionListener(this);
		buttonE = new ItemButton("E");
		buttonE.addActionListener(this);
		buttonF = new ItemButton("F");
		buttonF.addActionListener(this);
		button1 = new ItemButton("1");
		button1.addActionListener(this);
		button2 = new ItemButton("2");
		button2.addActionListener(this);
		button3 = new ItemButton("3");
		button3.addActionListener(this);
		button4 = new ItemButton("4");
		button4.addActionListener(this);
		button5 = new ItemButton("5");
		button5.addActionListener(this);
		button6 = new ItemButton("6");
		button6.addActionListener(this);
		
		
		abcPad.add(buttonA);
		abcPad.add(buttonB);
		abcPad.add(buttonC);
		abcPad.add(buttonD);
		abcPad.add(buttonE);
		abcPad.add(buttonF);
		
		numPad.add(button1);
		numPad.add(button2);
		numPad.add(button3);
		numPad.add(button4);
		numPad.add(button5);
		numPad.add(button6);
				
		// Add both buttons to buttonPane
		
		getChange = new JButton("Get Change");
		getChange.setPreferredSize(new Dimension(20,15));
		getChange.addActionListener(this);
		buttonPane.add(getChange);
		addMoney = new JButton("Add Money");
		addMoney.addActionListener(this);
		buttonPane.add(addMoney);
		
		// Add label and box to itemSelectPane
		
		itemSelection = new JLabel("Item Selection:");
		itemSelectPane.add(itemSelection);
		itemSelection.setAlignmentX(itemSelectPane.CENTER_ALIGNMENT);
		itemSelect = new JTextField(10);
		itemSelect.setEditable(false);
		itemSelectPane.add(itemSelect);
		itemSelect.setAlignmentX(itemSelectPane.CENTER_ALIGNMENT);
		
		// Add label and box to moneyRemainingPane
				
		moneyLabel = new JLabel("Money remaining:");
		moneyRemainingPane.add(moneyLabel);
		moneyLabel.setAlignmentX(moneyRemainingPane.CENTER_ALIGNMENT);
		moneyRemaining = new JTextField(10);
		moneyRemaining.setEditable(false);
		moneyRemainingPane.add(moneyRemaining);
		moneyRemaining.setAlignmentX(moneyRemainingPane.CENTER_ALIGNMENT);
		
		// Set up padPane
				
		padPane.add(abcPad);
		padPane.add(numPad);
		
		// Set up searchPane
		
		itemSearch = new JLabel("Search for item:");
		searchPane.add(itemSearch);
		itemSearchBox = new JTextField(10);
		itemSearchBox.addActionListener(this);
		searchPane.add(itemSearchBox);
		
		// Set up assortedElements
		
		itemResults = new JLabel("");
		itemResults.setMaximumSize(new Dimension(Integer.MAX_VALUE, 15));
		itemResults.setVisible(true);
		assortedElements.add(itemResults);
		itemResults.setAlignmentX(assortedElements.CENTER_ALIGNMENT);
		makeSelection = new JLabel("Make a selection:");
		makeSelection.setFont(new Font("Serif", Font.BOLD, 28));
		assortedElements.add(makeSelection);
		makeSelection.setAlignmentX(assortedElements.CENTER_ALIGNMENT);
		
		leftPane.add(searchPane);
		leftPane.add(Box.createVerticalGlue());
		leftPane.add(assortedElements);
		leftPane.add(Box.createVerticalGlue());
		leftPane.add(padPane);
		leftPane.add(Box.createVerticalGlue());
		leftPane.add(Box.createRigidArea(new Dimension(0,15)));
		leftPane.add(itemSelectPane);
		leftPane.add(Box.createVerticalGlue());
		leftPane.add(Box.createRigidArea(new Dimension(0,15)));
		leftPane.add(moneyRemainingPane);
		leftPane.add(Box.createVerticalGlue());
		leftPane.add(Box.createRigidArea(new Dimension(0,15)));
		leftPane.add(buttonPane);
		
		// Set up right pane
		
		menu = new JTextArea();
		menu.setVisible(true);
		menu.setEditable(false);
		scrollBar = new JScrollPane(menu);
		rightPane.add(scrollBar);
		vend = new JButton("Vend!");
		vend.addActionListener(this);
		rightPane.add(vend);
		vend.setAlignmentX(rightPane.CENTER_ALIGNMENT);
		
		mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		mainPanel.add(leftPane);
		mainPanel.add(rightPane);
		mainFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		mainFrame.add(mainPanel);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.validate();
		
		getCategories(filenames);
		categoryName = waitForCategorySelection(categories.keySet());
		vm = new VendingMachine(categories.get(categoryName));
		money = waitForMoney();
		moneyRemaining.setText(money.toString());
		populateMenu();
		mainFrame.setTitle(getName(categoryName + " Vending Machine"));
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
		
	}
}
