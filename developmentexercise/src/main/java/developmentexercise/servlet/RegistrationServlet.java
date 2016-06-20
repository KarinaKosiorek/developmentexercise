package developmentexercise.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import developmentexercise.databaseservice.DbaXmlFileRegistrationService;
import developmentexercise.databaseservice.RuntimeRegistrationService;
import developmentexercise.databaseservice.model.RegistrationService;
import developmentexercise.patterns.AccountDataFormatCorrectnessAnalizer;

public class RegistrationServlet extends HttpServlet {

  public final static Logger LOGGER = LoggerFactory.getLogger(RegistrationServlet.class);
  private static final long serialVersionUID = -4802159941011807667L;
  private RegistrationService registrationService = null;
  private AccountDataFormatCorrectnessAnalizer accountDataPatternMatcher = new AccountDataFormatCorrectnessAnalizer();

  @Override
  public void init(ServletConfig servletConfig) throws ServletException {
    registrationService = new DbaXmlFileRegistrationService();
    if (!registrationService.init()) {
      LOGGER.info("Error with database service initialization. Service will store accounts only during runtime!");
      registrationService = new RuntimeRegistrationService();
    } else {
      LOGGER.info("Database service initialized.");
    }
    if (registrationService == null) {
      LOGGER.info("Registration service not initialized. Closing application.");
      System.exit(-1);
    }
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    LOGGER.info("Post method execution");

    String username = request.getParameter("username");
    String password = request.getParameter("password");
    String message = "";
    String error = "";

    synchronized (registrationService) {
      try {
        LOGGER.info("Checking username length.");
        StringBuilder errorMessageBuilder = new StringBuilder("");
        if (!accountDataPatternMatcher.isUsernameCorrect(username, errorMessageBuilder)) {
          error = errorMessageBuilder.toString();
          LOGGER.info(error);
          dispatchRequest(username, password, message, error, request, response);
          return;
        }

        LOGGER.info("Checking if account with the given username already exists.");
        if (registrationService.accountExists(username)) {
          error = "Error: username already exists!";
          LOGGER.info(error);
          dispatchRequest(username, password, message, error, request, response);
          return;
        }

        LOGGER.info("Checking if password correct.");
        errorMessageBuilder = new StringBuilder("");
        if (!accountDataPatternMatcher.isPasswordCorrect(password, errorMessageBuilder)) {
          error = errorMessageBuilder.toString();
          LOGGER.info(error);
          dispatchRequest(username, password, message, error, request, response);
          return;
        }

        LOGGER.info("Registering account.");
        registrationService.addAccount(username, password);
        LOGGER.info("Account registered.");

        LOGGER.info("Going back to registration site.");
        dispatchRequest("", "", "Account registered!", null, request, response);
      } catch (Exception e) {
        error = "Server error: \n" + e.getMessage();
        dispatchRequest(username, password, message, error, request, response);
      }
    }
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doPost(request, response);
  }

  @Override
  public void destroy() {
    registrationService.close();
  }

  private void dispatchRequest(String username, String password, String message, String error, HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    request.setAttribute("error", error);
    request.setAttribute("message", message);
    request.setAttribute("username", username);
    request.setAttribute("password", password);
    request.getRequestDispatcher("registration.jsp").forward(request, response);
  }
}