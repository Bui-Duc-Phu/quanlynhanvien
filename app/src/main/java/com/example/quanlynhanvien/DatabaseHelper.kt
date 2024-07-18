package com.example.quanlynhanvien

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "QLNHANVIEN.db"

        // Table names
        private const val TABLE_NHANVIEN = "NHANVIEN"
        private const val TABLE_PHONGBAN = "PHONGBAN"
        private const val TABLE_CHUCVU = "CHUCVU"

        // NHANVIEN table columns
        private const val COLUMN_MANV = "MANV"
        private const val COLUMN_HOTEN = "HOTEN"
        private const val COLUMN_PHAI = "PHAI"
        private const val COLUMN_NGAYSINH = "NGAYSINH"
        private const val COLUMN_DIENTHOAI = "DIENTHOAI"
        private const val COLUMN_NGAYVAOLAM = "NGAYVAOLAM"
        private const val COLUMN_PHONG = "PHONG"
        private const val COLUMN_CHUCVU = "CHUCVU"
        private const val COLUMN_LUONG = "LUONG"
        private const val COLUMN_HESOLUONG = "HESOLUONG"
        private const val COLUMN_PHUCAP = "PHUCAP"

        // PHONGBAN table columns
        private const val COLUMN_MAPHONG = "MAPHONG"
        private const val COLUMN_TENPHONG = "TENPHONG"

        // CHUCVU table columns
        private const val COLUMN_MACV = "MACV"
        private const val COLUMN_TENCV = "TENCV"

        // Data for PHONGBAN table
        private val PHONGBAN_DATA = listOf(
            "Kế toán", "Nhân sự", "Vận hành", "Marketing", "Nghiên cứu", "Công nghệ"
        )

        // Data for CHUCVU table
        private val CHUCVU_DATA = listOf(
            "Giám đốc", "Trưởng phòng", "Nhân viên", "Chủ tịch", "Phó giám đốc", "Phó phòng"
        )
    }

    override fun onCreate(db: SQLiteDatabase) {

        val createTableNhanVien = "CREATE TABLE $TABLE_NHANVIEN (" +
                "$COLUMN_MANV INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_HOTEN TEXT," +
                "$COLUMN_PHAI TEXT," +
                "$COLUMN_NGAYSINH TEXT," +
                "$COLUMN_DIENTHOAI TEXT," +
                "$COLUMN_NGAYVAOLAM TEXT," +
                "$COLUMN_PHONG TEXT," +
                "$COLUMN_CHUCVU TEXT," +
                "$COLUMN_LUONG TEXT," +   // Comma added here
                "$COLUMN_HESOLUONG TEXT," + // Comma added here
                "$COLUMN_PHUCAP TEXT)"

        val createTablePhongBan = "CREATE TABLE $TABLE_PHONGBAN (" +
                "$COLUMN_MAPHONG INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_TENPHONG TEXT)"

        val createTableChucVu = "CREATE TABLE $TABLE_CHUCVU (" +
                "$COLUMN_MACV INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_TENCV TEXT)"

        db.execSQL(createTableNhanVien)
        db.execSQL(createTablePhongBan)
        db.execSQL(createTableChucVu)

        // Insert data into PHONGBAN table
        PHONGBAN_DATA.forEach { tenPhong ->
            val values = ContentValues().apply {
                put(COLUMN_TENPHONG, tenPhong)
            }
            db.insert(TABLE_PHONGBAN, null, values)
        }

        // Insert data into CHUCVU table
        CHUCVU_DATA.forEach { tenChucVu ->
            val values = ContentValues().apply {
                put(COLUMN_TENCV, tenChucVu)
            }
            db.insert(TABLE_CHUCVU, null, values)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NHANVIEN")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PHONGBAN")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CHUCVU")
        onCreate(db)
    }

    @SuppressLint("Range")
    fun getAllNhanVien(): List<NhanVien> {
        val nhanViens = mutableListOf<NhanVien>()
        val query = "SELECT * FROM $TABLE_NHANVIEN"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)
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
        db.close()
        return nhanViens
    }


}
