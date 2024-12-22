package com.pay.tg.service;


import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
//import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pay.tg.config.telegrame.ZpayMessageBot;
//import com.pay.tg.domain.OrderDetailDto;
import com.pay.tg.utils.ImageUtils;
import com.pay.tg.utils.PayDigestUtil;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TgMessageService {

    private static final Logger log = LoggerFactory.getLogger(TgMessageService.class);

    private final static String MD5_KEY = "aqAzxcaQDSADAZMKAZXCSDAz20230419";
    private static String BRL_REQ = "http://admin01.cxhd01.com";
    private static String IDR_REQ = "http://admin.cxhd01.com";
    private static String IDR_REQ_NEW = "http://admin.spxysz.com/";


    @Autowired
    private ZpayMessageBot zpayMessageBot;

    private static String env;

    @Value("${spring.profiles.active}")
    public void setEnv(String env) {
        this.env = env;
    }

    /**
     * 获取群Id
     *
     * @param update
     */
    public void executeGetChatId(Update update) {
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChat().getId().toString());
        message.setParseMode("Markdown");
        message.setText("KEY:  `" + update.getMessage().getChat().getId() + "`");
        try {
            zpayMessageBot.executeAsync(message);
        } catch (Exception e) {
            log.error("获取TG群聊ID异常：{}", e.toString());
        }
    }

    /**
     * 获取余额
     *
     * @param update
     */
    public void getPartnerBalance(Update update) {
        try {
            Map<String, String> partner = getPartner(update.getMessage().getChatId().toString());
            //Map<String, String> partner = getPartner("-601834622");
            if (!partner.containsKey("partnerId")) {
                log.info("没有获取到响应的Id：{}", partner);
                return;
            }
            JSONObject param = new JSONObject();
            param.put("partnerId", partner.get("partnerId"));
            param.put("verify_sign", PayDigestUtil.getSign(param, MD5_KEY));
            String url = partner.get("url") + "/api/tsend_2023/getPartnerBalance";
            HttpResponse httpResponse = HttpUtil.createPost(url)
                    .header("Content-Type", "application/json")
                    .body(param.toJSONString())
                    .execute();
            String body = httpResponse.body();
            log.info("请求地址：{}，获取响应：{}", url, body);
            JSONObject respon = JSON.parseObject(body);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setParseMode("HTML");
            sendMessage.setText("<strong><b>当前可用余额：" + respon.getBigDecimal("data").setScale(2, RoundingMode.HALF_UP) + "</b></strong>");
            sendMessage.setChatId(update.getMessage().getChatId().toString());
            sendMessage.setReplyToMessageId(update.getMessage().getMessageId());
            zpayMessageBot.executeAsync(sendMessage);
            //log.info("发送群消息：{}", update.getMessage().getChatId());
        } catch (Exception e) {
            log.error("发送消息失败：{}", e.getMessage());
        }

    }

    public static Map<String, String> getPartner(String chartId) {
        if (!"product".equals(env)) {
            BRL_REQ = "http://127.0.0.1:8021";
            IDR_REQ = "http://127.0.0.1:8021";
        }
        Map<String, String> res = new HashMap<>();
        JSONObject param = new JSONObject();
        param.put("chatId", chartId);
        param.put("verify_sign", PayDigestUtil.getSign(param, MD5_KEY));
        HttpResponse httpResponse = HttpUtil.createPost(IDR_REQ + "/api/tsend_2023/getCharGroupId")
                .header("Content-Type", "application/json")
                .body(param.toJSONString())
                .execute();
        String body = httpResponse.body();
        JSONObject respon = JSON.parseObject(body);
        if (respon.getString("status").equals("200")) {
            res.put("url", IDR_REQ);
            res.put("partnerId", respon.getString("data"));
            return res;
        }

        httpResponse = HttpUtil.createPost(IDR_REQ_NEW + "/api/tsend_2023/getCharGroupId")
                .header("Content-Type", "application/json")
                .body(param.toJSONString())
                .execute();
        body = httpResponse.body();
        respon = JSON.parseObject(body);
        if (respon.getString("status").equals("200")) {
            res.put("url", IDR_REQ_NEW);
            res.put("partnerId", respon.getString("data"));
            return res;
        }

        httpResponse = HttpUtil.createPost(BRL_REQ + "/api/tsend_2023/getCharGroupId")
                .header("Content-Type", "application/json")
                .body(param.toJSONString())
                .execute();
        body = httpResponse.body();
        respon = JSON.parseObject(body);
        if (respon.getString("status").equals("200")) {
            res.put("url", BRL_REQ);
            res.put("partnerId", respon.getString("data"));
        }
        return res;
    }

    public static Map<String, String> getUpsteamGroupId(String channelId) {
        if (!"product".equals(env)) {
            BRL_REQ = "http://127.0.0.1:8021";
            IDR_REQ = "http://127.0.0.1:8021";
        }
        Map<String, String> res = new HashMap<>();
        JSONObject param = new JSONObject();
        param.put("channelId", channelId);
        param.put("verify_sign", PayDigestUtil.getSign(param, MD5_KEY));
        HttpResponse httpResponse = HttpUtil.createPost(IDR_REQ + "/api/tsend_2023/getUpsteamGroupId")
                .header("Content-Type", "application/json")
                .body(param.toJSONString())
                .execute();
        String body = httpResponse.body();
        JSONObject respon = JSON.parseObject(body);
        if (respon.getString("status").equals("200")) {
            res.put("url", IDR_REQ);
            res.put("groupId", respon.getString("data"));
            return res;
        }

        httpResponse = HttpUtil.createPost(IDR_REQ_NEW + "/api/tsend_2023/getUpsteamGroupId")
                .header("Content-Type", "application/json")
                .body(param.toJSONString())
                .execute();
        body = httpResponse.body();
        respon = JSON.parseObject(body);
        if (respon.getString("status").equals("200")) {
            res.put("url", IDR_REQ_NEW);
            res.put("groupId", respon.getString("data"));
            return res;
        }

        httpResponse = HttpUtil.createPost(BRL_REQ + "/api/tsend_2023/getUpsteamGroupId")
                .header("Content-Type", "application/json")
                .body(param.toJSONString())
                .execute();
        body = httpResponse.body();
        respon = JSON.parseObject(body);
        if (respon.getString("status").equals("200")) {
            res.put("url", BRL_REQ);
            res.put("groupId", respon.getString("data"));
        }
        return res;
    }

    public void orderQuery(Update update) {
        try {
            Map<String, String> partner = getPartner(update.getMessage().getChatId().toString());
            //Map<String, String> partner = getPartner("-601834622");
            if (!partner.containsKey("partnerId")) {
                log.info("没有获取到响应的Id：{}", partner);
                return;
            }
            JSONObject param = new JSONObject();
            param.put("partnerId", partner.get("partnerId"));
            param.put("orderNo", update.getMessage().getText().split(" ")[1]);
            param.put("verify_sign", PayDigestUtil.getSign(param, MD5_KEY));
            String url = partner.get("url") + "/api/tsend_2023/orderQuery";
            log.info(param.toJSONString());
            HttpResponse httpResponse = HttpUtil.createPost(url)
                    .header("Content-Type", "application/json")
                    .body(param.toJSONString())
                    .execute();
            String body = httpResponse.body();
            log.info("请求地址：{}，获取响应：{}", url, body);
            JSONObject respon = JSON.parseObject(body);
            sendMesage(update.getMessage().getChatId().toString(), update.getMessage().getMessageId().toString(),
                    respon.getString("data"));
            //log.info("发送群消息：{}", update.getMessage().getChatId());
        } catch (Exception e) {
            log.error("发送消息失败：{}", e.getMessage());
        }
    }

    public void successRate(Update update, String type) {
        try {
            Map<String, String> partner = getPartner(update.getMessage().getChatId().toString());
            //Map<String, String> partner = getPartner("-601834622");
            if (!partner.containsKey("partnerId")) {
                log.info("没有获取到响应的Id：{}", partner);
                return;
            }
            JSONObject param = new JSONObject();
            param.put("partnerId", partner.get("partnerId"));
            param.put("type", update.getMessage().getText().split("@")[0].replace("/", ""));
            param.put("verify_sign", PayDigestUtil.getSign(param, MD5_KEY));
            String url = partner.get("url") + "/api/tsend_2023/successRate";
            log.info(param.toJSONString());
            HttpResponse httpResponse = HttpUtil.createPost(url)
                    .header("Content-Type", "application/json")
                    .body(param.toJSONString())
                    .execute();
            String body = httpResponse.body();
            log.info("请求地址：{}，获取响应：{}", url, body);
            JSONObject respon = JSON.parseObject(body);
            sendMesage(update.getMessage().getChatId().toString(), update.getMessage().getMessageId().toString(),
                    respon.getString("data"));
            //log.info("发送群消息：{}", update.getMessage().getChatId());
        } catch (Exception e) {
            log.error("发送消息失败：{}", e.getMessage());
        }

    }

    /**
     * 消息发送
     *
     * @param charId  群ID
     * @param replyId 回复消息Id
     * @param msgInfo 文本内容
     */
    private void sendMesage(String charId, String replyId, String msgInfo) {
        try {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setParseMode("HTML");
            sendMessage.setText(msgInfo);
            sendMessage.setChatId(charId);
            if (StringUtils.isNotBlank(replyId)) {
                sendMessage.setReplyToMessageId(Integer.valueOf(replyId));
            }
            zpayMessageBot.executeAsync(sendMessage);
        } catch (Exception e) {
            log.error("发送消息失败:{}", e.toString());
        }
    }

    /**
     * 投诉处理发送信息
     */
    public void sendFileAndMsessage(InputFile inputFile, String msg, String charId) {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(charId);
        sendDocument.setDocument(inputFile);
        try {
            zpayMessageBot.executeAsync(sendDocument);
        } catch (Exception e) {
            log.error("发送消息失败:{}", e.toString());
        }
        sendMesage(charId, null, msg);

    }

    /**
     * 添加进入需要查单得代收
     */
    public void orderNeedQuery(Update update) {
        Map<String, String> partner = getPartner(update.getMessage().getChatId().toString());
        if (!partner.containsKey("partnerId")) {
            log.info("没有获取到响应的Id：{}", partner);
            return;
        }
        String[] info = update.getMessage().getText().split(" ");
        String partnerOrderNo = info[1];
        String partnerId = partner.get("partnerId");
        JSONObject param = new JSONObject();
        param.put("partnerId", partnerId);
        param.put("partnerOrderNo", partnerOrderNo);
        param.put("verify_sign", PayDigestUtil.getSign(param, MD5_KEY));
        String url = partner.get("url") + "/api/tsend_2023/orderNeedQuery";
        log.info(param.toJSONString());
        HttpResponse httpResponse = HttpUtil.createPost(url)
                .header("Content-Type", "application/json")
                .body(param.toJSONString())
                .execute();
        String body = httpResponse.body();
        log.info("请求地址：{}，获取响应：{}", url, body);
        JSONObject respon = JSON.parseObject(body);
        sendMesage(update.getMessage().getChatId().toString(), update.getMessage().getMessageId().toString(),
                respon.getString("data"));
    }

    /*public void getPartnerOrderNo(Update update) {
        //log.info(update.getMessage().getChatId().toString());
        String msg = update.getMessage().getText();
        StringBuilder sendMsg = new StringBuilder();
        if (msg.contains("，")) {
            sendMsg.append("消息存在中文逗号，请使用英文后好隔开，例如:\n" +
                    "商户号：1000100020003008;\n" +
                    "商户订单号：\n" +
                    "商户订单号1,utr;\n" +
                    "商户订单号2,utr;\n" +
                    "商户订单号3,utr;");
            sendMesage(update.getMessage().getChatId().toString(), update.getMessage().getMessageId().toString(),
                    sendMsg.toString());
            return;
        }
        msg = msg.replace("商户号：", "");
        msg = msg.replace("商户订单号：", "");
        msg = msg.replaceAll(" ", "");
        msg = msg.replaceAll(":", "");
        msg = msg.replaceAll("：", "");
        msg = msg.replaceAll("\\t", "");
        msg = msg.replaceAll("\\n", "");
        //System.out.println(msg);
        //System.out.println(msg.contains("商户号"));
        String[] msgInfo = msg.split(";");
        Map<String, String> partnerOrderNoAndUTR = new HashMap<>();
        String partnerId = msgInfo[0];
        System.arraycopy(msgInfo, 1, msgInfo, 0, msgInfo.length - 1);
        Set<String> partnerOrderNo = new HashSet<>();
        boolean isError = false;
        for (String parterNo : msgInfo) {
            String[] strings = parterNo.split(",");
            if (strings.length <= 1) {
                isError = true;
                break;
            }
            partnerOrderNo.add(strings[0]);
            partnerOrderNoAndUTR.put(strings[0], strings[1]);
        }
        if (isError) {
            sendMsg.append("商户订单号格式异常，例如:\n" +
                    "商户号：1000100020003008;\n" +
                    "商户订单号：\n" +
                    "商户订单号1,utr;\n" +
                    "商户订单号2,utr;\n" +
                    "商户订单号3,utr;");
            sendMesage(update.getMessage().getChatId().toString(), update.getMessage().getMessageId().toString(),
                    sendMsg.toString());
            return;
        }
        JSONObject param = new JSONObject();
        param.put("partnerId", partnerId);
        param.put("timeMillis", System.currentTimeMillis());
        param.put("verify_sign", PayDigestUtil.getSign(param, MD5_KEY));
        param.put("partnerOrderNo", partnerOrderNo);
        //老平台
        String url = IDR_REQ + "/api/tsend_2023/partnerOrderNoQuery";
        String fileName = partnerId + "_" + DateUtil.format(new Date(), "yyyyMMddHHmmss");
        File file = new File(fileName + ".xlsx");
        log.info(param.toJSONString());
        HttpResponse httpResponse = HttpUtil.createPost(url)
                .header("Content-Type", "application/json")
                .body(param.toJSONString())
                .execute();
        String body = httpResponse.body();
        JSONObject respone = JSON.parseObject(body);
        List<OrderDetailDto> partnerOrderNos = new ArrayList<>();
        if (respone.getJSONArray("data").size() > 0) {
            partnerOrderNos.addAll(JSONObject.parseArray(respone.getJSONArray("data").toJSONString(), OrderDetailDto.class));
        }
        //新平台
        url = IDR_REQ_NEW + "/api/tsend_2023/partnerOrderNoQuery";
        httpResponse = HttpUtil.createPost(url)
                .header("Content-Type", "application/json")
                .body(param.toJSONString())
                .execute();
        body = httpResponse.body();
        respone = JSON.parseObject(body);
        if (respone.getJSONArray("data").size() > 0) {
            partnerOrderNos.addAll(JSONObject.parseArray(respone.getJSONArray("data").toJSONString(), OrderDetailDto.class));
        }
        //设置UTR
        partnerOrderNos.stream().forEach(info -> {
            info.setUtr(partnerOrderNoAndUTR.get(info.getPartnerOrderNo()));
        });
        EasyExcel.write(file.getAbsolutePath(), OrderDetailDto.class).sheet("订单数据").doWrite(partnerOrderNos);
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(update.getMessage().getChatId().toString());
        InputFile inputFile = new InputFile();
        inputFile.setMedia(file);
        sendDocument.setDocument(inputFile);
        try {
            zpayMessageBot.execute(sendDocument);
        } catch (Exception e) {
            log.error("发送消息失败:{}", e.toString());
        } finally {
            file.delete();
        }
    }*/


    public void getQueryPhoneOrder(Update update) {
        String msg = update.getMessage().getCaption();
        //获取群消息
        Map<String, String> partner = getPartner(update.getMessage().getChatId().toString());
        if (!partner.containsKey("partnerId")) {
            log.info("没有获取到响应的Id：{}", partner);
            return;
        }
        String[] info = msg.split(" ");
        List<PhotoSize> photos = update.getMessage().getPhoto();
        PhotoSize photo = photos.get(photos.size() - 1);
        String id = photo.getFileId();
        File locahostFile = null;
        try {

            //查单信息添加
            JSONObject param = new JSONObject();
            param.put("partnerId", partner.get("partnerId"));
            param.put("orderNo", info[1]);
            param.put("verify_sign", PayDigestUtil.getSign(param, MD5_KEY));
            String url = partner.get("url") + "/api/tsend_2023/orderQuery";
            log.info(param.toJSONString());
            HttpResponse httpResponse = HttpUtil.createPost(url)
                    .header("Content-Type", "application/json")
                    .body(param.toJSONString())
                    .execute();
            String body = httpResponse.body();
            log.info("请求地址：{}，获取响应：{}", url, body);
            JSONObject respon = JSON.parseObject(body);
            if (respon.getString("data").contains("订单不存在")
                    || respon.getString("data").contains("成功") || respon.getString("data").equals("失败")) {
                //回复下游
                sendMesage(update.getMessage().getChatId().toString(), update.getMessage().getMessageId().toString(),
                        respon.getString("data"));
                return;
            }
            JSONObject order = respon.getJSONObject("message");
            //获取图片中UTR
            GetFile getFile = new GetFile();
            getFile.setFileId(id);
            org.telegram.telegrambots.meta.api.objects.File file = zpayMessageBot.execute(getFile);
            String fileUrl = file.getFileUrl(zpayMessageBot.getBotToken());
            CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
                log.info("== DOWNLOADING IMAGE :{},path{}", fileUrl, file.getFilePath());
                return getPhoneUTR(fileUrl);
            });
            String utr = future1.get();
            log.info("获取图片地址UTR：{}", utr);
            //网络图片
            locahostFile = new File(file.getFilePath());
            ImageUtils.getUrlFile(locahostFile, fileUrl);
            //回复下游
            String data = respon.getString("data");
            SendMessage sendMessage = new SendMessage();
            sendMessage.setParseMode("HTML");
            StringBuilder builder = new StringBuilder();
            builder.append("<strong><b>UTR：" + utr + "</b></strong>\n");
            builder.append(data);
            builder.append("<strong><b>请稍等...</b></strong>\n");
            sendMessage.setText(builder.toString());
            sendMessage.setChatId(update.getMessage().getChatId().toString());
            sendMessage.setReplyToMessageId(update.getMessage().getMessageId());
            zpayMessageBot.executeAsync(sendMessage);
            //异常订单添加
            String partnerOrderNo = info[1];
            String partnerId = partner.get("partnerId");
            param = new JSONObject();
            param.put("partnerId", partnerId);
            param.put("partnerOrderNo", partnerOrderNo);
            param.put("verify_sign", PayDigestUtil.getSign(param, MD5_KEY));
            url = partner.get("url") + "/api/tsend_2023/orderNeedQuery";
            log.info(param.toJSONString());
            httpResponse = HttpUtil.createPost(url)
                    .header("Content-Type", "application/json")
                    .body(param.toJSONString())
                    .execute();
            body = httpResponse.body();
            log.info("请求地址：{}，获取响应：{}", url, body);

            //上游处理
            File finalLocahostFile = locahostFile;
            CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
                try {
                    SendPhoto sendPhoto = new SendPhoto();
                    sendPhoto.setCaption(utr + "  " + order.getString("orderNo"));
                    sendPhoto.setChatId(getUpsteamGroupId(order.getString("payChannelId")).get("groupId"));
                    sendPhoto.setPhoto(new InputFile(finalLocahostFile));
                    zpayMessageBot.execute(sendPhoto);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (null != finalLocahostFile) {
                        finalLocahostFile.delete();
                    }
                }
            });
        } catch (Exception e) {
            log.info("查询异常：{}",e.toString());
            e.printStackTrace();
        }
    }

    public static String getPhoneUTR(String phoneUrl) {
        try {
            ITesseract instance = new Tesseract();
            instance.setDatapath("/usr/share/tesseract/tessdata");
            //instance.setDatapath("classpath:/chidata");
            instance.setLanguage("eng");
            String doOCR = instance.doOCR(ImageUtils.convertImage(ImageUtils.getBufferedImageDestUrl(phoneUrl)));
            /*java.io.File imageLocation = new java.io.File("E:\\Downloads\\file_0.jpg");
            String doOCR = instance.doOCR(imageLocation);*/
            String[] upis = null;
            String group = null;
            if (doOCR.contains("UPI")) {
                upis = doOCR.split("UPI");
            } else if (doOCR.contains("UTR")) {
                upis = doOCR.split("UTR");
            } else if (doOCR.contains("Unf")) {
                upis = doOCR.split("Unf");
            }
            //提取分组
            List<String> strUtr = new ArrayList<>();
            if (null != upis) {
                for (int index = 1; index < upis.length; index++) {
                    strUtr.add(upis[index]);
                }
            } else {//没有则获取全部
                strUtr = Arrays.asList(doOCR);
            }
            for (String s : strUtr) {
                String[] split = s.split(" ");
                for (String info : split) {
                    Pattern pattern = Pattern.compile("(\\d{12})");
                    Matcher matcher = pattern.matcher(info.replaceAll("\\n", ""));
                    if (matcher.find()) {
                        group = matcher.group(1);
                        if (group.startsWith("3") && group.startsWith("4")) {
                            break;
                        }
                    }
                }
                if (StringUtils.isNotBlank(group) && group.startsWith("3") && group.startsWith("4"))
                    break;
            }
            log.info("获取UTR：{}", group);
            return group;
        } catch (Exception e) {
            log.info("获取图片UTR异常：{}",e.toString());
            e.printStackTrace();
        }
        return null;
    }

    /*public static void main(String[] args) {
        String utr = getPhoneUTR("https://api.telegram.org/file/bot5727099520:AAGJRxxXHfuxiU1M243W0n8REg8WQv9lWdY/photos/file_4.jpg");
        String[] upis = null;
        if (utr.contains("UPI")) {
            upis = utr.split("UPI");
        } else if (utr.contains("UTR")) {
            upis = utr.split("UTR");
        }
        String[] split = upis[1].split("\n");
        for (String s : split) {
            System.out.println("第一行：" + s);
            Pattern pattern = Pattern.compile("(\\d{12})");
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                String group = matcher.group(1);
                System.out.println("获取UTR:" + group);
                break;
            }
        }


    }*/


}
