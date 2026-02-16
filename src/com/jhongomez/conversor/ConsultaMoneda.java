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

    private final Gson gson = new Gson();

    public Map<String, Double> obtenerTasasFiltradas(String apiKey, String base) {
        String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + base;

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .GET()
            .header("Accept", "application/json")
            .timeout(Duration.ofSeconds(10))
            .build();

        try {
            HttpResponse<String> response =
                HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            String body = response.body();

            if (statusCode != 200) {
                throw new RuntimeException(
                    "Error al consultar /latest. Código HTTP: " + statusCode + ". Body: " + body
                );
            }

            JsonObject json = gson.fromJson(body, JsonObject.class);
            JsonObject rates = json.getAsJsonObject("conversion_rates");

            Map<String, Double> tasasFiltradas = new LinkedHashMap<>();
            String[] codigos = {"ARS", "BOB", "BRL", "CLP", "COP", "USD"};

            for (String codigo : codigos) {
                if (rates.has(codigo)) {
                    tasasFiltradas.put(codigo, rates.get(codigo).getAsDouble());
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
