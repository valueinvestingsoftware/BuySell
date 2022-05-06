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
import x.stocks.valueinvesting.SaleFragment;

public class CardSAdapter extends RecyclerView.Adapter<CardSAdapter.ViewHolder> {
    private List<ViewPurchasesSalesModel> sdata;
    private LayoutInflater mInflater;
    private Context context;
    ViewPurchasesSalesModel pos;
    public CardSAdapter(List<ViewPurchasesSalesModel> viewPurchases, Context context){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        sdata = viewPurchases;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.lrscardpurchases, null);
        return new CardSAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(sdata.get(position));
        pos = sdata.get(position);
    }

    @Override
    public int getItemCount() {return sdata.size();}

    public void setItems(List<ViewPurchasesSalesModel>viewPurchases){sdata = viewPurchases; }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivItemCP;
        TextView txtCPSupplier, txtCPItemName, txtCPQuantity, txtCPPrice;
        Button btDelete, btEdit;
        Integer saleId;
        String observationsSale;
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
            saleId = viewPurchasesSalesModel.getPurchaseId();
            observationsSale = viewPurchasesSalesModel.getObservations();

            btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db = new DatabaseHelper(context);

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Are you sure to delete this sale?");
                    builder.setIcon(android.R.drawable.ic_menu_delete);
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int result = db.deleteSale(saleId);
                            if(result > 0){
                                Toast.makeText(context, "Sale has been deleted!", Toast.LENGTH_SHORT).show();
                                sdata.remove(getBindingAdapterPosition());
                                notifyDataSetChanged();
                            }else{
                                Toast.makeText(context, "Failure to delete this sale!", Toast.LENGTH_SHORT).show();
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
                            Fragment mySaleFragment = new SaleFragment();

                            String customerName = txtCPSupplier.getText().toString();
                            String itemName = txtCPItemName.getText().toString();
                            String qty = txtCPQuantity.getText().toString();
                            String price = txtCPPrice.getText().toString();

                            Bundle bundlePurchase = new Bundle();
                            bundlePurchase.putString("insert", "0");
                            bundlePurchase.putString("saleid", saleId.toString());
                            bundlePurchase.putString("salecustomername", customerName);
                            bundlePurchase.putString("saleitemname", itemName);
                            bundlePurchase.putString("saleobservations", observationsSale);
                            bundlePurchase.putString("saleqty", qty);
                            bundlePurchase.putString("saleprice", price);
                            mySaleFragment.setArguments(bundlePurchase);

                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mySaleFragment).addToBackStack(null).commit();

                        }
                    });
                    builder.setNegativeButton("No", null);
                    builder.show();
                }
            });
        }

    }
}
