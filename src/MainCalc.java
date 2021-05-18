import javax.swing.*;
import java.awt.*;

public class MainCalc {
    public static void main(String ... args){       //to start the program
        EventQueue.invokeLater(()->{
            var frame = new CalcFrame();
            frame.setTitle("Plain calculator");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setIconImage(new ImageIcon("calc.png").getImage());

            frame.setVisible(true);
        });
    }
}


