package com.xupt.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xupt.pojo.Users;
import org.apache.ibatis.annotations.Mapper;

/**
 * Mapper 接口
 *
 * @since 2022-05-30
 */
@Mapper
public interface UsersMapper extends BaseMapper<Users> {}
