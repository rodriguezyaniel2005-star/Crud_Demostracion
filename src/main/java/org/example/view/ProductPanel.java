package org.example.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import org.bson.types.ObjectId;
import org.example.controller.CategoriaController;
import org.example.controller.ProductController;
import org.example.event.EntityChangeEvent;
import org.example.event.EntityChangeListener;
import org.example.event.EntityChangeManager;
import org.example.model.Categoria;
import org.example.model.Disco;

public class ProductPanel extends JPanel implements EntityChangeListener {
    private ProductController productController;
    private CategoriaController categoriaController;
    private EntityChangeManager eventManager; // Event manager to register for changes
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JTextField nombreField, descripcionField, precioField, stockField, marcaField;
    private JComboBox<Categoria> categoriaCombo;
    private JButton createBtn, updateBtn, deleteBtn, refreshBtn;
    
    public ProductPanel() {
        productController = new ProductController();
        categoriaController = new CategoriaController();
        eventManager = EntityChangeManager.getInstance();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Top panel for inputs
        JPanel inputPanel = createInputPanel();
        add(inputPanel, BorderLayout.NORTH);
        
        // Center panel for table
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);
        
        refreshTable();
        
        // Register listeners
        eventManager.addListener(EntityChangeEvent.ENTITY_CATEGORIA, this);
        eventManager.addListener(EntityChangeEvent.ENTITY_DISCO, this);
        eventManager.addListener(EntityChangeEvent.ENTITY_PRODUCTO, this);
    }
    
    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 4, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Crear/Actualizar Producto"));
        
        panel.add(new JLabel("Nombre:"));
        nombreField = new JTextField();
        panel.add(nombreField);
        
        panel.add(new JLabel("Descripción:"));
        descripcionField = new JTextField();
        panel.add(descripcionField);
        
        panel.add(new JLabel("Categoría:"));
        categoriaCombo = new JComboBox<>();
        refreshCategorias();
        panel.add(categoriaCombo);
        
        panel.add(new JLabel("Precio:"));
        precioField = new JTextField();
        panel.add(precioField);
        
        panel.add(new JLabel("Stock:"));
        stockField = new JTextField();
        panel.add(stockField);
        
        panel.add(new JLabel("Marca:"));
        marcaField = new JTextField();
        panel.add(marcaField);
        
        createBtn = new JButton("Crear");
        createBtn.addActionListener(e -> createProduct());
        panel.add(createBtn);
        
        updateBtn = new JButton("Actualizar");
        updateBtn.addActionListener(e -> updateProduct());
        panel.add(updateBtn);
        
        deleteBtn = new JButton("Eliminar");
        deleteBtn.addActionListener(e -> deleteProduct());
        panel.add(deleteBtn);
        
        refreshBtn = new JButton("Refrescar");
        refreshBtn.addActionListener(e -> refreshTable());
        panel.add(refreshBtn);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(
            new String[]{"ID", "Nombre", "Descripción", "Precio", "Stock", "Marca", "Categoría"}, 0);
        productTable = new JTable(tableModel);
        productTable.getSelectionModel().addListSelectionListener(e -> loadProductData());
        JScrollPane scrollPane = new JScrollPane(productTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
    
    private void createProduct() {
        try {
            String nombre = nombreField.getText();
            String descripcion = descripcionField.getText();
            double precio = Double.parseDouble(precioField.getText());
            int stock = Integer.parseInt(stockField.getText());
            String marca = marcaField.getText();
            Categoria categoria = (Categoria) categoriaCombo.getSelectedItem();
            
            if (categoria != null) {
                boolean success = productController.crearDisco(nombre, descripcion, categoria.getId(), precio, stock, marca);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Producto creado exitosamente");
                    clearFields();
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al crear el producto", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una categoría", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese números válidos en Precio y Stock", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateProduct() {
        int row = productTable.getSelectedRow();
        if (row >= 0) {
            try {
                String id = (String) tableModel.getValueAt(row, 0);
                String nombre = nombreField.getText();
                String descripcion = descripcionField.getText();
                double precio = Double.parseDouble(precioField.getText());
                int stock = Integer.parseInt(stockField.getText());
                String marca = marcaField.getText();
                Categoria categoria = (Categoria) categoriaCombo.getSelectedItem();
                
                if (categoria != null) {
                    boolean success = productController.actualizarDisco(new ObjectId(id), nombre, descripcion, 
                        categoria.getId(), precio, stock, marca);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "Producto actualizado exitosamente");
                        clearFields();
                        refreshTable();
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingrese datos válidos", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un producto", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteProduct() {
        int row = productTable.getSelectedRow();
        if (row >= 0) {
            String id = (String) tableModel.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "¿Desea eliminar este producto?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                productController.eliminarDisco(new ObjectId(id));
                JOptionPane.showMessageDialog(this, "Producto eliminado");
                refreshTable();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un producto", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Disco> discos = productController.obtenerTodosDiscos();
        if (discos != null) {
            for (Disco disco : discos) {
                tableModel.addRow(new Object[]{
                    disco.getId().toString(),
                    disco.getNombre(),
                    disco.getDescripcion(),
                    disco.getPrecio(),
                    disco.getStock(),
                    disco.getMarca(),
                    disco.getCategoriaNombre(),
                });
            }
        }
    }
    
    private void refreshCategorias() {
        categoriaCombo.removeAllItems();
        List<Categoria> categorias = categoriaController.obtenerTodasCategorias();
        if (categorias != null) {
            for (Categoria cat : categorias) {
                categoriaCombo.addItem(cat);
            }
        }
    }
    
    private void loadProductData() {
        int row = productTable.getSelectedRow();
        if (row >= 0) {
            String id = (String) tableModel.getValueAt(row, 0);
            Disco disco = productController.obtenerDisco(new ObjectId(id));
            if (disco != null) {
                nombreField.setText(disco.getNombre());
                descripcionField.setText(disco.getDescripcion());
                precioField.setText(String.valueOf(disco.getPrecio()));
                stockField.setText(String.valueOf(disco.getStock()));
                marcaField.setText(disco.getMarca());
                // Set categoria combo based on categoriaId
                for (int i = 0; i < categoriaCombo.getItemCount(); i++) {
                    Categoria cat = categoriaCombo.getItemAt(i);
                    if (cat.getId().equals(disco.getCategoriaId())) {
                        categoriaCombo.setSelectedIndex(i);
                        break;
                    }
                }
            }
        }
    }
    
    private void clearFields() {
        nombreField.setText("");
        descripcionField.setText("");
        precioField.setText("");
        stockField.setText("");
        marcaField.setText("");
    }
    
    @Override
    public void onEntityChanged(EntityChangeEvent event) {
        // Auto-refresh categoria combo when categories change
        if (EntityChangeEvent.ENTITY_CATEGORIA.equals(event.getEntityType())) {
            SwingUtilities.invokeLater(this::refreshCategorias);
        }
        // Auto-refresh table when products change
        else if (EntityChangeEvent.ENTITY_DISCO.equals(event.getEntityType()) || 
                 EntityChangeEvent.ENTITY_PRODUCTO.equals(event.getEntityType())) {
            SwingUtilities.invokeLater(this::refreshTable);
        }
    }
}
