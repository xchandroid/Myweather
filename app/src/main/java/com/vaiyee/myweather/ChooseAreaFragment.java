package com.vaiyee.myweather;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vaiyee.myweather.db.City;
import com.vaiyee.myweather.db.County;
import com.vaiyee.myweather.db.Sheng;
import com.vaiyee.myweather.util.HttpUtil;
import com.vaiyee.myweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class ChooseAreaFragment extends Fragment {
    private static final String TAG = "ChooseAreaFragment";

    public static final int LEVEL_PROVINCE = 0;

    public static final int LEVEL_CITY = 1;

    public static final int LEVEL_COUNTY = 2;

    private ProgressDialog progressDialog;

    private TextView titleText;

    private Button backButton;

    private ListView listView;

    private ArrayAdapter<String> adapter;

    private List<String> dataList = new ArrayList<>();

    /**
     * 省列表
     */
    private List<Sheng> provinceList;

    /**
     * 市列表
     */
    private List<City> cityList;

    /**
     * 县列表
     */
    private List<County> countyList;

    /**
     * 选中的省份
     */
    private Sheng selectedProvince;

    /**
     * 选中的城市
     */
    private City selectedCity;

    /**
     * 当前选中的级别
     */
    private int currentLevel;


    public ChooseAreaFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area,container,false);
        titleText = (TextView) view.findViewById(R.id.title_text);
        backButton = (Button)view.findViewById(R.id.back);
        listView = (ListView)view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (currentLevel==LEVEL_PROVINCE)
                {
                    selectedProvince = provinceList.get(i);
                    queryCity();
                }else if (currentLevel==LEVEL_CITY)
                {
                    selectedCity = cityList.get(i);
                    queryCounty();
                }
                else if (currentLevel == LEVEL_COUNTY)
                {
                    String weatherId = countyList.get(i).getWeatherId();
                    Intent intent =new Intent(getActivity(),WeatherActivity.class);
                    intent.putExtra("weather_id",weatherId);
                    startActivity(intent );
                    getActivity().finish();
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentLevel==LEVEL_CITY)
                {
                    queryProvince();
                }else if (currentLevel==LEVEL_COUNTY)
                {
                    queryCity();
                }
            }
        });
        queryProvince();
    }
//查询全国所有的省，先从数据库查询，如果数据库中没有再到服务器上查询
    private void queryProvince() {
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        provinceList=DataSupport.findAll(Sheng.class);
        if (provinceList.size()>0)
        {
            dataList.clear();
            for (Sheng s:provinceList)
            {
                dataList.add(s.getShengName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_PROVINCE;
        }
        else
        {
            String address = "http://guolin.tech/api/china";
            queryFromServer(address,"province");
        }
    }
       //根据传入的地址和类型查询省、市、县的信息
    private void queryFromServer(String address, final String type) {
        showProgressDialog();
        HttpUtil.sendOkhttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
               getActivity().runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       closeProgressDialog();
                       Toast.makeText(getContext(),"加载失败了喔",Toast.LENGTH_SHORT).show();
                   }
               });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseText = response.body().string();
                boolean result =false;
                if ("province".equals(type))
                {
                    result = Utility.handleProvinResponse(responseText);
                }else if ("city".equals(type))
                {
                    result = Utility.handleCityResponse(responseText,selectedProvince.getId());
                }
                else if ("county".equals(type))
                {
                    result = Utility.handleCountyResponse(responseText,selectedCity.getId());
                }
                if (result)
                {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type))
                            {
                                queryProvince();
                            }
                            else if ("city".equals(type))
                            {
                                queryCity();
                            }
                            else if ("county".equals(type))
                            {
                                queryCounty();
                            }
                        }
                    });
                }
            }
        });

    }

    //显示进度条对话框
    private void showProgressDialog() {
        if (progressDialog==null)
        {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载数据...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    //关闭进度条对话框
    private void closeProgressDialog()
    {
        if (progressDialog != null)
        {
            progressDialog.dismiss();
        }
    }

    //查询选中的市的所有县，先从数据库查询，如果数据库中没有再到服务器上查询
    private void queryCounty() {
        titleText.setText(selectedCity.getCityName());
        String []a =new String[]{"钦州","灵山","浦北"};
        backButton.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("cityid = ?",String.valueOf(selectedCity.getId())).find(County.class);
        if (countyList.size()>0) {
            dataList.clear();
            if (selectedCity.getCityCode() == 236) {
                for (int j = 0; j <3; j++) {
                    dataList.add(a[j]);
                }
                adapter.notifyDataSetChanged();
                listView.setSelection(0);
                currentLevel = LEVEL_COUNTY;
            } else {
                for (County county : countyList) {
                    dataList.add(county.getCountyName());
                }
                adapter.notifyDataSetChanged();
                listView.setSelection(0);
                currentLevel = LEVEL_COUNTY;
            }
        }
        else
        {
            int provinceCode = selectedProvince.getShengdaihao();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/"+ provinceCode+ "/"+cityCode;
            queryFromServer(address,"county");
        }
    }


    //查询选中的省的所有城市，先从数据库查询，如果数据库中没有再到服务器上查询
    private void queryCity() {
        titleText.setText(selectedProvince.getShengName());
        backButton.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("provinceid=?", String.valueOf(selectedProvince.getId())).find(City.class);
        if (cityList.size() > 0) {
            dataList.clear();
            for (City c : cityList) {
                dataList.add(c.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        } else
        {
            int provinceCode = selectedProvince.getShengdaihao();
            String address = "http://guolin.tech/api/china/"+provinceCode;
            queryFromServer(address,"city");
        }
    }
}
