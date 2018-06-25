package jesus.com.proyectobfoodandroid;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import jesus.com.proyectobfoodandroid.Adapters.NotificacionesAdapter;
import jesus.com.proyectobfoodandroid.Firebase.FirebaseManager;
import jesus.com.proyectobfoodandroid.Objects.Notificacion;

public class NotificacionesActivity extends AppCompatActivity {

    private Toolbar toolbar;

    // Objetos bluetoth.
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
     SharedPreferences sharedPreferences;
     SharedPreferences.Editor editor;

    // Listas.
    private List eventos = new ArrayList();
    private List listaNotificacionesUsuario;
    private List<String> beaconsYaDetectados = new ArrayList();

    // DB reference
    private FirebaseDatabase firebase;
    private DatabaseReference dbReference;

    private RecyclerView recyclerView;
    private NotificacionesAdapter notificacionesAdapter;

    // Notificaciones
    private NotificationCompat.Builder mBuilder;
    private NotificationManagerCompat notificationManager;
    private int contadorNotificaciones = 1;
    private Notificacion notifyAux;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);

        // Inicializamos las toolbar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Inicializamos el notificationsManager
        notificationManager = NotificationManagerCompat.from(NotificacionesActivity.this);

        // Inicializacion de objetos para compartir variables entre activitys.
        sharedPreferences = this.getSharedPreferences("misPreferencias", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Inicializar el recycler view.
        firebase = FirebaseManager.getFirebaseSingleton().getmDatabase();
        dbReference = firebase.getReference("Usuarios");

        listaNotificacionesUsuario = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.notificacionesList);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearManager = new LinearLayoutManager(this);
        linearManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearManager);

        // Obtenemos las notificaciones del usuario.
        obtainNotificaciones(getIntent().getStringExtra("email"));

        notificacionesAdapter = new NotificacionesAdapter(listaNotificacionesUsuario);
        recyclerView.setAdapter(notificacionesAdapter);


        // Obtenemos los eventos disponibles.
        eventos = FirebaseManager.getFirebaseSingleton().readEventos();

        // Obtenemos las notificaciones del usuario logueado.
        // user = FirebaseManager.getFirebaseSingleton().getUser(getIntent().getStringExtra("email"));

        // Por defecto la detección está desactivada.
        // this.controlEnabled = false;

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
            Log.d(TAG, "Beacon detectado: " + result.getDevice().getName() + result.getDevice().getAddress());

            String nombreBeaconDetectado = result.getDevice().getName();

            // Por cada beacon detectado hacemos las siguientes comprobaciones antes de crear una nueva notificacion.
            if (comprobarPertenenciaEventos(nombreBeaconDetectado)) {
                if(!comprobarRedundanciaNotificacion(nombreBeaconDetectado)) {
                    // Si estas dos condiciones se cumplen se debe crear una notificacion, diciendo que hay un nuevo evento cercano.

                    // PRUEBA.
                    int icono = R.mipmap.icon_bfood;
                    Intent intent = new Intent(NotificacionesActivity.this, ConfirmarActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(NotificacionesActivity.this, 0,intent, 0);

                    mBuilder = new NotificationCompat.Builder(getApplicationContext())
                            .setContentIntent(pendingIntent)
                            .setSmallIcon(icono)
                            .setContentTitle(notifyAux.getTitulo())
                            .setContentText(notifyAux.getContenido())
                            .setVibrate(new long[] {100, 250, 100, 500})
                            .setAutoCancel(true);

                    // Modificamos las variables compartidas
                    editor.putString("titulo", notifyAux.getTitulo());
                    editor.putString("descripcion", notifyAux.getContenido());
                    editor.commit();

                    notificationManager.notify(contadorNotificaciones, mBuilder.build());

                    // Agregamos el beacon ya detectado para que no lo notifique mas.
                    beaconsYaDetectados.add(nombreBeaconDetectado);
                    contadorNotificaciones++;
                }
            }
        }
    };

    private boolean comprobarPertenenciaEventos(String beacon) {

        boolean aux = false;

        for (Object evento: eventos) {
            HashMap eventoAux = (HashMap) evento;
            HashMap beaconAux = (HashMap) eventoAux.get("beacon");

            // Una vez tengamos el beacon comprobamos que su nombre sea igual al del detectado.
            if (beacon != null) {
                if (beacon.equals(beaconAux.get("nombre"))) {
                    // Creamos el objeto notificacion aux.
                    notifyAux = new Notificacion((String) eventoAux.get("nombre"), (String) eventoAux.get("descripcion"));
                    aux = true;
                }
            }
        }
        return aux;
    }

    private boolean comprobarRedundanciaNotificacion(String nombreBeacon){
        boolean aux = false;

        for (String beacon: beaconsYaDetectados) {
            if (beacon.equals(nombreBeacon)){
                aux = true;
            }
        }
        return aux;
    }

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
                // boolean controlBool = sharedPref.getBoolean("controlEnabled", controlEnabled);
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

    private void obtainNotificaciones(final String emailUser) {
        dbReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                HashMap user = (HashMap) dataSnapshot.getValue();

                if (user.get("email").equals(emailUser)) {
                    // Leemos las notificaciones del usuario logueado.
                    HashMap notificaciones = (HashMap)  user.get("notificaciones");
                    //TODO. Añadir funcionalidad cuando el usuario tenga alguna notificacion.

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}





