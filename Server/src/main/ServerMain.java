
package main;

import controller.ProductImplement;
import controller.ProductInterface;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerMain {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1098);
            ProductInterface productSkeleton = new ProductImplement();
            registry.rebind("Product", productSkeleton);
            
            
                System.out.println("server đã sẵn sàng ");
           
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
