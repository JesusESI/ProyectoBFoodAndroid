package jesus.com.proyectobfoodandroid;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class NotificacionesActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private BluetoothManager btManager;
    private BluetoothAdapter btAdapter;
    private BluetoothLeScanner btScanner;

    // Varaible de control.
    private boolean controlEnabled;

    // Constantes.
    private final static int REQUEST_ENABLE_BT = 1;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private String TAG = "DISCOVERED: ";

    // CONTEXTO control.
    // Context instantanea;
    // SharedPreferences sharedPref;
    // SharedPreferences.Editor editor;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);

        // Inicializamos las toolbar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Por defecto la detección está desactivada.
        // this.controlEnabled = false;

        // Prueba(Guardar variable de control de escaneo.).
        // instantanea = getApplication();
        // sharedPref = instantanea.getSharedPreferences("controlEnabled", Context.MODE_PRIVATE);
        // editor = sharedPref.edit();

        // Inicializamos el bluetooth.
        btManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        btAdapter = btManager.getAdapter();
        btScanner = btAdapter.getBluetoothLeScanner();

        if (btAdapter != null && !btAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        // Es necesario comprobar el permiso de localización para que la detección funcione.
        if (this.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Esta aplicación necesita permisos de localización.");
            builder.setMessage("Por favor los permisos de localización son necesarios para que la app funcione.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                }
            });
            builder.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Escaneo desactivado.", Toast.LENGTH_LONG).show();
        controlEnabled = false;
        stopScanning();
    }

    // Device scan callback.
    private ScanCallback leScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            //peripheralTextView.append("Device Name: " + result.getDevice().getName() + " rssi: " + result.getRssi() + " MAC: "+ result.getDevice().getAddress()+ "\n");
            Log.d(TAG, "Beacon detectado: "+ result.getDevice().getName() + result.getDevice().getAddress());
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notificaciones, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.detection:
                // Prueba(Coger variable de control enabled guardada)
                //boolean controlBool = sharedPref.getBoolean("controlEnabled", controlEnabled);
               // controlEnabled = controlBool;

                // Acción.
                if (controlEnabled == false) {
                    Toast.makeText(this, "Escaneando eventos cercanos...", Toast.LENGTH_LONG).show();
                    controlEnabled = true;
                    // editor.putBoolean("controlEnabled", controlEnabled);
                    startScanning();
                } else {
                    // Mostrar Toast.
                    Toast.makeText(this, "Escaneo desactivado.", Toast.LENGTH_LONG).show();
                    controlEnabled = false;
                    // editor.putBoolean("controlEnabled", controlEnabled);
                    stopScanning();
                }
                // Actualizamos el estado del escaneo.
                // editor.commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void startScanning() {
        System.out.println("start scanning");
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                btScanner.startScan(leScanCallback);
            }
        });
    }

    public void stopScanning() {
        System.out.println("stopping scanning");
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                btScanner.stopScan(leScanCallback);
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("Permisos de localización otorgados.");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Funcionalidad limitada");
                    builder.setMessage("Esta app no puede detectar beacons en segundo plano.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }
                    });
                    builder.show();
                }
                return;
            }
        }
    }
}



