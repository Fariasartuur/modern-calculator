package org.openjfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.List;

public class Controler {

    @FXML
    private TextArea displayArea;

    @FXML
    private Label historyLabel;

    private final List<Double> numbers = new ArrayList<>();
    private final List<String> operators = new ArrayList<>();

    private boolean newNumber = true;
    private boolean calculationFinished = false;

    @FXML
    private void handleNumberClick(ActionEvent event){
        if(newNumber){
            displayArea.setText("");
            if (calculationFinished) {
                historyLabel.setText("");
                calculationFinished = false;
            }
            newNumber = false;
        }

        if (displayArea.getText().length() >= 15) {
            return;
        }

        String buttonValue = ((Button) event.getSource()).getText();
        if(buttonValue.equals(",")){
            if(displayArea.getText().contains(".")) return;
            buttonValue = ".";
        }
        displayArea.appendText(buttonValue);
    }

    @FXML
    private void handleOperatorClick(ActionEvent event){
        String operator = ((Button) event.getSource()).getText();

        if (newNumber && !operators.isEmpty()) {
            operators.set(operators.size() - 1, operator);

            String currentHistory = historyLabel.getText();
            String newHistory = currentHistory.substring(0, currentHistory.length() - 3); // Remove " op "
            historyLabel.setText(newHistory + " " + operator + " ");
            return;
        }

        String currentNumberText = displayArea.getText();
        if(currentNumberText.isEmpty()) return;

        historyLabel.setText(historyLabel.getText() + currentNumberText + " " + operator + " ");

        numbers.add(Double.parseDouble(currentNumberText));
        operators.add(operator);
        newNumber = true;
        calculationFinished = false;
    }

    @FXML
    private void handleEqualsClick(ActionEvent event){
        String lastNumberText = displayArea.getText();
        if(lastNumberText.isEmpty() || operators.isEmpty()) return;

        historyLabel.setText(historyLabel.getText() + lastNumberText + " = ");

        numbers.add(Double.parseDouble(lastNumberText));

        for (int i = 0; i < operators.size(); i++) {
            String op = operators.get(i);
            if (op.equals("×") || op.equals("÷") || op.equals("*") || op.equals("/")) {
                double num1 = numbers.get(i);
                double num2 = numbers.get(i + 1);
                double result = calculate(num1, num2, op);

                if(Double.isNaN(result)) {
                    displayArea.setText("Erro");
                    handleClearClick(null);
                    return;
                }

                numbers.set(i, result);

                numbers.remove(i + 1);
                operators.remove(i);

                i--;
            }
        }

        double finalResult = numbers.get(0);
        for (int i = 0; i < operators.size(); i++) {
            finalResult = calculate(finalResult, numbers.get(i + 1), operators.get(i));
        }

        if (finalResult % 1 == 0) {
            displayArea.setText(String.format("%.0f", finalResult));
        } else {
            displayArea.setText(String.valueOf(finalResult));
        }

        numbers.clear();
        operators.clear();
        newNumber = true;
        calculationFinished = true;
    }

    @FXML
    private void handleClearClick(ActionEvent event){
        displayArea.setText("");
        historyLabel.setText("");
        numbers.clear();
        operators.clear();
        newNumber = true;
        calculationFinished = true;
    }

    @FXML
    private void handlePercentageClick(ActionEvent event){
        String visorText = displayArea.getText();

        if(visorText.isEmpty()) return;

        double currentNumber = Double.parseDouble(visorText);
        double percentResult;

        if (newNumber || numbers.isEmpty()) {
            historyLabel.setText(visorText + "% =");
            percentResult = currentNumber / 100.0;
            calculationFinished = true;
            newNumber = true;
        }
        else {
            double baseNumber = numbers.get(numbers.size() - 1);
            percentResult = baseNumber * (currentNumber / 100.0);

        }

        String formattedResult;
        if (percentResult % 1 == 0) {
            formattedResult = String.format("%.0f", percentResult);
        } else {
            formattedResult = String.valueOf(percentResult);
        }

        displayArea.setText(formattedResult);
    }

    @FXML
    private void handleBackspaceClick(ActionEvent event){
        if (newNumber && !operators.isEmpty()) {
            operators.remove(operators.size() - 1);

            double lastNumber = numbers.remove(numbers.size() - 1);

            if (lastNumber % 1 == 0) {
                displayArea.setText(String.format("%.0f", lastNumber));
            } else {
                displayArea.setText(String.valueOf(lastNumber));
            }

            String currentHistory = historyLabel.getText();
            String newHistory = currentHistory.substring(0, currentHistory.length() - 3); // Remove o " op "
            historyLabel.setText(newHistory);

            newNumber = false;
            return;
        }

        String text = displayArea.getText();
        if(!text.isEmpty()){
            if(newNumber) return;

            String newText = text.substring(0, text.length() - 1);
            displayArea.setText(newText);
        }
    }

    @FXML
    private void handleInvertSignClick(ActionEvent event){
        String visorText = displayArea.getText();

        if(visorText.isEmpty() || visorText.equals("0")){
            return;
        }

        if (newNumber) {
            historyLabel.setText("");
            calculationFinished = false;
        }

        double currentNumber = Double.parseDouble(visorText);
        double invertedNumber = currentNumber * -1;

        String formattedResult;
        if (invertedNumber % 1 == 0) {
            formattedResult = String.format("%.0f", invertedNumber);
        } else {
            formattedResult = String.valueOf(invertedNumber);
        }

        displayArea.setText(formattedResult);
        newNumber = false;
    }

    private double calculate(double num1, double num2, String operador) {
        switch (operador) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "*":
                return num1 * num2;
            case "/":
                if (num2 == 0) {
                    return Double.NaN;
                }
                return num1 / num2;
            default:
                return num2;
        }
    }
}
