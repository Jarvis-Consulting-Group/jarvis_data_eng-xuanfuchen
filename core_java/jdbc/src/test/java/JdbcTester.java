import ca.jrvs.apps.jdbc.DAO.CustomerDao;
import ca.jrvs.apps.jdbc.DAO.OrderDao;
import ca.jrvs.apps.jdbc.DTO.OrderDto;
import ca.jrvs.apps.jdbc.DTO.OrderLine;
import ca.jrvs.apps.jdbc.DatabaseConnectionManager;
import ca.jrvs.apps.jdbc.PO.Customer;
import ca.jrvs.apps.jdbc.PO.Order;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class JdbcTester {
    private Connection connection;

    @BeforeEach
    public void setUP() throws SQLException {
        DatabaseConnectionManager dcm =
                new DatabaseConnectionManager(
                        "localhost",
                        "hplussport",
                        "postgres",
                        "password"
                );
        try{
            this.connection = dcm.getConnection();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    @AfterEach
    public void tearDown() throws SQLException{
        if(connection != null && !connection.isClosed()){
            connection.close();
        }
    }

    @Test
    public void test_find_customer_by_id(){
        CustomerDao customerDao = new CustomerDao(connection);
        String expected = "Customer{id=1000, firstName='Victor', lastName='Woods', email='vwoodsp0@blogtalkradio.com', phone='(786)720-8933', address='00 Bunting Terrace', city='Miami', state='FL', zipCode='33164'}";
        String actual = customerDao.findById(1000).toString();
        assertEquals(expected, actual);
    }

    @Test
    public void test_create_customer(){
        CustomerDao customerDao = new CustomerDao(connection);
        Customer customer = new Customer("John", "Doe", "johndoe@example.com", "555-1234", "123 Main St", "Anytown", "CA", "12345");
        Long lastID = customerDao.getLastCustomer().getId();
        customer.setId(lastID + 1);
        customerDao.create(customer);
        String expected = customer.toString();
        String actual = customerDao.getLastCustomer().toString();
        assertEquals(expected, actual);
    }

    @Test
    public void test_delete_customer(){
        CustomerDao customerDao = new CustomerDao(connection);
        String expected = "Customer{id=0, firstName='null', lastName='null', email='null', phone='null', address='null', city='null', state='null', zipCode='null'}";
        Customer customer = new Customer("Jane", "Smith", "janedoe@example.com", "555-5678", "456 Main St", "Othertown", "NY", "67890");
        customerDao.create(customer);
        Customer lastCustomer = customerDao.getLastCustomer();
        Long lastId = lastCustomer.getId();
        assertEquals(false, customerDao.findById(lastId).toString().equals(expected), "Last customer should not be null before delete");
        customerDao.delete(lastId);
        assertEquals(true, customerDao.findById(lastId).toString().equals(expected), "Last customer should be null after delete");
    }

    @Test
    public void test_update_customer(){
        CustomerDao customerDao = new CustomerDao(connection);
        Customer customer = new Customer("Alice", "Brown", "alicebrown@example.com", "555-9012", "789 Main St", "Anycity", "TX", "23456");
        customerDao.create(customer);
        Long lastId = customerDao.getLastCustomer().getId();
        customer.setId(lastId);
        customer.setLastName("Davis");
        customerDao.update(customer);
        String expected = customer.toString();
        String actual = customerDao.findById(lastId).toString();
        assertEquals(true, actual.equals(expected));
    }

    @Test
    public void test_find_order_by_id(){
        OrderDao orderDao = new OrderDao(connection);
        OrderDto actual = orderDao.findById(1000);

        OrderDto expect = new OrderDto();
        // Create the order lines
        List<OrderLine> orderLines = new ArrayList<>();
        OrderLine orderLine1 = new OrderLine();
        orderLine1.setProductCode("MWCRA20");
        orderLine1.setProductName("Mineral Water");
        orderLine1.setProductSize(20);
        orderLine1.setProductVariety("Cranberry");
        orderLine1.setProductPrice(1.79);
        orderLine1.setQuantity(31);
        orderLines.add(orderLine1);

        OrderLine orderLine2 = new OrderLine();
        orderLine2.setProductCode("MWLEM32");
        orderLine2.setProductName("Mineral Water");
        orderLine2.setProductSize(32);
        orderLine2.setProductVariety("Lemon-Lime");
        orderLine2.setProductPrice(3.69);
        orderLine2.setQuantity(17);
        orderLines.add(orderLine2);

// Create the order object
        expect.setId(1000L);
        expect.setCustomerFirstName("Angela");
        expect.setCustomerLastName("Crawford");
        expect.setCustomerEmail("acrawford8p@com.com");
        expect.setSalespersonFirstName("Edward");
        expect.setSalespersonLastName("Kelley");
        expect.setSalespersonEmail("ekelleyu@hplussport.com");
        expect.setCreationDate(new Date(1463198400000L));
        expect.setTotalDue(118.22);
        expect.setStatus("paid");
        expect.setOrderLines(orderLines);

        assertEquals(true, actual.toString().equals(expect.toString()));
    }
}
