# Reportes de la API de Vericast

## Requerimientos

Este proyecto requiere:
* JDK 1.7
* Maven 3.0.5 o superior
* Tomcat 7.0
* Base de datos Mysql

## Configuración del proyecto

### Armado del ambiente de desarrollo

* Para hacer un build del proyecto se debe copiar el archivo ejemplo_settings.xml con el nombre settings.xml y definir alli las properties necesarias. Este archivo quedará fuera del control de versiones.
* Para hacer un build ejecutar:
```bash
mvn clean package -s settings.xml
```
especificando con -s la ruta al archivo settings.xml

* Para mockear la API de Vericast se puede:

  * Descomprimir la carpeta configuracion/vericast en el root de apache (Ej: /var/www/html)
  * Agregar properties para redefinir las URLs de conexión en el archivo settings.xml:
```bash
<vericast.api.channel.list>http://localhost/vericast/channel/list.php</vericast.api.channel.list>
<vericast.api.track.list.by.channel>http://localhost/vericast/channel/toptracks.php</vericast.api.track.list.by.channel>
```