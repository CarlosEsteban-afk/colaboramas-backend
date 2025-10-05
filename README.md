# Colabora+ Backend

Este proyecto es una **API** construida con [Spring Boot](https://spring.io/projects/spring-boot), conectada a una base de datos **PostgreSQL**.  
Esta API servira para la comunicación con la aplicación móvil desarrollada en [React Native](https://reactnative.dev/), utilizando en framework [Expo](https://docs.expo.dev/).


---

## Requisitos previos

Antes de comenzar asegúrate de tener instalado:

- [Java 21](https://sdkman.io/)
- [Maven 3.9+](https://maven.apache.org/)
- [PostgreSQL 14+](https://www.postgresql.org/)

---

## Configuración de la base de datos

Se debe crear una base de datos y un usuario para esta base de datos.

## Variables de entorno

La aplicación usa variables de entorno para conectar a la base de datos.
Se debe crear un archivo .env en la raíz del proyecto:

```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/db
SPRING_DATASOURCE_USERNAME=dbusr
SPRING_DATASOURCE_PASSWORD=dbpass
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_SECURITY_USER_NAME=admin
SPRING_SECURITY_USER_PASSWORD=admin123
SECURITY.JWT.USER.GENERATOR=AUTH0JWT-BACKEND
SECURITY.JWT.KEY.PRIVATE=key
```

Nota: La key para firmar JWT (SECURITY.JWT.KEY.PRIVATE=key) utiliza Algoritmo HS256, base 64, 32 bytes. 
Es posible generar una key personalizada teniendo en cuenta estas características a través de herramientas online.

## Levantar la aplicación

```bash
mvn spring-boot:run
```
