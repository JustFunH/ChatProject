package com.meguru.chatproject.common.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Description: 扫码成功对象，推送给用户的消息对象
 *
 * @author Meguru
 * @since 2025-05-28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScanSuccessMessageDTO implements Serializable {
    /**
     * 推送的uid
     */
    private Integer code;

}
