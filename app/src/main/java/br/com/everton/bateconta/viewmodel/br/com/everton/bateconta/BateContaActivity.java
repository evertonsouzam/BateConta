package br.com.everton.bateconta.viewmodel.br.com.everton.bateconta;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import br.com.everton.bateconta.R;
import br.com.everton.bateconta.model.Conta;
import br.com.everton.bateconta.viewmodel.ContaActivity;

public class BateContaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bate_conta);

        Bundle param = getIntent().getExtras();
        Double valorTotal  = param.getDouble("ValorTotal");

        final TextView tvValorTotal = (TextView) findViewById(R.id.tvValorTotalContaNew);

        tvValorTotal.setText(Double.toString(valorTotal));

    }

    public void confirmar(View v){
        finish();
    }

    public void confirmarNovo(View v){
        Bundle param = getIntent().getExtras();
        String username = param.getString("Usuario");

        ContaActivity conta = new ContaActivity();
        conta.limparConta(username);
        finish();
    }
}
