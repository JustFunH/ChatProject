package com.meguru.chatproject.user.controller;

import com.meguru.chatproject.common.domain.vo.response.ApiResult;
import com.meguru.chatproject.common.utils.RequestHolder;
import com.meguru.chatproject.domain.OssResp;
import com.meguru.chatproject.user.domain.vo.request.oss.UploadUrlReq;
import com.meguru.chatproject.user.service.IOssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description: oss控制层
 *
 * @author Meguru
 * @since 2025-05-30
 */
@RestController
@RequestMapping("/capi/oss")
@Api(tags = "oss相关接口")
public class OssController {
    @Autowired
    private IOssService ossService;

    @GetMapping("/upload/url")
    @ApiOperation("获取上传url")
    public ApiResult<OssResp> getUploadUrl(@Valid UploadUrlReq req) {
        return ApiResult.success(ossService.getUploadUrl(RequestHolder.get().getUid(), req));
    }
}
