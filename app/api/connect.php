<?php
    $servername = "localhost";
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



