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


        JLabel titleLabel = new JLabel("Welcome To Slime Slayer!");
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        int labelWidth = titleLabel.getPreferredSize().width;
        int titlex = (getWidth() - labelWidth) / 2;
        titleLabel.setBounds(titlex, 20, labelWidth, 30);
        add(titleLabel);


        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBounds(50, 70, 500, 350);
        panel.setOpaque(false);


        JLabel nameLabel = new JLabel("Enter your name:");
        nameLabel.setFont(new Font("Monospaced", Font.PLAIN, 20));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(nameLabel);


        panel.add(Box.createVerticalStrut(10));


        nameField = new JTextField(20);
        nameField.setText("PlayerName");
        nameField.setMaximumSize(new Dimension(200, 30));
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(nameField);


        panel.add(Box.createVerticalStrut(10));

        JLabel livesLabel = new JLabel("Lives");
        livesLabel.setFont(new Font("Monospaced", Font.PLAIN, 15));
        livesLabel.setForeground(Color.WHITE);
        livesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(livesLabel);


        JPanel radioButtonPanel = new JPanel();
        radioButtonPanel.setLayout(new FlowLayout());
        radioButtonPanel.setOpaque(false);

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

        ActionListener radioButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JRadioButton source = (JRadioButton) e.getSource();
                String selectedText = source.getText();

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

        radioButton1.addActionListener(radioButtonListener);
        radioButton2.addActionListener(radioButtonListener);
        radioButton3.addActionListener(radioButtonListener);
        radioButton4.addActionListener(radioButtonListener);
        radioButton5.addActionListener(radioButtonListener);



        JLabel timeLabel = new JLabel("Time");
        timeLabel.setFont(new Font("Monospaced", Font.PLAIN, 15));
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(timeLabel);

        JComboBox<String> comboBox = new JComboBox<>(new String[]{"30", "45", "60", "75", "90"});
        comboBox.setMaximumSize(new Dimension(200, 30));
        comboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        comboBox.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            timeSet = Integer.parseInt((String) comboBox.getSelectedItem());
        }
    });
        panel.add(comboBox);

        panel.add(Box.createVerticalStrut(10));


        JLabel mapLabel = new JLabel("Select a Map");
        mapLabel.setFont(new Font("Monospaced", Font.PLAIN, 20));
        mapLabel.setForeground(Color.WHITE);
        mapLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(mapLabel);

        JList<String> list = new JList<>(new String[]{"Forest", "Nether", "Warp Forest", "Ender", "BlueWorld"});
        JScrollPane listScrollPane = new JScrollPane(list);
        listScrollPane.setPreferredSize(new Dimension(150, 60));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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

        panel.add(Box.createVerticalStrut(10));

        contentpane.add(panel);

        JLabel student1 = new JLabel("Puriwat Tanansumrit 6380315\n");
        student1.setBounds(100,100,40,40);
        student1.setFont(new Font("Monospaced", Font.PLAIN, 15));
        student1.setForeground(Color.WHITE);
        student1.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(student1);

        JLabel student2 = new JLabel("Andaman Jamprasitsakul 6481331\n");
        student2.setBounds(100,100,40,40);
        student2.setFont(new Font("Monospaced", Font.PLAIN, 15));
        student2.setForeground(Color.WHITE);
        student2.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(student2);

        JLabel student3 = new JLabel("Charupat Trakulchang 6481176\n");
        student3.setBounds(100,100,40,40);
        student3.setFont(new Font("Monospaced", Font.PLAIN, 15));
        student3.setForeground(Color.WHITE);
        student3.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(student3);

        JLabel student4 = new JLabel("Chalantorn Sawangwongchinsri 6580873\n");
        student4.setBounds(100,100,40,40);
        student4.setFont(new Font("Monospaced", Font.PLAIN, 15));
        student4.setForeground(Color.WHITE);
        student4.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(student4);

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
            String playerName = nameField.getText();

            MainApplication mainApp = new MainApplication(playerName, numOfHearts,timeSet, BG);
            mainApp.HeartModify(numOfHearts);
            mainApp.TimerCountdown(timeSet);

            dispose();
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
