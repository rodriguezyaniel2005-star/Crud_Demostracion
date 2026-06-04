package org.example.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import org.example.controller.BuyerController;
import org.example.controller.SaleController;
import org.example.event.EntityChangeEvent;
import org.example.event.EntityChangeListener;
import org.example.event.EntityChangeManager;
import org.example.model.Buyer;
import org.example.model.Sale;

public class SalePanel extends JPanel implements EntityChangeListener {
    private SaleController saleController;
    private BuyerController buyerController;
    private EntityChangeManager eventManager; // Event manager to register for changes
    private JTable saleTable;
    private DefaultTableModel tableModel;
    private JComboBox<Buyer> buyerCombo;
    private JButton refreshBtn;
    
    public SalePanel() {
        saleController = new SaleController();
        buyerController = new BuyerController();
        eventManager = EntityChangeManager.getInstance();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel filterPanel = createFilterPanel();
        add(filterPanel, BorderLayout.NORTH);
        
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);
        
        refreshTable();
        
        // Register listener for Buyer changes to update combo box automatically
        eventManager.addListener(EntityChangeEvent.ENTITY_BUYER, this);
        // Also listen for Sale changes to refresh the table
        eventManager.addListener(EntityChangeEvent.ENTITY_SALE, this);
    }
    
    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Filtrar Ventas"));
        
        panel.add(new JLabel("Comprador:"));
        buyerCombo = new JComboBox<>();
        buyerCombo.addItem(null);
        List<Buyer> buyers = buyerController.obtenerTodosCompradores();
        if (buyers != null) {
            for (Buyer buyer : buyers) {
                buyerCombo.addItem(buyer);
            }
        }
        buyerCombo.addActionListener(e -> filterSales());
        panel.add(buyerCombo);
        
        refreshBtn = new JButton("Refrescar");
        refreshBtn.addActionListener(e -> refreshTable());
        panel.add(refreshBtn);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(
            new String[]{"ID", "Comprador", "Total", "Fecha", "Items"}, 0);
        saleTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(saleTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton analyzeBtn = new JButton("Analizar Venta");
        analyzeBtn.addActionListener(e -> analyzeSale());
        buttonPanel.add(analyzeBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Sale> sales = saleController.obtenerTodasVentas();
        if (sales != null) {
            for (Sale sale : sales) {
                Buyer buyer = buyerController.obtenerComprador(sale.getCompradorId());
                String buyerName = buyer != null ? buyer.getNombre() : "Unknown";
                String fecha = sale.getFecha().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                int itemCount = sale.getProductosComprados() != null ? sale.getProductosComprados().size() : 0;
                
                tableModel.addRow(new Object[]{
                    sale.getId().toString(),
                    buyerName,
                    String.format("$%.2f", sale.getTotalVenta()),
                    fecha,
                    itemCount + " items"
                });
            }
        }
    }
    
    private void filterSales() {
        tableModel.setRowCount(0);
        Buyer selectedBuyer = (Buyer) buyerCombo.getSelectedItem();
        
        if (selectedBuyer != null) {
            List<Sale> sales = saleController.obtenerVentasPorComprador(selectedBuyer.getId());
            if (sales != null) {
                for (Sale sale : sales) {
                    String fecha = sale.getFecha().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    int itemCount = sale.getProductosComprados() != null ? sale.getProductosComprados().size() : 0;
                    
                    tableModel.addRow(new Object[]{
                        sale.getId().toString(),
                        selectedBuyer.getNombre(),
                        String.format("$%.2f", sale.getTotalVenta()),
                        fecha,
                        itemCount + " items"
                    });
                }
            }
        } else {
            refreshTable();
        }
    }
    
    @Override
    public void onEntityChanged(EntityChangeEvent event) {
        // Auto-refresh buyer combo when buyers change (create/update/delete)
        if (EntityChangeEvent.ENTITY_BUYER.equals(event.getEntityType())) {
            SwingUtilities.invokeLater(this::refreshBuyerCombo);
        }
        // Auto-refresh sales table when sales change
        else if (EntityChangeEvent.ENTITY_SALE.equals(event.getEntityType())) {
            SwingUtilities.invokeLater(this::refreshTable);
        }
    }
    
    /**
     * Refresh the buyer combo box while preserving selection if possible
     */
    private void refreshBuyerCombo() {
        Buyer selectedBuyer = (Buyer) buyerCombo.getSelectedItem();
        buyerCombo.removeAllItems();
        buyerCombo.addItem(null);
        List<Buyer> buyers = buyerController.obtenerTodosCompradores();
        if (buyers != null) {
            for (Buyer buyer : buyers) {
                buyerCombo.addItem(buyer);
            }
        }
        // Try to restore previous selection
        if (selectedBuyer != null) {
            buyerCombo.setSelectedItem(selectedBuyer);
        }
    }
    
    private void analyzeSale() {
        int row = saleTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una venta de la tabla", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String idStr = (String) tableModel.getValueAt(row, 0);
        org.bson.types.ObjectId saleId = new org.bson.types.ObjectId(idStr);
        Sale sale = saleController.obtenerVenta(saleId);
        if (sale == null) {
            JOptionPane.showMessageDialog(this, "No se pudo cargar la información de la venta", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Buyer buyer = buyerController.obtenerComprador(sale.getCompradorId());
        showAnalysisDialog(sale, buyer);
    }
    
    private void showAnalysisDialog(Sale sale, Buyer buyer) {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Análisis de Venta / Historial del Cliente", true);
        dialog.setSize(800, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        
        // Customer Info Panel
        JPanel infoPanel = new JPanel(new GridLayout(2, 2, 10, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Información del Cliente"));
        infoPanel.add(new JLabel("Nombre: " + (buyer != null ? buyer.getNombre() + " " + buyer.getApellidos() : "Desconocido")));
        infoPanel.add(new JLabel("Email: " + (buyer != null ? buyer.getEmail() : "Desconocido")));
        infoPanel.add(new JLabel("Teléfono: " + (buyer != null ? buyer.getTelefono() : "Desconocido")));
        infoPanel.add(new JLabel("Dirección: " + (buyer != null ? buyer.getDireccion() : "Desconocido")));
        
        // SplitPane for the two tables
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(220);
        
        // Table 1: Products in this sale
        JPanel saleProductsPanel = new JPanel(new BorderLayout());
        saleProductsPanel.setBorder(BorderFactory.createTitledBorder("Productos en esta Venta"));
        DefaultTableModel saleProductsModel = new DefaultTableModel(
            new String[]{"Producto", "Categoría", "Cantidad", "Precio Unitario", "Subtotal"}, 0);
        JTable saleProductsTable = new JTable(saleProductsModel);
        
        if (sale.getProductosComprados() != null) {
            for (Sale.ProductoVendido p : sale.getProductosComprados()) {
                saleProductsModel.addRow(new Object[]{
                    p.getProductoNombre(),
                    p.getCategoriaNombre(),
                    p.getCantidad(),
                    String.format("$%.2f", p.getPrecioUnitario()),
                    String.format("$%.2f", p.getSubtotal())
                });
            }
        }
        saleProductsPanel.add(new JScrollPane(saleProductsTable), BorderLayout.CENTER);
        
        // Table 2: All products bought by this buyer
        JPanel allProductsPanel = new JPanel(new BorderLayout());
        allProductsPanel.setBorder(BorderFactory.createTitledBorder("Historial de Productos Comprados por el Cliente"));
        DefaultTableModel allProductsModel = new DefaultTableModel(
            new String[]{"Producto", "Categoría", "Cantidad Total", "Total Gastado"}, 0);
        JTable allProductsTable = new JTable(allProductsModel);
        
        if (buyer != null) {
            List<Sale> buyerSales = saleController.obtenerVentasPorComprador(buyer.getId());
            if (buyerSales != null) {
                java.util.Map<String, ProductSummary> summaryMap = new java.util.HashMap<>();
                for (Sale s : buyerSales) {
                    if (s.getProductosComprados() != null) {
                        for (Sale.ProductoVendido p : s.getProductosComprados()) {
                            String key = p.getProductoNombre();
                            ProductSummary summary = summaryMap.get(key);
                            if (summary == null) {
                                summary = new ProductSummary(p.getProductoNombre(), p.getCategoriaNombre());
                                summaryMap.put(key, summary);
                            }
                            summary.cantidad += p.getCantidad();
                            summary.totalGastado += p.getSubtotal();
                        }
                    }
                }
                for (ProductSummary summary : summaryMap.values()) {
                    allProductsModel.addRow(new Object[]{
                        summary.nombre,
                        summary.categoria,
                        summary.cantidad,
                        String.format("$%.2f", summary.totalGastado)
                    });
                }
            }
        }
        allProductsPanel.add(new JScrollPane(allProductsTable), BorderLayout.CENTER);
        
        splitPane.setTopComponent(saleProductsPanel);
        splitPane.setBottomComponent(allProductsPanel);
        
        dialog.add(infoPanel, BorderLayout.NORTH);
        dialog.add(splitPane, BorderLayout.CENTER);
        
        // Close Button
        JButton closeBtn = new JButton("Cerrar");
        closeBtn.addActionListener(e -> dialog.dispose());
        JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        closePanel.add(closeBtn);
        dialog.add(closePanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    private static class ProductSummary {
        String nombre;
        String categoria;
        int cantidad;
        double totalGastado;
        
        ProductSummary(String nombre, String categoria) {
            this.nombre = nombre;
            this.categoria = categoria;
            this.cantidad = 0;
            this.totalGastado = 0.0;
        }
    }
}
