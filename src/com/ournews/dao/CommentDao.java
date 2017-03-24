package com.ournews.dao;

/**
 * Created by Misutesu on 2017/3/11 0011.
 */
public interface CommentDao {
    String writeComment(String uid, String nid, String content);

    String getComment(String nid, String page, String size, String sort);

    String getCommentUseUid(String uid, String nid, String page, String size, String sort);

    String likeComment(String cid, String uid, String type);

    String writeChildComment(String uid, String cid, String content);

    String getChildComment(String cid, String page, String size, String sort);

    String getCommentMessage(String uid, String page, String size);
}
