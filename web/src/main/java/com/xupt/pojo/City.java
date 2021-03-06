package com.xupt.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/** @since 2022-05-30 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("city")
public class City extends Model<City> {

  private static final long serialVersionUID = 1L;

  /** 城市的id */
  private Integer id;

  /** 城市的名字 */
  private String cityName;

  /** 省的Id */
  private Integer provinceId;
}
