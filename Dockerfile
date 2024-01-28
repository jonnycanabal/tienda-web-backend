#Indica que se usa una imagen base de java 8
FROM openjdk:8-jdk-alpine

#Copia el archivo JAR de la ruta al contenedor y le da el nombre
COPY target/tienda-web-0.0.1-SNAPSHOT.jar java-app.jar

#Establece el punto de entrada para ejecutar la aplicacion
#Aplicacion Java, contenido en un archivo JAR llamada segun el nombre que se le de "java-app.jar"
ENTRYPOINT [ "java", "-jar", "java-app.jar" ]


