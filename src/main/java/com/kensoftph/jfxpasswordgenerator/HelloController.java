package com.kensoftph.jfxpasswordgenerator;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.util.Duration;

import java.util.Random;

public class HelloController {

    private String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private String lowercase = "abcdefghijklmnopqrstuvwxyz";
    private String numbers = "0123456789";
    private String specialchars = "~!@#$%^&*+-/.,\\{}[]();:?<>'_";
    private String generatedPassword = "";

    private Timeline timeline;

    @FXML
    private CheckBox checkboxLowercase;

    @FXML
    private CheckBox checkboxNumbers;

    @FXML
    private CheckBox checkboxSpecialCharacters;

    @FXML
    private CheckBox checkboxUppercase;

    @FXML
    private Label labelPassword;

    @FXML
    private Label labelStrength;

    @FXML
    private TextField textField;

    @FXML
    void buttonCopy(ActionEvent event) {
        if (generatedPassword != null) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(generatedPassword);
            clipboard.setContent(content);
            labelPassword.setText("Copied to clipboard");
        } else {
            labelPassword.setText("No password to copy");
        }
    }

    @FXML
    void buttonGenerate(ActionEvent event) {
        String passwordTextLength = textField.getText();
        try {
            int passwordLength = Integer.parseInt(passwordTextLength);
            if (passwordLength < 4 || passwordLength > 99) {
                labelPassword.setText("Password length must be between 4 and 99");
                labelStrength.setText("");
                return;
            }

            String characterSet = generateCharacterSet(
                    checkboxUppercase.isSelected(),
                    checkboxLowercase.isSelected(),
                    checkboxNumbers.isSelected(),
                    checkboxSpecialCharacters.isSelected()
            );

            generatePassword(passwordLength, characterSet);
            updatePasswordStrength(passwordLength);
        } catch (NumberFormatException e) {
            System.out.println(e);
        }
    }

    private String generateCharacterSet(boolean isUppercase, boolean isLowercase, boolean isNumbers, boolean isSpecialCharacters) {
        StringBuilder characterSet = new StringBuilder();
        if (isUppercase) {
            characterSet.append(uppercase);
        }

        if (isLowercase) {
            characterSet.append(lowercase);
        }

        if (isNumbers) {
            characterSet.append(numbers);
        }

        if (isSpecialCharacters) {
            characterSet.append(specialchars);
        }

        return characterSet.toString();
    }

    private void generatePassword(int passwordLength, String characterSet) {
        if (characterSet.isEmpty()) {
            labelPassword.setText("Select at least one character set");
            labelStrength.setText("");
            return;
        }

        generatedPassword = generateRandomPassword(passwordLength, characterSet);

    }

    private String generateRandomPassword(int passwordLength, String characterSet) {
        Random random = new Random();
        StringBuilder password = new StringBuilder();

        labelPassword.setText("");

        if(timeline != null){
            timeline.stop();
        }

        timeline = new Timeline();
        for (int i = 0; i < passwordLength; i++) {
            password.append(characterSet.charAt(random.nextInt(characterSet.length())));

            int index = i;
            KeyFrame keyFrame = new KeyFrame(Duration.millis(20 * i), event -> {
                labelPassword.setText(labelPassword.getText() + generatedPassword.charAt(index));
            });

            timeline.getKeyFrames().add(keyFrame);
        }

        timeline.play();

        return password.toString();
    }

    private void updatePasswordStrength(int passwordLength) {
        String strength;
        if (passwordLength > 15) {
            strength = "Strong";
        } else if (passwordLength >= 7) {
            strength = "Medium";
        } else {
            strength = "Weak";
        }

        labelStrength.setText("Password Strength: " + strength);
    }

}
