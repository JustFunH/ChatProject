package com.meguru.chatproject.chat.domain.entity.msg;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Description: 表情图片消息入参
 * @author Meguru
 * @since 2025-05-31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmojisMsgDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("下载地址")
    @NotBlank
    private String url;
}


