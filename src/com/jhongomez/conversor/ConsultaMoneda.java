package com.jhongomez.conversor;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConsultaMoneda {
    private static final String[] MONEDAS = {"ARS", "BOB", "BRL", "CLP", "COP", "USD"};

    private final Gson gson = new Gson();
    private final HttpClient cliente = HttpClient.newHttpClient();

    public Map<String, Double> obtenerTasas(String claveApi, String base) {
        String url = "https://v6.exchangerate-api.com/v6/" + claveApi + "/latest/" + base;

        HttpRequest solicitud = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .GET()
            .header("Accept", "application/json")
            .timeout(Duration.ofSeconds(10))
            .build();

        try {
            HttpResponse<String> respuesta =
                cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
            int codigoHttp = respuesta.statusCode();
            String cuerpo = respuesta.body();

            if (codigoHttp != 200) {
                throw new RuntimeException(
                    "Error al consultar la API. Código HTTP: " + codigoHttp
                );
            }

            JsonObject datos = gson.fromJson(cuerpo, JsonObject.class);
            JsonObject tasas = datos.getAsJsonObject("conversion_rates");

            Map<String, Double> tasasFiltradas = new LinkedHashMap<>();

            for (String codigo : MONEDAS) {
                if (tasas.has(codigo)) {
                    tasasFiltradas.put(codigo, tasas.get(codigo).getAsDouble());
                }
            }

            if (tasasFiltradas.isEmpty()) {
                throw new RuntimeException("No se encontraron tasas para las monedas solicitadas");
            }

            return tasasFiltradas;
        } catch (IOException e) {
            throw new RuntimeException("Error de entrada/salida al consultar la API", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("La consulta fue interrumpida", e);
        }
    }
}
