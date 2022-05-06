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
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import x.stocks.valueinvesting.Models.CustomerModel;
import x.stocks.valueinvesting.Models.PurchasesModel;

public class PurchaseFragment extends Fragment {

    private Double PurchaseQuantity, PurchasePrice;
    private String PurchaseDate, ItemId, itemName, SupplierId, supplierName, ObservationsPurchase;
    private Boolean PurchaseInApp;

    Spinner spinnerItems, spinnerSuppliers;
    View view;
    List<String> labelIds = new ArrayList<String>();
    List<String> labelItemIds = new ArrayList<String>();
    AutoCompleteTextView autocompleteSupplier, autocompleteItems;
    ImageView imagenItem = null;
    Button btSave;
    EditText etQtyPurchase, etPricePurchase, etObservationsPurchase;
    Bitmap bmp = null;
    private static final int PICK_IMAGE = 100;
    private static final int TAKE_PICTURE = 123;
    DatabaseHelper db;
    OutputStream outputstream;
    String insert, updateDate, purchaseQty, purchasePrice, purchaseId, leyend;
    String imageIdOfDisplayedItem, idOfDisplayedSupplier;
    Integer pId;
    int totalArrayLength, a = 0, retrievedNivel;
    Handler h = new Handler();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("PURCHASE");

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        view = inflater.inflate(R.layout.fragment_purchase, container, false);
        autocompleteSupplier = view.findViewById(R.id.autoCompleteSuppliers);
        autocompleteItems = view.findViewById(R.id.autoCompleteItems);
        imagenItem = view.findViewById(R.id.imageViewItem);
        btSave = view.findViewById(R.id.btnSave);
        etQtyPurchase = view.findViewById(R.id.txtQtyPurchase);
        etQtyPurchase.requestFocus();
        etPricePurchase = view.findViewById(R.id.txtPricePurchase);
        etObservationsPurchase = view.findViewById(R.id.txtObservationsPurchase);

        Bundle bundle = getArguments();

        if (bundle != null) {
            insert = bundle.getString("insert");
            purchaseId= bundle.getString("purchaseId");
            supplierName = bundle.getString("purchaseSupplierName");
            itemName = bundle.getString("purchaseItemName");
            ObservationsPurchase = bundle.getString("purchaseObservations");
            purchaseQty = bundle.getString("purchaseQty");
            purchasePrice = bundle.getString("purchasePrice");
        }

        db = new DatabaseHelper(getContext());

        etObservationsPurchase.setText(ObservationsPurchase);

        // Retrieve nivel from shared preferences
        SharedPreferences spRetrieveN = getContext().getApplicationContext().getSharedPreferences("MyUserPrefsN", Context.MODE_PRIVATE);
        retrievedNivel = spRetrieveN.getInt("ipn", 0);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        updateDate = sdf.format(new Date());

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etQtyPurchase.getText().toString().isEmpty()) {
                    etQtyPurchase.setError("Type a quantity!");
                } else {
                    if (etPricePurchase.getText().toString().isEmpty()) {
                        etPricePurchase.setError("Type a price!");
                    } else {
                        try {
                           // saveImage(); //I am not saving here, and I removed the possibility of picking an image while buying or selling
                        }catch(Exception e){
                            Toast.makeText(getActivity(), "Not saved - Take or choose a picture!", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                            return; //salgo del metodo si no se guarda la imagen
                        }
                        savePurchase();
                    }
                }
            }
        });


        spinnerItems = view.findViewById(R.id.spinnerItems);
        spinnerItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                String label = parent.getItemAtPosition(position).toString();

                // Showing selected spinner item
              //  Toast.makeText(parent.getContext(), String.valueOf(labelItemIds.get(position)) , Toast.LENGTH_LONG).show();

                if(insert=="1"){
                    etObservationsPurchase.setText("");
                    etQtyPurchase.setText("");
                    etPricePurchase.setText("");
                }

                //Show image in imageview
                imageIdOfDisplayedItem = String.valueOf(labelItemIds.get(position));
                bmp = db.getImage(Integer.parseInt(imageIdOfDisplayedItem));
                imagenItem.setImageBitmap(bmp);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        loadItems();
        spinnerItems.setSelection(((ArrayAdapter) spinnerItems.getAdapter()).getPosition(itemName));

        spinnerSuppliers = view.findViewById(R.id.spinnerProviders);
        spinnerSuppliers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String label = parent.getItemAtPosition(position).toString();
              //  Toast.makeText(getActivity(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
              //  Toast.makeText(getActivity(), String.valueOf(labelIds.get(position)), Toast.LENGTH_LONG).show();
                idOfDisplayedSupplier = String.valueOf(labelIds.get(position));

                if(purchaseQty!= ""){
                    etQtyPurchase.setText(purchaseQty);
                    etPricePurchase.setText(purchasePrice);
                }else{
                    etObservationsPurchase.setText("");
                    etQtyPurchase.setText("");
                    etPricePurchase.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        loadSuppliers();
        spinnerSuppliers.setSelection(((ArrayAdapter) spinnerSuppliers.getAdapter()).getPosition(supplierName));

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


    private void loadSuppliers() {
        // database handler
        DatabaseHelper db = new DatabaseHelper(getContext());

        // Spinner Drop down elements
        List<String> lables = db.getAllSuppliers(1);
         labelIds = db.getAllSuppliers(2);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerSuppliers.setAdapter(dataAdapter);
        // Spinner click listener
        autocompleteSupplier.setThreshold(1);
        autocompleteSupplier.setAdapter(dataAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == TAKE_PICTURE){
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            imagenItem.setImageBitmap(captureImage);
        }

        if(requestCode == PICK_IMAGE){
            Uri imageData =  data.getData();
            imagenItem.setImageURI(imageData);
        }
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

    private void saveImage() {  //I am not saving here, and I removed the possibility of picking an image while buying or selling
        db = new DatabaseHelper(getContext());
        Bitmap bitmap = ((BitmapDrawable)imagenItem.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 20, byteArray);
        byte[] img = byteArray.toByteArray();

        boolean update = db.UpdateImage(String.valueOf(imageIdOfDisplayedItem),img, true);
        if(update = true){
            Toast.makeText(getContext(), "Record was updated", Toast.LENGTH_SHORT).show();
          //  SaveImageOnApp(bitmap); //It becomes slow to save it on disk, I find no need to do it

        }else{
            Toast.makeText(getContext(), "FAILED", Toast.LENGTH_SHORT).show();
        }

    }

    private void savePurchase(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        ItemId = imageIdOfDisplayedItem;
        SupplierId = idOfDisplayedSupplier;
        etQtyPurchase = view.findViewById(R.id.txtQtyPurchase);
        etPricePurchase = view.findViewById(R.id.txtPricePurchase);
        etObservationsPurchase = view.findViewById(R.id.txtObservationsPurchase);
        PurchaseDate = sdf.format(new Date());
        PurchaseInApp = true;

         PurchaseQuantity= Double.parseDouble(etQtyPurchase.getText().toString());
         PurchasePrice = Double.parseDouble(etPricePurchase.getText().toString());
        ObservationsPurchase = etObservationsPurchase.getText().toString();

        if(insert == "0"){
            pId = Integer.parseInt(purchaseId);
        }else{
            pId = -1;
        }
        //Alimento el model y ejecuto el guardar en la base de datos la compra
        PurchasesModel purchasesModel = null;
        try{
            purchasesModel = new PurchasesModel(pId, Integer.parseInt(ItemId), Integer.parseInt(SupplierId), PurchaseQuantity,PurchasePrice, PurchaseDate,
                    ObservationsPurchase,PurchaseInApp, false);
           // Toast.makeText(getActivity(), purchasesModel.toString(), Toast.LENGTH_LONG).show();
        }catch(Exception e){
            Toast.makeText(getActivity(), "Error recording purchase", Toast.LENGTH_LONG).show();
        }
        DatabaseHelper dataBasehelper = new DatabaseHelper(getContext());
        boolean success = false;
        if(insert == "1"){
            success = dataBasehelper.addOnePurchase(purchasesModel);
        }else{
            success = dataBasehelper.updateOnePurchase(purchasesModel, purchaseId);
        }
        Toast.makeText(getActivity(), "Success = " +  success, Toast.LENGTH_SHORT).show();
    }

}
