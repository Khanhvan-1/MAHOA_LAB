# BÁO CÁO BÀI THỰC HÀNH LAB 1

## 1. Thông tin sinh viên
- **Họ và tên:** Nguyễn Hưng Văn Khánh
- **MSSV:** 1150080021
- **Lớp:** 11 ĐH THMT

## 2. Tên bài Lab
Mã hóa đối xứng cổ điển bằng Java Swing, Maven và SQL Server.

## 3. Công cụ sử dụng
- **VS Code:** Viết code và chạy chương trình Java.
- **Java và Maven:** Xây dựng chương trình.
- **Java Swing:** Thiết kế giao diện.
- **SQL Server và SSMS:** Lưu và xem lịch sử mã hóa.

## 4. Nội dung và kết quả
- Tạo project `LAB1_MaHoaCoDien` bằng Maven.
- Xây dựng 6 thuật toán: Caesar, Vigenère, Rail Fence, Playfair, Transposition và Autokey.
- Tạo giao diện mã hóa, giải mã và đọc, lưu file văn bản.
- Làm chức năng tổng hợp 6 thuật toán và lưu lịch sử vào SQL Server.
- Thực hiện 3 cách phân tích mật mã: Brute Force Caesar, Frequency Analysis và Known Plaintext.
- Tạo Menu chính gồm 9 nút để mở các chức năng.
- Kiểm tra các chức năng, chương trình chạy ổn định và không báo lỗi.

## 5. Ghi chú
- Bài thực hành được làm trên VS Code bằng Java Swing.
- Chạy chương trình bằng Maven.
- Cần bật SQL Server khi sử dụng chức năng lưu và xem lịch sử.
- Đã kiểm thử các thuật toán và các nút trên Menu chính.

**Lệnh chạy chương trình:**
```bash
mvn clean package
mvn exec:java "-Dexec.mainClass=week_01.frm_Menu"
```

## 6. Báo cáo
`Lab1_Lop11THMT_1150080021_NguyenHungVanKhanh.docx`