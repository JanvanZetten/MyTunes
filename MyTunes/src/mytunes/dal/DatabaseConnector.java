/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.dal;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.Connection;

/**
 *
 * @author Alex, Asbjørn og Jan
 */
public class DatabaseConnector
{

    private SQLServerDataSource dataSource;

    /**
     * Constructor saves connection information.
     */
    public DatabaseConnector()
    {
        dataSource = new SQLServerDataSource();

        dataSource.setServerName("EASV-DB2");
        dataSource.setPortNumber(1433);
        dataSource.setDatabaseName("CS2017A_asbj0378_MyTunes");
        dataSource.setUser("CS2017A_3_java");
        dataSource.setPassword("javajava");
    }

    /**
     * Gets connection.
     *
     * @return SQLServerDataSource.
     * @throws SQLServerException
     */
    public Connection getConnection() throws SQLServerException
    {
        return dataSource.getConnection();
    }

}
