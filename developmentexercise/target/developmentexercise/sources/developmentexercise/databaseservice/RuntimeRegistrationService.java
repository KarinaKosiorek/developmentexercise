package developmentexercise.databaseservice;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import developmentexercise.databaseservice.model.RegistrationService;

/**
 * Used only in case DbaXmlFileRegistrationService could not be started
 */
public class RuntimeRegistrationService implements RegistrationService {

  public final static Logger LOGGER = LoggerFactory.getLogger(RuntimeRegistrationService.class);

  private ConcurrentHashMap<String, String> accounts = new ConcurrentHashMap<String, String>();

  @Override
  public boolean init() {
    return (accounts != null);
  }

  @Override
  public void close() {
    // empty
  }

  @Override
  public boolean accountExists(String username) throws Exception {
    return accounts.get(username) == null ? false : true;
  }

  @Override
  public void addAccount(String username, String password) throws Exception {
    accounts.put(username, password);
  }
}
