# Implementación del Protocolo rdt3.0 en un Medio No Fiable

## Descripción

Este proyecto implementa el protocolo rdt3.0 (Reliable Data Transfer 3.0) sobre un medio de comunicación no fiable, utilizando dos etapas principales:

- **Simulación de Entorno No Fiable:** Se desarrolla un simulador que emula las condiciones de un canal de comunicación no fiable, incluyendo probabilidades de pérdida y corrupción de paquetes.
- **Adaptación a UDP:** El protocolo es implementado sobre UDP (User Datagram Protocol), el cual es un protocolo sin conexión y no garantiza la entrega ni el orden de los paquetes.

El proyecto tiene como objetivo replicar las características de un medio no fiable y probar cómo rdt3.0 maneja estos escenarios.

## Funcionalidades

### Etapa 1: Desarrollo del Simulador

1. **Simulador de Entorno No Fiable:**
   Se crea un simulador que emula un canal de comunicación con probabilidades configurables de pérdida y corrupción de paquetes. Las siguientes funciones son implementadas para simular el comportamiento de **rdt3.0**:

- **A/B salida(mensaje):** Envía datos a través del canal no fiable.
- **A/B entrada(paquete):** Gestiona los ACK enviados.
- **A interrupTimer():** Controla el temporizador para los reenvíos en caso de fallo.
- **Package validateChecksum():** Verifica la integridad de los paquetes mediante un checksum.
- **A startimer(entidad, incremento) y stoptimer(entidad):** Manejan la activación y detención del temporizador.
- **B enviarACK(num):** Envia ACK de vuelta al emisor

### Esquema de comunicacion del protocolo rdt3.0

![esquema de comunicacion entre el emisor y el receptor](/images/DC-rdt3.png)

2.- **Simulación de Errores:**
El simulador permite la alteración y pérdida de paquetes con distintas probabilidades, permitiendo observar cómo el protocolo maneja estos fallos.

## Etapa 2: Adaptación a UDP

1. Configuración de UDP:
   El programa es adaptado para usar sockets UDP, un protocolo sin conexión que no garantiza la entrega ni el orden de los paquetes. Cada paquete y su respectivo ACK son enviados y recibidos independientemente.

2. Manejo de Retransmisiones:
   Implementación de un mecanismo de retransmisión basado en temporizadores para garantizar la entrega de los paquetes.

3. Control de Secuencia:
   Se agrega un número de secuencia a cada paquete para asegurar que los mensajes sean procesados en el orden correcto, dado que UDP no garantiza el orden de entrega.

## Tecnologías utilizadas

- **Lenguaje:** Java
- **Protocolos de Red:** Sockets UDP
- **Formato de mensajes:** JSON
- **Serializacion:** GSON

## Pruebas Realizadas

1. **Pruebas sin errores:**Se configuraron probabilidades de error en cero para simular un escenario sin pérdida ni corrupción de paquetes.
2. **Simulación de corrupción de paquetes:** Se probó cómo el protocolo rdt3.0 maneja los paquetes alterados.
3. **Simulación de pérdida de paquetes:** Se verificó cómo el protocolo gestiona los reenvíos y la recuperación en un entorno con pérdidas.

## Conclusiones

1. **Limitaciones de rdt3.0:**
   Aunque rdt3.0 es eficiente en un medio no fiable, tiene limitaciones en cuanto a la velocidad y la sobrecarga de reenvíos debido a la implementación de temporizadores y retransmisiones.

2. **Aplicabilidad en la vida real:**
   Este protocolo no sería adecuado para sistemas de alta velocidad debido a su ineficiencia en redes con alta latencia o congestión. Protocolos como TCP serían más apropiados para estas condiciones.

3. **Lecciones Aprendidas:**
   Este proyecto me permitió profundizar en los mecanismos de transmisión fiable de datos, el manejo de errores y la simulación de redes. También adquirí experiencia en la implementación de protocolos sobre UDP, un protocolo sin conexión.

## Instalación y Ejecución

### Requisitos previos

- Tener instalado Java (versión 8 o superior).
- Clonar el repositorio:

```bash
git clone https://github.com/tu-usuario/tic-tac-toe-sockets.git
cd tic-tac-toe-sockets
```

### Compilación

Antes de ejecutar el simulador, es necesario compilar los archivos **.java.** Para ello, desde la carpeta **src/**, ejecuta los siguientes comandos:

```bash
javac -d ./build Simulador/*.java
javac -d ./build Udp/*.java
```

Este comando:

- Compila todos los archivos .java dentro de Simulador/ y Udp/ respectivamente.

- Genera los archivos .class en la carpeta build/, manteniendo la estructura de los paquetes.

### Ejecución

Una vez compilado, para ejecutar el simulador, desde la carpeta **build/**, usa:

```bash
java Simulador.Main
```

De igual forma puedes ejecutar la implementacion del protocolo

```bash
java Udp.Main
```

## Diagrama de Flujo del Protocolo rdt3.0

![diagrama de flujo de arquitectura](/images/DF-rdt3.drawio.png)
