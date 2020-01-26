package cn.pzhu.forum.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import org.springframework.util.CollectionUtils;

/**
 * @author impassivey
 */
public class ForumUtils {

  public static <S, R> List<R> toList(List<S> srcList, Function<S, R> convertFunction) {
    if (CollectionUtils.isEmpty(srcList)) {
      return Collections.emptyList();
    }
    List<R> dest = new ArrayList<>(srcList.size());
    for (S item : srcList) {
      if (item != null) {
        R apply = convertFunction.apply(item);
        if (apply != null) {
          dest.add(apply);
        }
      }
    }
    return dest;
  }


}
