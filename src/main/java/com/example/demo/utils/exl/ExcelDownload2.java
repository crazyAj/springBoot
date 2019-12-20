package com.example.demo.utils.exl;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class ExcelDownload2 {

    @Test
    public void download() {
        try {
            String path = "D:/work/发那科/bos/保全/temp";
            String sheetName = "保全点检手册";
            String fileName = sheetName + System.currentTimeMillis() + ".xls";
            String equipmentName = "数控端面外圆磨床";
            String equipmentType = "H234";
            String dateTime = "2019-11-18 9:28:00";
            String equipmentId = "M563";
            String factoryModeName = "凸轮轴";
            List<List<String>> titles = new ArrayList<>();
            titles.add(new ArrayList<String>() {{
                add("设备名称");
                add(equipmentName);
                add("设备型号");
                add(equipmentType);
                add("派工日期");
                add(dateTime);
            }});
            titles.add(new ArrayList<String>() {{
                add("设备编号");
                add(equipmentId);
                add("使用车间");
                add(factoryModeName);
                add("");
                add("");
            }});
            titles.add(new ArrayList<>(Arrays.asList(
                    "序号", "计划编号", "保全项目", "类别", "保全部位", "基准", "方法及工具", "处置", "保全时间", "保全工", "状态"
            )));
            List<String> data = new CopyOnWriteArrayList<>(titles.get(2));
            data.remove(0);
            List<List<String>> content = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                List<String> row = new ArrayList<>();
                row.add("" + (i + 1));
                if (i < 3) {
                    row.addAll(data);
                } else if (i < 8) {
                    row.addAll(data.stream().map(t -> t.concat("111")).collect(Collectors.toList()));
                } else {
                    row.addAll(data.stream().map(t -> t.concat("222")).collect(Collectors.toList()));
                }
                content.add(row);
            }
            String[] conditionList = {"领单", "完成"};
            Workbook wb = getWorkbook(sheetName, titles, content, conditionList);
            File file = new File(path + File.separator + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            OutputStream out = new FileOutputStream(file);
            wb.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 导出Excel
     *
     * @param sheetName sheet名称
     * @param titles    标题
     * @param rowDates  内容
     * @return
     */
    private Workbook getWorkbook(String sheetName, List<List<String>> titles, List<List<String>> rowDates, String[] conditionList) {

        // 创建一个Workbook，对应一个Excel文件
        Workbook wb = new HSSFWorkbook();

        // 在workbook中添加一个sheet,对应Excel文件中的sheet
        Sheet sheet = wb.createSheet(sheetName);

        Integer headLine = 5;
        List<String> title = titles.get(2);

        // 初始化表头
        Row row;
        Cell cell;
        for (int i = 0; i < headLine; i++) {
            row = sheet.createRow(i);
            for (int j = 0; j < title.size(); j++) {
                cell = row.createCell(j);
                if (i != headLine - 1) {
                    cell.setCellValue("");
                } else {
                    cell.setCellValue(title.get(j));
                }
            }
        }

        // 合并表头，赋值
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, title.size() - 1));
        sheet.getRow(0).getCell(0).setCellValue(sheetName);
        for (int i = 1; i < 3; i++) {
            sheet.addMergedRegion(new CellRangeAddress(i, i, 1, 2));
            sheet.addMergedRegion(new CellRangeAddress(i, i, 3, 4));
            sheet.addMergedRegion(new CellRangeAddress(i, i, 5, 6));
            sheet.addMergedRegion(new CellRangeAddress(i, i, 7, 8));
            sheet.addMergedRegion(new CellRangeAddress(i, i, 9, 10));

            sheet.getRow(i).getCell(0).setCellValue(titles.get(i - 1).get(0));
            sheet.getRow(i).getCell(1).setCellValue(titles.get(i - 1).get(1));
            sheet.getRow(i).getCell(3).setCellValue(titles.get(i - 1).get(2));
            sheet.getRow(i).getCell(5).setCellValue(titles.get(i - 1).get(3));
            sheet.getRow(i).getCell(7).setCellValue(titles.get(i - 1).get(4));
            sheet.getRow(i).getCell(9).setCellValue(titles.get(i - 1).get(5));
        }
        sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, title.size() - 1));

        // 表头样式
        CellStyle headStyle = wb.createCellStyle();
        // 设置字体
        Font font = wb.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        headStyle.setFont(font);
        // 水平居中
        headStyle.setAlignment(HorizontalAlignment.CENTER);
        // 垂直居中
        headStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 下边框
        headStyle.setBorderBottom(BorderStyle.THIN);
        // 左边框
        headStyle.setBorderLeft(BorderStyle.THIN);
        // 上边框
        headStyle.setBorderTop(BorderStyle.THIN);
        // 右边框
        headStyle.setBorderRight(BorderStyle.THIN);
        // 单元格保护
        headStyle.setLocked(true);

        for (int i = 0; i < headLine; i++) {
            row = sheet.getRow(i);
            for (int j = 0; j < row.getLastCellNum(); j++) {
                row.getCell(j).setCellStyle(headStyle);
            }
        }

        // 内容样式
        CellStyle bodyStyle = wb.createCellStyle();
        // 设置居中
        bodyStyle.setAlignment(HorizontalAlignment.CENTER);
        bodyStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 下边框
        bodyStyle.setBorderBottom(BorderStyle.THIN);
        // 左边框
        bodyStyle.setBorderLeft(BorderStyle.THIN);
        // 上边框
        bodyStyle.setBorderTop(BorderStyle.THIN);
        // 右边框
        bodyStyle.setBorderRight(BorderStyle.THIN);
        // 单元格不保护
        bodyStyle.setLocked(false);

        // 创建内容
        for (int i = 0; i < rowDates.size(); i++) {
            List<String> rowDate = rowDates.get(i);
            row = sheet.createRow(i + headLine);
            for (int j = 0; j < rowDate.size(); j++) {
                // 将内容按顺序赋给对应的列对象
                cell = row.createCell(j);
                cell.setCellValue(rowDate.get(j));
                if (j > rowDate.size() - 3) {
                    cell.setCellStyle(bodyStyle);
                }
            }
        }

        // 条件格式
        // 加载下拉列表内容
        DVConstraint constraint = DVConstraint.createExplicitListConstraint(conditionList);
        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(headLine, headLine + rowDates.size(), title.size() - 1, title.size() - 1);
        // 数据有效性对象
        HSSFDataValidation data_validation_list = new HSSFDataValidation(regions, constraint);
        sheet.addValidationData(data_validation_list);

        // 合并列
        Integer startRow = 0;
        Map<String, Integer> items = rowDates.stream().collect(Collectors.toMap(t -> t.get(2), t -> 1, Integer::sum));
        for (int i = 0; i < rowDates.size(); i++) {
            if (startRow == 0) {
                startRow = headLine;
            }
            String item = rowDates.get(i).get(2);
            Integer next = items.get(item);
            Integer endRow = startRow + next - 1;
            if (next > 1) {
                CellRangeAddress cra = new CellRangeAddress(startRow, endRow, 2, 2);
                sheet.addMergedRegion(cra);
            }
            cell = sheet.getRow(i + headLine).getCell(2);
            cell.setCellValue(item);
            cell.setCellStyle(bodyStyle);
            startRow = endRow + 1;
            i = endRow - headLine;
        }

        // 中文自动调整列宽
//        autoSizeColumn(sheet, rowDates.get(0).size());

        // 设置头宽度
        for (int i = 0; i < headLine; i++) {
            short colHeight = (short) (1.5 * sheet.getRow(i).getHeight());
            if (i == 3) {
                colHeight *= 0.5;
            }
            sheet.getRow(i).setHeight(colHeight);
        }
        // 隐藏列
        sheet.setColumnWidth(1, 0);

        // 锁定样式
        CellStyle lockStyle = wb.createCellStyle();
        // 设置居中
        lockStyle.setAlignment(HorizontalAlignment.CENTER);
        lockStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 下边框
        lockStyle.setBorderBottom(BorderStyle.THIN);
        // 左边框
        lockStyle.setBorderLeft(BorderStyle.THIN);
        // 上边框
        lockStyle.setBorderTop(BorderStyle.THIN);
        // 右边框
        lockStyle.setBorderRight(BorderStyle.THIN);
        // 单元格保护
        lockStyle.setLocked(true);
        for (int i = 0; i < rowDates.size(); i++) {
            List<String> rowDate = rowDates.get(i);
            row = sheet.getRow(i + headLine);
            for (int j = 0; j < rowDate.size(); j++) {
                // 将内容按顺序赋给对应的列对象
                cell = row.getCell(j);
                if (j < rowDate.size() - 2) {
                    cell.setCellStyle(lockStyle);
                }
            }
        }

        // 单元格保护，需要和lock一起使用，密码
        sheet.protectSheet("qwer1234");
        return wb;
    }

    /**
     * Excel 自适应文字
     *
     * @param sheet
     * @param size
     */
//    private void autoSizeColumn(Sheet sheet, int size) {
//        for (int columnNum = 0; columnNum < size; columnNum++) {
//            int columnWidth = sheet.getColumnWidth(columnNum) / 256;
//            for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
//                Row currentRow;
//                //当前行未被使用过
//                if (sheet.getRow(rowNum) == null) {
//                    currentRow = sheet.createRow(rowNum);
//                } else {
//                    currentRow = sheet.getRow(rowNum);
//                }
//
//                if (currentRow.getCell(columnNum) != null) {
//                    Cell currentCell = currentRow.getCell(columnNum);
//                    if (currentCell.getCellTypeEnum() == CellType.STRING) {
//                        int length = currentCell.getStringCellValue().getBytes().length;
//                        if (columnWidth < length) {
//                            columnWidth = length;
//                        }
//                    }
//                }
//            }
//            sheet.setColumnWidth(columnNum, columnWidth * 256);
//        }
//    }

}