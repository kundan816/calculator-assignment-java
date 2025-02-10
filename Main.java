package org.example;

import java.util.Scanner;
import java.util.Stack;


public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter the expression : ");
            String input = scanner.nextLine().trim();

            // Exit condition if user types "exit"
            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            try {
                // Evaluate the expression and display the result
                double result = evaluateExpression(input);
                System.out.println("Output: " + result);
            } catch (Exception e) {
                // Handle invalid input or errors
                System.out.println("Error: " + e.getMessage());
            }
        }
        scanner.close();      // Close the scanner resource
    }

    private static double evaluateExpression(String expression) throws Exception {
        // Remove all spaces from the expression
        expression = expression.replaceAll("\\s+", "");

        if (expression.isEmpty()) {
            throw new Exception("Expression cannot be empty");
        }

        Stack<Double> numbers = new Stack<>();    // Stack to store numbers
        Stack<Character> operators = new Stack<>();  // Stack to store operators

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);

            // Parse numbers
            if (Character.isDigit(ch) || ch == '.') {
                int j = i;
                // Find the end of the number
                while (j < expression.length() && (Character.isDigit(expression.charAt(j)) || expression.charAt(j) == '.')) {
                    j++;
                }
                // Push the number to the stack
                numbers.push(Double.parseDouble(expression.substring(i, j)));
                i = j - 1; // Move i to the last digit of the current number
            }
            else if (ch == '(') {
                operators.push(ch);   // Push opening parenthesis to operators stack
            }
            else if (ch == ')') {              // Process until matching '(' is found
                while (!operators.isEmpty() && operators.peek() != '(') {
                    numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()));
                }
                if (!operators.isEmpty()) {
                    operators.pop();// Remove '(' from stack
                }
            }
            else if (isOperator(ch)) {
                // Process operators with higher or equal precedence
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(ch)) {
                    if (operators.peek() == '(') {
                        break;
                    }
                    numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.push(ch);   // Push the current operator to stack
            } else {
                throw new Exception("Invalid character in expression: " + ch);
            }
        }

        while (!operators.isEmpty()) {         // Process any remaining operators
            if (operators.peek() == '(') {
                throw new Exception("Mismatched parentheses");
            }
            numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()));
        }

        return numbers.pop();              // Final result should be the only number left in the stack
    }

    private static boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/';
    }

    private static int precedence(char op) {
        switch (op) {
            case '+':
            case '-':
                return 1;    // Low precedence
            case '*':
            case '/':
                return 2;     // High precedence
            default:
                return 0;      // Invalid operator
        }
    }

    private static double applyOperation(char op, double b, double a) throws Exception {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new Exception("Division by zero");
                }
                return a / b;
            default:
                throw new Exception("Invalid operator: " + op);
        }
    }

}