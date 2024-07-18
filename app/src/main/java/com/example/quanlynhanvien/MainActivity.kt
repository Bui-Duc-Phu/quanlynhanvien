    package com.example.quanlynhanvien

    import android.annotation.SuppressLint
    import android.app.AlertDialog
    import android.app.DatePickerDialog
    import android.content.ContentValues
    import android.database.Cursor
    import android.database.sqlite.SQLiteDatabase
    import android.os.Bundle
    import android.view.View
    import android.widget.AdapterView
    import android.widget.ArrayAdapter
    import android.widget.Spinner
    import android.widget.Toast
    import androidx.appcompat.app.AppCompatActivity
    import com.example.quanlynhanvien.databinding.ActivityMainBinding
    import java.text.SimpleDateFormat
    import java.util.Calendar
    import java.util.Locale

    class MainActivity : AppCompatActivity() {

        private lateinit var binding: ActivityMainBinding
        private lateinit var dbHelper: DatabaseHelper
        private lateinit var db: SQLiteDatabase

        private var ngaySinh: Calendar = Calendar.getInstance()
        private var ngayVaoLam: Calendar = Calendar.getInstance()

        private lateinit var list: List<NhanVien>

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            dbHelper = DatabaseHelper(this)
            db = dbHelper.writableDatabase

            init_()
            setupButtonListeners()


            list = dbHelper.getAllNhanVien()
            listNhanVien()
        }

        private fun init_() {
            setUpPhongBan()
            setupDateSpinner(binding.spinnerNgaySinh, ngaySinh)
            setupDateSpinner(binding.spinnerNgayVaoLam, ngayVaoLam)

            binding.nut5Exit.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Xác nhận thoát")
                builder.setMessage("Bạn có chắc chắn muốn thoát khỏi ứng dụng?")
                builder.setPositiveButton("Thoát") { _, _ ->
                    finishAffinity() // Đóng tất cả các activity và thoát ứng dụng
                }
                builder.setNegativeButton("Hủy") { dialog, _ ->
                    dialog.dismiss() // Đóng dialog nếu người dùng không muốn thoát
                }
                val dialog = builder.create()
                dialog.show()
            }

        }

        private fun listNhanVien() {
            val adapter = Adapter(this,list)
            binding.ListNV.adapter = adapter
        }

        private fun setUpPhongBan() {
            val phongBanList = listOf("Kế toán", "Nhân sự", "Vận hành", "Marketing", "Nghiên cứu", "Công nghệ")

            val phongBanAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, phongBanList)
            phongBanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerPhong.adapter = phongBanAdapter
        }
        private fun setUpChucVu() {
            val phongBanList = listOf("Kế toán", "Nhân sự", "Vận hành", "Marketing", "Nghiên cứu", "Công nghệ")

            val phongBanAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, phongBanList)
            phongBanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        }

        @SuppressLint("ClickableViewAccessibility")
        private fun setupDateSpinner(spinner: Spinner, calendar: Calendar) {
            // Thiết lập ngày mặc định
            calendar.set(Calendar.DAY_OF_MONTH, 15)
            calendar.set(Calendar.MONTH, Calendar.JULY)

            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val defaultDate = dateFormat.format(calendar.time)

            val dateList = mutableListOf(defaultDate)
            val dateAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, dateList)
            dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = dateAdapter

            // Thiết lập chọn ngày khi nhấn vào Spinner
            spinner.setOnTouchListener { _, _ ->
                showDatePickerDialog(spinner, calendar, dateList, dateAdapter)
                true
            }

            // Thiết lập sự kiện khi chọn mục trong Spinner
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    // Không làm gì cả
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Không làm gì cả
                }
            }


        }

        private fun showDatePickerDialog(
            spinner: Spinner,
            calendar: Calendar,
            dateList: MutableList<String>,
            dateAdapter: ArrayAdapter<String>
        ) {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                calendar.set(selectedYear, selectedMonth, selectedDay)

                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val selectedDate = dateFormat.format(calendar.time)

                dateList.clear()
                dateList.add(selectedDate)
                dateAdapter.notifyDataSetChanged()
            }, year, month, day)

            // Thiết lập sự kiện khi dialog đóng
            datePickerDialog.setOnDismissListener {
                // Cập nhật giá trị vào biến ngày tháng chính
                if (spinner == binding.spinnerNgaySinh) {
                    ngaySinh = calendar
                } else if (spinner == binding.spinnerNgayVaoLam) {
                    ngayVaoLam = calendar
                }
            }

            datePickerDialog.show()
        }

        private fun setupButtonListeners() {
            binding.nut1Tinhtien.setOnClickListener {
                // Lấy thông tin từ giao diện để tính toán lương và hiển thị
                val heSoLuong = binding.heSoLuong.text.toString().toDoubleOrNull() ?: 0.0
                val phuCap = binding.phuCap.text.toString().toDoubleOrNull() ?: 0.0
                val thamNien = calculateThamNien(binding.spinnerNgayVaoLam.selectedItem.toString())

                val luong = calculateLuong(heSoLuong, phuCap, thamNien)
                binding.luong.setText(luong.toString())
            }

            binding.nut2AddNhanVien.setOnClickListener {
                // Thêm thông tin nhân viên vào CSDL
                val nhanVien = NhanVien(
                    hoTen = binding.hoTen.text.toString(),
                    phai = "", // Bổ sung khi cần thiết
                    ngaySinh = binding.spinnerNgaySinh.selectedItem.toString(),
                    dienThoai = binding.dienThoai.text.toString(),
                    ngayVaoLam = binding.spinnerNgayVaoLam.selectedItem.toString(),
                    phong = binding.spinnerPhong.selectedItem.toString(), // Ví dụ: Kế toán là phòng 1, Nhân sự là phòng 2, ...
                    chucVu = binding.chucVu.text.toString() ,
                    luong = binding.luong.text.toString(),
                    phucap = binding.phuCap.text.toString(),
                    hesoluong = binding.heSoLuong.text.toString()

                )

                val id = insertNhanVien(nhanVien)
                if (id > 0) {
                    Toast.makeText(this, "Thêm nhân viên thành công", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Thêm nhân viên thất bại", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun calculateThamNien(ngayVaoLam: String): Int {
            // Thực hiện tính thâm niên dựa trên ngày vào làm
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            val yearOfJoining = ngayVaoLam.substring(6).toIntOrNull() ?: currentYear
            return currentYear - yearOfJoining
        }

        private fun calculateLuong(heSoLuong: Double, phuCap: Double, thamNien: Int): Double {
            // Thực hiện tính toán lương dựa trên công thức đã cho
            val lcb = 2340000 // Lương cơ bản
            val thamNienBonus = thamNien * 0.01 * lcb
            val luong = (lcb * heSoLuong * 1.25) * 0.895 + 5800000 + thamNienBonus + phuCap
            return luong
        }

        private fun insertNhanVien(nhanVien: NhanVien): Long {
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put(COLUMN_HOTEN, nhanVien.hoTen)
                put(COLUMN_PHAI, nhanVien.phai)
                put(COLUMN_NGAYSINH, nhanVien.ngaySinh)
                put(COLUMN_DIENTHOAI, nhanVien.dienThoai)
                put(COLUMN_NGAYVAOLAM, nhanVien.ngayVaoLam)
                put(COLUMN_PHONG, nhanVien.phong)
                put(COLUMN_CHUCVU, nhanVien.chucVu)
                put(COLUMN_LUONG, nhanVien.luong)
                put(COLUMN_HESOLUONG, nhanVien.hesoluong)
                put(COLUMN_PHUCAP, nhanVien.phucap)
            }
            val id = db.insert(TABLE_NHANVIEN, null, values)
            db.close()
            return id
        }


        @SuppressLint("Range")
        private fun getAllNhanVien(): List<NhanVien> {
            val nhanViens = mutableListOf<NhanVien>()
            val query = "SELECT * FROM $TABLE_NHANVIEN"
            val cursor: Cursor = db.rawQuery(query, null)
            cursor.use {
                while (it.moveToNext()) {
                    val nhanVien = NhanVien(
                        maNV = it.getInt(it.getColumnIndex(COLUMN_MANV)),
                        hoTen = it.getString(it.getColumnIndex(COLUMN_HOTEN)),
                        phai = it.getString(it.getColumnIndex(COLUMN_PHAI)),
                        ngaySinh = it.getString(it.getColumnIndex(COLUMN_NGAYSINH)),
                        dienThoai = it.getString(it.getColumnIndex(COLUMN_DIENTHOAI)),
                        ngayVaoLam = it.getString(it.getColumnIndex(COLUMN_NGAYVAOLAM)),
                        phong = it.getString(it.getColumnIndex(COLUMN_PHONG)),
                        chucVu = it.getString(it.getColumnIndex(COLUMN_CHUCVU)),
                        luong = it.getString(it.getColumnIndex(COLUMN_LUONG)),       // Thêm trường lương
                        phucap = it.getString(it.getColumnIndex(COLUMN_PHUCAP))  ,    // Thêm trường phụ cấp
                        hesoluong = it.getString(it.getColumnIndex(COLUMN_HESOLUONG))


                    )
                    nhanViens.add(nhanVien)
                }
            }
            return nhanViens
        }

        override fun onDestroy() {
            super.onDestroy()
            db.close()
        }

        companion object {
            private const val TABLE_NHANVIEN = "NHANVIEN"
            private const val COLUMN_MANV = "MANV"
            private const val COLUMN_HOTEN = "HOTEN"
            private const val COLUMN_PHAI = "PHAI"
            private const val COLUMN_NGAYSINH = "NGAYSINH"
            private const val COLUMN_DIENTHOAI = "DIENTHOAI"
            private const val COLUMN_NGAYVAOLAM = "NGAYVAOLAM"
            private const val COLUMN_PHONG = "PHONG"
            private const val COLUMN_CHUCVU = "CHUCVU"
            private const val COLUMN_LUONG = "LUONG"
            private const val COLUMN_PHUCAP = "PHUCAP"
            private const val COLUMN_HESOLUONG = "HESOLUONG"
        }
    }
