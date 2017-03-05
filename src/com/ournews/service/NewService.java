package com.ournews.service;

/**
 * Created by Misutesu on 2017/3/5 0005.
 */
public interface NewService {
    String addNew(String title, String cover, String abstractContent, String content, String type);

    String getHomeNew(String type);

    String getTypeNew(String type, String page, String size, String sort);

    String getSearchNew(String name, String page, String size, String sort);

    String getContent(String uid, String nid);

    String writeComment(String uid, String nid, String content, String time, String key);

    String getComment(String nid, String page, String size, String sort);
}
