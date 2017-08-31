package cn.e3.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.e3.manager.utils.FastDFSClient;
import cn.e3.utils.JsonUtils;
import cn.e3.utils.KindEditerModel;

@Controller
public class UploadController {
    /**
     * 需求:上传图片值fastDFS服务器
     * 成功时:{
     *          "error":0,
     *          "url":"http://www.example.com/path/to/file.ext"};
     * 失败时:{
     *          "error":1,
     *          "message":"错误信息"}
     */
    //注入图片服务器地址
    @Value("${IMAGE_SERVER_URL}")
    private String IMAGE_SERVER_URL;
    
    
    @RequestMapping("/pic/upload")
    @ResponseBody
    public String uploadPic(MultipartFile uploadFile){
        
        // 获得文件完整文件名
        String originalFilename = uploadFile.getOriginalFilename();
        // 获得文件扩展名
        String extName = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
        try {
            // 创建上传文件工具类对象
            FastDFSClient fClient = new FastDFSClient("classpath:client.conf");
            //获得文件上传相对路径
            String url = fClient.uploadFile(uploadFile.getBytes(), extName);
            //组合图片上传地址
            url = IMAGE_SERVER_URL + url;
            
            KindEditerModel model = new KindEditerModel();
            //图片上传成功时:error:0,url
            model.setError(0);
            model.setUrl(url);
            //把响应对象转换成Json字符串
            String json = JsonUtils.objectToJson(model);
            return json;
        } catch (Exception e) {
            // 图片上传失败时:error:1,message
            KindEditerModel model = new KindEditerModel();
            model.setError(1);
            model.setMessage("上传图片失败");
            String json = JsonUtils.objectToJson(model);
            e.printStackTrace();
            return json;
        }
    }
}
