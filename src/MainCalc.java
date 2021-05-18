import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class MainCalc {
    public static void main(String ... args){       //to start the program
        EventQueue.invokeLater(()->{
            var frame = new CalcFrame();
            frame.setTitle("Simple calculator");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setIconImage(new ImageIcon("calc.png").getImage());

            frame.setVisible(true);
        });
    }
}


