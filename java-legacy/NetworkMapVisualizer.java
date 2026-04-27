import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class NetworkMapVisualizer extends JPanel {

    boolean showPath = false;

    @Override
    protected void paintComponent(Graphics g){

        super.paintComponent(g);

        g.setColor(Color.BLACK);

        // edges
        g.drawLine(120,120,320,120);  // A-B
        g.drawLine(120,120,220,270);  // A-C
        g.drawLine(320,120,420,270);  // B-D
        g.drawLine(220,270,420,270);  // C-D

        if(showPath){

            g.setColor(Color.RED);

            g.drawLine(120,120,220,270);  // A-C
            g.drawLine(220,270,420,270);  // C-D
        }

        g.setColor(Color.BLACK);

        // nodes
        g.fillOval(100,100,40,40);  // A
        g.fillOval(300,100,40,40);  // B
        g.fillOval(200,250,40,40);  // C
        g.fillOval(400,250,40,40);  // D

        // labels
        g.drawString("A",115,95);
        g.drawString("B",315,95);
        g.drawString("C",215,245);
        g.drawString("D",415,245);
    }

    public static void main(String[] args){

        JFrame frame = new JFrame("Network Map Visualizer");

        NetworkMapVisualizer panel =
        new NetworkMapVisualizer();

        JButton button =
        new JButton("Show Shortest Path");

        JButton reset =
        new JButton("Reset");

        button.addActionListener(e -> {

            panel.showPath = true;
            panel.repaint();
        });

        reset.addActionListener(e -> {

            panel.showPath = false;
            panel.repaint();
        });

        frame.setLayout(new BorderLayout());

        JPanel bottom = new JPanel();

        bottom.add(button);
        bottom.add(reset);

        frame.add(panel,BorderLayout.CENTER);
        frame.add(bottom,BorderLayout.SOUTH);

        frame.setSize(550,400);

        frame.setDefaultCloseOperation(
        JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);
    }
}