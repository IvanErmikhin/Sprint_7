import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import yandex_sprint_7.CreateCourier;
import yandex_sprint_7.LoginCourier;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static yandex_sprint_7.CourierController.*;

public class LoginCourierTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }
    @Test
    public void successCourierLoginTest() {
        CreateCourier createCourier = new CreateCourier("NarutoUzumakovich","12345","NARUTOOOOOOO");
        executeCreate(createCourier);
        LoginCourier loginCourier = new LoginCourier("NarutoUzumakovich","12345");
        Response response = executeLogin(loginCourier);
        response.then().assertThat().body("id", notNullValue())
                .and()
                .statusCode(SC_OK);
    }

    @Test
    public void failLoginNonExistCourierTest() {
        LoginCourier loginCourier = new LoginCourier("JolineKudjo","asdksa%jkhdk!");
        Response response = executeLogin(loginCourier);
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(SC_NOT_FOUND);
    }

    @Test
    public void failLoginWithWrongLoginTest() {
        LoginCourier loginCourier = new LoginCourier("NarutoUzumakovich123","12345");
        Response response = executeLogin(loginCourier);
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(SC_NOT_FOUND);
    }

    @Test
    public void failLoginWithWrongPasswordTest() {
        LoginCourier loginCourier = new LoginCourier("NarutoUzumakovich","123452937dsfhjl");
        Response response = executeLogin(loginCourier);
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(SC_NOT_FOUND);
    }

    @After
    public void deleteChanges() {
        LoginCourier loginCourier = new LoginCourier("NarutoUzumakovich","12345");
        executeDelete(loginCourier);
    }
}