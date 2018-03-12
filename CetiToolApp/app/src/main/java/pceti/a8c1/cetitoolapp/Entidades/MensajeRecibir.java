package pceti.a8c1.cetitoolapp.Entidades;

/**
 * Created by CesarAM on 04/03/2018.
 */

public class MensajeRecibir extends Mensaje {
    private Long hora;




    public MensajeRecibir(String mensaje, String urlFoto, String nombre, String image, String type_mensaje, Long hora) {
        super(mensaje, urlFoto, nombre, image, type_mensaje);
        this.hora = hora;
    }

    public Long getHora() {
        return hora;
    }

    public void setHora(Long hora) {
        this.hora = hora;
    }
}
