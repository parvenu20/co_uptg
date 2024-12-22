package com.pay.tg.rest;


import com.alibaba.fastjson.JSONObject;
import com.pay.tg.config.telegrame.ZpayMessageBot;
import com.pay.tg.service.TgMessageService;
import com.pay.tg.utils.ImageUtils;
import com.pay.tg.utils.PayDigestUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/send_custom")
public class TgController {

    private static final Logger log = LoggerFactory.getLogger(TgController.class);

    private final static String MD5_KEY = "aqAzxcaQDSADAZMKAZXCSDAz20230419";

    @Autowired
    private ZpayMessageBot zpayMessageBot;

    /**
     * 发送自定义消息
     *
     * @param req
     */
    @PostMapping("/send_2023/{chatId}")
    public Object sendCustomMsg(@PathVariable String chatId, @RequestBody JSONObject req) {
        log.info("上游发送：{}", req.toJSONString());
        Map<String, String> params = new HashMap<>();
        params.put("chatId", chatId);
        File locahostFile = new File(req.getString("filePath"));
        try {
            String sign = PayDigestUtil.getSign(params, MD5_KEY);
            if (!sign.equals(req.getString("verify_sign"))) {
                return false;
            }
            //网络图片
            ImageUtils.getUrlFile(locahostFile, req.getString("fileUrl"));
            SendPhoto sendPhoto = new SendPhoto();
            if (req.containsKey("eitUser") && StringUtils.isNotBlank(req.getString("eitUser"))) {
                sendPhoto.setCaption(req.getString("msgInfo")+"\n"+req.getString("eitUser"));
            } else {
                sendPhoto.setCaption(req.getString("msgInfo"));
            }
            sendPhoto.setChatId(chatId);
            sendPhoto.setPhoto(new InputFile(locahostFile));
            zpayMessageBot.execute(sendPhoto);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("发送自定义消息失败：{}", e.toString());
            return false;
        } finally {
            if (null != locahostFile) {
                locahostFile.delete();
            }
        }
    }

    @PostMapping("/sendCustomMsgLochastImage/{chatId}")
    public Object sendCustomMsgLochastImage(@PathVariable String chatId, @RequestBody JSONObject req) {
        log.info("上游发送：{}", req.toJSONString());
        Map<String, String> params = new HashMap<>();
        params.put("chatId", chatId);
        File locahostFile = new File(req.getString("filePath"));
        try {
            String sign = PayDigestUtil.getSign(params, MD5_KEY);
            if (!sign.equals(req.getString("verify_sign"))) {
                return false;
            }
            //网络图片
            SendPhoto sendPhoto = new SendPhoto();
            if (req.containsKey("eitUser") && StringUtils.isNotBlank(req.getString("eitUser"))) {
                sendPhoto.setCaption(req.getString("msgInfo")+"\n"+req.getString("eitUser"));
            } else {
                sendPhoto.setCaption(req.getString("msgInfo"));
            }
            sendPhoto.setChatId(chatId);
            sendPhoto.setPhoto(new InputFile(locahostFile));
            zpayMessageBot.execute(sendPhoto);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("发送自定义消息失败：{}", e.toString());
            return false;
        } finally {
            if (null != locahostFile) {
                locahostFile.delete();
            }
        }
    }

    /**
     * 发送自定义消息
     *
     * @param req
     */
    @PostMapping("/send_2023_loc/{chatId}")
    public Object send_2023_loc(@PathVariable String chatId, @RequestBody JSONObject req) {
        log.info("send_2023_loc发送自定义消息：{}", req.toJSONString());
        Map<String, String> params = new HashMap<>();
        params.put("chatId", chatId);
        File locahostFile = new File(req.getString("filePath"));
        try {
            String sign = PayDigestUtil.getSign(params, MD5_KEY);
            if (!sign.equals(req.getString("verify_sign"))) {
                return false;
            }
            //网络图片
            //ImageUtils.getUrlFile(locahostFile, req.getString("fileUrl"));
            SendPhoto sendPhoto = new SendPhoto();
            if (req.containsKey("eitUser") && StringUtils.isNotBlank(req.getString("eitUser"))) {
                sendPhoto.setCaption(req.getString("msgInfo")+"\n"+req.getString("eitUser"));
            } else {
                sendPhoto.setCaption(req.getString("msgInfo"));
            }
                sendPhoto.setChatId(chatId);
            sendPhoto.setPhoto(new InputFile(locahostFile));
            zpayMessageBot.execute(sendPhoto);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("发送自定义消息失败：{}", e.toString());
            return false;
        } finally {
            if (null != locahostFile) {
                locahostFile.delete();
            }
        }
    }

    /**
     * 发送自定义消息
     *
     * @param req
     */
    @PostMapping("/send_2023s")
    public void sendCustomMsgBatchs(@RequestBody JSONObject req) {
        try {
            log.info("send_2023s发送自定义消息：{}", req.toJSONString());
            JSONObject params = new JSONObject();
            params.put("chatInfosLeng", req.getJSONArray("chatInfos").size());
            String sign = PayDigestUtil.getSign(params, MD5_KEY);
            if (!sign.equals(req.getString("verify_sign"))) {
                return;
            }
            req.getJSONArray("chatInfos").forEach(info -> {
                try {
                    Map msgInfo = (Map) info;
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(msgInfo.get("chatId").toString());
                    if (StringUtils.isNotBlank(req.getString("relyId"))) {
                        sendMessage.setReplyToMessageId(Integer.valueOf(msgInfo.get("relyId").toString()));
                    }
                    sendMessage.setParseMode("HTML");
                    sendMessage.setText(msgInfo.get("info").toString());
                    zpayMessageBot.executeAsync(sendMessage);
                } catch (Exception e) {
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            log.error("发送自定义消息失败：{}", e.toString());
        }
    }

    /**
     * 发送自定义消息
     *
     * @param req
     */
    @PostMapping("/send_2023sBatchAndFile")
    public void sendCustomMsgBatchsAndFile(@RequestBody JSONObject req) {
        try {
            JSONObject params = new JSONObject();
            params.put("chatInfosLeng", req.getJSONArray("chatInfos").size());
            String sign = PayDigestUtil.getSign(params, MD5_KEY);
            if (!sign.equals(req.getString("verify_sign"))) {
                return;
            }
            req.getJSONArray("chatInfos").forEach(info -> {
                Map msgInfo = (Map) info;
                SendDocument sendDocument = new SendDocument();
                sendDocument.setChatId(msgInfo.get("chatId").toString());
                String filePath = msgInfo.get("filePath").toString();
                String ctemplate = msgInfo.get("ctemplate").toString();
                InputFile inputFile = new InputFile();
                inputFile.setMedia(new File(filePath + ctemplate), ctemplate);
                sendDocument.setDocument(inputFile);
                try {
                    zpayMessageBot.executeAsync(sendDocument);
                } catch (Exception e) {
                    log.error("发送消息失败:{}", e.toString());
                }
                try {
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(msgInfo.get("chatId").toString());
                    if (StringUtils.isNotBlank(req.getString("relyId"))) {
                        sendMessage.setReplyToMessageId(Integer.valueOf(msgInfo.get("relyId").toString()));
                    }
                    sendMessage.setParseMode("HTML");
                    sendMessage.setText(msgInfo.get("info").toString());
                    zpayMessageBot.executeAsync(sendMessage);
                } catch (Exception e) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.error("发送自定义消息失败：{}", e.toString());
        }
    }

    public static void main(String[] args) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("chatId", "-918534289");
        System.out.println(PayDigestUtil.getSign(params, MD5_KEY));
    }


}
