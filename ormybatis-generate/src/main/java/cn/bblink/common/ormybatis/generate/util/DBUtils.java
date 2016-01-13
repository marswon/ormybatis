package cn.bblink.common.ormybatis.generate.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

/**
 * jdbc连接tool数据库
 * @author donghui
 */
public class DBUtils {

    public static Connection getConnection() {
        String url = "jdbc:mysql://192.168.0.173/bblink_hoswifi?characterEncoding=utf8";
        String name = "root";
        String pwd = "bblink2014$";
        String driver = "com.mysql.jdbc.Driver";
        Connection conn = null;
        DbUtils.loadDriver(driver);
        try {
            conn = DriverManager.getConnection(url, name, pwd);
        } catch (SQLException e) {
           e.printStackTrace();
        }
        return conn;
    }

    public static List<Map> selectList(String sql) {
        Connection conn = getConnection();
        QueryRunner qr = new QueryRunner();
        List<Map> results = null;
        try {
            results = (List) qr.query(conn, sql, new MapListHandler());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return results;
    }

    public static int update(String sql) throws SQLException {
        Connection conn = getConnection();
        QueryRunner qr = new QueryRunner();
        int num = 0;
        try {
            num = qr.update(conn, sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return num;
    }

    public static Object selectObject(String sql) {
        if (DBUtils.selectList(sql).size() > 0) {
            Map map = DBUtils.selectList(sql).get(0);
            return map.values().iterator().next();
        }
        return null;
    }

    public static Map selectMap(String sql) {
        List<Map> list = DBUtils.selectList(sql);
        if (CollectionUtils.isEmpty(list)) {
            return new HashMap();
        }
        return list.get(0);
    }

}