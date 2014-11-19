import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class RandomNumberGenerator extends JFrame {
    private static final long serialVersionUID = 1L;
    private JFrame frame = new JFrame("Random Number Generator");
    private JButton submit = new JButton("Run");
    private JLabel inLabel1 = new JLabel("Number");
    private JLabel inLabel2 = new JLabel("1/(1+0)");
    SubmitMonitor submitMonitor = new SubmitMonitor();
    TextMonitor textMonitor = new TextMonitor();
    private JTextField data1 = new JTextField(10);
    private JTextField data2 = new JTextField(10);
    String inputNumber;

    public RandomNumberGenerator() {
        this.frame.setLayout(null);
        this.frame.setSize(400, 150);
        this.inLabel1.setBounds(30, 25, 80, 20);
        this.inLabel2.setBounds(30, 50, 80, 20);
        this.submit.setBounds(250, 90, 80, 20);
        this.data1.setBounds(100, 25, 200, 20);
        this.data2.setBounds(100, 50, 200, 20);
        this.frame.add(inLabel1);
        this.frame.add(inLabel2);
        this.frame.add(data1);
        this.frame.add(data2);
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
            File file = new File("Result.csv");

            while (total < max) {
                long number = rg.getLongSeed();
                // long time = System.nanoTime();
                // System.out.println(time);
                // number = number * time;
                // System.out.println(number);

                String binary = DecimalToBinary(number);
                if (binary.length() > 50) {
                    binary = binary.substring(1, 51);
                } else {
                    continue;
                }
//                System.out.println(binary);

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
            }
            Double ratio = (double) countOne / (double) (countOne + countZero);
            // System.out.println(ratio);
            // double end = System.currentTimeMillis();
            // System.out.println("time is : " + (end - start));

            data2.setText(ratio.toString());
        }

        private int countNum(String binary, String num) {
            int count = 0;
            for (int i = 0; i < binary.length(); i++) {
                if (binary.substring(i, i + 1).equals(num)) {
                    count++;
                }
            }
            return count;
        }

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
            //Using SecureRandom to read OS-provided entropy
            SecureRandom sec = new SecureRandom();
            byte[] sbuf = sec.generateSeed(8);
            ByteBuffer bb = ByteBuffer.wrap(sbuf);
            return bb.getLong();
        }
    }

}
