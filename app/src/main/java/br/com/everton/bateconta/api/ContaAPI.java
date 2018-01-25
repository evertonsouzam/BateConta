package br.com.everton.bateconta.api;

import java.util.List;

import br.com.everton.bateconta.model.Conta;
import br.com.everton.bateconta.model.Usuario;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by r on 18/01/2018.
 */

public interface ContaAPI {

    @GET("/conta/buscarConta/{usuario}")
    Call <List<Conta>> buscarConta(@Path("usuario") String usuario);

    @GET("/conta/buscarProduto/{usuario}/{produto}")
    Call <Conta> buscarProduto(@Path("usuario") String usuario, @Path("produto") String produto);

    @GET("/conta/valorConta/{usuario}")
    Call<Double> valorConta(@Path("usuario") String usuario);

    @POST("/conta/salvar")
    Call<Void> salvar(@Body Conta conta);

    @DELETE("/conta/delete/{usuario}/{produto}")
    Call<Void> apagarProdutoConta(@Path("usuario") String usuario, @Path("produto") String produto);

    @DELETE("/conta/delete/{usuario}")
    Call<Void> apagarConta(@Path("usuario") String usuario);
}
