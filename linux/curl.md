# CURL

### 使用curl上传下载文件

1. 上传文件：

   windows:

   ```
   curl -X POST "http://ip:port/api/v1/file" -H "accept: */*" -H "Content-Type: multipart/form-data" -F "file=@C:\Users\u68.jpg;type=image/png"
   ```

   linux:

   ```
   curl -X POST "http://ip:port/api/v1/file" -H "accept: */*" -H "Content-Type: multipart/form-data" -F "file=@/root/image/u68.jpg;type=image/png"
   ```

2. 下载文件：

   ```
   curl -X GET –O "http://ip:port/api/v1/file"
   ```









































