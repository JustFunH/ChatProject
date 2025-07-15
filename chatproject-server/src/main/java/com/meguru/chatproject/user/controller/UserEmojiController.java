package com.meguru.chatproject.user.controller;

import com.meguru.chatproject.common.domain.vo.request.IdReqVO;
import com.meguru.chatproject.common.domain.vo.response.ApiResult;
import com.meguru.chatproject.common.domain.vo.response.IdRespVO;
import com.meguru.chatproject.common.utils.RequestHolder;
import com.meguru.chatproject.user.domain.vo.request.user.UserEmojiReq;
import com.meguru.chatproject.user.domain.vo.response.user.UserEmojiResp;
import com.meguru.chatproject.user.service.IUserEmojiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户表情包
 *
 * @author Meguru
 * @since 2025-05-29
 */
@RestController
@RequestMapping("/capi/user/emoji")
@Api(tags = "用户表情包管理相关接口")
public class UserEmojiController {

    /**
     * 用户表情包 Service
     */
    @Resource
    private IUserEmojiService emojiService;


    /**
     * 表情包列表
     *
     * @return 表情包列表
     * @author WuShiJie
     * @createTime 2023/7/3 14:46
     **/
    @GetMapping("/list")
    @ApiOperation("表情包列表")
    public ApiResult<List<UserEmojiResp>> getEmojisPage() {
        return ApiResult.success(emojiService.list(RequestHolder.get().getUid()));
    }


    /**
     * 新增表情包
     *
     * @param req 用户表情包
     * @return 表情包
     * @author WuShiJie
     * @createTime 2023/7/3 14:46
     **/
    @PostMapping()
    @ApiOperation("新增表情包")
    public ApiResult<IdRespVO> insertEmojis(@Valid @RequestBody UserEmojiReq req) {
        return emojiService.insert(req, RequestHolder.get().getUid());
    }

    /**
     * 删除表情包
     *
     * @return 删除结果
     * @author WuShiJie
     * @createTime 2023/7/3 14:46
     **/
    @DeleteMapping()
    @ApiOperation("删除表情包")
    public ApiResult<Void> deleteEmojis(@Valid @RequestBody IdReqVO reqVO) {
        emojiService.remove(reqVO.getId(), RequestHolder.get().getUid());
        return ApiResult.success();
    }
}
