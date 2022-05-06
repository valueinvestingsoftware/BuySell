package x.stocks.valueinvesting;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import x.stocks.valueinvesting.DatabaseHelper.DatabaseHelper;
import x.stocks.valueinvesting.Models.SalesModel;

public class SaleFragment extends Fragment {

    private Double SaleQuantity, SalePrice;
    private String SaleDate, ItemId, itemName, CustomerId, customerName, ObservationsSale;
    private Boolean SaleInApp, puedeModificarPrecio = false;
    int retrievedNivel;

    Spinner spinnerItems, spinnerCustomers, spinnerPriceLists;
    View view;
    List<String> labelIds = new ArrayList<String>();
    List<String> labelPriceListsIds = new ArrayList<String>();
    List<String> labelItemIds = new ArrayList<String>();
    AutoCompleteTextView autocompleteCustomers, autocompleteItems;
    ImageView imagenItemSale = null;
    Button btSave;
    EditText etQtySale, etPriceSale, etObservationsSale;
    Bitmap bmp = null;
    DatabaseHelper db;
    OutputStream outputstream;
    String updateDate, insert;
    String imageIdOfDisplayedItem, idOfDisplayedCustomer, idOfDisplayedListPrice;
    String saleQty, salePrice, saleId;
    Integer sId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("SALE");

       getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        view = inflater.inflate(R.layout.fragment_sale, container, false);
        autocompleteCustomers = view.findViewById(R.id.autoCompleteCustomers);
        autocompleteItems = view.findViewById(R.id.autoCompleteSoldItems);
        imagenItemSale = view.findViewById(R.id.imageViewItemSale);
        btSave = view.findViewById(R.id.btnSaveSale);
        etQtySale =  view.findViewById(R.id.txtQtySale);
        etQtySale.requestFocus();
        etPriceSale =  view.findViewById(R.id.txtPriceSale);
        etObservationsSale = view.findViewById(R.id.txtObservationsSale);
        db = new DatabaseHelper(getContext());
        spinnerItems = view.findViewById(R.id.spinnerItemsSale10);
        spinnerPriceLists = view.findViewById(R.id.spinnerPriceListsSale10);
        spinnerCustomers = view.findViewById(R.id.spinnerCustomers);

        Bundle bundle = getArguments();

        if (bundle != null) {
            insert = bundle.getString("insert");
            saleId = bundle.getString("saleid");
            customerName = bundle.getString("salecustomername");
            itemName = bundle.getString("saleitemname");
            ObservationsSale = bundle.getString("saleobservations");
            saleQty = bundle.getString("saleqty");
            salePrice = bundle.getString("saleprice");
        }

        // Retrieve nivel from shared preferences
        SharedPreferences spRetrieveN = getContext().getApplicationContext().getSharedPreferences("MyUserPrefsN", Context.MODE_PRIVATE);
        retrievedNivel = spRetrieveN.getInt("ipn", 0);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        updateDate = sdf.format(new Date());

        if(insert== "0"){
            etQtySale.setText(saleQty);
            etPriceSale.setText(salePrice);
            etObservationsSale.setText(ObservationsSale);
        }else{
            etObservationsSale.setText("");
            etPriceSale.setText("");
            etQtySale.setText("");
        }

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etQtySale.getText().toString().isEmpty()) {
                    etQtySale.setError("Type a quantity!");
                } else {
                    if (etPriceSale.getText().toString().isEmpty()) {
                        etPriceSale.setError("Type a price!");
                    } else {
                        try {
                            // saveImage(); //I am not saving here, and I removed the possibility of picking an image while buying or selling
                        }catch(Exception e){
                            Toast.makeText(getActivity(), "Not saved - Take or choose a picture!", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                            return; //salgo del metodo si no se guarda la imagen
                        }
                        saveSale();
                    }
                }
            }
        });

        spinnerItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                String label = parent.getItemAtPosition(position).toString();

                // Showing selected spinner item
                //  Toast.makeText(parent.getContext(), String.valueOf(labelItemIds.get(position)), Toast.LENGTH_LONG).show();

                //Show image in imageview
                imageIdOfDisplayedItem = String.valueOf(labelItemIds.get(position));
                bmp = db.getImage(Integer.parseInt(imageIdOfDisplayedItem));
                imagenItemSale.setImageBitmap(bmp);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
      loadItems();
      spinnerItems.setSelection(((ArrayAdapter) spinnerItems.getAdapter()).getPosition(itemName));

        spinnerPriceLists.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String label = parent.getItemAtPosition(position).toString();

                // Showing selected spinner item
              //  Toast.makeText(parent.getContext(), String.valueOf(labelPriceListsIds.get(position)), Toast.LENGTH_LONG).show();
                idOfDisplayedListPrice = String.valueOf(labelPriceListsIds.get(position));
                imageIdOfDisplayedItem = String.valueOf(labelItemIds.get(position));

                if(idOfDisplayedCustomer !=""){
                    DatabaseHelper db = new DatabaseHelper(getContext());
                    Double price =  db.getPrice(idOfDisplayedListPrice, imageIdOfDisplayedItem);

                    if(insert == "1"){
                        etPriceSale.setText(price.toString());
                    }else{
                        if(puedeModificarPrecio == true){
                            etPriceSale.setText(price.toString());
                        }
                        puedeModificarPrecio = true;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(spinnerItems.getCount()==0){  //If I do not do this I get a crash since LoadPriceLists() references a value in the spinner

        }else{
            LoadPriceLists();
        }

        spinnerCustomers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String label = parent.getItemAtPosition(position).toString();
                //  Toast.makeText(getActivity(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
              //  Toast.makeText(getActivity(), String.valueOf(labelIds.get(position)), Toast.LENGTH_LONG).show();
                idOfDisplayedCustomer = String.valueOf(labelIds.get(position));
                if(idOfDisplayedListPrice!=""){
                  //  DatabaseHelper db = new DatabaseHelper(getContext());
                  // Double price =  db.getPrice(idOfDisplayedListPrice, idOfDisplayedCustomer);
                 // etPriceSale.setText(price.toString());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        loadCustomers();
        spinnerCustomers.setSelection(((ArrayAdapter) spinnerCustomers.getAdapter()).getPosition(customerName));

        return view;
    }


    private void loadItems() {
        // database handler
        DatabaseHelper db = new DatabaseHelper(getContext());

        // Spinner Drop down elements
        List<String> lables = db.getAllItems(1);
        labelItemIds = db.getAllItems(2);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerItems.setAdapter(dataAdapter);
        // Spinner click listener
        autocompleteItems.setThreshold(1);
        autocompleteItems.setAdapter(dataAdapter);
    }

    private void loadCustomers() {
        // database handler
        DatabaseHelper db = new DatabaseHelper(getContext());

        // Spinner Drop down elements
        List<String> lables = db.getAllCustomers(1);
        labelIds = db.getAllCustomers(2);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerCustomers.setAdapter(dataAdapter);
        // Spinner click listener

        autocompleteCustomers.setThreshold(1);
        autocompleteCustomers.setAdapter(dataAdapter);
    }

    private void LoadPriceLists() {
        // database handler
        DatabaseHelper db = new DatabaseHelper(getContext());

        // Spinner Drop down elements
        List<String> lables = db.getAllPriceLlists(1);
        labelPriceListsIds = db.getAllPriceLlists(2);
        labelItemIds = db.getAllItems(2);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerPriceLists.setAdapter(dataAdapter);
        // Spinner click listener
        db.close();
    }

    public void SaveImageOnApp(Bitmap bitmap){
        File filepath = Environment.getExternalStorageDirectory();
        File dir = new File(filepath.getAbsolutePath() + "/value/");
        dir.mkdir();
        File file = new File(dir, System.currentTimeMillis() + ".png");
        try{
            outputstream = new FileOutputStream(file);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.PNG, 20, outputstream);
        Toast.makeText(getContext(),"Image saved on disk", Toast.LENGTH_SHORT).show();
    }

    private void saveSale(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        ItemId = imageIdOfDisplayedItem;
        CustomerId = idOfDisplayedCustomer;
        etQtySale = view.findViewById(R.id.txtQtySale);
        etPriceSale = view.findViewById(R.id.txtPriceSale);
        etObservationsSale = view.findViewById(R.id.txtObservationsSale);
        SaleDate = sdf.format(new Date());
        SaleInApp = true;

        SaleQuantity= Double.parseDouble(etQtySale.getText().toString());
        SalePrice = Double.parseDouble(etPriceSale.getText().toString());
        ObservationsSale = etObservationsSale.getText().toString();

        if(insert == "0"){
            sId = Integer.parseInt(saleId);
        }else{
            sId = -1;
        }
        //Alimento el model y ejecuto el guardar en la base de datos la compra
        SalesModel salesModel = null;
        try{
            salesModel = new SalesModel(sId, Integer.parseInt(ItemId), Integer.parseInt(CustomerId), SaleQuantity, SalePrice, SaleDate,
                    ObservationsSale, 0.0, SaleInApp, false);
          //  Toast.makeText(getActivity(), salesModel.toString(), Toast.LENGTH_LONG).show();
        }catch(Exception e){
            Toast.makeText(getActivity(), "Error creating customer", Toast.LENGTH_LONG).show();
        }
        DatabaseHelper dataBasehelper = new DatabaseHelper(getContext());
        boolean success;
        if(insert == "0"){
            success = dataBasehelper.updateOneSale(salesModel, saleId);
        }else{
            success = dataBasehelper.addOneSale(salesModel);
        }

        Toast.makeText(getActivity(), "Success = " +  success, Toast.LENGTH_SHORT).show();
    }
}