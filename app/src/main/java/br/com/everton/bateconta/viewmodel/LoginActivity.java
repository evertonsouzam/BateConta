package br.com.everton.bateconta.viewmodel;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.com.everton.bateconta.R;
import br.com.everton.bateconta.api.UsuarioAPI;
import br.com.everton.bateconta.br.com.everton.bateconta.util.Util;
import br.com.everton.bateconta.model.Usuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    private final String KEY_APP_PREFERENCES = "login";
    private final String KEY_LOGIN = "login";

    private TextInputLayout tilLogin;
    private TextInputLayout tilSenha;
    private CheckBox cbManterConectado;

    Util util = new Util();
    UsuarioAPI api = util.getRetrofit().create(UsuarioAPI.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tilLogin = (TextInputLayout) findViewById(R.id.tilLogin);
        tilSenha = (TextInputLayout) findViewById(R.id.tilSenha);
        cbManterConectado = (CheckBox) findViewById(R.id.cbManterConectado);

        //Verifica se quer tirar o mater conectado
        if (getIntent() != null && getIntent().getStringExtra("Deslogar") != null){
            deslogar();
            finish();
        }

        String username = isConectado();
        if (!isConectado().equals(""))
            iniciarApp(true, username);
    }

    private String isConectado(){
        SharedPreferences shared = getSharedPreferences(KEY_APP_PREFERENCES,MODE_PRIVATE);
        return shared.getString(KEY_LOGIN,"");
    }
    private void manterConectado(String username){
        SharedPreferences pref = getSharedPreferences(KEY_APP_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_LOGIN, username);
        editor.commit();
    }
    public void logar(View v){
        String nome = tilLogin.getEditText().getText().toString();
        String senha = tilSenha.getEditText().getText().toString();
        if (!nome.equals("") && !senha.equals("")) {
            if (isLoginValidado(nome, senha)) {
            }
        }else{
            Toast.makeText(LoginActivity.this, R.string.Texto_Erro_PreencherUsuarioSenha, Toast.LENGTH_SHORT).show();
        }


    }

    public void deslogar(){

        SharedPreferences pref = getSharedPreferences(KEY_APP_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_LOGIN, "");
        editor.commit();
    }

    private boolean isLoginValidado(String nome, String senha) {

        api.login(nome, senha).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                Usuario usuario = new Usuario();
                usuario = response.body();
                if(usuario != null && usuario.getId() != null && !usuario.getId().equals("")){
                    if(cbManterConectado.isChecked()){
                        manterConectado(usuario.getNome());
                        iniciarApp(true, usuario.getNome());
                    }else{
                        iniciarApp(false, usuario.getNome());
                    }
                }else{
                    Toast.makeText(LoginActivity.this, R.string.Texto_Erro_UsuarioSenhaInvalido, Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(LoginActivity.this, R.string.Texto_Erro_BuscarLogin, Toast.LENGTH_SHORT).show();
            }
        });

        return false;
    }

    private void iniciarApp(boolean manterConectado, String username){
        Intent proximaTela = new Intent(this,MainActivity.class);
        proximaTela.putExtra("ManterConectado",manterConectado);
        proximaTela.putExtra("Usuario",username);
        startActivity(proximaTela);
        finish();
    }

    public void criarConta(View view){
        dialogUsuario(new Usuario());
    }

    public void dialogUsuario(final Usuario usuario){
        final boolean isInsert = usuario.getId() == null ? true : false;
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.novo_usuario);

        dialog.setTitle("Novo Usuario");

        final EditText etNomeUsuario = (EditText) dialog.findViewById(R.id.etNomeUsuario);
        final EditText etSenha = (EditText) dialog.findViewById(R.id.etSenha);
        final EditText etconfirmarSenha = (EditText) dialog.findViewById(R.id.etConfirmaSenha);

        Button btConfirmar = (Button) dialog.findViewById(R.id.btConfirmarUsuario);

        btConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etSenha.getText().toString().equals(etconfirmarSenha.getText().toString())){
                    usuario.setNome(etNomeUsuario.getText().toString());
                    usuario.setSenha(etSenha.getText().toString());
                    if (isInsert){
                        api.buscar(usuario.getNome()).enqueue(new Callback<Usuario>() {
                            @Override
                            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                                Usuario usuarioRetorno = new Usuario();
                                usuarioRetorno = response.body();
                                if (usuarioRetorno.getId() == null){
                                    salvarUsuario(usuario);
                                }else{
                                    Toast.makeText(LoginActivity.this, R.string.Texto_Erro_UsuarioUtilizado, Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<Usuario> call, Throwable t) {
                                Toast.makeText(LoginActivity.this, R.string.Texto_Erro_BuscaUsuario, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                }else{
                    Toast.makeText(LoginActivity.this, R.string.Texto_Erro_Senhas, Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

        Button btCancelar = (Button) dialog.findViewById(R.id.btCancelarUsuario);

        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void salvarUsuario(Usuario usuario) {
        api.salvar(usuario).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(LoginActivity.this, R.string.Texto_Sucesso, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(LoginActivity.this, R.string.Texto_Erro_CadastrarUsuario, Toast.LENGTH_SHORT).show();
            }
        });

    }

}
