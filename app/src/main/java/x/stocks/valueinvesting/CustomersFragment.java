package x.stocks.valueinvesting;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import x.stocks.valueinvesting.DatabaseHelper.DatabaseHelper;
import x.stocks.valueinvesting.Models.CustomerModel;

public class CustomersFragment extends Fragment {

    Integer idOfLastCustomerId;
    List<Integer> labelCustomerIds = new ArrayList<>();

    //Assign variable

    Button btLocation, btGoToMap, btSave, btFoto;
    ImageView ivFoto;
    TextView nameCliente, address, city, x, y, comment, telephone, mail, lblprecize;
    FusedLocationProviderClient client;
    String insert, customerId, customerName, lat, lon, direccion, ciudad, comentario, telefono, email, creationDate, editionDate, supplier, customerSupplier;
       Double doubleLat, doubleLon;
        View view;

    List<String> allLabelProviderNamesToCheckDuplicates = new ArrayList<String>();
    List<String> allLabelCustomerNamesToCheckDuplicates = new ArrayList<String>();

    private static final int PICK_IMAGE_ITEM = 100;
    private static final int TAKE_PICTURE_ITEM = 123;

    DatabaseHelper db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();

        if (bundle != null) {
            supplier = bundle.getString("supplier");
            insert = bundle.getString("insert");
            customerId = bundle.getString("customerId");
            customerName = bundle.getString("customerName");
            email = bundle.getString("customerEmail");
            telefono = bundle.getString("customerTelephone");
            comentario = bundle.getString("customerObservations");
            direccion = bundle.getString("customerAddress");
            ciudad = bundle.getString("customerCity");
            lat = bundle.getString("customerLatitud");
            lon = bundle.getString("customerLongitud");
        }

        if(supplier == "0"){
            getActivity().setTitle("CREATE CUSTOMERS");
            customerSupplier = "Customer";
        }else{
            getActivity().setTitle("CREATE SUPPLIERS");
            customerSupplier = "Supplier";
        }


        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        view = inflater.inflate(R.layout.fragment_customers, container, false);

        //Assign variable
        btLocation = view.findViewById(R.id.btnLocation);
        btGoToMap = view.findViewById(R.id.btnGoToMap);
        btGoToMap.setEnabled(false);
        btSave = view.findViewById(R.id.btnSaveCustomer);
        btSave.setEnabled(false);
        btFoto = view.findViewById(R.id.btnTakePictureCustomers);
        ivFoto = view.findViewById(R.id.ivCustomers);

        nameCliente = view.findViewById(R.id.txtCustomerName);
        nameCliente.setText(customerName);

        address = view.findViewById(R.id.lblAddressCustomer);
        address.setText(direccion);

        city = view.findViewById(R.id.lblCityCustomer);
        city.setText(ciudad);

        comment = view.findViewById(R.id.txtObservacionesCustomer);
        comment.setText(comentario);

        telephone = view.findViewById(R.id.txtTelephoneCustomer);
        telephone.setText(telefono);

        mail = view.findViewById(R.id.txtEmailCustomer);
        mail.setText(email);

        x = view.findViewById(R.id.lblLongitudCustomer);
        x.setText(lat);

        y = view.findViewById(R.id.lblLatitudCustomer);
        y.setText(lon);

        if(insert == "0"){
            db = new DatabaseHelper(getContext());
            byte[] imag = db.getImageBytesFromMap(Integer.parseInt(customerId));
            Bitmap bt = BitmapFactory.decodeByteArray(imag, 0, imag.length);
            ivFoto.setImageBitmap(bt);
            btSave.setEnabled(true);
            btLocation.setEnabled(false);
        }
        lblprecize = view.findViewById(R.id.lblPrecisionCustomer);

     //   customerName = nameCliente.getText().toString();
      //  direccion =address.getText().toString();
     //   ciudad = city.getText().toString();
    //    telefono = telephone.getText().toString();
     //   email = mail.getText().toString();
      //  comentario = comment.getText().toString();
     //   lon = x.getText().toString();
     //   lat = y.getText().toString();

        db = new DatabaseHelper(getContext());
        allLabelProviderNamesToCheckDuplicates = db.getAllSuppliers(1);
        allLabelCustomerNamesToCheckDuplicates = db.getAllCustomers(1);

        labelCustomerIds = db.getAllCustomersAndProvidesIdsToGetMaxId();

        //Initialize location client
        client = LocationServices.getFusedLocationProviderClient(getActivity());

        btSave.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogOnClickListener = new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                if(nameCliente.getText().toString().isEmpty()){
                                    nameCliente.setError("Type your customer's name!");
                                }else{
                                    if (x.getText().toString().isEmpty()) {
                                        x.setError("No coordinates have been found yet!");
                                    }else{
                                        if(supplier == "0"){
                                            customerName = nameCliente.getText().toString();
                                            if(insert=="0"){
                                                SaveSupplier();
                                            }else{
                                                if(allLabelCustomerNamesToCheckDuplicates.contains(customerName)){
                                                    Toast.makeText(getActivity(), "There is already another customer with the same name!", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    SaveSupplier();
                                                    allLabelCustomerNamesToCheckDuplicates = db.getAllCustomers(1);
                                                    labelCustomerIds = db.getAllCustomersAndProvidesIdsToGetMaxId();
                                                }
                                            }
                                        }else{
                                            customerName = nameCliente.getText().toString();
                                            if(insert == "0"){
                                                SaveSupplier();
                                            }else{
                                                if(allLabelProviderNamesToCheckDuplicates.contains(customerName)){
                                                    Toast.makeText(getActivity(), "There is already another supplier with the same name!", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    SaveSupplier();
                                                    allLabelProviderNamesToCheckDuplicates = db.getAllSuppliers(1);
                                                }
                                            }
                                        }
                                    }
                                }
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                }
            }
        };

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmation");
                builder.setIcon(android.R.drawable.stat_sys_download);
                builder.setCancelable(false);
                builder.setMessage("Do you want to create " + customerSupplier + " " + nameCliente.getText().toString() + "?").setPositiveButton("Yes", dialogOnClickListener)
                        .setNegativeButton("No", dialogOnClickListener).show();
            };
        });

        btLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getActivity()
                        , Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getActivity()
                                , Manifest.permission.ACCESS_COARSE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();

                }else{
                    //When permission is not granted
                    //Request permission
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                    , Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                }
            }
        });

        btFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{
                                    Manifest.permission.CAMERA
                            },
                            TAKE_PICTURE_ITEM);
                }

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, TAKE_PICTURE_ITEM);
            }
        });

        btGoToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (x.getText().toString().isEmpty()) {
                 x.setError("No coordinates to show!");
                }else{
                    if (ContextCompat.checkSelfPermission(getActivity()
                            , Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(getActivity()
                                    , Manifest.permission.ACCESS_COARSE_LOCATION)
                                    == PackageManager.PERMISSION_GRANTED) {

                        // colocar en las variables el contenido de los textboxes
                        customerName = nameCliente.getText().toString();
                        lon = x.getText().toString();
                        lat = y.getText().toString();

                        doubleLon = Double.parseDouble(x.getText().toString());
                        doubleLat = Double.parseDouble(y.getText().toString());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
                        creationDate = sdf.format(new Date());
                        editionDate = sdf.format(new Date());

                        openMapFragment();

                    }else{
                        //When permission is not granted
                        //Request permission
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                                , Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                    }
                }
                }

        });
        //return view
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == TAKE_PICTURE_ITEM) {
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            ivFoto.setImageBitmap(captureImage);
            btSave.setEnabled(true);
        }

        if (requestCode == PICK_IMAGE_ITEM) {
            Uri imageData = data.getData();
            ivFoto.setImageURI(imageData);
        }

    }
    private void openMapFragment() {

        MapFragment mapFragment = new MapFragment();
        Bundle bundle = new Bundle();
        bundle.putString("nameCliente", String.valueOf(customerName));
        bundle.putString("x", String.valueOf(lon));
        bundle.putString("y", String.valueOf(lat));
        mapFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getFragmentManager()
       .beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,
                mapFragment).commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Check condition
        if (requestCode == 100 && (grantResults.length > 0) &&
                (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            //When permissions are granted
            //Call Method
            getCurrentLocation();
        }else{
            //When permissions are denied
            //Display toast
            Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }
    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        //Initialize location manager
        LocationManager locationManager = (LocationManager) getActivity()
                .getSystemService(Context.LOCATION_SERVICE);
        //Check condition
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        ||locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            //When location service is enabled
            //Get last location
            client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                 //Initialize location
                    Location location = task.getResult();
                    //Check condition
                    if(location != null){

                        try {
                            double precision = location.getAccuracy();
                            double precisionRounded = Math.round (precision * 10000.0) / 10000.0;

                            //Initialize geocoder
                            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                            //Initialize address list
                            List<Address> addresses = geocoder.getFromLocation(
                               location.getLatitude(), location.getLongitude(),1
                            );
                            lblprecize.setText(Double.toString(precisionRounded) + " m");
                            city.setText(Html.fromHtml("<font color = '#6200EE'>" + addresses.get(0).getLocality()));
                            address.setText(Html.fromHtml("<font color = '#6200EE'>" + addresses.get(0).getAddressLine(0)));
                            x.setText(Html.fromHtml("<font color = '#6200EE'>" + addresses.get(0).getLongitude()));
                            y.setText(Html.fromHtml("<font color = '#6200EE'>" + addresses.get(0).getLatitude()));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //When Location result is not null
                        //Set Latitude and longitude, lo de abajo es un metodo diferente
                       // y.setText(String.valueOf(location.getLatitude()));
                       // x.setText(String.valueOf(location.getLongitude()));
                    }else{
                        //When location result is null
                        //Initialize location request
                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);

                        //Initialize Location call back
                        LocationCallback locationCallback = new LocationCallback(){
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                //Initialize location
                                Location location1 = locationResult.getLastLocation();
                                //Set Latitude and longitude
                                y.setText(String.valueOf(location1.getLatitude()));
                                x.setText(String.valueOf(location1.getLongitude()));
                            }
                        };
                        //Request location updates
                        client.requestLocationUpdates(locationRequest
                        , locationCallback, Looper.myLooper());
                    }
                }
            });
        }else{
            //When Location service is not enabled
            //Open Location settings
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    private void SaveSupplier(){

        db = new DatabaseHelper(getContext());

        Bitmap bitmap = ((BitmapDrawable) ivFoto.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        int width;
        int height;
        if(bitmap.getWidth() > bitmap.getHeight()){
            width = 300;
            height = (bitmap.getHeight() * width)/bitmap.getWidth();
        }else{
            width = 200;
            height = (bitmap.getHeight() * width)/bitmap.getWidth();
        }
        bitmap = Bitmap.createScaledBitmap(bitmap, width,
                height, true);
        bitmap.compress(Bitmap.CompressFormat.PNG, 10, byteArray);

        boolean supplierBool;
        if(supplier == "0"){
            supplierBool = false;
        }else{
            supplierBool = true;
        }
        customerName = nameCliente.getText().toString();
        direccion =address.getText().toString();
        ciudad = city.getText().toString();
        telefono = telephone.getText().toString();
        email = mail.getText().toString();
        comentario = comment.getText().toString();
        lon = x.getText().toString();
        lat = y.getText().toString();

        doubleLon = Double.parseDouble(x.getText().toString());
        doubleLat = Double.parseDouble(y.getText().toString());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        byte[] img = byteArray.toByteArray();

        creationDate = sdf.format(new Date());
        editionDate = sdf.format(new Date());

        if(insert == "1"){
            if(labelCustomerIds.isEmpty() == false){
                idOfLastCustomerId = Collections.max(labelCustomerIds);
            }else{
                idOfLastCustomerId = 0;
            }
        }else{
            idOfLastCustomerId = Integer.parseInt(customerId) -1;
        }


        if(telefono.length()==0){
            telefono = "999999999";
        }
        x.setError(null);

        CustomerModel customerModel = null;
        try{
            customerModel = new CustomerModel(idOfLastCustomerId + 1, customerName, supplierBool, direccion,ciudad,
                    telefono,email,comentario, doubleLat, doubleLon, creationDate, editionDate, true, false, img);
          //  Toast.makeText(getActivity(), customerModel.toString(), Toast.LENGTH_LONG).show();
        }catch(Exception e){
            Toast.makeText(getActivity(), "Error creating customer", Toast.LENGTH_LONG).show();
        }
        DatabaseHelper dataBasehelper = new DatabaseHelper(getContext());
        boolean success = false;
               if(insert == "1"){
                   success = dataBasehelper.addOneClient(customerModel);
               } else{
                   success = dataBasehelper.updateOneClient(customerModel);
               }
        btGoToMap.setEnabled(true);
        Toast.makeText(getActivity(), "Success = " +  success, Toast.LENGTH_SHORT).show();
    }
}
