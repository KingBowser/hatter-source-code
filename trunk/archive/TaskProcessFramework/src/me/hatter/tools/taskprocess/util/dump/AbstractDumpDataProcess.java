package me.hatter.tools.taskprocess.util.dump;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import me.hatter.tools.taskprocess.util.env.Env;
import me.hatter.tools.taskprocess.util.io.RollFilePrintWriter;
import me.hatter.tools.taskprocess.util.misc.ExceptionUtils;
import me.hatter.tools.taskprocess.util.misc.ObjectUtils;
import me.hatter.tools.taskprocess.util.misc.StringUtils;

public abstract class AbstractDumpDataProcess {

    protected static final String DRIVER_ORACLE = "oracle.jdbc.OracleDriver";
    protected static final String DRIVER_MYSQL  = "com.mysql.jdbc.Driver";

    protected void mainProcess() {
        try {
            AtomicInteger count = new AtomicInteger(0);
            final RollFilePrintWriter writer = new RollFilePrintWriter(Env.USER_DIR, getOutputFileName(),
                                                                       getOutputFileRollCount(), true);
            System.out.println("[INFO] Start: " + new Date());

            Class.forName(getDriver());
            Connection connection = DriverManager.getConnection(getUrl(), getUsername(), getPassword());

            String sql = getQuery(); // SQL
            System.out.println("[INFO] Runing sql: " + sql);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                if ((count.incrementAndGet() % 1000) == 0) {
                    System.out.println("[INFO] Running at: " + count.get());
                }
                writer.println(StringUtils.join(getRow(resultSet), ","));
            }
            System.out.println("[INFO] Run finish at: " + count.get());

            writer.close();
            connection.close();
            System.out.println("[INFO] Finish!!!!!!! " + new Date());
        } catch (Throwable t) {
            System.out.println("[ERROR] Exception occured: " + ExceptionUtils.getStackTrace(t));
        } finally {
            System.exit(0);
        }
    }

    protected List<String> getRow(ResultSet resultSet) throws SQLException {
        List<String> row = new ArrayList<String>();
        for (String field : getFields()) {
            row.add(getFieldValue(resultSet, field));
        }
        return row;
    }

    protected String getFieldValue(ResultSet resultSet, String field) throws SQLException {
        return ObjectUtils.toString(resultSet.getObject(field));
    }

    protected String getOutputFileName() {
        SimpleDateFormat SDF = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss");
        return "dataexport_" + getTable() + "." + SDF.format(new Date()) + ".txt";
    }

    protected long getOutputFileRollCount() {
        return 50000000;
    }

    protected String getQuery() {
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        sql.append(StringUtils.join(getFields(), ", "));
        sql.append(" from ");
        sql.append(getTable());
        String where = getWhere();
        if (!StringUtils.isEmpty(where)) {
            sql.append(" where ");
            sql.append(where);
        }
        return sql.toString();
    }

    // -----------------------------------------
    abstract protected String getDriver();

    abstract protected String getUrl();

    abstract protected String getUsername();

    abstract protected String getPassword();

    // -----------------------------------------
    abstract protected String getTable();

    abstract protected List<String> getFields();

    abstract protected String getWhere();
}
