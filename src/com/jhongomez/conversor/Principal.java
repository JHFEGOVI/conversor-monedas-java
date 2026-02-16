package com.jhongomez.conversor;

import java.util.Map;
import java.util.Scanner;

public class Principal {

    public static void main(String[] args) {
        String apiKey = args.length > 0 ? args[0] : System.getenv("EXCHANGE_API_KEY");

        if (apiKey == null || apiKey.isBlank()) {
            System.out.println("No se encontró API key.");
            System.out.println("Pásala por args o usa la variable de entorno EXCHANGE_API_KEY.");
            return;
        }

        ConsultaMoneda consulta = new ConsultaMoneda();
        Map<String, Double> tasas = consulta.obtenerTasasFiltradas(apiKey, "USD");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Sea bienvenido/a al Conversor de Moneda");

        boolean continuar = true;
        while (continuar) {
            System.out.println();
            System.out.println("1) USD → ARS");
            System.out.println("2) USD → BOB");
            System.out.println("3) USD → BRL");
            System.out.println("4) USD → CLP");
            System.out.println("5) USD → COP");
            System.out.println("6) Salir");
            System.out.print("Elija una opción: ");

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

            Double tasa = tasas.get(destino);
            if (tasa == null) {
                System.out.println("No se encontró tasa para la moneda " + destino + ".");
                continue;
            }

            System.out.print("Ingrese el monto en USD: ");
            String montoTexto = scanner.nextLine();

            double monto;
            try {
                monto = Double.parseDouble(montoTexto);
            } catch (NumberFormatException e) {
                System.out.println("Monto inválido. Intente nuevamente.");
                continue;
            }

            double resultado = Conversor.convertir(monto, tasa);
            System.out.printf("%.2f USD equivalen a %.2f %s%n", monto, resultado, destino);
        }
    }
}
