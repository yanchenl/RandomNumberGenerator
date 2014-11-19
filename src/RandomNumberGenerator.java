/*
 * Author: Yancheng Liu
 * Date: 11/18/2014
 * Project: Random Number Generator
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

public class RandomNumberGenerator extends JFrame {
    private static final long serialVersionUID = 1L;
    private JFrame frame = new JFrame("Random Number Generator");
    private JButton submit = new JButton("Run");
    private JLabel inLabel1 = new JLabel("Number");
    private JLabel inLabel2 = new JLabel("Progress");
    private JLabel inLabel3 = new JLabel("1/(1+0)");
    SubmitMonitor submitMonitor = new SubmitMonitor();
    TextMonitor textMonitor = new TextMonitor();
    private JTextField data1 = new JTextField(10);
    private JTextField data3 = new JTextField(10);
    private JProgressBar pro = new JProgressBar();
    String inputNumber;

    public RandomNumberGenerator() {
        this.frame.setLayout(null);
        this.frame.setSize(400, 200);
        this.inLabel1.setBounds(30, 25, 80, 20);
        this.inLabel2.setBounds(30, 50, 80, 20);
        this.inLabel3.setBounds(30, 75, 80, 20);
        this.submit.setBounds(250, 100, 80, 20);
        this.data1.setBounds(100, 25, 200, 20);
        this.pro.setBounds(100, 50, 200, 20);
        this.data3.setBounds(100, 75, 200, 20);
        this.frame.add(inLabel1);
        this.pro.setStringPainted(true);
        this.frame.add(inLabel2);
        this.frame.add(inLabel3);
        this.frame.add(data1);
        this.frame.add(pro);
        this.frame.add(data3);
        this.frame.add(submit);
        this.frame.setVisible(true);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.submit.addActionListener(submitMonitor);
        this.submit.addActionListener(textMonitor);
    }

    public class SubmitMonitor implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            inputNumber = getInput();
            // System.out.println(inputNumber);
            // begin to run the following code
            // double start = System.currentTimeMillis();
            RandomGenerator rg = new RandomGenerator();
            int max = Integer.parseInt(inputNumber);
            int countOne = 0;
            int countZero = 0;
            int total = 0;
            double progress = 0;
            File file = new File("Result.csv");

            while (total < max) {
                
                //generate seed according to the entropy of the system
                long number = rg.getLongSeed();
                // long time = System.nanoTime();
                // System.out.println(time);
                // number = number * time;
                // System.out.println(number);
                long number2 = System.nanoTime();
                String binary2 = DecimalToBinary(number2);
                // System.out.println(binary2);
                
                //determine the length of each part
                int random = new Random().nextInt(10) + 1;
                binary2 = binary2.substring(32, 32 + random);

                String binary1 = DecimalToBinary(number);
                if (binary1.length() > 50) {
                    binary1 = binary1.substring(1, (51 - random));
                } else {
                    continue;
                }
                // System.out.println(binary);
                String binary = binary1.concat(binary2);

                try {
                    file.createNewFile();
                    FileOutputStream fos = new FileOutputStream(file, true);
                    for (int i = 0; i < 50; i++) {
                        StringBuffer sb = new StringBuffer();
                        sb.append(binary.substring(i, i + 1));
                        sb.append(",");
                        fos.write(sb.toString().getBytes());
                    }
                    fos.close();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                countOne += countNum(binary, "1");
                countZero += countNum(binary, "0");
                total += binary.length();
                progress = 100 * total / (double) max;

                System.out.println(progress);

                pro.setValue((int) progress);
            }
            Double ratio = (double) countOne / (double) (countOne + countZero);
            // System.out.println(ratio);
            // double end = System.currentTimeMillis();
            // System.out.println("time is : " + (end - start));

            data3.setText(ratio.toString());
        }

        // function countNum() return the length of the binary number
        private int countNum(String binary, String num) {
            int count = 0;
            for (int i = 0; i < binary.length(); i++) {
                if (binary.substring(i, i + 1).equals(num)) {
                    count++;
                }
            }
            return count;
        }

        // function DecimalToBinary transfer the decimal to binary
        private String DecimalToBinary(long number) {
            if (number == 0) {
                return "0";
            }
            String binary = "";
            if (number < 0) {
                number = number * -1;
            }
            while (number > 0) {
                int reminder = (int) (number % 2);
                binary = reminder + binary;
                number = number / 2;
            }
            return binary;
        }

    }

    // TextMonitor monitor the user's input
    public class TextMonitor implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String d1 = data1.getText();
            inputNumber = d1;
        }
    }

    public String getInput() {
        return inputNumber;
    }

    public static void main(String[] args) throws InterruptedException {
        new RandomNumberGenerator();
    }

    class RandomGenerator {
        public long getLongSeed() {
            // Using SecureRandom to read OS-provided entropy
            SecureRandom sr = new SecureRandom();
            byte[] seed = sr.generateSeed(8);
            // System.out.println(seed);
            // create a buffer object around the byte array
            ByteBuffer bb = ByteBuffer.wrap(seed);
            return bb.getLong();
        }
    }

}
