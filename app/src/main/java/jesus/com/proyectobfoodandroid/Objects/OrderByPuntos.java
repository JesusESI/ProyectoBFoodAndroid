package jesus.com.proyectobfoodandroid.Objects;

import android.support.annotation.NonNull;

import java.util.Comparator;

public class OrderByPuntos implements Comparator {

    @Override
    public int compare(@NonNull Object a, @NonNull Object b) {

        User userA = (User) a;
        User userB = (User) b;

        int puntosA = Integer.parseInt(userA.getPuntos());
        int puntosB = Integer.parseInt(userB.getPuntos());

        if (puntosB == puntosA) {          //si la edad de la primer persona es igual a la edad de la segunda retornamos 0
            return 0;
        } else if (puntosB > puntosA) {    //si la edad de la primer persona es mayor la edad de la segunda retornamos 1
            return 1;
        } else {                           //si la edad de la primer persona es menor la edad de la segunda retornamos -1
            return -1;
        }
    }
}
