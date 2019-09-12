package com.hxs.xposedreddevil.utils;

import com.hxs.xposedreddevil.base.BaseApplication;
import com.hxs.xposedreddevil.greendao.DaoSession;
import com.hxs.xposedreddevil.greendao.DbCarryList;
import com.hxs.xposedreddevil.greendao.DbCarryListDao;

import java.util.List;

public class SQLiteUtils {
    private static SQLiteUtils instance;
    DbCarryListDao dbCarryListDao;
    DaoSession daoSession;

    private SQLiteUtils() {
        dbCarryListDao = BaseApplication.getInstances().getDaoSession().getDbCarryListDao();
        daoSession = BaseApplication.getInstances().getDaoSession();
    }

    public static SQLiteUtils getInstance() {
        if (instance == null) {
            synchronized (SQLiteUtils.class) {
                if (instance == null) {
                    instance = new SQLiteUtils();
                }
            }
        }
        return instance;
    }

    //增加
    public void addContacts(DbCarryList testBean) {
        dbCarryListDao.insert(testBean);
    }

    //分页查询
    public List<DbCarryList> selectPageContacts(int page) {
        return daoSession.queryBuilder(DbCarryList.class).offset(page * 50).limit(50).list();
//        return dbCarryListDao.loadAll();
    }

    /**
     * 按名字查询
     *
     * @param name
     * @return
     */
    public List<DbCarryList> queryNameEq(String name) {
        return dbCarryListDao.queryBuilder().where(DbCarryListDao.Properties.Name.like("%" + name + "%")).list();
    }

    /**
     * 按时间查询
     *
     * @param time
     * @return
     */
    public List<DbCarryList> queryTimeEq(String time) {
        return dbCarryListDao.queryBuilder().where(DbCarryListDao.Properties.Time.like("%" + time + "%")).list();
    }

    //删除
    public void deleteContacts(DbCarryList testBean) {
        dbCarryListDao.delete(testBean);
    }

    //修改
    public void updateContacts(DbCarryList testBean) {
        dbCarryListDao.update(testBean);
    }

    //条件查询
//    public List selectAllContacts() {
//        dbCarryListDao.detachAll();//清除缓存
//        List list1 = dbCarryListDao.queryBuilder().where(dbCarryListDao.Properties.LogingId.eq(ConfigUtils.getUid())).build().list();
//        return list1 == null ? new ArrayList() : list1;
//    }

    //删除表中内容
    public void deleteAllContact() {
        dbCarryListDao.deleteAll();
    }
}
