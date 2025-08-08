package com.zenware.producto.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class ProductoResourceTest {

    @Test
    void crear_y_obtener_ok() {
        var body = """
            { "nombre": "Trapeador", "precio": 25000 }
        """;

        var location =
                given()
                        .contentType(ContentType.JSON)
                        .body(body)
                        .when()
                        .post("/productos")
                        .then()
                        .statusCode(201)
                        .body("id", notNullValue())
                        .extract().header("Location");

        given()
                .when()
                .get(location)
                .then()
                .statusCode(200)
                .body("nombre", equalTo("Trapeador"))
                .body("precio", equalTo(25000.0f));
    }

    @Test
    void obtener_noEncontrado_404() {
        given()
                .when()
                .get("/productos/999999")
                .then()
                .statusCode(404)
                .body("code", equalTo(404))
                .body("message", equalTo("No encontrado"));
    }

    @Test
    void crear_conPrecioNegativo_400() {
        var body = """
            { "nombre": "Fregona", "precio": -1 }
        """;

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/productos")
                .then()
                .statusCode(400)
                .body("code", equalTo(400))
                .body("message", containsString("no puede ser negativo"));
    }

    @Test
    void eliminar_y_luego_404() {
        // crear
        var id =
                given()
                        .contentType(ContentType.JSON)
                        .body("{\"nombre\":\"Escoba\",\"precio\":1000}")
                        .when()
                        .post("/productos")
                        .then()
                        .statusCode(201)
                        .extract().jsonPath().getLong("id");

        // eliminar
        given().when()
                .delete("/productos/" + id)
                .then()
                .statusCode(204);

        // verificar 404
        given().when()
                .get("/productos/" + id)
                .then()
                .statusCode(404);
    }
}

