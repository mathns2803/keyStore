package com.matmini.keyStore.screen;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.management.openmbean.KeyAlreadyExistsException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.matmini.keyStore.decypher.AESCipher128;
import com.matmini.keyStore.manager.interfaces.KeysManagerInterface;
import com.matmini.keyStore.manager.interfaces.TableUpdateListener;
import com.matmini.keyStore.manager.service.KeysManagerService;
import com.matmini.keyStore.screen.enums.SimpleKeyStoreTableEnum;
import com.matmini.keyStore.screen.handlers.RegistriesHandler;
import com.matmini.keyStore.screen.jframes.Alerts;
import com.matmini.keyStore.screen.jframes.MultiButtonEditor;
import com.matmini.keyStore.screen.jframes.MultiButtonRenderer;
import com.matmini.keyStore.util.ConstantsParameters;

public class SimpleKeyStore implements TableUpdateListener {

	private JFrame frame;
	private JTable registriesTable;
	private KeysManagerInterface keysManagerService;
	private JTextField userTextField;
	private JTextField passwordTextField;
	private JTextField password2TextField;
	private JTextField websiteTextField;

	private JScrollPane scrollPane = new JScrollPane();

	public SimpleKeyStore() {
		initialize();
	}

	private void initialize() {
		keysManagerService = new KeysManagerService();

		setLookAndFeel();
		frame = new JFrame();
		frame.setTitle(ConstantsParameters.FRAME_TITLE);
		frame.getContentPane().setBackground(new Color(255, 255, 255));
		frame.setBounds(200, 200, 1000, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		JTabbedPane optionsTablePane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(optionsTablePane, BorderLayout.CENTER);

		JPanel keyListPannel = new JPanel();
		optionsTablePane.addTab("Available Keys", null, keyListPannel, null);
		keyListPannel.setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec.decode("500px:grow"), },
				new RowSpec[] { RowSpec.decode("255px:grow"), }));

		keyListPannel.add(scrollPane, "1, 1, fill, fill");

		buildRegistriesTable();

		optionsTablePane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				buildRegistriesTable();
			}
		});

		JPanel keyToolsPannel = new JPanel();
		optionsTablePane.addTab("Tools", null, keyToolsPannel, null);
		GridBagLayout gbl_keyToolsPannel = new GridBagLayout();
		gbl_keyToolsPannel.columnWidths = new int[] { 0, 0 };
		gbl_keyToolsPannel.rowHeights = new int[] { 0, 0 };
		gbl_keyToolsPannel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_keyToolsPannel.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		keyToolsPannel.setLayout(gbl_keyToolsPannel);

		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane_1 = new GridBagConstraints();
		gbc_tabbedPane_1.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane_1.gridx = 0;
		gbc_tabbedPane_1.gridy = 0;
		keyToolsPannel.add(tabbedPane_1, gbc_tabbedPane_1);

		JPanel panelAddRegistry = new JPanel();
		tabbedPane_1.addTab("Add Registry", null, panelAddRegistry, null);
		GridBagLayout gbl_panelAddRegistry = new GridBagLayout();
		gbl_panelAddRegistry.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_panelAddRegistry.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gbl_panelAddRegistry.columnWeights = new double[] { 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		gbl_panelAddRegistry.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panelAddRegistry.setLayout(gbl_panelAddRegistry);

		JLabel lblUser = new JLabel("User");
		GridBagConstraints gbc_lblUser = new GridBagConstraints();
		gbc_lblUser.anchor = GridBagConstraints.EAST;
		gbc_lblUser.insets = new Insets(0, 0, 5, 5);
		gbc_lblUser.gridx = 0;
		gbc_lblUser.gridy = 0;
		panelAddRegistry.add(lblUser, gbc_lblUser);

		userTextField = new JTextField();
		GridBagConstraints gbc_userTextField = new GridBagConstraints();
		gbc_userTextField.gridwidth = 8;
		gbc_userTextField.insets = new Insets(0, 0, 5, 5);
		gbc_userTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_userTextField.gridx = 1;
		gbc_userTextField.gridy = 0;
		panelAddRegistry.add(userTextField, gbc_userTextField);
		userTextField.setColumns(10);

		JLabel lblPassword = new JLabel("Password");
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.anchor = GridBagConstraints.EAST;
		gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassword.gridx = 0;
		gbc_lblPassword.gridy = 1;
		panelAddRegistry.add(lblPassword, gbc_lblPassword);

		passwordTextField = new JTextField();
		passwordTextField.setColumns(10);
		GridBagConstraints gbc_passwordTextField = new GridBagConstraints();
		gbc_passwordTextField.gridwidth = 8;
		gbc_passwordTextField.insets = new Insets(0, 0, 5, 5);
		gbc_passwordTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordTextField.gridx = 1;
		gbc_passwordTextField.gridy = 1;
		panelAddRegistry.add(passwordTextField, gbc_passwordTextField);

		JLabel lblPassword2 = new JLabel("Confirm Password");
		GridBagConstraints gbc_lblPassword2 = new GridBagConstraints();
		gbc_lblPassword2.anchor = GridBagConstraints.EAST;
		gbc_lblPassword2.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassword2.gridx = 0;
		gbc_lblPassword2.gridy = 2;
		panelAddRegistry.add(lblPassword2, gbc_lblPassword2);

		password2TextField = new JTextField();
		password2TextField.setColumns(10);
		GridBagConstraints gbc_password2TextField = new GridBagConstraints();
		gbc_password2TextField.gridwidth = 8;
		gbc_password2TextField.insets = new Insets(0, 0, 5, 5);
		gbc_password2TextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_password2TextField.gridx = 1;
		gbc_password2TextField.gridy = 2;
		panelAddRegistry.add(password2TextField, gbc_password2TextField);

		JLabel lblWebsite = new JLabel("Website");
		GridBagConstraints gbc_lblWebsite = new GridBagConstraints();
		gbc_lblWebsite.anchor = GridBagConstraints.EAST;
		gbc_lblWebsite.insets = new Insets(0, 0, 5, 5);
		gbc_lblWebsite.gridx = 0;
		gbc_lblWebsite.gridy = 3;
		panelAddRegistry.add(lblWebsite, gbc_lblWebsite);

		websiteTextField = new JTextField();
		websiteTextField.setColumns(10);
		GridBagConstraints gbc_websiteTextField = new GridBagConstraints();
		gbc_websiteTextField.gridwidth = 8;
		gbc_websiteTextField.insets = new Insets(0, 0, 5, 5);
		gbc_websiteTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_websiteTextField.gridx = 1;
		gbc_websiteTextField.gridy = 3;
		panelAddRegistry.add(websiteTextField, gbc_websiteTextField);

		JButton btnAddNewRegistry = new JButton("Add");
		btnAddNewRegistry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					boolean isValidRegistryFields = isValidRegistryFields();
					if (!isValidRegistryFields) {
						return;
					}
					try {
						AESCipher128 aes = AESCipher128.getInstance();
						keysManagerService.save(RegistriesHandler.registryFromInput(userTextField.getText(),
								aes.encrypt(passwordTextField.getText()), websiteTextField.getText()));
						Alerts.callAlertBox("Registry added", "Success", JOptionPane.INFORMATION_MESSAGE);
					} catch (KeyAlreadyExistsException ke) {
						Alerts.callAlertBox("User just registred for this website!", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}

		});
		GridBagConstraints gbc_btnAddNewRegistry = new GridBagConstraints();
		gbc_btnAddNewRegistry.gridx = 10;
		gbc_btnAddNewRegistry.gridy = 5;
		panelAddRegistry.add(btnAddNewRegistry, gbc_btnAddNewRegistry);

		frame.setVisible(true);
	}

	private void setLookAndFeel() {
		try {
			String osName = System.getProperty("os.name").toLowerCase();
			if (osName.contains("windows")) {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			} else if (osName.contains("linux")) {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			} else {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
		} catch (Exception e) {
			System.err.println("Failed to set LookAndFeel: " + e.getMessage());
		}
	}

	private void buildRegistriesTable() {
		registriesTable = new JTable();
		registriesTable
				.setModel(new DefaultTableModel(RegistriesHandler.parseListToObject(keysManagerService.realAll()),
						SimpleKeyStoreTableEnum.getAllColumns().toArray(new String[0])));
		scrollPane.setViewportView(registriesTable);

		MultiButtonEditor multiButtonEditor = new MultiButtonEditor(new JCheckBox(), registriesTable);
		multiButtonEditor.addTableUpdateListener(this);
		registriesTable.getColumnModel().getColumn(3).setCellRenderer(new MultiButtonRenderer());
		registriesTable.getColumnModel().getColumn(3).setCellEditor(multiButtonEditor);
	}

	private boolean isValidRegistryFields() {
		if (userTextField.getText() == null || userTextField.getText().isEmpty()
				|| userTextField.getText().contains(" ")) {
			Alerts.callAlertBox("User is empty or has white space", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (passwordTextField.getText() == null || passwordTextField.getText().isEmpty()
				|| passwordTextField.getText().contains(" ")) {
			Alerts.callAlertBox("Password is empty or has white space", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (password2TextField.getText() == null || password2TextField.getText().isEmpty()
				|| password2TextField.getText().contains(" ")) {
			Alerts.callAlertBox("Confirm password is empty or has white space", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (websiteTextField.getText() == null || websiteTextField.getText().isEmpty()
				|| websiteTextField.getText().contains(" ")) {
			Alerts.callAlertBox("Website is empty or has white space", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if (!passwordTextField.getText().equals(password2TextField.getText())) {
			Alerts.callAlertBox("Passwords are not equals", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	@Override
	public void onTableUpdate() {
		buildRegistriesTable();
	}

}
