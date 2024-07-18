package com.example.quanlynhanvien

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quanlynhanvien.databinding.CustomNhanVienBinding

class Adapter(private val context: Context, private val list: List<NhanVien>) :
    RecyclerView.Adapter<Adapter.ViewHolder>() {

    interface ItemClickListener {
        fun onItemClick(maNV: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CustomNhanVienBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nhanVien = list[position]

        holder.binding.maNV.text = "Mã NV: ${nhanVien.maNV}"
        holder.binding.nameNV.text = "Họ và tên: ${nhanVien.hoTen}"
        holder.binding.phone.text = "SĐT: ${nhanVien.dienThoai}"
        holder.binding.ngaySinh.text = "Ngày sinh: ${nhanVien.ngaySinh}"
        holder.binding.Phong.text = "Phòng: ${nhanVien.phong}"
        holder.binding.chucVu.text = "Chức vụ: ${nhanVien.chucVu}"
        holder.binding.heSoLuong.text = "Hệ số lương: ${nhanVien.hesoluong}"
        holder.binding.phuCap.text = "Phụ cấp: ${nhanVien.phucap}"
        holder.binding.luong.text = "Lương: ${nhanVien.luong}"
        holder.binding.phai.text = "Phái: nam"
        holder.binding.ngayVaoLma.text = "Ngày vào làm: ${nhanVien.ngayVaoLam}"

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val binding: CustomNhanVienBinding) : RecyclerView.ViewHolder(binding.root)
}
