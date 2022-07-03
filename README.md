# restaurant-booking

# URLs

- **API Gateway**: http://localhost:8765
- **Frontend**: http://localhost:8080

# Inicio de sesión

- Admin:
  - Usuario: micaela@gmail.com
  - Contraseña: soy micaela
- Restaurante:
  - Usuario: harry@gmail.com
  - Contraseña: soy harry
- Admin:
  - Usuario: diego@gmail.com
  - Contraseña: soy diego

# Postman

Se puede encontrar una colección de llamadas API y la configuración del entorno para utilizar en el programa Postman en la carpeta metadata 

# Frontend

Para ejecutar el frontend hay que ejecutar el comando npm run serve desde la carpeta front-end

# Docker

- **Creación imágenes:**
  - Front-end (en la carpeta): docker build -t rb/restaurant-booking-front-end:0.0.1-SNAPSHOT .
  - Resto, maven goal: spring-boot:build-image -DskipTests

- **Ejecución docker compose:**
  - docker-compose up
  - docker-compose -f dev-docker-compose.yaml up