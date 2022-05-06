package x.stocks.valueinvesting;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import x.stocks.valueinvesting.DatabaseHelper.CardPAdapter;
import x.stocks.valueinvesting.DatabaseHelper.DatabaseHelper;
import x.stocks.valueinvesting.Models.ViewPurchasesSalesModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ViewPurchasesFragment extends Fragment {

    View view;
    DatabaseHelper db;
    ArrayList<ViewPurchasesSalesModel> listaViewPurchasesSalesModel;
    CardPAdapter cardPadapter;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("VIEW PURCHASES");

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Inflate the layout for this fragment
       view = inflater.inflate(R.layout.fragment_view_purchases, container, false);

       db = new DatabaseHelper(getContext());

    List<String> itemIdList = new ArrayList<String>();
    List<String> supplierList = new ArrayList<String>();
    List<String> itemNameList = new ArrayList<String>();
    List<String> quantityList = new ArrayList<String>();
    List<String> priceList = new ArrayList<String>();
    List<String> purchaseIdList = new ArrayList<String>();
    List<String> commentList = new ArrayList<String>();
    itemIdList = db.getRecordedPurchases(0);
    supplierList = db.getRecordedPurchases(1);
    itemNameList = db.getRecordedPurchases(2);
    quantityList = db.getRecordedPurchases(3);
    priceList = db.getRecordedPurchases(4);
    purchaseIdList = db.getRecordedPurchases(5);
    commentList = db.getRecordedPurchases(6);

    listaViewPurchasesSalesModel = new ArrayList<>();

        for (int i = 0; i < supplierList.size(); i++) {
        Integer purchaseId =  Integer.valueOf(purchaseIdList.get(i));
        Integer itemId =  Integer.valueOf(itemIdList.get(i));
        String itemName = String.valueOf(itemNameList.get(i));
        Integer quantity = Integer.valueOf(quantityList.get(i));
        Double price = Double.valueOf(priceList.get(i));
        String supplierName = String.valueOf(supplierList.get(i));
        String comment = String.valueOf(commentList.get(i));
        byte [] imagenBytes = db.getImageBytesFromCatMap(itemId);
        listaViewPurchasesSalesModel.add(new ViewPurchasesSalesModel(purchaseId, itemName, quantity, price, supplierName, imagenBytes, comment));
    }

    cardPadapter = new CardPAdapter(listaViewPurchasesSalesModel, getContext());
    recyclerView = view.findViewById(R.id.rvViewPurchases);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(cardPadapter);
        return view;
}
}