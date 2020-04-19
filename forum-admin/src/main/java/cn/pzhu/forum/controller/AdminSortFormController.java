package cn.pzhu.forum.controller;

import cn.pzhu.forum.entity.Sort;
import cn.pzhu.forum.service.SortService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理员管理分类信息，需要admin角色信息
 */
@Slf4j
@RestController
@RequestMapping("/admin/sort")
@RequiresRoles("admin")
public class AdminSortFormController {

    /**
     * 提供管理员管理分类的服务
     */
    @Autowired
    private SortService sortService;

    /**
     * 添加分类信息
     *
     * @param sort 需要添加的分类的信息
     * @return 处理结果，msg的信息为success为处理成功,error为处理失败
     */
    @RequestMapping("add")
    public Map<String, String> add(Sort sort) {
        log.info("cn.pzhu.forum.controller.AdminSortFormController.add-添加分类信息-入参：" +
                "sort = {}", sort);
        Map<String, String> map = new HashMap<>();
        boolean add = sortService.add(sort);
        if (add) {
            map.put("msg", "成功!");
        } else {
            map.put("msg", "失败!");
        }
        return map;
    }

    /**
     * 更新分类信息
     *
     * @param sort 需要更新的分类的信息
     * @return 处理结果，msg的信息为success为处理成功,error为处理失败
     */
    @RequestMapping("update")
    public Map<String, String> update(Sort sort) {

        log.info("cn.pzhu.forum.controller.AdminSortFormController.update-更新分类新消息-入参:" +
                "sort = {}", sort.toString());

        Map<String, String> map = new HashMap<>();

        boolean add = sortService.update(sort);

        if (add) {
            map.put("msg", "成功!");
        } else {
            map.put("msg", "失败!");
        }

        return map;
    }

    /**
     * 删除分类信息
     *
     * @param id 分类的ID
     * @return 处理结果，msg的信息为success为处理成功,error为处理失败
     */
    @RequestMapping("delete")
    public Map<String, String> delete(int id) {

        log.info("cn.pzhu.forum.controller.AdminSortFormController.delete-删除分类新消息-入参：" +
                "sortId = {}", id);

        Map<String, String> map = new HashMap<>();

        boolean add = sortService.delete(id);

        if (add) {
            map.put("msg", "成功!");
        } else {
            map.put("msg", "删除失败，该分类下有其他分类信息!");
        }

        return map;
    }

}
