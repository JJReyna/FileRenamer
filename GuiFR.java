
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import javax.swing.SwingUtilities;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.SwingPropertyChangeSupport;

import java.io.PipedOutputStream;
import java.io.PipedInputStream;
import java.io.BufferedReader;
import java.io.PrintStream;

import java.net.URL;


public class GuiFR extends JFrame{

    private FileRenamer fr;

    private GuiFR() {
        fr = new FileRenamer();
        initGUI();
    }

    private void initGUI() {
        //Basic Elements
        setTitle("File Renamer ファイルリネーマー");
        setPreferredSize(new Dimension(400, 600));
        setResizable(true);
        setLocationRelativeTo(null);

        JPanel jPanel = new JPanel();

        //Directory stuff
        JLabel label = new JLabel("Set Directory: ");
        JButton submitDir = new JButton("Submit!");
        JLabel submittedDir = new JLabel("Current Directory: <null>");
        JTextField directoryT = new JTextField();
        directoryT.setPreferredSize(new Dimension(150,40));
        submitDir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //added replace(etc.) to create a valid directory for ease of use, probably a better way to do this
                if(OsUtils.isWindows()){
                    directoryT.setText(directoryT.getText().replace("\\","\\\\"));
                }
                if(OsUtils.isWindows() && (!directoryT.getText().substring(directoryT.getText().length() -1).equals("\\"))){
                    directoryT.setText(directoryT.getText() + "\\\\");
                }

                if(OsUtils.isUnix() && (!directoryT.getText().substring(directoryT.getText().length()-1).equals("/"))){
                    directoryT.setText(directoryT.getText() + "/");
                }
                System.out.println("Current Directory: " + directoryT.getText());
                fr.setDirectory(directoryT.getText());
                submittedDir.setText("Current Directory: " + directoryT.getText());
                fr.hashmapToTxt();
            }
        });

        //Radio Buttons
        JRadioButton removeKeyword = new JRadioButton("Remove Keyword");
        JRadioButton replaceWithKeyword = new JRadioButton("Replace with Keyword");
        JRadioButton prefixButton = new JRadioButton("Add Prefix to Files");
        JRadioButton postfixButton = new JRadioButton("Add Postfix to Files");
        JRadioButton removeIndexButton = new JRadioButton("Remove Characters in Indices");
        JRadioButton insertAtIndexButton = new JRadioButton("Insert Keyword at Index");
        ButtonGroup renamerOptions = new ButtonGroup();
        //renamerOptions.add(removeIndex);
        renamerOptions.add(removeKeyword);
        renamerOptions.add(replaceWithKeyword);
        renamerOptions.add(prefixButton);
        renamerOptions.add(postfixButton);
        renamerOptions.add(removeIndexButton);
        renamerOptions.add(insertAtIndexButton);

        //Corresponding TextFields for Radio buttons
        //additional warning for user
        String labelText = String.format("<html><div style=\"width:%dpx;\">%s</div></html>", 250, "Work in Progress! Use With Caution");
        JLabel achtung = new JLabel(labelText);
        Font font = new Font("Courier", Font.BOLD, 14);
        achtung.setFont(font);

        JTextField indexT = new JTextField("DEPRECATED");
        indexT.setEditable(false);
        indexT.setPreferredSize(new Dimension(150,20));
        
        JTextField keywordT = new JTextField();
        keywordT.setPreferredSize(new Dimension(150,20));
        JTextField replaceT = new JTextField();
        replaceT.setPreferredSize(new Dimension(150,20));

        JTextField removeIndexT1 = new JTextField();
        removeIndexT1.setPreferredSize(new Dimension(75,20));
        JTextField removeIndexT2 = new JTextField();
        removeIndexT2.setPreferredSize(new Dimension(75, 20));

        JTextField prefixT = new JTextField();
        prefixT.setPreferredSize(new Dimension(200, 20));
        JTextField postfixT = new JTextField();
        postfixT.setPreferredSize(new Dimension(200, 20));
        
        JTextField insertIndexT = new JTextField();
        insertIndexT.setPreferredSize(new Dimension(40,20));

        JTextField insertKeywordIndexT = new JTextField();
        insertKeywordIndexT.setPreferredSize(new Dimension(150, 20));

		//for displaying output to user
        JLabel checkLabel = new JLabel("Confirm new Filenames");
		JTextArea textAreaFoo = new JTextArea();
		textAreaFoo.setBorder(new EmptyBorder(5,5,5,5));
		textAreaFoo.setEditable(false);
		
		JButton submitButton = new JButton("Confirm");
		
		JScrollPane scrollPane = new JScrollPane(textAreaFoo, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(new Dimension(320, 150));
		scrollPane.setEnabled(true);
        scrollPane.setVisible(true);
        scrollPane.setViewportView(textAreaFoo);
        scrollPane.getPreferredSize();
		
		PrintStream standOut = System.out;
		PrintStream printStream = new PrintStream(new CustomOutputStream(textAreaFoo));
		
		System.setOut(printStream);
		System.setErr(printStream);
		
		submitButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
                try{
					if(replaceWithKeyword.isSelected()){
                        fr.renameConfirmTool(fr.replaceWithKeyword(replaceT.getText()));
                    }

                    if(removeKeyword.isSelected()){
                        fr.renameConfirmTool(fr.removeByKeyword(keywordT.getText()));
                    }

                    if(prefixButton.isSelected()){
                        fr.renameConfirmTool(fr.addPrefix(prefixT.getText()));
                    }

                    if(postfixButton.isSelected()){
                        fr.renameConfirmTool(fr.addPostfix(postfixT.getText()));
                    }

                    if(removeIndexButton.isSelected()){
                        fr.renameConfirmTool(fr.removeByIndex(Integer.parseInt(removeIndexT1.getText()), Integer.parseInt(removeIndexT2.getText())));
                    }

                    if(insertAtIndexButton.isSelected()){
                        fr.renameConfirmTool(fr.insertKeywordAtIndex(Integer.parseInt(insertIndexT.getText()), insertKeywordIndexT.getText()));
                    }
                    
                }catch(Exception ex){
                    System.out.println("Error: Set a Valid Directory");
                }
			}
		});
		
        //Run Button
        JButton run = new JButton("RUN");
        run.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                try{
                    //checks if specific button is pressed
                    if(replaceWithKeyword.isSelected()){
                        fr.rename(fr.replaceWithKeyword(replaceT.getText()));
                    }

                    if(removeKeyword.isSelected()){
                        fr.rename(fr.removeByKeyword(keywordT.getText()));
                    }

                    if(prefixButton.isSelected()){
                        fr.rename(fr.addPrefix(prefixT.getText()));
                    }

                    if(postfixButton.isSelected()){
                        fr.rename(fr.addPostfix(postfixT.getText()));
                    }

                    if(removeIndexButton.isSelected()){
                        fr.rename(fr.removeByIndex(Integer.parseInt(removeIndexT1.getText()), Integer.parseInt(removeIndexT2.getText())));
                    }

                    if(insertAtIndexButton.isSelected()){
                        fr.rename(fr.insertKeywordAtIndex(Integer.parseInt(insertIndexT.getText()), insertKeywordIndexT.getText()));
                    }

                }catch(Exception exp){
                    System.out.println("Error: Set a Valid Directory");
                }
            }
        });

        //Revert to original filename button
        JButton revertButton = new JButton("Revert to Original Filename");
        revertButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if(e.getSource() == revertButton){
                    fr.renameToOriginal();
                }
            }
        });

        //Additional Warnings for users
        String labelText2 = String.format("<html><div style=\"width:%dpx;\">%s</div></html>", 250, "Directory MUST Follow Formats upon Submission:");
        JLabel directoryWarning = new JLabel(labelText2);
        Font font2 = new Font("Courier", Font.BOLD, 13);
        directoryWarning.setFont(font2);
        JLabel directoryHowToWindows = new JLabel("For Windows: C:\\\\Users\\\\username\\\\Image Folder\\\\memes\\\\");
        JLabel directoryHowToLinux = new JLabel("For Linux/Unix: /home/user/Desktop/Image Folder/memes/");

        //Directory, submit, and warnings
        jPanel.add(label);
        jPanel.add(directoryT);
        jPanel.add(submitDir);
        jPanel.add(submittedDir);
        jPanel.add(achtung);

        //Radiobuttons and textfields
        jPanel.add(removeKeyword);
        jPanel.add(keywordT);
        jPanel.add(replaceWithKeyword);
        jPanel.add(replaceT);
        jPanel.add(prefixButton);
        jPanel.add(prefixT);
        jPanel.add(postfixButton);
        jPanel.add(postfixT);
        jPanel.add(removeIndexButton);
        jPanel.add(removeIndexT1);
        jPanel.add(removeIndexT2);
        jPanel.add(insertAtIndexButton);
        jPanel.add(insertIndexT);
        jPanel.add(insertKeywordIndexT);

        //new filenames textbox
        jPanel.add(scrollPane);
        jPanel.add(checkLabel);
		jPanel.add(submitButton);

        //Warnings and run button
        jPanel.add(directoryWarning);
        jPanel.add(directoryHowToWindows);
        jPanel.add(directoryHowToLinux);
        jPanel.add(run);
        jPanel.add(revertButton);

        add(jPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String... args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GuiFR gui = new GuiFR();
                gui.pack();
                gui.setVisible(true);
            }
        });
    }
}