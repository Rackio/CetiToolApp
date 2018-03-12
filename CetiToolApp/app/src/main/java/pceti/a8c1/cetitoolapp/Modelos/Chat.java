package pceti.a8c1.cetitoolapp.Modelos;

import java.util.Map;

/**
 * Created by CesarAM on 11/03/2018.
 */

public class Chat  {
    public String sender;
    public String receiver;
    public String senderUid;
    public String receiverUid;
    public String message;
   public String mensaje;
    public String nombre;
    public String image;
   public String type_mensaje;
    public Map hora;

    public Chat(){

    }


    public Chat(String sender, String receiver, String senderUid, String receiverUid, String mensaje, String nombre, String image, String type_mensaje, Map hora) {
        this.sender = sender;
        this.receiver = receiver;
        this.senderUid = senderUid;
        this.receiverUid = receiverUid;
        this.mensaje = mensaje;
        this.nombre = nombre;
        this.image = image;
        this.type_mensaje = type_mensaje;
        this.hora = hora;
    }
}
