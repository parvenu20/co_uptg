package com.pay.tg.config.telegrame;


import com.pay.tg.service.TgMessageService;
import com.pay.tg.utils.ImageUtils;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.util.ImageHelper;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.grizzly.utils.BufferInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
public class ZpayMessageBot extends TelegramLongPollingBot {

    private final static Logger logger = LoggerFactory.getLogger(ZpayMessageBot.class);


    @Autowired
    private TgMessageService tgMessageService;

    @Value("${spring.profiles.active}")
    private String env;

    @Override
    public String getBotUsername() {
        //return ConfigBots.Bots.ZPAY_INDIA_TO_CHINA_BOT.getUsername();
        if ("product".equals(env)) {
            return ConfigBots.Bots.ZPAY_INDIA_TO_CHINA_BOT.getUsername();
        }
        return "single_spark_bot";
    }

    @Override
    public String getBotToken() {
        if ("product".equals(env)) {
            return ConfigBots.Bots.ZPAY_INDIA_TO_CHINA_BOT.getToken();
        }
        return "6015787646:AAFzhSCmBuFgKlkaEGXiligw7mcrAy0dG3U";
    }

    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        if (!update.hasMessage()) {
            return;
        }
        // and check if the message has text
        if (update.getMessage().hasText()) {
            String msg = update.getMessage().getText().split("@")[0];
            /*logger.info("获取到的文本消息: {},当前发送人：{},发送人姓名：{}", msg
                    , update.getMessage().getFrom().getFirstName(), update.getMessage().getFrom().getUserName());*/
            if (msg.contains("or") || msg.contains("and") || msg.contains("1=1") || msg.contains("where")
                    || msg.contains("delete") || msg.contains("mysql") || msg.contains("update")) {
                return;
            }
            if ("/key".equals(msg)) {
                tgMessageService.executeGetChatId(update);
            } /*else if ("/余额".equals(msg) || "/balance".equals(msg)) {
                tgMessageService.getPartnerBalance(update);
            } else if (msg.startsWith("/代收 ")) {
                tgMessageService.orderQuery(update);
            } else if (msg.equals("/10分钟") || msg.equals("/10minute")) {
                tgMessageService.successRate(update, "10分钟");
            } else if (msg.equals("/30分钟") || msg.equals("/30minute")) {
                tgMessageService.successRate(update, "30分钟");
            } else if (msg.startsWith("/代收异常 ")) {
                tgMessageService.orderNeedQuery(update);
            } else if (msg.contains("商户号") && "-959551691".equals(update.getMessage().getChatId().toString())) {
                tgMessageService.getPartnerOrderNo(update);
            }*/
        }
        // and check if the message has Phone
        /*if (update.getMessage().hasPhoto()) {
            String caption = update.getMessage().getCaption();
            if (caption.startsWith("/p ")) {
                tgMessageService.getQueryPhoneOrder(update);
            }
        }*/
    }


}
