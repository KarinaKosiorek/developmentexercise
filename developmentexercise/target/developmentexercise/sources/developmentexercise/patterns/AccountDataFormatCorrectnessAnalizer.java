package developmentexercise.patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountDataFormatCorrectnessAnalizer {

  public boolean isPasswordCorrect(String password, StringBuilder errorMessage) {
    if (password == null || password.isEmpty() || password.length() < 8) {
      errorMessage.append("Error: the password length must be no less than 8 characters. ");
      return false;
    }

    boolean characterMatchingAccepted = true;

    Pattern patternAtLeastOneNumber = Pattern.compile("[0-9]");
    Matcher matcherAtLeastOneNumber = patternAtLeastOneNumber.matcher(password);
    if (!matcherAtLeastOneNumber.find()) {
      characterMatchingAccepted = false;
    }

    Pattern patternAtLeastOneLowercase = Pattern.compile("[a-z]");
    Matcher matacherAtLeastOneLowercase = patternAtLeastOneLowercase.matcher(password);
    if (!matacherAtLeastOneLowercase.find()) {
      characterMatchingAccepted = false;
    }

    Pattern patternAtLeastOneUppercase = Pattern.compile("[A-Z]");
    Matcher matcherAtLeastOneUppercase = patternAtLeastOneUppercase.matcher(password);
    if (!matcherAtLeastOneUppercase.find()) {
      characterMatchingAccepted = false;
    }

    if (!characterMatchingAccepted) {
      errorMessage.append("Error: password must contains at least 1 number, 1 uppercase, and 1 lowercase character. ");
    }
    return characterMatchingAccepted;
  }

  public boolean isUsernameCorrect(String username, StringBuilder errorMessage) {
    if (username == null || username.isEmpty() || username.length() < 5) {
      errorMessage.append("Error: the username length must be no less than 5 characters!");
      return false;
    }
    return true;
  }
}
