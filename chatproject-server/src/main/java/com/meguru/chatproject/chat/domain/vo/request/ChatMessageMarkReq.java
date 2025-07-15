package com.meguru.chatproject.chat.domain.vo.request;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 消息标记请求
 *
 * @author Meguru
 * @since 2025-05-28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageMarkReq {
    @NotNull
    @ApiModelProperty("消息id")
    private Long msgId;

    @NotNull
    @ApiModelProperty("标记类型 1点赞 2举报")
    private Integer markType;

    @NotNull
    @ApiModelProperty("动作类型 1确认 2取消")
    private Integer actType;
}
