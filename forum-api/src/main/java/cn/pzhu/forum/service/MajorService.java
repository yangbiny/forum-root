package cn.pzhu.forum.service;

import cn.pzhu.forum.entity.Major;

import java.util.List;

public interface MajorService {

    List<Major> list(int id);

    boolean add(Major major);

    boolean update(Major major);

    boolean delete(int id);

}
