package com.xupt.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xupt.pojo.MovieHall;
import com.xupt.service.MovieHallService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (MovieHall)表控制层
 *
 * @author ajian
 * @since 2022-06-03 16:47:20
 */
@Controller
@RequestMapping("/movieHall")
@ResponseBody
public class MovieHallController extends ApiController {
  /** 服务对象 */
  @Resource private MovieHallService movieHallService;

  /**
   * 分页查询所有数据
   *
   * @param page 分页对象
   * @param movieHall 查询实体
   * @return 所有数据
   */
  @GetMapping
  public R selectAll(Page<MovieHall> page, MovieHall movieHall) {
    return success(this.movieHallService.page(page, new QueryWrapper<>(movieHall)));
  }

  /**
   * 通过主键查询单条数据
   *
   * @param id 主键
   * @return 单条数据
   */
  @GetMapping("{id}")
  public R selectOne(@PathVariable Serializable id) {
    return success(this.movieHallService.getById(id));
  }

  /**
   * 新增数据
   *
   * @param movieHall 实体对象
   * @return 新增结果
   */
  @PostMapping
  public R insert(@RequestBody MovieHall movieHall) {
    return success(this.movieHallService.save(movieHall));
  }

  /**
   * 修改数据
   *
   * @param movieHall 实体对象
   * @return 修改结果
   */
  @PutMapping
  public R update(@RequestBody MovieHall movieHall) {
    return success(this.movieHallService.updateById(movieHall));
  }

  /**
   * 删除数据
   *
   * @param idList 主键结合
   * @return 删除结果
   */
  @DeleteMapping
  public R delete(@RequestParam("idList") List<Long> idList) {
    return success(this.movieHallService.removeByIds(idList));
  }
}
