package jesus.com.proyectobfoodandroid.Adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import jesus.com.proyectobfoodandroid.DatosComidaActivity;
import jesus.com.proyectobfoodandroid.Objects.Comida;
import jesus.com.proyectobfoodandroid.R;

public class ComidasAdapter extends  RecyclerView.Adapter<ComidasAdapter.ComidaViewHolder>{

    private List<Comida> listaComidas;
    private String evento;

    public ComidasAdapter(List<Comida> listaComidas, String evento) {
        this.listaComidas = listaComidas;
        this.evento = evento;
    }

    @NonNull
    @Override
    public ComidaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ComidaViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.comida_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ComidaViewHolder holder, int position) {

        final Comida comida = listaComidas.get(position);

        holder.nombreComida.setText(comida.getNombre());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent datosEvento = new Intent(v.getContext(), DatosComidaActivity.class);
                datosEvento.putExtra("comida", comida.getNombre());
                datosEvento.putExtra("evento", evento);
                v.getContext().startActivity(datosEvento);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaComidas.size();
    }

    class ComidaViewHolder extends RecyclerView.ViewHolder {

        TextView nombreComida;

        public ComidaViewHolder(View itemView) {
            super(itemView);

            nombreComida = itemView.findViewById(R.id.textNombreComida);


        }
    }
}
