package cn.pzhu.forum.service;

import cn.pzhu.forum.content.ArticleType;
import cn.pzhu.forum.entity.Sort;

import java.util.List;

/**
 * @author impassivey
 */
public interface SortService {

    /**
     * 查询所有的分类信息
     *
     * @return 分类信息集合
     */
    List<Sort> list();

    /**
     * 根据ID获得指定的分类信息
     *
     * @param id 分类ID
     * @return 查询的分类信息
     */
    Sort get(int id);

    /**
     * 更改分类信息
     *
     * @param sort 需要更改的信息
     * @return 操作结果
     */
    boolean update(Sort sort);

    /**
     * 删除分类信息
     *
     * @param id 分类ID
     * @return 操作结果
     */
    boolean delete(int id);

    /**
     * 添加分类信息
     *
     * @param sort 分类信息
     * @return 操作结果
     */
    boolean add(Sort sort);

    /**
     * 根据总的分类信息查询分类集合
     *
     * @param articleType 分类信息
     * @return 分类信息集合
     */
    List<Sort> list(ArticleType articleType);

    /**
     * 根绝分类ID查询分类信息，如果ID为空，则查询一级分类
     * @return 分类信息
     * @param id 分类ID
     */
    List<Sort> listWithId(Integer id);
}
