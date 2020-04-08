import java.awt.*;
import java.sql.*;
import java.util.Vector;
import javax.swing.*;

public class Driver {
    public static void main(String[] args) throws SQLException {
        JFrame f = new JFrame();
        JTable j;
        JTable k;

        try {
            // select driver. I used mssql
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            // start connection                                 //
            Connection myConn = DriverManager.getConnection("<INSERT JDBC URL HERE>; user=<INSERT USERNAME HERE>;password=<INSERT PASSWORD HERE>; databaseName=<INSERT DB-NAME HERE>");
            // call prepared statement. Have this created in your SQL server program. I used MSSMS
            CallableStatement myStmt = myConn.prepareCall("{call <INSERT STORED PROCEDURE HERE>}");
            // load the results from the stored procedures
            ResultSet rs = myStmt.executeQuery();
            // this invokes metadata from the table that's being called
            ResultSetMetaData rsmd = rs.getMetaData();
            // use the vector data-structure to make life easier
            int columnCount = rsmd.getColumnCount();
            Vector column = new Vector(columnCount);
            for(int i = 1; i<= columnCount; i++){
                column.add(rsmd.getColumnName(i));
            }
            Vector data = new Vector();
            Vector row = new Vector();
            // Generating resultSet1
            while(rs.next()) {
                row = new Vector(columnCount);
                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getString(i));
                }
                data.add(row);
            }
            // by invoking getMoreResults from myStmt, we get to the next table
            myStmt.getMoreResults();
            // load new resultset from the same stored procedure as last time
            ResultSet rs2 = myStmt.getResultSet();
            ResultSetMetaData rsmd2 = rs2.getMetaData();
            // using vectors again
            int columnCount2 = rsmd2.getColumnCount();
            Vector column2 = new Vector(columnCount2);
            for(int i = 1; i<= columnCount2; i++){
                column2.add(rsmd2.getColumnName(i));
            }
            Vector data2 = new Vector();
            Vector row2 = new Vector();
            // Generating resultSet2
            while(rs2.next()){
                row2 = new Vector(columnCount2);
                for (int i = 1; i <= columnCount2; i++) {
                    row2.add(rs2.getString(i));
                }
                data2.add(row2);
            }
            //jtable setup
            j = new JTable(data,column); //jtables luckily take in vectors as an argument
            k = new JTable(data2,column2);
            JScrollPane sp = new JScrollPane(j);
            JScrollPane sp2 = new JScrollPane(k);
            f.setTitle("JDBC And JTable");
            f.add(sp, BorderLayout.NORTH);
            f.add(sp2, BorderLayout.CENTER);
            f.setSize(1280, 720);
            f.setLocationRelativeTo(null);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setVisible(true);
            //
            System.out.println("Statement called and executed");
            //close connections and misc.
            myConn.close();
            myStmt.close();
            rs.close();
            rs2.close();


        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "error");
            e.printStackTrace();
        }
    }
}