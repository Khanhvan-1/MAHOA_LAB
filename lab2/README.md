BÁO CÁO BÀI THỰC HÀNH LAB 2

1. Thông tin sinh viên
- Họ và tên: Nguyễn Hưng Văn Khánh
- MSSV: 1150080021
- Lớp: 11 ĐH THMT

2. Tên bài Lab
Mã hóa đối xứng hiện đại bằng Java Swing, Maven, SQL Server và Python.

3. Công cụ sử dụng
- Java JDK 17 hoặc 21 và Maven: Xây dựng và chạy chương trình Java.
- Apache NetBeans / IDE Java: Xây dựng giao diện Java Swing và quản lý project Maven.
- Java Swing: Thiết kế giao diện mã hóa, giải mã, đăng ký, đăng nhập và xem lịch sử.
- SQL Server Express và SSMS: Quản lý tài khoản ứng dụng và lịch sử thao tác mã hóa.
- Microsoft JDBC Driver: Kết nối ứng dụng Java với SQL Server.
- Bouncy Castle: Hỗ trợ thuật toán Twofish.
- Python 3 và Tkinter: Xây dựng bài nâng cao AES-CBC có giao diện.
- Thư viện Python cryptography: Triển khai mã hóa AES-CBC.

4. Nội dung và kết quả
- Tạo project Maven LAB2_MaHoaHienDai với package week_02.
- Xây dựng các chức năng mã hóa và giải mã: DES, TripleDES (3DES), AES, RC4 và Twofish.
- Thực hiện bài nâng cao AES-CTR bằng Java và AES-256-CBC bằng Python (Tkinter).
- Xử lý dữ liệu UTF-8, Base64, HEX, khóa, IV, nonce và padding theo từng thuật toán.
- Hỗ trợ mở và lưu file văn bản; lưu kèm IV/nonce khi cần thiết để giải mã lại.
- Xây dựng chức năng đăng ký, đăng nhập, tạo ticket phiên làm việc và băm mật khẩu bằng PBKDF2 kết hợp salt.
- Kết nối SQL Server, lưu tài khoản vào bảng dbo.app_users và lưu thông tin lịch sử thao tác vào bảng dbo.crypto_history.
- Tạo giao diện menu chính frm_Menu để mở các chức năng mã hóa, kết nối SQL Server và xem lịch sử.
- Thực hiện các kịch bản kiểm thử mã hóa–giải mã, kiểm tra khóa không hợp lệ, file, đăng nhập và các bài nâng cao theo yêu cầu Lab 2.

5. Ghi chú
- Các thuật toán DES, 3DES và RC4 được triển khai để học tập, không khuyến nghị sử dụng để bảo vệ dữ liệu thực tế.
- Khi sử dụng đăng ký/đăng nhập và lịch sử, cần khởi động SQL Server, tạo CSDL LAB2_MaHoaHienDai và cấu hình kết nối trong ứng dụng.
- Không đưa mật khẩu thực tế, file .env, thông tin đăng nhập cá nhân hoặc dữ liệu nhạy cảm lên GitHub.
- Phần Java được chạy bằng Maven; phần AES-CBC Python được chạy riêng.
Lệnh chạy chương trình Java (trong thư mục có pom.xml):
mvn clean package
mvn exec:java "-Dexec.mainClass=week_02.frm_Menu"
Lệnh chạy bài AES-CBC bằng Python (trong thư mục có aes_cbc_gui.py):
py -m pip install cryptography
py aes_cbc_gui.py

6. Báo cáo
Báo cáo thực hành Lab 2 và các tệp mã nguồn được lưu trong thư mục lab2 của repository MAHOA_LAB.