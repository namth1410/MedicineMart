<?php
require_once('connect.php');

// Lấy danh sách các sản phẩm từ cơ sở dữ liệu
$query = "SELECT * FROM product";
$result = mysqli_query($conn, $query);

// Chuyển đổi kết quả thành một mảng dữ liệu
$data = array();
while ($row = mysqli_fetch_assoc($result)) {
    $data[] = $row;
}

// Trả về kết quả dưới dạng JSON
header('Content-Type: application/json');
echo json_encode($data);
?>