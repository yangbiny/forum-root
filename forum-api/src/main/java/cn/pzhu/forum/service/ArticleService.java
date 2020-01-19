package cn.pzhu.forum.service;

import cn.pzhu.forum.content.ArticleType;
import cn.pzhu.forum.entity.Article;

import java.util.List;

public interface ArticleService {

    /**
     * 添加博客信息
     *
     * @param article 需要添加的博客信息
     * @return 添加结果
     */
    String add(Article article);

    /**
     * 获得指定ID的博客信息
     *
     * @param id 博客的ID
     * @return 查询结果
     */
    Article get(Integer id);

    /**
     * 获得所有的博客信息
     *
     * @return 所有的博客信息
     */
    List<Article> list();

    /**
     * 根据分类ID查询文章信息
     *
     * @param sortId 分类ID
     * @param number 需要的文章个数
     * @return 该分类下的新闻ID
     */
    List<Article> list(Integer sortId, int number);

    /**
     * 根据传入的文章的大方面的分类信息查询文章
     *
     * @param articleType 文章分类信息
     * @param number      一次性查询的个数
     * @return 文章的集合
     */
    List<Article> list(ArticleType articleType, int number);

    /**
     * 根据用户的ID查询用户已经发表的所有博客
     *
     * @param userId 用户ID
     * @return 博客列表
     */
    List<Article> userList(String userId);

    /**
     * 根据大的类别查询置顶列表
     *
     * @param articleType 类别
     * @param number      博客数量
     * @return 博客集合
     */
    List<Article> topList(ArticleType articleType, int number);

    /**
     * 根据关键字查询博客信息
     *
     * @param key 关键字
     * @return 博客集合
     */
    List<Article> list(String key);

    /**
     * 更新博客信息
     *
     * @param article 需要更新的博客信息
     * @return 更新结果
     */
    boolean update(Article article);

    /**
     * 删除指定的博客信息
     *
     * @param id 博客ID
     * @return 删除状态
     */
    boolean delete(Integer id);

    /**
     * 置顶指定的博客信息
     *
     * @param id 博客ID
     * @return 置顶状态
     */
    boolean setTop(Integer id);


    /**
     * 根据用户名和文章主要关键字principal查询结果
     *
     * @param userName  文章发表用户的用户名
     * @param principal 文章的主要关键字
     * @return 查询结果
     */
    Article get(String userName, String principal);

    /**
     * 用户点赞
     *
     * @param userName  点赞用户
     * @param principal 点赞文章的主要关键字
     * @return 点赞结果
     */
    Integer like(String userName, String principal);

    /**
     * 统计当前博客的点赞数
     *
     * @param principal 博客的主要关键字
     * @return 总数
     */
    Integer likeCount(String principal);

    /**
     * 用于判断指定用户是否已经点赞当前为文章
     *
     * @param userName  用户名
     * @param principal 博客主要关键字
     * @return false表示没有点赞，true表示已经点赞
     */
    boolean hashLiked(String userName, String principal);

    /**
     * 根据博客的标题查询类似的博客信息
     *
     * @param title  博客的标题
     * @param number 博客数量
     * @return 类似文章的集合
     */
    List<Article> similar(String title, int number);

    /**
     * 统计文章数量
     *
     * @return 文章总数
     */
    int articleCount();

    /**
     * 点赞次数统计（整个论坛）
     *
     * @return 点赞次数
     */
    int recordCount();

}
