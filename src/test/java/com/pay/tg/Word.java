package com.pay.tg;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelPicUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.apache.poi.ss.usermodel.PictureData;

public class Word {

    public static void main(String[] args) throws Exception{
        File file1 = new File("C:\\Users\\Administrator\\Desktop\\a.xls");
        FileInputStream file=new FileInputStream(file1);
        List<TaskExcelDto> list = new ArrayList<>();
        // EasyExcel解析普通数据（这里为了演示简单写一下，实战建议写个实现类）
        EasyExcel.read(file, TaskExcelDto.class, new AnalysisEventListener<TaskExcelDto>() {

            @Override
            public void invoke(TaskExcelDto data, AnalysisContext context) {
                list.add(data);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {

            }
        }).sheet().doRead();
        // 使用HuTools工具类解析表格的图片
        //注意这个方法只能读取浮动的图片，嵌入单元格的读取不到！
        //注意这个方法只能读取浮动的图片，嵌入单元格的读取不到！
        //注意这个方法只能读取浮动的图片，嵌入单元格的读取不到！
        file1 = new File("C:\\Users\\Administrator\\Desktop\\a.xls");
        file=new FileInputStream(file1);
        ExcelReader reader = ExcelUtil.getReader(file);
        //key是图片位置，row_cell的格式；value是图片数据
        Map<String, PictureData> picMap = ExcelPicUtil.getPicMap(reader.getWorkbook(), 0);
        //这里只关心行数，把数据装到Bean里面去，也可用map在循环中取获取
        picMap.forEach((k, v) -> {
            String[] split = k.split(StrUtil.UNDERLINE);
            Integer index = Integer.valueOf(split[0]);
            list.get(index - 1).setPictureData(v);
        });

        list.stream().forEach(info->{
            System.out.println(info);
            byte[] data = info.getPictureData().getData();
            File imgFile=fileToBytes(data,"data/",info.getUtr()+".jpg");
            System.out.println("发送消息");
            imgFile.delete();
        });

        /*//建议使用并行流上传图片，多线程加速
        list.stream().parallel().forEach(i -> {
            if (null == i.getPictureData()) {
                return;
            }
            String mimeType = i.getPictureData().getMimeType();
            String[] mimeTypes = mimeType.split(StrUtil.SLASH);
            try {
                String fileName =  UUID.randomUUID() + "." + mimeTypes[1];
                byte[] data = i.getPictureData().getData();

            } catch (Exception e) {
                 e.printStackTrace();
            }
        });*/

    }

    /**
     * 将Byte数组转换成文件
     * @param bytes byte数组
     * @param filePath 文件路径  如 D:\\Users\\Downloads\\
     * @param fileName  文件名
     */
    public static File fileToBytes(byte[] bytes, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {

            file = new File(filePath + fileName);
            if (!file.getParentFile().exists()){
                //文件夹不存在 生成
                file.getParentFile().mkdirs();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }


}