package jesus.com.proyectobfoodandroid.Adapters;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import jesus.com.proyectobfoodandroid.Objects.Logro;
import jesus.com.proyectobfoodandroid.R;

import static jesus.com.proyectobfoodandroid.R.color.verde;

public class LogroAdapter extends RecyclerView.Adapter<LogroAdapter.LogroViewHolder>{

    private List<Logro> listaLogros;

    public LogroAdapter(List<Logro> listaLogros) {
        this.listaLogros = listaLogros;
    }

    @NonNull
    @Override
    public LogroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LogroViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.logro_item, parent, false));
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull LogroViewHolder holder, int position) {

        Logro logro = listaLogros.get(position);
        holder.nombreLogro.setText(logro.getNombre());
        holder.descripcionLogro.setText(logro.getDescripcion());
        holder.puntosLogro.setText(logro.getPuntos());
    }

    @Override
    public int getItemCount() {
        return listaLogros.size();
    }

    class LogroViewHolder extends RecyclerView.ViewHolder {

        TextView nombreLogro, descripcionLogro, puntosLogro;

        public LogroViewHolder(View itemView) {
            super(itemView);

            nombreLogro = (TextView) itemView.findViewById(R.id.nombreLogro);
            descripcionLogro = (TextView) itemView.findViewById(R.id.descripcionLogro);
            puntosLogro = (TextView) itemView.findViewById(R.id.puntosLogro);
        }
    }
}
