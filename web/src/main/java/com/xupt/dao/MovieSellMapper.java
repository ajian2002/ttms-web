package com.xupt.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xupt.pojo.MovieSell;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * (MovieSell)表数据库访问层
 *
 * @author ajian
 * @since 2022-06-03 17:08:51
 */
public interface MovieSellMapper extends BaseMapper<MovieSell> {

  /**
   * 批量新增数据（MyBatis原生foreach方法）
   *
   * @param entities List<MovieSell> 实例对象列表
   * @return 影响行数
   */
  int insertBatch(@Param("entities") List<MovieSell> entities);

  /**
   * 批量新增或按主键更新数据（MyBatis原生foreach方法）
   *
   * @param entities List<MovieSell> 实例对象列表
   * @return 影响行数
   * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
   */
  int insertOrUpdateBatch(@Param("entities") List<MovieSell> entities);
}
