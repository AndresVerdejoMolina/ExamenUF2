package com.example.examenuf2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import com.bumptech.glide.Glide;

public class AdapterIncidencia extends ArrayAdapter<Incidencia> {

    public AdapterIncidencia(Context context, List<Incidencia> objects){
        super(context, 0, objects);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtener inflater.
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolder holder;

        // ¿Ya se infló este view?
        if (null == convertView) {
            //Si no existe, entonces inflarlo con itemlist.xml
            convertView = inflater.inflate(
                    R.layout.itemlist,
                    parent,
                    false);

            holder = new ViewHolder();
            holder.foto = (ImageView) convertView.findViewById(R.id.fotoTarea);
            holder.descripcion = (TextView) convertView.findViewById(R.id.descripcion);
            holder.aula = (TextView) convertView.findViewById(R.id.aula);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Cancion actual.
        Incidencia lead = getItem(position);

        // Setup.
        holder.descripcion.setText(lead.getDescripcion());
        holder.aula.setText(lead.getAula());
        Glide.with(getContext()).load(lead.getUrlFoto()).into(holder.foto);

        return convertView;
    }

    static class ViewHolder {
        ImageView foto;
        TextView descripcion;
        TextView aula;
    }

}
