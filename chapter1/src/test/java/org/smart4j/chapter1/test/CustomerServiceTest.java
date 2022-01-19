package org.smart4j.chapter1.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.smart4j.chapter1.helper.DataBaseHelper;
import org.smart4j.chapter1.model.Customer;
import org.smart4j.chapter1.service.CustomerService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerServiceTest {

    private final CustomerService customerService;

    public CustomerServiceTest() {
        customerService = new CustomerService();
    }

    @Before
    public void init() throws IOException {

        DataBaseHelper.executeSqlFile("sql/customer_init.sql");
    }

    @Test
    public void getCustomerListTest(){
        List<Customer> customerList = null;
        customerList = customerService.getCustomerList();
        Assert.assertEquals(2,customerList.size());
    }

    @Test
    public void getCustomerTest(){
        Integer id = 1;
        Customer customer = customerService.getCustomer(id);
        Assert.assertNotNull(customer);
    }
    @Test
    public void createCustomerTest(){
        Map<String,Object> fieldMap = new HashMap<String,Object>();
        fieldMap.put("name","张三");
        fieldMap.put("email","张三@163.com");
        boolean result = customerService.createCustomer(fieldMap);
        Assert.assertTrue(result);
    }

    @Test
    public void updateCustomerTest(){
        Integer id = 2;
        Map<String,Object> fieldMap = new HashMap<String,Object>();
        fieldMap.put("name","张三丰");
        fieldMap.put("email","zhangsanfeng@163.com");
        boolean result = customerService.updateCustomer(id, fieldMap);
        Assert.assertTrue(result);
    }

    @Test
    public void deleteCustomerTest(){
        Integer id = 2;
        boolean result = customerService.deleteCustomer(id);
        Assert.assertTrue(result);
    }

}
