<?php
header('content-type: image/png');
$imgurl = $_GET['i'];
echo file_get_contents($imgurl);
?>