package pceti.a8c1.cetitoolapp.Entidades;

import com.google.firebase.database.ServerValue;

import java.util.Map;

/**
 * Created by CesarAM on 04/03/2018.
 */

public class MensajeEnviar extends Mensaje {
    private Map hora;



    public MensajeEnviar(String mensaje, String nombre, String image, Map hora, String type_mensaje) {
        super(mensaje, nombre, image, type_mensaje, hora);
        this.hora = hora;
    }

    public MensajeEnviar(String mensaje, String urlFoto, String nombre, String image, String type_mensaje, Map hora) {
        super(mensaje, urlFoto, nombre, image, type_mensaje);
        this.hora = hora;
    }

    public Map getHora() {
        return hora;
    }

    public void setHora(Map hora) {
        this.hora = hora;
    }
}
