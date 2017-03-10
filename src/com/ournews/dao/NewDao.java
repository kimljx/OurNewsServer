package com.ournews.dao;

/**
 * Created by Misutesu on 2017/1/16 0016.
 */
public interface NewDao {

    String addNews(String title, String cover, String abstractContent, String content, String type);

    String getHomeNews(String selectType);

    String getNewList(String type, String page, String size, String sort);

    String getSearchNew(String name, String page, String size, String sort);

    String getNewContentUser(String uid, String nid);

    String getNewContent(String id);

    String collectNew(String nid, String uid, String token, String type);

    String getCollections(String id, String token, String uid, String page, String size, String sort);

    String getHistory(String id, String token, String uid, String page, String size, String sort);

    String writeComment(String uId, String nId, String content);

    String getComment(String nid, String page, String size, String sort);
}
