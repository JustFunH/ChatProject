package com.meguru.chatproject.chat.domain.vo.request;

import com.meguru.chatproject.common.domain.vo.request.CursorPageBaseReq;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
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
public class ChatMessageReadReq extends CursorPageBaseReq {
    @ApiModelProperty("消息id")
    @NotNull
    private Long msgId;

    @ApiModelProperty("查询类型 1已读 2未读")
    @NotNull
    private Long searchType;
}
