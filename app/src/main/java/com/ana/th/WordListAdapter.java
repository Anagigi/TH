package com.ana.th;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;


/* Se crea un adaptador para conectar datos con vieww elementos en una lista.
Se crea un adaptador que asocia su lista de palabras con view elementos de la lista de palabras.
Para conectar datos con view elementos, el adaptador necesita saber acerca de los
View elemntos. Utiliza un ViewHolder que describe un View elemento y su posicion dentro
del recyclerView.
Primero se crea un adaptador que cierra la brecha entre los datos en su lista de palabras y
el recyclerview que lo muestra.
WordListadapter extiende un adaptador generico para recyclerview use un view soporte especifico
para su aplicacion y definido en su interior.
Dentro de la clase, va a haber una clase interna llamada wordViewHolder creada para el adaptador.
Despues se necesita almacenar los datos en el adaptador, WordListAdapter necesita un constructor que
inicialice la lista de palabras a partir de los datos.
 */
public class WordListAdapter extends
        RecyclerView.Adapter<WordListAdapter.WordViewHolder> {

    private final LinkedList<String> mWordList;
    private LayoutInflater mInflater;

    public WordListAdapter(Context context, LinkedList<String> mWordList) {

        mInflater = LayoutInflater.from(context);
        this.mWordList = mWordList;
    }

    class WordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView wordItemView;
        final WordListAdapter mAdapter;

        public WordViewHolder(View itemView, WordListAdapter adapter) {
            super(itemView);
            wordItemView = itemView.findViewById(R.id.Nodo);
            this.mAdapter = adapter;
            //Se conecta onclicklistener con el View.
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // Get the position of the item that was clicked.
            int mPosition = getLayoutPosition();
            // Use that to access the affected item in mWordList.
            String element = mWordList.get(mPosition);
            // Change the word in the mWordList.
            mWordList.set(mPosition, "Leyendo nodo... " + element);
            // Notify the adapter, that the data has changed so it can
            // update the RecyclerView to display the data.
            mAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public WordListAdapter.WordViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {

        View mItemView = mInflater.inflate(R.layout.listasensores,
                parent, false);
        return new WordViewHolder(mItemView, this);

    }

    @Override
    public void onBindViewHolder(WordListAdapter.WordViewHolder holder, int position) {
        String mCurrent = mWordList.get(position);
        holder.wordItemView.setText(mCurrent);
    }

    @Override
    public int getItemCount() {
        return mWordList.size();
    }
}