package cn.pzhu.forum.controller;

import cn.pzhu.forum.entity.Major;
import cn.pzhu.forum.service.MajorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员专业管理页面,需要admin的角色
 */
@RestController
@Slf4j
@RequestMapping("/admin")
@RequiresRoles("admin")
public class AdminMajorFormController {

    /**
     * 提供专业管理的服务
     */
    @Autowired
    private MajorService majorService;

    /**
     * 添加专业信息
     *
     * @param major 专业信息
     * @return 处理结果，msg的信息为success为处理成功,error为处理失败
     */
    @RequestMapping("/addMajor")
    public Map<String, String> addMajor(Major major) {

        log.info("cn.pzhu.forum.controller.AdminMajorFormController.addMajor-添加专业信息-入参：" +
                "major = {}", major.toString());

        Map<String, String> map = new HashMap<>();

        boolean flag = majorService.add(major);
        if (flag) {
            map.put("msg", "success");
        } else {
            map.put("msg", "error");
        }

        return map;
    }

    /**
     * 查询指定学院下的专业信息
     *
     * @param id 学院的ID
     * @return 处理结果，msg的信息为success为处理成功,error为处理失败
     */
    @RequestMapping(value = "/list", headers = "Accept=application/json")  // 要求将List转换为JSON
    public List<Major> list(int id) {

        log.info("cn.pzhu.forum.controller.AdminMajorFormController.list-查询指定学院的专业信息-入参：" +
                "schooldId = {}", id);

        List<Major> list = majorService.list(id);

        log.info("cn.pzhu.forum.controller.AdminMajorFormController.list-返回值-list长度为：{}", list == null ? null : list.size());
        return list;
    }

    /**
     * 更新专业信息
     *
     * @param major 需要更新的专业的信息
     * @return 处理结果，msg的信息为success为处理成功,error为处理失败
     */
    @RequestMapping("/updateMajor")
    public Map<String, String> update(Major major) {

        log.info("cn.pzhu.forum.controller.AdminMajorFormController.update-更新专业信息-入参：" +
                "major = {}", major.toString());

        Map<String, String> map = new HashMap<>();

        boolean update = majorService.update(major);

        if (update) {
            map.put("msg", "success");
        } else {
            map.put("msg", "error");
        }

        return map;
    }

    /**
     * 删除专业额信息
     *
     * @param id 需要删除的专业的ID
     * @return 处理结果，msg的信息为success为处理成功,error为处理失败
     */
    @RequestMapping("/deleteMajor")
    public Map<String, String> delete(int id) {

        log.info("cn.pzhu.forum.controller.AdminMajorFormController.delete-删除专业信息-入参：" +
                "majorId = {}", id);

        Map<String, String> map = new HashMap<>();

        boolean delete = majorService.delete(id);

        if (delete) {
            map.put("msg", "success");
        } else {
            map.put("msg", "error");
        }

        return map;
    }

}
