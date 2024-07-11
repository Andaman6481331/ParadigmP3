package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartMenu extends JFrame implements ActionListener {
    private JButton startButton;
    private JTextField nameField; // Text field to enter the name
    int numOfHearts;

    public StartMenu() {
        setTitle("Start Menu");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Top Welcome Text
        JLabel titleLabel = new JLabel("Welcome To Slime Slayer!");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        int labelWidth = titleLabel.getPreferredSize().width;
        int titlex = (getWidth() - labelWidth) / 2;
        titleLabel.setBounds(titlex, 20, labelWidth, 30);
        add(titleLabel);

        // Set BoxLayout for vertical alignment
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBounds(50, 70, 300, 450); // Adjust these bounds as needed
        panel.setOpaque(false); // Make the panel transparent

        // "Enter your name" Label
        JLabel nameLabel = new JLabel("Enter your name:");
        nameLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the label
        panel.add(nameLabel);

        // Add vertical spacing
        panel.add(Box.createVerticalStrut(10));

        // JTextField for name input
        nameField = new JTextField(20);
        nameField.setMaximumSize(new Dimension(200, 30));
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the text field
        panel.add(nameField);

        // Add vertical spacing
        panel.add(Box.createVerticalStrut(10));

        JLabel permitLabel = new JLabel("Lives");
        permitLabel.setFont(new Font("Serif", Font.PLAIN, 15));
        permitLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the label
        panel.add(permitLabel);

        // JRadioButtons aligned horizontally
        JPanel radioButtonPanel = new JPanel();
        radioButtonPanel.setLayout(new FlowLayout());
        radioButtonPanel.setOpaque(false); // Make the panel transparent

        ButtonGroup group = new ButtonGroup();
        JRadioButton radioButton1 = new JRadioButton("1");
        JRadioButton radioButton2 = new JRadioButton("2");
        JRadioButton radioButton3 = new JRadioButton("3");
        JRadioButton radioButton4 = new JRadioButton("4");
        JRadioButton radioButton5 = new JRadioButton("5");

        group.add(radioButton1);
        group.add(radioButton2);
        group.add(radioButton3);
        group.add(radioButton4);
        group.add(radioButton5);

        radioButtonPanel.add(radioButton1);
        radioButtonPanel.add(radioButton2);
        radioButtonPanel.add(radioButton3);
        radioButtonPanel.add(radioButton4);
        radioButtonPanel.add(radioButton5);

        panel.add(radioButtonPanel);

        // Create a single ActionListener for all radio buttons
        ActionListener radioButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JRadioButton source = (JRadioButton) e.getSource();
                String selectedText = source.getText();

                // Use switch statement to set numOfHearts based on selected text
                switch (selectedText) {
                    case "1":
                        numOfHearts = 1;
                        break;
                    case "2":
                        numOfHearts = 2;
                        break;
                    case "3":
                        numOfHearts = 3;
                        break;
                    case "4":
                        numOfHearts = 4;
                        break;
                    case "5":
                        numOfHearts = 5;
                        break;
                }
            }
        };
        // Add the ActionListener to all radio buttons
        radioButton1.addActionListener(radioButtonListener);
        radioButton2.addActionListener(radioButtonListener);
        radioButton3.addActionListener(radioButtonListener);
        radioButton4.addActionListener(radioButtonListener);
        radioButton5.addActionListener(radioButtonListener);



        // Add vertical spacing
//        panel.add(Box.createVerticalStrut(10));

        // JComboBox
        JComboBox<String> comboBox = new JComboBox<>(new String[]{"Item 1", "Item 2", "Item 3", "Item 4", "Item 5"});
        comboBox.setMaximumSize(new Dimension(200, 30));
        comboBox.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the combo box
        panel.add(comboBox);

        // Add vertical spacing
        panel.add(Box.createVerticalStrut(20));

        // JList
        JList<String> list = new JList<>(new String[]{"Item 1", "Item 2", "Item 3", "Item 4", "Item 5"});
        JScrollPane listScrollPane = new JScrollPane(list);
        listScrollPane.setPreferredSize(new Dimension(200, 100));
        panel.add(listScrollPane);

        // Add vertical spacing
        panel.add(Box.createVerticalStrut(20));

        // Add panel to frame
        add(panel);

        // Start Button
        startButton = new JButton("Start Game");
        startButton.setFont(new Font("Serif", Font.BOLD, 20));
        startButton.addActionListener(this);
        startButton.setBounds(100, 500, 200, 30);
        add(startButton);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            String playerName = nameField.getText(); // Get the entered name
//            new MainApplication(playerName);

            MainApplication mainApp = new MainApplication(playerName, numOfHearts);
            mainApp.HeartModify(numOfHearts);
            dispose(); // Close the start menu
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StartMenu().setVisible(true);
            }
        });
    }
}
