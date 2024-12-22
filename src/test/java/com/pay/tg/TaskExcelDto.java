package com.pay.tg;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import org.apache.poi.ss.usermodel.PictureData;

import java.io.Serializable;

/**
 * <p>
 * 任务表格映射
 * </p>
 *
 * @author kobe
 * @since 2023-03-04
 */
@Data
public class TaskExcelDto implements Serializable {
 
    private static final long serialVersionUID = 1L;
 
    //这个注解表示解析的时候忽略
    @ExcelProperty("utr")
    private String utr;
 
    @ExcelProperty("订单号")
    private String orderNo;

    // 图片数据，临时变量
    @ExcelIgnore
    private PictureData pictureData;
 
 
}