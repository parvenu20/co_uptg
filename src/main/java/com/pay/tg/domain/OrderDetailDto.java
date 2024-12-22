/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *//*

package com.pay.tg.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.math.BigDecimal;

*/
/**
* @author fire
* @date 2023-06-12
*//*

@Data
@HeadRowHeight(20)
public class OrderDetailDto {

    @ColumnWidth(15)

    @ExcelProperty(value = "商户号")
    String partnerId;

    @ColumnWidth(15)
    @ExcelProperty(value = "商户名称")
    String partnerName;

    @ColumnWidth(15)
    @ExcelProperty(value = "通道编码")
    Long channelId;

    @ColumnWidth(15)
    @ExcelProperty(value = "通道名称")
    String channelName;

    @ColumnWidth(10)
    @ExcelProperty(value = "订单金额")
    BigDecimal amount;

    @ColumnWidth(10)
    @ExcelProperty(value = "商户金额")
    BigDecimal partnerProportionAmount;

    @ColumnWidth(20)
    @ExcelProperty(value = "平台订单号")
    String orderNo;

    @ColumnWidth(20)
    @ExcelProperty(value = "商户订单号")
    String partnerOrderNo;

    @ColumnWidth(20)
    @ExcelProperty(value = "渠道订单号")
    String channelOrderNo;

    @ColumnWidth(20)
    @ExcelProperty(value = "UTR")
    String utr;

    @ColumnWidth(5)
    @ExcelProperty(value = "状态（0-待支付，1-待支付，2-成功，3-失败）")
    String status;

    @ColumnWidth(20)
    @ExcelProperty(value = "创建时间")
    String createTime;

    @ColumnWidth(20)
    @ExcelProperty(value = "成功时间")
    String successTime;
}*/
