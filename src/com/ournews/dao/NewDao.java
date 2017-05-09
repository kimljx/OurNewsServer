package com.ournews.dao;

/**
 * Created by Misutesu on 2017/1/16 0016.
 */
public interface NewDao {

    String addNews(String mid, String title, String cover, String abstractContent, String content, String type, String push);

    String getOwnNew(String mid, String page, String size, String sort);

    String changeNewState(String id, String nid, String state);

    String getHomeNews(String selectType);

    String getNewList(String type, String page, String size, String sort);

    String getSearchNew(String name, String page, String size, String sort);

    String getNewContentUser(String uid, String nid);

    String getNewContent(String id);

    String getNewContentUserForWeb(String uid, String nid);

    String getNewContentForWeb(String id);

    String collectNew(String nid, String uid, String type);

    String getCollections(String id, String uid, String page, String size, String sort);

    String getHistory(String id, String uid, String page, String size, String sort);
}
