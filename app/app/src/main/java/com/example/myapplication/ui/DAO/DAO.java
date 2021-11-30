package com.example.myapplication.ui.DAO;


import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.List;

import static com.mysql.jdbc.Util.stackTraceToString;

public class DAO<T> {

    Connection con;
    private String url;
    private String user;
    private String password;

    public DAO() {
        registerDriver();
    }

    public DAO(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;

        registerDriver();
    }


    //##Utility functions##
    private static void registerDriver() {
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            System.out.println("Driver registered");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private void connectionClosed() {
        if (this.con != null) {
            try {
                con.close();
            } catch (SQLException e2) {
                System.out.println(e2.getMessage());
            }
        }
    }
    private void printSQLExeption(SQLException sqlException) {
        System.out.println(sqlException.getErrorCode());
        System.out.println((stackTraceToString(sqlException)));
        System.out.println(sqlException.getMessage());
    }


    //##Internal operational functions (select/delete/insert/update)##
    private List<T> select(String query, Class typeClass, Object... params) throws SQLException {
        ResultSetHandler<List<T>> handler = new BeanListHandler<T>(typeClass);
        List<T> rowsSelected = new QueryRunner().query(con, query, handler, params);

        if(rowsSelected == null){
            System.out.println("Cannot select");
            throw new SQLException();
        }
        else{
            System.out.println("Selection done.");
        }
        return rowsSelected;
    }
    private int delete(String query, Object... params) throws SQLException {
        int numRowsDeleted = new QueryRunner().update(con, query, params);

        if(numRowsDeleted!=1){
            System.out.println("Cannot delete this row");
            throw new SQLException();
        }
        else{
            System.out.println("Row deleted");
        }
        return numRowsDeleted;
    }
    private int insert(String query, Object... params) throws SQLException {
        int numRowsInserted = new QueryRunner().update(con, query, params);

        if(numRowsInserted!=1){
            System.out.println("Cannot insert this row");
            throw new SQLException();
        }
        else{
            System.out.println("Row inserted");
        }
        return numRowsInserted;
    }
    private int update(String query, Object... params) throws SQLException {
        int numRowsUpdated = new QueryRunner().update(con, query, params);

        if(numRowsUpdated!=1){
            System.out.println("Cannot insert this row");
            throw new SQLException();
        }
        else{
            System.out.println("Row inserted");
        }
        return numRowsUpdated;
    }


    //##Public functional functions##
    public List<T> exc_select(String query, Class typeClass, Object... params) {
        List<T> rowsSelected = null;

        if (query != null && typeClass != null && params != null){
            try {
                con = DriverManager.getConnection(url, user, password);

                if (con != null) {
                    System.out.println("Connected to the database");
                    rowsSelected = select(query, typeClass, params);
                }
                else {
                    System.out.println("Can't connect to the database");
                }
            } catch (SQLException sqlException) {
                printSQLExeption(sqlException);
            } finally {
                connectionClosed();
            }
        }
        else {
            System.err.println("Query or required parameters is not valid");
        }

        return rowsSelected;
    }
    public int exc_delete(String query, Object... params) {
        int numRowsDeleted = 0;

        if(query != null && params != null){
            try {
                con = DriverManager.getConnection(url, user, password);
                if (con != null) {
                    System.out.println("Connected to the database");
                    numRowsDeleted = delete(query, params);
                }
                else {
                    System.out.println("Can't connect to the database");
                }
            }catch (SQLException sqlException){
                printSQLExeption(sqlException);
            }finally {
                connectionClosed();
            }
        }
        else{
            System.err.println("Query or required parameters is not valid");
        }

        return numRowsDeleted;
    }
    public int exc_insert(String query, Object... params) {
        int numRowsInserted = 0;

        if (query != null && params != null){
            try {
                con = DriverManager.getConnection(url, user, password);
                if (con != null) {
                    System.out.println("Connected to the database");
                    numRowsInserted = insert(query, params);
                }
                else {
                    System.out.println("Can't connect to the database");
                }
            }catch (SQLException sqlException){
                printSQLExeption(sqlException);
            }finally {
                connectionClosed();
            }
        }
        else {
            System.err.println("Query or required parameters is not valid");
        }

        return numRowsInserted;
    }
    public int exc_update(String query, Object... params) {
        int rowupdated = 0;

        if (query != null && params != null){
            try {
                con = DriverManager.getConnection(url, user, password);
                if (con != null) {
                    System.out.println("Connected to the database");
                    update(query, params);
                }
                else {
                    System.out.println("Can't connect to the database");
                }
            }catch (SQLException sqlException){
                System.out.println(sqlException.getErrorCode());
                System.out.println((stackTraceToString(sqlException)));
                System.out.println(sqlException.getMessage());
            }finally {
                connectionClosed();
            }
        }
        else {
            System.err.println("query isn't valid or parameters are required");
        }

        return rowupdated;
    }

}



