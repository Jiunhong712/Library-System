import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import java.sql.*;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Librarian extends JFrame {

	private JPanel pane1;
	private JTextField text1;
	private JTextField text2;
	private JTextField text3;
	private JTextField text4;

	public Librarian() {
		setTitle("Librarian Page"); // Set "Librarian Page" to display as the title of the frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // The window will close when the exit button is pressed
		setSize(461, 311); // Set the size of the frame
	    setLocationRelativeTo(null); // Set the window to be at the middle when opened
		pane1 = new JPanel(); // Create a panel to add components

		setContentPane(pane1);
		pane1.setLayout(null); // pane1 has no specific layout, so the components can be placed at specific coordinates
		
		JLabel label1 = new JLabel("Book ID:"); // Create label1 to display the text "Book ID:"
		label1.setFont(new Font("Tahoma", Font.PLAIN, 16)); // Set the font and size of the text
		label1.setBounds(90, 54, 80, 29); // Set the coordinates of the text
		pane1.add(label1); // Add label1 to pane 1
		
		JLabel label2 = new JLabel("Book name:"); // Create label2 to display the text "Book name:"
		label2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		label2.setBounds(90, 93, 137, 29);
		pane1.add(label2); // Add label2 to pane 1
		
		JLabel label3 = new JLabel("Status:"); // Create label3 to display the text "Status:"
		label3.setFont(new Font("Tahoma", Font.PLAIN, 16));
		label3.setBounds(90, 132, 137, 29);
		pane1.add(label3); // Add label3 to pane 1
		
		JLabel label4 = new JLabel("Period:"); // Create label4 to display the text "Period:"
		label4.setFont(new Font("Tahoma", Font.PLAIN, 16));
		label4.setBounds(90, 171, 92, 29);
		pane1.add(label4); // Add label4 to pane 1
		
		text1 = new JTextField(); // Create a text field and declare it as text1
		text1.setBounds(191, 54, 165, 29);
		pane1.add(text1); // Add text1 to pane 1
		
		text2 = new JTextField(); // Create a text field and declare it as text2
		text2.setBounds(191, 93, 165, 29);
		pane1.add(text2); // Add text2 to pane 1
		
		text3 = new JTextField(); // Create a text field and declare it as text3
		text3.setBounds(191, 132, 165, 29);
		pane1.add(text3); // Add text3 to pane 1
		
		text4 = new JTextField(); // Create a text field and declare it as text4
		text4.setBounds(191, 171, 165, 29);
		pane1.add(text4); // Add text4 to pane 1
		
		JButton update = new JButton("ADD"); // Create ADD button
		update.setFont(new Font("Tahoma", Font.PLAIN, 14));
		update.setBounds(242, 232, 92, 32);
		pane1.add(update); // Add ADD button to pane 1
		Action updatelistener = new Action(this); // Add action listener to ADD button
		update.addActionListener(updatelistener);
		
		JButton remove = new JButton("REMOVE"); // Create REMOVE button
		remove.setFont(new Font("Tahoma", Font.PLAIN, 14));
		remove.setBounds(344, 232, 92, 32);
		pane1.add(remove); // Add REMOVE button to pane 1
		Action removelistener = new Action(this); // Add action listener to REMOVE button
		remove.addActionListener(removelistener);
	}
	
	// Implementation of ActionListener interface
	class Action implements ActionListener {
	    private Librarian librarian;

	    public Action(Librarian librarian) {
	        this.librarian = librarian;
	    }

	    @Override
	    public void actionPerformed(ActionEvent e) {
	        librarian.actionPerformed(e);
	    }
	}
	
	public void actionPerformed(ActionEvent e) {
	    String url = "jdbc:mysql://localhost:3306/library"; // URL of database
	    String user = "root"; // Username of database
	    String pass = "1234"; // Password of database

	    // Establish database connection
	    try (Connection conn = DriverManager.getConnection(url, user, pass)) {	    	
	    	// Create a TreeMap to store the book data
	    	Map<String, String[]> booklist = new TreeMap<>();

	    	// Handle add action
	    	if (e.getActionCommand().equals("ADD")) {
	    	    // Retrieve user input from text fields
	    	    String id = text1.getText(); // Retrieve Book ID from text1 and save as id
	    	    String bookname = text2.getText(); // Retrieve Book name from text2 and save as bookname
	    	    String status = text3.getText(); // Retrieve Status from text3 and save as status
	    	    String period = text4.getText(); // Retrieve Period from text4 and save as period
	    	    
	    	    // Store the user input into the TreeMap
	    	    String[] bookData = {bookname, status, period};
	    	    booklist.put(id, bookData);

	            String selectSql = "SELECT * FROM books WHERE id=?"; // Holds the query we want to execute
	            try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
	                selectStmt.setString(1, id); // Set the value of the first parameter of SQL query to the value of id
	                ResultSet resultSet = selectStmt.executeQuery(); // Execute query and retrieve a result set

	                // Prepare SQL statement for adding book record
	                String sql;
	                if (!resultSet.next()) { // ID does not exist
	                    sql = "INSERT INTO books (id, bookname, status, period) VALUES (?, ?, ?, ?)"; // Add data into books table in database
	                    
	                 // Write the data to a CSV file
	    	            try {
	    	            	String filename = "booklist.csv";
	    	                FileWriter csvWriter = new FileWriter(filename); // Write text into a file named "booklist.csv"
	    	                csvWriter.append("ID,Book Name,Status,Period\n"); // Add text to the header

	    	                // Loop through each entry in the booklist Map
	    	                for (Entry<String, String[]> entry : booklist.entrySet()) {
	    	                    // Get the key and value for the current entry
	    	                    String key = entry.getKey();
	    	                    String[] value = entry.getValue();
	    	                    
	    	                    // Append the key to the CSV file
	    	                    csvWriter.append(key.toString());
	    	                    csvWriter.append(",");
	    	                    
	    	                    // Append the three values from the value array to the CSV file
	    	                    csvWriter.append(value[0]);
	    	                    csvWriter.append(",");
	    	                    csvWriter.append(value[1]);
	    	                    csvWriter.append(",");
	    	                    csvWriter.append(value[2]);
	    	                    
	    	                    // Add a new line to the CSV file
	    	                    csvWriter.append("\n");
	    	                }

	    	                // Flush the CSV writer to make sure all data is written to the file
	    	                csvWriter.flush();

	    	                // Close the CSV writer
	    	                csvWriter.close();

	    	                // Show success message if the process is successful
	    	                JOptionPane.showMessageDialog(null, filename + " has been successfully updated.");

	    	                // Handles IOException
	    	                } catch (IOException ee) {
	    	                    ee.printStackTrace();
	    	                }
	                } 
	                else {
	                    JOptionPane.showMessageDialog(null, "Book with ID " + id + " already exist. Please remove the book first."); // Pop up message
	                    return; // Return to the librarian page
	                }

	                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	                    stmt.setString(1, id);
	                    stmt.setString(2, bookname);
	                    stmt.setString(3, status);
	                    stmt.setString(4, period);

	                    // Execute SQL statement
	                    int rowsUpdated = stmt.executeUpdate();

	                    // Show success message if update was successful
	                    if (rowsUpdated > 0) {
	                        JOptionPane.showMessageDialog(null, "Book with ID " + id + " has been successfully added to the database.");
	                    } 
	                    
	                    // Show fail message if update was not successful
	                    else {
	                        JOptionPane.showMessageDialog(null, "Error adding book into database.");
	                    }
	                    
	                    // Handles SQLException
	                } catch (SQLException ex) {
	                    JOptionPane.showMessageDialog(null, "Error adding book into database: " + ex.getMessage());
	                }
	            } 
	        }

	        // Handle remove action
	        if (e.getActionCommand().equals("REMOVE")) {
	            String id = text1.getText(); // Retrieve Book ID from text1
	            
	            // Remove the book data from the TreeMap
	            booklist.remove(id);

	            // Write the data to a CSV file
	            try {
	            	String filename = "booklist.csv";
	                FileWriter csvWriter = new FileWriter(filename); // Write text into a file named "booklist.csv"
	                csvWriter.append("ID,Book Name,Status,Period\n"); // Add text to the header

	                // Loop through each entry in the booklist Map
	                for (Entry<String, String[]> entry : booklist.entrySet()) {
	                    // Get the key and value for the current entry
	                    String key = entry.getKey();
	                    String[] value = entry.getValue();
	                    
	                    // Append the key to the CSV file
	                    csvWriter.append(key.toString());
	                    csvWriter.append(",");
	                    
	                    // Append the three values from the value array to the CSV file
	                    csvWriter.append(value[0]);
	                    csvWriter.append(",");
	                    csvWriter.append(value[1]);
	                    csvWriter.append(",");
	                    csvWriter.append(value[2]);
	                    
	                    // Add a new line to the CSV file
	                    csvWriter.append("\n");
	                }

	                // Flush the CSV writer to make sure all data is written to the file
	                csvWriter.flush();

	                // Close the CSV writer
	                csvWriter.close();

	                // Show success message if the process is successful
	                JOptionPane.showMessageDialog(null, filename + " has been successfully updated.");

	                // Handles IOException
	                } catch (IOException ee) {
	                    ee.printStackTrace();
	                }
	            
	            // Prepare SQL statement for deleting book
	            String sql = "DELETE FROM books WHERE id=?"; // Delete the Book ID from database
	            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	                stmt.setString(1, id);

	                // Execute SQL statement
	                int rowsDeleted = stmt.executeUpdate();

	                // Show success message if delete was successful
	                if (rowsDeleted > 0) {
	                    JOptionPane.showMessageDialog(null, "Book with ID " + id + " has been successfully deleted from the database.");

	                    // Clear text fields after successful delete
	                    text1.setText("");
	                    text2.setText("");
	                    text3.setText("");
	                    text4.setText("");
	                } 
	                
	                // Show fail message if delete was not successful
	                else {
	                    JOptionPane.showMessageDialog(null, "Book ID " + id + " does not exist in the database.");
	                }
	                
	                // Handles SQLException
	            } catch (SQLException ex) {
	                JOptionPane.showMessageDialog(null, "Error deleting book record: " + ex.getMessage());
	            }
	        }
	        
	    // Handles SQLException
	    } catch (SQLException ex) {
	        JOptionPane.showMessageDialog(null, "Error connecting to database: " + ex.getMessage());
	    }
	}

	public class CSVWriter {

	    private FileWriter writer;
	    public CSVWriter(String fileName) throws IOException {
	        writer = new FileWriter(fileName);
	    }

	    // Method for writing a header row to the CSV file
	    public void writeHeader(String[] header) throws IOException {
	        writeRow(header);
	    }

	    // Method for writing a data row to the CSV file
	    public void writeRow(String[] header) throws IOException {
	        // Create a StringBuilder to hold the row data
	        StringBuilder sb = new StringBuilder();
	        
	        // Append each value in the row to the StringBuilder
	        for (int i = 0; i < header.length; i++) {
	            sb.append(header.length);
	            
	            // If this is not the last value in the row, append a comma
	            if (i < header.length - 1) {
	                sb.append(",");
	            }
	        }
	        
	        // Add a new line to the end of the row data
	        sb.append("\n");
	        
	        // Write the row data to the CSV file
	        writer.write(sb.toString());
	    }

	    // Method for closing the CSV file
	    public void close() throws IOException {
	        writer.close();
	    }
	}


	// Main function
	public static void main(String[] args) {
		Librarian frame = new Librarian();
		frame.setVisible(true);
	}
}
