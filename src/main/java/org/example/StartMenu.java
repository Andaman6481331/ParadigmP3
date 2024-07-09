package org.example;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class StartMenu extends JFrame implements ActionListener {
    private JPanel	     contentpane;
    private JButton startButton;
    private JList<String>	list;
    private JTextArea	text;
<<<<<<< Updated upstream
    private JButton	print_button, draw_button, clear_button;

    private String [] items = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};
    private Object [] messageFromList;


=======
    private JToggleButton [] tb;
    private ButtonGroup      bgroup;

    private String [] items = {"1", "3", "5"};
    private Object [] messageFromList;
    
>>>>>>> Stashed changes
    public StartMenu() {
        setTitle("Start Menu");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        contentpane = (JPanel)getContentPane();
        contentpane.setBackground( Color.LIGHT_GRAY );
        contentpane.setLayout( new FlowLayout() );

        JLabel titleLabel = new JLabel("Welcome to the Game!");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
<<<<<<< Updated upstream
        add(titleLabel, BorderLayout.NORTH);


        // ----- (4) List + ListSelectionEvent
        list = new JList( items );
        list.setVisibleRowCount(5);
        list.addListSelectionListener( new ListSelectionListener() {
            @Override
            public void valueChanged( ListSelectionEvent e )
            {
                if( !e.getValueIsAdjusting() )
                {
                    messageFromList = list.getSelectedValues();
                }
            }
        });
        // Panel for list and text area
        text = new JTextArea(5, 20);
        JScrollPane listScrollPane = new JScrollPane(list);
        JScrollPane textScrollPane = new JScrollPane(text);

        JPanel mpanel = new JPanel();
        mpanel.setLayout(new GridLayout(1, 2));
        mpanel.add(listScrollPane);
        mpanel.add(textScrollPane);

        add(mpanel, BorderLayout.CENTER);
=======
        contentpane.add(titleLabel);

//=======================radioGrouping==================================
//        JLabel obobLabel= new JLabel();
//        bgroup  = new ButtonGroup();
//        tb      = new JToggleButton[3];
//        JPanel bpanel = new JPanel();
//        for (int i=0; i < 3; i++)
//        {
//            tb[i] = new JRadioButton( items[i] );
//            bgroup.add( tb[i] );
//            bpanel.add( tb[i] );
//        }
//        obobLabel.add(bpanel);
//        contentpane.add(obobLabel);
        bgroup = new ButtonGroup();
        tb = new JToggleButton[3];
        JPanel bpanel = new JPanel();
        for (int i = 0; i < 3; i++) {
            tb[i] = new JRadioButton(items[i]);
            bgroup.add(tb[i]);
            bpanel.add(tb[i]);
        }
        contentpane.add(bpanel);

>>>>>>> Stashed changes

        startButton = new JButton("Start Game");
        startButton.setFont(new Font("Serif", Font.BOLD, 20));
        startButton.addActionListener(this);
        add(startButton, BorderLayout.SOUTH);
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            new MainApplication();
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
