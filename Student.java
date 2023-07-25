import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class Student extends JFrame {

	private JPanel pane1;
	private JTextField text1;

	public Student() {
		setTitle("Student Page"); // Set "Student Page" to display as the title of the frame
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
		
		JLabel label2 = new JLabel("Book name: "); // Create label2 to display the text "Book name:"
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
		
		JButton search = new JButton("SEARCH"); // Create SEARCH button
		search.setFont(new Font("Tahoma", Font.PLAIN, 14));
		search.setBounds(319, 232, 92, 32);
		pane1.add(search); // Add SEARCH button to pane 1
		
		JTextArea area1 = new JTextArea(); // Create a text area and declare it as area1
		area1.setEditable(false); // Set the text area to be non-editable
		area1.setBounds(191, 93, 165, 29);
		pane1.add(area1); // Add area1 to pane 1
		
		JTextArea area2 = new JTextArea(); // Create a text area and declare it as area2
		area2.setEditable(false);
		area2.setBounds(191, 132, 165, 29);
		pane1.add(area2); // Add area2 to pane 1
		
		JTextArea area3 = new JTextArea(); // Create a text area and declare it as area3
		area3.setEditable(false);
		area3.setBounds(191, 171, 165, 29);
		pane1.add(area3); // Add area3 to pane 1
		
		// Add action listener to SEARCH button
        search.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String id = text1.getText(); // Retrieve text from text1 and store as string type named id
                Connection conn = null;
                Statement stmt = null;
                ResultSet resultSet = null;
                
                String url = "jdbc:mysql://localhost:3306/library"; // URL of database
        	    String user = "root"; // Username of database
        	    String pass = "1234"; // Password of database

                try {
                    // Establish database connection
                    conn = DriverManager.getConnection(url, user, pass);
                    stmt = conn.createStatement();

                    // Execute query to get book information
                    String query = "SELECT bookname, status, period FROM books WHERE id = " + id;
                    resultSet = stmt.executeQuery(query);
                    
                    // Display book information in text areas
                    if (resultSet.next()) {
                        String bookname = resultSet.getString("bookname");
                        String status = resultSet.getString("status");
                        String period = resultSet.getString("period");
                        area1.setText(bookname);
                        area2.setText(status);
                        area3.setText(period);
                        // Display success message if book data has been successfully found
                        JOptionPane.showMessageDialog(null, "Success.");
                    } 
                    else {
                        // Book ID is not found
                        area1.setText("");
                        area2.setText("");
                        area3.setText("");
                        // Display fail message if book data cannot be found
                        JOptionPane.showMessageDialog(null, "Book with ID " + id + " not found.");
                    }
                    // Handles SQLException
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
	
	// Main function
	public static void main(String[] args) {
		Student frame = new Student();
		frame.setVisible(true);
	}
}

