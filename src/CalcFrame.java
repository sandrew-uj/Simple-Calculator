import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class CalcFrame extends JFrame {                 //class of frame, where calculator appears
    private static final int DEFAULT_WIDTH = 400;       //default sizes for frame
    private static final int DEFAULT_HEIGHT = 300;

    private static final int ROWS_COUNT = 20;       //default sizes for text area
    private static final int COLUMNS_COUNT = 40;

    private JTextArea screen;
    private String firstVar = "0";                  // first operand
    private String secondVar = "0";                 // second operand
    private String operationCode;                   // code of operation: +, -, *, /
    private boolean operatorIsEntered = false;      //shows when operator (+, -, *, /) is entered
    private Font font = new Font("Cambria", Font.PLAIN, 21);  //font for buttons and text area

    public CalcFrame(){
        var layout = new GridBagLayout();           // set layout for calculator
        setLayout(layout);


        screen = new JTextArea(ROWS_COUNT, COLUMNS_COUNT);     // add screen, where will be numbers, which user enters
        screen.setEditable(false);
        screen.setLineWrap(true);
        screen.setBorder(new RoundedBorder(10));
        screen.setFont(font);
        screen.setText("0");

        ActionListener numberListener = e->{
            String numberElem = ((CalcButton)e.getSource()).getText();
            String screenNumber = screen.getText();

            if ((screenNumber.equals("0") || screenNumber.equals("Math Error")) && numberElem != ".")      //erase 0 or Math Error
                screen.setText("" + numberElem);
            else
                screen.setText(screenNumber + numberElem);
            screen.repaint();

            if (!operatorIsEntered) {
                firstVar += numberElem;
            }
            else{
                secondVar += numberElem;
            }
        };

        var numberButtons = new CalcButton[10];            //add buttons with digits
        for (int i = 0; i < 10; ++i){
            numberButtons[i] = new CalcButton(Integer.toString(i));
            numberButtons[i].addActionListener(numberListener);
        }

        var pointButton = new CalcButton(".");
        pointButton.addActionListener(numberListener);

        var delButton = new CalcButton("DEL");     //button, that erases only one digit
        delButton.addActionListener(e->{

            if (!operatorIsEntered && !firstVar.equals("0")){                     //if firstVar == 0, it means that user hasn't yet
                firstVar = firstVar.substring(0, firstVar.length()-1);      //entered a number and he can't delete it
                if (firstVar.length() == 0) firstVar = "0";
                screen.setText(firstVar);
            }
            else if (!secondVar.equals("0")){
                secondVar = secondVar.substring(0, secondVar.length()-1);
                if (secondVar.length() == 0) secondVar = "0";
                screen.setText(secondVar);
            }
            screen.repaint();

        });

        var acButton = new CalcButton("AC");       //this action clears all screen and sets firstVar and secondVar to defaults
        acButton.addActionListener(e->{
            firstVar = secondVar = "0";
            screen.setText(firstVar);
            screen.repaint();
        });

        ActionListener operationListener = e->{
            operationCode = ((CalcButton)e.getSource()).getText();
            operatorIsEntered = true;
            screen.setText("0");
        };

        var multiplyButton = new CalcButton("\u00D7");
        multiplyButton.addActionListener(operationListener);

        var divButton = new CalcButton("\u00F7");
        divButton.addActionListener(operationListener);

        var plusButton = new CalcButton("+");
        plusButton.addActionListener(operationListener);

        var subtractionButton = new CalcButton("\u2013");
        subtractionButton.addActionListener(operationListener);

        var eqButton = new CalcButton("=");
        eqButton.addActionListener(e->{
            boolean error = false;
            double firstVar_d = Double.parseDouble(firstVar);           //converse operands to double
            double secondVar_d = Double.parseDouble(secondVar);

            switch(operationCode){
                case "\u00D7":
                    firstVar_d *= secondVar_d;
                    break;
                case "\u00F7":
                    if (Math.abs(secondVar_d) > 10e-10)         //check whether secondVar_d == 0
                        firstVar_d /= secondVar_d;
                    else{
                        firstVar_d = 0;
                        error = true;
                    }
                    break;
                case "+":
                    firstVar_d += secondVar_d;
                    break;
                case "\u2013":
                    firstVar_d -= secondVar_d;
                    break;
            }

            firstVar = Double.toString(firstVar_d);             //converse first operand back to string
            secondVar = "0";
            operatorIsEntered = false;

            if (error)
                screen.setText("Math Error");
            else
                screen.setText(firstVar);               //derive result on the screen
            screen.repaint();
        });

        add(screen, new GBC(0, 0, 1, 2).setFill(GBC.BOTH).setInsets(1).setWeight(0, 100));        //layout of components

        var panel1 = new JPanel();                                          //set layout
        panel1.setLayout(new GridLayout(3, 5, 2, 2));
        panel1.add(numberButtons[7]);
        panel1.add(numberButtons[8]);
        panel1.add(numberButtons[9]);
        panel1.add(delButton);
        panel1.add(acButton);
        panel1.add(numberButtons[4]);
        panel1.add(numberButtons[5]);
        panel1.add(numberButtons[6]);
        panel1.add(multiplyButton);
        panel1.add(divButton);
        panel1.add(numberButtons[1]);
        panel1.add(numberButtons[2]);
        panel1.add(numberButtons[3]);
        panel1.add(plusButton);
        panel1.add(subtractionButton);
        add(panel1, new GBC(0, 2, 1, 3).setFill(GBC.BOTH).setInsets(1).setWeight(0, 100));

        var panel2 = new JPanel();
        panel2.setLayout(new GridLayout(1, 3, 2, 2));
        panel2.add(numberButtons[0]);
        panel2.add(pointButton);
        panel2.add(eqButton);
        add(panel2, new GBC(0, 5, 1, 1).setFill(GBC.BOTH).setWeight(100, 30));

        pack();
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    private class CalcButton extends JButton{       //button class helper
        CalcButton(String title){
            super(title);
            this.setBorder(new RoundedBorder(10));
            this.setFont(font);
        }
    }
}
