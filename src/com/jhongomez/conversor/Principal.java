package com.jhongomez.conversor;

import java.text.DecimalFormat;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class Principal {

    public static void main(String[] args) {
        String apiKey = System.getenv("EXCHANGE_API_KEY");

        if (apiKey == null || apiKey.isBlank()) {
            System.out.println("No se encontró API key.");
            System.out.println("Configura la variable de entorno EXCHANGE_API_KEY.");
            return;
        }

        ConsultaMoneda consulta = new ConsultaMoneda();
        Scanner scanner = new Scanner(System.in);
        DecimalFormat df = new DecimalFormat("#,##0.00");

        System.out.println("Sea bienvenido/a al Conversor de Moneda");

        boolean continuar = true;
        while (continuar) {
            mostrarMenu();

            String opcionTexto = scanner.nextLine();
            int opcion;

            try {
                opcion = Integer.parseInt(opcionTexto);
            } catch (NumberFormatException e) {
                System.out.println("Opción inválida. Intente nuevamente.");
                continue;
            }

            if (opcion == 6) {
                continuar = false;
                scanner.close();
                System.out.println("Saliendo del conversor...");
                continue;
            }

            String destino;
            switch (opcion) {
                case 1:
                    destino = "ARS";
                    break;
                case 2:
                    destino = "BOB";
                    break;
                case 3:
                    destino = "BRL";
                    break;
                case 4:
                    destino = "CLP";
                    break;
                case 5:
                    destino = "COP";
                    break;
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
                    continue;
            }

            Map<String, Double> tasas;
            try {
                tasas = consulta.obtenerTasasFiltradas(apiKey, "USD");
            } catch (RuntimeException e) {
                System.out.println("No se pudo consultar la API. Intente nuevamente.");
                continue;
            }

            Double tasa = tasas.get(destino);
            if (tasa == null) {
                System.out.println("No se encontró tasa para la moneda " + destino + ".");
                continue;
            }

            System.out.print("Ingrese el monto en USD: ");

            double monto;
            try {
                monto = scanner.nextDouble();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Ingrese un número válido");
                scanner.nextLine();
                continue;
            }

            if (monto <= 0) {
                System.out.println("El monto debe ser mayor que 0");
                continue;
            }

            double resultado = Conversor.convertir(monto, tasa);
            System.out.println(
                df.format(monto) + " USD equivalen a " + df.format(resultado) + " " + destino
            );
        }
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("1) USD → ARS");
        System.out.println("2) USD → BOB");
        System.out.println("3) USD → BRL");
        System.out.println("4) USD → CLP");
        System.out.println("5) USD → COP");
        System.out.println("6) Salir");
        System.out.print("Elija una opción: ");
    }
}
