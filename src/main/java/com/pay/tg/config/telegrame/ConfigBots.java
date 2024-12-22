package com.pay.tg.config.telegrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.commands.DeleteMyCommands;
import org.telegram.telegrambots.meta.api.methods.commands.GetMyCommands;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeAllChatAdministrators;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ConfigBots {

    private final static Logger logger = LoggerFactory.getLogger(ConfigBots.class);

    public enum Bots {
        ZPAY_INDIA_TO_CHINA_BOT("helpSpringBot", "6592775822:AAGy4crdVANzpshk2j6nLt08L_DM7UisyvI");

        private final String username;
        private final String token;

        // 构造方法
        Bots(String username, String token) {
            this.username = username;
            this.token = token;
        }

        public String getUsername() {
            return username;
        }

        public String getToken() {
            return token;
        }
    }

    @Autowired
    private ZpayMessageBot zpayMessageBot;

    @Bean
    public TelegramBotsApi telegramBotsApi() throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(zpayMessageBot);
       /* List<BotCommand> commands = new ArrayList<>();
        commands.add(new BotCommand("id", "群信息"));
        commands.add(new BotCommand("balance", "余额查询"));
        commands.add(new BotCommand("p", "/代收 商户订单号"));
        commands.add(new BotCommand("10minute", "10分钟转化率"));
        commands.add(new BotCommand("30minute", "30分钟转化率"));
        SetMyCommands cmds = new SetMyCommands();
        cmds.setCommands(commands);
        //cmds.setLanguageCode("en");
        cmds.setScope(new BotCommandScopeDefault());
        Boolean aBoolean = zpayMessageBot.execute(cmds);
        logger.info("执行效果：{}",aBoolean);
       *//* DeleteMyCommands cmds = new DeleteMyCommands();
        cmds.setLanguageCode("en");
        cmds.setScope(new BotCommandScopeAllChatAdministrators());
        Boolean execute = zpayMessageBot.execute(cmds);
        logger.info("执行效果：{}",execute);*//*
        GetMyCommands getCmds = new GetMyCommands();
        //getCmds.setLanguageCode("en");
        getCmds.setScope(new BotCommandScopeDefault());
        ArrayList<BotCommand> botCommands = zpayMessageBot.execute(getCmds);
        logger.info("获取：");
        botCommands.forEach(info->{
            logger.info("备注：{}",info.getCommand());
        });*/
        return botsApi;
    }

}
