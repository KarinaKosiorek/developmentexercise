package developmentexercise.databaseservice.model;

public interface RegistrationService {

  public boolean init();

  public boolean accountExists(String username) throws Exception;

  public void addAccount(String username, String password) throws Exception;

  public void close();
}
