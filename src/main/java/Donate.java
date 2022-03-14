import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.common.HttpMethods;
import com.arronlong.httpclientutil.common.HttpResult;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import jdk.nashorn.internal.parser.JSONParser;
import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

//提供登陆爬取方法
//传入名字和身份证号码
public class Donate {

  HttpConfig httpConfig;

  public void donate(String name, String identify, String tele)
      throws HttpProcessException, IOException {
    //主页url，可以获取cookie
    String homeUrl = "https://register.codac.org.cn/SignUp.aspx";
    //身份证姓名url，用来验证身份证姓名是否合法
    String verifyName = "https://register.codac.org.cn/webapi/webapi/register/verify_check.ashx";
    //该地址用来获取验证码
    String codeUrl = "https://register.codac.org.cn/ajaxashx/verificationcodes.ashx";
    //该地址用来提取最终的结果
    String result = "https://register.codac.org.cn/ajaxashx/smssend.ashx";

    //获取上下文，用来设置cookie
    HttpClientContext httpClientContext = new HttpClientContext();
    CookieStore basicCookieStore = new BasicCookieStore();
    httpClientContext.setCookieStore(basicCookieStore);

    //初始化httpconfig,设置headers
    httpConfig = HttpConfig.custom().url(homeUrl).context(httpClientContext);

    //用来发起请求，获取cookie
    String html = HttpClientUtil.get(httpConfig);
    //填装身份证姓名用来请求
    HashMap<String, Object> map = new HashMap<>();
    //reg_name: 徐高松
    //reg_iden: 331082200203221397
    //reg_website: 1
    //reg_position: 0
    map.put("reg_name", name);
    map.put("reg_iden", identify);
    map.put("reg_website", 1);
    map.put("reg_position", 0);

    //这一步用来验证身份证号和姓名
    Header[] headers = HttpHeader.custom().userAgent(
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36")
        .referer("https://register.codac.org.cn/SignUp.aspx")
        .other("Origin", "https://register.codac.org.cn").host("register.codac.org.cn").build();
    httpConfig.headers(headers);
    HttpResult httpResult = HttpClientUtil.sendAndGetResp(
        httpConfig.map(map).url(verifyName).method(
            HttpMethods.POST));
    System.out.println(httpResult.getResult());

    //直接请求获取图片
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    httpConfig.url(codeUrl);

    out = (ByteArrayOutputStream) HttpClientUtil.down(httpConfig.out(out));
    FileOutputStream fileOutputStream = new FileOutputStream("1.png");
    fileOutputStream.write(out.toByteArray());
    fileOutputStream.close();
    String code = ChaoJiYing.PostPic("username", "password",
        "userId", "TYPE", "0", out.toByteArray());
    JSONObject jsonObject = JSON.parseObject(code);
    String pic_str = jsonObject.getString("pic_str");

    map.clear();
    map.put("PhoneNumber",tele);
    map.put("verifycode",pic_str);
    map.put("reg_name",name);
    map.put("reg_ident",identify);
    map.put("reg_position",0);
    java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat ("EEE MMM d yyyy HH:mm:ss",
        Locale.ENGLISH);
    String format = formatter.format(new Date());
    map.put("date",format+" GMT 0800 (中国标准时间)");

    httpConfig.url(result).map(map);
    String post = HttpClientUtil.post(httpConfig);
    System.out.println(post);

  }

  public static void main(String[] args) throws HttpProcessException, IOException {
    Donate D = new Donate();
    D.donate("name","identifyId","telephone");



  }

}
