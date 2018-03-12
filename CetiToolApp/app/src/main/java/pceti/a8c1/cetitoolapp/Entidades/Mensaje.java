package pceti.a8c1.cetitoolapp.Entidades;

import java.util.Map;

/**
 * Created by CesarAM on 04/03/2018.
 */

public class Mensaje {
    public String mensaje;
    public String urlFoto;
    public String nombre;
    public String image;
    public String type_mensaje;

    public Mensaje(String mensaje, String nombre, String image, String type_mensaje, Map hora) {
    }

    public Mensaje(String mensaje, String nombre, String image, String type_mensaje) {
        this.mensaje = mensaje;
        this.nombre = nombre;
        this.image = image;
        this.type_mensaje = type_mensaje;
    }

    public Mensaje(String mensaje, String urlFoto, String nombre, String image, String type_mensaje) {
        this.mensaje = mensaje;
        this.urlFoto = urlFoto;
        this.nombre = nombre;
        this.image = image;
        this.type_mensaje = type_mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType_mensaje() {
        return type_mensaje;
    }

    public void setType_mensaje(String type_mensaje) {
        this.type_mensaje = type_mensaje;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }
}


