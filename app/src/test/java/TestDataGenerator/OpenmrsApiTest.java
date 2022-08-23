package TestDataGenerator;

import Api.Request;
import Constants.Constant;
import Jsonparser.Parser;
import Api.Openmrs;
import org.apache.http.HttpResponse;
import org.junit.jupiter.api.*;

import java.io.IOException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OpenmrsApiTest
{
    Openmrs mrs=new Openmrs();

    @Test
    @Order(1)
    public void verifyLogin()
    {
       HttpResponse response= mrs.login(Constant.USERNAME,Constant.PASSWORD);
       Assertions.assertEquals(200,response.getStatusLine().getStatusCode());

    }
    @Test
    @Order(2)
    public void verifyGetSessionId() throws IOException {
        HttpResponse response= mrs.getSessionId();
        Assertions.assertEquals(200,response.getStatusLine().getStatusCode());
        Parser parser=new Parser(response);
        Assertions.assertEquals("true",parser.get("authenticated").toString());
        Assertions.assertTrue(Request.getCookieStore().toString().contains("reporting_session"));
        Assertions.assertTrue(Request.getCookieStore().toString().contains("JSESSIONID"));


    }
    @Test
    @Order(3)
    public void verifyGetLocation() throws IOException
    {
        mrs.setUserLocation(Constant.LOCATION);
        Assertions.assertNotNull(Request.getUuid());

    }

}
