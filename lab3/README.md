# BÁO CÁO BÀI THỰC HÀNH LAB 3

## 1. Thông tin sinh viên

- **Họ và tên:** Nguyễn Hưng Văn Khánh
- **MSSV:** 1150080021
- **Lớp:** 11 ĐH THMT

## 2. Tên bài Lab

Mã hóa khóa công khai bằng Java Swing, Maven và SQL Server: RSA, ECC, hệ mã lai RSA + AES và chữ ký số ECDSA.

## 3. Công cụ sử dụng

- **VS Code:** Viết mã nguồn và chạy chương trình Java.
- **Java JDK 17 và Maven:** Biên dịch và quản lý thư viện.
- **Java Swing:** Xây dựng giao diện.
- **SQL Server và SSMS:** Lưu, xem và thống kê lịch sử thao tác.
- **Microsoft JDBC Driver:** Kết nối Java với SQL Server.

## 4. Nội dung và kết quả

- Tạo project `LAB3_MaHoaKhoaCongKhai`, package `week_03`.
- RSA-OAEP-SHA256 với khóa 2048/3072 bit, chạy trên Console và giao diện Swing.
- ECC sử dụng ECDH P-256, HKDF-SHA256 và AES-256-GCM.
- Hệ mã lai RSA-OAEP + AES-256-GCM mã hóa văn bản trên 1.000 ký tự.
- Chữ ký SHA256withECDSA, kiểm tra thông điệp bị sửa và khóa công khai không khớp.
- Lưu/nạp khóa thử nghiệm và xác minh chữ ký chỉ với khóa công khai.
- Menu mở các form; bảng `dbo.History` ghi thuật toán, thao tác, độ dài đầu vào và kết quả thành công/thất bại.
- Kiểm tra tự động 30 trường hợp thuật toán; kiểm tra kết nối JDBC và quyền đọc/ghi lịch sử.

## 5. Hướng dẫn chạy

Mã nguồn nằm trong `LAB13/LAB3_MaHoaKhoaCongKhai`. Mở Terminal tại thư mục có `pom.xml`:

```bat
mvn clean package
java -cp "target/classes;target/dependency/*" week_03.frm_Menu
```

Chạy bài RSA Console:

```bat
java -cp target/classes week_03.RSADemo
```

Chạy kiểm tra thuật toán sau khi build:

```bat
java -cp "target/classes;target/test-classes" week_03.Lab3SelfTest
```

Trên máy thực hành ban đầu có thể mở `LAB13/run-java.cmd`; file này sử dụng đường dẫn JDK/Maven cài trên máy đó. Máy khác dùng các lệnh ở trên.

Trong SSMS, dùng tài khoản có quyền tạo database/login để chạy lần lượt `01_Tao_CSDL.sql` và `02_Tao_TaiKhoan_LAB.sql` trong thư mục `03_SQL_SERVER`. Script tài khoản dùng mật khẩu mẫu cho môi trường thực hành; không tái sử dụng cho hệ thống thật. Sau đó mở form kết nối SQL, nhập server, port, database, tài khoản và mật khẩu rồi bấm **Test Connection**. Mỗi lần khởi động lại chương trình cần kết nối lại.

Giao diện được viết bằng Swing trong VS Code, không có tệp thiết kế NetBeans `.form`. Các tệp build và tệp chứa khóa riêng được loại khỏi kho. `ecdsa_public.txt` chỉ chứa khóa công khai thử nghiệm.

## 6. Báo cáo

[Báo cáo Word LAB3](LAB13/Lab13_Lop11THMT_1150080021_NguyenHungVanKhanh.docx)
