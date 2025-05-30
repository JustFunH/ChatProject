package com.meguru.chatproject.user.service;

import com.meguru.chatproject.domain.OssResp;
import com.meguru.chatproject.user.domain.vo.request.oss.UploadUrlReq;

public interface IOssService {
    OssResp getUploadUrl(Long uid, UploadUrlReq req);
}
