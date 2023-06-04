package com.mborodin.uwm.api.bot;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TelegramUserData {

    private String id;
    private String uwmUserId;
    @JsonAlias("user_name")
    private String userName;
    @JsonAlias("first_name")
    private String firstName;
    @JsonAlias("last_name")
    private String lastName;
    @JsonAlias("photo_url")
    private String photoUrl;
    @JsonAlias("auth_date")
    private String authDate;
    private String hash;
}
