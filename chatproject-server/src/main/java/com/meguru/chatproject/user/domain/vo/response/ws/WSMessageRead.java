package com.meguru.chatproject.user.domain.vo.response.ws;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 *
 * @author Meguru
 * @since 2025-05-28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WSMessageRead {
    @ApiModelProperty("消息")
    private Long msgId;
    @ApiModelProperty("阅读人数（可能为0）")
    private Integer readCount;
}
