package cn.pzhu.forum.service;

import cn.pzhu.forum.entity.School;

import java.util.List;

public interface SchoolService {

    List<School> list();

    boolean add(String name);

    boolean update(School school);

    boolean delete(int id);


}
