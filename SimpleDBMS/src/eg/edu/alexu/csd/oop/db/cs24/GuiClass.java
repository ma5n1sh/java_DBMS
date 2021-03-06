package eg.edu.alexu.csd.oop.db.cs24;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class GuiClass {

	private JFrame frame;
	private JTable table;
	private JTextArea commandArea;
	private DefaultTableModel model;
	private JLabel updaredRowslbl;
	
	private CommandChecker comCheck;
	
	private String document = "";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			GuiClass window = new GuiClass();
			window.frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}

	/**
	 * Create the application.
	 */
	public GuiClass() {
		comCheck = new CommandChecker();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Database Management System");
		frame.setBounds(100, 100, 632, 459);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JScrollPane tableScrollPane = new JScrollPane();
		tableScrollPane.setBounds(260, 11, 346, 399);
		frame.getContentPane().add(tableScrollPane);
		
		model = new DefaultTableModel();
		setTableModel();
		table = new JTable();
		table.setModel(model);
		tableScrollPane.setViewportView(table);
		
		setCommand();
	}

	private void setTableModel() {
		int count = model.getRowCount();
		for (int i = 0; i < count; i++) {
			model.removeRow(0);
		}
		String[] colName = comCheck.getColumnsNames();
		if(colName != null) {
			model.setColumnIdentifiers(colName);
		}else {
			model.setColumnIdentifiers(new String[0]);
		}
		Object[][] data = comCheck.getDataSet();
		if (data != null) {
			for (int i = 0; i < data.length; i++) {
				model.addRow(data[i]);
			} 
		}
		
	}

	private void setCommand() {
		JScrollPane commandScrollPane = new JScrollPane();
		commandScrollPane.setBounds(10, 61, 239, 159);
		frame.getContentPane().add(commandScrollPane);
		
		commandArea = new JTextArea();
		commandScrollPane.setViewportView(commandArea);
		commandArea.setTabSize(5);
		commandArea.setLineWrap(true);
		commandArea.setFont(new Font("Courier New", Font.PLAIN, 13));
		commandArea.setWrapStyleWord(true);
		
		JButton btnEnter = new JButton("Enter");
		btnEnter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int count = document.length();
					String oldDoc = document;
					document = commandArea.getText().replaceAll("\n", "");
					if(document.lastIndexOf(oldDoc, 0) != -1) {
						comCheck.directCommand(document.substring(count));
					}else {
						comCheck.directCommand(document.substring(getCommandStartIndex(oldDoc)));
					}
					updaredRowslbl.setText("Number of updated rows: " + comCheck.getUpdatedRows());
					setTableModel();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnEnter.setBounds(80, 231, 89, 23);
		frame.getContentPane().add(btnEnter);
		
		setLabel();
	}

	private void setLabel() {
		updaredRowslbl = new JLabel("Number of updated rows: 0");
		updaredRowslbl.setHorizontalAlignment(SwingConstants.CENTER);
		updaredRowslbl.setBounds(10, 282, 240, 23);
		frame.getContentPane().add(updaredRowslbl);
	}
	
	private int getCommandStartIndex(String oldDoc) {
		int start = 0;
		for (int i = 0; i < document.length(); i++) {
			if(oldDoc.charAt(i) != document.charAt(i)) {
				start = i;
				break;
			}
		}
		return start;
	}
}
