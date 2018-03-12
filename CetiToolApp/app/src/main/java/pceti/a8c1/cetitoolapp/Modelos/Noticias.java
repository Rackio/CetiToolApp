package pceti.a8c1.cetitoolapp.Modelos;

/**
 * Created by ANDRES CISNEROS on 11/02/2018.
 */

public class Noticias {

    private String Titulo;
    private String Desc;
    private String Imagen;
    private String Filtro;
    private String userFacebook;

    public Noticias(){

    }

    public Noticias(String titulo, String desc, String imagen, String filtro) {
        Titulo = titulo;
        Desc = desc;
        Imagen = imagen;
        Filtro = filtro;
        userFacebook=userFacebook;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
    }

    public String getFiltro() {
        return Filtro;
    }

    public void setFiltro(String filtro) {
        Filtro = filtro;
    }

    public String getUserFacebook() {
        return userFacebook;
    }

    public void setUserFacebook(String userFacebook) {
        this.userFacebook = userFacebook;
    }

}
