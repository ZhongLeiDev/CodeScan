package com.zl.dbmail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Range;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.CellFormat;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.NumberFormat;
import jxl.write.WritableCell;
import jxl.write.WritableCellFeatures;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class SaveToExcel {
//	static HashMap map = new HashMap();

	/*
	 * �����ȫ
	 */
	public static void main(String[] args) {
		try {
			// copyDateFormat(new File("c:\\a.xls"), 0, "c:\\copy of a.xls");
			writeExcelUseFormat("c:\\format.xls", "test");

			// buildNewFormTemplete(new File("c:/templete.xls"),new File(
			// "c:/buildNewFormTemplete.xls"));
			// modifyDirectly1(new File("c:/templete.xls"));
			// modifyDirectly2(new File("c:/templete.xls"));
			// copyDateAndFormat(new File("c:/a.xls"), 0, "c:/a2.xls");
		}
		catch (Exception e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}
	}

	public static void modifyDirectly2(File inputFile) throws Exception {
		Workbook w1 = Workbook.getWorkbook(inputFile);
		WritableWorkbook w2 = Workbook.createWorkbook(inputFile, w1);
		WritableSheet sheet = w2.getSheet(0);

		WritableCell cell = null;
		CellFormat cf = null;

		// �Ӵ�
		cell = sheet.getWritableCell(0, 0);
		WritableFont bold = new WritableFont(WritableFont.ARIAL, WritableFont.DEFAULT_POINT_SIZE, WritableFont.BOLD);
		cf = new WritableCellFormat(bold);
		cell.setCellFormat(cf);

		// �����»���
		cell = sheet.getWritableCell(0, 1);
		WritableFont underline = new WritableFont(WritableFont.ARIAL, WritableFont.DEFAULT_POINT_SIZE, WritableFont.NO_BOLD, false, UnderlineStyle.SINGLE);
		cf = new WritableCellFormat(underline);
		cell.setCellFormat(cf);

		// ֱ����ӿ��Ը��ǵ�
		setCellValueDirectly(sheet, sheet.getCell(0, 2), Double.valueOf(4), CellType.NUMBER);

		w2.write();
		w2.close();
	}

	public static void modifyDirectly1(File file) {
		try {
			// Excel����ļ�
			Workbook wb = Workbook.getWorkbook(file);
			// ��һ���ļ��ĸ���������ָ������д�ص�ԭ�ļ�
			WritableWorkbook book = Workbook.createWorkbook(file, wb);
			WritableSheet sheet0 = book.getSheet(0);
			sheet0.addCell(new Label(0, 1, "��С��"));

			// ���һ��������
			WritableSheet sheet = book.createSheet(" �ڶ�ҳ ", 1);
			sheet.addCell(new Label(0, 0, " �ڶ�ҳ�Ĳ������� "));
			book.write();
			book.close();
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void buildNewFormTemplete(File inputFile, File outputFile) {
		try {
			// Excel����ļ�
			Workbook wb = Workbook.getWorkbook(inputFile);
			// ��һ���ļ��ĸ���������ָ������д�ص�ԭ�ļ�
			WritableWorkbook book = Workbook.createWorkbook(outputFile, wb);
			WritableSheet sheet0 = book.getSheet(0);
			sheet0.addCell(new Label(0, 1, "��С��"));

			// ���һ��������
			WritableSheet sheet = book.createSheet(" �ڶ�ҳ ", 1);
			sheet.addCell(new Label(0, 0, " �ڶ�ҳ�Ĳ������� "));

			book.write();
			book.close();
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void copyDateAndFormat(File inputFile, int inputFileSheetIndex, String outputFilePath) throws Exception {
		Workbook book = null;
		Cell cell = null;
		// 1.�������������
		WorkbookSettings setting = new WorkbookSettings();
		java.util.Locale locale = new java.util.Locale("zh", "CN");
		setting.setLocale(locale);
		setting.setEncoding("ISO-8859-1");
		book = Workbook.getWorkbook(inputFile, setting);
		Sheet readonlySheet = book.getSheet(inputFileSheetIndex);

		OutputStream os = new FileOutputStream(outputFilePath);// �����Excel�ļ�URL
		WritableWorkbook wwb = Workbook.createWorkbook(os);// ������д������
		WritableSheet writableSheet = wwb.createSheet(readonlySheet.getName(), 0);// ������д������

		// 2.��д��ͬ���ݸ�ʽ������
		for (int rowIndex = 0; rowIndex < readonlySheet.getRows(); rowIndex++) {
			for (int colIndex = 0; colIndex < readonlySheet.getColumns(); colIndex++) {
				cell = readonlySheet.getCell(colIndex, rowIndex);
				// A2B2Ϊ�ϲ��ĵ�Ԫ��A2�����ݣ�B2Ϊ��
				// if(colIndex == 0 && rowIndex == 1){
				// System.out.println(colIndex + "," + rowIndex + " type:" +
				// cell.getType() +" :" + cell.getContents());
				// }

				// ���и������ø�ʽ��
				if (cell.getType() == CellType.DATE || cell.getType() == CellType.DATE_FORMULA) {
					writableSheet.addCell(new jxl.write.DateTime(colIndex, rowIndex, ((DateCell) cell).getDate(), new jxl.write.WritableCellFormat(cell.getCellFormat())));
				}
				else if (cell.getType() == CellType.NUMBER || cell.getType() == CellType.NUMBER_FORMULA) {
					writableSheet.addCell(new jxl.write.Number(colIndex, rowIndex, ((jxl.NumberCell) cell).getValue(), new jxl.write.WritableCellFormat(cell.getCellFormat())));
				}
				else if (cell.getType() == CellType.EMPTY) {
					// �յ��Լ��ϲ���Ԫ���е�һ�����
					// System.out.println("EMPTY:"+cell.getContents());
					// System.err.println("�յ�Ԫ�� at " + colIndex + "," + rowIndex
					// +" content:" + cell.getContents());
				}
				else if (cell.getType() == CellType.LABEL || cell.getType() == CellType.STRING_FORMULA) {
					writableSheet.addCell(new Label(colIndex, rowIndex, cell.getContents(), new jxl.write.WritableCellFormat(cell.getCellFormat())));
				}
				else {
					System.err.println("������Ԫ�����ͣ�" + cell.getType() + " at " + colIndex + "," + rowIndex + " content:" + cell.getContents());
				}

				// if(cell.getType() == CellType.STRING_FORMULA){
				// System.err.println(colIndex + "," + rowIndex +":" +
				// cell.getContents() +" type:" + cell.getType());
				// }
			}
		}

		// 3.����ϲ���Ԫ�������(���ƺϲ���Ԫ���ʽ)
		Range[] range = readonlySheet.getMergedCells();
		for (int i = 0; i < range.length; i++) {
			// System.out.println("��"+i+"���ϲ��ĵ�Ԫ��:"
			// +",getTopLeft="+range[i].getTopLeft().getColumn()
			// +","+range[i].getTopLeft().getRow()
			// +",getBottomRight="+range[i].getBottomRight().getColumn()
			// +","+range[i].getBottomRight().getRow()
			// );
			// topleftXIndex, topleftYIndex, bottomRightXIndex,
			// bottomRightYIndex
			writableSheet.mergeCells(range[i].getTopLeft().getColumn(), range[i].getTopLeft().getRow(), range[i].getBottomRight().getColumn(), range[i].getBottomRight().getRow());
		}

		// 4.�������и߿�
		for (int colIndex = 0; colIndex < readonlySheet.getColumns(); colIndex++) {
			writableSheet.setColumnView(colIndex, readonlySheet.getColumnView(colIndex));
		}
		for (int rowIndex = 0; rowIndex < readonlySheet.getRows(); rowIndex++) {
			writableSheet.setRowView(rowIndex, readonlySheet.getRowView(rowIndex));
		}

		wwb.write();
		wwb.close();
		os.close();
	}

	public static void writeExcelUseFormat(String outputFilePath, String outputFileSheetName) throws Exception {
		OutputStream os = new FileOutputStream(outputFilePath);// �����Excel�ļ�URL
		WritableWorkbook wwb = Workbook.createWorkbook(os);// ������д������
		WritableSheet sheet = wwb.createSheet(outputFileSheetName, 0);// ������д������

		sheet.addCell(new Label(0, 0, "����"));
		sheet.addCell(new Label(1, 0, "��Ч��"));

		// 1.д��ʱ������ݸ�ʽ
		jxl.write.DateFormat df = new jxl.write.DateFormat("yyyy-MM-dd");
		jxl.write.WritableCellFormat wcfDF = new jxl.write.WritableCellFormat(df);
		jxl.write.DateTime labelDTF = new jxl.write.DateTime(1, 1, new Date(), wcfDF); // �Զ����ʽ
		sheet.addCell(labelDTF);

		// 2.������ʽ
		// WritableFont()���������˵����
		// �����������һ�����������ԷŽ�ȥ�ö�����
		// ��һ��: TIMES�������С����д����18
		// �ڶ���: BOLD���ж��Ƿ�Ϊб��,ѡ��trueʱΪб��
		// ������: ARIAL
		// ���ĸ�: UnderlineStyle.NO_UNDERLINE �»���
		// �����: jxl.format.Colour.RED ������ɫ�Ǻ�ɫ��
		jxl.write.WritableFont wf = new jxl.write.WritableFont(WritableFont.TIMES, 18, WritableFont.BOLD, true);
		jxl.write.WritableCellFormat wcfF = new jxl.write.WritableCellFormat(wf);
		wcfF.setWrap(true);// �Զ�����
		wcfF.setAlignment(jxl.format.Alignment.CENTRE);// ��ˮƽ���뷽ʽָ��Ϊ����
		wcfF.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);// �Ѵ�ֱ���뷽ʽָ��Ϊ����
		jxl.write.Label labelC = new jxl.write.Label(0, 1, "This is a Label cell", wcfF);
		sheet.addCell(labelC);

		// 3.��Ӵ���formatting��Number����
		jxl.write.NumberFormat nf = new jxl.write.NumberFormat("#.##");
		jxl.write.WritableCellFormat wcfN = new jxl.write.WritableCellFormat(nf);
		jxl.write.Number labelNF = new jxl.write.Number(0, 2, 3.1415926, wcfN);
		sheet.addCell(labelNF);

		// 4.���Boolean����
		jxl.write.Boolean labelB = new jxl.write.Boolean(0, 3, false);
		sheet.addCell(labelB);

		// 5.����һ��ע��
		WritableCellFeatures cellFeatures = new WritableCellFeatures();
		cellFeatures.setComment("���Boolean����");
		labelB.setCellFeatures(cellFeatures);

		// 6.��Ԫ���ڻ���
		WritableCellFormat wrappedText = new WritableCellFormat(WritableWorkbook.ARIAL_10_PT);
		wrappedText.setWrap(true);// �ɻ��е�label��ʽ
		Label label = new Label(4, 0, "����,\012���ԡ�����", wrappedText); // "\012"ǿ�ƻ���
		sheet.addCell(label);

		// 7.���ֵĹ�ʽ����
		jxl.write.Number n = new jxl.write.Number(0, 9, 4.5);// A10
		sheet.addCell(n);
		n = new jxl.write.Number(1, 9, 8);// B10
		sheet.addCell(n);
		NumberFormat dp3 = new NumberFormat("#.###"); // ���õ�Ԫ����������ָ�ʽ
		WritableCellFormat dp3cell = new WritableCellFormat(dp3);
		dp3cell.setWrap(true);
		Formula f = new Formula(2, 9, "(a10+b10)/2", dp3cell); // ����C10��ʽ
		sheet.addCell(f);
		f = new Formula(3, 9, "SUM(A10:B10)", dp3cell);// ����D10��ʽ
		sheet.addCell(f);

		// 8.����sheet����ʽ
		sheet.getSettings().setProtected(true); // ����xls�ı�������Ԫ��Ϊֻ����
		sheet.getSettings().setPassword("123"); // ����xls������
		sheet.getSettings().setDefaultColumnWidth(10); // �����е�Ĭ�Ͽ��,2cm����
		sheet.setRowView(3, 200);// ���õ�4�и߶�
		sheet.setRowView(2, false);// ���������Զ����и���չ
		sheet.setColumnView(0, 300);// ���õ�1�п�ȣ�6cm����
		sheet.mergeCells(0, 5, 1, 7);// �ϲ���Ԫ�񣺺ϲ�A6B8Ҳ����1��6�� �� 2��7��֮��ľ���

		// 9.���ñ߿�
		drawRect(sheet, 5, 6, 7, 6, BorderLineStyle.THICK, Colour.BLACK, null);

		sheet.mergeCells(1, 2, 3, 3);

		wwb.write();
		wwb.close();
		os.close();
	}

	public static void drawRect(WritableSheet sheet, int x, int y, int width, int height, BorderLineStyle style, Colour BorderColor, Colour bgColor) throws WriteException {
		for (int w = 0; w < width; w++) {
			for (int h = 0; h < height; h++) {
				WritableCellFormat alignStyle = new WritableCellFormat(); // ��Ԫ����ʽ
				alignStyle.setAlignment(Alignment.CENTRE); // ���ö��뷽ʽ
				alignStyle.setVerticalAlignment(VerticalAlignment.CENTRE);// ���ö��뷽ʽ
				if (h == 0) // ����
					alignStyle.setBorder(Border.TOP, style, BorderColor);// ���ñ߿����ɫ����ʽ

				if (w == 0) // ����
					alignStyle.setBorder(Border.LEFT, style, BorderColor);// ���ñ߿����ɫ����ʽ

				if (w == width - 1) // ����
					alignStyle.setBorder(Border.RIGHT, style, BorderColor);// ���ñ߿����ɫ����ʽ

				if (h == height - 1) // ����
					alignStyle.setBorder(Border.BOTTOM, style, BorderColor);// ���ñ߿����ɫ����ʽ
				// drawLine(sheet, x, y, Border.BOTTOM);
				if (bgColor != null) alignStyle.setBackground(bgColor); // ����ɫ
				Label mergelabel = new Label(x, y, "", alignStyle);
				// topleftXIndex, topleftYIndex, bottomRightXIndex,
				// bottomRightYIndex
				// sheet.mergeCells(2, 5, 10, 10);
				sheet.addCell(mergelabel);
				y++;
			}
			y -= height;
			x++;
		}
	}

	public static ArrayList<String> sampleReadExcel(File inputFile, int inputFileSheetIndex) throws Exception {
		ArrayList<String> list = new ArrayList<String>();
		Workbook book = null;
		Cell cell = null;
		// �������������
		WorkbookSettings setting = new WorkbookSettings();
		java.util.Locale locale = new java.util.Locale("zh", "CN");
		setting.setLocale(locale);
		setting.setEncoding("ISO-8859-1");
		book = Workbook.getWorkbook(inputFile, setting);

		Sheet sheet = book.getSheet(inputFileSheetIndex);
		for (int rowIndex = 0; rowIndex < sheet.getRows(); rowIndex++) {// Excel��һ��Ϊ��ͷ,���J��ֵ��Ϊ1
			for (int colIndex = 0; colIndex < sheet.getColumns(); colIndex++) {// ֻ���Excel��ȡ��2��
				cell = sheet.getCell(colIndex, rowIndex);
				list.add(cell.getContents());
			}
		}

		// �����⣺�����ʵ�ʲ����ʱ��û��д��������Ƿ�ᵼ�²������ĵ����������ڴ棿jxl�����и�ReadWrite.javaû�йرն��ģ�ֻ�ر���д�ġ�
		book.close();

		return list;
	}

	public static void setCellValueDirectly(WritableSheet sheet, Cell cell, Object newValue, CellType type) throws Exception {
		if (type == CellType.DATE || type == CellType.DATE_FORMULA) {
			sheet.addCell(new jxl.write.DateTime(cell.getColumn(), cell.getRow(), (Date) newValue, new jxl.write.WritableCellFormat(cell.getCellFormat())));
		}
		else if (type == CellType.NUMBER || type == CellType.NUMBER_FORMULA) {
			sheet.addCell(new jxl.write.Number(cell.getColumn(), cell.getRow(), ((Double) newValue).doubleValue(), new jxl.write.WritableCellFormat(cell.getCellFormat())));
		}
		else if (type == CellType.LABEL || type == CellType.STRING_FORMULA) {
			sheet.addCell(new Label(cell.getColumn(), cell.getRow(), (String) newValue, new jxl.write.WritableCellFormat(cell.getCellFormat())));
		}
		else {
			throw new Exception("��֧�ֵ�������Ԫ�����ͣ�" + type);
			// System.err.println("��֧�ֵ�������Ԫ�����ͣ�" + cell.getType() + " at " +
			// cell.getColumn() + "," + cell.getRow() +" current content:" +
			// cell.getContents());
		}
	}

}
