import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JFrame {

	// Declare the variables
	private JPanel pane1;
	private JPasswordField pass;
	private JTextField user;
    private JComboBox<String> type;
	private Librarian librarianGUI;
    private Student studentGUI;

	public Login() {
		setTitle("Login Interface"); // Set "Login Interface" to display as the title of the frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // The window will close when the exit button is pressed
		setSize(461, 311); // Set the size of the frame
		setLocationRelativeTo(null); // Set the window to be in the middle when opened
		pane1 = new JPanel(); // Create a panel to add components

		setContentPane(pane1);
		pane1.setLayout(null); // pane 1 has no specific layout, so the components can be placed at specific coordinates
		
		JLabel label1 = new JLabel("User Type:"); // Create label1 to display the text "User Type:"
		label1.setFont(new Font("Tahoma", Font.PLAIN, 16)); // Set the font and size of the text
		label1.setBounds(90, 54, 80, 29); // Set the coordinates of the text
		pane1.add(label1); // Add label1 to pane 1
		
		JLabel label2 = new JLabel("Username:"); // Create label2 to display the text "Username:"
		label2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		label2.setBounds(90, 121, 80, 29);
		pane1.add(label2); // Add label2 to pane 1
		
		JLabel label3 = new JLabel("Password:"); // Create label3 to display the text "Password:"
		label3.setFont(new Font("Tahoma", Font.PLAIN, 16));
		label3.setBounds(90, 160, 80, 29);
		pane1.add(label3); // Add label3 to pane 1
		
		pass = new JPasswordField(); // Create a password field and declare it as pass
		pass.setBounds(193, 162, 165, 29);
		pane1.add(pass); // Add pass to pane 1
		
		user = new JTextField(); // Create a text field and declare it as user
		user.setBounds(193, 123, 165, 29);
		pane1.add(user); // Add user to pane 1
		
		String[] userTypes = {"Student", "Librarian"}; // Set the choices of the combo box
        type = new JComboBox<>(userTypes);
		type.setFont(new Font("Tahoma", Font.PLAIN, 14));
		type.setMaximumRowCount(2); // Set the combo box to display 2 choices
		type.setBounds(228, 58, 100, 20);
		pane1.add(type); // Add the combo box to pane 1
		
		JButton enter = new JButton("ENTER"); // Create a ENTER button
		enter.setFont(new Font("Tahoma", Font.PLAIN, 14));
		enter.setBounds(312, 221, 92, 32);
		pane1.add(enter); // Add ENTER button to pane 1
		Action updatelistener = new Action(this); // Add action listener to the ENTER button
		enter.addActionListener(updatelistener);
	}
	
	// Implementation of ActionListener interface
	class Action implements ActionListener {
	    private Login login;

	    public Action(Login login) {
	        this.login = login;
	    }

	    @Override
	    public void actionPerformed(ActionEvent e) {
	        login.actionPerformed(e);
	    }
	}
	
    public void actionPerformed(ActionEvent e) {
        // Get user input from the interface
        String userType = (String)type.getSelectedItem(); // Get the choice of user from combo box
        String username = user.getText(); // Get the username input
        String password = new String(pass.getPassword()); // Get the password input
        
        String url = "jdbc:mysql://localhost:3306/library"; // Url of the database
        String user = "root"; // Username of the database
        String pass = "1234"; // Password of the database
        
        // Declaring variables that will be used in SQL
        Connection conn = null; // Connection to the database
        Statement stmt = null; // Statements. SELECT/INSERT/UPDATE/DELETE
        ResultSet rs = null; // Typically obtained by executing SELECT on statement object
        
        boolean isValidUser = false; // Check user credentials	
        try {
            // Establish database connection
            conn = DriverManager.getConnection(url, user, pass); 
            stmt = conn.createStatement();

            // execute query based on user type
            if (userType.equals("Student")) { 
            	// Executes query on student table of the database
                rs = stmt.executeQuery("SELECT * FROM student WHERE username='" + username + "' AND password='" + password + "'"); 
                isValidUser = rs.next(); // Check whether query returns any rows of data from the database and set isValidUser to true or false
                if (isValidUser) {
                    studentGUI = new Student();
                    studentGUI.setVisible(true); // The student page will be shown
                    setVisible(false); // The login page will be hidden
                }
            } else if (userType.equals("Librarian")) {
            	// Executes query on librarian table of the database
                rs = stmt.executeQuery("SELECT * FROM librarian WHERE username='" + username + "' AND password='" + password + "'");
                isValidUser = rs.next();
                if (isValidUser) {
                    librarianGUI = new Librarian();
                    librarianGUI.setVisible(true); // The librarian page will be shown
                    setVisible(false);
                }
            }
            // Handles SQLException
        } catch (SQLException ex) { 
            ex.printStackTrace();
        }

        // Show login result
        if (isValidUser) {
            JOptionPane.showMessageDialog(this, "Login successful!"); // Pop up message

        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password."); // Pop up message
        }
    }

    // Main function
	public static void main(String[] args) {
		Login frame = new Login();
		frame.setVisible(true);
	}
}
