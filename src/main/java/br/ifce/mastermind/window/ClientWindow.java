package br.ifce.mastermind.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import br.ifce.mastermind.constants.Constants;
import br.ifce.mastermind.entities.ChatMessage;
import br.ifce.mastermind.entities.MasterMindMessage;
import br.ifce.mastermind.entities.MasterMindPlayer;
import br.ifce.mastermind.entities.Message;
import br.ifce.mastermind.remote.RemoteServer;
import br.ifce.mastermind.util.ColorUtil;

public class ClientWindow implements Serializable {
	
	private static ClientWindow	instance;
	private static final long	serialVersionUID	= 1L;
	private static Logger		logger				= Logger.getLogger(ClientWindow.class.getName());
	private Color				customBlue			= new Color(220, 220, 255);
	private MasterMindPlayer	player;
	private RemoteServer		server;
	private MasterMindMessage	passwordMessage;
	private Integer				attemptCount		= 0;
	private List<ColoredJLabel>	selectedColors;
	private JButton				addColorButton;
	private JButton				confirmButton;
	private JButton				clearButton;
	private JButton				sendButton;
	private JComboBox<String>	selectColorComboBox;
	private JPanel				selectionPanel;
	private JPanel				selectedPanel;
	private JScrollPane			scrollPane;
	private JScrollPane			chatScrollPane;
	private JScrollPane			messagesScrollPane;
	private JPanel				messagesPanel;
	private JTextArea			chatTextArea;
	private JLabel				nameLabel;
	private List<JPanel>		rows;
	private JPanel				passwordRow;
	
	private ClientWindow() {
		
		logger.info("Starting client GUI....");
		
		this.selectedColors = new ArrayList<ColoredJLabel>();
		this.confirmButton = new JButton("Confirm");
		this.confirmButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				confirmSelectedColors();
			}
		});
		this.clearButton = new JButton("Clear");
		this.clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clearSelectedColors();
			}
		});
		this.sendButton = new JButton("Send");
		this.sendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendChatMessage();
			}
		});
		this.selectionPanel = new JPanel();
		this.selectionPanel.setBorder(new LineBorder(Color.DARK_GRAY));
		this.selectionPanel.setVisible(true);
		this.selectedPanel = new JPanel();
		this.selectedPanel.setVisible(true);
		this.selectedPanel.setLayout(new BoxLayout(this.selectedPanel, BoxLayout.Y_AXIS));
		this.chatTextArea = new JTextArea();
		this.chatTextArea.setLineWrap(true);
		messagesPanel = new JPanel();
		messagesPanel.setLayout(new StackLayout(StackLayout.VERTICAL));
		this.scrollPane = new JScrollPane(selectedPanel);
		this.scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.scrollPane.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createMatteBorder(1, 5, 1, 1, Color.DARK_GRAY),
				"Attempts",
				TitledBorder.CENTER,
				TitledBorder.DEFAULT_POSITION));
		this.chatScrollPane = new JScrollPane(this.chatTextArea);
		this.chatScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.chatScrollPane.setBorder(new LineBorder(Color.DARK_GRAY));
		this.messagesScrollPane = new JScrollPane(messagesPanel);
		this.messagesScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.messagesScrollPane.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.DARK_GRAY),
				"Chat",
				TitledBorder.CENTER,
				TitledBorder.DEFAULT_POSITION));
		this.selectColorComboBox = new JComboBox<String>(ColorUtil.getValidColorNames());
		this.addColorButton = new JButton("Add");
		this.addColorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addSelectedColor();
			}
		});
	}
	
	public static ClientWindow getInstance() {
		if (instance == null) instance = new ClientWindow();
		return instance;
	}
	
	public void start() {
		
		this.rows = new ArrayList<JPanel>();
		this.nameLabel = new JLabel("Welcome! " + this.player.getName());
		
		MasterMindMessage dummyMessage = new MasterMindMessage();
		dummyMessage.setResponse(null);
		dummyMessage.setPlayer(this.player);
		dummyMessage.setSequence(0);
		dummyMessage.setColors(new Color[] { Color.DARK_GRAY, Color.DARK_GRAY, Color.DARK_GRAY, Color.DARK_GRAY });
		
		this.passwordRow = new JPanel();
		this.passwordRow.add(this.getSelectedRow(dummyMessage));
		this.passwordRow.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createMatteBorder(1, 5, 1, 1, Color.DARK_GRAY),
				"Password",
				TitledBorder.CENTER,
				TitledBorder.DEFAULT_POSITION));
		
		final JFrame frame = new JFrame();
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				addComponents(frame.getContentPane());
				
				frame.setTitle("Master Mind");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setLocationByPlatform(true);
				frame.pack();
				frame.setVisible(true);
			}
		});
	}
	
	public void setEnableControls(boolean flag) {
		this.selectColorComboBox.setEnabled(flag);
		this.addColorButton.setEnabled(flag);
		this.clearButton.setEnabled(flag);
		this.confirmButton.setEnabled(flag);
	}
	
	private void confirmSelectedColors() {
		
		if (selectedColors.size() == Constants.COLORS_QUANTITY) {
			
			MasterMindMessage message = new MasterMindMessage();
			message.setPlayer(this.player);
			message.setSequence(this.attemptCount++);
			
			Color[] colors = new Color[Constants.COLORS_QUANTITY];
			
			for (int i = 0; i < selectedColors.size(); i++) {
				colors[i] = selectedColors.get(i).getColor();
			}
			
			message.setColors(colors);
			
			try {
				server.processMessage(message);
			} catch (RemoteException e) {
				logger.log(Level.SEVERE, "Couldn't confirm selected colors!", e);
			}
		}
	}
	
	public void setPasswordRow(Message message) {
		this.passwordMessage = (MasterMindMessage) message;
		this.passwordRow.removeAll();
		this.passwordRow.add(this.getSelectedRow(this.passwordMessage));
		this.passwordRow.repaint();
		this.passwordRow.updateUI();
	}
	
	public void addServerConfirmationMessage(Message masterMindMessage) {
		this.rows.add(this.getSelectedRow((MasterMindMessage) masterMindMessage));
		this.refreshSelectedPanel();
	}
	
	private void refreshSelectedPanel() {
		this.selectedPanel.removeAll();
		
		for (int i = this.rows.size() - 1; i >= 0; i--) {
			this.selectedPanel.add(this.rows.get(i));
		}
		
		if (this.rows.size() < this.selectedPanel.getHeight() / this.rows.get(0).getPreferredSize().getHeight()) {
			this.selectedPanel.add(Box.createVerticalStrut((int) (this.selectedPanel.getHeight() - (this.rows.get(0).getPreferredSize().getHeight() * this.rows.size()) - 10)));
		}
		
		this.selectedPanel.updateUI();
	}
	
	private JPanel getSelectedRow(MasterMindMessage masterMindMessage) {
		
		JPanel row = new JPanel();
		
		JPanel messagePanel = getMessageColorsPanel(masterMindMessage.getColors());
		JPanel responsePanel = getMessageColorsPanel(masterMindMessage.getResponse());
		
		row.add(new JLabel(masterMindMessage.getPlayer().getName() + " # " + String.format("%03d", (masterMindMessage.getSequence()))));
		
		if (messagePanel != null) {
			row.add(messagePanel);
		}
		
		if (responsePanel != null) {
			row.add(responsePanel);
		}
		
		row.add(new JLabel("(" + String.format("%03d", this.rows.size()) + ")"));
		
		return row;
	}
		
	private JPanel getMessageColorsPanel(Color[] colors) {
		
		JPanel messagePanel = null;
		
		if (colors != null) {
			
			messagePanel = new JPanel();
			messagePanel.setBorder(new LineBorder(Color.DARK_GRAY));
			
			for (int i = 0; i < colors.length; i++) {
				ColoredJLabel label = new ColoredJLabel(colors[i]);
				messagePanel.add(label);
			}
			
			messagePanel.setBackground(customBlue);
		}
		
		return messagePanel;
	}
	
	private void addSelectedColor() {
		String color = (String) selectColorComboBox.getSelectedItem();
		int index = selectColorComboBox.getSelectedIndex();
		
		if (index >= 0 && selectedColors.size() <= Constants.COLORS_QUANTITY) {
			selectColorComboBox.removeItemAt(index);
			ColoredJLabel coloredJLabel = new ColoredJLabel(ColorUtil.string2Color(color));
			selectedColors.add(coloredJLabel);
			selectionPanel.add(coloredJLabel);
			selectionPanel.updateUI();
			
			if (selectedColors.size() == Constants.COLORS_QUANTITY) {
				addColorButton.setEnabled(false);
			}
			
			logger.info("Color " + color + " has benn selected.");
		}
	}
	
	private void clearSelectedColors() {
		selectionPanel.removeAll();
		selectionPanel.updateUI();
		
		for (int i = 0; i < selectedColors.size(); i++) {
			selectColorComboBox.addItem(ColorUtil.color2String(selectedColors.get(i).getColor()));
		}
		
		selectedColors.clear();
		addColorButton.setEnabled(true);
		
		logger.info("Cleaning all selected colors... ready to start again! ");
	}
	
	private void sendChatMessage() {
		ChatMessage message = new ChatMessage();
		message.setPlayer(player);
		message.setText(this.chatTextArea.getText());
		message.setTime(Calendar.getInstance().getTime());
		message.setSequence(0);
		
		try {
			server.processMessage(message);
		} catch (RemoteException e) {
			logger.log(Level.SEVERE, "Couldn't send chat message!", e);
		}
	}
	
	public void addChatMessage(Message message) {
		
		ChatMessage chatMessage = (ChatMessage) message;
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		JPanel messagePanel = new JPanel(new StackLayout(StackLayout.VERTICAL));
		JLabel timeAuthor = new JLabel("[" + format.format(chatMessage.getTime()) + "] " + chatMessage.getPlayer().getName() + " : ");
		timeAuthor.setFont(new Font("Serif", Font.BOLD, 11));
		timeAuthor.setForeground(Color.BLUE);
		JLabel content = new JLabel(chatMessage.getText());
		content.setFont(new Font("Serif", Font.PLAIN, 11));
		messagePanel.add(timeAuthor);
		messagePanel.add(content);
		messagesPanel.add(messagePanel);
		messagesPanel.updateUI();
	}
	
	public void addComponents(Container container) {
		
		JPanel leftPanel = new JPanel();
		leftPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		leftPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.fill = GridBagConstraints.BOTH;
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 4;
		leftPanel.add(this.nameLabel, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 4;
		leftPanel.add(this.passwordRow, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 4;
		constraints.weightx = 2;
		constraints.weighty = 1;
		leftPanel.add(this.scrollPane, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		constraints.weightx = 1;
		constraints.weighty = 0;
		leftPanel.add(this.selectionPanel, constraints);
		
		constraints.gridx = 2;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.weightx = 0;
		leftPanel.add(this.clearButton, constraints);
		
		constraints.gridx = 3;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		leftPanel.add(this.confirmButton, constraints);
		
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 3;
		leftPanel.add(this.selectColorComboBox, constraints);
		
		constraints.gridx = 3;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		leftPanel.add(this.addColorButton, constraints);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		rightPanel.setLayout(new BorderLayout());
		
		this.chatTextArea.setRows(4);
		this.chatTextArea.setColumns(12);
		
		JPanel messagePanel = new JPanel();
		messagePanel.add(this.chatScrollPane);
		messagePanel.add(this.sendButton);
		
		rightPanel.add(messagePanel, BorderLayout.SOUTH);
		rightPanel.add(this.messagesScrollPane, BorderLayout.CENTER);
		rightPanel.setMinimumSize(new Dimension(250, 500));
		
		JSplitPane splitPane1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane1.setOneTouchExpandable(true);
		splitPane1.setLeftComponent(leftPanel);
		splitPane1.setRightComponent(rightPanel);
		splitPane1.setDividerLocation(10000);
		
		container.setPreferredSize(new Dimension(600, 700));
		container.add(splitPane1);
	}
	
	public MasterMindPlayer getPlayer() {
		return player;
	}
	
	public void setPlayer(MasterMindPlayer player) {
		this.player = player;
	}
	
	public RemoteServer getServer() {
		return server;
	}
	
	public void setServer(RemoteServer server) {
		this.server = server;
	}
	
}
