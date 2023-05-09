<?php
    $servername = "fa23-2402-800-62d0-bf1c-b95c-c6e0-177-7120.ngrok-free.app";
    $username = "root";
    $password = "";
    $dbname = "medicine";

    // Thiết lập kết nối đến MySQL
    $conn = mysqli_connect($servername, $username, $password, $dbname);

    // Kiểm tra kết nối
    if (!$conn) {
        die("Connection failed: " . mysqli_connect_error());
    } else {
        print("true");
        echo "Connected successfully";
    }

    // Đóng kết nối
    mysqli_close($conn);
?>



