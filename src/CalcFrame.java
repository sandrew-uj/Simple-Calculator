import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;

public class CalcFrame extends JFrame {                 //class of frame, where calculator appears
    private static final int DEFAULT_WIDTH = 400;       //default sizes for frame
    private static final int DEFAULT_HEIGHT = 300;

    private static final int ROWS_COUNT = 20;       //default sizes for text area
    private static final int COLUMNS_COUNT = 40;

    private static final String multiply = "\u00D7";    //special symbols for multiply, division and minus
    private static final String div = "\u00F7";
    private static final String minus = "\u2013";

    private JTextArea screen;
    private LinkedList<String> input;               //users input: numbers and operations
    private String current = "0";                   //current number
    private String operationCode;                   //code of operation: +, -, *, /
    private boolean operatorIsEntered = false;      //shows when operator (+, -, *, /) is entered to avoid double operator
    private Font font = new Font("Cambria", Font.PLAIN, 21);  //font for buttons and text area

    public CalcFrame(){
        var layout = new GridBagLayout();           // set layout for calculator
        setLayout(layout);

        input = new LinkedList<String>();           //add linked list

        screen = new JTextArea(ROWS_COUNT, COLUMNS_COUNT);     // add screen, where will be numbers, which user enters
        screen.setEditable(false);
        screen.setLineWrap(true);
        screen.setBorder(new RoundedBorder(10));
        screen.setFont(font);
        screen.setText(current);

        ActionListener numberListener = e->{
            String numberElem = ((CalcButton)e.getSource()).getText();

            if (current != "0" || numberElem == ".")
                current += numberElem;
            else if (numberElem != "0")     //avoid doubled zeros and if swap zero on the screen to entered digit
                current = numberElem;

            operatorIsEntered = false;
            screen.setText(current);
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
            if (current.length() > 1)
                current = current.substring(0, current.length()-1);
            else
                current = "0";

            screen.setText(current);
            screen.repaint();
        });

        var acButton = new CalcButton("AC");       //this action clears all screen and sets firstVar and secondVar to defaults
        acButton.addActionListener(e->{
            current = "0";
            input.clear();
            screen.setText(current);

            screen.repaint();
        });

        ActionListener operationListener = e->{
            operationCode = ((CalcButton)e.getSource()).getText();

            if (isNumber(current) && !operatorIsEntered) {      //avoid double operation
                input.add(current);
                input.add(operationCode);
                current = "0";
                screen.setText(current);
            }else
                setErrorMessage();
            operatorIsEntered = true;
        };

        var multiplyButton = new CalcButton(multiply);
        multiplyButton.addActionListener(operationListener);

        var divButton = new CalcButton(div);
        divButton.addActionListener(operationListener);

        var plusButton = new CalcButton("+");
        plusButton.addActionListener(operationListener);

        var subtractionButton = new CalcButton(minus);
        subtractionButton.addActionListener(operationListener);

        var eqButton = new CalcButton("=");
        eqButton.addActionListener(e->{         //to get result
            if (isNumber(current)){
                input.add(current);

                ListIterator<String> iter = input.listIterator();
                iter.next();
                while(iter.hasNext()){                  //firstly calculate multiplications and divisions
                    String operation = iter.next();
                    if (operation.equals(multiply) || operation.equals(div)){
                        iter.previous();
                        Double firstVar = Double.parseDouble(iter.previous());      //first component
                        iter.remove();                                              //remove component and * or /

                        iter.next();
                        iter.remove();

                        Double secondVar = Double.parseDouble(iter.next());     //second component
                        iter.remove();

                        if (operation.equals(multiply))                 //add multiplication or division to list
                            iter.add(new Double(firstVar*secondVar).toString());
                        else{
                            if (Math.abs(secondVar) < 1e-10)            //if secondVar == 0 derive error message
                                setErrorMessage();
                            else
                                iter.add(new Double(firstVar/secondVar).toString());
                        }

                    }else
                        iter.next();
                }

                if (!input.isEmpty()){
                    iter = input.listIterator();
                    iter.next();
                    while(iter.hasNext()){              //Secondly calculate sums and subtractions
                        String operation = iter.next();

                        iter.previous();
                        Double firstVar = Double.parseDouble(iter.previous());      //get first component
                        iter.remove();                                              //remove components and - or +

                        iter.next();
                        iter.remove();

                        Double secondVar = Double.parseDouble(iter.next());     //get second component
                        iter.remove();

                        if (operation.equals("+"))                              //add subtraction or sum to list
                            iter.add(new Double(firstVar+secondVar).toString());
                        else
                            iter.add(new Double(firstVar-secondVar).toString());

                    }

                    iter.previous();
                    current = iter.next();      //set current and remove this element from list
                    iter.remove();

                    screen.setText(current);
                }

            }else{
                setErrorMessage();
            }
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

    private boolean isNumber(String suspend){       //approve is this number of not
        boolean trueNumber = true;
        boolean wasPoint = false;
        for (int i = 0; i < suspend.length() && trueNumber; ++i){
            char ch = suspend.charAt(i);
            trueNumber = i == 0 && ch == '-' || ch >= '0' && ch <= '9' || ch == '.' && !wasPoint;
            if (ch == '.') wasPoint = true;
        }

        if (suspend.length() == 0) trueNumber = false;
        return trueNumber;
    }

    private void setErrorMessage(){                             //derives error message on the screen
        JOptionPane.showMessageDialog(this, "Math Error",
                "Error", JOptionPane.ERROR_MESSAGE);
        current = "0";
        input.clear();
        screen.setText(current);
    }
}
