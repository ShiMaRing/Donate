ChaoJiYing.java文件是封装好超级鹰接口调用类,请复制到自己项目中,然后调用里面的类函数就行.

详细API请参考:https://www.chaojiying.com/api-5.html

接口返回的中文以unicode编码 在JAVA中用 通过JSONObject.fromObject获取到的数据会自动转成汉字

调用说明:

//识别图片_按图片文件路径
  PostPic(String username, String password, String softid, String codetype, String len_min, String filePath)

//识别图片_按图片二进制流
  PostPic(String username, String password, String softid, String codetype, String len_min, byte[] byteArr)

//识别图片_按图片base64字符串 请提前参考base64注意事项 https://www.chaojiying.com/api-46.html
  PostPic_base64(String username, String password, String softid, String codetype, String len_min, String file_base64)
  
//报错返分
  ReportError(String username, String password, String softid, String id)


超级鹰官方网站:https://www.chaojiying.com

