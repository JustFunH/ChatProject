package com.meguru.chatproject.chat.domain.vo.request.msg;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Description: 文本消息入参
 *
 * @author Meguru
 * @since 2025-05-28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TextMsgReq {

    @NotBlank(message = "内容不能为空")
    @Size(max = 1024, message = "消息内容过长，服务器扛不住啊，兄dei")
    @ApiModelProperty("消息内容")
    private String content;

    @ApiModelProperty("回复的消息id,如果没有别传就好")
    private Long replyMsgId;

    @ApiModelProperty("艾特的uid")
    @Size(max = 10, message = "一次别艾特这么多人")
    private List<Long> atUidList;
}
