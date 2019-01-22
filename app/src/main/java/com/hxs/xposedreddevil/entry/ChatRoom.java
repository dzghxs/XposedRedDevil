package com.hxs.xposedreddevil.entry;

import org.litepal.crud.DataSupport;

public class ChatRoom extends DataSupport{
    String name = "";
    String displayname = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }
}
