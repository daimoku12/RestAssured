package restAssured;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import io.restassured.http.Headers;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.XmlConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.http.Header;
import org.apache.http.impl.bootstrap.HttpServer;
import org.hamcrest.Matcher;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.restassured.matcher.ResponseAwareMatcher;
import io.restassured.matcher.RestAssuredMatchers.*;
import io.restassured.matcher.ResponseAwareMatcherComposer.*;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.mapper.ObjectMapperType;
import static io.restassured.path.json.JsonPath.*;
import io.restassured.path.json.JsonPath;

//https://github.com/toddmotto/public-apis/
//http://services.groupkt.com/post/c9b0ccb9/restful-webservices-to-get-and-search-countries.htm
//http://static.javadoc.io/io.rest-assured/rest-assured/3.0.4/io/restassured/RestAssured.html
/*-Dorg.eclipse.ecf.provider.filetransfer.excludeContributors=org.eclipse.ecf.provider.filetransfer.httpclient4
-Dhttp.proxyPort=8080
-Dhttp.proxyHost=webproxy.wlb2.nsroot.net
-Dhttp.nonProxyHosts=localhost|127.0.0.1  */

public class Second 
{  
  //@BeforeClass
  public void setProxy() 
  {
    //RestAssured.proxy("127.0.0.1", 8888);
    //RestAssured.proxy("http://127.0.0.1:8888");
    //System.setProperty("http.proxyHost", "webproxy.wlb2.nsroot.net");
    //System.setProperty("http.proxyPort", "8080");
    //System.setProperty("http.nonProxyHosts", "127.0.0.1");      
    //System.setProperty("java.net.useSystemProxies", "true");
  }  
  
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
       body("RestResponse.result.name", hasItems("Australia","Albania"));
       //body("RestResponse.result.name", hasItems("Australia","Albania")).log().all();     
  
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
  
  //@Test
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
  
  //@Test
  public void testWithoutRoot()
    {
    given().
       get("http://services.groupkt.com/country/get/iso2code/IN").
     then().
         body("RestResponse.result.name", is("India")).
         body("RestResponse.result.alpha2_code", is("IN")).
         body("RestResponse.result.alpha3_code", is("IND"));
    
/*    {
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
  public void testWithRootandDetachRoot()
    {
    given().
       get("http://services.groupkt.com/country/get/iso2code/IN").
     then().
         root("RestResponse.result").
         body("name", is("India")).
         body("alpha2_code", is("IN")).
         detachRoot("result").
         body("result.alpha3_code", is("IND"));
  }
  
  //@Test
  public void testPostRequest()
  {
  given().
    header("Content-Type", "application/x-www-form-urlencoded").
    param("userName", "ratescmtrader").
    param("passWord", "Auto1234").   
  when().    
    post("https://uat.citivelocity.com/portal-auth/stepUpAuth?source=CVDesktopApp").
  then().
    statusCode(302).log().all();
  }
  
  //@Test
  public void testGetResponseAsString()
    {
    String responseString = get("http://services.groupkt.com/country/get/iso2code/IN").asString();
    System.out.println(responseString);
  }
  
  //@Test
  public void testExtractDetailsUsingPath()
    {
/*      {
      "albumId": 1,
      "id": 1,
      "title": "accusamus beatae ad facilis cum similique qui sunt",
      "url": "http://placehold.it/600/92c952",
      "thumbnailUrl": "http://placehold.it/150/92c952"
    }*/
    
    String href = when().
                  get("http://jsonplaceholder.typicode.com/photos/1").
              then().
                  contentType(ContentType.JSON).
                  body("albumId", equalTo(1)).
              extract().
                  path("url");
  
    System.out.println(href);    
    when().get(href).then().statusCode(200);
  }
  
  //@Test
  public void testExtractPathInOneLine()
    {  
    //type 1
    String href1 = get("http://jsonplaceholder.typicode.com/photos/1").path("thumbnailUrl");
    System.out.println(href1);    
    when().get(href1).then().statusCode(200);
    
    //type 2
    String href2 = get("http://jsonplaceholder.typicode.com/photos/1").andReturn().jsonPath().getString("thumbnailUrl");
    System.out.println(href2);    
    when().get(href2).then().statusCode(200);
  }
  
  //@Test
  public void testExtractResponse()
  {
  Response response = 
    
  given().
    param("userName", "ratescmtrader").
    param("passWord", "Auto1234"). 
    header("Content-Type", "application/x-www-form-urlencoded").
  when().    
    post("https://uat.citivelocity.com/portal-auth/stepUpAuth?source=CVDesktopApp").then().extract().response();
  
    System.out.println(response.statusCode());
    System.out.println(response.contentType());
  }
  
  //@Test
  public void testLengthOfResponse()
  {
  when().
       get("http://services.groupkt.com/country/get/all").
   then().
       body("RestResponse.result.alpha3_code*.length().sum()", greaterThan(100));
  }
  
  //@Test
  public void testGetResponseAsList()
  {
  String response = get("http://services.groupkt.com/country/get/all").asString();
  List <String> ls = from(response).getList("RestResponse.result.name");
  System.out.println(ls.size());
  
  for (String country: ls)
  {
    if(country.equals("India"))
      System.out.println("Pass");
  }
  }
  
  //@Test
  public void testConditionsOnList()
  {
  String response = get("http://services.groupkt.com/country/get/all").asString();
  List <String> ls = from(response).getList("RestResponse.result.findAll { it.name.length() > 40 }.name");
  System.out.println(ls);
  //Groovy has an implicit variable called 'it' which represents current item in list
  }
  
  //@Test
  public void testJsonPath1()
  {
  String responseAsString = when().get("http://jsonplaceholder.typicode.com/photos").then().extract().asString();
  
  List <Integer> albumIds = from(responseAsString).get("id");
  System.out.println(albumIds.size()); //Without using Json path
  
/*        {
        "albumId": 1,
        "id": 1,
        "title": "accusamus beatae ad facilis cum similique qui sunt",
        "url": "http://placehold.it/600/92c952",
        "thumbnailUrl": "http://placehold.it/150/92c952"
      },
      {
        "albumId": 1,
        "id": 2,
        "title": "reprehenderit est deserunt velit ipsam",
        "url": "http://placehold.it/600/771796",
        "thumbnailUrl": "http://placehold.it/150/771796"
      }*/  
  }
  
  //@Test
  public void testJsonPath2()
  {
  String json = when().get("http://services.groupkt.com/country/get/all").then().extract().asString();
  JsonPath jsonPath = new JsonPath(json).setRoot("RestResponse.result");
  List <String> list = jsonPath.get("name");
  System.out.println(list.size()); //With using Json path
  
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
        } ]}}*/
   }
  
  //@Test
  public void testResponseHeaders()
  {
  Response response = get("http://jsonplaceholder.typicode.com/photos");  
    
  //to get single Header
  String header1 = response.getHeader("CF-RAY");
  System.out.println(header1);

  //to get all headers
  Headers headers = response.getHeaders();
  for (io.restassured.http.Header h: headers)
  {
    System.out.println(h.getName()+":"+h.getValue());
  }
  }
  
  //@Test
  public void testCookies()
  {
  Response response = get("http://jsonplaceholder.typicode.com/photos");  
  Map<String, String> cookies = response.getCookies();
  
  for(Map.Entry<String, String> entry : cookies.entrySet())
    {
      System.out.println(entry.getKey()+":"+entry.getValue());
    }
  }
  
  //@Test
  public void testDetailedCookies()
  {
  Response response = get("http://jsonplaceholder.typicode.com/photos");  
  
  Cookie a = response.getDetailedCookie("__cfduid");
  System.out.println(a.hasExpiryDate());
  System.out.println(a.getExpiryDate());
  System.out.println(a.hasValue());
  }
  
  //@Test
  public void testConnectRequest()
  {
    when().request("CONNECT", "https://api.fonts.com/rest/json/Accounts/").then().statusCode(400);   
  }
  
  //@Test
  public void testQueryParameters()
  {
    given().
         queryParam("A", "A val").
         queryParam("B", "B val").
    when().
       get("CONNECT", "https://api.fonts.com/rest/json/Accounts/").then().statusCode(400);
    
    //https://www.youtube.com/watch?v=ZJ_2k07TUbw&index=5&list=PLEiBaBxmVLi-hoi61aX-2agQb8EXSCT5f
    //?v=ZJ_2k07TUbw (v - key, value - ZJ_2k07TUbw)
    //Query parameters are set in GET request
  }
  
  //@Test
  public void testFormParameters()
  {
    given().
         formParam("A", "A val").
         formParam("B", "B val").
    when().
       post("CONNECT", "https://api.fonts.com/rest/json/Accounts/").then().statusCode(400);   
  }
  
  //@Test
  public void testSetParameters()
  {
    given().
         param("A", "A val").
         param("B", "B val").
    when().
       post("CONNECT", "https://api.fonts.com/rest/json/Accounts/").then().statusCode(400);
    //If request is GET - param is treated as FormParameter
    //If request is POST - param is treated as QueryParameter
  }
  
  //@Test
  public void setMultiValueParameters()
  {
    List<String> list = new ArrayList<String>();
    list.add("one");
    list.add("two");
    
    given().
         param("A", "val1", "val2", "val3").
         param("B", "B val").
         param("C", list).
    when().
       post("CONNECT", "https://api.fonts.com/rest/json/Accounts/").then().statusCode(400);
  }
  
  //@Test
  public void testSetPathParameters()
  {
    given().
         pathParam("type", "json").
         pathParam("section", "Domains").
    when().
       post("CONNECT", "https://api.fonts.com/rest/{type}/{section}/").then().statusCode(400);   
  }
  
  //@Test
  public void testSetCookiesInRequest()
  {
    given().
         cookie("__utmt", "1").
   when().
       get("http://www.webservicex.com/globalweather.asmx?op=GetCitiesByCountry").then().statusCode(400);   
  }
  
  //@Test
  public void testSetMultipleCookiesInRequest()
  {
    //to set multi value
  given().cookie("key", "val1", "val2"); //this will create 2 cookies: key = val1; key = val2
  
  //to set detailed cookie
  Cookie cookie = new Cookie.Builder("some_cookie", "some_value").setSecured(true).setComment("some comment").build();
  given().cookie(cookie).when().get("/cookie").then().assertThat().body(equalTo("x"));
  
  //to set multiple detailed cookies
  Cookie cookie1 = new Cookie.Builder("some_cookie", "some_value").setSecured(true).setComment("some comment").build();
  Cookie cookie2 = new Cookie.Builder("some_cookie", "some_value").setSecured(true).setComment("some comment").build();
  Cookies cookies = new Cookies(cookie1, cookie2);
  given().cookies(cookies).when().get("/cookie").then().assertThat().body(equalTo("x"));
  }
  
  //@Test
  public void testSetHeaders()
  {
  given().header("k", "v").header("k2", "val1", "val2").header("k1", "v1", "v2", "v3").when().get("https://api.fonts.com/rest/json/Accounts/").then().statusCode(400);
  //We can pass single header, header with multiple values and multiple headers
  }
  
  //@Test
  public void testSetContentType()
  {
  given().contentType(ContentType.JSON).contentType("application/json; charset=uft-8").when().get("https://api.fonts.com/rest/json/Accounts/").then().statusCode(400);
  //We can pass single header, header with multiple values and multiple headers
  }
  
  //@Test
  public void testStatusInResponse()
  {  
  given().get("http://jsonplaceholder.typicode.com/photos").then().assertThat().statusCode(200).log().all();
  given().get("http://jsonplaceholder.typicode.com/photos").then().assertThat().statusLine("HTTP/1.1 200 OK");
  given().get("http://jsonplaceholder.typicode.com/photos").then().assertThat().statusLine(containsString("OK"));
  }
  
  //@Test
  public void testHeadersInResponse()
  {  
  given().get("http://jsonplaceholder.typicode.com/photos").then().assertThat().header("X-Powered-By", "Express");
  given().get("http://jsonplaceholder.typicode.com/photos").then().assertThat().headers("Vary", "Accept-Encoding", "Content-Type", containsString("json"));
  }
  
  //@Test
  public void testContentTypeInResponse()
  {  
  given().get("http://jsonplaceholder.typicode.com/photos").then().assertThat().contentType(ContentType.JSON);
  }
  
  //@Test
  public void testBodyInResponse()
  {  
  String responseString = get("http://www.thomas-bayer.com/sqlrest/CUSTOMER/02/").asString();  
  given().get("http://www.thomas-bayer.com/sqlrest/CUSTOMER/02/").then().assertThat().body(equalTo(responseString));
  
/*  <CUSTOMER xmlns:xlink="http://www.w3.org/1999/xlink">
  <ID>2</ID>
  <FIRSTNAME>Anne</FIRSTNAME>
  <LASTNAME>Miller</LASTNAME>
  <STREET>20 Upland Pl.</STREET>
  <CITY>Lyon</CITY>
  </CUSTOMER>*/
  }
  
  //@Test
  public void testBodyParametersInResponse()
  {  
  given().get("http://jsonplaceholder.typicode.com/photos/1").then().body("thumbnailUrl", endsWith("92c952"));
  given().get("http://jsonplaceholder.typicode.com/photos/1").then().body("thumbnailUrl", response -> equalTo("http://placehold.it/150/92c952"));
/*     {
      "albumId": 1,
      "id": 1,
      "title": "accusamus beatae ad facilis cum similique qui sunt",
      "url": "http://placehold.it/600/92c952",
      "thumbnailUrl": "http://placehold.it/150/92c952"
    }*/
  }
  
  //@Test
  public void testCookiesInResponse()
  {  
  given().get("http://jsonplaceholder.typicode.com/comments").then().log().all().assertThat().cookie("cookieName", "cookieValue");
  }
  
  //@Test
  public void testResponseTime()
  {
   long t = given().get("").time();
   System.out.println(t);
   
   long t1 = given().get("http://jsonplaceholder.typicode.com/comments").timeIn(TimeUnit.MILLISECONDS);
   System.out.println(t1);
   
   given().get("http://jsonplaceholder.typicode.com/comments").then().time(lessThan(500L));
  }  
}
