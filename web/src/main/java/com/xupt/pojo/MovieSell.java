package com.xupt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * (MovieSell)表实体类
 *
 * @author ajian
 * @since 2022-06-03 17:08:51
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("movie_sell")
@AllArgsConstructor
public class MovieSell extends Model<MovieSell> {
  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;

  private Integer sellId;
  // 售票员赚得钱
  private Double sellMoney;
}
