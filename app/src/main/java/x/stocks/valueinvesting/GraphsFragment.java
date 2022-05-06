package x.stocks.valueinvesting;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import x.stocks.valueinvesting.DatabaseHelper.DatabaseHelper;
import x.stocks.valueinvesting.Models.BuySellModel;
import x.stocks.valueinvesting.Models.PurchasesModel;
import x.stocks.valueinvesting.Models.SalesModel;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GraphsFragment extends Fragment {

    String retrievedIP, whereClause;
    Handler h = new Handler();
    View view;
    DatabaseHelper db;
    TextView txtTop, txtPickDate, lblStatus;
    Button btnPerformance, btDate, btAvailability, btClass, btCategory, btSubcategory, btDownloadPS, btAvailabilityAll, btPerformanceAll, btSuppliers, btClients;
    Switch swType, swTopBottom;
    Integer que, retrievedValue, topBottom;
    String tittle, retrievedDate, retrievedDateSales;
    SharedPreferences spg;
    int mYear, mMonth, mDayOfMonth, a = 0;
    Spinner spClases, spCategorias, spSubcategorias;
    List<String> itemIdList = new ArrayList<String>();
    List<String> saleDateList = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("BASIC STATISTICS");

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SharedPreferences spRetrieve = getContext().getApplicationContext().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        retrievedValue = spRetrieve.getInt("value", 10);
        retrievedDate = spRetrieve.getString("date", "2021-01-01");
        retrievedDateSales = spRetrieve.getString("dateSales", retrievedDate);
        retrievedIP = spRetrieve.getString("ip", "");

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_graphs, container, false);

        btnPerformance = view.findViewById(R.id.btnPerformance);
        btDate = view.findViewById(R.id.btnDate);
        btAvailability = view.findViewById(R.id.btnItemAvailability);
        txtTop = view.findViewById(R.id.txtTop);
        txtPickDate = view.findViewById(R.id.lblPickAdate);
        swType = view.findViewById(R.id.switchType);
        swTopBottom = view.findViewById(R.id.swTop);
        lblStatus = view.findViewById(R.id.lblDownloadStatusPS);
        btClass = view.findViewById(R.id.btnClass);
        btCategory = view.findViewById(R.id.btnCategoria);
        btSubcategory = view.findViewById(R.id.btnSubCat);
        spClases = view.findViewById(R.id.spinnerClass);
        spCategorias = view.findViewById(R.id.spinnerCategories);
        spSubcategorias = view.findViewById(R.id.spinnerSubcategories);
        btDownloadPS = view.findViewById(R.id.btnSyncDownPS);
        btAvailabilityAll = view.findViewById(R.id.btnAllItemsAvailability);
        btPerformanceAll = view.findViewById(R.id.btnAllItemsPerformance);
        btClients = view.findViewById(R.id.btnGraphCustomers);
        btSuppliers = view.findViewById(R.id.btnGraphSuppliers);

        Boolean tieneRows;
        tieneRows = BuySellHasRows();

        if(tieneRows==false){
            btAvailabilityAll.setEnabled(false);
            btPerformanceAll.setEnabled(false);
            btClients.setEnabled(false);
            btSuppliers.setEnabled(false);
        }

        //Alimento esta lista para al dar clic en descargar ventas haga un loop solo entre los items de BuySell tableque tienen registros
        db = new DatabaseHelper(getContext());
        itemIdList =  db.getRecordedSalesDownloaded(retrievedDate, 1);
        saleDateList = db.getRecordedSalesDownloaded(retrievedDate, 2);

        Boolean soldItemsTableHasRows = SoldItemsHasRows();
        if(soldItemsTableHasRows==false) {
            lblStatus.setText("Sales have not been downloaded yet!");
        }else{
            lblStatus.setText("Sales after the " + retrievedDateSales + " were downloaded!");
        }

        txtTop.setText(retrievedValue.toString());
        txtPickDate.setText(retrievedDate);

        //To assign into shared preferences
        spg = getContext().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        //view.clearFocus();

        spClases.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadClasses("Categoria", spCategorias, "WHERE Clase = '" + spClases.getSelectedItem().toString() + "'");

                String cat = spRetrieve.getString("categoria", "");
                spCategorias.setSelection(((ArrayAdapter) spCategorias.getAdapter()).getPosition(cat));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        loadClasses("Clase", spClases, "");
        //set the selected item in the subcategory spinner based on the shared preferences
        String clas = spRetrieve.getString("clase", "");
        spClases.setSelection(((ArrayAdapter) spClases.getAdapter()).getPosition(clas));

        spCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadClasses("Subcategoria", spSubcategorias, "WHERE Categoria = '" + spCategorias.getSelectedItem().toString() + "'");

                String subcat = spRetrieve.getString("subcategoria", "");
                spSubcategorias.setSelection(((ArrayAdapter) spSubcategorias.getAdapter()).getPosition(subcat));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //set the selected item in the subcategory spinner based on the shared preferences

        spSubcategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            final Calendar cal = Calendar.getInstance();
             mDayOfMonth = cal.get(Calendar.DATE);
             mMonth = cal.get(Calendar.MONTH);
             mYear = cal.get(Calendar.YEAR);
             DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                 @Override
                 public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                     String fixedMonth =  returnCorrectNumberDigits(month + 1);
                     String fixedDayOfMonth = returnCorrectNumberDigits(dayOfMonth);
                     txtPickDate.setText(year + "-" + fixedMonth + "-" + fixedDayOfMonth);
                     retrievedDate = txtPickDate.getText().toString();

                     SharedPreferences.Editor editor = spg.edit();
                     editor.putInt("value", retrievedValue);
                     editor.putString("date", retrievedDate);
                     editor.commit();
                 }
             }, mYear, mMonth, mDayOfMonth);
             datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
             datePickerDialog.show();
            }
        });

        btAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallTheRightGraph(2);
            }
        });
        btnPerformance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallTheRightGraph(1);
            }
        });

        btClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveInSharedPreferencesClasses();
                SharedPreferences spRetrieve = getContext().getApplicationContext().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);

                CallTheRightGraphClasses(1, "");
            }
        });

        btCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveInSharedPreferencesClasses();
                SharedPreferences spRetrieve = getContext().getApplicationContext().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);

                CallTheRightGraphClasses(2, spClases.getSelectedItem().toString());
            }
        });

        btSubcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveInSharedPreferencesClasses();
                SharedPreferences spRetrieve = getContext().getApplicationContext().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
                Toast.makeText(getContext(), spRetrieve.getString("subcategoria", ""), Toast.LENGTH_SHORT).show();

                CallTheRightGraphClasses(3, spCategorias.getSelectedItem().toString());
            }
        });

        btDownloadPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogOnClickListener = new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = spg.edit();
                        editor.putString("dateSales", retrievedDate);
                        editor.commit();

                        db = new DatabaseHelper(getContext());
                        db.delete("SoldItems");
                        BajarSales();

                        Boolean soldItemsTableHasRows = SoldItemsHasRows();
                        if(soldItemsTableHasRows==false) {
                            lblStatus.setText("Sales have not been downloaded yet!");
                        }else{
                            lblStatus.setText("Sales after the " + retrievedDate + " were downloaded!");
                            Toast.makeText(getContext(), retrievedDate, Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmation");
                builder.setIcon(android.R.drawable.stat_sys_download);
                builder.setCancelable(false);
                builder.setMessage("Do you want to download all sales made after the " + retrievedDate).setPositiveButton("Yes", dialogOnClickListener)
                        .setNegativeButton("No", dialogOnClickListener).show();
            }
        });

        btAvailabilityAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallTheRightGraph(3);
            }
        });

        btPerformanceAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallTheRightGraph(4);
            }
        });

        return view;
    }

    private String returnCorrectNumberDigits(Integer entra){
        String sale = entra.toString();
        String intermedio = entra.toString();
        if(intermedio.length() < 2){
           sale = "0" + entra.toString();
        }
        return sale;
    }
    private void CallTheRightGraph(int queGrafico){
        if (txtTop.getText().toString().length() == 0) {
            txtTop.setError("Type a number between 1 and 100!");
        } else {
            int intTop = Integer.parseInt(txtTop.getText().toString());
            if (intTop == 0) {
                txtTop.setError("Type a number bigger that 0!");
            } else {
                Bar_Chart_Fragment barFragment = new Bar_Chart_Fragment();
                if (intTop > 100) {
                    txtTop.setError("Type a number smaller than 101!");
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("topNumber", intTop);
                    if (swTopBottom.isChecked()) {
                        topBottom = 0;
                    } else {
                        topBottom = 1;
                    }
                    if (swType.isChecked()) {
                        que = 1;
                        tittle = "TOP PERFORMING ITEMS PER PROFIT";
                    } else {
                        que = 0;
                        tittle = "TOP PERFORMING ITEMS PER SOLD QUANTITY";
                    }
                    retrievedValue = Integer.parseInt(txtTop.getText().toString());
                    retrievedDate = txtPickDate.getText().toString();

                    bundle.putInt("que", que);
                    bundle.putInt("topBottom", topBottom);
                    bundle.putString("tittle", tittle);
                    bundle.putString("date", retrievedDate);
                    bundle.putInt("queGrafico", queGrafico);
                    barFragment.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = getFragmentManager()
                            .beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container,
                            barFragment).addToBackStack("tag").commit();
                }
            }
        }
    }

    private void CallTheRightGraphClasses(int queGrafico, String filtro){
        if (txtTop.getText().toString().length() == 0) {
            txtTop.setError("Type a number between 1 and 100!");
        } else {
            int intTop = Integer.parseInt(txtTop.getText().toString());
            if (intTop == 0) {
                txtTop.setError("Type a number bigger that 0!");
            } else {
                Pie_Chart_Fragment pieFragment = new Pie_Chart_Fragment();
                if (intTop > 100) {
                    txtTop.setError("Type a number smaller than 101!");
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("topNumber", intTop);
                    if (swTopBottom.isChecked()) {
                        topBottom = 0;
                    } else {
                        topBottom = 1;
                    }
                    if (swType.isChecked()) {
                        que = 1;
                        tittle = "TOP PERFORMING ITEMS PER PROFIT";
                    } else {
                        que = 0;
                        tittle = "TOP PERFORMING ITEMS PER SOLD QUANTITY";
                    }
                    retrievedValue = Integer.parseInt(txtTop.getText().toString());
                    retrievedDate = txtPickDate.getText().toString();

                    bundle.putInt("que", que);
                    bundle.putInt("topBottom", topBottom);
                    bundle.putString("tittle", tittle);
                    bundle.putString("date", retrievedDate);
                    bundle.putInt("queGrafico", queGrafico);
                    bundle.putString("filtro", filtro);
                    pieFragment.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = getFragmentManager()
                            .beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container,
                            pieFragment).addToBackStack("tag").commit();
                }
            }
        }

    }

    private Boolean loadClasses(String tipo, Spinner sp, String filtro) {

        Boolean exito = false;

        try {
            // database handler
            DatabaseHelper db = new DatabaseHelper(getContext());

            // Spinner Drop down elements
            List<String> lables = db.getAllClasses(tipo, filtro);

            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item, lables);

            // Drop down layout style - list view with radio button
            dataAdapter
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            sp.setAdapter(dataAdapter);

            exito = true;

            return exito;

        } catch (Exception e) {
            exito = false;
            return exito;
        }
    }

    private void BajarSales(){
        new Thread(new Runnable() {

            @Override
            public void run() {
                h.post(new Runnable() {
                    @Override
                    public void run() {

                        Integer itemId;
                        String saleDate = retrievedDate;
                        final int[] k = {0};

                        for (int i = 0; i < itemIdList.size(); i++) {
                            itemId = Integer.parseInt(itemIdList.get(i));

                        // Instantiate the RequestQueue.
                        RequestQueue mQueue = Volley.newRequestQueue(getActivity());
                        String url = "http://" + retrievedIP + "/Api/SoldItems?ItemId=" + itemId + "&soldDate=" + saleDate;

                        // Request a string response from the provided URL.
                        JsonRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                            @Override
                            public void onResponse(JSONArray response) {
                                Integer id = 0;
                                Integer ItemId = 0;
                                Integer ClientId = 0;
                                Double SaleQuantity = 0.0;
                                Double SalePrice = 0.0;
                                String SaleDate = "";
                                String Observations = "";
                                Double Profit = 0.0;

                                JSONObject item = null;
                                try {
                                    a = 0;
                                    a = itemIdList.size() + 1;
                                    k[0] = k[0] + 1;
                                    for (int j = 0; j < response.length(); j++) {

                                        item = response.getJSONObject(j);
                                        id = id + 1;
                                        ItemId = item.getInt("itemid");
                                        ClientId = item.getInt("ClientId");
                                        SaleQuantity = item.getDouble("SaleQuantity");
                                        SalePrice = item.getDouble("SalePrice");
                                        SaleDate = item.getString("SaleDate");
                                        Profit = item.getDouble("Profit");

                                        SalesModel salesModel = new SalesModel(id, ItemId, ClientId, SaleQuantity, SalePrice, SaleDate, "none", Profit, false, false);

                                        DatabaseHelper dataBasehelper = new DatabaseHelper(getContext());
                                        boolean success = dataBasehelper.addOneSaleDownloaded(salesModel);
                                        int percentage = ((k[0] + 1) * 100) / a;
                                        Toast.makeText(getActivity(), "Downloading sale's info: " + percentage + "%", Toast.LENGTH_SHORT).show();
                                        lblStatus.setText("Sales after the " + retrievedDate + " were downloaded!");
                                    }

                                } catch (JSONException e) {
                                    //  btSyncDownItems.setEnabled(true);
                                    //  Toast.makeText(getActivity(), "There was an error while downloading items, please try again!", Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }

                                // Toast.makeText(getActivity(), "Sales were downloaded successfully!", Toast.LENGTH_LONG).show();
                              //  lblStatus.setText("Sales were downloaded successfully!");

                                Boolean soldItemsTableHasRows = SoldItemsHasRows();
                                if(soldItemsTableHasRows==false) {
                                    // btDownloadPS.setEnabled(false);
                                    btAvailabilityAll.setEnabled(false);
                                    btPerformanceAll.setEnabled(false);
                                    btClients.setEnabled(false);
                                    btSuppliers.setEnabled(false);
                                }else{
                                    // btDownloadPS.setEnabled(false);
                                    btAvailabilityAll.setEnabled(true);
                                    btPerformanceAll.setEnabled(true);
                                    btClients.setEnabled(true);
                                    btSuppliers.setEnabled(true);
                                }
                            }
                        }, new Response.ErrorListener() {

                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getActivity(), error.toString() + "_onResponse", Toast.LENGTH_LONG).show();
                            }
                        });


                        //Add the request to the RequestQ Queue
                        mQueue.add(request);
                        //    Toast.makeText(getActivity(), "You clicked me 1", Toast.LENGTH_SHORT).show();
                    }
                    }

                });
            }
        }).start();

    }

    private Boolean BuySellHasRows() {

        Boolean hasRows = false;

        try {
            // database handler
            DatabaseHelper db = new DatabaseHelper(getContext());
            List<String> lables = db.getAllClasses("Categoria", "");

            for(int i = 0; i< lables.size(); i++){
                hasRows = true;
                break;
            }

            return hasRows;

        } catch (Exception e) {
            hasRows = false;
            return hasRows;
        }
    }

    private Boolean SoldItemsHasRows() {

        Boolean hasRows = false;

        try {
            // database handler
            DatabaseHelper db = new DatabaseHelper(getContext());
            List<String> lables = db.getSoldIds(0, "");

            for(int i = 0; i< lables.size(); i++){
                hasRows = true;
                break;
            }

            return hasRows;

        } catch (Exception e) {
            hasRows = false;
            return hasRows;
        }
    }
    private void SaveInSharedPreferencesClasses(){
        SharedPreferences.Editor editor = spg.edit();
        editor.putString("clase", spClases.getSelectedItem().toString());
        editor.putString("categoria", spCategorias.getSelectedItem().toString());
        editor.putString("subcategoria", spSubcategorias.getSelectedItem().toString());
        editor.commit();
    }
}
