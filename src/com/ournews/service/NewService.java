package com.ournews.service;

/**
 * Created by Misutesu on 2017/3/5 0005.
 */
public interface NewService {
    String addNew(String mid, String token, String title, String cover, String abstractContent, String content, String type, String push);

    String getHomeNew(String type);

    String getTypeNew(String type, String page, String size, String sort);

    String getSearchNew(String name, String page, String size, String sort);

    String getContent(String uid, String nid, int type);

    String collectionNew(String nid, String uid, String token, String type);

    String getCollections(String id, String token, String uid, String page, String size, String sort);

    String getHistory(String id, String token, String uid, String page, String size, String sort);
}
