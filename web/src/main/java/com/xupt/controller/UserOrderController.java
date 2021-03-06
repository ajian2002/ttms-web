package com.xupt.controller;

import static com.xupt.interceptor.loginInterceptor.getToken;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xupt.common.ApiController;
import com.xupt.common.R;
import com.xupt.pojo.AreaCinemas;
import com.xupt.pojo.HallSeat;
import com.xupt.pojo.Movie;
import com.xupt.pojo.MovieHall;
import com.xupt.pojo.MoviePlan;
import com.xupt.pojo.UserOrder;
import com.xupt.pojo.Users;
import com.xupt.service.AreaCinemasService;
import com.xupt.service.CinemaMoviesService;
import com.xupt.service.HallSeatService;
import com.xupt.service.MovieHallService;
import com.xupt.service.MoviePlanService;
import com.xupt.service.MovieService;
import com.xupt.service.UserOrderService;
import com.xupt.service.UsersService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * (UserOrder)表控制层
 *
 * @author ajian
 * @since 2022-06-03 17:04:50
 */
@Controller
@RequestMapping("/userOrder")
@ResponseBody
public class UserOrderController extends ApiController {
  /** 服务对象 */
  @Resource private UserOrderService userOrderService;

  @Resource private CinemaMoviesService cinemaMoviesService;
  @Resource private UsersService usersService;
  @Resource private MoviePlanService moviePlanService;
  @Resource private MovieService movieService;
  @Resource private MovieHallService movieHallService;
  @Resource private AreaCinemasService areaCinemasService;
  @Resource private HallSeatService hallSeatService;
  /**
   * 分页查询所有数据
   *
   * @param userOrderAndPage(page) 分页对象
   * @param userOrderAndPage(userOrder) 查询实体
   * @return 所有数据
   */
  @PostMapping
  public R selectAll(@RequestBody UserOrderAndPage<UserOrder> userOrderAndPage) {
    UserOrder userOrder = userOrderAndPage.getUserOrder();
    Page<UserOrder> page = userOrderAndPage.getPage();
    return success(this.userOrderService.page(page, new QueryWrapper<>(userOrder)));
  }

  @GetMapping("/getUserOrders")
  public R getUserOrders(@RequestHeader("token") String token) {
    Integer userId = getToken(token);
    Users users = usersService.getOne(new QueryWrapper<Users>(new Users().setUid(userId)));
    if (users == null) {
      return failed("用户不存在");
    }
    List<UserOrder> userOrders =
        userOrderService.list(new QueryWrapper<UserOrder>(new UserOrder().setUserId(userId)));
    List<HallSeat> hallSeats = new ArrayList<>(userOrders.size());
    for (UserOrder userOrder : userOrders) {
      HallSeat hallSeat =
          hallSeatService.getOne(
              new QueryWrapper<HallSeat>(new HallSeat().setOrderId(userOrder.getId())));
      hallSeats.add(hallSeat);
    }
    List<UserOrderAndHallSeat> userOrderAndSeats = new ArrayList<>(hallSeats.size());
    for (int i = 0; i < hallSeats.size(); i++) {
      UserOrderAndHallSeat UserOrderAndHallSeat = new UserOrderAndHallSeat();
      UserOrderAndHallSeat.setHallSeat(hallSeats.get(i));
      UserOrderAndHallSeat.setUserOrder(userOrders.get(i));
      userOrderAndSeats.add(UserOrderAndHallSeat);
    }
    return success(userOrderAndSeats);
  }

  //  /**
  //   * 新增数据
  //   *
  //   * @param userOrder 实体对象
  //   * @return 新增结果
  //   */
  //  @PostMapping("/new")
  //  public R insert(@RequestBody UserOrderDto userOrderDto) {
  //    try {
  //      // userid, planid, hallseat
  //      boolean isSuccess = this.userOrderService.save(userOrder);
  //      if (isSuccess) {
  //        return success(userOrder);
  //      }
  //    } catch (Exception e) {
  //      return failed("新增失败" + e.getMessage());
  //    }
  //    return failed("新增失败");
  //  }

  //  /**
  //   * 修改数据
  //   *
  //   * @param userOrder 实体对象
  //   * @return 修改结果
  //   */
  //  @PutMapping
  //  public R update(@RequestBody UserOrder userOrder) {
  //    try {
  //      // userid, planid, hallseat
  //      boolean isSuccess = this.userOrderService.updateById(userOrder);
  //      if (isSuccess) {
  //        userOrder = this.userOrderService.getById(userOrder.getId());
  //        return success(userOrder);
  //      }
  //    } catch (Exception e) {
  //      return failed("修改失败" + e.getMessage());
  //    }
  //    return failed("修改失败");
  //  }

  /**
   * 删除数据
   *
   * @param idList 主键结合
   * @return 删除结果
   */
  @DeleteMapping
  public R delete(@RequestParam("idList") List<Long> idList) {
    // order
    // seat
    // movieMoney
    idList.forEach(
        i -> {
          UserOrder userOrder = this.userOrderService.getById(i);
          try {
            userOrderService.removeById(i);
          } catch (Exception ignored) {
          }
          try {
            HallSeat hallSeat =
                hallSeatService.getOne(
                    new QueryWrapper<HallSeat>(new HallSeat().setOrderId(Math.toIntExact(i))));
            if (hallSeat != null) {
              hallSeat.setOrderId(-1);
              hallSeat.setTicketStatus(1);
              hallSeatService.updateById(hallSeat);
            }
          } catch (Exception ignored) {
          }
          try {
            Movie moive =
                movieService.getOne(
                    new QueryWrapper<Movie>(new Movie().setId(userOrder.getMovieId())));
            if (moive != null) {
              moive.setDayMoney(moive.getDayMoney() - userOrder.getOrderMoney());
              moive.setMovieMoney(moive.getMovieMoney() - userOrder.getOrderMoney());
              movieService.updateById(moive);
            }
          } catch (Exception ignored) {
          }
        });
    return success("删除成功（order,seat。，movieMoney）");
  }
  /** 购票 */
  @PostMapping("/new/buyTicket")
  public R buyTicket(
      @RequestHeader String token, @RequestBody UserOrderAndSeatDto userOrderAndSeat) {
    if (userOrderAndSeat == null
        || userOrderAndSeat.getUserOrderDto() == null
        || userOrderAndSeat.getHallSeatDto() == null) {
      return failed("购票失败，数据为空");
    }
    Integer userId = getToken(token);

    UserOrderDto userOrderDto = userOrderAndSeat.getUserOrderDto();
    HallSeatDto hallSeatDto = userOrderAndSeat.getHallSeatDto();
    // check user exist
    Users user = usersService.getOne(new QueryWrapper<>(new Users().setUid(userId)));
    if (user == null) return failed("用户不存在");
    // check plan exist
    MoviePlan plan =
        moviePlanService.getOne(
            new QueryWrapper<>(new MoviePlan().setId(userOrderDto.getPlanId())));
    if (plan == null) return failed("购票失败，演出计划为空");
    HallSeat hallSeat =
        hallSeatService.getOne(
            new QueryWrapper<>(
                new HallSeat()
                    .setSeatLine(hallSeatDto.getSeatLine())
                    .setSeatColumn(hallSeatDto.getSeatColumn())
                    .setMoviePlanId(plan.getId())));
    if (hallSeat == null) return failed("购票失败，座位不存在");

    if (new Date().after(plan.getMovieStartTime())) {
      return failed("演出已开始");
    }

    UserOrder userOrder = new UserOrder().setUserId(user.getUid());
    userOrder.setOrderMoney(userOrderDto.getOrderMoney());
    userOrder.setOrderStatus("已购买");
    userOrder.setPayTime(new Date());
    try {
      setPlanId(userOrder, plan.getId());
    } catch (Exception e) {
      failed(e.getMessage());
    }
    try {
      userOrderService.buyTicket(userOrder, hallSeat);
      return success(userOrder);
    } catch (Exception e) {
      return failed("购票失败，请重试. " + e.getMessage());
    }
  }

  @DeleteMapping("/returnTicket")
  public R returnTicket(@RequestHeader String token, @RequestParam("idList") List<Long> idList) {
    try {
      if (idList.size() != 1) return failed("退票失败，只能退一张票");
      UserOrder userOrder = userOrderService.getById(idList.get(0));
      if (userOrder == null || userOrder.getId() == null) return failed("退票失败");
      userOrder = userOrderService.getById(userOrder.getId());
      if (userOrder == null) return failed("订单不存在");

      if (new Date().after(userOrder.getMovieStartTime())) {
        throw new RuntimeException("演出已开始,不能退票");
      }

      Integer userId = getToken(token);
      if (userId == null) return failed("退票失败");
      if (!userId.equals(userOrder.getUserId())) return failed("您无权操作");
      Users users = usersService.getOne(new QueryWrapper<>(new Users().setUid(userId)));
      if (users == null) return failed("用户不存在");
      if (userOrderService.returnTicket(userOrder)) {
        return success(null);
      }
    } catch (Exception e) {
      return failed("服务器异常");
    }
    return failed("服务器异常");
  }

  private void setPlanId(UserOrder userOrder, Integer planId) {
    MoviePlan plan = moviePlanService.getOne(new QueryWrapper<>(new MoviePlan().setId(planId)));
    if (plan == null) throw new RuntimeException("订单计划不存在");
    MovieHall hall =
        movieHallService.getOne(new QueryWrapper<>(new MovieHall().setId(plan.getHallId())));
    if (hall == null) throw new RuntimeException("订单影厅不存在");
    AreaCinemas areaCinemas = areaCinemasService.getById(hall.getCinemaId());
    if (areaCinemas == null) throw new RuntimeException("订单影院不存在");
    Movie moive =
        movieService.getOne(new QueryWrapper<>(new Movie().setId(plan.getCinemaMovieId())));
    if (moive == null) throw new RuntimeException("订单电影不存在");
    userOrder.setCinemaName(areaCinemas.getCinemaName());
    userOrder.setMovieName(moive.getMovieName());
    userOrder.setCinemaId(areaCinemas.getId());
    userOrder.setTicketMoney(plan.getTicketMoney());
    userOrder.setMovieStartTime(plan.getMovieStartTime());
    userOrder.setHallName(hall.getHallName());
    userOrder.setMovieType(moive.getMovieType());
    userOrder.setMovieTime(moive.getMovieMinute());
    userOrder.setMovieId(moive.getId());
    userOrder.setMovieHead(moive.getMovieHead());
    userOrder.setPlanId(plan.getId());
  }
}

@Data
@Component
class UserOrderAndPage<T> {
  private UserOrder userOrder;
  private Page<T> page;
}

@Data
@AllArgsConstructor
class UserOrderAndSeatDto {
  private UserOrderDto userOrderDto;
  private HallSeatDto hallSeatDto;
}

@Data
@AllArgsConstructor
class UserOrderDto {
  private Double orderMoney;
  private Integer planId;
}

@Data
@AllArgsConstructor
class HallSeatDto {
  private Integer seatLine;
  // 座位的列
  private Integer seatColumn;
}

@Data
class UserOrderAndHallSeat {
  private UserOrder userOrder;
  private HallSeat hallSeat;
}
