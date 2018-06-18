package jesus.com.proyectobfoodandroid.DAO;

public interface UserDao {

    // Métodos Interfaz.
    String getApodo(String email);
    String getNumeroPuntos(String email);
    String getNumeroLogoros(String email);
    String getPosicion(String email);
    String getNotificaciones(String email);
}
