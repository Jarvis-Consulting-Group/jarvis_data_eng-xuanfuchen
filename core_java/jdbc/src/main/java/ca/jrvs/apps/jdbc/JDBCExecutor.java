package ca.jrvs.apps.jdbc;

import ca.jrvs.apps.jdbc.DAO.CustomerDao;
import ca.jrvs.apps.jdbc.DAO.OrderDao;
import ca.jrvs.apps.jdbc.DTO.OrderDto;
import ca.jrvs.apps.jdbc.PO.Customer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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
            CustomerDao customer = new CustomerDao(connection);
            Customer newCustomer = new Customer("Jane", "Smith", "janedoe@example.com", "555-5678", "456 Main St", "Othertown", "NY", "67890");

            System.out.println(customer.findById(10002));
//            OrderDao orderDao = new OrderDao(connection);
//            List<OrderDto> orders = orderDao.getOrdersByCustomer(789);
//            orders.forEach(System.out::println);
//            System.out.println(orderDao.findById(1009).toString().equals(orders.get(0).toString()));
//            System.out.println(orderDao.findById(1064).toString().equals(orders.get(1).toString()));
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
