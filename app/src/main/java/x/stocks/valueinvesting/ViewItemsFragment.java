package x.stocks.valueinvesting;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import x.stocks.valueinvesting.DatabaseHelper.CardItemsAdapter;
import x.stocks.valueinvesting.DatabaseHelper.DatabaseHelper;
import x.stocks.valueinvesting.Models.ItemsModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ViewItemsFragment extends Fragment {
    View view;
    DatabaseHelper db;
    ArrayList<ItemsModel> listaViewItemsModel;
    CardItemsAdapter cardItemsAdapter;
    RecyclerView recyclerViewItemsView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("ITEMS CREATED IN APP");

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_items, container, false);

        db = new DatabaseHelper(getContext());

        List<String> itemIdList = new ArrayList<String>();
        List<String> itemNameList = new ArrayList<String>();

        itemIdList = db.getRecordedItems(0);
        itemNameList = db.getRecordedItems(1);

        listaViewItemsModel = new ArrayList<>();

        for (int i = 0; i < itemNameList.size(); i++) {
            Integer itemId =  Integer.valueOf(itemIdList.get(i));
            String itemName =  String.valueOf(itemNameList.get(i));
            byte [] imagenBytes = db.getImageBytesFromCatMap(itemId);
            listaViewItemsModel.add(new ItemsModel(itemId, "None", itemName, 4, "1999-01-01", "1999-01-01", imagenBytes, true, true, false));
        }

        cardItemsAdapter = new CardItemsAdapter(listaViewItemsModel, getContext());
        recyclerViewItemsView = view.findViewById(R.id.rvViewItemsView);
        recyclerViewItemsView.setHasFixedSize(true);
        recyclerViewItemsView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewItemsView.setAdapter(cardItemsAdapter);
        return view;

    }
}