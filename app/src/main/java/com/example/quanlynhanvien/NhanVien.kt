package com.example.quanlynhanvien

data class NhanVien(
    var maNV: Int? = null, // Mã số nhân viên (sẽ tự động tăng trong CSDL SQLite)
    var hoTen: String = "", // Họ và tên nhân viên
    var phai: String = "", // Giới tính
    var ngaySinh: String = "", // Ngày sinh (định dạng dd/MM/yyyy)
    var dienThoai: String = "", // Điện thoại
    var ngayVaoLam: String = "", // Ngày vào làm (định dạng dd/MM/yyyy)
    var phong: String = "", // Mã phòng ban (liên kết với PHONGBAN table)
    var chucVu: String = "" ,// Mã chức vụ (liên kết với CHUCVU table)\
    var luong: String = "" ,
    var phucap: String = "" ,
    var hesoluong: String = "" ,

)
