package com.zl.dbmail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckStringTypeUtils {
	
	/** 
     * ���ܣ����֤����Ч��֤ 
     *  
     * @param IDStr 
     *            ���֤�� 
     * @return ��Ч������"" ��Ч������String��Ϣ 
     * @throws ParseException 
     */  
    public static boolean IDCardValidate(String IDStr) throws ParseException {  
        String[] ValCodeArr = { "1", "0", "x", "9", "8", "7", "6", "5", "4",  
                "3", "2" };  
        String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",  
                "9", "10", "5", "8", "4", "2" };  
        String Ai = "";  
        // ================ ����ĳ��� 15λ��18λ ================  
        if (IDStr.length() != 15 && IDStr.length() != 18) {  
            return false;  
        }  
        // =======================(end)========================  
  
        // ================ ���� �������Ϊ��Ϊ���� ================  
        if (IDStr.length() == 18) {  
            Ai = IDStr.substring(0, 17);  
        } else if (IDStr.length() == 15) {  
            Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);  
        }  
        if (isNumeric(Ai) == false) {  
            return false;  
        }  
        // =======================(end)========================  
  
        // ================ ���������Ƿ���Ч ================  
        String strYear = Ai.substring(6, 10);// ���  
        String strMonth = Ai.substring(10, 12);// �·�  
        String strDay = Ai.substring(12, 14);// �·�  
        if (isDataFormat(strYear + "-" + strMonth + "-" + strDay) == false) {  
            return false;  
        }  
        GregorianCalendar gc = new GregorianCalendar();  
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);  
        try {  
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150  
                    || (gc.getTime().getTime() - s.parse(  
                            strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {  
                return false;  
            }  
        } catch (NumberFormatException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (java.text.ParseException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {  
            return false;  
        }  
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {  
            return false;  
        }  
        // =====================(end)=====================  
  
        // ================ ������ʱ����Ч ================  
        Hashtable<String,String> h = GetAreaCode();  
        if (h.get(Ai.substring(0, 2)) == null) {  
            return false;  
        }  
        // ==============================================  
  
        // ================ �ж����һλ��ֵ ================  
        int TotalmulAiWi = 0;  
        for (int i = 0; i < 17; i++) {  
            TotalmulAiWi = TotalmulAiWi  
                    + Integer.parseInt(String.valueOf(Ai.charAt(i)))  
                    * Integer.parseInt(Wi[i]);  
        }  
        int modValue = TotalmulAiWi % 11;  
        String strVerifyCode = ValCodeArr[modValue];  
        Ai = Ai + strVerifyCode;  
  
        if (IDStr.length() == 18) {  
            if (Ai.equals(IDStr) == false) {  
                return false;  
            }  
        } else {  
            return true;  
        }  
        // =====================(end)=====================  
        return true;  
    }
    
    /** 
     * ���ܣ��ж��ַ����Ƿ�Ϊ���� 
     *  
     * @param str 
     * @return 
     */  
    private static boolean isNumeric(String str) {  
        Pattern pattern = Pattern.compile("[0-9]*");  
        Matcher isNum = pattern.matcher(str);  
        if (isNum.matches()) {  
            return true;  
        } else {  
            return false;  
        }  
    }  
  
    /** 
     * ���ܣ����õ������� 
     *  
     * @return Hashtable ���� 
     */  
    @SuppressWarnings({ "rawtypes", "unchecked" })  
    private static Hashtable<String,String> GetAreaCode() {  
        Hashtable<String,String> hashtable = new Hashtable();  
        hashtable.put("11", "����");  
        hashtable.put("12", "���");  
        hashtable.put("13", "�ӱ�");  
        hashtable.put("14", "ɽ��");  
        hashtable.put("15", "���ɹ�");  
        hashtable.put("21", "����");  
        hashtable.put("22", "����");  
        hashtable.put("23", "������");  
        hashtable.put("31", "�Ϻ�");  
        hashtable.put("32", "����");  
        hashtable.put("33", "�㽭");  
        hashtable.put("34", "����");  
        hashtable.put("35", "����");  
        hashtable.put("36", "����");  
        hashtable.put("37", "ɽ��");  
        hashtable.put("41", "����");  
        hashtable.put("42", "����");  
        hashtable.put("43", "����");  
        hashtable.put("44", "�㶫");  
        hashtable.put("45", "����");  
        hashtable.put("46", "����");  
        hashtable.put("50", "����");  
        hashtable.put("51", "�Ĵ�");  
        hashtable.put("52", "����");  
        hashtable.put("53", "����");  
        hashtable.put("54", "����");  
        hashtable.put("61", "����");  
        hashtable.put("62", "����");  
        hashtable.put("63", "�ຣ");  
        hashtable.put("64", "����");  
        hashtable.put("65", "�½�");  
        hashtable.put("71", "̨��");  
        hashtable.put("81", "���");  
        hashtable.put("82", "����");  
        hashtable.put("91", "����");  
        return hashtable;  
    }  
  
    /** 
     * ��֤�����ַ����Ƿ���YYYY-MM-DD��ʽ 
     *  
     * @param str 
     * @return 
     */  
    private static boolean isDataFormat(String str) {  
        boolean flag = false;  
        // String  
        // regxStr="[1-9][0-9]{3}-[0-1][0-2]-((0[1-9])|([12][0-9])|(3[01]))";  
        String regxStr = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";  
        Pattern pattern1 = Pattern.compile(regxStr);  
        Matcher isNo = pattern1.matcher(str);  
        if (isNo.matches()) {  
            flag = true;  
        }  
        return flag;  
    }  
    
 	/**
	  * �������Ƿ�������.
	  *
	  * @param str ָ�����ַ���
	  * @return �Ƿ�������:��Ϊtrue������false
	  */
 	public static Boolean isEmail(String str) {
 		Boolean isEmail = false;
 		String expr = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
 		if (str.matches(expr)) {
 			isEmail = true;
 		}
 		return isEmail;
 	}

    /**
     * �ж��Ƿ������п���
     * @param cardId
     * @return
     */
    public static boolean checkBankCard(String cardId) {  
        char bit = getBankCardCheckCode(cardId  
                .substring(0, cardId.length() - 1));  
        if (bit == 'N') {  
            return false;  
        }  
        return cardId.charAt(cardId.length() - 1) == bit;  
  
    }  
    
    private static char getBankCardCheckCode(String nonCheckCodeCardId) {  
        if (nonCheckCodeCardId == null  
                || nonCheckCodeCardId.trim().length() == 0  
                || !nonCheckCodeCardId.matches("\\d+")) {  
            // ������Ĳ������ݷ���N  
            return 'N';  
        }  
        char[] chs = nonCheckCodeCardId.trim().toCharArray();  
        int luhmSum = 0;  
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {  
            int k = chs[i] - '0';  
            if (j % 2 == 0) {  
                k *= 2;  
                k = k / 10 + k % 10;  
            }  
            luhmSum += k;  
        }  
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');  
    }

    /**
     * �ж��Ƿ����ֻ���
     * @param phone
     * @return
     */
    public static boolean checkPhone(String phone) {  
        Pattern pattern = Pattern  
                .compile("^(13[0-9]|15[0-9]|153|15[6-9]|180|18[23]|18[5-9])\\d{8}$");  
        Matcher matcher = pattern.matcher(phone);  
  
        if (matcher.matches()) {  
            return true;  
        }  
        return false;  
    }


    /**
     * �������ж�һ���ַ����Ƿ�Ϊnull���ֵ.
     *
     * @param str ָ�����ַ���
     * @return true or false
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }
    
 	/**
	  * �������Ƿ�������.
	  *
	  * @param str ָ�����ַ���
	  * @return  �Ƿ�������:��Ϊtrue������false
	  */
    public static Boolean isChinese(String str) {
    	Boolean isChinese = true;
        String chinese = "[\u0391-\uFFE5]";
        if(!isEmpty(str)){
	         //��ȡ�ֶ�ֵ�ĳ��ȣ�����������ַ�����ÿ�������ַ�����Ϊ2������Ϊ1
	         for (int i = 0; i < str.length(); i++) {
	             //��ȡһ���ַ�
	             String temp = str.substring(i, i + 1);
	             //�ж��Ƿ�Ϊ�����ַ�
	             if (temp.matches(chinese)) {
	             }else{
	            	 isChinese = false;
	             }
	         }
        }
        return isChinese;
    }
    
    /**
     * �������Ƿ��������.
     *
     * @param str ָ�����ַ���
     * @return  �Ƿ��������:��Ϊtrue������false
     */
    public static Boolean isContainChinese(String str) {
    	Boolean isChinese = false;
        String chinese = "[\u0391-\uFFE5]";
        if(!isEmpty(str)){
	         //��ȡ�ֶ�ֵ�ĳ��ȣ�����������ַ�����ÿ�������ַ�����Ϊ2������Ϊ1
	         for (int i = 0; i < str.length(); i++) {
	             //��ȡһ���ַ�
	             String temp = str.substring(i, i + 1);
	             //�ж��Ƿ�Ϊ�����ַ�
	             if (temp.matches(chinese)) {
	            	 isChinese = true;
	             }else{
	            	 
	             }
	         }
        }
        return isChinese;
    }

}
