package cn.pzhu.forum.util;

import cn.pzhu.forum.content.QiNiuContent;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.dm.model.v20151123.SingleSendMailResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Region;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public class Utils {

    /**
     * Generate a captcha image of the specified width and height
     *
     * @param width  the width of the Verification Code.
     * @param height the height of the Verification Code.
     * @return Map The key is the String of the Code.The BufferedImage
     * is the source of the Verification Code.
     */
    public static Map<String, BufferedImage> GenerateVerificationCode(int width, int height) {

        Map<String, BufferedImage> map = new HashMap<>();
        Random random = new Random();

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.fillRect(0, 0, width, height);

        Font font = new Font("楷书", Font.BOLD, 60);
        graphics.setColor(new Color(random.nextInt(240) + 10));

        /* 绘制干扰线条 */
        for (int i = 0; i < random.nextInt(50); i++) {

            int x = random.nextInt(width - 5) + 1;
            int y = random.nextInt(height - 5) + 1;
            int x1 = random.nextInt(width - 5) + 1;
            int y1 = random.nextInt(height - 5) + 1;

            /* 设置直线样式 */
            BasicStroke bs = new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);

            /* 定义直线 */
            Line2D line2D = new Line2D.Double(x, y, x1, y1);
            graphics.setStroke(bs);
            graphics.setColor(new Color(random.nextInt(200) + 10));
            graphics.draw(line2D);

        }

        StringBuilder rCode = new StringBuilder();

        for (int i = 0; i < 4; ) {

            int ch = random.nextInt(57) + 65;
            if (ch > 90 && ch < 97) {
                continue;
            }

            int color = random.nextInt(245) + 10;
            graphics.setColor(new Color(color));
            graphics.translate(random.nextInt(5), random.nextInt(5));

            String c = Character.toString((char) ch);
            int x = width / 4 * i;
            int y = random.nextInt(15) + 10;
            graphics.drawString(c, x, y);
            rCode.append(c);
            i++;
        }
        graphics.dispose();
        map.put(rCode.toString(), image);

        return map;
    }

    /**
     * Send information for a specified Email
     *
     * @param code      Captcha information
     * @param email     Specify the Email to send information
     * @param emailType Email type
     * @return The result of sending a message.True is successful.
     */
    public static boolean sendMail(String code, String email, String emailType) {

        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAIDxjkfOVMr9lH", "kTZoAuDBADEbejDTdTQ5funhD1bni4");

        IAcsClient client = new DefaultAcsClient(profile);
        SingleSendMailRequest request = new SingleSendMailRequest();

        try {
            //request.setVersion("2017-06-22");// 如果是除杭州region外的其它region（如新加坡region）,必须指定为2017-06-22
            request.setAccountName("Impassive@cone4.top");
            request.setFromAlias("Impassive");
            request.setAddressType(1);
            request.setTagName("Impassive");
            request.setReplyToAddress(true);
            request.setToAddress(email);
            //可以给多个收件人发送邮件，收件人之间用逗号分开，批量发信建议使用BatchSendMailRequest方式
            //request.setToAddress("邮箱1,邮箱2");
            request.setSubject(emailType);
            request.setHtmlBody("【Impassive】:尊敬的用户，您现在正在注册Impassive的网站，您的验证码为:【" + code + "】,我们的工作人员不会索取您的验证码，请不要将您的验证码透露给他人!");
            SingleSendMailResponse httpResponse = client.getAcsResponse(request);

            return true;

        } catch (ClientException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Send information for a specified Email
     *
     * @param message   Specified information
     * @param emails    Specify the Email to send information
     * @param emailType Email type
     * @return The result of sending a message.True is successful.
     */
    public static boolean sendBatchMail(String message, String[] emails, String emailType) {

        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAIDxjkfOVMr9lH", "kTZoAuDBADEbejDTdTQ5funhD1bni4");

        IAcsClient client = new DefaultAcsClient(profile);
        SingleSendMailRequest request = new SingleSendMailRequest();

        for (String email : emails) {
            try {
                //request.setVersion("2017-06-22");// 如果是除杭州region外的其它region（如新加坡region）,必须指定为2017-06-22
                request.setAccountName("Impassive@cone4.top");
                request.setFromAlias("Impassive");
                request.setAddressType(1);
                request.setTagName("Impassive");
                request.setReplyToAddress(true);
                request.setToAddress(email);
                //可以给多个收件人发送邮件，收件人之间用逗号分开，批量发信建议使用BatchSendMailRequest方式
                //request.setToAddress(email);
                request.setSubject(emailType);
                request.setHtmlBody(message);
                SingleSendMailResponse httpResponse = client.getAcsResponse(request);

                String envId = httpResponse.getEnvId();

            } catch (ClientException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    /**
     * Generate a digital verification code for the specified number of digits
     *
     * @param number Number of digits
     * @return Code
     */
    public static String generatedCode(int number) {

        Random random = new Random();

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < number; i++) {
            int temp = random.nextInt(9);
            builder.append(temp);
        }

        random = null;

        return builder.toString();
    }

    /**
     * Determine if the string contains special characters
     *
     * @param str String to be judged
     * @return True is contain.
     */
    public static boolean stringFilter(String str) {
        String regEx = "[`~!@#$%^&*()+=|{}':;,\\[\\].<>/?！￥…（）—【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        boolean b = m.find();

        return b;
    }

    /**
     * Upload file to the specified path
     *
     * @param multipartFile File to be uploaded
     * @param path          File save path
     * @return Status
     */
    public static boolean uploadImage(MultipartFile multipartFile, String path) {
        try {
            InputStream inputStream = multipartFile.getInputStream();
            byte[] data = new byte[1024];
            int len = 0;
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(path);
                while ((len = inputStream.read(data)) != -1) {
                    fileOutputStream.write(data, 0, len);
                }

                return true;
            } catch (IOException e) {

                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 生成随机的字符串
     *
     * @return 生成的字符串
     */
    public static String getRandomName() {

        return UUID.randomUUID().toString();
    }

    /**
     * 获得当前时间的字符串形式
     *
     * @param flag 是否包含时分秒，true包含，false不包含
     * @return 时间字符串
     */
    public static String getDate(boolean flag) {
        LocalDateTime now = LocalDateTime.now();
        StringBuffer stringBuffer = new StringBuffer();
        // 获得年
        int year = now.getYear();

        // 获得月
        int month = now.getMonth().getValue();

        // 获得月
        int day = now.getDayOfMonth();

        if (flag) {

            int hour = now.getHour();

            int minute = now.getMinute();

            int second = now.getSecond();

            stringBuffer.append(year).append("年").append(month).append("月").append(day).append("日 ")
                    .append(hour).append(":").append(minute).append(":").append(second);

        } else {
            stringBuffer.append(year).append("年").append(month).append("月").append(day).append("日");
        }

        return stringBuffer.toString();
    }

    /**
     * 获得年份
     *
     * @return 年份
     */
    public static String getYear() {

        LocalDateTime dateTime = LocalDateTime.now();

        int year = dateTime.getYear();

        return year + "";
    }

    /**
     * 获得月份
     *
     * @return 月份
     */
    public static Integer getMonth() {

        LocalDateTime dateTime = LocalDateTime.now();

        return dateTime.getMonth().getValue();
    }

    /**
     * 生成二维码
     *
     * @param text   二维码的内容
     * @param width  宽度
     * @param height 高度
     * @return 二维码的字节数组
     * @throws WriterException QRCodeWriter Exception
     * @throws IOException     输入输出异常
     */
    public static byte[] getQRCode(String text, int width, int height) throws WriterException, IOException {

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 上传图片到七牛云
     *
     * @param file 文件资源
     * @param name 上传后的文件名称
     * @return 文件路径
     */
    public static String uploadImg(ByteArrayInputStream file, String name) {

        // 华南地区
        Configuration cfg = new Configuration(Region.region2());

        UploadManager uploadManager = new UploadManager(cfg);
        try {

            Auth auth = Auth.create(QiNiuContent.accessKey, QiNiuContent.secretKey);
            String bucket = auth.uploadToken(QiNiuContent.bucket);

            Response put = uploadManager.put(file, name, bucket, null, null);

            DefaultPutRet defaultPutRet = new Gson()
                .fromJson(put.bodyString(), DefaultPutRet.class);

            String returnPath = QiNiuContent.path + "/" + defaultPutRet.key;

            log.info("上传文件到七牛云最终地址:{}", returnPath);

            return returnPath;

        } catch (QiniuException e) {
            e.printStackTrace();
            log.error("上传文件到七牛云出错：" + Arrays.toString(e.getStackTrace()));
        }

        return "";
    }

}
