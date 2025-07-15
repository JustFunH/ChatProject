package com.meguru.chatproject.chat.domain.entity.msg;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * Description: 语音消息入参
 * @author Meguru
 * @since 2025-05-31
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SoundMsgDTO extends BaseFileDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("时长（秒）")
    @NotNull
    private Integer second;
}
