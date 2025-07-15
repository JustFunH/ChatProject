package com.meguru.chatproject.common.domain.vo.request;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * @author Meguru
 * @since 2025-05-28
 */
@Data
@ApiModel("基础翻页请求")
public class PageBaseReq {

    @ApiModelProperty("页面大小")
    @Min(0)
    @Max(50)
    private Integer pageSize = 10;

    @ApiModelProperty("页面索引（从1开始）")
    private Integer pageNo = 1;

    /**
     * 获取mybatisPlus的page
     *
     * @return
     */
    public Page plusPage() {
        return new Page(pageNo, pageSize);
    }
}
