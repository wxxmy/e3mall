package cn.e3.fdfs;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import cn.e3.manager.utils.FastDFSClient;

public class MyFdfs {
    /**
     * 需求:测试用java客户端上传图片
     * @throws MyException 
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    @Test
    public void picUpload() throws FileNotFoundException, IOException, MyException{
        //设置配置文件路径
        String clientConf = "E:\\gitDemo\\e3-manager-web\\src\\main\\resources\\client.conf";
        //设置要上传的图片路径
        String picPath = "E://girl.jpg";
        // 加载配置文件路径
        ClientGlobal.init(clientConf);
        // 创建调度服务器客户端
        TrackerClient tClient = new TrackerClient();
        // 利用客户端创建调度服务服务器
        TrackerServer trackerServer = tClient.getConnection();
        
        StorageServer storageServer = null;
        //创建Storage客户端对象
        StorageClient storageClient = new StorageClient(trackerServer, storageServer );
        //
        String[] urls = storageClient.upload_file(picPath, "jpg", null);
        for (String url : urls) {
            System.out.println(url);
        }
    }
    
    //使用工具类上传图片
    @Test
    public void uploadPic(){
      //设置配置文件路径
        String clientConf = "E:\\gitDemo\\e3-manager-web\\src\\main\\resources\\client.conf";
        //设置要上传的图片路径
        String picPath = "E://screen.png";
        //
        try {
            FastDFSClient fastDFSClient = new FastDFSClient(clientConf);
            String url = fastDFSClient.uploadFile(picPath);
            System.out.println(url);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}

