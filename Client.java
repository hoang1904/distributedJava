package controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import model.Product;

public class Client extends JFrame {
    private JTextArea kqArea; // kq là viết tắt của "kết quả"
    private ProductInterface product;

    public Client() {
        super("--- Quản lý Sản phẩm ---");
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1098);
            product = (ProductInterface) registry.lookup("Product");
            product.connectToDb();
        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 1, 5, 5));

        JButton themButton = new JButton("Thêm Sản phẩm");
        themButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                themSanPham();
            }
        });
        buttonPanel.add(themButton);

        JButton xoaButton = new JButton("Xóa Sản phẩm");
        xoaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                xoaSanPham();
            }
        });
        buttonPanel.add(xoaButton);

        JButton capNhatButton = new JButton("Cập nhật Sản phẩm");
        capNhatButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                capNhatSanPham();
            }
        });
        buttonPanel.add(capNhatButton);

        JButton timButton = new JButton("Tìm kiếm Sản phẩm");
        timButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                timKiemSanPham();
            }
        });
        buttonPanel.add(timButton);

        JButton hienThiButton = new JButton("Hiển thị Tất cả Sản phẩm");
        hienThiButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hienThiTatCaSanPham();
            }
        });
        buttonPanel.add(hienThiButton);

        kqArea = new JTextArea(10, 20);
        kqArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(kqArea);

        mainPanel.add(buttonPanel, BorderLayout.WEST);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void themSanPham() {
        // Xóa kết quả cũ
        kqArea.setText("");
        
        try {
            String idInput = JOptionPane.showInputDialog(this, "Nhập ID của sản phẩm:");
            if (idInput == null || idInput.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID không thể để trống.", "Nhập không hợp lệ", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int id = Integer.parseInt(idInput);

            // Kiểm tra xem ID đã tồn tại trong cơ sở dữ liệu chưa
            Product existingProduct = product.findProduct(id);
            if (existingProduct != null) {
                JOptionPane.showMessageDialog(this, "Sản phẩm với ID " + id + " đã tồn tại.", "Nhập không hợp lệ", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String name = JOptionPane.showInputDialog(this, "Nhập tên của sản phẩm:");
            if (name == null || name.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên không thể để trống.", "Nhập không hợp lệ", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Product p = new Product(id, name);
            boolean success = product.addProduct(p);
            if (success) {
                kqArea.append("Sản phẩm được thêm thành công.\n");
            } else {
                kqArea.append("Không thể thêm sản phẩm.\n");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID phải là số nguyên hợp lệ.", "Nhập không hợp lệ", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void xoaSanPham() {
        // Xóa kết quả cũ
        kqArea.setText("");
        
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog(this, "Nhập ID của sản phẩm cần xóa:"));
            if (id <= 0) {
                JOptionPane.showMessageDialog(this, "ID phải là số nguyên dương.", "Nhập không hợp lệ", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Kiểm tra xem sản phẩm có tồn tại trong cơ sở dữ liệu hay không
            Product existingProduct = product.findProduct(id);
            if (existingProduct == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm với ID đã nhập.", "Không tìm thấy", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = product.removeProduct(id);
            if (success) {
                kqArea.append("Sản phẩm đã được xóa thành công.\n");
            } else {
                kqArea.append("Không thể xóa sản phẩm.\n");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID phải là số nguyên hợp lệ.", "Nhập không hợp lệ", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void capNhatSanPham() {
        // Xóa kết quả cũ
        kqArea.setText("");
        
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog(this, "Nhập ID của sản phẩm cần cập nhật:"));
            if (id <= 0) {
                JOptionPane.showMessageDialog(this, "ID phải là số nguyên dương.", "Nhập không hợp lệ", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Kiểm tra xem sản phẩm có tồn tại trong cơ sở dữ liệu hay không
            Product existingProduct = product.findProduct(id);
            if (existingProduct == null) {
                JOptionPane.showMessageDialog(this, "Sản phẩm với ID " + id + " không tồn tại.", "Nhập không hợp lệ", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String name = JOptionPane.showInputDialog(this, "Nhập tên mới của sản phẩm:");
            if (name == null || name.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên không thể để trống.", "Nhập không hợp lệ", JOptionPane.ERROR_MESSAGE);
                return;
            }

            product.updateProduct(id, name);
            kqArea.append("Sản phẩm đã được cập nhật thành công.\n");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID phải là số nguyên hợp lệ.", "Nhập không hợp lệ", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void timKiemSanPham() {
        // Xóa kết quả cũ
        kqArea.setText("");
        
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog(this, "Nhập ID của sản phẩm cần tìm:"));
            if (id <= 0) {
                JOptionPane.showMessageDialog(this, "ID phải là số nguyên dương.", "Nhập không hợp lệ", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Product foundProduct = product.findProduct(id);
            if (foundProduct != null) {
                kqArea.append(foundProduct.toString() + "\n");
            } else {
                kqArea.append("Không tìm thấy sản phẩm.\n");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID phải là số nguyên hợp lệ.", "Nhập không hợp lệ", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void hienThiTatCaSanPham() {
        // Xóa kết quả cũ
        kqArea.setText("");
        
        try {
            ArrayList<Product> products = product.selectall();
            for (Product p : products) {
                kqArea.append(p.toString() + "\n");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Client();
            }
        });
    }
}
