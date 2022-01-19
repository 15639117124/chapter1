package org.smart4j.chapter1.service;

import org.omg.CORBA.PRIVATE_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.chapter1.helper.DataBaseHelper;
import org.smart4j.chapter1.model.Customer;
import org.smart4j.chapter1.util.PropsUtil;

import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class CustomerService {


    private static final Logger LOGGER = LoggerFactory.getLogger(PropsUtil.class);//不太懂
    //获取客户列表
    public List<Customer> getCustomerList() {
        String sql = "select * from customer";
        return DataBaseHelper.queryEntityList(Customer.class,sql);
    }

    //获取客户
    public Customer getCustomer(Integer id){
        String sql = "select * from customer where id = "+id;
        return DataBaseHelper.queryEntity(Customer.class,sql);

    }

    //创建客户
    public boolean createCustomer(Map<String,Object> fieldMap){
        return DataBaseHelper.insertEntity(Customer.class,fieldMap);

    }

    //更新客户
    public boolean updateCustomer(Integer id,Map<String,Object> fieldMap){
        return DataBaseHelper.updateEntity(Customer.class,id,fieldMap);
    }

    //删除客户
    public boolean deleteCustomer(Integer id){
        return DataBaseHelper.deleteEmpty(Customer.class,id);
    }
}
