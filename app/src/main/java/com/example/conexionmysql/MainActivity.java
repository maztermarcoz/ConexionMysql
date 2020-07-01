package com.example.conexionmysql;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    //Objeto para relacionar la vista
    ListView listView;

    private void relacionarVista(){
        //Conexion con la vista
        listView = (ListView)findViewById(R.id.ListView);
    }

    //Metodo para consultar el JSON de la aplicacion web
    private void downloadJson(final String urlWebService){
        //El AsyncTask es considerado un hilo
        class DownloadJSON extends AsyncTask<Void, Void, String>{

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    //Objeto para abrir la conexion URL con el servidor de aplicaciones
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();

                    //Construccion de objetos
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(
                            new InputStreamReader(

                                    con.getInputStream()
                            )

                    );
                    String json;

                    while ((json = bufferedReader.readLine()) != null){
                        sb.append(json + "\n");

                    }

return sb.toString();



                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                return null;
            }

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s){
                super.onPostExecute(s);

                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();

                try {
                    loadIntolListView(s);
                } catch (JSONException e){
                    e.printStackTrace();
                }

            }

        }
        DownloadJSON getJSON = new DownloadJSON();
        getJSON.execute();

    }
    private void loadIntolListView(String json) throws JSONException{
        JSONArray jsonArray = new JSONArray(json);

        String[] stocks = new String[jsonArray.length()];
        // Recorre el arreglo JSONArray para extraer cada objeto JSON
        for (int i= 0; i < jsonArray.length(); i++){
            JSONObject obj = jsonArray.getJSONObject(i);
            stocks[i] = obj.getString("nombre");

        }
        //Manda los datos de stocks a la lista
        ArrayAdapter <String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, stocks);

        listView.setAdapter(arrayAdapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //invocacion de los metodos
        relacionarVista();

        //Establecemos conexion con el servicio
        downloadJson("http://192.168.100.117:8081/contacto/list");

    }
}