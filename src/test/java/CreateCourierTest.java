import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import yandex_sprint_7.CreateCourier;
import yandex_sprint_7.LoginCourier;

import static org.hamcrest.Matchers.*;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_CONFLICT;
import static org.apache.http.HttpStatus.SC_OK;
import static yandex_sprint_7.CourierController.*;

public class CreateCourierTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    public void successSingleTest() {
        CreateCourier createCourier = new CreateCourier("NarutoUzumakovich","12345","NARUTOOOOOOO");
        LoginCourier loginCourier = new LoginCourier("NarutoUzumakovich","12345");
        if (executeLogin(loginCourier).getStatusCode() == SC_OK) {
            executeDelete(loginCourier);
        } else {
            Response response = executeCreate(createCourier);
            response.then().assertThat().body("ok", equalTo(true))
                    .and()
                    .statusCode(SC_CREATED);
        }
    }

    @Test
    public void failDoubleTest() {
        CreateCourier createCourier = new CreateCourier("NarutoUzumakovich","12345","NARUTOOOOOOO");
        executeCreate(createCourier);
        Response response = executeCreate(createCourier);
        response.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(SC_CONFLICT);

    }

    @After
    public void deleteChanges() {
        LoginCourier loginCourier = new LoginCourier("NarutoUzumakovich","12345");
        executeDelete(loginCourier);
    }
}