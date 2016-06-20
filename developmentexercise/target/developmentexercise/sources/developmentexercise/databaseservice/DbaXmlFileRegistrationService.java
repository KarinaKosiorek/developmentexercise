package developmentexercise.databaseservice;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import developmentexercise.databaseservice.model.RegistrationService;

public class DbaXmlFileRegistrationService implements RegistrationService {

  public final static Logger LOGGER = LoggerFactory.getLogger(DbaXmlFileRegistrationService.class);

  public final static String DBA_XMLFILE_NAME = "developmentexercise-accounts.xml";
  private File dbaxmlfile = null;
  private String homePath = "";

  private Document xmlFileDocument = null;

  private final static String xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
  private final static String xmlRoot = "<accounts id=\"accounts\"></accounts>";

  public DbaXmlFileRegistrationService() {
  }

  public boolean init() {
    try {
      LOGGER.info("Initialization");

      initDbaXmlFile();
      if (dbaxmlfile == null) {
        LOGGER.error("Cannot create or access database xml file " + DBA_XMLFILE_NAME + " in user home directory " + homePath);
        return false;
      }

      initDbaXmlFactory();
      if (xmlFileDocument == null) {
        LOGGER.error("Cannot create database builder from .xml file : " + dbaxmlfile.getPath());
        return false;
      }
    } catch (Exception e) {
      LOGGER.error("Error with database service intialization: " + e.getMessage());
      return false;
    }
    return true;
  }

  @Override
  public void close() {
    // empty
  }

  public boolean accountExists(String username) throws Exception {
    XPath xpath = XPathFactory.newInstance().newXPath();
    String query = "/accounts/account[username=\'" + username + "\']";
    XPathExpression expr = xpath.compile(query);
    Object evelObject = expr.evaluate(xmlFileDocument, XPathConstants.NODESET);
    NodeList nodeList = (NodeList) evelObject;
    if (nodeList.getLength() > 0) {
      return true;
    }
    return false;
  }

  public void addAccount(String username, String password) throws Exception {
    createNewAccount(username, password);
    saveToDatabase();
  }

  private void initDbaXmlFile() throws Exception {
    LOGGER.info("Initiating database xml file.");

    homePath = System.getProperty("user.home");
    LOGGER.info("User directory : " + homePath);

    String separator = File.separator;

    dbaxmlfile = new File(homePath + separator + DBA_XMLFILE_NAME);
    LOGGER.info("Database file : " + dbaxmlfile.getAbsolutePath());

    if (!dbaxmlfile.exists()) {
      dbaxmlfile.createNewFile();
      FileWriter fileWriter = new FileWriter(dbaxmlfile);
      BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
      bufferedWriter.write(xmlHeader + "\n" + xmlRoot);
      bufferedWriter.close();
    }
  }

  private void initDbaXmlFactory() throws ParserConfigurationException, SAXException, IOException {
    LOGGER.info("Creating database builder.");
    DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
    if (domFactory != null) {
      domFactory.setIgnoringComments(true);
      DocumentBuilder builder = domFactory.newDocumentBuilder();
      if (builder != null) {
        xmlFileDocument = builder.parse(dbaxmlfile);
      }
    }
  }

  private void createNewAccount(String username, String password) throws Exception {
    NodeList accounts = xmlFileDocument.getElementsByTagName("accounts");
    Element accountTag = xmlFileDocument.createElement("account");
    Element usernameTag = xmlFileDocument.createElement("username");
    Element passwordTag = xmlFileDocument.createElement("password");
    Text usernameText = xmlFileDocument.createTextNode(username);
    Text passwordText = xmlFileDocument.createTextNode(password);
    usernameTag.appendChild(usernameText);
    passwordTag.appendChild(passwordText);
    accountTag.appendChild(usernameTag);
    accountTag.appendChild(passwordTag);
    accounts.item(0).appendChild(accountTag);
    LOGGER.info("Successfully created new account.");
  }

  private void saveToDatabase() throws Exception {
    Transformer transformer;
    transformer = TransformerFactory.newInstance().newTransformer();
    StreamResult result = new StreamResult(new FileOutputStream(dbaxmlfile));
    DOMSource source = new DOMSource(xmlFileDocument);
    transformer.transform(source, result);
    LOGGER.info("New account added to database.");
  }
}
