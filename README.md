## Título del proyecto
Conversor de Moneda en Java

## Descripción
Aplicación de consola en Java que consume la ExchangeRate API para convertir USD a ARS, BOB, BRL, CLP y COP.

## Características
- Consumo de API REST
- Uso de Gson para procesar JSON
- Conversión entre monedas
- Menú interactivo en consola
- Manejo de errores
- Uso de variable de entorno para la API key
- Código organizado en clases

## Tecnologías usadas
- Java 17
- Gson
- ExchangeRate API
- IntelliJ IDEA

## Estructura del proyecto
- `Principal.java` → menú e interacción con el usuario
- `ConsultaMoneda.java` → consulta la API
- `Conversor.java` → realiza la conversión

## Cómo ejecutar el proyecto en Linux/Mac
1. Exporta tu API key:

```bash
export EXCHANGE_API_KEY=tu_api_key
```

2. Compila y ejecuta el proyecto desde IntelliJ IDEA o con `javac`/`java`.

## Ejemplo de uso
```text
USD → COP
Ingrese el monto en USD: 100
100.00 USD equivalen a XXXXX COP
```

## Autor
Jhon Fernando Gómez  
Estudiante de Backend Java
