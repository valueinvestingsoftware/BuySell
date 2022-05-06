package x.stocks.valueinvesting;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import x.stocks.valueinvesting.DatabaseHelper.DatabaseHelper;
import x.stocks.valueinvesting.Models.ItemSalesDataModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class Bar_Chart_Fragment extends Fragment {

    ArrayList<BarEntry> itemValues;
    ArrayList<String> itemnames;
    ArrayList<ItemSalesDataModel> itemSalesDataModel = new ArrayList<>();

    String tittle, pickAdate;
    Integer topNumber, que, topBottom, queGrafico;
    BarChart barChart;
    View view;
    DatabaseHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        view = inflater.inflate(R.layout.fragment_bar_chart, container, false);

        Bundle bundle = getArguments();
        if (bundle != null){
            topNumber = bundle.getInt("topNumber");
            que = bundle.getInt("que");
            topBottom = bundle.getInt("topBottom");
            tittle = bundle.getString("tittle");
            pickAdate = bundle.getString("date");
            queGrafico = bundle.getInt("queGrafico");
            getActivity().setTitle(tittle);

            barChart = view.findViewById(R.id.barChart);

            itemValues = new ArrayList<>();
            itemnames = new ArrayList<>();
            db = new DatabaseHelper(getContext());

            switch (queGrafico){
                case 1:
                    itemSalesDataModel = db.LoadDataSales(que, topNumber, topBottom, pickAdate, 1);
                    break;
                case 2:
                    itemSalesDataModel = db.LoadDataSales(que, topNumber, topBottom, pickAdate, 2);
                    break;
                case 3:
                    itemSalesDataModel = db.LoadDataSalesAll(que, topNumber, topBottom, pickAdate, 2);
                break;
                case 4:
                    itemSalesDataModel = db.LoadDataSalesAll(que, topNumber, topBottom, pickAdate, 1);
                    break;
            }

            for(int i = 0; i < itemSalesDataModel.size(); i ++){
            String itemName = itemSalesDataModel.get(i).getItemname();
            Integer itemValue = itemSalesDataModel.get(i).getItemvalue();

                itemValues.add(new BarEntry(i, itemValue));
                itemnames.add(itemName);
            }

            BarDataSet bardataset = new BarDataSet(itemValues, "Items");
            bardataset.setColors(ColorTemplate.MATERIAL_COLORS);
            bardataset.setValueTextColor(Color.BLACK);
            bardataset.setValueTextSize(12f);

            BarData bardata = new BarData(bardataset);
            barChart.setFitBars(true);
            barChart.setData(bardata);
            barChart.getDescription().setText("Top performing items");
            XAxis x = barChart.getXAxis();
            x.setValueFormatter(new IndexAxisValueFormatter(itemnames));
            x.setPosition(XAxis.XAxisPosition.TOP);
            x.setDrawGridLines(false);
            x.setDrawAxisLine(false);
            x.setGranularity(1f);
            x.setLabelCount(itemnames.size());
            x.setLabelRotationAngle(270);
            barChart.animateY(2000);
            barChart.invalidate();

            Toast.makeText(getActivity(), topNumber.toString() + ", " + que.toString(), Toast.LENGTH_SHORT).show();
        }
        // Inflate the layout for this fragment
        return view;
    }

   // private void LoadDataSales(){
  //      itemSalesDataModel.clear();
   //     itemSalesDataModel.add(new ItemSalesDataModel("Item1", 20));
   //     itemSalesDataModel.add(new ItemSalesDataModel("Item2", 27));
   //     itemSalesDataModel.add(new ItemSalesDataModel("Item3", 16));
   //     itemSalesDataModel.add(new ItemSalesDataModel("Item4", 12));
   // }
}