package x.stocks.valueinvesting;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import x.stocks.valueinvesting.DatabaseHelper.DatabaseHelper;
import x.stocks.valueinvesting.Models.BuySellModel;
import x.stocks.valueinvesting.Models.CustomerModel;
import x.stocks.valueinvesting.Models.ItemsModel;
import x.stocks.valueinvesting.Models.PriceListsModel;
import x.stocks.valueinvesting.Models.PricesModel;
import x.stocks.valueinvesting.Models.SalesPersonsModel;

import android.os.Handler;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;


public class SyncFragment extends Fragment  {
    String retrievedIP;
    String retrievedSalesPerson;
    String retrievedPhoneCode;
    TextView tip;
    Button btSyncUpClients, btSyncDownClients, btSyncUpItems, btSyncDownItems;
    Button btCheckInServer, btSyncUpPurchasesSales, btUpdate, btUpdateSalesPerson;
    Spinner spinnerSalesPersons;
    View view;
    int totalArrayLength = 0, a = 0, levels = 0, retrievedNivel;            ;
    Handler h = new Handler();
    DatabaseHelper db;
    Boolean HayClientes = false, HayPurchases = false, HaySales = false, HayItems = false;

    SharedPreferences sp, spn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("SYNC");

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        view = inflater.inflate(R.layout.fragment_sync, container, false);
        spinnerSalesPersons = view.findViewById(R.id.spSalesPersons);
        btSyncDownClients = view.findViewById(R.id.btnSyncDownClients);
        btSyncUpClients = view.findViewById(R.id.btnSyncUpClients);
        btSyncDownItems = view.findViewById(R.id.btnSyncDownItems);
        btSyncUpItems = view.findViewById(R.id.btnSyncUpItems);
        btSyncUpPurchasesSales = view.findViewById(R.id.btnSyncUpPurchasesSales);
        btUpdate = view.findViewById(R.id.btnUpdateIp);
        btUpdateSalesPerson = view.findViewById(R.id.btnUpdateSalesPerson);
        btCheckInServer = view.findViewById(R.id.btnCheckInServer);
        tip = view.findViewById(R.id.txtIp);

        // Retrieve ip from shared preferences
        SharedPreferences spRetrieve = getContext().getApplicationContext().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        retrievedIP = spRetrieve.getString("ip", "");
        retrievedSalesPerson = spRetrieve.getString("salesperson", "No sales person has been saved yet!");
        retrievedPhoneCode = spRetrieve.getString("phonecode", "+111");

        tip.setText(retrievedIP);
        //To assign into shared preferences
        sp = getContext().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);

        // Retrieve nivel from shared preferences
        SharedPreferences spRetrieveN = getContext().getApplicationContext().getSharedPreferences("MyUserPrefsN", Context.MODE_PRIVATE);
        retrievedNivel = spRetrieveN.getInt("ipn", 0);
        //To assign into shared preferences
        spn = getContext().getSharedPreferences("MyUserPrefsN", Context.MODE_PRIVATE);

        btSyncDownItems.setEnabled(false);
        btSyncDownClients.setEnabled(false);
        btSyncUpClients.setEnabled(false);
        btSyncUpPurchasesSales.setEnabled(false);
        btSyncUpItems.setEnabled(false);

        db = new DatabaseHelper(getContext());
        db.delete("SalesPersons");
        BajarSalesPersons();
        BajarConfig();
        GetNiveles();
        
        btCheckInServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerificaSiMapSyncContainsValues();
                VerificaSiPurchasedItemsSyncContainsValues();
                VerificaSiSoldItemsSyncContainsValues();
                VerificaSiCatMapSyncContainsValues();

                loadSalesPersons();
                Toast.makeText(requireContext(), "Salesperson: " + retrievedSalesPerson, Toast.LENGTH_LONG).show();
                Toast.makeText(requireContext(), "Country code: " + retrievedPhoneCode, Toast.LENGTH_LONG).show();
                spinnerSalesPersons.setSelection(((ArrayAdapter) spinnerSalesPersons.getAdapter()).getPosition(retrievedSalesPerson));

                btSyncDownItems.setEnabled(true);
                btSyncDownClients.setEnabled(true);
            }
        });

        btSyncDownClients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

          DialogInterface.OnClickListener dialogOnClickListener = new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                  switch (which){
                      case DialogInterface.BUTTON_POSITIVE:
                          db = new DatabaseHelper(getContext());
                          db.delete("Map");
                          db.delete("PurchasedItems");
                          db.delete("SoldItems");
                          BajarClientes();
                          loadSalesPersons();
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
                builder.setMessage("Customers, suppliers, purchases and sales will be deleted before downloading customers and providers, do you want to proceed?").setPositiveButton("Yes", dialogOnClickListener)
                        .setNegativeButton("No", dialogOnClickListener).show();
                    };
                });

        btSyncDownItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogOnClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                db = new DatabaseHelper(getContext());
                                db.delete("CatMap");
                                db.delete("BuySell");
                                db.delete("Prices");
                                db.delete("PurchasedItems");
                                db.delete("SoldItems");
                                db.delete("PriceLists");
                                BajarItems();
                                BajarBuySell();
                                BajarPrices();
                                BajarPriceLists();
                                loadSalesPersons();
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
                builder.setMessage("Items, prices, price lists, purchases and sales will be deleted before downloading items, do you want to proceed?").setPositiveButton("Yes", dialogOnClickListener)
                        .setNegativeButton("No", dialogOnClickListener).show();
            }
        });

        btSyncUpClients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   db = new DatabaseHelper(getContext());
                //  byte[] img = db.getImageBytes(1129);

                DialogInterface.OnClickListener dialogOnClickListener = new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Boolean exito;
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                if(HayClientes == false){
                                    exito = LoopThroughClientes();
                                    UpdateSyncAuditUpSync(2, "Clients and Suppliers");
                                    if(exito == true){
                                        Toast.makeText(getActivity(), "Clients/Suppliers have been uploaded to the server!", Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(getActivity(), "Neither Clients nor Suppliers have been uploaded to the server!", Toast.LENGTH_LONG).show();
                                    }
                                    HayClientes = true;
                                }else{
                                    Toast.makeText(getActivity(), "You have already uploaded suppliers and customers to the server!", Toast.LENGTH_LONG).show();
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
                builder.setMessage("Do you want to upload customers and suppliers?").setPositiveButton("Yes", dialogOnClickListener)
                        .setNegativeButton("No", dialogOnClickListener).show();
            }
        });

        btSyncUpItems.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               DialogInterface.OnClickListener dialogOnClickListener = new DialogInterface.OnClickListener() {

                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       Boolean exito = false;
                       switch (which) {
                           case DialogInterface.BUTTON_POSITIVE:
                               exito = LoopThroughItems();
                               UpdateSyncAuditUpSync(1, "Items");
                               if(exito==true){
                                   Toast.makeText(getActivity(), "Items have been synchronized to the server!", Toast.LENGTH_LONG).show();

                               }else{
                                   Toast.makeText(getActivity(), "No items have been synchronized to the server!", Toast.LENGTH_LONG).show();

                               }
                               break;
                           case DialogInterface.BUTTON_NEGATIVE:
                               break;
                       }
                   }
               };
               AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
               builder.setTitle("Confirmation");
               builder.setIcon(android.R.drawable.ic_menu_upload);
               builder.setCancelable(false);
               builder.setMessage("Do you want to upload items created or updated in this app?").setPositiveButton("Yes", dialogOnClickListener)
                       .setNegativeButton("No", dialogOnClickListener).show();
        }

        });

        btSyncUpPurchasesSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogOnClickListener = new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Boolean exito = false;
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:

                                UpdateSyncAuditUpSync(3, "Purchases");
                                UpdateSyncAuditUpSync(4, "Sales");
                                if(HayPurchases == false){
                                   exito =  LoopThroughPurchases();
                                   if(exito == true){
                                       Toast.makeText(getActivity(), "Purchases have been uploaded to the server!", Toast.LENGTH_LONG).show();
                                   }else{
                                       Toast.makeText(getActivity(), "No Purchases have been uploaded to the server!", Toast.LENGTH_LONG).show();
                                   }
                                    HayPurchases = true;
                                }else{
                                    Toast.makeText(getActivity(), "You have already uploaded purchases to the server", Toast.LENGTH_LONG).show();
                                }

                                if(HaySales==false){
                                   exito = LoopThroughSales();
                                   if(exito == true){
                                       Toast.makeText(getActivity(), "Sales have been uploaded to the server!", Toast.LENGTH_LONG).show();
                                   }else{
                                       Toast.makeText(getActivity(), "No Sales have been uploaded to the server!", Toast.LENGTH_LONG).show();
                                   }
                                    HaySales=true;
                                }else{
                                    Toast.makeText(getActivity(), "You have already uploaded sales to the server", Toast.LENGTH_LONG).show();
                                }

                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmation");
                builder.setIcon(android.R.drawable.ic_menu_upload);
                builder.setCancelable(false);
                builder.setMessage("Do you want to upload purchases and sales recorded in this app?").setPositiveButton("Yes", dialogOnClickListener)
                        .setNegativeButton("No", dialogOnClickListener).show();
            }
        });

        btUpdate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogOnClickListener = new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                retrievedIP = tip.getText().toString();
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("ip", retrievedIP);
                                editor.commit();
                                Toast.makeText(getActivity(), "IP has been saved!", Toast.LENGTH_LONG).show();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmation");
                builder.setIcon(android.R.drawable.ic_menu_save);
                builder.setCancelable(false);
                builder.setMessage("Do you want to update the server's ip?").setPositiveButton("Yes", dialogOnClickListener)
                        .setNegativeButton("No", dialogOnClickListener).show();
            }
        });

        btUpdateSalesPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogOnClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                retrievedSalesPerson = spinnerSalesPersons.getSelectedItem().toString();
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("salesperson", retrievedSalesPerson);
                                editor.commit();
                                Toast.makeText(getActivity(), "Salesperson has been saved!", Toast.LENGTH_LONG).show();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmation");
                builder.setIcon(android.R.drawable.ic_menu_save);
                builder.setCancelable(false);
                builder.setMessage("Do you want to update the Salesperson?").setPositiveButton("Yes", dialogOnClickListener)
                        .setNegativeButton("No", dialogOnClickListener).show();
            }
        });
        return view;
    }


    private void BajarSalesPersons(){
        new Thread(new Runnable() {

            @Override
            public void run() {
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        // Instantiate the RequestQueue.
                        RequestQueue mQueue = Volley.newRequestQueue(getActivity());
                        String url = "http://" + retrievedIP + "/api/SalesPersons";

                        // Request a string response from the provided URL.
                        JsonRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                            @Override
                            public void onResponse(JSONArray response) {
                                Integer id = 0;
                                String name = "";

                                JSONObject item = null;
                                try {
                                    a = 0;
                                    totalArrayLength = 0;
                                    for (int i = 0; i < response.length(); i++)
                                    {
                                        totalArrayLength = i;
                                    }
                                    a = totalArrayLength + 1;

                                    for (int i = 0; i < response.length(); i++) {

                                        item = response.getJSONObject(i);
                                        id = item.getInt("id");
                                        name = item.getString("Name");

                                        SalesPersonsModel salesPersonsModel = new SalesPersonsModel(id, name);

                                        DatabaseHelper dataBasehelper = new DatabaseHelper(getContext());
                                        boolean success = dataBasehelper.addSalesPersons(salesPersonsModel);
                                        int percentage = ((i + 1) * 100)/a;
                                    //    Toast.makeText(getActivity(), "Downloading salespersons: " +  percentage + "%", Toast.LENGTH_SHORT).show();

                                    }
                                } catch (JSONException e) {
                                    //  btSyncDownClients.setEnabled(false);
                                    //   Toast.makeText(getActivity(), "There was an error while downloading customers, please try again!", Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }

                              //  Toast.makeText(getActivity(), "Salespersons were downloaded successfully!", Toast.LENGTH_LONG).show();
                                btSyncDownClients.setEnabled(false);
                            }
                        }, new Response.ErrorListener() {


                            public void onErrorResponse(VolleyError error) {
                              //  Toast.makeText(getActivity(), error.toString() + "_onResponse", Toast.LENGTH_LONG).show();
                            }
                        });

                        //Add the request to the RequestQ Queue
                        mQueue.add(request);
                        //    Toast.makeText(getActivity(), "You clicked me 1", Toast.LENGTH_SHORT).show();

                    }

                });
            }
        }).start();
    }

    private void BajarConfig(){
        new Thread(new Runnable() {

            @Override
            public void run() {
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        // Instantiate the RequestQueue.
                        RequestQueue mQueue = Volley.newRequestQueue(getActivity());
                        String url = "http://" + retrievedIP + "/api/Config";

                        // Request a string response from the provided URL.
                        JsonRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                            @Override
                            public void onResponse(JSONArray response) {
                                Integer id = 0;
                                String phoneCode = "";

                                JSONObject item = null;
                                try {

                                    for (int i = 0; i < response.length(); i++) {

                                        item = response.getJSONObject(i);
                                        id = item.getInt("id");
                                        phoneCode = item.getString("CountryCode");

                                        retrievedPhoneCode = phoneCode;
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString("phonecode", retrievedPhoneCode);
                                        editor.commit();

                                    }
                                } catch (JSONException e) {
                                    //  btSyncDownClients.setEnabled(false);
                                    //   Toast.makeText(getActivity(), "There was an error while downloading customers, please try again!", Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }

                                //  Toast.makeText(getActivity(), "Salespersons were downloaded successfully!", Toast.LENGTH_LONG).show();
                                btSyncDownClients.setEnabled(false);
                            }
                        }, new Response.ErrorListener() {


                            public void onErrorResponse(VolleyError error) {
                                //  Toast.makeText(getActivity(), error.toString() + "_onResponse", Toast.LENGTH_LONG).show();
                            }
                        });

                        //Add the request to the RequestQ Queue
                        mQueue.add(request);
                        //    Toast.makeText(getActivity(), "You clicked me 1", Toast.LENGTH_SHORT).show();

                    }

                });
            }
        }).start();
    }

    private void BajarClientes(){
    new Thread(new Runnable() {

        @Override
        public void run() {
            h.post(new Runnable() {
                @Override
                public void run() {
                    // Instantiate the RequestQueue.
                    RequestQueue mQueue = Volley.newRequestQueue(getActivity());
                    String url = "http://" + retrievedIP + "/api/Map";

                    // Request a string response from the provided URL.
                    JsonRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                        @Override
                        public void onResponse(JSONArray response) {
                            Integer id = 0;
                            Boolean Supplier = true;
                            String Contact = "";
                            String Address = "";
                            String City = "";
                            String Telephone = "";
                            String Email = "Email";
                            String Comment = "";
                            Double Latitude = 0.00;
                            Double Longitude = 0.00;
                            String CreateDate = "";
                            String UpdateDate = "";
                            Boolean CreatedInApp = true;
                            Object Image = "";

                            JSONObject item = null;
                            try {
                                a = 0;
                                totalArrayLength = 0;
                                for (int i = 0; i < response.length(); i++)
                                {
                                    totalArrayLength = i;
                                }
                                a = totalArrayLength + 1;

                                for (int i = 0; i < response.length(); i++) {

                                    item = response.getJSONObject(i);
                                    id = item.getInt("id");
                                    Contact = item.getString("Contacto");
                                    Supplier = item.getBoolean("Supplier");
                                    Address = item.getString("Address");
                                    City = item.getString("City");
                                    Telephone = item.getString("telefono");
                                    Email = item.getString("email");
                                    //Aqui iria la variable del campo valor que no uso pero devuelve la API
                                    Comment = item.getString("Comment");
                                    Latitude = item.getDouble("Latitud");
                                    Longitude = item.getDouble("Longitud");
                                    CreateDate = item.getString("FechaCreacion");
                                    UpdateDate = item.getString("FechaEdicion");
                                    CreatedInApp = item.getBoolean("CreatedInApp");
                                    Image = item.get("Image");

                                    byte [] imageByte = null;

                                    if(!Image.equals(null))
                                    {
                                        imageByte = Base64.decode(String.valueOf(Image), Base64.NO_WRAP);

                                        //   ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                                        //  ObjectOutputStream oos = new ObjectOutputStream(byteArray);
                                        //   oos.writeObject(Image);
                                        //   oos.flush();
                                        //   imageByte = byteArray.toByteArray();

                                    }else{
                                        imageByte = null;
                                    }

                                    CustomerModel customerModel = new CustomerModel(id, Contact,Supplier, Address,City,  //en lugar de id se puede colocar -1 si el campo id de android es autoincremento
                                            Telephone,Email,Comment, Latitude, Longitude, CreateDate, UpdateDate, CreatedInApp, false, imageByte);

                                    DatabaseHelper dataBasehelper = new DatabaseHelper(getContext());
                                    boolean success = dataBasehelper.addOneClient(customerModel);
                                    int percentage = ((i + 1) * 100)/a;
                                Toast.makeText(getActivity(), "Downloading customers and suppliers: " +  percentage + "%", Toast.LENGTH_SHORT).show();
                                    btSyncDownClients.setEnabled(false);
                                }
                            } catch (JSONException e) {
                              //  btSyncDownClients.setEnabled(false);
                             //   Toast.makeText(getActivity(), "There was an error while downloading customers, please try again!", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }

                         //   Toast.makeText(getActivity(), "Customers and suppliers were downloaded successfully!", Toast.LENGTH_LONG).show();

                        }
                    }, new Response.ErrorListener() {


                        public void onErrorResponse(VolleyError error) {
                         //   Toast.makeText(getActivity(), error.toString() + "_onResponse", Toast.LENGTH_LONG).show();
                        }
                    });

                    //Add the request to the RequestQ Queue
                    mQueue.add(request);
                    //    Toast.makeText(getActivity(), "You clicked me 1", Toast.LENGTH_SHORT).show();

                }

            });
        }
    }).start();
}

    private void BajarItems(){
        new Thread(new Runnable() {

            @Override
            public void run() {
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        // Instantiate the RequestQueue.
                        RequestQueue mQueue = Volley.newRequestQueue(getActivity());
                        String url = "http://" + retrievedIP + "/api/CatMap?level=" + levels;

                        // Request a string response from the provided URL.
                        JsonRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                            @Override
                            public void onResponse(JSONArray response) {
                                Integer id = 0;
                                String Cod = "";
                                String Category = "";
                                Integer Nivel = 0;
                                String CreationDate = "";
                                String UpdateDate = "";
                                Object Image = "";

                                JSONObject item = null;
                                try {
                                    a = 0;
                                    totalArrayLength = 0;
                                    for (int i = 0; i < response.length(); i++)
                                    {
                                        totalArrayLength = i;
                                    }
                                    a = totalArrayLength + 1;

                                    for (int i = 0; i < response.length(); i++) {

                                        item = response.getJSONObject(i);
                                        id = item.getInt("id");
                                        Cod = item.getString("Cod");
                                        Category = item.getString("Category");
                                        Nivel = item.getInt("Nivel");
                                        CreationDate = item.getString("FechaCreacion");
                                        UpdateDate = item.getString("FechaEdicion");
                                        Image = item.get("Image");

                                        byte [] imageByte = null;

                                        if(!Image.equals(null))
                                        {
                                            imageByte = Base64.decode(String.valueOf(Image), Base64.NO_WRAP);

                                         //   ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                                          //  ObjectOutputStream oos = new ObjectOutputStream(byteArray);
                                         //   oos.writeObject(Image);
                                         //   oos.flush();
                                         //   imageByte = byteArray.toByteArray();

                                        }else{
                                            imageByte = null;
                                        }

                                        if(Nivel == levels) { //No quiero todos los niveles solo el final y el anterior al final
                                            ItemsModel itemsModel = new ItemsModel(id, Cod, Category, Nivel, CreationDate, UpdateDate, imageByte, false, false, false);

                                            DatabaseHelper dataBasehelper = new DatabaseHelper(getContext());
                                            boolean success = dataBasehelper.addOneItem(itemsModel);
                                            int percentage = ((i+1) * 100) / a;
                                           Toast.makeText(getActivity(), "Downloading items: " + percentage + "%", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } catch (JSONException e) {
                                  //  btSyncDownItems.setEnabled(true);
                                  //  Toast.makeText(getActivity(), "There was an error while downloading items, please try again!", Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }

                              //  Toast.makeText(getActivity(), "Items were downloaded successfully!", Toast.LENGTH_LONG).show();
                                btSyncDownItems.setEnabled(false);
                            }
                        }, new Response.ErrorListener() {

                            public void onErrorResponse(VolleyError error) {
                             //   Toast.makeText(getActivity(), error.toString() + "_onResponse", Toast.LENGTH_LONG).show();
                            }
                        });

                        //Add the request to the RequestQ Queue
                        mQueue.add(request);
                        //    Toast.makeText(getActivity(), "You clicked me 1", Toast.LENGTH_SHORT).show();

                    }

                });
            }
        }).start();

    }

    private void BajarBuySell(){
        new Thread(new Runnable() {

            @Override
            public void run() {
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        // Instantiate the RequestQueue.
                        RequestQueue mQueue = Volley.newRequestQueue(getActivity());
                        String url = "http://" + retrievedIP + "/api/BuySell";

                        // Request a string response from the provided URL.
                        JsonRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                            @Override
                            public void onResponse(JSONArray response) {
                                Integer id = 0;
                                Integer ItemId = 0;
                                String Cod = "";
                                String PurchaseDate = "";
                                String SaleDate = "";
                                Double PurchasePrice = 0.0;
                                Integer PurchaseQuantity = 0;
                                Double SalePrice = 0.0;
                                Integer SaleQuantity = 0;
                                Integer AvailableQuantity = 0;
                                String Clase = "";
                                String Categoria = "";
                                String Subcategoria = "";
                                String Tipo = "";
                                String SubTipo = "";
                                Double Profit = 0.0;

                                JSONObject item = null;
                                try {
                                    a = 0;
                                    totalArrayLength = 0;
                                    for (int i = 0; i < response.length(); i++)
                                    {
                                        totalArrayLength = i;
                                    }
                                    a = totalArrayLength + 1;

                                    for (int i = 0; i < response.length(); i++) {

                                        item = response.getJSONObject(i);
                                        id = item.getInt("id");
                                        ItemId = item.getInt("ItemId");
                                        Cod = item.getString("Cod");
                                        PurchaseDate = item.getString("PurchaseDate");
                                        SaleDate = item.getString("SaleDate");
                                        PurchasePrice = item.getDouble("PurchasePrice");
                                        PurchaseQuantity = item.getInt("PurchaseQuantity");
                                        SalePrice = item.getDouble("SalePrice");
                                        SaleQuantity = item.getInt("SaleQuantity");
                                        AvailableQuantity = item.getInt("AvailableQuantity");
                                        Clase = item.getString("Clase");
                                        Categoria = item.getString("Categoria");
                                        Subcategoria = item.getString("Subcategoria");
                                        Tipo = item.getString("Tipo");
                                        SubTipo = item.getString("SubTipo");
                                        Profit = item.getDouble("Profit");

                                        BuySellModel buysellModel = new BuySellModel(id, ItemId, Cod, PurchaseDate, SaleDate, PurchasePrice, PurchaseQuantity, SalePrice, SaleQuantity, AvailableQuantity, Clase, Categoria, Subcategoria, Tipo, SubTipo, Profit);

                                            DatabaseHelper dataBasehelper = new DatabaseHelper(getContext());
                                            boolean success = dataBasehelper.addOneBuySell(buysellModel);
                                            int percentage = ((i+1) * 100) / a;
                                          //  Toast.makeText(getActivity(), "Downloading item's info: " + percentage + "%", Toast.LENGTH_SHORT).show();
                                       
                                    }
                                } catch (JSONException e) {
                                    //  btSyncDownItems.setEnabled(true);
                                    //  Toast.makeText(getActivity(), "There was an error while downloading items, please try again!", Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }

                                Toast.makeText(getActivity(), "Items were downloaded successfully!", Toast.LENGTH_LONG).show();
                                btSyncDownItems.setEnabled(false);
                            }
                        }, new Response.ErrorListener() {

                            public void onErrorResponse(VolleyError error) {
                             //   Toast.makeText(getActivity(), error.toString() + "_onResponse", Toast.LENGTH_LONG).show();
                            }
                        });

                        //Add the request to the RequestQ Queue
                        mQueue.add(request);
                        //    Toast.makeText(getActivity(), "You clicked me 1", Toast.LENGTH_SHORT).show();

                    }

                });
            }
        }).start();

    }

    private void BajarPriceLists(){
        new Thread(new Runnable() {

            @Override
            public void run() {
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        // Instantiate the RequestQueue.
                        RequestQueue mQueue = Volley.newRequestQueue(getActivity());
                        String url = "http://" + retrievedIP + "/api/PriceLists";

                        // Request a string response from the provided URL.
                        JsonRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                            @Override
                            public void onResponse(JSONArray response) {
                                Integer id = 0;
                                String Category = "";
                                String Tipo = "";

                                JSONObject item = null;
                                try {
                                    a = 0;
                                    totalArrayLength = 0;
                                    for (int i = 0; i < response.length(); i++)
                                    {
                                        totalArrayLength = i;
                                    }
                                    a = totalArrayLength + 1;

                                    for (int i = 0; i < response.length(); i++) {
                                        item = response.getJSONObject(i);
                                        id = item.getInt("id");
                                        Category = item.getString("Category");
                                        Tipo = item.getString("Tipo");

                                            PriceListsModel priceListsModel = new PriceListsModel(id, Category, Tipo);

                                            DatabaseHelper dataBasehelper = new DatabaseHelper(getContext());
                                            boolean success = dataBasehelper.addOnePriceList(priceListsModel);
                                            int percentage = ((i + 1) * 100) / a;
                                          //  Toast.makeText(getActivity(), "Downloading Price lists: " + percentage + "%", Toast.LENGTH_SHORT).show();
                                                                            }
                                } catch (JSONException e) {
                                  //  Toast.makeText(getActivity(), "There was an error while downloading price lists, please try again!", Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }

                                Toast.makeText(getActivity(), "Price lists were downloaded successfully!", Toast.LENGTH_LONG).show();
                            }
                        }, new Response.ErrorListener() {

                            public void onErrorResponse(VolleyError error) {
                             //   Toast.makeText(getActivity(), error.toString() + "_onResponse", Toast.LENGTH_LONG).show();
                            }
                        });

                        //Add the request to the RequestQ Queue
                        mQueue.add(request);
                        //    Toast.makeText(getActivity(), "You clicked me 1", Toast.LENGTH_SHORT).show();

                    }

                });
            }
        }).start();

    }

    private void BajarPrices(){
        new Thread(new Runnable() {

            @Override
            public void run() {
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        // Instantiate the RequestQueue.
                        RequestQueue mQueue = Volley.newRequestQueue(getActivity());
                        String url = "http://" + retrievedIP + "/api/Prices";

                        // Request a string response from the provided URL.
                        JsonRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                            @Override
                            public void onResponse(JSONArray response) {
                                Integer id = 0;
                                Integer PriceListId = 0;
                                Integer ItemId = 0;
                                Double Price = 0.0;

                                JSONObject item = null;
                                try {
                                    a = 0;
                                    totalArrayLength = 0;
                                    for (int i = 0; i < response.length(); i++)
                                    {
                                        totalArrayLength = i;
                                    }
                                    a = totalArrayLength + 1;

                                    for (int i = 0; i < response.length(); i++) {
                                        item = response.getJSONObject(i);
                                        id = item.getInt("id");
                                        PriceListId = item.getInt("PriceListId");
                                        ItemId = item.getInt("ItemId");
                                        Price = item.getDouble("Price");

                                        PricesModel pricesModel = new PricesModel(id, PriceListId, ItemId, Price);

                                        DatabaseHelper dataBasehelper = new DatabaseHelper(getContext());
                                        boolean success = dataBasehelper.addOnePrice(pricesModel);
                                        int percentage = ((i + 1) * 100) / a;
                                      //  Toast.makeText(getActivity(), "Downloading Prices = " + percentage + "%", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                   // Toast.makeText(getActivity(), "There was an error while downloading prices, please try again!", Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }

                                Toast.makeText(getActivity(), "Prices were downloaded successfully!", Toast.LENGTH_LONG).show();
                            }
                        }, new Response.ErrorListener() {

                            public void onErrorResponse(VolleyError error) {
                             //   Toast.makeText(getActivity(), error.toString() + "_onResponse", Toast.LENGTH_LONG).show();
                            }
                        });

                        //Add the request to the RequestQ Queue
                        mQueue.add(request);
                        //    Toast.makeText(getActivity(), "You clicked me 1", Toast.LENGTH_SHORT).show();

                    }

                });
            }
        }).start();

    }
    public Boolean LoopThroughItems(){
        Boolean exito = false;
        db = new DatabaseHelper(getContext());
        String idItemsCreadosEnApp, idItemsExistentes, itemNamesCreadosEnApp, imagen;
        byte[] image;

        //Lo de abajo es para syncronizar items, creados en app, con el servidor para que las imagenes se suban
         if(HayItems ==  false){
             List<String> ItemsCreadosEnApp = db.getOnlyItemsConImagenes("1", 0);
             List<String> ItemNamesCreadosEnApp = db.getOnlyItemsConImagenes("1", 1);
             for (int i=0; i<ItemsCreadosEnApp.size(); i++) {
                 idItemsCreadosEnApp = ItemsCreadosEnApp.get(i);
                 itemNamesCreadosEnApp = ItemNamesCreadosEnApp.get(i);
                 //   image = db.getImageBytes(Integer.parseInt(id));
                 PostItemsUpSync(Integer.parseInt(idItemsCreadosEnApp));
                 Toast.makeText(getContext(), "Item " + itemNamesCreadosEnApp + " was uploaded to the server", Toast.LENGTH_SHORT).show();
                 exito = true;
             }
             HayItems = true;
        }else{
            Toast.makeText(getContext(), "You have already uploaded your new items to the server!", Toast.LENGTH_LONG).show();
        }

        List<String> ItemsConImagenes = db.getOnlyItemsConImagenes("0", 0);
        List<String> ItemNamesCreadosEnApp = db.getOnlyItemsConImagenes("0", 1);
            //Lo de abajo es para syncronizar items, existentes, con el servidor para que las imagenes se suban
        for (int i=0; i<ItemsConImagenes.size(); i++) {
            idItemsExistentes = ItemsConImagenes.get(i);
            itemNamesCreadosEnApp = ItemNamesCreadosEnApp.get(i);
         //   image = db.getImageBytes(Integer.parseInt(id));

            UpdateItemUpSync(Integer.parseInt(idItemsExistentes));
            Toast.makeText(getContext(), "Item " + itemNamesCreadosEnApp + " was Synchronized to the server", Toast.LENGTH_SHORT).show();
            exito = true;
        }
        return exito;
    }

    public void PostItemsUpSync(Integer Id){
        new Thread(new Runnable() {

            @Override
            public void run() {
                h.post(new Runnable() {

                    @Override
                    public void run() {

                        final String data = db.getItemstoUpSync(Id);

                        // Instantiate the RequestQueue.
                        RequestQueue mQueue = Volley.newRequestQueue(getContext());
                        String url = "http://" + retrievedIP + "/api/CatMapSync";
                        // Request a string response from the provided URL.
                        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                try{
                                    JSONObject jsonObject = new JSONObject(response);
                                    Toast.makeText(getContext(), jsonObject.toString(), Toast.LENGTH_SHORT).show();
                                }catch(JSONException e){
                                  //  btSyncUpItems.setEnabled(true);
                                  //  btSyncUpClients.setEnabled(false);
                                  //  Toast.makeText(getActivity(), "There was an error while uploading items, please try again!", Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            }

                        }, new Response.ErrorListener() {

                            public void onErrorResponse(VolleyError error) {
                              Toast.makeText(getActivity(), "Item " + Id.toString() + " has not been uploaded to the server!", Toast.LENGTH_SHORT).show();
                            }
                        }) {

                            @Override
                            public String getBodyContentType(){ return "application/json; charset=utf-8"; }

                            @Override
                            public byte[] getBody() throws AuthFailureError {
                                try{
                                    return data == null ? null : data.getBytes ("utf-8");

                                }catch(UnsupportedEncodingException uee){
                                    return null;
                                }
                            }
                        };

                        //Add the request to the RequestQ Queue
                        mQueue.add(request);

                    }

                });
            }
        }).start();

    }
    private void UpdateItemUpSync(Integer Id){
        new Thread(new Runnable() {

            @Override
            public void run() {
                h.post(new Runnable() {

                    @Override
                    public void run() {

                        final String data = db.getItemstoUpSync(Id);

                        // Instantiate the RequestQueue.
                        RequestQueue mQueue = Volley.newRequestQueue(getActivity());
                        String url = "http://" + retrievedIP + "/api/CatMap/" + Id;
                             // Request a string response from the provided URL.
                        StringRequest request = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                            try{
                                JSONObject jsonObject = new JSONObject(response);
                                Toast.makeText(getContext(), jsonObject.toString(), Toast.LENGTH_SHORT).show();

                            }catch(JSONException e){
                              //  btSyncUpItems.setEnabled(true);
                              //  btSyncUpClients.setEnabled(false);
                              //  Toast.makeText(getActivity(), "There was an error while updating items, please try again!", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                            }

                        }, new Response.ErrorListener() {

                            public void onErrorResponse(VolleyError error) {
                               Toast.makeText(getActivity(), "Item " + Id.toString() + " was not sychronized successfuly", Toast.LENGTH_SHORT).show();
                            }
                        }) {

                            @Override
                            public String getBodyContentType(){ return "application/json; charset=utf-8"; }

                            @Override
                            public byte[] getBody() throws AuthFailureError {
                                try{
                                    return data == null ? null : data.getBytes ("utf-8");

                                }catch(UnsupportedEncodingException uee){
                                    return null;
                                }
                            }

                        };

                         //Add the request to the RequestQ Queue
                        mQueue.add(request);
                        //    Toast.makeText(getActivity(), "You clicked me 1", Toast.LENGTH_SHORT).show();

                    }

                });
            }
        }).start();

    }

    private void UpdateSyncAuditUpSync(Integer Id, String name){
        new Thread(new Runnable() {

            @Override
            public void run() {
                h.post(new Runnable() {

                    @Override
                    public void run() {

                        final String data = db.getSyncAudittoUpSync(Id, name);

                        // Instantiate the RequestQueue.
                        RequestQueue mQueue = Volley.newRequestQueue(getActivity());
                        String url = "http://" + retrievedIP + "/api/SyncAudit/" + Id;
                        // Request a string response from the provided URL.
                        StringRequest request = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                try{
                                    JSONObject jsonObject = new JSONObject(response);
                                    Toast.makeText(getContext(), jsonObject.toString(), Toast.LENGTH_SHORT).show();

                                }catch(JSONException e){
                                    //  btSyncUpItems.setEnabled(true);
                                    //  btSyncUpClients.setEnabled(false);
                                    //  Toast.makeText(getActivity(), "There was an error while updating items, please try again!", Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            }

                        }, new Response.ErrorListener() {

                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getActivity(), "Item " + Id.toString() + " was not sychronized successfuly", Toast.LENGTH_SHORT).show();
                            }
                        }) {

                            @Override
                            public String getBodyContentType(){ return "application/json; charset=utf-8"; }

                            @Override
                            public byte[] getBody() throws AuthFailureError {
                                try{
                                    return data == null ? null : data.getBytes ("utf-8");

                                }catch(UnsupportedEncodingException uee){
                                    return null;
                                }
                            }

                        };

                        //Add the request to the RequestQ Queue
                        mQueue.add(request);
                        //    Toast.makeText(getActivity(), "You clicked me 1", Toast.LENGTH_SHORT).show();

                    }

                });
            }
        }).start();

    }

    public Boolean LoopThroughClientes(){
        Boolean exito = false;
        db = new DatabaseHelper(getContext());
        String id, clienteNames;
        List<String> clientesCreadosEnApp = db.getOnlyClientsAndSuppliersCreatedInApp(0);
        List<String> clienteNamesCreadosEnApp = db.getOnlyClientsAndSuppliersCreatedInApp(0);
        for (int i=0; i<clientesCreadosEnApp.size(); i++) {
            id = clientesCreadosEnApp.get(i);
            clienteNames = clienteNamesCreadosEnApp.get(i);
            PostClientesUpSync(Integer.parseInt(id));
            Toast.makeText(getContext(), "Customer/Supplier " + clienteNames + " was uploaded to the server", Toast.LENGTH_SHORT).show();
            exito = true;
        }
        return exito;
    }

    public void PostClientesUpSync(Integer Id){
        new Thread(new Runnable() {

            @Override
            public void run() {
                h.post(new Runnable() {

                    @Override
                    public void run() {

                        final String data = db.getClientstoUpSync(Id);

                        // Instantiate the RequestQueue.
                        RequestQueue mQueue = Volley.newRequestQueue(getContext());
                        String url = "http://" + retrievedIP + "/api/MapSync";
                        // Request a string response from the provided URL.
                        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                try{
                                    JSONObject jsonObject = new JSONObject(response);
                                    Toast.makeText(getContext(), jsonObject.toString(), Toast.LENGTH_SHORT).show();
                                }catch(JSONException e){
                                  //  btSyncUpClients.setEnabled(true);
                                  //  btSyncUpPurchasesSales.setEnabled(false);
                                  //  Toast.makeText(getActivity(), "There was an error while uploading suppliers and customers, please try again!", Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            }

                        }, new Response.ErrorListener() {

                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getActivity(), "Customer/Supplier " + Id.toString() + " has not been uploaded to the server!", Toast.LENGTH_SHORT).show();
                            }
                        }) {

                            @Override
                            public String getBodyContentType(){ return "application/json; charset=utf-8"; }

                            @Override
                            public byte[] getBody() throws AuthFailureError {
                                try{
                                    return data == null ? null : data.getBytes ("utf-8");

                                }catch(UnsupportedEncodingException uee){
                                    return null;
                                }
                            }
                        };

                        //Add the request to the RequestQ Queue
                        mQueue.add(request);

                    }

                });
            }
        }).start();

    }

    public Boolean LoopThroughPurchases(){
        Boolean exito = false;
        db = new DatabaseHelper(getContext());
        String id;
        List<String> purchasesIds = db.getPurchasedIds(0, "");
        for (int i=0; i<purchasesIds.size(); i++) {
            id = purchasesIds.get(i);
            //   image = db.getImageBytes(Integer.parseInt(id));

            PostPurchasesUpSync(Integer.parseInt(id));
            exito = true;
            Toast.makeText(getContext(), "Purchase id " + id + " was uploaded to the server", Toast.LENGTH_SHORT).show();
        }
        return exito;
    }

    public void PostPurchasesUpSync(Integer Id){
        new Thread(new Runnable() {

            @Override
            public void run() {
                h.post(new Runnable() {

                    @Override
                    public void run() {

                        final String data = db.getPurchasestoUpSync(Id);

                        // Instantiate the RequestQueue.
                        RequestQueue mQueue = Volley.newRequestQueue(getContext());
                        String url = "http://" + retrievedIP + "/api/PurchasedItemsSync";
                        // Request a string response from the provided URL.
                        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                try{
                                    JSONObject jsonObject = new JSONObject(response);
                                    Toast.makeText(getContext(), jsonObject.toString(), Toast.LENGTH_LONG).show();
                                }catch(JSONException e){
                                   // btSyncUpPurchasesSales.setEnabled(true);
                                   // Toast.makeText(getActivity(), "There was an error while uploadfing purchases, please try again!", Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            }

                        }, new Response.ErrorListener() {

                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getActivity(), "Purchase " + Id.toString() + " has not been uploaded to the server!", Toast.LENGTH_SHORT).show();
                            }
                        }) {

                            @Override
                            public String getBodyContentType(){ return "application/json; charset=utf-8"; }

                            @Override
                            public byte[] getBody() throws AuthFailureError {
                                try{
                                    return data == null ? null : data.getBytes ("utf-8");

                                }catch(UnsupportedEncodingException uee){
                                    return null;
                                }
                            }
                        };

                        //Add the request to the RequestQ Queue
                        mQueue.add(request);

                    }

                });
            }
        }).start();

    }

    public Boolean LoopThroughSales(){
        Boolean exito = false;
        db = new DatabaseHelper(getContext());
        String id;
        List<String> soldIds = db.getSoldIds(0, "");
        for (int i=0; i<soldIds.size(); i++) {
            id = soldIds.get(i);
            //   image = db.getImageBytes(Integer.parseInt(id));

            SharedPreferences spRetrieve = getContext().getApplicationContext().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
            retrievedSalesPerson = spRetrieve.getString("salesperson", "1");
            int salesPersonId;

            if(retrievedSalesPerson == "1"){
                salesPersonId = 1;
            }else{
                salesPersonId = db.getSalesPersonId(retrievedSalesPerson);
            }

            PostSoldUpSync(Integer.parseInt(id), salesPersonId);
            exito = true;
            Toast.makeText(getContext(), "Sale " + id + " was uploaded to the server", Toast.LENGTH_SHORT).show();
        }
        return exito;
    }

    public void PostSoldUpSync(Integer Id, int salesPersonId){
        new Thread(new Runnable() {

            @Override
            public void run() {
                h.post(new Runnable() {

                    @Override
                    public void run() {

                        final String data = db.getSoldtoUpSync(Id, salesPersonId);

                        // Instantiate the RequestQueue.
                        RequestQueue mQueue = Volley.newRequestQueue(getContext());
                        String url = "http://" + retrievedIP + "/api/SoldItemsSync";
                        // Request a string response from the provided URL.
                        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                try{
                                    JSONObject jsonObject = new JSONObject(response);
                                    Toast.makeText(getContext(), jsonObject.toString(), Toast.LENGTH_LONG).show();
                                }catch(JSONException e){
                                  //  btSyncUpPurchasesSales.setEnabled(true);
                                  //  Toast.makeText(getActivity(), "There was an error while uploading sales, please try again!", Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            }

                        }, new Response.ErrorListener() {

                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getActivity(), "Sale " + Id.toString() + " has not been uploaded to the server!", Toast.LENGTH_SHORT).show();
                            }
                        }) {

                            @Override
                            public String getBodyContentType(){ return "application/json; charset=utf-8"; }

                            @Override
                            public byte[] getBody() throws AuthFailureError {
                                try{
                                    return data == null ? null : data.getBytes ("utf-8");

                                }catch(UnsupportedEncodingException uee){
                                    return null;
                                }
                            }
                        };

                        //Add the request to the RequestQ Queue
                        mQueue.add(request);

                    }

                });
            }
        }).start();

    }

    private void VerificaSiMapSyncContainsValues(){

                        // Instantiate the RequestQueue.
                        RequestQueue mQueue = Volley.newRequestQueue(getActivity());
                        String url = "http://" + retrievedIP + "/api/MapSync";

                        // Request a string response from the provided URL.
                        JsonRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                            @Override
                            public void onResponse(JSONArray response) {
                                Boolean Sincronizado = true;

                                JSONObject item = null;
                                try {

                                    for (int i = 0; i < response.length(); i++) {

                                        item = response.getJSONObject(i);
                                        Sincronizado=item.getBoolean("Sincronizado");
                                            if(Sincronizado==false){
                                                HayClientes = true;
                                                Toast.makeText(getContext(), "Either suppliers or clients still need to be synchronized in the server!", Toast.LENGTH_SHORT).show();
                                                break;
                                            }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {


                            public void onErrorResponse(VolleyError error) {
                              //  Toast.makeText(getActivity(), error.toString() + "_onResponse", Toast.LENGTH_LONG).show();
                            }
                        });

                        //Add the request to the RequestQ Queue
                        mQueue.add(request);
                        //    Toast.makeText(getActivity(), "You clicked me 1", Toast.LENGTH_SHORT).show();

      }

    private void VerificaSiPurchasedItemsSyncContainsValues(){

                        // Instantiate the RequestQueue.
                        RequestQueue mQueue = Volley.newRequestQueue(getActivity());
                        String url = "http://"+ retrievedIP + "/api/PurchasedItemsSync";

                        // Request a string response from the provided URL.
                        JsonRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                            @Override
                            public void onResponse(JSONArray response) {

                                Boolean Sincronizado = true;

                                JSONObject item = null;
                                try {

                                    for (int i = 0; i < response.length(); i++) {
                                        item = response.getJSONObject(i);
                                       Sincronizado = item.getBoolean("Sincronizado");
                                         if(Sincronizado==false){
                                             HayPurchases = true;
                                             Toast.makeText(getContext(), "Purchases still need to be synchronized in the server!", Toast.LENGTH_SHORT).show();
                                             break;
                                         }
                                    }
                                } catch (JSONException e) {
                                  //  HayPurchases = false;
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {

                            public void onErrorResponse(VolleyError error) {
                             //   Toast.makeText(getActivity(), error.toString() + "_onResponse", Toast.LENGTH_LONG).show();
                            }
                        });

                        //Add the request to the RequestQ Queue
                        mQueue.add(request);
                        //    Toast.makeText(getActivity(), "You clicked me 1", Toast.LENGTH_SHORT).show();

    }


    private void VerificaSiSoldItemsSyncContainsValues(){

                        // Instantiate the RequestQueue.
                        RequestQueue mQueue = Volley.newRequestQueue(getActivity());
                        String url = "http://" + retrievedIP + "/api/SoldItemsSync";

                        // Request a string response from the provided URL.
                        JsonRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                            @Override
                            public void onResponse(JSONArray response) {

                                Boolean Sincronizado = true;

                                JSONObject item = null;
                                try {

                                    for (int i = 0; i < response.length(); i++) {
                                        item = response.getJSONObject(i);
                                          Sincronizado = item.getBoolean("Sincronizado");
                                        if(Sincronizado==false){
                                            HaySales = true;
                                            Toast.makeText(getContext(), "Sales still need to be synchronized in the server!", Toast.LENGTH_SHORT).show();
                                            break;
                                        }

                                    }
                                } catch (JSONException e) {
                                    HaySales = false;
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {

                            public void onErrorResponse(VolleyError error) {
                             //   Toast.makeText(getActivity(), error.toString() + "_onResponse", Toast.LENGTH_LONG).show();
                            }
                        });

                        //Add the request to the RequestQ Queue
                        mQueue.add(request);
                        //    Toast.makeText(getActivity(), "You clicked me 1", Toast.LENGTH_SHORT).show();

        }

    private void VerificaSiCatMapSyncContainsValues(){

        // Instantiate the RequestQueue.
        RequestQueue mQueue = Volley.newRequestQueue(getActivity());
        String url = "http://" + retrievedIP + "/api/CatMapSync";

        // Request a string response from the provided URL.
        JsonRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                Boolean Sincronizado = true;

                JSONObject item = null;
                try {

                    for (int i = 0; i < response.length(); i++) {
                        item = response.getJSONObject(i);

                        Sincronizado = item.getBoolean("Sincronizado");
                        if(Sincronizado==false) {
                            HayItems = true;
                            Toast.makeText(getContext(), "Items still need to be synchronized in the server!", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }

                    //Para que solo se activen los botones cuando todos este sincronizado en el servidor
                    if(HaySales == false & HayPurchases == false & HayClientes == false & HayItems == false){
                        btSyncUpClients.setEnabled(true);
                        btSyncUpPurchasesSales.setEnabled(true);
                        btSyncUpItems.setEnabled(true);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            public void onErrorResponse(VolleyError error) {
              //  Toast.makeText(getActivity(), error.toString() + "_onResponse", Toast.LENGTH_LONG).show();
            }
        });

        //Add the request to the RequestQ Queue
        mQueue.add(request);
        //    Toast.makeText(getActivity(), "You clicked me 1", Toast.LENGTH_SHORT).show();

    }

    private void loadSalesPersons() {
        // database handler
        DatabaseHelper db = new DatabaseHelper(getContext());

        // Spinner Drop down elements
        List<String> lables = db.getAllSalesPersons();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerSalesPersons.setAdapter(dataAdapter);
    }

    private void GetNiveles(){

        // Instantiate the RequestQueue.
        RequestQueue mQueue = Volley.newRequestQueue(getActivity());
        String url = "http://" + retrievedIP + "/api/Config";

        // Request a string response from the provided URL.
        JsonRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Integer id = 0;
                Integer itemLevels = 0;
                JSONObject item = null;
                try {

                    for (int i = 0; i < response.length(); i++) {
                        item = response.getJSONObject(i);
                        id = item.getInt("id");
                        itemLevels =item.getInt("ItemLevels");
                        //Assign to sharedPreferences
                        retrievedNivel = itemLevels;
                        SharedPreferences.Editor editor = spn.edit();
                        editor.putInt("ipn", retrievedNivel);
                        editor.commit();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                levels = itemLevels;
                Toast.makeText(getContext(), levels + " levels", Toast.LENGTH_SHORT).show();
            }

        }, new Response.ErrorListener() {


            public void onErrorResponse(VolleyError error) {
             //   Toast.makeText(getActivity(), error.toString() + "_onResponse", Toast.LENGTH_LONG).show();
            }
        });

        //Add the request to the RequestQ Queue
        mQueue.add(request);
        //    Toast.makeText(getActivity(), "You clicked me 1", Toast.LENGTH_SHORT).show();

    }
}