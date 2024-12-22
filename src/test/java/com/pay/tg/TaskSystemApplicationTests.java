package com.pay.tg;

import com.pay.tg.common.Constants;
import com.pay.tg.config.telegrame.ZpayMessageBot;
import com.pay.tg.service.TgMessageService;
import com.pay.tg.utils.PayDigestUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.SortedMap;
import java.util.TreeMap;

@SpringBootTest(classes = {TgApplication.class})
@RunWith(SpringRunner.class)
public class TaskSystemApplicationTests {

    private final static Logger logger = LoggerFactory.getLogger(TaskSystemApplicationTests.class);


    @Autowired
    private TgMessageService messageService;

    @Autowired
    private ZpayMessageBot zpayMessageBot;

    @Test
    public void contextLoads() throws TelegramApiException {


        String msg = "/us 59319424394  71f8fcd48d934cc7b4567c4ec9cc8ec0\n" +
                "/us 322728681830  f4c3f3187b1b46b28f236548ce2c3e19\n" +
                "/us 312725777983  28ce5476b2434a37ab653b14840ae7d3\n" +
                "/us 322709536087  92efd998e6a44089b291d121b865f9a0\n" +
                "/us 359341627738  bd05be9179ae4ba281a3d9cbebdb122f\n" +
                "/us 359416063797  4db6c43cd7e64d79b0e3a8ed295ec267\n" +
                "/us 322804259247  e62d99262fa9417bae57f1f158d056a8\n" +
                "/us 322847666460  0d0ac43c4fbf4ce0a82c4b46208e001d\n" +
                "/us 322843244789  bf73ae1fb62940c7b0fbd1f0d3daac2e\n" +
                "/us 322915549216  24b5e33d97a64a04a8a075ce1cb5595f\n" +
                "/us 322923978789  f95042c49df0429ab4bcb753d9197d42\n" +
                "/us 322968444690  d63a19a981b34fdcae6dede26300bc12\n" +
                "/us 322980049453  589cafbd51594613847c59f9019d6561\n" +
                "/us 322909387157  6f6bb80f2a7e4d8cb70c9254b337d360\n" +
                "/us 322982610803  7314859b7b9b4c5eb0736663a8dae9da\n" +
                "/us 322965517945  738bdff294ad4ba89af9f24b2c25cc4b\n" +
                "/us 323062715806  29b594c4be8848baa37d6cc53b93d39a\n" +
                "/us 323038982612  c0cd60cbba9843778773b044a5c3f52b\n" +
                "/us 323085502899  e9877bd21a024e24915cab921f9a27da\n" +
                "/us 359612112566  930dc55b6aa04669aa0eebbee063cf4d\n" +
                "/us 323023609389  05d94321ea684a2197dccd92c936f6dc\n" +
                "/us 323054644892  aede455edc254bf3ab63c6200c769b87\n" +
                "/us 323018638910  6fb3bf070dc44724a6a9fcb641f99d85\n" +
                "/us 359642410005  c60b4f4241c747f0b89f94fa8b805152\n" +
                "/us 323125034553  f636dbe3c25d4a96a40defe2ac47bba7\n" +
                "/us 323112063778  39001934833d4ee993e636e8fe6a8a15\n" +
                "/us 323174056509  960e21c07b9844989846137cc772d92a\n" +
                "/us 359711608983  50d28e4552ad49519664308e64ff1add\n" +
                "/us 323109770553  90d501d23a8248aea9e84a50a17ff48e\n" +
                "/us 323170999293  4437f058abe249ffabd32e1ebf18eace\n" +
                "/us 323146950615  4fe8a548b94a4e868ff8763b9caf312a\n" +
                "/us 323166711744  b00f2dfdce034b4ea1d02c40062a81d0\n" +
                "/us 323163381143  f4f79ad441dc470eb6d755e1dc2cfb4b\n" +
                "/us 323157429172  65e2a1d6e26947af9db327f8dd34cc00\n" +
                "/us 323269536141  8eeaf229eccc4a65984d0f7a66054eae\n" +
                "/us 323007971625  8cc30359c3574ef8b1a80381d926c441\n" +
                "/us 359816543519  53ab968cd49345f58f9b8b8186d2d44e\n" +
                "/us 323225697224  9256ccc7556d4bf686894f3973b1a400\n" +
                "/us 323236942053  7aea37a011e74e9e85fadfa96a2d77f0\n" +
                "/us 323292539797  c7ba4a6bf60b4b9582b54cc9a2ecaf2f\n" +
                "/us 323255010909  11c9e4a9c33f4202bdbdea80b44378d8\n" +
                "/us 323204068354  332d8a93a2c24537bb2283232978373c\n" +
                "/us 323291845573  c8770744b32d4f6f9546a4a630340309\n" +
                "/us 359832999217  ba67bb3b25da4822b2bd4c3e73bf4f9c\n" +
                "/us 323231904189  7ce367e64cf14bff8783e7da93f791e8\n" +
                "/us 323296712943  32bb97d295ce416cbb3df09bea434273\n" +
                "/us 359838387163  8f2c136d453444c78360d5ae96a76011\n" +
                "/us 323285957409  c0810708090b4c48a6f9dee3d8dcf522\n" +
                "/us 323254747297  67db41a4d23744689cd5ce350f36c3f3\n" +
                "/us 323261144873  fb4c1efe83b842f18037584797224ee8\n" +
                "/us 323241361267  e81c1117a2534f7ea276de4f5a9d6457\n" +
                "/us 323299252871  56274b5d3fa04bfdba000ed93aacf03f\n" +
                "/us 323215258247  1cc5cba94b2e43f6a3dd34f914e445c7\n" +
                "/us 359836110481  701ff07571b843c88d7f39d2e0f73517\n" +
                "/us 323243005709  e7bbe9ad68594a85b744eb8727707ebe\n" +
                "/us 323299553348  1c89f8f1d4c9422d8998af2450ac328d\n" +
                "/us 323216577603  c5471b03d48249e4a2cdfb45e2a0e6f5\n" +
                "/us 323250499753  f9cad0188c694054b97dcfec18ac32a4\n" +
                "/us 323206099293  2f24a3db08b64badadd07bef2b962517\n" +
                "/us 323230284863  7d0286cfc41d45a3acd602d3fd8a33cd\n" +
                "/us 323246246993  9c2a62e69da74842980da691b15a4718\n" +
                "/us 359845666672  b37a0def810f4cd791f295cdf066a4b3\n" +
                "/us 323273644750  8e8379d14e8e4361b2328ab41a835346\n" +
                "/us 323209086518  7ac21ab75d2748c48fc7a856659ae9a9\n" +
                "/us 323291284208  e8a4fdcb2b2940d081d4300aaa57019b\n" +
                "/us 323248652643  14593227f18b4029b83a6e737913cb0e\n" +
                "/us 323205391483  dbf450378a8e4c0a92dd263b9b760bdb\n" +
                "/us 323231197089  dbc8f20e2ff04857a77045a9630dd087\n" +
                "/us 323214177776  fda2b6d4aa9b448a98cd0f0c12dabcef\n" +
                "/us 323293501842  c9f3eef6d3f24393939c3c312133f644\n" +
                "/us 323212516096  a21b7b7dd35f4f3483a3ff38ee3835e6\n" +
                "/us 323217907136  e10c646c71b34f55be4abb395cd39787\n" +
                "/us 323269962670  335c90876a5046ea94d8740344834270\n" +
                "/us 359848614880  46ef70459ad1420c98a7a88ea91f9452\n" +
                "/us 323236507473  334e6199d7cf424e9f4eefe709b3fc48\n" +
                "/us 323282201140  76c274df5b5b47f18e47b28a31c2d303\n" +
                "/us 359849507939  e80ddc0111ea4b48956855e593c596b4\n" +
                "/us 323205947222  c0f5dc3f806b47bfa279319db12ff78e\n" +
                "/us 323244196478  f8e02b197a8b4648b7354507e3968956\n" +
                "/us 323231941146  c905230f6e8a41cc9740375a4d255e1d\n" +
                "/us 359842051579  45521d0db61b46f9bd6cc06fd8ea2907";
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId("-666220555");
        sendMessage.setParseMode("HTML");
        sendMessage.setText(msg);
        zpayMessageBot.execute(sendMessage);

    }


    @Test
    public void test() throws Exception {
        try {
            /*URL url1 = new URL("https://api.telegram.org/file/bot5727099520:AAGJRxxXHfuxiU1M243W0n8REg8WQv9lWdY/photos/file_4.jpg");
            FileUtils.copyURLToFile(url1, file1);*/
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId("-941575312");
            sendPhoto.setCaption("/py 410191290644  Test202404111711590775ddad");
            InputFile inputFile = new InputFile();
            File file1 = new File("C:\\Users\\Administrator\\Desktop\\photo_2024-04-11_14-57-22.jpg");
            inputFile.setMedia(file1);
            sendPhoto.setPhoto(inputFile);
            zpayMessageBot.execute(sendPhoto);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    @Test
    public void SendMses() throws Exception {

        try {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("avw2fRzDkjYaNdJhSjtCiF");
            sendMessage.setChatId("-910010343");
            zpayMessageBot.execute(sendMessage);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    public static void main(String[] args) {

    }


}
