package com.xupt.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xupt.pojo.MoviePlan;
import java.util.List;

/**
 * Mapper 接口
 *
 * @since 2022-05-30
 */
public interface MoviePlanMapper extends BaseMapper<MoviePlan> {
  List<MoviePlan> getMovieListByDate(String date1, String date2);
}
