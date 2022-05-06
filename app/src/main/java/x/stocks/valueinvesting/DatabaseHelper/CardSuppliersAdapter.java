package x.stocks.valueinvesting.DatabaseHelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import x.stocks.valueinvesting.CustomersFragment;
import x.stocks.valueinvesting.MapFragment;
import x.stocks.valueinvesting.Models.CustomerModel;
import x.stocks.valueinvesting.R;

public class CardSuppliersAdapter extends RecyclerView.Adapter<CardSuppliersAdapter.ViewHolder> {
    private List<CustomerModel> mdata;
    private LayoutInflater mInflater;
    private Context context;
    private String Que;
    CustomerModel pos;

    public CardSuppliersAdapter(List<CustomerModel> customerModel, Context context, String que) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.Que = que;
        mdata = customerModel;
    }

    @NonNull
    @Override
    public CardSuppliersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.lrscardsuppliers, null);
        return new CardSuppliersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CardSuppliersAdapter.ViewHolder holder, final int position) {
        holder.bindData(mdata.get(position));
        pos = mdata.get(position);
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivSupplier;
        TextView txtSupplierName, txtAddress, txtEmail, txtPhone, txtCity;
        Button btDelete, btMap, btWhatsapp, btRoute, btEdit;
        Integer customerId;
        String phoneNumber, observaciones;
        Double x, y;

        ViewHolder(View itemView) {
            super(itemView);
            ivSupplier = itemView.findViewById(R.id.ivCardSupplier);
            txtSupplierName = itemView.findViewById(R.id.txtCardSupplierName);
            txtAddress = itemView.findViewById(R.id.txtCardAddress);
            txtEmail = itemView.findViewById(R.id.txtCardEmail);
            txtPhone = itemView.findViewById(R.id.txtCardPhone);
            txtCity = itemView.findViewById(R.id.txtCardCity);
            btDelete = itemView.findViewById(R.id.btnDeletePurchasesSales);
            btMap = itemView.findViewById(R.id.btnMapFromDeleteSupplier);
            btWhatsapp = itemView.findViewById(R.id.btnWhatsappFromDeleteSupplier);
            btRoute = itemView.findViewById(R.id.btnRouteFromDeleteSupplier);
            btEdit = itemView.findViewById(R.id.btnEditFromDeleteSupplier);
        }

        void bindData(final CustomerModel customersModel) {
            byte[] byteImage = customersModel.getImage();
            Bitmap bmp = BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
            ivSupplier.setImageBitmap(bmp);
            txtSupplierName.setText(customersModel.getContacto());
            txtAddress.setText(customersModel.getAddress());
            txtEmail.setText(customersModel.getEmail().toString());
            txtPhone.setText(customersModel.getTelefono().toString());
            txtCity.setText(customersModel.getCity().toString());
            observaciones =customersModel.getComment().toString();
            customerId = customersModel.getId();
            phoneNumber = customersModel.getTelefono();
            x = customersModel.getLongitude();
            y = customersModel.getLatitude();

            DatabaseHelper db = new DatabaseHelper(context);
            List<String> suppliersId = db.getPurchasedIds(2, " WHERE PurchaseInApp = 1 AND Sincronizado = 0");
            List<String> customersId = db.getSoldIds(2, " WHERE SoldInApp = 1 AND Sincronizado = 0");

            btDelete.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                                builder.setTitle("Confirmation");
                                                builder.setMessage("Are you sure to delete this " + Que + "?");
                                                builder.setIcon(android.R.drawable.ic_menu_delete);
                                                builder.setCancelable(false);
                                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        if (suppliersId.contains(customerId.toString())) {
                                                            Toast.makeText(context, "This supplier already has purchases!", Toast.LENGTH_SHORT).show();
                                                            return;
                                                        } else {
                                                            if (customersId.contains(customerId.toString())) {
                                                                Toast.makeText(context, "This customer already has sales!", Toast.LENGTH_SHORT).show();
                                                                return;
                                                            } else {
                                                                int result = db.deleteMapRecord(customerId);
                                                                if (result > 0) {
                                                                    Toast.makeText(context, Que + " has been deleted!", Toast.LENGTH_SHORT).show();
                                                                    mdata.remove(getBindingAdapterPosition());
                                                                    notifyDataSetChanged();
                                                                } else {
                                                                    Toast.makeText(context, "Failure to delete this " + Que + "!", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        }
                                                    }
                                                });

                                                builder.setNegativeButton("No", null);
                                                builder.show();
                                            }
                                        }
            );

            btMap.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             int clientId = customerId;
                                             String clienteNombre = db.getCustomersNameForMap(String.valueOf(clientId));

                                             AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                             builder.setTitle("Confirmation");
                                             builder.setMessage("Do you want to see " + clienteNombre + " in the map?");
                                             builder.setIcon(android.R.drawable.ic_menu_delete);
                                             builder.setCancelable(false);
                                             builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                 @Override
                                                 public void onClick(DialogInterface dialog, int which) {
                                                     Double lat = db.getCustomersCoordinates(0, String.valueOf(clientId));
                                                     Double lon = db.getCustomersCoordinates(1, String.valueOf(clientId));
                                                     openMapFragment(clienteNombre, lat, lon, v);
                                                 }
                                             });
                                             builder.setNegativeButton("No", null);
                                             builder.show();
                                         }
                                     }
            );
            btWhatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String phone = phoneNumber;
                    String message = "", retrievedPhoneCode;
                    String codigoPais = "+593";
                    int clientId = customerId;
                    String clienteNombre = db.getCustomersNameForMap(String.valueOf(clientId));

                    SharedPreferences sp;
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    SharedPreferences spRetrieve = activity.getApplicationContext().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
                    retrievedPhoneCode = spRetrieve.getString("phonecode", "+111");
                    // sp = activity.getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want to send a whatsapp message to " + clienteNombre + "?");
                    builder.setIcon(android.R.drawable.ic_menu_delete);
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            boolean installed = AppInstalledOrNot("com.whatsapp", v);
                            if(installed == true){
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + retrievedPhoneCode + phone + "&text=" + message));
                                activity.startActivity(intent);
                            }else{
                                Toast.makeText(v.getContext(), "Whatsapp is not installed on this phone", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    builder.setNegativeButton("No", null);
                    builder.show();
                }
            });

            btRoute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clientId = customerId;
                    String clienteNombre = db.getCustomersNameForMap(String.valueOf(clientId));

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want to see " + clienteNombre + " in the map?");
                    builder.setIcon(android.R.drawable.ic_menu_delete);
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Double lat = db.getCustomersCoordinates(0, String.valueOf(clientId));
                            Double lon = db.getCustomersCoordinates(1, String.valueOf(clientId));
                            openMapFragment(clienteNombre, lat, lon, v);
                        }
                    });
                    builder.setNegativeButton("No", null);
                    builder.show();
                }
            });
            btEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Double lat = db.getCustomersCoordinates(0, String.valueOf(customerId));
                    Double lon = db.getCustomersCoordinates(1, String.valueOf(customerId));
                    Integer supplier = db.getCustomersOrSupplier(customerId.toString());

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want to edit " + txtSupplierName.getText().toString() + "?");
                    builder.setIcon(android.R.drawable.ic_menu_delete);
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AppCompatActivity activity = (AppCompatActivity) v.getContext();
                            Fragment myCustomersFragment = new CustomersFragment();

                            Bundle bundleC = new Bundle();
                            bundleC.putString("supplier", String.valueOf(supplier.toString()));
                            bundleC.putString("insert", "0");
                            bundleC.putString("customerId", customerId.toString());
                            bundleC.putString("customerName", txtSupplierName.getText().toString());
                            bundleC.putString("customerEmail", txtEmail.getText().toString());
                            bundleC.putString("customerTelephone", txtPhone.getText().toString());
                            bundleC.putString("customerObservations", observaciones);
                            bundleC.putString("customerAddress", txtAddress.getText().toString());
                            bundleC.putString("customerCity", txtCity.getText().toString());
                            bundleC.putString("customerLatitud", lat.toString());
                            bundleC.putString("customerLongitud", lon.toString());
                            myCustomersFragment.setArguments(bundleC);

                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myCustomersFragment).addToBackStack(null).commit();
                        }
                    });
                    builder.setNegativeButton("No", null);
                    builder.show();
                }
            });
        }
    }

    private void openMapFragment(String supplierName, Double lat, Double lon, View view) {

        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        Fragment myMapFragment = new MapFragment();

        Bundle bundle = new Bundle();
        bundle.putString("nameCliente", String.valueOf(supplierName));
        bundle.putString("x", String.valueOf(lon));
        bundle.putString("y", String.valueOf(lat));
        myMapFragment.setArguments(bundle);

        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myMapFragment).addToBackStack(null).commit();
    }

    private boolean AppInstalledOrNot(String url, View view){

        AppCompatActivity activity = (AppCompatActivity) view.getContext();

        PackageManager packageManager = activity.getPackageManager();
        boolean app_installed;
        try{
            packageManager.getPackageInfo(url, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }catch(PackageManager.NameNotFoundException e){
            app_installed = false;
        }
        return app_installed;
    }
}
