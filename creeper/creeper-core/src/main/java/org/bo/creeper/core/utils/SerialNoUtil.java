package org.bo.creeper.core.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 序号工具类
 * 
 * @author 杨波
 * 
 */
public final class SerialNoUtil {

    private SerialNoUtil() {

    }

    // 数据表编号
    private static Map<String, String> tableSeqenceMap = new HashMap<String, String>() {
        /**
         * serialVersionUID
         */
        private static final long serialVersionUID = 4688443584905462104L;

        {
            /*
             * 编号第一位数字 5:creeper(数据采集)
             */

            // acqu start
            put("tbl_acquisition", "500");
            put("tbl_acquisition_history", "501");
            put("tbl_acquisition_temp", "503");
            // acqu end
        }

    };

    /**
     * 获取表的主键值
     * 
     * @param tableName
     *            表名
     * @return 表的主键值
     */
    public static String getTabPkSn(String tableName) {

        String tableSeqence = tableSeqenceMap.get(tableName.toLowerCase());
        if (tableSeqence == null) {
            throw new RuntimeException();
        }

        String milliseconds = String.valueOf(new Date().getTime());

        return tableSeqence
                + milliseconds.substring(milliseconds.length() - 12)
                + RandomUtils.generateNumberString(5);

    }
    
    public static String getOrderPKSn(){
    	String milliseconds = String.valueOf(new Date().getTime());
        return milliseconds.substring(milliseconds.length() - 12)
                + RandomUtils.generateNumberString(4);
    }
}
