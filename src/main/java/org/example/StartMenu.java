package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class StartMenu extends JFrame implements ActionListener {
    private JLabel contentpane;
    private JButton startButton;
    private JTextField nameField; // Text field to enter the name
    public String BG = MyConstants.FILE_BG1;
    int numOfHearts = 3;
    int timeSet = 30;

    public StartMenu() {
        setTitle("Start Menu");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        setContentPane(contentpane = new JLabel());
        MyImageIcon background = new MyImageIcon(MyConstants.GameMenu);
        contentpane.setIcon(background);
        contentpane.setLayout(null);

        // Top Welcome Text
        JLabel titleLabel = new JLabel("Welcome To Slime Slayer!");
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        int labelWidth = titleLabel.getPreferredSize().width;
        int titlex = (getWidth() - labelWidth) / 2;
        titleLabel.setBounds(titlex, 20, labelWidth, 30);
        add(titleLabel);

        // Set BoxLayout for vertical alignment
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBounds(50, 70, 500, 350); // Adjust these bounds as needed
        panel.setOpaque(false); // Make the panel transparent

        // "Enter your name" Label
        JLabel nameLabel = new JLabel("Enter your name:");
        nameLabel.setFont(new Font("Monospaced", Font.PLAIN, 20));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the label
        panel.add(nameLabel);

        // Add vertical spacing
        panel.add(Box.createVerticalStrut(10));

        // JTextField for name input
        nameField = new JTextField(20);
        nameField.setText("PlayerName");
        nameField.setMaximumSize(new Dimension(200, 30));
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the text field
        panel.add(nameField);

        // Add vertical spacing
        panel.add(Box.createVerticalStrut(10));

        JLabel livesLabel = new JLabel("Lives");
        livesLabel.setFont(new Font("Monospaced", Font.PLAIN, 15));
        livesLabel.setForeground(Color.WHITE);
        livesLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the label
        panel.add(livesLabel);

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

        JLabel timeLabel = new JLabel("Time");
        timeLabel.setFont(new Font("Monospaced", Font.PLAIN, 15));
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the label
        panel.add(timeLabel);

        // JComboBox
        JComboBox<String> comboBox = new JComboBox<>(new String[]{"30", "45", "60", "75", "90"});
//      comboBox.addActionListener(radioButtonListener);
        comboBox.setMaximumSize(new Dimension(200, 30));
        comboBox.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the combo box
        comboBox.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            timeSet = Integer.parseInt((String) comboBox.getSelectedItem());
        }
    });
        panel.add(comboBox);

        // Add vertical spacing
        panel.add(Box.createVerticalStrut(10));

        // JList
        JLabel mapLabel = new JLabel("Select a Map");
        mapLabel.setFont(new Font("Monospaced", Font.PLAIN, 20));
        mapLabel.setForeground(Color.WHITE);
        mapLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the label
        panel.add(mapLabel);

        JList<String> list = new JList<>(new String[]{"Forest", "Nether", "Warp Forest", "Ender", "BlueWorld"});
        JScrollPane listScrollPane = new JScrollPane(list);
        listScrollPane.setPreferredSize(new Dimension(150, 60));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Add a ListSelectionListener to update the variable background
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedValue = list.getSelectedValue();
                    switch (selectedValue) {
                        case "Forest":
                            BG = MyConstants.FILE_BG1;
                            break;
                        case "Nether":
                            BG = MyConstants.FILE_BG2;
                            break;
                        case "Warp Forest":
                            BG = MyConstants.FILE_BG3;
                            break;
                        case "Ender":
                            BG = MyConstants.FILE_BG4;
                            break;
                        case "BlueWorld":
                            BG = MyConstants.FILE_BG5;
                            break;
                    }
                }
            }
        });

        panel.add(listScrollPane);

        // Add vertical spacing
        panel.add(Box.createVerticalStrut(10));

        // Add panel to contentpane
        contentpane.add(panel);

        // Start Button
        startButton = new JButton("Start Game");
        startButton.setFont(new Font("Monospaced", Font.BOLD, 20));
        startButton.addActionListener(this);
        startButton.setBounds(200, 500, 200, 30);
        add(startButton);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            String playerName = nameField.getText(); // Get the entered name
//            new MainApplication(playerName);

            MainApplication mainApp = new MainApplication(playerName, numOfHearts,timeSet, BG);
            mainApp.HeartModify(numOfHearts);
            mainApp.TimerCountdown(timeSet);

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
