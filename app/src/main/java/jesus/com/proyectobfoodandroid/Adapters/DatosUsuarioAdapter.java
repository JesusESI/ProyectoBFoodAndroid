package jesus.com.proyectobfoodandroid.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import jesus.com.proyectobfoodandroid.Objects.User;
import jesus.com.proyectobfoodandroid.R;

public class DatosUsuarioAdapter extends RecyclerView.Adapter<DatosUsuarioAdapter.DatosUsuarioWiewHolder> {

    private List<User> usuario;

    public DatosUsuarioAdapter(List<User> usuario) {
        this.usuario = usuario;
    }

    @NonNull
    @Override
    public DatosUsuarioWiewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DatosUsuarioWiewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_usuario, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DatosUsuarioWiewHolder holder, int position) {

        // TODO. cORRESPONDER CON LOS DATOS OBTENIDOS.

        holder.apodo.setText("Flurry");
        holder.posicion.setText("1");
        holder.puntos.setText("1000");
        holder.logros.setText("100");
        holder.notificaciones.setText("400");
    }

    @Override
    public int getItemCount() {
        return usuario.size();
    }

    class DatosUsuarioWiewHolder extends RecyclerView.ViewHolder {

        private ImageView imagenUsuario;
        private TextView apodo, posicion, puntos ,logros, notificaciones;


        public DatosUsuarioWiewHolder(View itemView) {
            super(itemView);

            this.apodo = (TextView) itemView.findViewById(R.id.textApodo);
            this.posicion = (TextView) itemView.findViewById(R.id.textPosicion);
            this.puntos = (TextView) itemView.findViewById(R.id.textPuntos);
            this.logros = (TextView) itemView.findViewById(R.id.textLogros);
            this.notificaciones = (TextView) itemView.findViewById(R.id.textNotificaciones);

        }
    }
}
