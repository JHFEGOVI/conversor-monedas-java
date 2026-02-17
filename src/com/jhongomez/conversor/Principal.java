package com.jhongomez.conversor;

import java.text.DecimalFormat;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class Principal {
    private static final String MONEDA_BASE = "USD";

    public static void main(String[] args) {
        String claveApi = System.getenv("EXCHANGE_API_KEY");

        if (claveApi == null || claveApi.isBlank()) {
            System.out.println("No se encontró API key.");
            System.out.println("Configura la variable de entorno EXCHANGE_API_KEY.");
            return;
        }

        ConsultaMoneda consulta = new ConsultaMoneda();
        Scanner teclado = new Scanner(System.in);
        DecimalFormat formato = new DecimalFormat("#,##0.00");

        System.out.println("Sea bienvenido/a al Conversor de Moneda");

        boolean continuar = true;
        while (continuar) {
            mostrarMenu();

            String textoOpcion = teclado.nextLine();
            int opcion;

            try {
                opcion = Integer.parseInt(textoOpcion);
            } catch (NumberFormatException e) {
                System.out.println("Opción inválida. Intente nuevamente.");
                continue;
            }

            if (opcion == 6) {
                continuar = false;
                teclado.close();
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
                tasas = consulta.obtenerTasas(claveApi, MONEDA_BASE);
            } catch (RuntimeException e) {
                System.out.println("Error al consultar tasas. Verifique su conexión o la API key.");
                continue;
            }

            Double tasa = tasas.get(destino);
            if (tasa == null) {
                System.out.println("No se encontró tasa para la moneda " + destino + ".");
                continue;
            }

            System.out.print("Ingrese el monto en " + MONEDA_BASE + ": ");

            double monto;
            try {
                monto = teclado.nextDouble();
                teclado.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Ingrese un número válido");
                teclado.nextLine();
                continue;
            }

            if (monto <= 0) {
                System.out.println("El monto debe ser mayor que 0");
                continue;
            }

            double resultado = Conversor.convertir(monto, tasa);
            System.out.println(
                formato.format(monto) + " " + MONEDA_BASE + " equivalen a " + formato.format(resultado) + " " + destino
            );
        }
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("1) " + MONEDA_BASE + " → ARS");
        System.out.println("2) " + MONEDA_BASE + " → BOB");
        System.out.println("3) " + MONEDA_BASE + " → BRL");
        System.out.println("4) " + MONEDA_BASE + " → CLP");
        System.out.println("5) " + MONEDA_BASE + " → COP");
        System.out.println("6) Salir");
        System.out.print("Elija una opción: ");
    }
}
