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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class Pie_Chart_Fragment extends Fragment {

    ArrayList<PieEntry> classValues;
    ArrayList<ItemSalesDataModel> itemSalesDataModel = new ArrayList<>();

    String tittle, pickAdate, filtro;
    Integer topNumber, que, topBottom, queGrafico;
    PieChart pieChart;
    View view;
    DatabaseHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pie__chart_, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            topNumber = bundle.getInt("topNumber");
            que = bundle.getInt("que");
            topBottom = bundle.getInt("topBottom");
            tittle = bundle.getString("tittle");
            pickAdate = bundle.getString("date");
            queGrafico = bundle.getInt("queGrafico");
            filtro = bundle.getString("filtro");
            getActivity().setTitle(tittle);

            pieChart = view.findViewById(R.id.pieChart);

            classValues = new ArrayList<>();
            db = new DatabaseHelper(getContext());

            switch (queGrafico){
                case 1:
                    itemSalesDataModel = db.LoadDataSalesClasses(que, topNumber, topBottom, pickAdate, 1, filtro);
                    break;
                case 2:
                    itemSalesDataModel = db.LoadDataSalesClasses(que, topNumber, topBottom, pickAdate, 2, filtro);
                    break;
                case 3:
                    itemSalesDataModel = db.LoadDataSalesClasses(que, topNumber, topBottom, pickAdate, 3, filtro);
                    break;
            }

            for(int i = 0; i < itemSalesDataModel.size(); i ++){
                String className = itemSalesDataModel.get(i).getItemname();
                Integer classValue = itemSalesDataModel.get(i).getItemvalue();

                classValues.add(new PieEntry(classValue, className + "\n" + classValue));
            }

            PieDataSet piedataset = new PieDataSet(classValues, "Classes");
            piedataset.setColors(ColorTemplate.MATERIAL_COLORS);
            piedataset.setValueTextColor(Color.BLACK);
            piedataset.setValueTextSize(12f);

            PieData piedata = new PieData(piedataset);

            pieChart.setData(piedata);
            pieChart.setUsePercentValues(true);
            pieChart.getDescription().setEnabled(true);
            pieChart.getDescription().setText("Top performing classes");

            pieChart.setCenterText("Classes");
            pieChart.animate();
            pieChart.invalidate();
        }
            return view;
    }
}