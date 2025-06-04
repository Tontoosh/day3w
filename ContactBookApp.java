import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class ContactBookApp extends JFrame {
    // Контакт классын тодорхойлолт
    private static class Contact {
        private String name;
        private String phone;
        private String desc;

        public Contact(String name, String phone, String desc) {
            this.name = name.trim();
            this.phone = phone.trim();
            this.desc = desc.trim();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name.trim();
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone.trim();
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc.trim();
        }
    }

    private ArrayList<Contact> contacts = new ArrayList<>();

    private JLabel lblName;
    private JTextField fieldName;
    private JLabel lblPhone;
    private JTextField fieldPhone;
    private JLabel lblDesc;
    private JTextField fieldDesc;

    private JButton btnAdd;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnView;

    private JTable table;
    private DefaultTableModel tableModel;

    public ContactBookApp() {
        super("Санал хүсэлт");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        // Оруулах талбарууд
        JPanel panelInputs = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        lblName = new JLabel("Нэр:");
        fieldName = new JTextField(20);
        lblPhone = new JLabel("Утас:");
        fieldPhone = new JTextField(20);
        lblDesc = new JLabel("Санал:");
        fieldDesc = new JTextField(20);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelInputs.add(lblName, gbc);
        gbc.gridx = 1;
        panelInputs.add(fieldName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelInputs.add(lblPhone, gbc);
        gbc.gridx = 1;
        panelInputs.add(fieldPhone, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panelInputs.add(lblDesc, gbc);
        gbc.gridx = 1;
        panelInputs.add(fieldDesc, gbc);

        // Товчлуурууд
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnAdd    = new JButton("Нэмэх");
        btnEdit   = new JButton("Засах");
        btnDelete = new JButton("Устгах");
        btnView   = new JButton("Харах");
        panelButtons.add(btnAdd);
        panelButtons.add(btnEdit);
        panelButtons.add(btnDelete);
        panelButtons.add(btnView);

        // Хүснэгт
        String[] columnNames = {"Нэр", "Утас", "Санал"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);

        // Layout
        setLayout(new BorderLayout());
        add(panelInputs, BorderLayout.NORTH);    // Дээд талд талбарууд
        add(scrollPane, BorderLayout.CENTER);    // Голд хүснэгт
        add(panelButtons, BorderLayout.SOUTH);   // Доор товчлуурууд

        // Үйлдлүүд
        btnAdd.addActionListener(e -> addContact());
        btnEdit.addActionListener(e -> editContact());
        btnDelete.addActionListener(e -> deleteContact());
        btnView.addActionListener(e -> viewSelected());
    }

    private void addContact() {
        String name = fieldName.getText().trim();
        String phone = fieldPhone.getText().trim();
        String desc = fieldDesc.getText().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Нэр болон утасны дугаар шаардлагатай!",
                "Анхаар",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Contact newContact = new Contact(name, phone, desc);
        contacts.add(newContact);
        updateTable();
        clearFields();
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        for (Contact c : contacts) {
            Object[] rowData = { c.getName(), c.getPhone(), c.getDesc() };
            tableModel.addRow(rowData);
        }
    }

    private void clearFields() {
        fieldName.setText("");
        fieldPhone.setText("");
        fieldDesc.setText("");
    }

    private void deleteContact() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) return;

        String name = contacts.get(selectedRow).getName();
        contacts.remove(selectedRow);
        updateTable();
        JOptionPane.showMessageDialog(
            this,
            name + " устгагдлаа.",
            "Амжилттай",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void editContact() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) return;

        String newName = fieldName.getText().trim();
        String newPhone = fieldPhone.getText().trim();
        String newDesc = fieldDesc.getText().trim();

        if (newName.isEmpty() || newPhone.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Шинэ нэр болон дугаар шаардлагатай!",
                "Анхаар",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Contact contact = contacts.get(selectedRow);
        contact.setName(newName);
        contact.setPhone(newPhone);
        contact.setDesc(newDesc);

        updateTable();
        clearFields();
    }

    private void viewSelected() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) return;

        Contact contact = contacts.get(selectedRow);
        fieldName.setText(contact.getName());
        fieldPhone.setText(contact.getPhone());
        fieldDesc.setText(contact.getDesc());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ContactBookApp app = new ContactBookApp();
            app.setVisible(true);
        });
    }
}
