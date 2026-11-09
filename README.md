# Quản Lý Bán Hàng (Case Study Module 2 - Java)

Ứng dụng **console** quản lý bán hàng viết bằng Java (Gradle), lưu dữ liệu vào file CSV.
Chủ đề: quản lý danh mục, sản phẩm, khách hàng và đơn hàng của một cửa hàng nhỏ.

## 1. Chức năng

| Menu | Chức năng |
|---|---|
| Category Manager | Thêm, sửa, xóa, xem danh sách danh mục |
| Product Manager | Thêm, sửa, xóa, xem danh sách, **tìm theo tên**, **sắp xếp theo giá tăng dần** |
| Customer Manager | Thêm, sửa, xóa, xem danh sách khách hàng |
| Order Manager | **Tạo đơn hàng**, xem danh sách đơn, xem chi tiết một đơn |

### Quy trình tạo đơn hàng
1. Chọn khách hàng (nhập sai id thì nhập lại).
2. Chọn sản phẩm và số lượng, có thể thêm nhiều sản phẩm (nhập `0` để kết thúc).
3. Chương trình kiểm tra tồn kho, nếu thêm cùng một sản phẩm nhiều lần trong một đơn thì gộp số lượng và kiểm tra theo **tổng số lượng**.
4. Khi chốt đơn: tính tổng tiền, **trừ tồn kho**, ghi đơn vào file.
5. Đơn không có mặt hàng nào thì bị hủy, không ghi file.

## 2. Thực thể và quan hệ

- `Category` (id, name)
- `Product` (id, name, price, stock, categoryId): thuộc một Category
- `Customer` (id, name, phone)
- `Order` (orderId, customerId, date, totalPrice, danh sách `OrderItem`)
- `OrderItem` (orderId, productId, quantity, price)

Quan hệ: Category 1-n Product, Customer 1-n Order, Order n-n Product thông qua `OrderItem`.
Các thực thể tham chiếu nhau bằng **id** (không giữ đối tượng).

Sơ đồ phân tích: [step-1.png](src/main/step-by-step/step-1.png) (nghiệp vụ, giao diện), [step-2.png](src/main/step-by-step/step-2.png) (thực thể), [step-3.png](src/main/step-by-step/step-3.png) (thiết kế hệ thống).

## 3. Kiến trúc

Mô hình nhiều tầng, giống bài mẫu QuanLySanPham:

```
src/main/java
├── entity       Category, Product, Customer, Order, OrderItem
├── repository   IRepository<T> + 4 repository đọc/ghi file CSV
├── service      IService<T> + service nghiệp vụ (Singleton)
├── ui           MainUi, CategoryUi, ProductUi, CustomerUi, OrderUi
├── validator    CategoryValidator, ProductValidator, CustomerValidator (regex)
└── lib          InputHelper, errors/ (DataNotFoundException, InsufficientStockException)
data/            categories.csv, products.csv, customers.csv, orders.csv, orderItems.csv
```

Luồng gọi: `UI` -> `Service` -> `Repository` -> file CSV.

## 4. Kỹ thuật và kiến thức đã áp dụng

- **OOP**: lớp, đóng gói (private + getter/setter), interface (`IRepository`, `IService`), generic.
- **Singleton** cho các service: đọc file một lần khi khởi động, mỗi lần thay đổi ghi lại file.
- **Collection**: `List`, `ArrayList`, `Map` (`LinkedHashMap` để ghép đơn hàng với các dòng hàng khi đọc file).
- **Xử lý file**: `FileReader/BufferedReader`, `FileWriter/BufferedWriter`. Đơn hàng tách thành 2 file (`orders.csv` và `orderItems.csv`) rồi ghép lại khi đọc.
- **Ngoại lệ tự định nghĩa**: `DataNotFoundException`, `InsufficientStockException`.
- **Regex** để kiểm tra dữ liệu nhập: tên (chỉ chữ cái, 2-30 ký tự; tên khách 5-30), số điện thoại (đúng 10 chữ số), giá và tồn kho (số dương).
- **Thuật toán sắp xếp tự viết**: selection sort theo giá (làm trên bản sao, không đổi thứ tự danh sách gốc).
- **Tìm kiếm** theo tên, không phân biệt hoa thường.
- `LocalDate` cho ngày lập đơn.

## 5. Quyết định thiết kế

- `OrderItem.price` lưu **giá tại thời điểm bán**, nên đổi giá sản phẩm sau này không làm sai đơn cũ.
- `Order.totalPrice` được tính một lần khi chốt đơn và lưu lại.
- Tồn kho chỉ bị trừ khi **chốt đơn**, không trừ lúc thêm từng mặt hàng. Nếu hủy đơn thì kho không bị ảnh hưởng.
- Xóa danh mục hoặc khách hàng đang được tham chiếu thì dữ liệu cũ hiển thị `(Unknown)`, chương trình không bị lỗi.

## 6. Cách chạy

Yêu cầu: JDK 17 trở lên.

1. Mở project bằng IntelliJ IDEA (Gradle).
2. Chạy lớp `Main` (`src/main/java/Main.java`). **Thư mục làm việc phải là thư mục gốc của project**, vì chương trình đọc file ở đường dẫn `data/...`.
3. Dữ liệu mẫu đã có sẵn trong thư mục `data/`.

## 7. Kiểm thử

Đã kiểm thử thủ công toàn bộ chức năng với dữ liệu rỗng và dữ liệu mẫu, gồm:
- Thêm, sửa, xóa, liệt kê cho 3 đối tượng; các trường hợp nhập sai (tên, số điện thoại, giá, tồn kho, id không tồn tại).
- Tạo đơn: đơn thường, nhập sai khách/sản phẩm, số lượng không hợp lệ, vượt tồn kho, gộp cùng sản phẩm, hết hàng, đơn rỗng.
- Kiểm tra tồn kho giảm đúng, file CSV ghi đúng, đóng chương trình mở lại vẫn đọc đủ dữ liệu.

## 8. Hạn chế và hướng phát triển

- Chưa có chức năng thống kê (doanh thu, sản phẩm bán chạy, hàng sắp hết).
- Chưa có hủy hoặc xóa đơn hàng.
- Chưa chặn xóa danh mục hoặc khách hàng đang được tham chiếu.
- Id sinh theo thời gian (giây), có thể trùng nếu hai bản ghi cùng loại tạo trong cùng một giây.

## 9. Ghi chú về việc sử dụng AI

Trong quá trình làm, em dùng Claude (AI) để hướng dẫn, giải thích lỗi và rà soát code. Phần lớn code các lớp entity, repository, service, validator và giao diện do em tự viết rồi nhờ kiểm tra. Một số phần có sự hỗ trợ trực tiếp của AI khi em bị vướng: thuật toán sắp xếp theo giá, phương thức tạo đơn và chốt đơn trong `OrderService`, giao diện thêm đơn hàng (`OrderUi.addUI`) và validator danh mục. Em đã đọc và nắm được luồng hoạt động của các phần này.
