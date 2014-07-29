package br.ifce.mastermind.window;

import br.ifce.mastermind.client.Client;
import br.ifce.mastermind.component.ColoredJLabel;
import br.ifce.mastermind.util.ColorUtil;
import br.ifce.mastermind.util.MessageUtil;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by jrocha on 25/07/14.
 */
public class ClientWindow  {

    private static ClientWindow instance;
    private static Logger logger = Logger.getLogger(ClientWindow.class.getName());

    private GridBagLayout layout;
    private GridBagConstraints constraints;
    private List<ColoredJLabel> selectedColors;
    private JButton addColorButton;
    private JButton confirmButton;
    private JButton clearButton;
    private JComboBox selectColorComboBox;
    private JPanel selectionPanel;
    private JPanel selectedPanel;
    private JLabel nameLabel;

    public static ClientWindow getInstance() {
        if (instance == null) {
            instance = new ClientWindow();
        }

        return instance;
    }

    private ClientWindow() {

        logger.info("Starting client GUI....");

        this.layout = new GridBagLayout();
        this.constraints = new GridBagConstraints();
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

        this.selectColorComboBox = new JComboBox(ColorUtil.getValidColorNames());

        this.addColorButton = new JButton("Add");
        this.addColorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSelectedColor();
            }
        });

        this.nameLabel = new JLabel("Welcome, ");
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

    private void confirmSelectedColors() {

        if (selectedColors.size() == 4) {
            try {
                StringBuilder colors = new StringBuilder();

                for (int i = 0; i < selectedColors.size(); i++) {
                    colors.append(ColorUtil.color2String(selectedColors.get(i).getColor()));

                    if (i < selectedColors.size() - 1) {
                    colors.append(", ");
                    }
                }

                MessageUtil.sendMessage(Client.getInstance().getClientSocket(), colors.toString());
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Couldn't confirm selected colors!", e);
            }
        }
    }

    private void addSelectedColor() {
        String color = (String) selectColorComboBox.getSelectedItem();
        int index = selectColorComboBox.getSelectedIndex();

        if (index >= 0 && selectedColors.size() <= 4) {
            selectColorComboBox.removeItemAt(index);
            ColoredJLabel coloredJLabel = new ColoredJLabel(ColorUtil.string2Color(color));
            selectedColors.add(coloredJLabel);
            selectionPanel.add(coloredJLabel);
            selectionPanel.updateUI();

            if (selectedColors.size() == 4) {
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

    public void setNameLabelValue (String value) {
        this.nameLabel.setText("Welcome, " + value + "!");
    }

    public void addComponents(Container container) {

        container.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        container.setLayout(layout);
        container.setPreferredSize(new Dimension(310, 480));

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
        container.add(this.selectedPanel, constraints);

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
