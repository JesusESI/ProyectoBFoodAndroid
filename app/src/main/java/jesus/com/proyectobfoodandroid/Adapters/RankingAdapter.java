package jesus.com.proyectobfoodandroid.Adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

import jesus.com.proyectobfoodandroid.DatosUsuarioActivity;
import jesus.com.proyectobfoodandroid.Objects.User;
import jesus.com.proyectobfoodandroid.R;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.RankingWiewHolder>{

    private List<User> listaUsuariosRanking;

    public RankingAdapter(List<User> listaUsuariosRanking) {
        this.listaUsuariosRanking = listaUsuariosRanking;
    }

    @NonNull
    @Override
    public RankingWiewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RankingWiewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_ranking, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RankingWiewHolder holder, int position) {
        final User usuario = listaUsuariosRanking.get(position);

        int pos = listaUsuariosRanking.size() - position;

        holder.apodo.setText(usuario.getApodo());
        holder.puntos.setText(usuario.getPuntos());
        holder.posicion.setText(String.valueOf(pos) + "º");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent datosUsuario = new Intent(v.getContext(), DatosUsuarioActivity.class);
                datosUsuario.putExtra("email", usuario.getEmail());
                v.getContext().startActivity(datosUsuario);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaUsuariosRanking.size();
    }

    class RankingWiewHolder extends  RecyclerView.ViewHolder {

        // Elementos del view.
        private ImageView userImage;
        private TextView posicion, apodo, puntos;

        public RankingWiewHolder(View itemView) {
            super(itemView);

            this.posicion = (TextView) itemView.findViewById(R.id.textPosicionRanking);
            this.apodo = (TextView) itemView.findViewById(R.id.textApodoRanking);
            this.puntos = (TextView) itemView.findViewById(R.id.textPuntosRanking);
        }
    }
}


