package org.bennri.view;

import org.bennri.utils.PasswordGenerator;
import org.bennri.listener.MouseMovementListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;

public class View extends JFrame{
    private JPanel rootPanel;
    private JPanel grid;
    private PasswordGenerator pwGenerator;

    public void initGUI() {
        pwGenerator = new PasswordGenerator();

        NumberFormat format = NumberFormat.getInstance();
        format.setGroupingUsed(false);
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setAllowsInvalid(false);
        formatter.setMinimum(0);

        this.rootPanel = new JPanel();
        rootPanel.setLayout(new BorderLayout());

        this.grid = new JPanel();
        grid.setLayout(new GridLayout(3, 2));

        JLabel pwLength = new JLabel("Length");
        JLabel bitsLabel = new JLabel("Bits Entropy");
        JFormattedTextField bitsField = new JFormattedTextField(formatter);
        JFormattedTextField lengthField = new JFormattedTextField(formatter);
        JButton generateButton = new JButton("Generate PW");
        JButton copyToClipBoardButton = new JButton("Copy to clipboard");
        JTextField pwField = new JTextField("", SwingConstants.CENTER);
        pwField.setEditable(false);
        pwLength.setBackground(Color.white);

        DocumentListener docListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                if (!lengthField.getText().equals("")) {
                    double entropy = pwGenerator.computeEntropy(
                            Integer.parseInt(lengthField.getText()));
                    bitsField.setText(entropy + "");
                    bitsField.revalidate();
                }
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
            }
        };

        lengthField.getDocument().addDocumentListener(docListener);

        generateButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {

                if (!lengthField.getText().equals("")) {
                    MouseMovementListener m = new MouseMovementListener(Integer.parseInt(lengthField.getText()));
                    // collect mouse movements
                    m.collectPoints();
                    // use the coordinates to create a password
                    String newPW = pwGenerator.generatePassword(m.getCoords());
                    pwField.setText(newPW);
                    pwField.revalidate();
                }


            }
        });


        copyToClipBoardButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                StringSelection stringSelection = new StringSelection(pwField.getText());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
            }});

        rootPanel.add(new JLabel("Password Generator", SwingConstants.CENTER), BorderLayout.PAGE_START);

        grid.add(pwLength);
        grid.add(lengthField);
        grid.add(bitsLabel);
        grid.add(bitsField);
        grid.add(generateButton);
        grid.add(copyToClipBoardButton);
        rootPanel.add(grid, BorderLayout.CENTER);
        rootPanel.add(pwField, BorderLayout.PAGE_END);

        this.add(rootPanel);
        setLocationRelativeTo(null);
        setSize(new Dimension(400, 200));
        this.setTitle("Password Generator");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
