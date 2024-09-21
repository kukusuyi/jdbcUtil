package apiMysql;

import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ConMysql implements AutoCloseable {
    Logger log = Logger.getLogger("org.example.apiMysql.ConMysql");

    private final Connection connection;

    // 这里之所以注释掉了 驱动加载 ，是因为在 java5 以上版本都会自动加载该类
    public ConMysql(String url, String username, String password) throws SQLException {
//        Class.forName("com.mysql.cj.jdbc.Driver");
        this.connection = DriverManager.getConnection(url,username,password);
        log.finest("连接成功");
    }

    public ResultSet executeQuery(String column, ArrayList<String> findObject) throws SQLException {
        String find = String.join(",", findObject);
        String query = "SELECT " + find + " FROM " + column;
        log.finest("sql查询:"+query);
        ResultSet resultSet = this.connection.prepareStatement(query).executeQuery();
        return resultSet;
    }

    public int updateQueryWithStringMap(String column, Map<String,String> updateObject) throws SQLException {

        if(updateObject.isEmpty()){
            return 0;
        }

        StringBuilder updateQueryBuilder = new StringBuilder("UPDATE ").append(column).append(" SET ");
        List<String> keys = new ArrayList<>(updateObject.keySet());
        for (int i = 0; i < keys.size(); i++) {
            updateQueryBuilder.append(keys.get(i)).append(" = ?");
            if (i < keys.size() - 1) {
                updateQueryBuilder.append(", ");
            }
        }

        try(PreparedStatement preparedStatement = this.connection.prepareStatement(updateQueryBuilder.toString())){
            int index = 1;
            for (String value : updateObject.values()) {
                preparedStatement.setString(index++, value);
            }
            return preparedStatement.executeUpdate();
        }
    }

    public int updateQueryWithStringMap(@NotNull String column, @NotNull Map<String,String> updateObject, String whereClause) throws SQLException {
        if(updateObject.isEmpty() || column.isEmpty()){
            return 0;
        }

        StringBuilder updateQueryBuilder = new StringBuilder("UPDATE ").append(column).append(" SET ");
        List<String> keys = new ArrayList<>(updateObject.keySet());
        for (int i = 0; i < keys.size(); i++) {
            updateQueryBuilder.append(keys.get(i)).append(" = ?");
            if (i < keys.size() - 1) {
                updateQueryBuilder.append(", ");
            }
        }

        if (whereClause != null && !whereClause.isEmpty()) {
            updateQueryBuilder.append(" WHERE ").append(whereClause);
        }

        try(PreparedStatement preparedStatement = this.connection.prepareStatement(updateQueryBuilder.toString())){
            int index = 1;
            for (String value : updateObject.values()) {
                preparedStatement.setString(index++, value);
            }
            return preparedStatement.executeUpdate();
        }
    }



    public int updateQueryWithIntegerMap(String column, Map<String, Integer> updateObject) throws SQLException {

        return 0;
    }

    private void closeConnection() throws SQLException {
        if (this.connection != null) {
            connection.close();
        }
    }

    @Override
    public void close() throws Exception {
        closeConnection();
    }
}
