
package controller;


import java.sql.Connection;
import java.io.Serializable;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.ResultSet;
import model.Product;


public class ProductImplement extends UnicastRemoteObject implements ProductInterface, Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Connection conn;
    
    public ProductImplement() throws RemoteException{       
    }
    
    @Override
    public void connectToDb() throws RemoteException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/rmi";
            String userName = "root";
            String password = "123456789";
            conn = DriverManager.getConnection(url, userName, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean addProduct(Product p) throws RemoteException {
        String sql = "INSERT INTO tblproduct(id, name) VALUES(?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, p.getId());
            ps.setString(2, p.getName());
            
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean removeProduct(int id) throws RemoteException {
        String sql  ="delete from tblproduct where id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Product findProduct(int id) throws RemoteException {
        String sql = "Select * from tblproduct where id = " + id;
        try {
            Statement s = conn.createStatement();
            ResultSet result  = s.executeQuery(sql);
            while(result.next()){
                int idproduct = result.getInt("id");
                String name  = result.getString("name");
                Product p = new Product(idproduct, name);
                
                return p;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("khong tim thay");
        return null;
    }

    @Override
    public void updateProduct(int id, String name) throws RemoteException {
        String sql = "UPDATE tblproduct SET name = ? WHERE id = ?";
    
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, id);

            preparedStatement.executeUpdate();
        
            
            System.out.println("Cập nhập thành công");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public ArrayList<Product> selectall() throws RemoteException {
        ArrayList<Product> result = new ArrayList<>();
        
        try {
            String sql = "select * from tblproduct";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while(rs.next()){
                int id = rs.getInt("id");
                String name  = rs.getString("name");
                Product p = new Product(id, name);
                
                result.add(p);
            }
        } catch (SQLException ex) {        
        }
        return result;
    }

    
    
}
