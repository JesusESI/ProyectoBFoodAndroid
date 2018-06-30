package jesus.com.proyectobfoodandroid;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

import jesus.com.proyectobfoodandroid.Firebase.FirebaseManager;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;

    private List eventos;
    private DatabaseReference dbReference;
    private FirebaseDatabase firebase;
    // Coordenadas

    private double LAT_CIU  = 38.9848295;
    private double LON_CIU = -3.927377799999931;
    /*private LatLngBounds CIU_BOUND = new LatLngBounds(new LatLng(38.94141187092434, -3.8525943777518705), new LatLng(39.00873510472663,
            -3.9731565706483707));*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Obtenemos las referencias.
        firebase = FirebaseManager.getFirebaseSingleton().getmDatabase();
        dbReference = firebase.getReference("Eventos");

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Obtener Localizaciones de eventos.

        // Prueba.
        LatLng ciu = new LatLng(LAT_CIU, LON_CIU);
        //mMap.addMarker(new MarkerOptions().position(ciu).title("Marker en Ciudad Real"));
        updateListMarcadores();
        mMap.moveCamera(CameraUpdateFactory.zoomBy(5));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ciu));


    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
            Toast.makeText(this, marker.getTitle() + " has been clicked " + clickCount + " times.", Toast.LENGTH_SHORT).show();
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }



    private void updateListMarcadores() {
        dbReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // Devuelve un HashMap debemos recorrerlo y guardar los datos.

               HashMap eventos = (HashMap) dataSnapshot.getValue();
               HashMap beaconsEvento = (HashMap) eventos.get("beacon");
               String lati = beaconsEvento.get("latitud").toString();
               String longi = beaconsEvento.get("longitud").toString();
               String title = eventos.get("nombre").toString();


               // Creamos el nuevo marcador y lo añadimos al mapa.
                LatLng nuevo = new LatLng(Double.valueOf(lati), Double.valueOf(longi));
                mMap.addMarker(new MarkerOptions().position(nuevo).title(title).icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_bfood)));

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
