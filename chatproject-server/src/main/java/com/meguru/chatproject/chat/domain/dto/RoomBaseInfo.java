package com.meguru.chatproject.chat.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Description: 房间详情
 *
 * @author Meguru
 * @since 2025-05-28
 */
@Data
public class RoomBaseInfo {
    @ApiModelProperty("房间id")
    private Long roomId;
    @ApiModelProperty("会话名称")
    private String name;
    @ApiModelProperty("会话头像")
    private String avatar;
    /**
     * 房间类型 1群聊 2单聊
     */
    private Integer type;

    /**
     * 是否全员展示 0否 1是
     *
     * @see com.meguru.chatproject.chat.domain.enums.HotFlagEnum
     */
    private Integer hotFlag;

    /**
     * 群最后消息的更新时间
     */
    @TableField("active_time")
    private Date activeTime;

    /**
     * 最后一条消息id
     */
    @TableField("last_msg_id")
    private Long lastMsgId;
}
