package com.mborodin.uwm.client.client;

import com.mborodin.uwm.api.bot.TelegramUserData;
import com.mborodin.uwm.client.config.AuthClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "uwm-bot",
        url = "#{'${uwm-bot.api.url}'}",
        configuration = {AuthClientConfiguration.class})
public interface UwmBotClient {

    @PostMapping("/uwm-bot/auth")
    void authInTelegramBot(TelegramUserData userData);
}
