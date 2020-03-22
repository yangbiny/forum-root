package cn.pzhu.forum.dao;

import cn.pzhu.forum.entity.Article;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ArticleDao {

    /**
     * 添加博客信息
     *
     * @param article 需要添加的博客信息
     * @return 添加结果
     */
    @Insert("insert into article set userId = #{userId}, title = #{title},userName = #{userName}," +
        "time = #{time},sortId = #{sortId},context = #{context},readNumber = 0,top = 0,contextMd = #{contextMd},"
        +
        "principal = #{principal},status = 0")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    Integer add(Article article);

    /**
     * 获得指定ID的博客信息
     *
     * @param id 博客的ID
     * @return 查询结果
     */
    @Select(
        "select article.id,title,userName,time,sort.name sortName,sortId,context,contextMd,readNumber,top,principal,status "
            +
            "from article,sort where sort.id = article.sortId and article.id = #{id}")
    @ResultMap("article")
    Article get(@Param("id") Integer id);

    /**
     * 获得所有的博客信息
     *
     * @return 所有的博客信息
     */
    @Select(
        "select article.id,title,userName,time,sort.name sortName,sortId,context,contextMd,readNumber,top,principal,status "
            + "from article,sort where sort.id = article.sortId")
    @Results(id = "article", value = {
        @Result(property = "id", column = "id"),
        @Result(property = "title", column = "title"),
        @Result(property = "userName", column = "userName"),
        @Result(property = "time", column = "time"),
        @Result(property = "sort.name", column = "sortName"),
        @Result(property = "sort.id", column = "sortId"),
        @Result(property = "context", column = "context"),
        @Result(property = "contextMd", column = "contextMd"),
        @Result(property = "readNumber", column = "readNumber"),
        @Result(property = "top", column = "top"),
        @Result(property = "principal", column = "principal"),
        @Result(property = "status", column = "status"),
    })
    List<Article> list();

    /**
     * 根据用户的ID查询用户发表的博客信息
     *
     * @param userName 用户昵称（唯一的）
     * @return 博客列表
     */
    @Select(
        "select article.id,title,userName,time,sort.name sortName,sortId,context,contextMd,readNumber,top,principal,status "
            +
            "from article,sort where sort.id = article.sortId and userName = #{userName}")
    @ResultMap("article")
    List<Article> userArticleList(@Param("userName") String userName);

    /**
     * 指定分类下的博客信息
     *
     * @param id 分类ID
     * @return 博客集合
     */
    @Select(
        "select article.id,title,userName,time,sort.name sortName,sortId,context,contextMd,readNumber,top,principal,status "
            +
            "from article,sort where sort.id = article.sortId and sortId = #{id} and status = 0")
    @ResultMap("article")
    List<Article> sortArticleList(@Param("id") int id);

    /**
     * 更新博客信息
     *
     * @param article 需要更新的博客信息
     * @return 更新结果
     */
    @Update("update article set title = #{title},context = #{context},contextMd = #{contextMd}" +
        ",sortId = #{sortId},status = 1 where id = #{id}")
    boolean update(Article article);

    /**
     * 删除指定的博客信息
     *
     * @param id 博客ID
     * @return 删除状态
     */
    @Update("update article status = 2 where id = #{id}")
    boolean delete(@Param("id") Integer id);


    /**
     * 置顶指定的博客信息
     *
     * @param id 博客ID
     * @return 置顶状态
     */
    @Update("update article set top = 1 where id = #{id}")
    boolean setTop(@Param("id") Integer id);

    /**
     * 根据指定的用户名和文章主要关键字查询文章
     *
     * @param userName  用户名
     * @param principal 主要关键字
     * @return 查询结果
     */
    @Select(
        "select article.id,title,userName,time,sort.name sortName,sortId,context,contextMd,readNumber,top,principal,status "
            +
            "from article,sort where sort.id = article.sortId and userName = #{userName} and principal = #{principal}")
    @ResultMap("article")
    Article getByPrincipal(@Param("userName") String userName,
        @Param("principal") String principal);

    /**
     * 更新阅读次数
     *
     * @param id 博客ID
     * @return 更新状态
     */
    @Update("update article set readNumber = readNumber + 1 where id = #{id}")
    boolean uploadReadNumber(@Param("id") Integer id);


    /**
     * 根据关键字模糊查询
     *
     * @param key 关键字
     * @return 查询结果
     */
    @Select(
        "select article.id,title,userName,time,sort.name sortName,sortId,context,contextMd,readNumber,top,principal "
            +
            "from article,sort where sort.id = article.sortId and title like #{key} and status = 0")
    @ResultMap("article")
    List<Article> keyList(@Param("key") String key);

    /**
     * 查询所有被点赞的博客
     * (结束分类ID存储分类出现次数)
     *
     * @return 博客集合
     */
    @Select("select sort.name sortName,count(sortId) sortId " +
        "from article,sort,record where sort.id = article.sortId and article.principal = record.articlePrincipal "
        +
        "and article.principal in (select articlePrincipal from record) GROUP BY sortName")
    @Results(id = "articles", value = {
        @Result(property = "sort.name", column = "sortName"),
        @Result(property = "sort.id", column = "sortId"),
    })
    List<Article> recordList();


    /**
     * 更新文章得状态信息
     *
     * @param id 文章ID
     * @param status 状态
     */
    @Update("update article set status = #{status} where id = #{id}")
    Integer updateArticleStatus(@Param("id") int id, @Param("status") Integer status);

    /**
     * 查询分页信息
     *
     * @param start 开始查询得位置
     * @param limit 查询得数量
     * @return 文章信息
     */
    @Select(
        "select article.id,title,userName,time,sortId,context,contextMd,readNumber,top,principal,status "
            + "from article where status = 0 limit #{start},#{limit}")
    @ResultMap("article")
    List<Article> listWithPageFroAdmin(@Param("start") int start, @Param("limit") int limit);

    @Select("select article.id,title,userName,time,sortId,context,contextMd,readNumber,top,principal,status "
                    + "from article where status = 0")
    @ResultMap("article")
    List<Article> selectPendingArticle();

    @Select("select article.id,title,userName,time,sortId,context,contextMd,readNumber,top,principal,status "
            + "from article where status = 0 and title like #{text} or userName like #{text} limit #{start},#{limit}")
    @ResultMap("article")
    List<Article> selectArticleByKeyword(@Param("text") String text, @Param("start") Integer start, @Param("limit") Integer limit);
}
