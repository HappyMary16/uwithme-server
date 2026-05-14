package com.mborodin.uwm.api.bot;

import java.util.TreeMap;

public class TelegramData extends TreeMap<String, String> {

    public String getId() {
        return this.get("id");
    }

    public String getHash() {
        return this.get("hash");
    }
}
