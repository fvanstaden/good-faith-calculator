package main;

import java.awt.Component;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.spire.doc.Body;
import com.spire.doc.Document;
import com.spire.doc.Section;
import com.spire.doc.Table;
import com.spire.doc.documents.Paragraph;
import com.spire.doc.documents.Style;
import com.spire.doc.documents.TextSelection;
import com.spire.doc.fields.TextRange;
import com.spire.doc.formatting.CharacterFormat;

import javax.swing.JProgressBar;

public class PrinterWindow extends JDialog {

	private static Controller controller;
	public static TablePane tablePane;

	protected Person patient;
	protected Person addressee;

	private JPanel buttonPane;
	private JButton printButton;
	private JButton cancelButton;
	private JTree fileTree;
	private JTextField userNameTF;
	private JTextField userTitleTF;
	private JTextField ptNameTF;
	private JTextField ptIDTF;
	private JTextField ptDOBTF;
	private JTextField ptAddressTF;
	private JTextField textField_1;
	private JTextField ptPhoneTF;
	private JTextField pt2NameTF;
	private JTextField pt2AddressTF;
	private JTextField pt2PhoneTF;
	private JTextField ptFaxTF;
	private JTextField pt2FaxTF;
	private JProgressBar progressBar;
	private int returnResult;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			new PrinterWindow(controller);

		} catch (Exception e) {
			new PrinterWindow(new Controller());
			e.printStackTrace();
		}
	}

	protected class Person{

		protected String name = "(!*NO NAME*!)";
		protected String DOB = "(!*NO DOB*!)";
		protected String id = "(!*NO ID*!)";

		protected String address = "(!*NO ADDRESS*!)";
		protected String phone = "(!*NO PHONE*!)";
		protected String fax = "(!*NO FAX*!)";

		public Person(String name, String DOB, String id, String address, String phone, String fax) {
			this.name = name;
			this.DOB = DOB;
			this.id = id;
			this.address = address;
			this.phone = phone;
		}

		public Person(String name, String address, String phone, String fax) {
			this.name = name;
			this.address = address;
			this.phone = phone;
			this.fax = fax;
		}

	}

	public PrinterWindow(Controller x) {
		
		controller = x;
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		
		setVisible(true);
		setTitle("Print Calculation");
		controller = x;
		setResizable(false);
		setBounds(100, 100, 549, 345);
		JPanel panel = new JPanel();
		{
			buttonPane = new JPanel();
			{
				printButton = new JButton("Print");
				printButton.setToolTipText("Print the entered details onto the selected template. Will timeout after 20 seconds to stop user from being softlocked.");
				printButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							if(fileTree.getLastSelectedPathComponent() != null) {
								
								MyThread thread = new MyThread();
								returnResult = -1;
								thread.start();
								printTemplate();
								thread.join();
								
							}
							
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
			
					}
				});
				
				
				
				printButton.setActionCommand("OK");
				getRootPane().setDefaultButton(printButton);
			}
			{
				cancelButton = new JButton("Close");
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
			}
		}
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(buttonPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 246, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(buttonPane, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);

		JScrollPane scrollPane = new JScrollPane();

		JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
		tabbedPane.setFocusable(false);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 185, GroupLayout.PREFERRED_SIZE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 229, GroupLayout.PREFERRED_SIZE)
						.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 229, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		gl_panel.linkSize(SwingConstants.VERTICAL, new Component[] {scrollPane, tabbedPane});

		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("User Information", null, panel_1, null);

		userNameTF = new JTextField();
		userNameTF.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Full Name:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);

		userTitleTF = new JTextField();
		userTitleTF.setColumns(10);

		JLabel lblNewLabel_1_1 = new JLabel("Title:");
		lblNewLabel_1_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JButton btnNewButton = new JButton("Save");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.setUserName(userNameTF.getText());
				controller.setTitle(userTitleTF.getText());
				controller.saveUserInfo(userNameTF.getText(), userTitleTF.getText());
			}
		});
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING, false)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(userNameTF, GroupLayout.PREFERRED_SIZE, 218, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(lblNewLabel_1_1, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
							.addGap(4)
							.addComponent(userTitleTF, GroupLayout.PREFERRED_SIZE, 218, GroupLayout.PREFERRED_SIZE))
						.addComponent(btnNewButton))
					.addContainerGap())
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_1)
						.addComponent(userNameTF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel_1_1, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGap(1)
							.addComponent(userTitleTF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnNewButton)
					.addContainerGap(114, Short.MAX_VALUE))
		);
		panel_1.setLayout(gl_panel_1);

		JPanel panel_1_1 = new JPanel();
		tabbedPane.addTab("Patient Information", null, panel_1_1, null);

		JLabel lblNewLabel_1_2 = new JLabel("Full Name:");
		lblNewLabel_1_2.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_1_2.setFont(new Font("Tahoma", Font.PLAIN, 14));

		ptNameTF = new JTextField();
		ptNameTF.setColumns(10);

		JLabel lblNewLabel_1_1_1 = new JLabel("Patient ID:");
		lblNewLabel_1_1_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));

		ptIDTF = new JTextField();
		ptIDTF.setColumns(10);

		JLabel lblNewLabel_1_1_1_1 = new JLabel("DOB:");
		lblNewLabel_1_1_1_1.setToolTipText("MM/DD/YYYY");
		lblNewLabel_1_1_1_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_1_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));

		ptDOBTF = new JTextField();
		ptDOBTF.setToolTipText("MM/DD/YYYY");
		ptDOBTF.setColumns(10);

		ptAddressTF = new JTextField();
		ptAddressTF.setColumns(10);

		JLabel lblNewLabel_1_1_1_2 = new JLabel("Address:");
		lblNewLabel_1_1_1_2.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_1_1_1_2.setFont(new Font("Tahoma", Font.PLAIN, 14));

		textField_1 = new JTextField();
		textField_1.setColumns(10);

		JLabel lblNewLabel_1_1_1_3 = new JLabel("Patient ID:");
		lblNewLabel_1_1_1_3.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_1_1_1_3.setFont(new Font("Tahoma", Font.PLAIN, 14));

		ptPhoneTF = new JTextField();
		ptPhoneTF.setColumns(10);

		JLabel lblNewLabel_1_1_1_4 = new JLabel("Phone:");
		lblNewLabel_1_1_1_4.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_1_1_1_4.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JLabel lblNewLabel_1_1_1_4_2 = new JLabel("Fax:");
		lblNewLabel_1_1_1_4_2.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_1_1_1_4_2.setFont(new Font("Tahoma", Font.PLAIN, 14));

		ptFaxTF = new JTextField();
		ptFaxTF.setColumns(10);
		GroupLayout gl_panel_1_1 = new GroupLayout(panel_1_1);
		gl_panel_1_1.setHorizontalGroup(
			gl_panel_1_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1_1.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_panel_1_1.createSequentialGroup()
							.addComponent(lblNewLabel_1_2, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(ptNameTF, GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE))
						.addGroup(Alignment.TRAILING, gl_panel_1_1.createSequentialGroup()
							.addComponent(lblNewLabel_1_1_1_1, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
							.addGap(4)
							.addComponent(ptDOBTF, GroupLayout.PREFERRED_SIZE, 218, GroupLayout.PREFERRED_SIZE))
						.addGroup(Alignment.TRAILING, gl_panel_1_1.createSequentialGroup()
							.addComponent(lblNewLabel_1_1_1, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
							.addGap(4)
							.addComponent(ptIDTF, GroupLayout.PREFERRED_SIZE, 218, GroupLayout.PREFERRED_SIZE))
						.addGroup(Alignment.TRAILING, gl_panel_1_1.createSequentialGroup()
							.addComponent(lblNewLabel_1_1_1_2, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
							.addGap(4)
							.addComponent(ptAddressTF, GroupLayout.PREFERRED_SIZE, 218, GroupLayout.PREFERRED_SIZE))
						.addGroup(Alignment.TRAILING, gl_panel_1_1.createSequentialGroup()
							.addComponent(lblNewLabel_1_1_1_3, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
							.addGap(4)
							.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, 218, GroupLayout.PREFERRED_SIZE))
						.addGroup(Alignment.TRAILING, gl_panel_1_1.createSequentialGroup()
							.addComponent(lblNewLabel_1_1_1_4, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
							.addGap(4)
							.addComponent(ptPhoneTF, GroupLayout.PREFERRED_SIZE, 218, GroupLayout.PREFERRED_SIZE))
						.addGroup(Alignment.TRAILING, gl_panel_1_1.createSequentialGroup()
							.addComponent(lblNewLabel_1_1_1_4_2, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
							.addGap(4)
							.addComponent(ptFaxTF, GroupLayout.PREFERRED_SIZE, 218, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_panel_1_1.setVerticalGroup(
			gl_panel_1_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_1_2)
						.addComponent(ptNameTF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1_1.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel_1_1_1_1, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_1_1.createSequentialGroup()
							.addGap(1)
							.addComponent(ptDOBTF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1_1.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel_1_1_1, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_1_1.createSequentialGroup()
							.addGap(1)
							.addComponent(ptIDTF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1_1.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel_1_1_1_2, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_1_1.createSequentialGroup()
							.addGap(1)
							.addComponent(ptAddressTF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1_1.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel_1_1_1_4, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_1_1.createSequentialGroup()
							.addGap(1)
							.addComponent(ptPhoneTF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1_1.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel_1_1_1_4_2, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_1_1.createSequentialGroup()
							.addGap(1)
							.addComponent(ptFaxTF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(242)
					.addGroup(gl_panel_1_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1_1.createSequentialGroup()
							.addGap(75)
							.addComponent(lblNewLabel_1_1_1_3, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_1_1.createSequentialGroup()
							.addGap(76)
							.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel_1_1.setLayout(gl_panel_1_1);

		JPanel panel_1_1_1 = new JPanel();
		tabbedPane.addTab("Addressed Information", null, panel_1_1_1, null);

		JLabel lblNewLabel_1_2_1 = new JLabel("Full Name:");
		lblNewLabel_1_2_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_1_2_1.setFont(new Font("Tahoma", Font.PLAIN, 14));

		pt2NameTF = new JTextField();
		pt2NameTF.setColumns(10);

		JLabel lblNewLabel_1_1_1_2_1 = new JLabel("Address:");
		lblNewLabel_1_1_1_2_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_1_1_1_2_1.setFont(new Font("Tahoma", Font.PLAIN, 14));

		pt2AddressTF = new JTextField();
		pt2AddressTF.setColumns(10);

		JLabel lblNewLabel_1_1_1_4_1 = new JLabel("Phone:");
		lblNewLabel_1_1_1_4_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_1_1_1_4_1.setFont(new Font("Tahoma", Font.PLAIN, 14));

		pt2PhoneTF = new JTextField();
		pt2PhoneTF.setColumns(10);

		pt2FaxTF = new JTextField();
		pt2FaxTF.setColumns(10);

		JLabel lblNewLabel_1_1_1_4_2_1 = new JLabel("Fax:");
		lblNewLabel_1_1_1_4_2_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_1_1_1_4_2_1.setFont(new Font("Tahoma", Font.PLAIN, 14));

		final JComboBox comboBox = new JComboBox();
		comboBox.addItem("");
		comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(comboBox.getSelectedItem() != "") {
					String selectedItem = comboBox.getSelectedItem().toString();
					pt2NameTF.setText(selectedItem);
					pt2AddressTF.setText(controller.contactsList.get(selectedItem).split("\\|")[0]);
					pt2PhoneTF.setText(controller.contactsList.get(selectedItem).split("\\|")[1]);
				}

			}
		});

		for (String s : controller.contactsList.keySet()) {
			
			comboBox.addItem(s);
		}

		JLabel lblNewLabel_1_1_1_4_2_1_1 = new JLabel("Saved:");
		lblNewLabel_1_1_1_4_2_1_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_1_1_1_4_2_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GroupLayout gl_panel_1_1_1 = new GroupLayout(panel_1_1_1);
		gl_panel_1_1_1.setHorizontalGroup(
			gl_panel_1_1_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1_1_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1_1_1.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel_1_1_1.createSequentialGroup()
							.addComponent(lblNewLabel_1_2_1, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(pt2NameTF, GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE))
						.addGroup(gl_panel_1_1_1.createSequentialGroup()
							.addComponent(lblNewLabel_1_1_1_2_1, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
							.addGap(4)
							.addComponent(pt2AddressTF, GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE))
						.addGroup(gl_panel_1_1_1.createSequentialGroup()
							.addComponent(lblNewLabel_1_1_1_4_1, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
							.addGap(4)
							.addComponent(pt2PhoneTF, GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE))
						.addGroup(gl_panel_1_1_1.createSequentialGroup()
							.addComponent(lblNewLabel_1_1_1_4_2_1, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
							.addGap(4)
							.addComponent(pt2FaxTF, GroupLayout.PREFERRED_SIZE, 218, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_1_1_1.createSequentialGroup()
							.addComponent(lblNewLabel_1_1_1_4_2_1_1, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(comboBox, 0, 218, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_panel_1_1_1.setVerticalGroup(
			gl_panel_1_1_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1_1_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1_1_1.createParallelGroup(Alignment.BASELINE)
						.addGroup(gl_panel_1_1_1.createSequentialGroup()
							.addGap(3)
							.addComponent(lblNewLabel_1_2_1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addComponent(pt2NameTF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1_1_1.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel_1_1_1_2_1, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_1_1_1.createSequentialGroup()
							.addGap(1)
							.addComponent(pt2AddressTF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1_1_1.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel_1_1_1_4_1, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_1_1_1.createSequentialGroup()
							.addGap(1)
							.addComponent(pt2PhoneTF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1_1_1.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel_1_1_1_4_2_1, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_1_1_1.createSequentialGroup()
							.addGap(1)
							.addComponent(pt2FaxTF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(56)
					.addGroup(gl_panel_1_1_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_1_1_1_4_2_1_1, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		panel_1_1_1.setLayout(gl_panel_1_1_1);

		JLabel lblNewLabel = new JLabel("Select a Template:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		scrollPane.setColumnHeaderView(lblNewLabel);

		fileTree = new JTree();
		scrollPane.setViewportView(fileTree);

		panel.setLayout(gl_panel);
		
		progressBar = new JProgressBar();
		
		JLabel lblNewLabel_1_3 = new JLabel("Do not open the Desktop shortcut when printing, it will lag the program!");
		lblNewLabel_1_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_3.setFont(new Font("Tahoma", Font.PLAIN, 9));
		GroupLayout gl_buttonPane = new GroupLayout(buttonPane);
		gl_buttonPane.setHorizontalGroup(
			gl_buttonPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_buttonPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_buttonPane.createParallelGroup(Alignment.LEADING)
						.addComponent(progressBar, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 313, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_1_3, GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(printButton, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
					.addGap(0))
		);
		gl_buttonPane.setVerticalGroup(
			gl_buttonPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_buttonPane.createSequentialGroup()
					.addGroup(gl_buttonPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_buttonPane.createParallelGroup(Alignment.BASELINE)
							.addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
							.addComponent(printButton, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE))
						.addGroup(Alignment.TRAILING, gl_buttonPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(1)
							.addComponent(lblNewLabel_1_3, GroupLayout.PREFERRED_SIZE, 11, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_buttonPane.linkSize(SwingConstants.VERTICAL, new Component[] {printButton, cancelButton});
		gl_buttonPane.linkSize(SwingConstants.HORIZONTAL, new Component[] {printButton, cancelButton});
		buttonPane.setLayout(gl_buttonPane);
		getContentPane().setLayout(groupLayout);

		getTemplates();
		getUserInfo();
		
	}

	private void getUserInfo() {
		
		userNameTF.setText(controller.userName);
		userTitleTF.setText(controller.userTitle);
		
	}

	public void createPatientClass() {

		patient = new Person(ptNameTF.getText(), ptDOBTF.getText(), ptIDTF.getText(), ptAddressTF.getText(), ptPhoneTF.getText(), ptFaxTF.getText());
		addressee = new Person(pt2NameTF.getText(), pt2AddressTF.getText(), pt2PhoneTF.getText(), pt2FaxTF.getText());

	}

	public void getTemplates() {

		File fileLocation;
		File[] filesList;
		final ArrayList<DefaultMutableTreeNode> nodes = new ArrayList();

		try {
			fileLocation = new File(Controller.templatesFolderLocation);
			filesList = fileLocation.listFiles();

		} catch (Exception e) {
			return;
		}

		for (File x : filesList) {

			nodes.add(new DefaultMutableTreeNode(x));

		}

		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("templates");

		for(DefaultMutableTreeNode x : nodes) {
			rootNode.add(new DefaultMutableTreeNode(x));
		}

		fileTree.setModel(new DefaultTreeModel(rootNode));

	}

	public void printTemplate() {

		if(fileTree.getLastSelectedPathComponent() != null) {

			createPatientClass();

			HashMap<String, String> printElements = new HashMap();

			printElements.put("{{userName}}", controller.userName);
			printElements.put("{{userTitle}}", controller.userTitle);

			printElements.put("{{locationOfCare.name}}", controller.locationOfCare.name);
			printElements.put("{{locationOfCare.address}}", controller.locationOfCare.address);
			printElements.put("{{locationOfCare.phone}}", controller.locationOfCare.phone);
			printElements.put("{{locationOfCare.fax}}", controller.locationOfCare.fax);

			printElements.put("{{patient.name}}", patient.name);
			printElements.put("{{patient.DOB}}", patient.DOB);
			printElements.put("{{patient.id}}", patient.id);
			printElements.put("{{patient.address}}", patient.address);
			printElements.put("{{patient.phone}}", patient.phone);
			printElements.put("{{patient.fax}}", patient.fax);

			printElements.put("{{addressee.name}}", addressee.name);
			printElements.put("{{addressee.address}}", addressee.address);
			printElements.put("{{addressee.phone}}", addressee.phone);
			printElements.put("{{addressee.fax}}", addressee.fax);
			

			ArrayList<String> rowData = new ArrayList(tablePane.tableMain.getRowCount());
			String rowString;

			//rowData "cptCode|description|codeCost|copayAmnt|coinsuranceAmnt|toDeductible|remainingDeductibletoMeet|rowTotal" last index will only be the final total.

			for(int i = 0; i < tablePane.tableMain.getRowCount(); i++) {
				rowString = "";
				for (int j = 0; j < tablePane.tableMain.getColumnCount(); j++) {
					if(j == 1) {
						rowString += controller.codeDescriptions.get(tablePane.tableMain.getValueAt(i,0).toString()) + "|";
						rowString += tablePane.tableMain.getValueAt(i,1).toString() + "|";
						continue;
					}
					if((j+1) != tablePane.tableMain.getColumnCount()){
						rowString += tablePane.tableMain.getValueAt(i,j).toString() + "|";
					}else{
						rowString += tablePane.tableMain.getValueAt(i,j).toString();
					}
					
				}

				rowData.add(rowString);

			}


			//System.out.println("(printTemplate) " + rowData);
			//System.out.println("(printTemplate) " + fileTree.getLastSelectedPathComponent());

			Document document;


			document = new Document(fileTree.getLastSelectedPathComponent().toString());


			for (HashMap.Entry<String, String> entry : printElements.entrySet()) {

				document.replace(entry.getKey(), entry.getValue(),false,true);

			}

			try {
				if(tablePane.tableMain.getRowCount() > 0) {
					TextSelection tableSelection = document.findString("{{table}}", true, true);

					Section section = document.getSections().get(0);
					section.getTables().get(0);
					Table costTable = section.addTable();
					costTable.applyStyle("Plain Table 4");

					TextRange range = tableSelection.getAsOneRange();
					Paragraph paragraph = range.getOwnerParagraph();
					Body body = paragraph.ownerTextBody();
					int index = body.getChildObjects().indexOf(paragraph);

					costTable.resetCells(tablePane.tableMain.getRowCount() + 1, 4);
					body.getChildObjects().remove(paragraph);
					body.getChildObjects().insert(index, costTable);

					costTable.get(0, 0).addParagraph().setText("Service Requested");
					costTable.get(0, 1).addParagraph().setText("Service Description");
			        costTable.get(0, 2).addParagraph().setText("Full Undiscounted Fee");
			        costTable.get(0, 3).addParagraph().setText("Contractual Discounted Fee");
			       
			       
			        for(int i = 0; i < rowData.size(); i++) {
			        	String[] tempRowData = rowData.get(i).split("\\|");
			        	
			        	//Set "Service Requested" Column

						costTable.get(i + 1, 0).addParagraph().setText(tempRowData[0]);
						
						//Set "Service Description" Column
						
						costTable.get(i + 1, 1).addParagraph().setText(tempRowData[1]);
						costTable.get(i + 1, 1).setWidth(160);
						
						//Set "Full Undiscounted Fee" Column & Format Amount
						
						costTable.get(i + 1, 2).addParagraph().setText("$ " + String.format("%.2f", Double.valueOf(tempRowData[2])));
						
						//Set "Contractual Discounted Fee" Column & Format Amount
						 
						costTable.get(i + 1, 3).addParagraph().setText("$ " + String.format("%.2f", Double.valueOf(tempRowData[tempRowData.length-1])));
					}
					costTable.addRow();
					costTable.get(costTable.getRows().getCount() - 1, 3).addParagraph().setText("Estimated Total Due: $" + String.format("%.2f", tablePane.finalTotal));
					
					
			    	}
				
				
				controller.fileChooser.setCurrentDirectory(new File("S:\\"));
				controller.fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);;
				controller.fileChooser.setDialogTitle("Save As");
		    	returnResult = controller.fileChooser.showSaveDialog(null);
		    	if(returnResult == JFileChooser.APPROVE_OPTION) {
		    		File saveFile = controller.fileChooser.getSelectedFile();
		    		document.saveToFile(saveFile + ".pdf");
		    		
				}
		    	progressBar.setValue(0);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			

		}

	}
	
	public class MyThread extends Thread {
		
		public void run() {
			int i = 60;
			
			//How long before the printer automatically closes (in case of lag)
			
			int timeOut = 40;
			while((i < 90 || timeOut != 0) && returnResult == -1) {
				try {
					progressBar.setValue(i);
					
					
					if(!controller.fileChooser.isFocusable()) {
						i += 30;	
					}else {
						progressBar.setValue(100);
					}
					
					if(controller.fileChooser.isFocusable()) {
						timeOut--;
					}
					
					
					if(timeOut == 0) {
						System.out.println("Timeout reached, closing filechooser");
						controller.fileChooser.cancelSelection();
						returnResult = JFileChooser.CANCEL_OPTION;
						break;
					}
					
					Thread.sleep(1000);
				} catch (Exception e) {
				// TODO: handle exception
				}
				
			}
			
		}
		
	}
}

