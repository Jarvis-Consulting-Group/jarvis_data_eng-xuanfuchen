CREATE OR REPLACE FUNCTION get_orders_by_customer (p_customer_id bigint)
  RETURNS TABLE (
    cust_first_name varchar(50),
    cust_last_name varchar(50),
    cust_email varchar(50),
    order_id bigint,
    creation_dt timestamp,
    total_due numeric(10,2),
    status varchar(50),
    sales_first_name varchar(50),
    sales_last_name varchar(50),
    sales_email varchar(50),
    item_quanitty int,
    item_code varchar(50),
    item_name varchar(50),
    item_size int,
    item_variety varchar(50),
    item_price numeric(10,2)
)
AS $$
BEGIN
  RETURN QUERY SELECT
  c.first_name,
  c.last_name,
  c.email,
  o.order_id,
  o.creation_date,
  o.total_due,
  o.status,
  s.first_name,
  s.last_name,
  s.email,
  ol.quantity,
  p.code,
  p.name,
  p.size,
  p.variety,
  p.price
  FROM orders o
  JOIN customer c on o.customer_id = c.customer_id
  JOIN salesperson s on o.salesperson_id = s.salesperson_id
  JOIN order_item ol on ol.order_id = o.order_id
  JOIN product p on ol.product_id = p.product_id
  where c.customer_id = p_customer_id
  order by o.order_id, p.code;
END; $$

LANGUAGE 'plpgsql';