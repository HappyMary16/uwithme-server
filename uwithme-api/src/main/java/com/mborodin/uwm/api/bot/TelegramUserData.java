package com.mborodin.uwm.api.bot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TelegramUserData {

    private String uwmUserId;
    private TelegramData telegramData;
}
