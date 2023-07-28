import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

import static java.lang.Character.isDigit;

public class SpreadSheet {

  private static Map<String, String> spreadSheet = new HashMap<>();

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Select any of the below mentioned options:");
    while (true) {
      System.out.println("Press 1 for setting cell value");
      System.out.println("Press 2 for getting cell value");
      System.out.println("Press 0 to stop editing");
      int option = scanner.nextInt();
      switch (option) {
        case 1 -> {
          System.out.println("Enter cell number");
          scanner.nextLine();
          String cellNumber = scanner.nextLine();
          System.out.println("Enter cell value");
          String cellValue = scanner.nextLine();
          setCellValue(cellNumber, cellValue);
          System.out.println();
        }
        case 2 -> {
          System.out.println("Enter cell number");
          scanner.nextLine();
          String cellNumber = scanner.nextLine();
          getCellValue(cellNumber);
        }
        case 0 -> {
          System.out.println("Stopping the editing.");
          return;
        }
        default -> System.out.println("Incorrect option selected. Please try again.");
      }
    }
  }

  private static void setCellValue(String cellNumber, String cellValue) {
    if (isBlank(cellNumber) || isBlank(cellValue)) {
      System.out.println("Empty values entered. Please try again.");
      return;
    }
    if (!isValidCellNumber(cellNumber)) {
      System.out.println("Incorrect cell number entered. Please try again.");
      return;
    }
    try {
      if (cellValue.startsWith("=")) {
        spreadSheet.put(cellNumber, evaluateValue(cellValue));
      } else {
        spreadSheet.put(cellNumber, cellValue);
      }
      System.out.println("Value set successfully");
    } catch (Exception e) {
      System.out.println("Invalid value provide. Please re-verify");
    }
  }

  private static String evaluateValue(String cellValue) {
    String expression = cellValue.substring(1);
    return evaluate(expression);
  }

  private static Boolean isValidCellNumber(String cellNumber) {
    Character firstCharacter = cellNumber.charAt(0);
    return isAlphabet(firstCharacter);
  }

  private static Boolean isAlphabet(Character character) {
    return (character >= 'A' && character <= 'Z') || (character >= 'a' && character <= 'z');
  }

  private static void getCellValue(String cellNumber) {
    if (null != spreadSheet && null != spreadSheet.get(cellNumber)) {
      System.out.println("Value is: " + spreadSheet.get(cellNumber));
    } else {
      System.out.println("This cell is empty.");
    }
    System.out.println();
  }

  private static boolean isBlank(final CharSequence cs) {
    final int strLen = length(cs);
    if (strLen == 0) {
      return true;
    }
    for (int i = 0; i < strLen; i++) {
      if (!Character.isWhitespace(cs.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  private static int length(final CharSequence cs) {
    return cs == null ? 0 : cs.length();
  }

  private static Boolean isAlphaNumeric(Character character) {
    return isAlphabet(character) || isDigit(character);
  }

  private static String evaluate(String value) {
    int i;
    Stack <Integer> values = new Stack<>();
    Stack <Character> ops = new Stack<>();

    for (i = 0; i < value.length(); i++) {
      if(value.charAt(i) == ' ') {
        continue;
      } else if (value.charAt(i) == '(') {
        ops.push(value.charAt(i));
      } else if (isAlphaNumeric(value.charAt(i))) {
        String val = "";
        while(i < value.length() && isAlphaNumeric(value.charAt(i))) {
          val = val.concat(String.valueOf(value.charAt(i)));
          i++;
        }
        if (isValidCellNumber(val)) {
          values.push(isBlank(spreadSheet.get(val)) ? 0 : Integer.parseInt(spreadSheet.get(val)));
        } else {
          values.push(Integer.parseInt(val));
        }
        i--;
      } else if (value.charAt(i) == ')') {
        while (!ops.empty() && ops.peek() != '(') {
          int val2 = values.peek();
          values.pop();

          int val1 = values.peek();
          values.pop();

          char op = ops.peek();
          ops.pop();

          values.push(applyOp(val1, val2, op));
        }
        if(!ops.empty())
          ops.pop();
      } else {
        while (!ops.empty() && precedence(ops.peek()) >= precedence(value.charAt(i))) {
          int val2 = values.peek();
          values.pop();

          int val1 = values.peek();
          values.pop();

          char op = ops.peek();
          ops.pop();

          values.push(applyOp(val1, val2, op));
        }
        ops.push(value.charAt(i));
      }
    }
    while(!ops.empty()) {
      int val2 = values.peek();
      values.pop();

      int val1 = values.peek();
      values.pop();

      char op = ops.peek();
      ops.pop();

      values.push(applyOp(val1, val2, op));
    }
    return values.peek().toString();
  }

  private static int precedence(char op) {
    if (op == '+' || op == '-')
      return 1;
    if (op == '*' || op == '/')
      return 2;
    return 0;
  }

  private static Integer applyOp(int a, int b, char op) {
    switch(op) {
      case '+' -> {
        return a + b;
      }
      case '-' -> {
        return a - b;
      }
      case '*' -> {
        return a * b;
      }
      case '/' -> {
        return a / b;
      }
    }
    throw new IllegalArgumentException("Invalid operation provided.");
  }

}
