package developementexercise.patterns;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import developmentexercise.patterns.AccountDataFormatCorrectnessAnalizer;

public class AccountDataFormatCorrectnessAnalizerTest {

  private AccountDataFormatCorrectnessAnalizer analizer;
  private StringBuilder stringBuilder;

  @BeforeTest
  public void beforeTest() {
    analizer = new AccountDataFormatCorrectnessAnalizer();
    stringBuilder = new StringBuilder();
  }

  @Test
  public void isPasswordCorrectTest() {
    assertFalse(analizer.isPasswordCorrect(null, stringBuilder));
    assertFalse(analizer.isPasswordCorrect("", stringBuilder));
    assertFalse(analizer.isPasswordCorrect("abcde", stringBuilder));
    assertFalse(analizer.isPasswordCorrect("aA4vsg", stringBuilder));
    assertFalse(analizer.isPasswordCorrect("adfgdfgdfgdfg", stringBuilder));
    assertFalse(analizer.isPasswordCorrect("absdf%^$%^", stringBuilder));
    assertFalse(analizer.isPasswordCorrect("AbsssVssssss", stringBuilder));
    assertFalse(analizer.isPasswordCorrect("1sdfsdfss3df2sdf", stringBuilder));
    assertFalse(analizer.isPasswordCorrect("AAAAAAAAAAAA234AA", stringBuilder));
    assertTrue(analizer.isPasswordCorrect("a3cdAfgh", stringBuilder));
    assertTrue(analizer.isPasswordCorrect("a3cdAsdffVgh12", stringBuilder));
    assertTrue(analizer.isPasswordCorrect("a3cdAsdffVgh12", stringBuilder));
  }

  @Test
  public void isUsernameCorrectTest() {
    assertTrue(analizer.isUsernameCorrect("abcde", stringBuilder));
    assertTrue(analizer.isUsernameCorrect("Abc123defgh", stringBuilder));
    assertFalse(analizer.isUsernameCorrect("abcd", stringBuilder));
    assertFalse(analizer.isUsernameCorrect("", stringBuilder));
    assertFalse(analizer.isUsernameCorrect(null, stringBuilder));
  }
}
