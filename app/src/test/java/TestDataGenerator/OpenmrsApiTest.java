package TestDataGenerator;

import Api.Request;
import Constants.Constant;
import Jsonparser.Parser;
import Openmrs.Openmrs;
import org.apache.http.HttpResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
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
    public void verifyGetLocation() throws IOException {
        HttpResponse response= mrs.setUserLocation(Constant.LOCATION);
        Parser parser=new Parser(response);
        Map<String,String> map=Parser.zipToMap(parser.getValuesForGivenKey("name"),parser.getValuesForGivenKey("uuid"));
        Assertions.assertTrue(map.containsKey(Constant.LOCATION));

    }

}
