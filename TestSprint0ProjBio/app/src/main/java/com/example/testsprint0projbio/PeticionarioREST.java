
package com.example.testsprint0projbio;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.os.AsyncTask;
import android.util.Log;

// ------------------------------------------------------------------------
// PeticionarioREST class: Maneja las peticiones HTTP REST de forma asíncrona.
// ------------------------------------------------------------------------
public class PeticionarioREST extends AsyncTask<Void, Void, Boolean> {

    // --------------------------------------------------------------------
    // Interface RespuestaREST: Proporciona un método de callback para manejar la respuesta de la petición REST.
    // --------------------------------------------------------------------
    public interface RespuestaREST {
        void callback (int codigo, String cuerpo);
    }

    // --------------------------------------------------------------------
    // --------------------------------------------------------------------
    // Método HTTP (GET, POST, etc.)
    private String elMetodo;

    // URL de destino de la petición
    private String urlDestino;

    // Cuerpo de la petición (para métodos POST, PUT, etc.)
    private String elCuerpo = null;

    // Objeto que maneja la respuesta de la petición
    private RespuestaREST laRespuesta;

    // Código de respuesta HTTP
    private int codigoRespuesta;

    // Cuerpo de la respuesta HTTP
    private String cuerpoRespuesta = "";

    // --------------------------------------------------------------------
    // Método para realizar una petición REST.
    //     * @param metodo String: Método HTTP.
    //     * @param urlDestino String: URL de destino.
    //     * @param cuerpo String: Cuerpo de la petición (puede ser null).
    //     * @param laRespuesta RespuestaREST: Objeto para manejar la respuesta de la petición.
    // --------------------------------------------------------------------

    public void hacerPeticionREST (String metodo, String urlDestino, String cuerpo, RespuestaREST  laRespuesta) {
        this.elMetodo = metodo;
        this.urlDestino = urlDestino;
        this.elCuerpo = cuerpo;
        this.laRespuesta = laRespuesta;

        this.execute(); // otro thread ejecutará doInBackground()
    }

    // --------------------------------------------------------------------
    // Constructor por defecto de la clase PeticionarioREST.
    // --------------------------------------------------------------------
    public PeticionarioREST() {
        Log.d("clienterestandroid", "constructor()");
    }

    // --------------------------------------------------------------------
    // Método que se ejecuta en un hilo separado para realizar la petición HTTP.
    //     * @param params Void: Parámetros de entrada (no utilizados).
    //     * @return Boolean: true si la petición se realizó correctamente, false en caso contrario
    // --------------------------------------------------------------------
    @Override
    protected Boolean doInBackground(Void... params) {
        Log.d("clienterestandroid", "doInBackground()");

        try {

            // envio la peticion

            // pagina web para hacer pruebas: URL url = new URL("https://httpbin.org/html");
            // ordinador del despatx 158.42.144.126 // OK URL url = new URL("http://158.42.144.126:8080");

            Log.d("clienterestandroid", "doInBackground() me conecto a >" + urlDestino + "<");

            URL url = new URL(urlDestino);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty( "Content-Type", "application/json; charset-utf-8" );
            connection.setRequestMethod(this.elMetodo);
            // connection.setRequestProperty("Accept", "*/*);

            // connection.setUseCaches(false);
            connection.setDoInput(true);

            if ( ! this.elMetodo.equals("GET") && this.elCuerpo != null ) {
                Log.d("clienterestandroid", "doInBackground(): no es get, pongo cuerpo");
                connection.setDoOutput(true);
                // si no es GET, pongo el cuerpo que me den en la peticin
                DataOutputStream dos = new DataOutputStream (connection.getOutputStream());
                dos.writeBytes(this.elCuerpo);
                dos.flush();
                dos.close();
            }

            // ya he enviado la peticin
            Log.d("clienterestandroid", "doInBackground(): peticin enviada ");

            // ahora obtengo la respuesta

            int rc = connection.getResponseCode();
            String rm = connection.getResponseMessage();
            String respuesta = rc + " : " + rm;
            Log.d("clienterestandroid", "doInBackground() recibo respuesta = " + respuesta);
            this.codigoRespuesta = rc;

            try {

                InputStream is = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                Log.d("clienterestandroid", "leyendo cuerpo");
                StringBuilder acumulador = new StringBuilder ();
                String linea;
                while ( (linea = br.readLine()) != null) {
                    Log.d("clienterestandroid", linea);
                    acumulador.append(linea);
                }
                Log.d("clienterestandroid", "FIN leyendo cuerpo");

                this.cuerpoRespuesta = acumulador.toString();
                Log.d("clienterestandroid", "cuerpo recibido=" + this.cuerpoRespuesta);

                connection.disconnect();

            } catch (IOException ex) {
                // dispara excepcin cuando la respuesta REST no tiene cuerpo y yo intento getInputStream()
                Log.d("clienterestandroid", "doInBackground() : parece que no hay cuerpo en la respuesta");
            }

            return true; // doInBackground() termina bien

        } catch (Exception ex) {
            Log.d("clienterestandroid", "doInBackground(): ocurrio alguna otra excepcion: " + ex.getMessage());
        }

        return false; // doInBackground() NO termina bien
    } // ()

    // --------------------------------------------------------------------
    // Método que se ejecuta después de doInBackground() para manejar los resultados en el hilo principal.
    //     * @param comoFue Boolean: Indica si la operación fue exitosa.
    // --------------------------------------------------------------------
    protected void onPostExecute(Boolean comoFue) {
        // llamado tras doInBackground()
        Log.d("clienterestandroid", "onPostExecute() comoFue = " + comoFue);
        this.laRespuesta.callback(this.codigoRespuesta, this.cuerpoRespuesta);
    }

} // class


