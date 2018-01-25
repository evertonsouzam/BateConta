package br.com.everton.bateconta.viewmodel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import br.com.everton.bateconta.R;
import br.com.everton.bateconta.viewmodel.br.com.everton.bateconta.SobreActivity;

public class MainActivity extends AppCompatActivity {

    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle param = getIntent().getExtras();
        boolean msg = param.getBoolean ("ManterConectado");
        username = param.getString("Usuario");
        setContentView(R.layout.activity_main);
        if (!msg){
            Button btDesconectar = (Button) findViewById(R.id.btDesconectar);
            btDesconectar.setVisibility(View.INVISIBLE);
        }

    }

    public void comecar(View view) {
        Bundle param = getIntent().getExtras();
        username = param.getString("Usuario");
        Intent proximaTela = new Intent(this, ContaActivity.class);
        proximaTela.putExtra("Usuario",username);
        startActivity(proximaTela);

    }

    public void sair(View view){

        finish();
    }

    public void sobre(View view){
        Intent proximaTela = new Intent(this, SobreActivity.class);
        startActivity(proximaTela);
    }


    public void desconectar(View view){
        Intent proximaTela = new Intent(this, LoginActivity.class);
        proximaTela.putExtra("Deslogar","1");
        startActivity(proximaTela);

        sair(view);
    }
}
