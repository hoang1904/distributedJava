package controller;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import model.Product;

public interface ProductInterface extends Remote{
    public void connectToDb() throws RemoteException;
    public boolean addProduct(Product p) throws RemoteException;
    public boolean removeProduct(int id) throws RemoteException;
    public Product findProduct(int id) throws RemoteException;
    public void updateProduct(int id, String name) throws RemoteException;
    public ArrayList<Product> selectall() throws RemoteException;
}