package com.example.appbanmypham.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appbanmypham.R;
import com.example.appbanmypham.adapter.DuongTheAdapter;
import com.example.appbanmypham.model.SanPhamMoi;
import com.example.appbanmypham.retrofit.ApiBanHang;
import com.example.appbanmypham.retrofit.RetrofitClient;
import com.example.appbanmypham.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    EditText edtsearch;
    DuongTheAdapter adapterDt;
    List<SanPhamMoi> sanPhamMoiList;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        ActionToolBar();
    }

    private void initView() {
        sanPhamMoiList = new ArrayList<>();
        // Khởi tạo Retrofit và API
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);

        //anh xa
        edtsearch = findViewById(R.id.edtsearch);
        toolbar = findViewById(R.id.toobar);
        // Cấu hình
        recyclerView = findViewById(R.id.recycleview_search);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        // Thiết lập sự kiện cho ô nhập liệu tìm kiếm (EditText)
        edtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence , int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0){
                    sanPhamMoiList.clear();
                    adapterDt = new DuongTheAdapter(getApplicationContext(), sanPhamMoiList);
                    recyclerView.setAdapter(adapterDt);// Đặt adapter cho RecyclerView để hiển thị danh sách rỗng
                }else {
                    // Gọi phương thức để lấy dữ liệu tìm kiếm
                    getDataSearch(charSequence.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });



    }

    private void getDataSearch(String s) {
        sanPhamMoiList.clear();
        compositeDisposable.add(apiBanHang.search(s)/// Gọi API để tìm kiếm sản phẩm
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if (sanPhamMoiModel.isSuccess()){
                                sanPhamMoiList = sanPhamMoiModel.getResult();
                                adapterDt = new DuongTheAdapter(getApplicationContext(), sanPhamMoiList);
                                recyclerView.setAdapter(adapterDt);
                            }else {
                                Toast.makeText(getApplicationContext(), sanPhamMoiModel.getMessage(), Toast.LENGTH_SHORT).show();
                                sanPhamMoiList.clear();
                                adapterDt.notifyDataSetChanged();// Cập nhật lại adapter để hiển thị danh sách rỗng
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                ));
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        //Quay lai
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}