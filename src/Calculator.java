import java.util.*;

public class Calculator {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("Enter expression to calculate or EXIT to finish:");
            String expression = sc.nextLine();
            if (expression.equalsIgnoreCase("exit")) {
                break;
            }
            if (parenthesisTest(expression)) {
                System.out.println("Result is: " + Double.toString(calculate(expression)));
            } else {
                System.err.println("Braces are not balanced!");
            }
        }
        sc.close();
    }

    public static boolean parenthesisTest(String expressionToTest) {
        Map<String, String> parenthesis = new HashMap<>();
        parenthesis.put("]", "[");
        parenthesis.put("}", "{");
        parenthesis.put(")", "(");

        String allowedCharacters = "[0-9+-/*]";
        String[] tokens = expressionToTest.split("");
        Stack<String> stack = new Stack();
        boolean result = true;
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].equals("(") || tokens[i].equals("[") ||
                    tokens[i].equals("{")) {
                stack.push(tokens[i]);
            } else if (tokens[i].matches(allowedCharacters)) {
                continue;
            } else {
                if (!stack.isEmpty() &&
                        parenthesis.get(tokens[i]).equals(stack.peek())) {
                    stack.pop();
                } else {
                    result = false;
                    break;
                }
            }
        }
        if (stack.empty() && result) {
            return true;
        } else {
            return false;
        }
    }

    public static double calculate(String expressionToCalculate) {
        double result = 0;
        double innerResult = 0;
        Stack<Character> tokensStack = new Stack<>();
        char[] expressionAsCharArray = expressionToCalculate.toCharArray();
        String innerExpressionString = "";
        for (int i = 0; i < expressionAsCharArray.length; i++) {
            tokensStack.push(expressionAsCharArray[i]);
            if (i == (expressionAsCharArray.length - 1)) {
                do {
                    innerExpressionString = tokensStack.pop() + innerExpressionString;
                } while (!tokensStack.isEmpty() && !Character.toString(tokensStack.peek()).matches("[(]"));
                return calculateInnerExpression(innerExpressionString);
            }
            //converting expression in parenthesis
            if (Character.toString(expressionAsCharArray[i]).equals(")")) {

                tokensStack.pop();//removing ")"
                do {
                    innerExpressionString = tokensStack.pop() + innerExpressionString;
                } while (!Character.toString(tokensStack.peek()).matches("[(]"));
                tokensStack.pop();//removing "("
                innerResult = calculateInnerExpression(innerExpressionString);
                innerExpressionString = "";
                String tempString = String.valueOf(innerResult);
                char[] tempCharArray = tempString.toCharArray();
                for (int z = 0; z < tempCharArray.length; z++) {
                    tokensStack.push(tempCharArray[z]);
                }
            }
        }
        return result;
    }

    public static double calculateInnerExpression(String expressionToCalculate) {
        double result = 0;
        List<Double> temp = new ArrayList<>();
        List<String> operations = new ArrayList<>();
        char[] expressionAsCharArray = expressionToCalculate.toCharArray();
        String innerExpressionString = "";
        Stack<Character> characterStack = new Stack<>();
        //put everything on stack
        for (Character charAtI : expressionAsCharArray) {
            characterStack.push(charAtI);
        }
        // converting to double
        do {

            do {
                innerExpressionString = characterStack.pop() + innerExpressionString;
            } while (!characterStack.isEmpty() && Character.toString(characterStack.peek()).matches("[0-9.]"));
            temp.add(Double.parseDouble(innerExpressionString));
            if (!characterStack.isEmpty()) {
                operations.add(String.valueOf(characterStack.pop()));
            }
            innerExpressionString = "";

        } while (!characterStack.isEmpty());
        for (int i = operations.size(); i > 0; i--) {
            //calculation
            switch (operations.get(operations.size() - 1)) {
                case "+":
                    result = temp.get(operations.size()) + temp.get(operations.size() - 1);
                    break;
                case "-":
                    result = temp.get(operations.size()) - temp.get(operations.size() - 1);
                    break;
                case "/":
                    result = temp.get(operations.size()) / temp.get(operations.size() - 1);
                    break;
                case "*":
                    result = temp.get(operations.size()) * temp.get(operations.size() - 1);
                    break;
            }
            //addition of result to list
            temp.remove(operations.size());
            temp.remove(operations.size() - 1);
            temp.add(result);
            operations.remove(operations.size() - 1);
            result = 0;
        }
        //rounding to 4th decimal place
        long rounding = (long) (temp.get(0) * 1000);
        return rounding / 1000;
    }
}
