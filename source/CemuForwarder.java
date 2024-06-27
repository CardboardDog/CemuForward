import javax.swing.*;
import java.awt.event.*;
import CemuFW.ForwardGen;
public class CemuForwarder{
    public static void main(String[] args){
        // setup
        JFrame window = new JFrame("Cemu Forwarder");
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        // title
        JLabel title = new JLabel("Cemu Forwarder 1.0");
        title.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        panel.add(title);

        // rom path
        JPanel pathPanel = new JPanel();
        pathPanel.add(new JLabel("Extracted ROM:"));
        JTextField pathField = new JTextField("~/dumps/example",48);
        pathPanel.add(pathField);
        JButton searchButton = new JButton("...");
        searchButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                JFileChooser pickFolder = new JFileChooser();
                pickFolder.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if(pickFolder.showOpenDialog(window) == JFileChooser.APPROVE_OPTION){
                    pathField.setText(pickFolder.getSelectedFile().getPath());
                }
            }
        });
        pathPanel.add(searchButton);
        panel.add(pathPanel);

        // options
        JPanel checkPanel = new JPanel();
        JCheckBox fullscreenCheck = new JCheckBox("fullscreen");
        JCheckBox consoleCheck = new JCheckBox("debugger");
        checkPanel.add(fullscreenCheck);
        checkPanel.add(consoleCheck);
        panel.add(checkPanel);
        JPanel argPanel = new JPanel();
        argPanel.add(new JLabel("Extra Launch Args:"));
        JTextField argField = new JTextField("",20);
        argPanel.add(argField);
        panel.add(argPanel);

        // generate
        JButton forwardButton = new JButton("Forward");
        forwardButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                ForwardGen.createLauncher(
                    pathField.getText(),
                    fullscreenCheck.isSelected(),
                    consoleCheck.isSelected(),
                    argField.getText()
                );
            }
        });
        panel.add(forwardButton);

        // run
        window.add(panel);
        window.pack();
        window.setVisible(true);
    }
}