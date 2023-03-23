package ca.jrvs.apps.jdbc;

import ca.jrvs.apps.jdbc.DAO.CustomerDao;
import ca.jrvs.apps.jdbc.DAO.OrderDao;
import ca.jrvs.apps.jdbc.PO.Customer;

import java.sql.Connection;
import java.sql.SQLException;

public class JDBCExecutor {
    public static void main(String[] args) {
        DatabaseConnectionManager dcm =
                new DatabaseConnectionManager(
                        "localhost",
                        "hplussport",
                        "postgres",
                        "password"
                );
        try{
            Connection connection = dcm.getConnection();
            OrderDao orderDao = new OrderDao(connection);
            System.out.println(orderDao.findById(1000).toString());
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
