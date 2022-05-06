package x.stocks.valueinvesting.DatabaseHelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import x.stocks.valueinvesting.Models.ViewPurchasesSalesModel;
import x.stocks.valueinvesting.PurchaseFragment;
import x.stocks.valueinvesting.R;

public class CardPAdapter extends RecyclerView.Adapter<CardPAdapter.ViewHolder> {
private List<ViewPurchasesSalesModel> mdata;
private LayoutInflater mInflater;
private Context context;
ViewPurchasesSalesModel pos;

public CardPAdapter(List<ViewPurchasesSalesModel> viewPurchases, Context context){
    this.mInflater = LayoutInflater.from(context);
    this.context = context;
    mdata = viewPurchases;
}

    @NonNull
    @Override
    public CardPAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.lrscardpurchases, null);
        return new CardPAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CardPAdapter.ViewHolder holder, final int position) {
       holder.bindData(mdata.get(position));
       pos = mdata.get(position);
    }

    @Override
    public int getItemCount() {return mdata.size();}

    public void setItems(List<ViewPurchasesSalesModel>viewPurchases){mdata = viewPurchases; }

    public class ViewHolder extends RecyclerView.ViewHolder{
    ImageView ivItemCP;
    TextView txtCPSupplier, txtCPItemName, txtCPQuantity, txtCPPrice;
    Button btDelete, btEdit;
    Integer purchaseId;
    String observationsPurchase;
    DatabaseHelper db;

    ViewHolder(View itemView){
        super(itemView);
        ivItemCP = itemView.findViewById(R.id.ivItemCardP);
        txtCPSupplier = itemView.findViewById(R.id.txtCardPSupplierName);
        txtCPItemName = itemView.findViewById(R.id.txtCardPitemName);
        txtCPQuantity = itemView.findViewById(R.id.txtCardPQuantity);
        txtCPPrice = itemView.findViewById(R.id.txtCardPprice);
        btDelete = itemView.findViewById(R.id.btnDeletePurchasesSales);
        btEdit = itemView.findViewById(R.id.btnEditPurchasesSales);
    }

    void bindData(final ViewPurchasesSalesModel viewPurchasesSalesModel){
        byte[] byteImage = viewPurchasesSalesModel.getImage();
        Bitmap bmp = BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
        ivItemCP.setImageBitmap(bmp);
        txtCPSupplier.setText(viewPurchasesSalesModel.getSupplierCardP());
        txtCPItemName.setText(viewPurchasesSalesModel.getItemNameCardP());
        txtCPQuantity.setText(viewPurchasesSalesModel.getQuantityCardP().toString());
        txtCPPrice.setText(viewPurchasesSalesModel.getPriceCardP().toString());
        purchaseId = viewPurchasesSalesModel.getPurchaseId();
        observationsPurchase = viewPurchasesSalesModel.getObservations();
        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = new DatabaseHelper(context);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure to delete this purchase?");
                builder.setIcon(android.R.drawable.ic_menu_delete);
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int result = db.deletePurchase(purchaseId);
                        if(result > 0){
                            Toast.makeText(context, "Purchase has been deleted!", Toast.LENGTH_SHORT).show();
                            mdata.remove(getBindingAdapterPosition());
                            notifyDataSetChanged();
                        }else{
                            Toast.makeText(context, "Failure to delete this purchase!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("No", null);
                builder.show();
            }
        });
        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirmation");
                builder.setMessage("Do you want to edit this purchase?");
                builder.setIcon(android.R.drawable.ic_menu_delete);
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        Fragment myPurchaseFragment = new PurchaseFragment();

                        String SupplierName = txtCPSupplier.getText().toString();
                        String ItemName = txtCPItemName.getText().toString();
                        String qty = txtCPQuantity.getText().toString();
                        String price = txtCPPrice.getText().toString();

                        Bundle bundlePurchase = new Bundle();
                        bundlePurchase.putString("insert", "0");
                        bundlePurchase.putString("purchaseId", purchaseId.toString());
                        bundlePurchase.putString("purchaseSupplierName", SupplierName);
                        bundlePurchase.putString("purchaseItemName", ItemName);
                        bundlePurchase.putString("purchaseObservations", observationsPurchase);
                        bundlePurchase.putString("purchaseQty", qty);
                        bundlePurchase.putString("purchasePrice", price);
                        myPurchaseFragment.setArguments(bundlePurchase);

                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myPurchaseFragment).addToBackStack(null).commit();

                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();
            }
        });
    }

    }
}
