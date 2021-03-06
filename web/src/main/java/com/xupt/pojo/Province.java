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
@TableName("province")
public class Province extends Model<Province> {

  private static final long serialVersionUID = 1L;

  /** 省的ID */
  private Integer id;

  /** 省的名字 */
  private String provinceName;
}
