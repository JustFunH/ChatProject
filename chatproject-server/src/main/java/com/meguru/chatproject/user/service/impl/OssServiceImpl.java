package com.meguru.chatproject.user.service.impl;

import com.meguru.chatproject.MinIOTemplate;
import com.meguru.chatproject.common.exception.BusinessErrorEnum;
import com.meguru.chatproject.common.exception.BusinessException;
import com.meguru.chatproject.common.utils.AssertUtil;
import com.meguru.chatproject.domain.OssReq;
import com.meguru.chatproject.domain.OssResp;
import com.meguru.chatproject.user.domain.enums.OssSceneEnum;
import com.meguru.chatproject.user.domain.vo.request.oss.UploadUrlReq;
import com.meguru.chatproject.user.service.IOssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description: OSS相关处理类
 *
 * @author Meguru
 * @since 2025-05-29
 */
@Service
public class OssServiceImpl implements IOssService {
    @Autowired
    private MinIOTemplate minIOTemplate;

    @Override
    public OssResp getUploadUrl(Long uid, UploadUrlReq req) {
        OssSceneEnum sceneEnum = OssSceneEnum.of(req.getScene());
        AssertUtil.isNotEmpty(sceneEnum, BusinessErrorEnum.OSS_ERROR);
        OssReq ossReq = OssReq.builder()
                .fileName(req.getFileName())
                .filePath(sceneEnum.getPath())
                .uid(uid)
                .build();
        return minIOTemplate.getPreSignedObjectUrl(ossReq);
    }
}
