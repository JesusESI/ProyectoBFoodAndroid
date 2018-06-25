package jesus.com.proyectobfoodandroid.Adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import jesus.com.proyectobfoodandroid.DatosUsuarioActivity;
import jesus.com.proyectobfoodandroid.Objects.Notificacion;
import jesus.com.proyectobfoodandroid.Objects.User;
import jesus.com.proyectobfoodandroid.R;

public class NotificacionesAdapter extends RecyclerView.Adapter<NotificacionesAdapter.NotificacionesWiewHolder> {

    private List<Notificacion> listadoNotificacionesUsuario;

    public NotificacionesAdapter(List<Notificacion> listadoNotificacionesUsuario) {
        this.listadoNotificacionesUsuario = listadoNotificacionesUsuario;
    }

    @NonNull
    @Override
    public NotificacionesWiewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificacionesAdapter.NotificacionesWiewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.notificacion_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificacionesWiewHolder holder, int position) {
        final Notificacion notificacion = listadoNotificacionesUsuario.get(position);

        holder.textTitulo.setText(notificacion.getTitulo());

        if (notificacion.isAceptado()) {
            holder.textAceptado.setText("Aceptado");
        } else {
            holder.textAceptado.setText("No aceptado");
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent datosEvento = new Intent(v.getContext(), DatosUsuarioActivity.class);
                datosUsuario.putExtra("email", usuario.getEmail());
                v.getContext().startActivity(datosUsuario);*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return listadoNotificacionesUsuario.size();
    }

    class NotificacionesWiewHolder extends RecyclerView.ViewHolder {

        private TextView textTitulo;
        private TextView textAceptado;

        public NotificacionesWiewHolder(View itemView) {
            super(itemView);

            textTitulo = itemView.findViewById(R.id.textTitulo);
            textAceptado = itemView.findViewById(R.id.textAceptado);

        }
    }
}


