package main;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.JavaBean;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

public class MainWindow extends JFrame {

	private JPanel mainPane;
	private JTabbedPane tabbedPane;
	public Controller controller;

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() { 

				try {
					MainWindow frame = new MainWindow();
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					SwingUtilities.updateComponentTreeUI(frame);
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public MainWindow() {
		
		controller = new Controller();
		
		setTitle("Estimator (Java Version: " + System.getProperty("java.version") + ") ");

		setResizable(false);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 841, 812);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(SystemColor.menu);
		setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("Tab Options");
		mnNewMenu.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		mnNewMenu.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
			}
		});
		mnNewMenu.setHorizontalAlignment(SwingConstants.LEFT);
		menuBar.add(mnNewMenu);

		JMenuItem mntmNewMenuItem = new JMenuItem("Add Tab");
		mntmNewMenuItem.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				addTab();
			}
		});

		mnNewMenu.add(mntmNewMenuItem);

		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Remove Tab\r\n");
		mntmNewMenuItem_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(tabbedPane.getTabCount() == 1) {
					tabbedPane.removeTabAt(tabbedPane.getSelectedIndex());
					addTab();
				}else {
					tabbedPane.removeTabAt(tabbedPane.getSelectedIndex());
					for(int i = 0; i < tabbedPane.getTabCount(); i++) {
						tabbedPane.setTitleAt(i, "Tab: " + (i+1));
					}

				}

			}
		});

		mnNewMenu.add(mntmNewMenuItem_1);

		JMenu mnOptions = new JMenu("Settings");
		mnOptions.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		mnOptions.setHorizontalAlignment(SwingConstants.CENTER);
		menuBar.add(mnOptions);

		JCheckBoxMenuItem mntmNewMenuItem_3 = new JCheckBoxMenuItem("Hide unpriced codes");
		mntmNewMenuItem_3.setToolTipText("Hides a majority of CPT codes with 0 or missing prices. (Insurance Specific)");
		mntmNewMenuItem_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.hideCodesSetting = !controller.hideCodesSetting;
			}
		});
		mnOptions.add(mntmNewMenuItem_3);

		JMenuItem mntmNewMenuItem_2 = new JMenuItem("Print Current Table");
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PrinterWindow printerWindow = new PrinterWindow(controller);
				try {
					printerWindow.tablePane = (TablePane) tabbedPane.getSelectedComponent();
				} catch (Exception e2) {
					System.out.println("(Print Current Table)" + e2);
				}

			}
		});
		mnOptions.add(mntmNewMenuItem_2);
		mainPane = new JPanel();
		mainPane.setBackground(SystemColor.inactiveCaption);
		mainPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(mainPane);

		tabbedPane = new JTabbedPane(SwingConstants.TOP);
		tabbedPane.setFont(new Font("Tahoma", Font.PLAIN, 14));
		tabbedPane.setFocusable(false);

		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		GroupLayout gl_mainPane = new GroupLayout(mainPane);
		gl_mainPane.setHorizontalGroup(
			gl_mainPane.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 802, Short.MAX_VALUE)
		);
		gl_mainPane.setVerticalGroup(
			gl_mainPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_mainPane.createSequentialGroup()
					.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(27, Short.MAX_VALUE))
		);

		mainPane.setLayout(gl_mainPane);



		addTab();
	}

	public void addTab() {

		tabbedPane.addTab("Tab " + (tabbedPane.getTabCount() + 1), new TablePane(controller));
		tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);

	}
}
