package br.ifce.mastermind.window;

import br.ifce.mastermind.client.Client;
import br.ifce.mastermind.component.ColoredJLabel;
import br.ifce.mastermind.component.StackLayout;
import br.ifce.mastermind.constants.Constants;
import br.ifce.mastermind.enums.ClientType;
import br.ifce.mastermind.message.ChatMessage;
import br.ifce.mastermind.message.MasterMindMessage;
import br.ifce.mastermind.util.ColorUtil;
import br.ifce.mastermind.util.MessageUtil;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by jrocha on 25/07/14.
 */
public class ClientWindow {

    private static final Color customBlue = new Color(220, 220, 255);
    private static ClientWindow instance;
    private static Logger logger = Logger.getLogger(ClientWindow.class.getName());
    private List<ColoredJLabel> selectedColors;
    private JButton addColorButton;
    private JButton confirmButton;
    private JButton clearButton;
    private JButton sendButton;
    private JComboBox selectColorComboBox;
    private JPanel selectionPanel;
    private JPanel selectedPanel;
    private JScrollPane scrollPane;
    private JScrollPane chatScrollPane;
    private JScrollPane messagesScrollPane;
    private JPanel messagesPanel;
    private JTextArea chatTextArea;
    private JLabel nameLabel;
    private List<JPanel> rows;
    private MasterMindMessage passwordMessage;
    private JPanel passwordRow;
    private Integer attemptCount = 0;
    private String clientName = "";
    private JSplitPane splitPane;

    static {
        try {
            UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Couldn't create Look and Feel settings!", e);
        }
    }

    private JComboBox comboBox1;

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
        this.messagesPanel = new JPanel();
        this.messagesPanel.setLayout(new StackLayout(StackLayout.VERTICAL));
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
        this.messagesScrollPane = new JScrollPane(this.messagesPanel);
        this.messagesScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.messagesScrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY),
                "Chat",
                TitledBorder.CENTER,
                TitledBorder.DEFAULT_POSITION));
        this.selectColorComboBox = new JComboBox(ColorUtil.getValidColorNames());
        this.addColorButton = new JButton("Add");
        this.addColorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSelectedColor();
            }
        });
        this.nameLabel = new JLabel("Welcome, ");
        this.rows = new ArrayList<JPanel>();
        this.splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        MasterMindMessage dummyMessage = new MasterMindMessage();
        dummyMessage.setRaw("PASSWORD");
        dummyMessage.setResponse(null);
        dummyMessage.setSequence(0);
        dummyMessage.setColors(new Color[]{Color.DARK_GRAY, Color.DARK_GRAY, Color.DARK_GRAY, Color.DARK_GRAY});
        dummyMessage.setClientName(Constants.MASTER);
        dummyMessage.setClientType(ClientType.MASTER);

        this.passwordRow = new JPanel();
        this.passwordRow.add(this.getSelectedRow(dummyMessage));
        this.passwordRow.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createMatteBorder(1, 5, 1, 1, Color.DARK_GRAY),
                "Password",
                TitledBorder.CENTER,
                TitledBorder.DEFAULT_POSITION));
    }


    public static ClientWindow getInstance() {
        if (instance == null) {
            instance = new ClientWindow();
        }

        return instance;
    }

    public void start() {

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

    public void disableControls() {
        this.selectColorComboBox.setEnabled(false);
        this.addColorButton.setEnabled(false);
        this.clearButton.setEnabled(false);
        this.confirmButton.setEnabled(false);
    }

    private void confirmSelectedColors() {

        if (selectedColors.size() == Constants.COLORS_QUANTITY) {

            MasterMindMessage message = new MasterMindMessage();
            message.setClientName(this.clientName);
            message.setSequence(this.attemptCount++);

            StringBuilder rawInfo = new StringBuilder();
            Color[] colors = new Color[Constants.COLORS_QUANTITY];

            for (int i = 0; i < selectedColors.size(); i++) {
                rawInfo.append(ColorUtil.color2String(selectedColors.get(i).getColor()));
                colors[i] = selectedColors.get(i).getColor();

                if (i < selectedColors.size() - 1) {
                    rawInfo.append(", ");
                }
            }

            message.setColors(colors);
            message.setRaw(rawInfo.toString());

            try {
                MessageUtil.sendMasterMindMessage(Client.getInstance().getClientSocket(), message);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Couldn't confirm selected colors!", e);
            }
        }
    }

    public void setPasswordRow(MasterMindMessage message) {
        this.passwordMessage = message;
        this.passwordRow.removeAll();
        this.passwordRow.add(this.getSelectedRow(this.passwordMessage));
        this.passwordRow.repaint();
        this.passwordRow.updateUI();
    }

    public void addServerConfirmationColors(MasterMindMessage masterMindMessage) {
        this.rows.add(this.getSelectedRow(masterMindMessage));
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
        //JPanel responsePanel = getResponseColorsPanel(masterMindMessage.getResponse());

        row.add(new JLabel(masterMindMessage.getClientName() + " # " + String.format("%03d", (masterMindMessage.getSequence()))));

        if (messagePanel != null) {
            row.add(messagePanel);
        }

        if (responsePanel != null) {
            row.add(responsePanel);
        }

        row.add(new JLabel("(" + String.format("%03d", this.rows.size()) + ")"));

        return row;
    }

    private JPanel getResponseColorsPanel(Color[] colors) {
        JPanel responsePanel = null;
        JPanel row1 = new JPanel();
        JPanel row2 = new JPanel();

        if (colors != null) {

            responsePanel = new JPanel(new StackLayout(StackLayout.VERTICAL));
            responsePanel.setBorder(new LineBorder(Color.DARK_GRAY));

            for (int i = 0; i < colors.length; i++) {
                ColoredJLabel label = new ColoredJLabel(colors[i]);
                if (i % 2 == 0)
                    row1.add(label);
                else
                    row2.add(label);
            }

            responsePanel.setBackground(customBlue);
            responsePanel.add(row1);
            responsePanel.add(row2);
        }

        return responsePanel;
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

    public void setNameLabelValue(String value) {
        this.nameLabel.setText("Welcome, " + value + "!");
        this.clientName = value;
    }

    private void sendChatMessage() {
        ChatMessage message = new ChatMessage();
        message.setContent(this.chatTextArea.getText());
        message.setAuthor(this.clientName);

        try {
            MessageUtil.sendMasterMindMessage(Client.getInstance().getClientSocket(), message);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Couldn't confirm selected colors!", e);
        }
    }

    public void addChatMessage(ChatMessage message) {

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        JPanel messagePanel = new JPanel(new StackLayout(StackLayout.VERTICAL));
        JLabel timeAuthor = new JLabel( "[" + format.format(new Date()) + "] " + message.getAuthor() + " : ") ;
        timeAuthor.setFont(new Font("Serif", Font.BOLD, 11));
        timeAuthor.setForeground(Color.BLUE);
        JLabel content = new JLabel(message.getContent());
        content.setFont(new Font("Serif", Font.PLAIN, 11));
        messagePanel.add(timeAuthor);
        messagePanel.add(content);
        this.messagesPanel.add(messagePanel);
        this.messagesPanel.updateUI();
    }

    public void addComponents(Container container) {

        container.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        container.setLayout(new GridBagLayout());
        container.setPreferredSize(new Dimension(760, 680));

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.fill = GridBagConstraints.BOTH;

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 4;
        container.add(this.nameLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 4;
        container.add(this.passwordRow, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 4;
        constraints.weightx = 2;
        constraints.weighty = 1;
        container.add(this.scrollPane, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        constraints.weightx = 1;
        constraints.weighty = 0;
        container.add(this.selectionPanel, constraints);

        constraints.gridx = 2;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.weightx = 0;
        container.add(this.clearButton, constraints);

        constraints.gridx = 3;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        container.add(this.confirmButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 3;
        container.add(this.selectColorComboBox, constraints);

        constraints.gridx = 3;
        constraints.gridy = 4;
        constraints.gridwidth = 1;
        container.add(this.addColorButton, constraints);

        constraints.gridx = 4;
        constraints.gridy = 0;
        constraints.gridwidth = 8;
        constraints.gridheight = 3;
        constraints.weightx = 2;
        constraints.weighty = 1;
        container.add(this.messagesScrollPane, constraints);

        constraints.gridx = 4;
        constraints.gridy = 3;
        constraints.gridwidth = 3;
        constraints.gridheight = 2;
        constraints.weightx = 1;
        constraints.weighty = 0;
        container.add(this.chatScrollPane, constraints);

        constraints.gridx = 8;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.gridheight = 2;
        constraints.weightx = 0;
        container.add(this.sendButton, constraints);
    }

    public void addMasterPasswordMessage(MasterMindMessage message) {
        this.passwordMessage = message;
    }
}
