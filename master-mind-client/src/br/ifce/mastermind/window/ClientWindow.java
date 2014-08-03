package br.ifce.mastermind.window;

import br.ifce.mastermind.client.Client;
import br.ifce.mastermind.component.ColoredJLabel;
import br.ifce.mastermind.constants.Constants;
import br.ifce.mastermind.entities.MasterMindMessage;
import br.ifce.mastermind.util.ColorUtil;
import br.ifce.mastermind.util.MessageUtil;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by jrocha on 25/07/14.
 */
public class ClientWindow {

    private static ClientWindow instance;
    private static Logger logger = Logger.getLogger(ClientWindow.class.getName());

    private List<ColoredJLabel> selectedColors;
    private JButton addColorButton;
    private JButton confirmButton;
    private JButton clearButton;
    private JComboBox selectColorComboBox;
    private JPanel selectionPanel;
    private JPanel selectedPanel;
    private JScrollPane scrollPane;
    private JLabel nameLabel;

    private List<JPanel> rows;
    private Integer attemptCount = 0;
    private String clientName = "";


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

        this.selectionPanel = new JPanel();
        this.selectionPanel.setBorder(new LineBorder(Color.DARK_GRAY));
        this.selectionPanel.setVisible(true);

        this.selectedPanel = new JPanel();
        this.selectedPanel.setBorder(new LineBorder(Color.DARK_GRAY));
        this.selectedPanel.setVisible(true);
        this.selectedPanel.setLayout(new BoxLayout(this.selectedPanel, BoxLayout.Y_AXIS));

        this.scrollPane = new JScrollPane(selectedPanel);
        this.scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

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
    }

    public static ClientWindow getInstance() {
        if (instance == null) {
            instance = new ClientWindow();
        }

        return instance;
    }

    public void start() {

        final JFrame frame = new JFrame();

        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Couldn't create Look and Feel settings!", e);
        }

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

    public void addServerConfirmationColors(MasterMindMessage masterMindMessage) {

        JPanel messagePanel = new JPanel();
        messagePanel.setBorder(new LineBorder(Color.DARK_GRAY));

        JLabel senderLabel = new JLabel();
        senderLabel.setText(masterMindMessage.getClientName() + " # " + String.format("%03d", (masterMindMessage.getSequence())));

        JPanel responsePanel = new JPanel();
        responsePanel.setBorder(new LineBorder(Color.DARK_GRAY));

        Color[] colors = masterMindMessage.getColors();
        Color[] response = masterMindMessage.getResponse();

        for (int i = 0; i < colors.length; i++) {
            ColoredJLabel label = new ColoredJLabel(colors[i]);
            messagePanel.add(label);
        }

        for (int i = 0; i < response.length; i++) {
            ColoredJLabel label = new ColoredJLabel(response[i]);
            responsePanel.add(label);
        }

        messagePanel.setVisible(true);
        responsePanel.setVisible(true);

        this.selectedPanel.removeAll();

        JPanel row = new JPanel();

        row.add(senderLabel);
        row.add(messagePanel);
        row.add(responsePanel);

        this.rows.add(row);

        row.add(new JLabel("(" + String.format("%03d", (this.rows.size() - 1)) + ")"));

        for (int i = this.rows.size() - 1; i >= 0; i--) {
            this.selectedPanel.add(this.rows.get(i));
        }

        if (this.rows.size() < this.selectedPanel.getHeight() / row.getPreferredSize().getHeight()) {
            this.selectedPanel.add(Box.createVerticalStrut((int) (this.selectedPanel.getHeight() - (row.getPreferredSize().getHeight() * this.rows.size()) - 10)));
        }

        this.selectedPanel.updateUI();

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

    public void addComponents(Container container) {

        container.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        container.setLayout(new GridBagLayout());
        container.setPreferredSize(new Dimension(360, 480));

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
        constraints.weightx = 2;
        constraints.weighty = 1;
        container.add(this.scrollPane, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.weightx = 1;
        constraints.weighty = 0;
        container.add(this.selectionPanel, constraints);

        constraints.gridx = 2;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.weightx = 0;
        container.add(this.clearButton, constraints);

        constraints.gridx = 3;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        container.add(this.confirmButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 3;
        container.add(this.selectColorComboBox, constraints);

        constraints.gridx = 3;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        container.add(this.addColorButton, constraints);

    }

}
