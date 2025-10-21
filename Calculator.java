import javax.swing.*;
import java.awt.*;

public class Calculator extends JFrame {
    private JTextField display;
    private double currentValue = 0;
    private String currentOp = "";
    private boolean startNumber = true; // whether to start a new number

    public Calculator() {
        setTitle("Simple Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(320, 420);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        display = new JTextField("0");
        display.setEditable(false);
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setFont(new Font("Arial", Font.BOLD, 28));
        display.setBackground(Color.WHITE);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4, 5, 5));

        String[] buttons = {
            "C", "±", "%", "/",
            "7", "8", "9", "*",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "0", ".", "=", ""
        };

        for (String text : buttons) {
            if (text.equals("")) {
                buttonPanel.add(new JLabel());
                continue;
            }
            JButton btn = new JButton(text);
            btn.setFont(new Font("Arial", Font.PLAIN, 20));
            btn.addActionListener(e -> onButtonPress(text));
            buttonPanel.add(btn);
        }

        getContentPane().setLayout(new BorderLayout(5, 5));
        getContentPane().add(display, BorderLayout.NORTH);
        getContentPane().add(buttonPanel, BorderLayout.CENTER);
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private void onButtonPress(String text) {
        switch (text) {
            case "C":
                currentValue = 0;
                currentOp = "";
                display.setText("0");
                startNumber = true;
                break;
            case "±":
                toggleSign();
                break;
            case "%":
                percentage();
                break;
            case "+":
            case "-":
            case "*":
            case "/":
                applyOperator(text);
                break;
            case "=":
                calculateResult();
                currentOp = "";
                break;
            case ".":
                appendDecimal();
                break;
            default: // digits
                appendDigit(text);
                break;
        }
    }

    private void appendDigit(String digit) {
        if (startNumber) {
            display.setText(digit);
            startNumber = false;
        } else {
            String txt = display.getText();
            if (txt.equals("0")) txt = digit;
            else txt += digit;
            display.setText(txt);
        }
    }

    private void appendDecimal() {
        if (startNumber) {
            display.setText("0.");
            startNumber = false;
            return;
        }
        String txt = display.getText();
        if (!txt.contains(".")) {
            display.setText(txt + ".");
        }
    }

    private void toggleSign() {
        String txt = display.getText();
        if (txt.equals("0")) return;
        if (txt.startsWith("-")) display.setText(txt.substring(1));
        else display.setText("-" + txt);
    }

    private void percentage() {
        try {
            double val = Double.parseDouble(display.getText());
            val = val / 100.0;
            display.setText(trimDouble(val));
            startNumber = true;
        } catch (NumberFormatException ex) {
            display.setText("Error");
            startNumber = true;
        }
    }

    private void applyOperator(String op) {
        try {
            double displayed = Double.parseDouble(display.getText());
            if (currentOp.isEmpty()) {
                currentValue = displayed;
            } else {
                currentValue = compute(currentValue, displayed, currentOp);
                display.setText(trimDouble(currentValue));
            }
            currentOp = op;
            startNumber = true;
        } catch (NumberFormatException ex) {
            display.setText("Error");
            startNumber = true;
            currentOp = "";
        } catch (ArithmeticException ex) {
            display.setText("Error");
            startNumber = true;
            currentOp = "";
        }
    }

    private void calculateResult() {
        if (currentOp.isEmpty()) return;
        try {
            double displayed = Double.parseDouble(display.getText());
            double result = compute(currentValue, displayed, currentOp);
            display.setText(trimDouble(result));
            currentValue = result;
            startNumber = true;
        } catch (NumberFormatException | ArithmeticException ex) {
            display.setText("Error");
            startNumber = true;
            currentOp = "";
        }
    }

    private double compute(double a, double b, String op) {
        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/":
                if (b == 0) throw new ArithmeticException("Division by zero");
                return a / b;
            default: return b;
        }
    }

    private String trimDouble(double v) {
        if (v == (long) v) return String.format("%d", (long) v);
        else return String.format("%s", v);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Calculator c = new Calculator();
            c.setVisible(true);
        });
    }
}
