package restAssured;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.response.Response;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.XmlConfig;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.hamcrest.Matcher;
import org.testng.annotations.Test;
import io.restassured.matcher.ResponseAwareMatcher;
import io.restassured.matcher.RestAssuredMatchers.*;
import io.restassured.matcher.ResponseAwareMatcherComposer.*;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.mapper.ObjectMapperType;
import static io.restassured.path.json.JsonPath.*;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;

//https://github.com/toddmotto/public-apis/
//http://services.groupkt.com/post/c9b0ccb9/restful-webservices-to-get-and-search-countries.htm

public class Second 
{
  //@Test
  public void first()
  {
  Response response = (Response) 
    
  given().
    param("userName", "ratescmtrader").
    param("passWord", "Auto1234"). 
    header("Content-Type", "application/x-www-form-urlencoded").
  when().    
    post("https://uat.citivelocity.com/portal-auth/stepUpAuth?source=CVDesktopApp");
      
  Map<String, String> cookies = response.getCookies();
  
  for(Map.Entry<String, String> entry : cookies.entrySet())
    {
      System.out.println(entry.getKey()+":"+entry.getValue());
    }
  }
  
  //@Test
  public void testStatusCode()
  {
  given().get("http://services.groupkt.com/country/get/iso2code/IN").then().statusCode(200);
    
  }
  
  //@Test
  public void testLogging()
  {
  given().get("http://services.groupkt.com/country/get/iso2code/IN").then().statusCode(200).log().all();
  
/*  {
    "RestResponse" : {
      "messages" : [ "Country found matching code [IN]." ],
      "result" : {
        "name" : "India",
        "alpha2_code" : "IN",
        "alpha3_code" : "IND"
      }
    }
    }*/
  }
  
  //@Test
  public void testEqualToFunction()
  {
  given().
       get("http://services.groupkt.com/country/get/iso2code/IN").
   then().
       body("RestResponse.result.name",equalTo("India"));
  }
  
  //@Test
  public void testHasItemFunction()
  {
  given().
       get("http://services.groupkt.com/country/get/all").
   then().
       body("RestResponse.result.name",hasItems("Australia","Albania"));
  
/*  {
      "RestResponse" : {
        "messages" : [ "Total [249] records found." ],
        "result" : [ {
          "name" : "Afghanistan",
          "alpha2_code" : "AF",
          "alpha3_code" : "AFG"
        }, {
          "name" : "Albania",
          "alpha2_code" : "AL",
          "alpha3_code" : "ALB"
        }, {
            "name" : "Zimbabwe",
            "alpha2_code" : "ZW",
            "alpha3_code" : "ZWE"
          } ]
        }
  }*/
  }  
  
  //@Test
  public void testXML()
    {
    given().
       get("http://www.thomas-bayer.com/sqlrest/CUSTOMER/10/").
     then().
       body("CUSTOMER.ID",equalTo("10")).
       log().all();
    
/*    <CUSTOMER xmlns:xlink="http://www.w3.org/1999/xlink">
      <ID>10</ID>
      <FIRSTNAME>Sue</FIRSTNAME>
      <LASTNAME>Fuller</LASTNAME>
      <STREET>135 Upland Pl.</STREET>
      <CITY>Dallas</CITY>
    </CUSTOMER>*/
   }  
  
  //@Test
  public void testXMLCompleteText()
    {
    given().
       get("http://www.thomas-bayer.com/sqlrest/CUSTOMER/10/").
     then().
       body("CUSTOMER.text()",equalTo("10SueFuller135 Upland Pl.Dallas")).
       log().all();    
    }  
  
  //@Test
  public void testXPath()
    {
    given().
       get("http://www.thomas-bayer.com/sqlrest/CUSTOMER/10/").
     then().
       body(hasXPath("/CUSTOMER/FIRSTNAME", containsString("Sue")));    
    }
  
  @Test
  public void testXPath1()
    {
    given().
       get("http://www.thomas-bayer.com/sqlrest/INVOICE/").
     then().
       body(hasXPath("/INVOICEList/INVOICE[text()='40']")).log().all();
    
/*    <INVOICEList xmlns:xlink="http://www.w3.org/1999/xlink">
    <INVOICE xlink:href="http://www.thomas-bayer.com/sqlrest/INVOICE/0/">0</INVOICE>
    <INVOICE xlink:href="http://www.thomas-bayer.com/sqlrest/INVOICE/24/">24</INVOICE>
    <INVOICE xlink:href="http://www.thomas-bayer.com/sqlrest/INVOICE/37/">37</INVOICE>
    <INVOICE xlink:href="http://www.thomas-bayer.com/sqlrest/INVOICE/38/">38</INVOICE>
    <INVOICE xlink:href="http://www.thomas-bayer.com/sqlrest/INVOICE/39/">39</INVOICE>
    <INVOICE xlink:href="http://www.thomas-bayer.com/sqlrest/INVOICE/40/">40</INVOICE>
    <INVOICE xlink:href="http://www.thomas-bayer.com/sqlrest/INVOICE/41/">41</INVOICE>
    </INVOICEList>*/  
    }
}