package com.ournews.dao;

import com.ournews.bean.Comment;
import com.ournews.bean.New;

import java.util.List;
import java.util.Map;

/**
 * Created by Misutesu on 2017/1/16 0016.
 */
public interface NewDao {

    boolean addNews(String title, String cover, String abstractContent, String content, String createTime, String type);

    Map<Integer,List<New>> getHomeNews(String selectType);

    List<New> getNewList(int type, int page, int size, int sort);

    List<New> searchNew(String name, int page, int size, int sort);

    int userBrowseNew(long nid, long uid, String time);

    New getNewContent(long id);

    int collectNew(long uid, long nid, int type);

    int writeComment(long uId, long nId, String content, String createTime);

    List<Comment> getComment(long nid, int page, int size, int sort);
}
