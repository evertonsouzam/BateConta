package br.com.everton.bateconta.api;

import br.com.everton.bateconta.model.Usuario;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by r on 16/01/2018.
 */

public interface UsuarioAPI {

    @GET("/usuario/login/{nome}/{senha}")
    Call<Usuario> login(@Path("nome") String nome, @Path("senha") String senha);

    @POST(value = "/usuario/salvar")
    Call<Void> salvar(@Body Usuario usuario);

    @GET("/usuario/buscar/{nome}")
    Call<Usuario> buscar(@Path("nome") String nome);

}
