package com.meguru.chatproject.user.domain.vo.request.user;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;


/**
 * Description: 批量查询用户汇总详情
 *
 * @author Meguru
 * @since 2025-05-28
 */
@Data
public class SummaryInfoReq {
    @ApiModelProperty(value = "用户信息入参")
    @Size(max = 50)
    private List<infoReq> reqList;

    @Data
    public static class infoReq {
        @ApiModelProperty(value = "uid")
        private Long uid;
        @ApiModelProperty(value = "最近一次更新用户信息时间")
        private Long lastModifyTime;
    }
}
