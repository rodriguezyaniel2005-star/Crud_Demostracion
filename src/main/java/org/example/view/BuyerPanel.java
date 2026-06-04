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

import org.example.controller.BuyerController;
import org.example.event.EntityChangeEvent;
import org.example.event.EntityChangeListener;
import org.example.event.EntityChangeManager;
import org.example.model.Buyer;

public class BuyerPanel extends JPanel implements EntityChangeListener {
    private BuyerController buyerController;
    private EntityChangeManager eventManager; // Event manager to register for changes
    private JTable buyerTable;
    private DefaultTableModel tableModel;
    private JTextField nombreField, apellidosField, emailField, telefonoField, direccionField;
    private JButton createBtn, updateBtn, deleteBtn, refreshBtn;
    
    public BuyerPanel() {
        buyerController = new BuyerController();
        eventManager = EntityChangeManager.getInstance();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel inputPanel = createInputPanel();
        add(inputPanel, BorderLayout.NORTH);
        
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);
        
        refreshTable();
        
        // Register listener for Buyer changes to keep table synchronized
        eventManager.addListener(EntityChangeEvent.ENTITY_BUYER, this);
    }
    
    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 7, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Crear/Actualizar Comprador"));
        
        panel.add(new JLabel("Nombre:"));
        nombreField = new JTextField();
        panel.add(nombreField);
        
        panel.add(new JLabel("Apellidos:"));
        apellidosField = new JTextField();
        panel.add(apellidosField);
        
        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);
        
        panel.add(new JLabel("Teléfono:"));
        telefonoField = new JTextField();
        panel.add(telefonoField);
        
        panel.add(new JLabel("Dirección:"));
        direccionField = new JTextField();
        panel.add(direccionField);
        
        createBtn = new JButton("Crear");
        createBtn.addActionListener(e -> createBuyer());
        panel.add(createBtn);
        
        updateBtn = new JButton("Actualizar");
        updateBtn.addActionListener(e -> updateBuyer());
        panel.add(updateBtn);
        
        deleteBtn = new JButton("Eliminar");
        deleteBtn.addActionListener(e -> deleteBuyer());
        panel.add(deleteBtn);
        
        refreshBtn = new JButton("Refrescar");
        refreshBtn.addActionListener(e -> refreshTable());
        panel.add(refreshBtn);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(
            new String[]{"ID", "Nombre", "Apellidos", "Email", "Teléfono", "Dirección"}, 0);
        buyerTable = new JTable(tableModel);
        buyerTable.getSelectionModel().addListSelectionListener(e -> loadBuyerData());
        JScrollPane scrollPane = new JScrollPane(buyerTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
    
    private void createBuyer() {
        String nombre = nombreField.getText();
        String apellidos = apellidosField.getText();
        String email = emailField.getText();
        String telefono = telefonoField.getText();
        String direccion = direccionField.getText();
        
        if (!nombre.isEmpty() && !apellidos.isEmpty() && !email.isEmpty() && !telefono.isEmpty() && !direccion.isEmpty()) {
            boolean success = buyerController.crearComprador(nombre, apellidos, email, telefono, direccion);
            if (success) {
                JOptionPane.showMessageDialog(this, "Comprador creado exitosamente");
                clearFields();
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Error al crear el comprador", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Ingrese todos los datos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateBuyer() {
        int row = buyerTable.getSelectedRow();
        if (row >= 0) {
            String id = (String) tableModel.getValueAt(row, 0);
            String nombre = nombreField.getText();
            String apellidos = apellidosField.getText();
            String email = emailField.getText();
            String telefono = telefonoField.getText();
            String direccion = direccionField.getText();
            
            boolean success = buyerController.actualizarComprador(new org.bson.types.ObjectId(id), nombre, apellidos, email, telefono, direccion);
            if (success) {
                JOptionPane.showMessageDialog(this, "Comprador actualizado exitosamente");
                clearFields();
                refreshTable();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un comprador", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteBuyer() {
        int row = buyerTable.getSelectedRow();
        if (row >= 0) {
            String id = (String) tableModel.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "¿Desea eliminar este comprador?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                buyerController.eliminarComprador(new org.bson.types.ObjectId(id));
                JOptionPane.showMessageDialog(this, "Comprador eliminado");
                refreshTable();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un comprador", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Buyer> buyers = buyerController.obtenerTodosCompradores();
        if (buyers != null) {
            for (Buyer buyer : buyers) {
                tableModel.addRow(new Object[]{
                    buyer.getId().toString(),
                    buyer.getNombre(),
                    buyer.getApellidos(),
                    buyer.getEmail(),
                    buyer.getTelefono(),
                    buyer.getDireccion()
                });
            }
        }
    }
    
    private void loadBuyerData() {
        int row = buyerTable.getSelectedRow();
        if (row >= 0) {
            String id = (String) tableModel.getValueAt(row, 0);
            Buyer buyer = buyerController.obtenerComprador(new org.bson.types.ObjectId(id));
            if (buyer != null) {
                nombreField.setText(buyer.getNombre());
                apellidosField.setText(buyer.getApellidos());
                emailField.setText(buyer.getEmail());
                telefonoField.setText(buyer.getTelefono());
                direccionField.setText(buyer.getDireccion());
            }
        }
    }
    
    private void clearFields() {
        nombreField.setText("");
        apellidosField.setText("");
        emailField.setText("");
        telefonoField.setText("");
        direccionField.setText("");
    }
    
    @Override
    public void onEntityChanged(EntityChangeEvent event) {
        // Auto-refresh table when buyers change
        if (EntityChangeEvent.ENTITY_BUYER.equals(event.getEntityType())) {
            SwingUtilities.invokeLater(this::refreshTable);
        }
    }
}
