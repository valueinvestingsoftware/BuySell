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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import x.stocks.valueinvesting.ItemsFragment;
import x.stocks.valueinvesting.MapFragment;
import x.stocks.valueinvesting.Models.ItemsModel;
import x.stocks.valueinvesting.R;
import x.stocks.valueinvesting.ViewItemsFragment;

public class CardItemsAdapter extends RecyclerView.Adapter<CardItemsAdapter.ViewHolder> {
    private List<ItemsModel> mdata;
    private LayoutInflater mInflater;
    private Context context;
    ItemsModel pos;

    public CardItemsAdapter(List<ItemsModel> itemsModel, Context context){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        mdata = itemsModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.lrscarditems, null);
        return new CardItemsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CardItemsAdapter.ViewHolder holder, final int position) {
        holder.bindData(mdata.get(position));
        pos = mdata.get(position);
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivItemView;
        TextView txtItemName;
        Button btDelete, btEdit;
        Integer itemId;
        DatabaseHelper db;

        ViewHolder(View itemView){
            super(itemView);
            ivItemView = itemView.findViewById(R.id.ivItemCardView);
            txtItemName = itemView.findViewById(R.id.txtCardItemNameView);
            btDelete = itemView.findViewById(R.id.btnDeleteItemsView);
            btEdit = itemView.findViewById(R.id.btnEditItemsView);
        }

        void bindData(final ItemsModel itemsModel){
            byte[] byteImage = itemsModel.getImage();
            Bitmap bmp = BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
            ivItemView.setImageBitmap(bmp);
            txtItemName.setText(itemsModel.getCategory());
            itemId = itemsModel.getId();

            DatabaseHelper db = new DatabaseHelper(context);
            List<String> itemIdsP = db.getPurchasedIds(1, " WHERE PurchaseInApp = 1 AND Sincronizado = 0");
            List<String> itemIdsS = db.getSoldIds(1, " WHERE SoldInApp = 1 AND Sincronizado = 0");

            btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Are you sure to delete this item?");
                    builder.setIcon(android.R.drawable.ic_menu_delete);
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(itemIdsP.contains(itemId.toString())){
                                Toast.makeText(context, "This item already has purchases!", Toast.LENGTH_SHORT).show();
                                return;
                            }else{
                                if(itemIdsS.contains(itemId.toString())){
                                    Toast.makeText(context, "This item already has sales!", Toast.LENGTH_SHORT).show();
                                    return;
                                }else{
                                    int result = db.deleteCatMapRecord(itemId);
                                    if(result > 0){
                                        Toast.makeText(context, "Item has been deleted!", Toast.LENGTH_SHORT).show();
                                        mdata.remove(getBindingAdapterPosition());
                                        notifyDataSetChanged();
                                    }else{
                                        Toast.makeText(context, "Failure to delete this Item!", Toast.LENGTH_SHORT).show();
                                    }
                                }
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
                    int id = itemId;
                    String name = txtItemName.getText().toString();

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want to edit item " + name);
                    builder.setIcon(android.R.drawable.ic_menu_edit);
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AppCompatActivity activity = (AppCompatActivity) v.getContext();
                            Fragment myItemsFragment = new ItemsFragment();

                            Bundle bundle = new Bundle();
                            bundle.putString("insert", "0");
                            bundle.putString("itemId", String.valueOf(id));
                            bundle.putString("itemName", name);
                            myItemsFragment.setArguments(bundle);

                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myItemsFragment).addToBackStack(null).commit();
                        }
                    });
                    builder.setNegativeButton("No", null);
                    builder.show();
                }
            });
        }

    }
}
