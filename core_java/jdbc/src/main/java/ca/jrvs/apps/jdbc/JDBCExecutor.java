package ca.jrvs.apps.jdbc;

import ca.jrvs.apps.jdbc.DAO.CustomerDao;
import ca.jrvs.apps.jdbc.PO.Customer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCExecutor {
    public static void main(String[] args) {
        DatabaseConnectionManager dcm = new DatabaseConnectionManager(  "localhost",
                                                                        "hplussport",
                                                                        "postgres",
                                                                        "password");
        try{
            Connection connection = dcm.getConnection();
            CustomerDao customerDao = new CustomerDao(connection);
            customerDao.findById(10001).print();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
