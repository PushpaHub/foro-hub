# Foro hub

 <img src="Portada.jpg" alt="Imagen de portada - cubitos con las letras portada" style="width: 100%;">

Esta aplicación API de back-end realiza las actividades en un foro. Te permite suscribirte con tu nombre, email y contraseña y después usar el foro en la forma comun.

## Descripción del proyecto
Esta aplicación se realizó como parte de la formación en Oracle Next Eucation (ONE) y Alura Latam, usando Spring Boot, MySQL y Security de Spring.

Ofrece las opciones de CRUD para los tópicos del foro:

- Registro de tópicos y guardarlos en la base de datos topicos
- Listar los tópicos que están en la base de datos
- Mostrar los datos de un tópico seleccionado
- Modificar el tópico seleccionado
- Eliminar el tópico seleccionado de la base de datos

y todo esto únicamente con el acceso autorizado
- usando la forma STATELESS de autenticación con JWT (JSON Web Token)


## Organización de código

### Organización de los paquetes y archivos de código

main

    -java
        - controller
            - AutenticacionController
            - TopicoController
        - domain
            - topico
                - Curso
                - DatosActualizarTopico
                - DatosListadoTopico
                - DatosRegistroTopico
                - DatosRespuestaTopico
                - Estatus
                - Topico
                - TopicoRepository
            - usuario
                - DatosAutenticacionUsuario
                - Usuario
                - UsuarioRepository
        - infra
            - errores
                - TratadorDeErrores
            - security
                - AutenticacionService
                - DatosJWTToken
                - SecurityConfigurations
                - SecurityFilter
                - TokenService
        - ForohubApplication

    - resources
        - db.migration
            - V1__create-table-topicos.sql
            - V2__create-table-usuarios.sql
        - application.properties
pom.xml        

### Dependencias usadas (el archivo pom.xml)
- pring-boot-starter-web:3.0.2
- spring-boot-devtools:3.0.2
- lombok:1.18.24
- spring-boot-starter-test:3.0.2
- spring-boot-starter-data-jpa:3.0.2
- flyway-core:9.5.1
- flyway-mysql:9.5.1
- mysql-connector-j:8.0.32
- spring-boot-starter-validation:3.0.2
- spring-boot-starter-security:3.0.2
- java-jwt:4.2.1

### Base de datos MySQL

Se usan 2 tablas:
- topicos
- usuarios

## Organización en la forma gráfica

![UML del proyecto](presentacion_grafica.png)



## Cómo usar la aplicación
Para probar o usar la aplicación se requeiere seguir los siguientes pasos:

1. Descarga todos los archivos y carpetas en una carpeta en tu computadora.
    - esto puedes hacer usando la consola o precionando boton verde "Code" en el repositorio de GitHub y escoges Download ZIP

2. Instala MySQL en tu computadora y crea la base de datos con el nombre forohub.

3. Abre el proyecto en IntelliJ 

4. Define las siguientes variables de entorno en tu sistema operativo (Windows o Mac):
    - SQL_USERNAME --> tu username para entrar en MySQL 
    - SQL_PASSWORD --> tu username para entrar en MySQL
    - JWT_SECRET --> tu password para la autorización con JWT

    Si el complilador no acepta las variables, defínelas también en IntelliJ (Run/Edit Configurations/Environment variables)

    El programa se ejecutará en localhost:8080.

5. Run la clase DesafioLiteraluraApplication. 

6. Con primer run se te construyeron las dos tablas en la base de datos. Ingresa en la tabla usuarios un nombre (login) y tu password que definiste en JWT_SECRET, encriptado. Lo encriptas con la encriptación Bcrypt en internet.

7. Descarga un cliente de API, te recomiendo Insomnia.

8. En Insomnia puedes realizar las actividades mencionadas:
    - POST http://localhost:8080/login (autenticación y autorización con el login y clave que incluiste en la base de datos en la tabla usuarios). Cópia el JWT que te lo regresa el sistema y pégalo a todas otras actividades en la parte Auth/Bearer token.
    - POST http://localhost:8080/topicos (Registro de un tópico)
    - GET http://localhost:8080/topicos Listar los tópicos que están en la base de datos.
    - GET http://localhost:8080/topicos/{id} Mostrar los datos de tópico con "id" (pones número)
    - PUT http://localhost:8080/topicos/{id} Modificar el tópico con "id" (pones número)
    - DEL http://localhost:8080/topicos/2 Eliminar el tópico con "id" (pones número) de la base de datos


## Tecnologías usadas
- Java SE17
- Spring Boot v.3.0.2
- IDE IntelliJ
- Cliente de API Insomnia
- MySQL de Oracle
- lucidchart.com para presentación gráfica
- Editor de Visual Studio Code para este README

## Estado del proyecto
El proyecto está terminado en el nivel básico.
Están abiertas varias opciones para aumentarlo y hacer el foro completo.

## Licencia
GNU General Public License v3.0