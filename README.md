# Asociación de juegos de mesa

Aplicación Java para gestionar socios, juegos y partidas, utilizando MariaDB/MySQL como base de datos.

## 1. Requisitos previos

Para ejecutar el proyecto se necesita:

- Java JDK 21.
- Maven.
- Un servidor MariaDB o MySQL accesible desde el equipo donde se ejecuta la aplicación.
- La base de datos `asociacion_juegos` creada y configurada.

El proyecto utiliza MariaDB Connector/J como controlador JDBC. La dependencia se encuentra declarada en `pom.xml`, por lo que Maven la descarga automáticamente.

## 2. Configuración de la conexión JDBC

La conexión se gestiona mediante la clase `org.example.database.Conexion`.

La aplicación utiliza las siguientes variables de configuración:

| Variable | Descripción | Ejemplo |
|---|---|---|
| `DB_URL` | URL JDBC del servidor y la base de datos | `jdbc:mariadb://192.168.1.100:3306/asociacion_juegos` |
| `DB_USER` | Usuario de la base de datos | `asociacion_app` |
| `DB_PASSWORD` | Contraseña del usuario | La contraseña configurada en el servidor |

El puerto predeterminado de MariaDB/MySQL es `3306`, aunque puede variar según la instalación.

### Archivo `.env`

En la raíz del proyecto, crea un archivo `.env` a partir de `.env.example` y configura los valores correspondientes:

```dotenv
DB_URL=jdbc:mariadb://192.168.1.100:3306/asociacion_juegos
DB_USER=asociacion_app
DB_PASSWORD=tu_contraseña
```

Sustituye `192.168.1.100` por la dirección IP o el nombre DNS del servidor de base de datos.

Si la aplicación y la base de datos están en el mismo equipo, puedes utilizar `localhost`.

El archivo `.env` contiene credenciales y no debe subirse al repositorio. El proyecto incluye `.env` en `.gitignore`. Debe compartirse únicamente `.env.example`, sin contraseñas reales.

También es posible configurar las variables de entorno en el sistema operativo o en el entorno de ejecución del IDE.

## 3. Preparar el servidor para conexiones remotas

Si MariaDB/MySQL se ejecuta en otro equipo, deben configurarse el servidor, los permisos y la red.

### 3.1. Permitir conexiones de red

Comprueba la configuración de MariaDB/MySQL para que el servicio escuche en la interfaz de red correspondiente.

En MariaDB, la opción suele llamarse `bind-address`. Su configuración depende del sistema operativo y de la instalación.

No es necesario exponer el servicio a todas las interfaces si basta con escuchar en una interfaz privada concreta. Evita publicar el puerto de la base de datos directamente en Internet.

Después de cambiar la configuración, reinicia el servicio si es necesario.

### 3.2. Crear un usuario con permisos limitados

Crea un usuario específico para la aplicación y concédele permisos únicamente sobre la base de datos del proyecto.

Ejemplo de SQL que debe ejecutar un administrador, sustituyendo la IP por la dirección del equipo cliente autorizado:

```sql
CREATE USER 'asociacion_app'@'192.168.1.50'
IDENTIFIED BY 'una_contraseña_segura';

GRANT ALL PRIVILEGES ON asociacion_juegos.*
TO 'asociacion_app'@'192.168.1.50';
```

La dirección `192.168.1.50` representa el equipo donde se ejecuta la aplicación Java, no el servidor de base de datos.

Si el usuario ya existe, ajusta sus permisos en lugar de intentar crearlo de nuevo. La sintaxis y las opciones disponibles pueden variar entre versiones de MariaDB y MySQL.

Evita utilizar `root` para la conexión habitual de la aplicación.

### 3.3. Configurar el cortafuegos

Permite conexiones TCP al puerto de MariaDB/MySQL únicamente desde los equipos o redes autorizados.

Comprueba también que la red permite la comunicación entre el cliente y el servidor.

En entornos de producción, es recomendable utilizar una red privada o una VPN y cifrar la conexión cuando corresponda.

## 4. Preparar la base de datos

La estructura de la base de datos se encuentra en:

`src/main/java/org/example/docker/init.sql`

Utiliza este script para inicializar las tablas cuando corresponda. Ejecútalo con una cuenta autorizada y sobre la base de datos correcta.

Antes de ejecutar el script en un entorno con datos existentes, revisa sus instrucciones para evitar modificaciones no deseadas.

## 5. Ejecutar la aplicación

Desde la raíz del proyecto, ejecuta:

```bash
mvn clean compile
```

Si la compilación finaliza correctamente, ejecuta la clase principal desde el IDE o mediante el mecanismo de ejecución configurado para el proyecto.

Comprueba que la aplicación puede acceder a la base de datos y que las operaciones de consulta funcionan.

## 6. Resolución de problemas

### Access denied for user

El servidor rechaza la autenticación. Comprueba el usuario, la contraseña y los permisos asignados a la combinación de usuario y host desde la que se conecta la aplicación.

### Connection refused

El servidor puede estar detenido, no escuchar en la interfaz adecuada o utilizar otro puerto. Comprueba el servicio, la URL JDBC y el cortafuegos.

### Connection timed out

Comprueba la conectividad de red, la dirección IP, las reglas del cortafuegos y que el servidor permita conexiones desde el equipo cliente.

### Unknown database

Comprueba que la base de datos indicada en `DB_URL` existe y que el nombre está escrito correctamente.

### La aplicación utiliza valores de configuración inesperados

Comprueba el contenido de `.env`, las variables de entorno del proceso y el directorio de trabajo desde el que se inicia la aplicación.

## 7. Buenas prácticas de seguridad

- No guardes contraseñas reales en el repositorio.
- No concedas acceso remoto a `root`.
- Limita los permisos del usuario de aplicación a la base de datos necesaria.
- Restringe las conexiones por IP y cortafuegos.
- Evita exponer el puerto de la base de datos directamente a Internet.
- Utiliza contraseñas seguras y canales cifrados cuando corresponda.
