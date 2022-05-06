package x.stocks.valueinvesting;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import x.stocks.valueinvesting.DatabaseHelper.CardSAdapter;
import x.stocks.valueinvesting.DatabaseHelper.DatabaseHelper;
import x.stocks.valueinvesting.Models.ViewPurchasesSalesModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ViewSalesFragment extends Fragment {

    View view;
    DatabaseHelper db;
    ArrayList<ViewPurchasesSalesModel> listaViewPurchasesSalesModel;
    CardSAdapter cardSadapter;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("VIEW SALES");

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        view = inflater.inflate(R.layout.fragment_view_purchases, container, false);

        db = new DatabaseHelper(getContext());

        List<String> itemIdList = new ArrayList<String>();
        List<String> supplierList = new ArrayList<String>();
        List<String> itemNameList = new ArrayList<String>();
        List<String> quantityList = new ArrayList<String>();
        List<String> priceList = new ArrayList<String>();
        List<String> purchaseIdList = new ArrayList<String>();
        List<String> commentList = new ArrayList<String>();
        itemIdList = db.getRecordedSales(0);
        supplierList = db.getRecordedSales(1);
        itemNameList = db.getRecordedSales(2);
        quantityList = db.getRecordedSales(3);
        priceList = db.getRecordedSales(4);
        purchaseIdList = db.getRecordedSales(5);
        commentList = db.getRecordedSales(6);
        listaViewPurchasesSalesModel = new ArrayList<>();

        for (int i = 0; i < supplierList.size(); i++) {
            Integer purchaseId = Integer.valueOf(purchaseIdList.get(i));
            Integer itemId = Integer.valueOf(itemIdList.get(i));
            String itemName = String.valueOf(itemNameList.get(i));
            Integer quantity = Integer.valueOf(quantityList.get(i));
            Double price = Double.valueOf(priceList.get(i));
            String supplierName = String.valueOf(supplierList.get(i));
            String comment = String.valueOf(commentList.get(i));
            byte[] imagenBytes = db.getImageBytesFromCatMap(itemId);
            listaViewPurchasesSalesModel.add(new ViewPurchasesSalesModel(purchaseId, itemName, quantity, price, supplierName, imagenBytes, comment));
        }

        cardSadapter = new CardSAdapter(listaViewPurchasesSalesModel, getContext());
        recyclerView = view.findViewById(R.id.rvViewPurchases);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(cardSadapter);
        return view;
    }
    }

