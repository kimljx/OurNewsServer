package com.ournews.service;

/**
 * Created by Misutesu on 2017/3/11 0011.
 */
public interface CommentService {
    String writeComment(String uid, String nid, String content, String time, String token, String key);

    String getComment(String uid, String nid, String page, String size, String sort);

    String writeChildComment(String uid, String cid, String content, String time, String token, String key);

    String lickComment(String cid, String uid, String token, String type);

    String getChildComment(String cid, String page, String size, String sort);

    String getCommentMessage(String uid, String token, String page, String size);
}
