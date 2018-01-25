package br.com.everton.bateconta.br.com.everton.bateconta.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.everton.bateconta.R;
import br.com.everton.bateconta.model.Conta;

/**
 * Created by r on 15/01/2018.
 */

public class ContaAdapter extends RecyclerView.Adapter<ContaAdapter.ContaViewHolder> {

    private List<Conta> contas;
    private Activity activity;
    private int lastPositionSelected;

    public class ContaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener{
        public TextView tvProduto, tvValor, tvQuantidade;

        public ContaViewHolder(View view) {
            super(view);
            tvProduto = (TextView) view.findViewById(R.id.tvProduto);
            tvValor = (TextView) view.findViewById(R.id.tvValor);
            tvQuantidade = (TextView) view.findViewById(R.id.tvQuantidade);
            view.setOnClickListener(this);
            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view){
            activity.openContextMenu(view);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
            lastPositionSelected = getLayoutPosition();
            menu.setHeaderIcon(R.mipmap.ic_launcher);
            menu.setHeaderTitle(activity.getString(R.string.app_name));
            MenuInflater inflater = activity.getMenuInflater();
            inflater.inflate(R.menu.context_lista, menu);
        }
    }

    public int getLastPositionSelected(){
        return lastPositionSelected;
    }

    public ContaAdapter(Activity activity, List<Conta> contas){
        this.activity = activity;
        this.contas = contas;
    }

    @Override
    public ContaViewHolder onCreateViewHolder(ViewGroup pareten, int viewType){
        View itemView = LayoutInflater.from(pareten.getContext()).inflate(R.layout.conta_list_row,pareten,false);
        return new ContaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContaViewHolder holder, int position){
        Conta movie = contas.get(position);
        holder.tvProduto.setText(movie.getProduto());
        //TODO ver o valor
        holder.tvValor.setText(String.valueOf(movie.getValor()));
        holder.tvQuantidade.setText(String.valueOf(movie.getQuantidade()));

    }

    @Override
    public int getItemCount() {
        return contas.size();
    }

    public void add (Conta conta){
        contas.add(conta);
        notifyDataSetChanged();
    }

    public void addAll(List<Conta> contasList){
        for (Conta conta : contasList){
                contas.add(conta);
            }
        notifyDataSetChanged();
    }

    public Conta getContaSelected(){
        return contas.get(lastPositionSelected);
    }

    public void removeContaSelected(){
        //notifyItemRemoved(lastPositionSelected);
        contas.remove(contas.get(lastPositionSelected));
        notifyDataSetChanged();

    }


}
