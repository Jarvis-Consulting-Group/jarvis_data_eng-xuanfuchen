package ca.jrvs.apps.jdbc.DAO;

import ca.jrvs.apps.jdbc.DTO.OrderDto;
import ca.jrvs.apps.jdbc.DTO.OrderLine;
import ca.jrvs.apps.jdbc.PO.Customer;
import ca.jrvs.apps.jdbc.util.DataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDao extends DataAccessObject<OrderDto> {
    private static final String GET_BY_ID =
            "SELECT c.first_name AS customer_first_name, " +
            "   c.last_name AS customer_last_name, " +
            "   c.email AS customer_email, " +
            "   o.order_id, " +
            "   o.creation_date, " +
            "   o.total_due, " +
            "   o.status, " +
            "   s.first_name AS salesperson_first_name, " +
            "   s.last_name AS salesperson_last_name, " +
            "   s.email AS salesperson_email, " +
            "   ol.quantity, " +
            "   p.code, " +
            "   p.name, " +
            "   p.size, " +
            "   p.variety, " +
            "   p.price " +
            "FROM orders o " +
            "   JOIN customer c ON o.customer_id = c.customer_id " +
            "   JOIN salesperson s ON o.salesperson_id = s.salesperson_id " +
            "   JOIN order_item ol ON ol.order_id = o.order_id " +
            "   JOIN product p ON ol.product_id = p.product_id " +
            "WHERE o.order_id = ?;";

    private static final String GET_BY_CUSTOMER = "SELECT * FROM get_orders_by_customer(?)";
    public OrderDao(Connection connection) {
        super(connection);
    }

    @Override
    public OrderDto findById(long id) {
        OrderDto order = null;
        try(PreparedStatement statement = connection.prepareStatement(GET_BY_ID)){
            //finish the statement by adding the argument into it
            statement.setLong(1, id);
            //get the result set from the execution result
            ResultSet rs = statement.executeQuery();
            //iterate through all retrieved records(there should be only one records)
            while(rs.next()){
                //create an orderDto object if it has not been created
                if(order == null){
                    order = new OrderDto();
                    order.setId(rs.getLong("order_id"));
                    order.setCustomerFirstName(rs.getString("customer_first_name"));
                    order.setCustomerLastName(rs.getString("customer_last_name"));
                    order.setCustomerEmail(rs.getString("customer_email"));
                    order.setCreationDate(rs.getDate("creation_date"));
                    order.setTotalDue(rs.getDouble("total_due"));
                    order.setStatus(rs.getString("status"));
                    order.setSalespersonFirstName(rs.getString("salesperson_first_name"));
                    order.setSalespersonLastName(rs.getString("salesperson_last_name"));
                    order.setSalespersonEmail(rs.getString("salesperson_email"));
                    order.setOrderLines(new ArrayList<>());
                }
                //add all orderlines to the orderline list
                OrderLine orderLine = new OrderLine();
                orderLine.setQuantity(rs.getInt("quantity"));
                orderLine.setProductCode(rs.getString("code"));
                orderLine.setProductName(rs.getString("name"));
                orderLine.setProductSize(rs.getInt("size"));
                orderLine.setProductVariety(rs.getString("variety"));
                orderLine.setProductPrice(rs.getDouble("price"));
                order.getOrderLines().add(orderLine);
            }
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return order;
    }

    @Override
    public List<OrderDto> findAll() {
        return null;
    }

    @Override
    public OrderDto update(OrderDto dto) {
        return null;
    }

    @Override
    public OrderDto create(OrderDto dto) {
        return null;
    }

    @Override
    public void delete(long id) {
    }

    public List<OrderDto> getOrdersByCustomer(long customerId){
        List<OrderDto> orders = new ArrayList<>();
        try(PreparedStatement statement = this.connection.prepareStatement(GET_BY_CUSTOMER)){
            //finished the statement by adding the argument into it
            statement.setLong(1, customerId);
            //get the result set form the execution result.
            ResultSet rs = statement.executeQuery();
            OrderDto order = null;
            long prevOrderId = 0;
            while(rs.next()){
                long currOrderId = rs.getLong("order_id");
                //if the order id of current record is not the same as the previous record,
                //then we need to create a new order object
                if(currOrderId != prevOrderId){
                    order = new OrderDto();
                    orders.add(order);
                    order.setId(currOrderId);
                    order.setCustomerFirstName(rs.getString("cust_first_name"));
                    order.setCustomerLastName(rs.getString("cust_last_name"));
                    order.setCustomerEmail(rs.getString("cust_email"));
                    order.setCreationDate(rs.getDate("creation_dt"));
                    order.setTotalDue(rs.getDouble("total_due"));
                    order.setStatus(rs.getString("status"));
                    order.setSalespersonFirstName(rs.getString("sales_first_name"));
                    order.setSalespersonLastName(rs.getString("sales_last_name"));
                    order.setSalespersonEmail(rs.getString("sales_email"));
                    order.setOrderLines(new ArrayList<>());
                }
                //build a new orderline object and add it to the orderline list
                OrderLine orderLine = new OrderLine();
                orderLine.setQuantity(rs.getInt("item_quanitty"));
                orderLine.setProductCode(rs.getString("item_code"));
                orderLine.setProductName(rs.getString("item_name"));
                orderLine.setProductSize(rs.getInt("item_size"));
                orderLine.setProductVariety(rs.getString("item_variety"));
                orderLine.setProductPrice(rs.getDouble("item_price"));
                order.getOrderLines().add(orderLine);
                //in the next iteration, current ID will become the previous ID
                prevOrderId = currOrderId;
            }
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return orders;
    }
}
