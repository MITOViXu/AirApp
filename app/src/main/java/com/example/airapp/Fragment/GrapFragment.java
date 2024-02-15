package com.example.airapp.Fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.airapp.ApiClient;
import com.example.airapp.BodyChart;
import com.example.airapp.LogIn.InterfaceChart;
import com.example.airapp.R;
import com.example.airapp.ReponseChart;
import com.example.airapp.URL;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GrapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GrapFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String[] items = {"humidity", "rainfall", "temperature", "windSpeed" };
    String[] iterm2 = {"week", "month", "year"};
    String attribute = "humidity";
    String timeType = "week";
    AutoCompleteTextView autoCompleteTextView;
    AutoCompleteTextView autoCompleteTextView2;
    ArrayAdapter<String> addapterItems;
    ArrayAdapter<String> addapterItems2;
    String accessToken;
    EditText endingDay;
    String authorization;
    InterfaceChart interfaceChart;
    TextView tvChart;
    private LineChart chart;
    private float lineWidth = 4f,
            valueTextSize = 10f;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GrapFragment() {
        // Required empty public constructor
    }
    public JsonObject returnJson(BodyChart bodyChart)
    {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("fromTimestamp", bodyChart.getFromTimestamp());
        jsonObject.addProperty("toTimestamp", bodyChart.getToTimestamp());
        jsonObject.addProperty("fromTime", bodyChart.getFromTime());
        jsonObject.addProperty("toTime", bodyChart.getToTime());
        jsonObject.addProperty("type", bodyChart.getType());
        return jsonObject;
    }
    //(String assetId, String attributeName, String Authorization, JsonObject jsonObject)
    public void call(String attribute, String timeType)
    {
        BodyChart bodyChart = new BodyChart(timeType);
        JsonObject jsonObject = returnJson(bodyChart);
        Retrofit retrofit = ApiClient.getClient(URL.mainURL);
        interfaceChart = retrofit.create(InterfaceChart.class);
        Call<List<ReponseChart>> call = interfaceChart.getChart("5zI6XqkQVSfdgOrZ1MyWEf", attribute, authorization, jsonObject);
        call.enqueue(new Callback<List<ReponseChart>>() {
            @Override
            public void onResponse(Call<List<ReponseChart>> call, Response<List<ReponseChart>> response) {
                if(response.isSuccessful())
                {

                    if(response.body().equals(null))
                    {
                        Toast.makeText(requireContext(), "Null: " + attribute + timeType, Toast.LENGTH_SHORT).show();

                    }
                    {
                        List<ReponseChart> reponseChartList = response.body();
                        Toast.makeText(requireContext(), "success" + reponseChartList.size(), Toast.LENGTH_SHORT).show();
                        int count = 25;
                        int step = reponseChartList.size()/count;
                        List<ReponseChart> finalChart = new ArrayList<>();
                        for (int i = 0; i < reponseChartList.size(); i += step)
                        {
                            finalChart.add(reponseChartList.get(i));
                        }

                        DrawLineChart(finalChart, timeType);
                    }
                }
            }
            @Override
            public void onFailure(Call<List<ReponseChart>> call, Throwable t) {

                tvChart.setText("gà");
            }
        });
    }

    public void call(String attribute, String timeType, String toTime)
    {
        BodyChart bodyChart = new BodyChart(timeType, toTime);
        JsonObject jsonObject = returnJson(bodyChart);

        Retrofit retrofit = ApiClient.getClient(URL.mainURL);
        interfaceChart = retrofit.create(InterfaceChart.class);
        Call<List<ReponseChart>> call = interfaceChart.getChart("5zI6XqkQVSfdgOrZ1MyWEf", "rainfall", authorization, jsonObject);
        call.enqueue(new Callback<List<ReponseChart>>() {
            @Override
            public void onResponse(Call<List<ReponseChart>> call, Response<List<ReponseChart>> response) {
                if(response.isSuccessful())
                {
                    List<ReponseChart> reponseChartList = response.body();
                    //vẽ ở đây
                    Toast.makeText(requireContext(), "call: " + attribute + timeType, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    tvChart.setText("sai");
                }
            }
            @Override
            public void onFailure(Call<List<ReponseChart>> call, Throwable t) {

                tvChart.setText("gà");
            }
        });
    }

    private void DrawLineChart(List<ReponseChart> chartList, String timeType) {
        int displayedElements = 30;
        String pattern = "HH:mm";
        if(chartList == null){
            return;
        }
        if (timeType.equals("week")) {
            pattern = "dd:HH";
        } else if (timeType.equals("month")) {
            pattern = "MM:dd";
        } else if (timeType.equals("year")) {
            pattern = "yyyy:MM";
        }

        final String finalPattern = pattern;

        int totalElements = chartList.size();
        int step = totalElements/displayedElements;

        List<Entry> entries = new ArrayList<>();
        for (int i = chartList.size() - 1; i >= 0; i--) {
            entries.add(new Entry(chartList.get(i).getX(), chartList.get(i).getY()));
        }

        LineDataSet lineDataSet = new LineDataSet(entries, attribute);

        lineDataSet.setLineWidth(3f);
        lineDataSet.setValueTextSize(10f);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);
        LineData data = new LineData(dataSets);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new ValueFormatter() {
            private final SimpleDateFormat format = new SimpleDateFormat(finalPattern, Locale.getDefault());

            @Override
            public String getFormattedValue(float value) {
                // Chuyển giá trị trục hoành (milliseconds từ epoch) thành chuỗi ngày tháng
                long timestamp = (long) value;
                return format.format(new Date(timestamp));
            }
        });

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setGranularity(10f);

        chart.setData(data);
        chart.invalidate();
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GrapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GrapFragment newInstance(String accessToken) {
        GrapFragment fragment = new GrapFragment();
        Bundle args = new Bundle();
        args.putString("access_token", accessToken);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.e("Hello", "Fragment Graph");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        // Lấy access_token từ Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            accessToken = bundle.getString("access_token");
            authorization = "Bearer " + accessToken;
            // Sử dụng access_token theo cách bạn cần
            Log.d("Graph AccessTtoken", "Received access_token: " + accessToken);
        }
    }
    private void openDialog(){
        DatePickerDialog dia = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int mo, int day) {
                endingDay.setText((year) + "." + String.valueOf(mo) + "." + String.valueOf(day));
            }
        }, 2023, 1, 15);
        dia.show();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_grap, container, false);

        tvChart = view.findViewById(R.id.textView22);
        autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView2 = view.findViewById(R.id.autoCompleteTextView2);
        chart = view.findViewById(R.id.lineChart);
        chart.setNoDataText("");
        addapterItems = new ArrayAdapter<String>(requireContext(), R.layout.dropdown_item, items);
        addapterItems2 = new ArrayAdapter<String>(requireContext(), R.layout.dropdown_item, iterm2);
        autoCompleteTextView.setAdapter(addapterItems);
        autoCompleteTextView2.setAdapter(addapterItems2);
        endingDay= view.findViewById(R.id.editTextText);
        call(attribute, timeType);
        endingDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                attribute = item;
                call(attribute, timeType);
            }
        });
        autoCompleteTextView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                timeType = item;
                call(attribute, timeType);
            }
        });

        ImageButton logout = view.findViewById(R.id.imageButton2);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().finish();
            }
        });
        return view;
    }
}