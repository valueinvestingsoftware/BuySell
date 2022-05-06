package x.stocks.valueinvesting;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import x.stocks.valueinvesting.DatabaseHelper.CardSuppliersAdapter;
import x.stocks.valueinvesting.DatabaseHelper.DatabaseHelper;
import x.stocks.valueinvesting.Models.CustomerModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ViewSuppliersFragment extends Fragment {

    View view;
    DatabaseHelper db;
    ArrayList<CustomerModel> listaViewSuppliersModel;
    CardSuppliersAdapter cardadapterSuppliers;
    RecyclerView recyclerViewSuppliers;
    String supplierOrClient;
    String supplierCustomerName;
    String tittle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Bundle bundle = getArguments();

        if (bundle != null) {
            supplierOrClient = bundle.getString("SupplierOCustomer");
            tittle = bundle.getString("title");
        }

        if(supplierOrClient == "1"){
            supplierCustomerName = "Supplier";
        }else{
            supplierCustomerName = "Customer";
        }

        getActivity().setTitle(tittle);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_suppliers, container, false);

        db = new DatabaseHelper(getContext());

        List<String> idList = new ArrayList<String>();
        List<String> supplierList = new ArrayList<String>();
        List<String> addressList = new ArrayList<String>();
        List<String> telephoneList = new ArrayList<String>();
        List<String> emailList = new ArrayList<String>();
        List<String> cityList = new ArrayList<String>();

        idList = db.getRecordedSuppliers(0, supplierOrClient);
        supplierList = db.getRecordedSuppliers(1, supplierOrClient);
        addressList = db.getRecordedSuppliers(2, supplierOrClient);
        telephoneList = db.getRecordedSuppliers(3, supplierOrClient);
        emailList = db.getRecordedSuppliers(4, supplierOrClient);
        cityList = db.getRecordedSuppliers(5, supplierOrClient);

        listaViewSuppliersModel = new ArrayList<>();

        for (int i = 0; i < supplierList.size(); i++) {
            int id =  Integer.valueOf(idList.get(i));
            String supplier =  String.valueOf(supplierList.get(i));
            String address = String.valueOf(addressList.get(i));
            String telephone = String.valueOf(telephoneList.get(i));
            String email = String.valueOf(emailList.get(i));
            String city = String.valueOf(cityList.get(i));
            byte [] imagenBytes = db.getImageBytesFromMap(id);

            listaViewSuppliersModel.add(new CustomerModel(id, supplier, true, address, city, telephone, email, "None", 0.0, 0.0, "1999-01-01", "1999-01-01", true, false, imagenBytes));
        }

        cardadapterSuppliers = new CardSuppliersAdapter(listaViewSuppliersModel, getContext(), supplierCustomerName);
        recyclerViewSuppliers = view.findViewById(R.id.rvViewSuppliers);
        recyclerViewSuppliers.setHasFixedSize(true);
        recyclerViewSuppliers.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSuppliers.setAdapter(cardadapterSuppliers);
        return view;
    }
}