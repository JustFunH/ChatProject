package com.meguru.chatproject.chat.domain.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 * @author Meguru
 * @since 2025-05-28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageReadInfoResp {
    @ApiModelProperty("消息id")
    private Long msgId;

    @ApiModelProperty("已读数")
    private Integer readCount;

    @ApiModelProperty("未读数")
    private Integer unReadCount;

}
