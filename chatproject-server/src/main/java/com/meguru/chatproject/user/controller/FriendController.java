package com.meguru.chatproject.user.controller;


import com.meguru.chatproject.common.domain.vo.request.CursorPageBaseReq;
import com.meguru.chatproject.common.domain.vo.request.PageBaseReq;
import com.meguru.chatproject.common.domain.vo.response.ApiResult;
import com.meguru.chatproject.common.domain.vo.response.CursorPageBaseResp;
import com.meguru.chatproject.common.domain.vo.response.PageBaseResp;
import com.meguru.chatproject.common.utils.RequestHolder;
import com.meguru.chatproject.user.domain.vo.request.friend.FriendApplyReq;
import com.meguru.chatproject.user.domain.vo.request.friend.FriendApproveReq;
import com.meguru.chatproject.user.domain.vo.request.friend.FriendCheckReq;
import com.meguru.chatproject.user.domain.vo.request.friend.FriendDeleteReq;
import com.meguru.chatproject.user.domain.vo.response.friend.FriendApplyResp;
import com.meguru.chatproject.user.domain.vo.response.friend.FriendCheckResp;
import com.meguru.chatproject.user.domain.vo.response.friend.FriendResp;
import com.meguru.chatproject.user.domain.vo.response.friend.FriendUnreadResp;
import com.meguru.chatproject.user.service.IUserFriendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 好友相关接口
 * </p>
 *
 * @author Meguru
 * @since 2025-05-30
 */
@RestController
@RequestMapping("/capi/user/friend")
@Api(tags = "好友相关接口")
@Slf4j
public class FriendController {
    @Resource
    private IUserFriendService friendService;

    @GetMapping("/check")
    @ApiOperation("批量判断是否是自己好友")
    public ApiResult<FriendCheckResp> check(@Valid FriendCheckReq request) {
        Long uid = RequestHolder.get().getUid();
        return ApiResult.success(friendService.check(uid, request));
    }

    @PostMapping("/apply")
    @ApiOperation("申请好友")
    public ApiResult<Void> apply(@Valid @RequestBody FriendApplyReq request) {
        Long uid = RequestHolder.get().getUid();
        friendService.apply(uid, request);
        return ApiResult.success();
    }

    @DeleteMapping()
    @ApiOperation("删除好友")
    public ApiResult<Void> delete(@Valid @RequestBody FriendDeleteReq request) {
        Long uid = RequestHolder.get().getUid();
        friendService.deleteFriend(uid, request.getTargetUid());
        return ApiResult.success();
    }

    @GetMapping("/apply/page")
    @ApiOperation("好友申请列表")
    public ApiResult<PageBaseResp<FriendApplyResp>> page(@Valid PageBaseReq request) {
        Long uid = RequestHolder.get().getUid();
        return ApiResult.success(friendService.pageApplyFriend(uid, request));
    }

    @GetMapping("/apply/unread")
    @ApiOperation("申请未读数")
    public ApiResult<FriendUnreadResp> unread() {
        Long uid = RequestHolder.get().getUid();
        return ApiResult.success(friendService.unread(uid));
    }

    @PutMapping("/apply")
    @ApiOperation("审批同意")
    public ApiResult<Void> applyApprove(@Valid @RequestBody FriendApproveReq request) {
        friendService.applyApprove(RequestHolder.get().getUid(), request);
        return ApiResult.success();
    }

    @GetMapping("/page")
    @ApiOperation("联系人列表")
    public ApiResult<CursorPageBaseResp<FriendResp>> friendList(@Valid CursorPageBaseReq request) {
        Long uid = RequestHolder.get().getUid();
        return ApiResult.success(friendService.friendList(uid, request));
    }
}

