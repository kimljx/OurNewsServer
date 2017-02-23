<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>上传文件</title>
</head>
<body>
<form action="uploadImage" method="post" enctype="multipart/form-data">
    <table align="center" border="1">
        <caption><h2>上传</h2></caption>
        <tr>
            <td>上传文件：</td>
            <td><input type="file" name="upload"/></td>
        </tr>
        <tr align="center">
            <td colspan="2"><input type="submit" value="上传"/></td>
        </tr>
    </table>
</form>
</body>
</html> 