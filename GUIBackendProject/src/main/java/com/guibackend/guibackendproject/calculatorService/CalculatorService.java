package com.guibackend.guibackendproject.calculatorService;

import org.springframework.stereotype.Service;

import java.util.Stack;

@Service
public class CalculatorService {

    public double evaluateExpression(String expr, String type) {
        if (type.equalsIgnoreCase("rpn")) {
            return evaluateRPN(expr);
        } else if (type.equalsIgnoreCase("algebraic")) {
            String rpn = convertToRPN(expr);
            return evaluateRPN(rpn);
        } else {
            throw new IllegalArgumentException("Unknown expression type");
        }
    }

    private double evaluateRPN(String expr) {
        Stack<Double> stack = new Stack<>();
        for (String token : expr.split("\\s+")) {
            if (token.matches("-?\\d+(\\.\\d+)?")) {
                stack.push(Double.parseDouble(token));
            } else if (token.matches("[+\\-*/]")) {
                if (stack.size() < 2) throw new IllegalArgumentException("Invalid RPN Expression");
                double b = stack.pop(), a = stack.pop();
                switch (token) {
                    case "+": stack.push(a + b); break;
                    case "-": stack.push(a - b); break;
                    case "*": stack.push(a * b); break;
                    case "/": stack.push(a / b); break;
                }
            } else {
                throw new IllegalArgumentException("Invalid token: " + token);
            }
        }
        if (stack.size() != 1) throw new IllegalArgumentException("Invalid RPN Expression");
        return stack.pop();
    }

    private String convertToRPN(String expr) {
        StringBuilder output = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        // Tokenize: numbers (including decimals), operators, and parentheses
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("-?\\d+(?:\\.\\d+)?|[+\\-*/()]").matcher(expr.replaceAll("\\s+", ""));
        java.util.List<String> tokens = new java.util.ArrayList<>();
        while (matcher.find()) {
            String token = matcher.group();
            if (!token.isEmpty()) tokens.add(token);
        }

        for (String token : tokens) {
            if (token.matches("-?\\d+(\\.\\d+)?")) { // number (int or decimal)
                output.append(token).append(" ");
            } else if (token.matches("[+\\-*/]")) {
                while (!stack.isEmpty() && stack.peek() != '(' && precedence(stack.peek()) >= precedence(token.charAt(0))) {
                    output.append(stack.pop()).append(" ");
                }
                stack.push(token.charAt(0));
            } else if (token.equals("(")) {
                stack.push('(');
            } else if (token.equals(")")) {
                boolean foundOpeningParen = false;
                while (!stack.isEmpty()) {
                    char op = stack.pop();
                    if (op == '(') {
                        foundOpeningParen = true;
                        break;
                    } else {
                        output.append(op).append(" ");
                    }
                }
                if (!foundOpeningParen) {
                    throw new IllegalArgumentException("Mismatched parentheses");
                }
            }
        }

        while (!stack.isEmpty()) {
            char op = stack.pop();
            if (op == '(' || op == ')') {
                throw new IllegalArgumentException("Mismatched parentheses");
            }
            output.append(op).append(" ");
        }

        return output.toString().trim();
    }


    private int precedence(char op) {
        return (op == '+' || op == '-') ? 1 : 2;
    }
}
