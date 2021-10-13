package com.example.adapters;

import android.app.Activity;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easykit.R;
import com.example.models.Kit;

import java.util.ArrayList;
import java.util.List;

public class AdapterProducto extends BaseAdapter {

    protected Activity activity;
    protected List<Kit> items;


    public AdapterProducto (Activity activity, ArrayList<Kit> items) {
        this.activity = activity;
        this.items = items;
    }

    public void clear() {
        items.clear();
    }

    public void addAll(ArrayList<Kit> kits) {
        for (int i = 0; i < kits.size(); i++) {
            items.add(kits.get(i));
        }
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.item_producto, null);
        }

        Kit dir = items.get(position);

        ImageView foto = (ImageView) v.findViewById(R.id.foto);
        foto.setImageDrawable(dir.getImagen());

        TextView nombre = (TextView) v.findViewById(R.id.nombre);
        nombre.setText(dir.getNombre());

        TextView precio = (TextView) v.findViewById(R.id.precio);
        precio.setText("Precio: $ " + String.valueOf(dir.getPrecio()));

        TextView descripcion = (TextView) v.findViewById(R.id.descripcion);
        descripcion.setText("Tema: " + dir.getDescripcion());

        return v;
    }


}
