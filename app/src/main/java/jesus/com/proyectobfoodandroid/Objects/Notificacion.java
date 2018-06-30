package jesus.com.proyectobfoodandroid.Objects;

public class Notificacion {

    private String usuarioNotificacion;
    private String titulo;
    private boolean aceptado;
    private String contenido;
    private String tipoEvento;

    public Notificacion(String usuario, String titulo, boolean aceptado, String contenido) {
        this.usuarioNotificacion = usuario;
        this.titulo = titulo;
        this.aceptado = aceptado;
        this.contenido = contenido;
    }

    public Notificacion(String titulo, String contenido,  String tipo) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.tipoEvento = tipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public boolean isAceptado() {
        return aceptado;
    }

    public void setAceptado(boolean aceptado) {
        this.aceptado = aceptado;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String conteniido) {
        this.contenido = conteniido;
    }

    public String getUsuarioNotificacion() {
        return usuarioNotificacion;
    }

    public void setUsuarioNotificacion(String usuarioNotificacion) {
        this.usuarioNotificacion = usuarioNotificacion;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }
}
