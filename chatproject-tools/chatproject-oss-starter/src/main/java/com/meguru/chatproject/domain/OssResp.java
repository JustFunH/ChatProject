package com.meguru.chatproject.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 上传url请求出参
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OssResp {

    @ApiModelProperty(value = "上传的临时url")
    private String uploadUrl;

    @ApiModelProperty(value = "成功后能够下载的url")
    private String downloadUrl;
}
