package com.meguru.chatproject.user.controller;


import com.meguru.chatproject.common.domain.vo.response.ApiResult;
import com.meguru.chatproject.common.exception.BusinessErrorEnum;
import com.meguru.chatproject.common.utils.AssertUtil;
import com.meguru.chatproject.common.utils.RequestHolder;
import com.meguru.chatproject.user.domain.dto.SummaryInfoDTO;
import com.meguru.chatproject.user.domain.enums.RoleEnum;
import com.meguru.chatproject.user.domain.vo.request.user.*;
import com.meguru.chatproject.user.domain.vo.response.user.LoginSuccess;
import com.meguru.chatproject.user.domain.vo.response.user.UserInfoResp;
import com.meguru.chatproject.user.service.ICaptchaService;
import com.meguru.chatproject.user.service.ILoginService;
import com.meguru.chatproject.user.service.IRoleService;
import com.meguru.chatproject.user.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author Meguru
 * @since 2025-05-29
 */
@RestController
@RequestMapping("/capi/user")
@Api(tags = "用户相关接口")
public class UserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private ILoginService loginService;
    @Autowired
    private ICaptchaService captchaService;

    @PostMapping("/public/register")
    @ApiOperation("用户注册")
    public ApiResult<LoginSuccess> register(@Valid @RequestBody RegisterReq req) {
        return ApiResult.success(loginService.register(req));
    }

    @PostMapping("/public/login")
    @ApiOperation("用户登录")
    public ApiResult<LoginSuccess> login(@Valid @RequestBody LoginReq req) {
        return ApiResult.success(loginService.login(req));
    }

    @PostMapping("/public/sendCode")
    @ApiOperation("发送验证码")
    public ApiResult<Void> send(@Valid @RequestBody SendCodeReq req) {
        captchaService.sendEmailCaptcha(req.getEmail());
        return ApiResult.success();
    }

    @GetMapping("/userInfo")
    @ApiOperation("用户详情")
    public ApiResult<UserInfoResp> getUserInfo() {
        return ApiResult.success(userService.getUserInfo(RequestHolder.get().getUid()));
    }

    @PostMapping("/public/summary/userInfo/batch")
    @ApiOperation("用户聚合信息-返回的代表需要刷新的")
    public ApiResult<List<SummaryInfoDTO>> getSummeryUserInfo(@Valid @RequestBody SummaryInfoReq req) {
        return ApiResult.success(userService.getSummeryUserInfo(req));
    }

    @PutMapping("/name")
    @ApiOperation("修改用户名")
    public ApiResult<Void> modifyName(@Valid @RequestBody ModifyNameReq req) {
        userService.modifyName(RequestHolder.get().getUid(), req);
        return ApiResult.success();
    }

    @PutMapping("/black")
    @ApiOperation("拉黑用户")
    public ApiResult<Void> black(@Valid @RequestBody BlackReq req) {
        Long uid = RequestHolder.get().getUid();
        boolean hasPower = roleService.hasPower(uid, RoleEnum.ADMIN);
        AssertUtil.isTrue(hasPower, BusinessErrorEnum.USER_POWER_ERROR);
        userService.black(req);
        return ApiResult.success();
    }
}
