package com.hxs.xposedreddevil.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class DbCarryList {
    @Id(autoincrement = true)
    private Long id;
    public String name;
    public String money;
    public String time;
    public String status;
    @Generated(hash = 714541733)
    public DbCarryList(Long id, String name, String money, String time,
            String status) {
        this.id = id;
        this.name = name;
        this.money = money;
        this.time = time;
        this.status = status;
    }
    @Generated(hash = 1408936085)
    public DbCarryList() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getMoney() {
        return this.money;
    }
    public void setMoney(String money) {
        this.money = money;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }


}
