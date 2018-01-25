package br.com.everton.bateconta.viewmodel;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

import br.com.everton.bateconta.R;
import br.com.everton.bateconta.api.ContaAPI;
import br.com.everton.bateconta.br.com.everton.bateconta.adapter.ContaAdapter;
import br.com.everton.bateconta.br.com.everton.bateconta.util.Util;
import br.com.everton.bateconta.model.Conta;
import br.com.everton.bateconta.viewmodel.br.com.everton.bateconta.BateContaActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by r on 09/01/2018.
 */



public class ContaActivity extends AppCompatActivity {

    private RecyclerView rvProdutos;
    private ContaAdapter mAdapter;
    private FloatingActionMenu fMenu;
    private Conta produtoSelecionado;
    private String username;
    Util util = new Util();
    ContaAPI api = util.getRetrofit().create(ContaAPI.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conta);

        Bundle param = getIntent().getExtras();
        username = param.getString("Usuario");
        fMenu = (FloatingActionMenu) findViewById(R.id.fMenu);
        inicializaLista();
        carregaProdutos(username);
    }

    private void inicializaLista() {
        rvProdutos = (RecyclerView) findViewById(R.id.rvProdutos);
        mAdapter = new ContaAdapter(this, new ArrayList<Conta>());
        rvProdutos.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvProdutos.setItemAnimator(new DefaultItemAnimator());
        rvProdutos.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        rvProdutos.setAdapter(mAdapter);
    }

    private void carregaProdutos(String username) {

        api.buscarConta(username).enqueue(new Callback<List<Conta>>() {
            @Override
            public void onResponse(Call<List<Conta>> call, Response<List<Conta>> response) {
                mAdapter.addAll(response.body());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Conta>> call, Throwable t) {

            }
        });

    }

    public boolean onContextItemSelected(MenuItem item) {
        Bundle param = getIntent().getExtras();
        username = param.getString("Usuario");
        switch (item.getItemId()) {
            case R.id.menu_apagar:
                apagar(mAdapter.getContaSelected());
                break;

            case R.id.menu_editar:
                dialogConta(mAdapter.getContaSelected());
                break;
            case R.id.somar:
                somar(mAdapter.getContaSelected());
                break;
            case R.id.subtrair:
                subtrair(mAdapter.getContaSelected());
                break;
        }
        return super.onContextItemSelected(item);
    }

    public void apagar(final Conta conta){
        mAdapter.removeContaSelected();

        api.apagarProdutoConta(conta.getUsuario(), conta.getProduto()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(ContaActivity.this, R.string.Texto_Sucesso_Apagar, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ContaActivity.this, R.string.Texto_Erro_Apagar, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void somar(final Conta conta){
        conta.setQuantidade(conta.getQuantidade() + 1);
        api.salvar(conta).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ContaActivity.this, R.string.Texto_Erro_Somar, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void subtrair(final Conta conta){
        if (conta.getQuantidade() > 0 ){
            conta.setQuantidade(conta.getQuantidade() - 1);
            api.salvar(conta).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(ContaActivity.this, R.string.Texto_Erro_Subtrair, Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    public void novoProduto(View v) {
        fMenu.close(true);

        dialogConta(new Conta());
    }

    public void sair(View v){
        finish();
    }

    public void baterConta(View v){
        fMenu.close(true);
        Bundle param = getIntent().getExtras();
        username = param.getString("Usuario");

        api.valorConta(username).enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                Double valorTotal = response.body();
                MostrarBaterConta(valorTotal, username);

            }

            @Override
            public void onFailure(Call<Double> call, Throwable t) {

            }
        });
        finish();
    }

    public void MostrarBaterConta(Double valorTotal, String username){

        Intent proximaTela = new Intent(this, BateContaActivity.class);

        proximaTela.putExtra("Usuario",username);
        proximaTela.putExtra("ValorTotal",valorTotal);
        startActivity(proximaTela);


    }

    private void dialogConta(final Conta conta) {
        final boolean isInsert = conta.getId() == null && conta.getProduto() == null ? true : false;
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.novo_produto);
        dialog.setTitle(R.string.Texto_Novo_Produto);

        final EditText etProduto = (EditText) dialog.findViewById(R.id.etProduto);
        final EditText etValor = (EditText) dialog.findViewById(R.id.etValor);
        final EditText etQuantidade = (EditText) dialog.findViewById(R.id.etQuantidade);

        etProduto.setText(conta.getProduto());
        etValor.setText(String.valueOf(conta.getValor() == null ? 0 : conta.getValor()));
        etQuantidade.setText(String.valueOf(conta.getQuantidade()));

        Button btConfirmar = (Button) dialog.findViewById(R.id.btConfirmarProduto);

        btConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conta.setProduto(etProduto.getText().toString());
                conta.setValor(Double.parseDouble(etValor.getText().toString()));
                conta.setQuantidade(Integer.parseInt(etQuantidade.getText().toString()));
                //conta.setQuantidade(0);
                conta.setUsuario(username);
                api.salvar(conta).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (isInsert) mAdapter.add(conta);

                        mAdapter.notifyDataSetChanged();

                        dialog.dismiss();
                        Toast.makeText(ContaActivity.this, R.string.Texto_Sucesso, Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(ContaActivity.this, R.string.Texto_Erro_Adicionar, Toast.LENGTH_SHORT).show();
                    }
                });
                //conta.save();


            }
        });

        Button btCancelar = (Button) dialog.findViewById(R.id.btCancelarProduto);

        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void limparConta(String username){
        api.apagarConta(username).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

}