package org.example.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import org.example.controller.CategoriaController;
import org.example.event.EntityChangeEvent;
import org.example.event.EntityChangeListener;
import org.example.event.EntityChangeManager;
import org.example.model.Categoria;

public class CategoriaPanel extends JPanel implements EntityChangeListener {
    private CategoriaController categoriaController;
    private EntityChangeManager eventManager; // Event manager to register for changes
    private JTable categoriaTable;
    private DefaultTableModel tableModel;
    private JTextField nombreField, descripcionField;
    private JButton createBtn, updateBtn, deleteBtn, refreshBtn;
    
    public CategoriaPanel() {
        categoriaController = new CategoriaController();
        eventManager = EntityChangeManager.getInstance();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel inputPanel = createInputPanel();
        add(inputPanel, BorderLayout.NORTH);
        
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);
        
        refreshTable();
        
        // Register listener for Categoria changes to keep table synchronized
        eventManager.addListener(EntityChangeEvent.ENTITY_CATEGORIA, this);
    }
    
    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 3, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Crear/Actualizar Categoría"));
        
        panel.add(new JLabel("Nombre:"));
        nombreField = new JTextField();
        panel.add(nombreField);
        
        panel.add(new JLabel("Descripción:"));
        descripcionField = new JTextField();
        panel.add(descripcionField);
        
        createBtn = new JButton("Crear");
        createBtn.addActionListener(e -> createCategoria());
        panel.add(createBtn);
        
        updateBtn = new JButton("Actualizar");
        updateBtn.addActionListener(e -> updateCategoria());
        panel.add(updateBtn);
        
        deleteBtn = new JButton("Eliminar");
        deleteBtn.addActionListener(e -> deleteCategoria());
        panel.add(deleteBtn);
        
        refreshBtn = new JButton("Refrescar");
        refreshBtn.addActionListener(e -> refreshTable());
        panel.add(refreshBtn);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(
            new String[]{"ID", "Nombre", "Descripción"}, 0);
        categoriaTable = new JTable(tableModel);
        categoriaTable.getSelectionModel().addListSelectionListener(e -> loadCategoriaData());
        JScrollPane scrollPane = new JScrollPane(categoriaTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
    
    private void createCategoria() {
        String nombre = nombreField.getText();
        String descripcion = descripcionField.getText();
        
        if (!nombre.isEmpty() && !descripcion.isEmpty()) {
            boolean success = categoriaController.crearCategoria(nombre, descripcion);
            if (success) {
                JOptionPane.showMessageDialog(this, "Categoría creada exitosamente");
                clearFields();
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Error al crear la categoría", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Ingrese todos los datos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateCategoria() {
        int row = categoriaTable.getSelectedRow();
        if (row >= 0) {
            String id = (String) tableModel.getValueAt(row, 0);
            String nombre = nombreField.getText();
            String descripcion = descripcionField.getText();
            
            boolean success = categoriaController.actualizarCategoria(new org.bson.types.ObjectId(id), nombre, descripcion);
            if (success) {
                JOptionPane.showMessageDialog(this, "Categoría actualizada exitosamente");
                clearFields();
                refreshTable();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una categoría", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteCategoria() {
        int row = categoriaTable.getSelectedRow();
        if (row >= 0) {
            String id = (String) tableModel.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "¿Desea eliminar esta categoría?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                categoriaController.eliminarCategoria(new org.bson.types.ObjectId(id));
                JOptionPane.showMessageDialog(this, "Categoría eliminada");
                refreshTable();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una categoría", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Categoria> categorias = categoriaController.obtenerTodasCategorias();
        if (categorias != null) {
            for (Categoria cat : categorias) {
                tableModel.addRow(new Object[]{
                    cat.getId().toString(),
                    cat.getNombre(),
                    cat.getDescripcion()
                });
            }
        }
    }
    
    private void loadCategoriaData() {
        int row = categoriaTable.getSelectedRow();
        if (row >= 0) {
            String id = (String) tableModel.getValueAt(row, 0);
            Categoria categoria = categoriaController.obtenerCategoria(new org.bson.types.ObjectId(id));
            if (categoria != null) {
                nombreField.setText(categoria.getNombre());
                descripcionField.setText(categoria.getDescripcion());
            }
        }
    }
    
    private void clearFields() {
        nombreField.setText("");
        descripcionField.setText("");
    }
    
    @Override
    public void onEntityChanged(EntityChangeEvent event) {
        // Auto-refresh table when categories change
        if (EntityChangeEvent.ENTITY_CATEGORIA.equals(event.getEntityType())) {
            SwingUtilities.invokeLater(this::refreshTable);
        }
    }
}
