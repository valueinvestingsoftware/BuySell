package x.stocks.valueinvesting;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import x.stocks.valueinvesting.DatabaseHelper.DatabaseHelper;
import x.stocks.valueinvesting.Models.ItemsModel;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static java.util.Comparator.comparing;

public class ItemsFragment extends Fragment {


    String insert, itemName, creationDate, imageIdOfDisplayedItem, itemId;
    Integer idOfLastItem, retrievedNivel;
    Spinner spinnerItems;
    AutoCompleteTextView autocompleteItemsUpdatePicture;

    List<String> labelItemIds = new ArrayList<String>();
    List<String> allLabelItemNamesToCheckDuplicates = new ArrayList<String>();

    Button btPickImage, btTakePicture, btInsertUpdateItem, btEditItem, btNewItem;
    TextView txtItemName;
    ImageView ivItemPhoto;
    View view;

    private static final int PICK_IMAGE_ITEM = 100;
    private static final int TAKE_PICTURE_ITEM = 123;

    DatabaseHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("CREATE ITEMS");

        Bundle bundle = getArguments();

        if (bundle != null) {
            insert = bundle.getString("insert");
            itemId = bundle.getString("itemId");
            itemName = bundle.getString("itemName");
        }

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        view = inflater.inflate(R.layout.fragment_items, container, false);

        //Assign variable
        btPickImage = view.findViewById(R.id.btnPickItemImage);
        btTakePicture = view.findViewById(R.id.btnTakePictureItem);
        btInsertUpdateItem = view.findViewById(R.id.btnInsertImageItem);
        btNewItem = view.findViewById(R.id.btnNewItem);
        btEditItem = view.findViewById(R.id.btnEditItem);
        autocompleteItemsUpdatePicture = view.findViewById(R.id.autoCompleteItemsUpdatePicture);

        txtItemName = view.findViewById(R.id.txtItemName);
        txtItemName.requestFocus();

        ivItemPhoto = view.findViewById(R.id.imageViewCreoItem);

        // Retrieve nivel from shared preferences
        SharedPreferences spRetrieveN = getContext().getApplicationContext().getSharedPreferences("MyUserPrefsN", Context.MODE_PRIVATE);
        retrievedNivel = spRetrieveN.getInt("ipn", 0);

        db = new DatabaseHelper(getContext());
        allLabelItemNamesToCheckDuplicates = db.getAllItems(1);

        if(insert=="0"){
           btInsertUpdateItem.setText("Update");
        }else{
            btInsertUpdateItem.setText("Insert");
        }

       btNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert = "1";
                btNewItem.setEnabled(false);
                txtItemName.setEnabled(true);
                btInsertUpdateItem.setEnabled(true);
                btInsertUpdateItem.setText("Insert");
                btEditItem.setEnabled(false);
                txtItemName.setText("");
                txtItemName.setError(null);

                allLabelItemNamesToCheckDuplicates = db.getAllItems(1);
            }
        });

        btPickImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Pick an image"), PICK_IMAGE_ITEM);
            }
        });

        btTakePicture.setOnClickListener(new View.OnClickListener() {
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

        spinnerItems = view.findViewById(R.id.spinnerUpdatePicture);
        spinnerItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                String label = parent.getItemAtPosition(position).toString();

                // Showing selected spinner item
                //  Toast.makeText(parent.getContext(), String.valueOf(labelItemIds.get(position)), Toast.LENGTH_LONG).show();

                    //Show image in imageview
                    txtItemName.setText(spinnerItems.getSelectedItem().toString());
                    imageIdOfDisplayedItem = String.valueOf(labelItemIds.get(position));
                    Bitmap bmp = db.getImage(Integer.parseInt(imageIdOfDisplayedItem));
                    ivItemPhoto.setImageBitmap(bmp);
                        txtItemName.setEnabled(false);
                        btNewItem.setEnabled(true);
                        btEditItem.setEnabled(true);
                        btInsertUpdateItem.setEnabled(false);
                txtItemName.setError(null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        loadItems();
        spinnerItems.setSelection(((ArrayAdapter) spinnerItems.getAdapter()).getPosition(itemName));

        btInsertUpdateItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogOnClickListener = new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                if (txtItemName.getText().toString().isEmpty()) {
                                    txtItemName.setError("Type an item name!");
                                } else {
                                    try {
                                        if(insert == "0"){
                                            saveImageAndItem();
                                        }else{
                                            if (allLabelItemNamesToCheckDuplicates.contains(txtItemName.getText().toString())) {
                                                Toast.makeText(getActivity(), "There is already an item with this name!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                saveImageAndItem();
                                            }
                                        }

                                    } catch (Exception e) {
                                        Toast.makeText(getActivity(), "Not saved - Take or choose a picture!", Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                        return; //salgo del metodo si no se guarda la imagen
                                    }
                                }
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                String what = "";
                if(insert == "0"){
                    what = " update";
                }else{
                    what = " insert";
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmation");
                builder.setIcon(android.R.drawable.stat_sys_download);
                builder.setCancelable(false);
                builder.setMessage("Do you want to " + what + " item " + txtItemName.getText().toString() + "?").setPositiveButton("Yes", dialogOnClickListener)
                        .setNegativeButton("No", dialogOnClickListener).show();
            };
        });

        btEditItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert = "0";
                txtItemName.setEnabled(true);
                btInsertUpdateItem.setEnabled(true);
                btInsertUpdateItem.setText("Update");
                txtItemName.setError(null);
                itemId = imageIdOfDisplayedItem;
            }
        });
        return view;
    }

    private void UpdateImage() { //I am not updating only image anymore
        db = new DatabaseHelper(getContext());
        Bitmap bitmap = ((BitmapDrawable) ivItemPhoto.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();

        int width = 0;
        int height = 0;
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

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        byte[] img = byteArray.toByteArray();
        Integer selectedItem = 0;
        itemName = spinnerItems.getSelectedItem().toString();
        creationDate = sdf.format(new Date());
        try {
            selectedItem = db.getSelectedItemId(itemName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ItemsModel itemsModel = null;
        try {

            itemsModel = new ItemsModel(selectedItem, "A", itemName, 0, creationDate, creationDate, img, true, true, false);
            DatabaseHelper db = new DatabaseHelper(getContext());
            boolean insert = db.updateOneItem(itemsModel, selectedItem.toString());
            if (insert = true) {
                Toast.makeText(getContext(), "Item image was updated", Toast.LENGTH_SHORT).show();
                //  SaveImageOnApp(bitmap); //It becomes slow to save it on disk, I find no need to do it
            } else {
                Toast.makeText(getContext(), "FAILED", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == TAKE_PICTURE_ITEM) {
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            ivItemPhoto.setImageBitmap(captureImage);
        }

        if (requestCode == PICK_IMAGE_ITEM) {
            Uri imageData = data.getData();
            ivItemPhoto.setImageURI(imageData);
        }
    }

    private void saveImageAndItem() {
        db = new DatabaseHelper(getContext());
        Bitmap bitmap = ((BitmapDrawable) ivItemPhoto.getDrawable()).getBitmap();
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

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        byte[] img = byteArray.toByteArray();
        itemName = txtItemName.getText().toString();
        creationDate = sdf.format(new Date());
        try {
            if(insert=="0"){
                idOfLastItem = Integer.parseInt(itemId) - 1;
            }else{
                labelItemIds = db.getAllItems(2);
                idOfLastItem = Integer.parseInt(Collections.max(labelItemIds));
            }
        } catch (Exception e) {
            idOfLastItem = 9999;
            e.printStackTrace();
        }

        ItemsModel itemsModel = null;
        try {

            itemsModel = new ItemsModel(idOfLastItem + 1, "A", itemName, 0, creationDate, creationDate, img, true, true, false);
            DatabaseHelper db = new DatabaseHelper(getContext());
            boolean exito = false;
            String what = "";
            if(insert=="1"){
                exito = db.addOneItem(itemsModel);
                what = " inserted";
            }else{
                exito = db.updateOneItem(itemsModel, itemId);
                what = " updated";
            }

            if (exito = true) {
                Toast.makeText(getContext(), "Item was " + what, Toast.LENGTH_SHORT).show();
                //  SaveImageOnApp(bitmap); //It becomes slow to save it on disk, I find no need to do it
                exito = loadItems();
                spinnerItems.setSelection(((ArrayAdapter) spinnerItems.getAdapter()).getPosition(itemName));

            } else {
                Toast.makeText(getContext(), "FAILED", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Boolean loadItems() {

        Boolean exito = false;

        try {
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
            autocompleteItemsUpdatePicture.setThreshold(1);
            autocompleteItemsUpdatePicture.setAdapter(dataAdapter);

            exito = true;
            return exito;

        } catch (Exception e) {
            exito = false;
            return exito;
        }
    }
}

