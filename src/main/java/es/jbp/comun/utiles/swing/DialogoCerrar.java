package es.jbp.comun.utiles.swing;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

/**
 * Diálogo generico que contiene un panel interior y permite ser cerrado.
 *
 * @author  Jorge Berjano
 */
public class DialogoCerrar extends JDialog {
   
    /** Panel interior que contiene el dialogo */
    private JComponent panelInterior;
    
    /** Constructor */
    public DialogoCerrar(Frame parent, String titulo) {
        super(parent, true);
        setTitle(titulo);
        initComponents();
        inicializar();
    }
    
    public DialogoCerrar(Frame parent, String titulo, JComponent panelInterior) {
        this(parent, titulo);
        setPanelInterior(panelInterior);
    }
    
    private void inicializar() {

        // Se establece el botón por defecto
        this.getRootPane().setDefaultButton(botonCerrar);
        
        // Se hace una 'Keyboard Binding' para procesar la tecla de escape
        KeyStroke ks = KeyStroke.getKeyStroke("ESCAPE");
        Action action = new AbstractAction("cancelar") {
            public void actionPerformed(ActionEvent evt) {
                cerrarDialogo();
            }
        };
        Object binding = action.getValue(action.NAME);
        JComponent comp = getRootPane();
        comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ks, binding);
        comp.getActionMap().put(binding, action);
    }
    
    public JComponent getPanelInterior() {
        return panelInterior;
    }
    
    public void setPanelInterior(JComponent panelInterior) {
        this.panelInterior = panelInterior;
        getContentPane().add(panelInterior, java.awt.BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
    }

    @Override
    public void setVisible(boolean b) {
        setLocationRelativeTo(getParent());
        super.setVisible(b);
    }
    
    /**
     * Cierra el diálogo.
     */
    protected void cerrarDialogo() {
        setVisible(false);
        dispose();
    }
   

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelBotones = new javax.swing.JPanel();
        botonCerrar = new javax.swing.JButton();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        botonCerrar.setText("Cerrar");
        botonCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCerrarActionPerformed(evt);
            }
        });
        panelBotones.add(botonCerrar);

        getContentPane().add(panelBotones, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents
        
    private void botonCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCerrarActionPerformed
        cerrarDialogo();
}//GEN-LAST:event_botonCerrarActionPerformed
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        cerrarDialogo();
    }//GEN-LAST:event_closeDialog
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonCerrar;
    private javax.swing.JPanel panelBotones;
    // End of variables declaration//GEN-END:variables
    
}
