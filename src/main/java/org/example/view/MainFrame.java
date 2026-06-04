package org.example.view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private ProductPanel productPanel;
    private CategoriaPanel categoriaPanel;
    private BuyerPanel buyerPanel;
    private SalePanel salePanel;
    
    public MainFrame() {
        setTitle("Music Shop Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(true);
        
        tabbedPane = new JTabbedPane();
        
        productPanel = new ProductPanel();
        categoriaPanel = new CategoriaPanel();
        buyerPanel = new BuyerPanel();
        salePanel = new SalePanel();
        
        tabbedPane.addTab("Productos", productPanel);
        tabbedPane.addTab("Categorías", categoriaPanel);
        tabbedPane.addTab("Compradores", buyerPanel);
        tabbedPane.addTab("Ventas", salePanel);
        
        add(tabbedPane);
        setVisible(true);
    }
}
