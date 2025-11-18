package com.example.newbt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper database;
    ListView lvCongViec;
    ArrayList<CongViec> arrayCongViec;
    CongViecAdapter adapter;
    FloatingActionButton fabThem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ
        lvCongViec = findViewById(R.id.listViewCongViec);
        fabThem = findViewById(R.id.fabThem);
        arrayCongViec = new ArrayList<>();

        // Setup adapter
        adapter = new CongViecAdapter(this, R.layout.list_item_cong_viec, arrayCongViec);
        lvCongViec.setAdapter(adapter);

        // Tạo database "ghichu.db"
        database = new DatabaseHelper(this, "ghichu.db", null, 1);

        // Tạo bảng CongViec
        database.QueryData("CREATE TABLE IF NOT EXISTS CongViec(Id INTEGER PRIMARY KEY AUTOINCREMENT, TenCV VARCHAR(200))");

        // Lấy dữ liệu và hiển thị lên listview
        GetDataCongViec();

        // Bắt sự kiện thêm
        fabThem.setOnClickListener(view -> DialogThem());
    }

    private void GetDataCongViec() {
        Cursor dataCongViec = database.GetData("SELECT * FROM CongViec");
        arrayCongViec.clear();
        while (dataCongViec.moveToNext()) {
            int id = dataCongViec.getInt(0);
            String ten = dataCongViec.getString(1);
            arrayCongViec.add(new CongViec(id, ten));
        }
        adapter.notifyDataSetChanged();
    }

    private void DialogThem() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_them_cong_viec);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        EditText edtTen = dialog.findViewById(R.id.editTextTenCongViec);
        Button btnThem = dialog.findViewById(R.id.buttonThem);
        Button btnHuy = dialog.findViewById(R.id.buttonHuy);

        btnThem.setOnClickListener(v -> {
            String tencv = edtTen.getText().toString();
            if (tencv.isEmpty()) {
                Toast.makeText(MainActivity.this, "Vui lòng nhập tên công việc!", Toast.LENGTH_SHORT).show();
            } else {
                database.QueryData("INSERT INTO CongViec VALUES(null, '" + tencv + "')");
                Toast.makeText(MainActivity.this, "Đã thêm!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                GetDataCongViec();
            }
        });

        btnHuy.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    public void DialogSuaCongViec(String ten, int id) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_sua_cong_viec);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        EditText edtTenCV = dialog.findViewById(R.id.editTextSuaTenCV);
        Button btnXacNhan = dialog.findViewById(R.id.buttonXacNhan);
        Button btnHuy = dialog.findViewById(R.id.buttonHuySua);

        edtTenCV.setText(ten);

        btnXacNhan.setOnClickListener(v -> {
            String tenMoi = edtTenCV.getText().toString().trim();
            database.QueryData("UPDATE CongViec SET TenCV = '" + tenMoi + "' WHERE Id = " + id);
            Toast.makeText(MainActivity.this, "Đã cập nhật", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            GetDataCongViec();
        });

        btnHuy.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    public void DialogXoaCongViec(String tencv, int id) {
        AlertDialog.Builder dialogXoa = new AlertDialog.Builder(this);
        dialogXoa.setMessage("Bạn có chắc muốn xóa công việc '" + tencv + "' không?");
        dialogXoa.setPositiveButton("Có", (dialog, which) -> {
            database.QueryData("DELETE FROM CongViec WHERE Id = " + id);
            Toast.makeText(MainActivity.this, "Đã xóa", Toast.LENGTH_SHORT).show();
            GetDataCongViec();
        });
        dialogXoa.setNegativeButton("Không", null);
        dialogXoa.show();
    }
}