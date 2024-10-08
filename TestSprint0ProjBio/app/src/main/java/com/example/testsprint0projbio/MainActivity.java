package com.example.testsprint0projbio;

import static android.Manifest.permission.BLUETOOTH_CONNECT;

import static androidx.activity.result.contract.ActivityResultContracts.*;

import android.Manifest.permission;
import android.os.Build;
import android.os.Bundle;

// ------------------------------------------------------------------
// ------------------------------------------------------------------

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;


// ------------------------------------------------------------------
// MainActivity class: Controla la actividad principal de la aplicación que escanea dispositivos Bluetooth LE,
// muestra su información y permite enviar datos POST a un servidor.
// ------------------------------------------------------------------

public class MainActivity extends AppCompatActivity {

    // --------------------------------------------------------------
    // Etiqueta de log para la depuración
    // --------------------------------------------------------------
    private static final String ETIQUETA_LOG = ">>>>";

    // --------------------------------------------------------------
    // Escáner Bluetooth LE
    // --------------------------------------------------------------
    private BluetoothLeScanner elEscanner;

    // --------------------------------------------------------------
    // Callback para los resultados de escaneo
    // --------------------------------------------------------------
    private ScanCallback callbackDelEscaneo = null;

    // --------------------------------------------------------------
    // Cadena UUID específica para filtrar dispositivos
    // --------------------------------------------------------------
    private final String uuidString = "holaMundoManuela";

    //---------------------------------------------------------------
    // Objeto para manejar la trama iBeacon
    //---------------------------------------------------------------
    private TramaIBeacon tib;

    // Indica si el escaneo está en curso
    private boolean isScanning = false;

    // TextView para mostrar el valor "Major" del iBeacon
    public TextView showMajor;

    // Botón para enviar una petición POST de prueba
    public Button enviarPostPrueba;

    // Botón para activar el envío de POST
    public Button EncenderEnvioPost;



    // --------------------------------------------------------------
    // Método para iniciar la búsqueda de todos los dispositivos Bluetooth LE.
    //      @throws SecurityException si no se tiene el permiso BLUETOOTH_SCAN.
    // --------------------------------------------------------------
    private void buscarTodosLosDispositivosBTLE() {
        Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): empieza ");

        Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): instalamos scan callback ");

        this.callbackDelEscaneo = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult resultado) {
                super.onScanResult(callbackType, resultado);
                Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): onScanResult() ");

                mostrarInformacionDispositivoBTLE(resultado);
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
                Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): onBatchScanResults() ");

            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
                Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): onScanFailed() ");

            }
        };

        Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): empezamos a escanear ");

        if (ActivityCompat.checkSelfPermission(this, permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                requestPermissionLuancher.launch(permission.BLUETOOTH_SCAN);
            }
        }
        this.elEscanner.startScan(this.callbackDelEscaneo);

    } // ()

    // --------------------------------------------------------------
    // Muestra información detallada sobre un dispositivo Bluetooth LE detectado.
    //     * @param resultado ScanResult: Resultado del escaneo de un dispositivo.
    //     * @throws SecurityException si no se tiene el permiso BLUETOOTH_CONNECT.
    // --------------------------------------------------------------
    private void mostrarInformacionDispositivoBTLE(ScanResult resultado) {


        BluetoothDevice bluetoothDevice = resultado.getDevice();
        byte[] bytes = Objects.requireNonNull(resultado.getScanRecord()).getBytes();
        int rssi = resultado.getRssi();

        Log.d(ETIQUETA_LOG, " ******************");
        Log.d(ETIQUETA_LOG, " ** DISPOSITIVO DETECTADO BTLE ****** ");
        Log.d(ETIQUETA_LOG, " ******************");
        if (ActivityCompat.checkSelfPermission(this, BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                requestPermissionLuancher.launch(BLUETOOTH_CONNECT);
            }
        }
        Log.d(ETIQUETA_LOG, " nombre = " + bluetoothDevice.getName());
        Log.d(ETIQUETA_LOG, " toString = " + bluetoothDevice);

        Log.d(ETIQUETA_LOG, " dirección = " + bluetoothDevice.getAddress());
        Log.d(ETIQUETA_LOG, " rssi = " + rssi);

        Log.d(ETIQUETA_LOG, " bytes = " + new String(bytes));
        Log.d(ETIQUETA_LOG, " bytes (" + bytes.length + ") = " + Utilidades.bytesToHexString(bytes));

        TramaIBeacon tib = new TramaIBeacon(bytes);

        Log.d(ETIQUETA_LOG, " ----------------------------------------------------");
        Log.d(ETIQUETA_LOG, " prefijo  = " + Utilidades.bytesToHexString(tib.getPrefijo()));
        Log.d(ETIQUETA_LOG, "          advFlags = " + Utilidades.bytesToHexString(tib.getAdvFlags()));
        Log.d(ETIQUETA_LOG, "          advHeader = " + Utilidades.bytesToHexString(tib.getAdvHeader()));
        Log.d(ETIQUETA_LOG, "          companyID = " + Utilidades.bytesToHexString(tib.getCompanyID()));
        Log.d(ETIQUETA_LOG, "          iBeacon type = " + Integer.toHexString(tib.getiBeaconType()));
        Log.d(ETIQUETA_LOG, "          iBeacon length 0x = " + Integer.toHexString(tib.getiBeaconLength()) + " ( "
                + tib.getiBeaconLength() + " ) ");
        Log.d(ETIQUETA_LOG, " uuid  = " + Utilidades.bytesToHexString(tib.getUUID()));
        Log.d(ETIQUETA_LOG, " uuid  = " + Utilidades.bytesToString(tib.getUUID()));
        Log.d(ETIQUETA_LOG, " major  = " + Utilidades.bytesToHexString(tib.getMajor()) + "( "
                + Utilidades.bytesToInt(tib.getMajor()) + " ) ");
        Log.d(ETIQUETA_LOG, " minor  = " + Utilidades.bytesToHexString(tib.getMinor()) + "( "
                + Utilidades.bytesToInt(tib.getMinor()) + " ) ");
        Log.d(ETIQUETA_LOG, " txPower  = " + Integer.toHexString(tib.getTxPower()) + " ( " + tib.getTxPower() + " )");
        Log.d(ETIQUETA_LOG, " ******************");

    } // ()
    // --------------------------------------------------------------
    // /**
    //     * Inicia la búsqueda de un dispositivo Bluetooth LE específico.
    //     * Filtra por UUID.
    //     * @throws SecurityException si no se tiene el permiso BLUETOOTH_SCAN.
    //     */
    // --------------------------------------------------------------
    private void buscarEsteDispositivoBTLE() {
        Log.d(ETIQUETA_LOG, " buscarEsteDispositivoBTLE(): empieza ");

        Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): instalamos scan callback ");






        this.callbackDelEscaneo = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult resultado) {
                super.onScanResult(callbackType, resultado);
                byte[] bytes = Objects.requireNonNull(resultado.getScanRecord()).getBytes();
                tib = new TramaIBeacon(bytes);
                if ( uuidString.equals(Utilidades.bytesToString(tib.getUUID())) ) {
                    mostrarInformacionDispositivoBTLE(resultado);
                    showMajor();
                }
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
                Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): onBatchScanResults() ");
            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
                Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): onScanFailed() ");
            }
        };

        Log.d(ETIQUETA_LOG, "  buscarEsteDispositivoBTLE(): empezamos");

        if (ActivityCompat.checkSelfPermission(this, permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                requestPermissionLuancher.launch(permission.BLUETOOTH_SCAN);
            }
        }

        // Iniciar escaneig amb filtre
        this.elEscanner.startScan(this.callbackDelEscaneo);

        // Know if the sensor scan is running
        isScanning = true;
    } // ()

    // --------------------------------------------------------------
    // Detiene la búsqueda de dispositivos Bluetooth LE.
    // --------------------------------------------------------------
    private void detenerBusquedaDispositivosBTLE() {

        if (this.callbackDelEscaneo == null) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.checkSelfPermission(this, permission.BLUETOOTH_SCAN);
        }
        this.elEscanner.stopScan(this.callbackDelEscaneo);
        this.callbackDelEscaneo = null;

    } // ()

    // --------------------------------------------------------------
    // Muestra el valor "Major" del dispositivo iBeacon detectado
    // --------------------------------------------------------------
    private void showMajor() {
        Log.d(ETIQUETA_LOG, " showMajor()");

        // Comprovar si s'està escanejant
        if (isScanning) {

            String display = getString(R.string.ppm) + (Arrays.toString(tib.getMajor()));
            Log.d(ETIQUETA_LOG, " Major: " + display);
            showMajor.setText(display);

        } else {
            Log.d(ETIQUETA_LOG, " No s'està escanejant.");
        }
    } // ()

    // --------------------------------------------------------------
    // Método que se ejecuta cuando se pulsa el botón para buscar dispositivos BTLE.
    //     * @param v View: la vista que fue pulsada.
    // --------------------------------------------------------------
    public void botonBuscarDispositivosBTLEPulsado(View v) {
        Log.d(ETIQUETA_LOG, " boton buscar dispositivos BTLE Pulsado");
        this.buscarTodosLosDispositivosBTLE();
    } // ()

    // --------------------------------------------------------------
    // Método que se ejecuta cuando se pulsa el botón para buscar un dispositivo BTLE específico.
    //     * @param v View: la vista que fue pulsada.
    // --------------------------------------------------------------
    public void botonBuscarNuestroDispositivoBTLEPulsado(View v) {
        Log.d(ETIQUETA_LOG, " boton nuestro dispositivo BTLE Pulsado");
        this.buscarEsteDispositivoBTLE();
    } // ()

    // --------------------------------------------------------------
    // Método que se ejecuta cuando se pulsa el botón para detener la búsqueda de dispositivos BTLE.
    //     * @param v View: la vista que fue pulsada.
    // --------------------------------------------------------------
    public void botonDetenerBusquedaDispositivosBTLEPulsado(View v) {
        Log.d(ETIQUETA_LOG, " boton detener busqueda dispositivos BTLE Pulsado");
        this.detenerBusquedaDispositivosBTLE();
    } // ()

    // --------------------------------------------------------------
    // Método que se ejecuta cuando se pulsa el botón para enviar un POST de prueba.
    //     * @param v View: la vista que fue pulsada.
    // --------------------------------------------------------------

    public void botonEnviarPostPrueba(View v) {
        Log.d(ETIQUETA_LOG, " boton Enviar Post Pulsado");
        this.enviarPostPrueba();
    }

    //----------------------------------------------------------------
    // Método para enviar una petición POST de prueba.
    //----------------------------------------------------------------

    private void enviarPostPrueba() {
        Data inputData = new Data.Builder()
                .putString(PeticionarioRESTWorker.KEY_METHOD, "POST")
                .putString(PeticionarioRESTWorker.KEY_URL, "http://192.168.1.2:80/mediciones")
                .putString(PeticionarioRESTWorker.KEY_BODY, "{ \"medida\": 50.5, \"lugar\": \"Zona Industrial\", \"tipo_gas\": \"CO2\", \"hora\": \"2024-09-26 14:30:00\" }")
                .build();
        // Start the Worker to make the request
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(PeticionarioRESTWorker.class)
                .setInputData(inputData)
                .build();

        WorkManager.getInstance(this).enqueue(workRequest);
    }
    //----------------------------------------------------------------
    // Método que se ejecuta al crear la actividad principal.
    //     * @param savedInstanceState Bundle: estado guardado de la instancia.
    //----------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SET XML VARIABLES
        showMajor = findViewById(R.id.showMajor);
        enviarPostPrueba = findViewById(R.id.enviarPostPrueba);
        EncenderEnvioPost = findViewById(R.id.ToggleEnviarPost);

        enviarPostPrueba.setOnClickListener(this::botonEnviarPostPrueba);

        //SET SCANNER
        BluetoothAdapter elAdaptadorBT = BluetoothAdapter.getDefaultAdapter();
        this.elEscanner = elAdaptadorBT.getBluetoothLeScanner();

    } // ()

    //----------------------------------------------------------------
    // Lanzador de actividad para solicitar permisos.
    //----------------------------------------------------------------

    private final ActivityResultLauncher<String> requestPermissionLuancher =
            registerForActivityResult(new RequestPermission(), isGranted ->
            {
                if (isGranted) {
                    Log.d(ETIQUETA_LOG, " onRequestPermissionResult(): permisos concedidos  !!!!");
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                } else {
                    Log.d(ETIQUETA_LOG, " onRequestPermissionResult(): Socorro: permisos NO concedidos  !!!!");

                }
            });

} // class
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
// --------------------------------------------------------------
