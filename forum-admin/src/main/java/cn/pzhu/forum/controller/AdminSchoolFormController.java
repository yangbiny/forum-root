package cn.pzhu.forum.controller;

import cn.pzhu.forum.entity.School;
import cn.pzhu.forum.service.SchoolService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理员管理学院信息，需要admin角色信息
 */
@RestController
@Slf4j
@RequestMapping("/admin")
@RequiresRoles("admin")
public class AdminSchoolFormController {

    /**
     * 提供学院信息维护服务
     */
    @Autowired
    private SchoolService schoolService;

    /**
     * 添加学院信息
     *
     * @param name 学院名称
     * @return 处理结果，msg的信息为success为处理成功,error为处理失败
     */
    @RequestMapping("/addSchool")
    public Map<String, String> addSchool(String name) {

        log.info("cn.pzhu.forum.controller.AdminSchoolFormController.addSchool-添加学院信息-入参：" +
                "schoolName = {}", name);

        Map<String, String> map = new HashMap<>();

        boolean flag = schoolService.add(name);

        if (flag) {
            map.put("msg", "成功!");
        } else {
            map.put("msg", "失败!");
        }

        return map;
    }

    /**
     * 更新学院信息
     *
     * @param school 需要更新的学院信息
     * @return 处理结果，msg的信息为success为处理成功,error为处理失败
     */
    @RequestMapping("/updateSchool")
    public Map<String, String> updateSchool(School school) {

        log.info("cn.pzhu.forum.controller.AdminSchoolFormController.updateSchool-修改学院信息-入参：" +
                "school = {}", school.toString());

        boolean flag = schoolService.update(school);

        Map<String, String> map = new HashMap<>();

        if (flag) {
            map.put("msg", "成功!");
        } else {
            map.put("msg", "失败!");
        }

        return map;
    }

    /**
     * 删除学院信息
     *
     * @param id 学院ID
     * @return 处理结果，msg的信息为success为处理成功,error为处理失败
     */
    @RequestMapping("/deleteSchool")
    public Map<String, String> deleteSchool(int id) {

        log.info("cn.pzhu.forum.controller.AdminSchoolFormController.deleteSchool-删除学院-入参：" +
                "schoolId = {}", id);

        boolean flag = schoolService.delete(id);

        Map<String, String> map = new HashMap<>();

        if (flag) {
            map.put("msg", "成功!");
        } else {
            map.put("msg", "失败!");
        }

        return map;
    }

}
