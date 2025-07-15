package com.meguru.chatproject.chat.domain.vo.request;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
public class ChatMessageReadInfoReq {
    @ApiModelProperty("消息id集合（只查本人）")
    @Size(max = 20)
    private List<Long> msgIds;
}
