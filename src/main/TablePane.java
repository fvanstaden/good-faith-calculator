package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import com.spire.doc.Column;

import net.miginfocom.swing.MigLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultComboBoxModel;


public class TablePane extends JPanel{

	DecimalFormat rt = new DecimalFormat("0.00###########");

	public JTable tableMain;
	private JComboBox insuranceCBX;
	private JComboBox codeCBX;
	private JComboBox favoritesCBX;
	public double enteredDeductible;
	private JTextField deductibleTextField;
	private JLabel displayDeductibleTextField;
	public JLabel totalLabel;
	public double finalTotal;
	private Controller controller;

	private long previousCalculateCall;
	private JLabel insuranceCount;
	private JLabel codeCount;
	private JLabel favoritesCount;
	private ArrayList<Integer> columnWidths = new ArrayList<Integer>();
	
	private boolean debugMode = false;

	public TablePane(Controller x) {
		controller = x;
		
		setLayout(new MigLayout("", "[900\r\npx,grow,left]", "[top][bottom][40px:n]"));

		JPanel panel = new JPanel();
		panel.setBackground(new Color(240, 240, 240));
		add(panel, "cell 0 0,grow");

		JPanel insurancePanel = new JPanel();
		insurancePanel.setFocusable(false);

		JPanel codePanel = new JPanel();
		codePanel.setFocusable(false);

		JLabel lblNewLabel_1_1_1 = new JLabel("CPT Code:  ");
		lblNewLabel_1_1_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));

		codeCBX = new JComboBox();
		codeCBX.setAlignmentX(Component.RIGHT_ALIGNMENT);
		codeCBX.setFont(new Font("Tahoma", Font.PLAIN, 14));
		codeCBX.setName("");
		codeCBX.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		JButton btnNewButton = new JButton("Add Code\r\n");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				addRowNormal();
			}
		});
		
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 12));

		codeCount = new JLabel("X\r\n");
		codeCount.setHorizontalAlignment(SwingConstants.LEFT);
		codeCount.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GroupLayout gl_codePanel = new GroupLayout(codePanel);
		gl_codePanel.setHorizontalGroup(
			gl_codePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_codePanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel_1_1_1, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(codeCBX, GroupLayout.PREFERRED_SIZE, 487, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(codeCount, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_codePanel.setVerticalGroup(
			gl_codePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_codePanel.createSequentialGroup()
					.addGroup(gl_codePanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_codePanel.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblNewLabel_1_1_1)
							.addComponent(codeCBX, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnNewButton))
						.addComponent(codeCount, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(11, Short.MAX_VALUE))
		);
		codePanel.setLayout(gl_codePanel);

		JPanel favoritesPanel = new JPanel();
		favoritesPanel.setFocusable(false);

		JLabel lblNewLabel_1_1_2 = new JLabel("Favorites:  ");
		lblNewLabel_1_1_2.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_1_1_2.setFont(new Font("Tahoma", Font.PLAIN, 14));

		favoritesCBX = new JComboBox();
		favoritesCBX.setAlignmentX(Component.RIGHT_ALIGNMENT);
		favoritesCBX.setFont(new Font("Tahoma", Font.PLAIN, 14));
		favoritesCBX.setName("");
		favoritesCBX.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		JButton btnAddFavorite = new JButton("Add Favorite\r\n");
		btnAddFavorite.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				addRowFavorite();
			}
		});
		
		btnAddFavorite.setFont(new Font("Tahoma", Font.PLAIN, 12));

		favoritesCount = new JLabel("Y");
		favoritesCount.setHorizontalAlignment(SwingConstants.LEFT);
		favoritesCount.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GroupLayout gl_favoritesPanel = new GroupLayout(favoritesPanel);
		gl_favoritesPanel.setHorizontalGroup(
			gl_favoritesPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_favoritesPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel_1_1_2, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(favoritesCBX, GroupLayout.PREFERRED_SIZE, 487, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(favoritesCount, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnAddFavorite, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(13, Short.MAX_VALUE))
		);
		gl_favoritesPanel.setVerticalGroup(
			gl_favoritesPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_favoritesPanel.createSequentialGroup()
					.addGroup(gl_favoritesPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_favoritesPanel.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblNewLabel_1_1_2)
							.addComponent(favoritesCBX, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
						.addComponent(favoritesCount, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnAddFavorite))
					.addContainerGap(11, Short.MAX_VALUE))
		);
		favoritesPanel.setLayout(gl_favoritesPanel);

		JPanel deducPanel = new JPanel();
		deducPanel.setFocusable(false);

		deductibleTextField = new JTextField();
		deductibleTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					enteredDeductible = Double.parseDouble(getDeductibleTextFieldText());
					setDisplayDeductibleTextFieldText("Deductible: $ " + getDeductibleTextFieldText());
					displayDeductibleTextField.setForeground(Color.black);

				}
				catch(Exception x){
					enteredDeductible = 0.00;
					displayDeductibleTextField.setForeground(Color.red);
					displayDeductibleTextField.setText("0.00");
					setDisplayDeductibleTextFieldText("Deductible: $ 0.00");
				}
				calculateTotal();
			}
		});


		deductibleTextField.setAlignmentX(Component.RIGHT_ALIGNMENT);
		deductibleTextField.setName("");
		deductibleTextField.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		
		JLabel lblNewLabel_1_1_2_1 = new JLabel("Deductible:");
		lblNewLabel_1_1_2_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_1_1_2_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JButton btnNewButton_1_1_1_1 = new JButton("Toggle Deduc.");
		btnNewButton_1_1_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton_1_1_1_1.setToolTipText("Toggles the 'Affects Deduc.' for all rows.");
		
		
		btnNewButton_1_1_1_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				

				for(int i = 0; i < tableMain.getRowCount(); i++) {
					
					Boolean cuurentValueBoolean = (Boolean) tableMain.getValueAt(i, 2);
					
					
					tableMain.setValueAt(!(cuurentValueBoolean), i, 2);
				
					
				}
				calculateTotal();
				
			}
		});
		
		btnNewButton_1_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GroupLayout gl_deducPanel = new GroupLayout(deducPanel);
		gl_deducPanel.setHorizontalGroup(
			gl_deducPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_deducPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel_1_1_2_1, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(deductibleTextField, GroupLayout.PREFERRED_SIZE, 214, GroupLayout.PREFERRED_SIZE)
					.addGap(485)
					.addComponent(btnNewButton_1_1_1_1, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_deducPanel.setVerticalGroup(
			gl_deducPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_deducPanel.createSequentialGroup()
					.addGroup(gl_deducPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_1_1_2_1, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(deductibleTextField, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton_1_1_1_1))
					.addContainerGap(10, Short.MAX_VALUE))
		);
		deducPanel.setLayout(gl_deducPanel);
		panel.setLayout(new GridLayout(0, 1, 0, 0));

		JLabel lblNewLabel_1_1 = new JLabel("Insurance:  ");
		lblNewLabel_1_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));

		insuranceCBX = new JComboBox();
		
		insuranceCBX.addItemListener(evt -> {
			insuranceCarrierChangedEvent();
		});


		insuranceCBX.setAlignmentX(Component.RIGHT_ALIGNMENT);
		insuranceCBX.setFont(new Font("Tahoma", Font.PLAIN, 14));
		insuranceCBX.setName("");
		insuranceCBX.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		insuranceCount = new JLabel("");
		insuranceCount.setHorizontalAlignment(SwingConstants.LEFT);
		insuranceCount.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GroupLayout gl_insurancePanel = new GroupLayout(insurancePanel);
		gl_insurancePanel.setHorizontalGroup(
			gl_insurancePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_insurancePanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel_1_1, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(insuranceCBX, GroupLayout.PREFERRED_SIZE, 487, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(insuranceCount, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(153, Short.MAX_VALUE))
		);
		gl_insurancePanel.setVerticalGroup(
			gl_insurancePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_insurancePanel.createSequentialGroup()
					.addGroup(gl_insurancePanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_1_1)
						.addComponent(insuranceCBX, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addComponent(insuranceCount, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		insurancePanel.setLayout(gl_insurancePanel);
		panel.add(insurancePanel);
		panel.add(codePanel);
		panel.add(favoritesPanel);
		panel.add(deducPanel);

		JPanel deducPanel2 = new JPanel();
		deducPanel2.setFocusable(false);
		panel.add(deducPanel2);

		displayDeductibleTextField = new JLabel("Deductible: $ 0.00");
		displayDeductibleTextField.setForeground(Color.RED);
		displayDeductibleTextField.setHorizontalAlignment(SwingConstants.LEFT);
		displayDeductibleTextField.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JButton btnNewButton_1 = new JButton("Remove Row(s)");
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(tableMain.getRowCount() == 0) {return;}
				DefaultTableModel model = (DefaultTableModel) tableMain.getModel();
				int[] selectedRows = tableMain.getSelectedRows();
				for(int i = selectedRows.length - 1; i >= 0 ; i -- ) {
					model.removeRow(selectedRows[i]);
				}
				if(selectedRows.length == 0) {

					model.removeRow(tableMain.getRowCount() -1);

				}
				
			}
		});
		
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 11));

		JButton btnNewButton_1_1 = new JButton("Share (%)");
		btnNewButton_1_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				
				String coInsuranceAmnt = null;
				for(int i = 0; i < tableMain.getRowCount(); i++) {
					
					if(coInsuranceAmnt == null && !tableMain.getValueAt(i, 4).toString().trim().matches("0")) {
						//Find the first non-zero value
						coInsuranceAmnt = tableMain.getValueAt(i, 4).toString().trim();
						i = 0;
					}
					
					tableMain.setValueAt(coInsuranceAmnt, i , 4);
					
				}
				calculateTotal();
				
			}
		});
		
		btnNewButton_1_1.setToolTipText("Applies the first non-zero (%) value to all other rows.");
		btnNewButton_1_1.setFont(new Font("Tahoma", Font.PLAIN, 11));
		
		JButton btnNewButton_1_1_1 = new JButton("Recalculate");
		btnNewButton_1_1_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				calculateTotal();
			}
		});
	
		btnNewButton_1_1_1.setToolTipText("Forces a recalculate.");
		btnNewButton_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GroupLayout gl_deducPanel2 = new GroupLayout(deducPanel2);
		gl_deducPanel2.setHorizontalGroup(
			gl_deducPanel2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_deducPanel2.createSequentialGroup()
					.addContainerGap()
					.addComponent(displayDeductibleTextField, GroupLayout.PREFERRED_SIZE, 319, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 184, Short.MAX_VALUE)
					.addComponent(btnNewButton_1_1_1, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnNewButton_1_1, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnNewButton_1, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_deducPanel2.setVerticalGroup(
			gl_deducPanel2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_deducPanel2.createSequentialGroup()
					.addGroup(gl_deducPanel2.createParallelGroup(Alignment.BASELINE)
						.addComponent(displayDeductibleTextField)
						.addComponent(btnNewButton_1)
						.addComponent(btnNewButton_1_1)
						.addComponent(btnNewButton_1_1_1))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		deducPanel2.setLayout(gl_deducPanel2);

		JScrollPane scrollPane = new JScrollPane();

		add(scrollPane, "cell 0 1,grow");

		tableMain = new JTable();
		tableMain.setFont(new Font("Tahoma", Font.PLAIN, 14));
		scrollPane.setViewportView(tableMain);
		tableMain.getTableHeader().setReorderingAllowed(false);
		tableMain.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"CPT Code", "Cost", "Affects Deduc.", "Co-Pay", "(%) Co-Insurance", "To Deduc.", "Remaining Deduc.", "To Total"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, String.class, Boolean.class, String.class, String.class, String.class, String.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, true, true, true, true, true, true, true
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		tableMain.getColumnModel().getColumn(0).setResizable(false);
		tableMain.getColumnModel().getColumn(0).setPreferredWidth(70);
		tableMain.getColumnModel().getColumn(0).setMinWidth(0);
		tableMain.getColumnModel().getColumn(1).setResizable(false);
		tableMain.getColumnModel().getColumn(1).setPreferredWidth(70);
		tableMain.getColumnModel().getColumn(1).setMinWidth(0);
		tableMain.getColumnModel().getColumn(2).setResizable(false);
		tableMain.getColumnModel().getColumn(2).setPreferredWidth(90);
		tableMain.getColumnModel().getColumn(2).setMinWidth(0);
		tableMain.getColumnModel().getColumn(3).setResizable(false);
		tableMain.getColumnModel().getColumn(3).setPreferredWidth(70);
		tableMain.getColumnModel().getColumn(3).setMinWidth(0);
		tableMain.getColumnModel().getColumn(4).setResizable(false);
		tableMain.getColumnModel().getColumn(4).setPreferredWidth(90);
		tableMain.getColumnModel().getColumn(4).setMinWidth(0);
		tableMain.getColumnModel().getColumn(5).setResizable(false);
		tableMain.getColumnModel().getColumn(5).setPreferredWidth(70);
		tableMain.getColumnModel().getColumn(5).setMinWidth(0);
		tableMain.getColumnModel().getColumn(6).setResizable(false);
		tableMain.getColumnModel().getColumn(6).setPreferredWidth(100);
		tableMain.getColumnModel().getColumn(6).setMinWidth(0);
		tableMain.getColumnModel().getColumn(7).setResizable(false);
		tableMain.getColumnModel().getColumn(7).setMinWidth(0);
		
		for (int i = 0; i < tableMain.getColumnCount(); i++) {
			//Store Preferred Widths so that they can be referenced to restore columns after attrny insurance selection
			columnWidths.add(tableMain.getColumnModel().getColumn(i).getPreferredWidth());
		}
		//ADD LISTENER TO CALL CALCULATE TOTAL AUTO...

		tableMain.getModel().addTableModelListener(new TableModelListener() {

			@Override
			public void tableChanged(TableModelEvent e) {
				
				if(e.getType() == e.INSERT) {
					if(debugMode) {System.out.println("Inserted Row, Updating...");}
					calculateTotal();
				}
				
				if(e.getType() == TableModelEvent.UPDATE) {
					
					if(System.currentTimeMillis() - previousCalculateCall < 5 || tableMain.getRowCount() == 0) {
						if(debugMode) {System.out.println("Refusing function call");} 
						previousCalculateCall = System.currentTimeMillis();
			        	return;
					}
					previousCalculateCall = System.currentTimeMillis();
					if(debugMode) {System.out.println("Changed Row, Updating..." + e.getColumn());} 
					calculateTotal();
				}
				
				if(e.getType() == e.DELETE) {
					if(debugMode) {System.out.println("Deleted Row, Updating...");} 
					calculateTotal();
				}
				
			}
		});

		JPanel panel_1 = new JPanel();
		add(panel_1, "cell 0 2,grow");
		panel_1.setLayout(null);

		totalLabel = new JLabel("Patient Pays: $");
		totalLabel.setHorizontalAlignment(SwingConstants.LEFT);
		totalLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		totalLabel.setBounds(509, 10, 257, 25);
		panel_1.add(totalLabel);

		//POPULATE CBX FUNCTION

		insuranceCBX.addItem("");
		codeCBX.addItem("");
		favoritesCBX.addItem("");
		
		if(controller.insuranceNames == null) {return;}

		for (String s : controller.insuranceNames) {
			insuranceCBX.addItem(s);
		}


		//--END--
	}

	public void addRowNormal() {
		
		if(codeCBX.getSelectedItem() != null) {
			
			String selectedCode = codeCBX.getSelectedItem().toString().split(" \\| ")[0];
			String selectedInsurance = insuranceCBX.getSelectedItem().toString();

			if(selectedCode != "" && selectedInsurance != "") {

				DefaultTableModel model = (DefaultTableModel) tableMain.getModel();

				Vector<Object> tempVector = new Vector();

				tempVector.add(selectedCode);
				tempVector.add(controller.codes.get(selectedCode).get(selectedInsurance).get(1));
				tempVector.add(false);
				tempVector.add(0);
				tempVector.add(0);

				model.addRow(tempVector);

				calculateTotal();

			}
			
		}

		
	}

	public void addRowFavorite() {
		
		if(favoritesCBX.getSelectedItem() != null) {
			
			String selectedCode = favoritesCBX.getSelectedItem().toString().split(" \\| ")[0];
			String selectedInsurance = insuranceCBX.getSelectedItem().toString();

			if(selectedCode != "" && selectedInsurance != "") {
				DefaultTableModel model = (DefaultTableModel) tableMain.getModel();

				Vector tempVector = new Vector();

				tempVector.add(selectedCode);
				tempVector.add(controller.codes.get(selectedCode).get(selectedInsurance).get(1));
				tempVector.add(false);
				tempVector.add(0);
				tempVector.add(0);

				model.addRow(tempVector);

				calculateTotal();

			}
		}

	}

	public void calculateTotal() {

		previousCalculateCall = System.currentTimeMillis();

		DefaultTableModel tableModel = (DefaultTableModel) tableMain.getModel();

		finalTotal = 0.0;
        Double remainingDeductible = enteredDeductible;
        
        //This for loop makes sure any empty cells have some value so they can always be converted to a Double.
        for(int i = 0; i < tableModel.getRowCount(); i++){
            for(int j = 0; j < tableModel.getColumnCount(); j++){
                if(tableModel.getValueAt(i, j) == null || String.valueOf((tableModel.getValueAt(i, j))).equalsIgnoreCase("")){
                	tableModel.setValueAt(0, i ,j);
                }
            }

            ArrayList<Object> rowInfo = new ArrayList();

            for(int b = 1; b < tableMain.getColumnCount(); b++) {
            	if(tableModel.getValueAt(i, b) == null || String.valueOf((tableModel.getValueAt(i, b))).equalsIgnoreCase("")){
            		rowInfo.add(0);
            	}else {
            		rowInfo.add(tableModel.getValueAt(i, b));
            	}


            }
            
           
            //rowInfo [0] is cost ... [4] is deductible met amt
            double cost = Double.parseDouble(String.valueOf(rowInfo.get(0)));
            boolean countsToDeduc = Boolean.parseBoolean(String.valueOf(rowInfo.get(1)));
            double copay = insuranceCBX.getSelectedItem().toString().indexOf("(Attorney)") == -1 ? Double.parseDouble(String.valueOf(rowInfo.get(2))) : 0;
            double coinsurancePercent = Double.parseDouble(String.valueOf(rowInfo.get(3)));
            double deductibleMetAmount = Double.parseDouble(String.valueOf(rowInfo.get(4)));
            double rowTotal = 0;

            tableModel.setValueAt("0", i, 5);
            tableModel.setValueAt("0", i, 6);

            if(enteredDeductible != 0 && countsToDeduc && copay == 0){
                if(cost >= remainingDeductible){
                    deductibleMetAmount = Math.abs(remainingDeductible - cost);
                    tableModel.setValueAt(String.format("%.2f", remainingDeductible), i, 5);
                    remainingDeductible = 0.00;
                }if(cost < remainingDeductible){
                    remainingDeductible = remainingDeductible - cost;
                    rowTotal = cost;
                    tableModel.setValueAt(round(cost), i, 5);
                    tableModel.setValueAt(String.format("%.2f", remainingDeductible), i, 6);
                }if(remainingDeductible == 0.00) {
                    if (coinsurancePercent != 0.00) {
                        rowTotal = calculateCoinsurance(coinsurancePercent, deductibleMetAmount);
                    } else {
                        rowTotal = deductibleMetAmount;
                    }
                    rowTotal += Double.parseDouble(String.valueOf(tableModel.getValueAt(i, 5)));

                }
            }else{
                if(coinsurancePercent != 0.00){
                	//Add a && countsToDeduc to remove the ability to have a row be affected by coins without a deductible
                    rowTotal = calculateCoinsurance(coinsurancePercent, cost);
                }else if(copay == 0){
                    rowTotal = cost;
                }
            }

            if(copay != 0.00){rowTotal = copay;}
            rowTotal = round(rowTotal);
            tableModel.setValueAt(String.format("%.2f", rowTotal),i,7);
            finalTotal += rowTotal;

        }

        finalTotal = round(finalTotal);
        totalLabel.setText("Patient Pays: $" + String.format("%.2f", finalTotal));

    }
	
	private void insuranceCarrierChangedEvent() 
	{
		if(insuranceCBX.getSelectedItem() == null) {
			return;
		}
		else {
			
			codeCBX.setEnabled(true);
			favoritesCBX.setEnabled(true);

			codeCBX.removeAllItems();
			favoritesCBX.removeAllItems();
			
			if(!insuranceCBX.getSelectedItem().toString().trim().equals("")) {

				if(controller.codeDescriptions == null) {return;}

				for (String s : controller.codeDescriptions.keySet()) {
					
					String cost = controller.codes.get(s.split(" \\| ")[0]).get(insuranceCBX.getSelectedItem().toString()).get(1).toString().trim();
					
					if((cost == "" || cost == "0" || cost == null) && controller.hideCodesSetting){

						//codeCBX.addItem(s);

					}else {
						codeCBX.addItem(s + " | " + controller.codeDescriptions.get(s));
		
					}
				}

				codeCount.setText("(" + codeCBX.getItemCount() + ")");

				for (String s : controller.favoriteCodeDescriptions.keySet()) {
					if(insuranceCBX.getSelectedItem() != null) {
						String cost = controller.codes.get(s.split(" \\| ")[0]).get(insuranceCBX.getSelectedItem().toString()).get(1).toString().trim();
						if((cost == "" || cost == "0" || cost == null) && controller.hideCodesSetting){

							//favoritesCBX.addItem(s);

						}else {
							favoritesCBX.addItem(s + " | " + controller.favoriteCodeDescriptions.get(s));

						}
					}
				}

				favoritesCount.setText("(" + favoritesCBX.getItemCount() + ")");

			}else {
				codeCount.setText("(0)");
				favoritesCount.setText("(0)");
				codeCBX.setEnabled(false);
				favoritesCBX.setEnabled(false);
			}
		}
		
		
		if(tableMain != null) {
			
			if(insuranceCBX.getSelectedItem() != null && insuranceCBX.getSelectedItem().toString().indexOf("(Attorney)") != -1) {
				
				deductibleTextField.setEnabled(false);
				deductibleTextField.setText("");
				
				tableMain.getColumnModel().getColumn(2).setMaxWidth(0);
				tableMain.getColumnModel().getColumn(3).setMaxWidth(0);
				tableMain.getColumnModel().getColumn(5).setMaxWidth(0);
				tableMain.getColumnModel().getColumn(6).setMaxWidth(0);
				tableMain.getColumnModel().getColumn(4).setHeaderValue("(%) Discount Amount");
				
			}else {
				
				deductibleTextField.setEnabled(true);
				
				tableMain.getColumnModel().getColumn(2).setMaxWidth(2147483647);
				tableMain.getColumnModel().getColumn(2).setPreferredWidth(columnWidths.get(3));
				
				tableMain.getColumnModel().getColumn(3).setMaxWidth(2147483647);
				tableMain.getColumnModel().getColumn(3).setPreferredWidth(columnWidths.get(3));
				
				tableMain.getColumnModel().getColumn(5).setMaxWidth(2147483647);
				tableMain.getColumnModel().getColumn(5).setPreferredWidth(columnWidths.get(5));
				
				tableMain.getColumnModel().getColumn(6).setMaxWidth(2147483647);
				tableMain.getColumnModel().getColumn(6).setPreferredWidth(columnWidths.get(6));
				
				tableMain.getColumnModel().getColumn(4).setHeaderValue("(%) Co-Insurance");
				
			}
			
			calculateTotal();
		}
		
	}

	private double calculateCoinsurance(Double percent, Double amount){
        return (percent / 100) * amount;
    }

	private Double round(Double n){return Double.parseDouble(rt.format(n));}

	public String getDeductibleTextFieldText() {
		return deductibleTextField.getText();
	}
	public void setDeductibleTextFieldText(String text) {
		deductibleTextField.setText(text);
	}
	public String getDisplayDeductibleTextFieldText() {
		return displayDeductibleTextField.getText();
	}
	public void setDisplayDeductibleTextFieldText(String text_1) {
		displayDeductibleTextField.setText(text_1);
	}
	public TableModel getTableModel() {
		return tableMain.getModel();
	}
	public void setTableModel(TableModel model) {
		tableMain.setModel(model);
	}
	public JTextField getDeductibleTextField() {
		return deductibleTextField;
	}
}
