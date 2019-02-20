package com.teemor;



import com.teemor.DAO.TableDao;
import com.teemor.DAO.TableDaoImpl;
import com.teemor.generator.SQLConfig;
import com.teemor.generator.GenUtils;
import com.teemor.model.ColumnEntity;
import com.teemor.model.TableEntity;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: lujing
 * @description: 面板的启动页面
 * @create: 2018/9/6 下午2:05
 */

public class GeneratorStarter extends JFrame {
    
    private static final long serialVersionUID = 1L;
    private JTextArea ipFiled;
    private JTextField dbFiled;
    private JTextField dbNameFiled;
    private JTextField tabField;
    private JTextField businessField;
    private JTextField authorField;
    private JCheckBox checkBox;
    private JTextField userField;
    private JTextField pwdField;
    private JComboBox dbBox;
    private JLabel labelInfo;
    private JTable jtable;
    private GeneratorStarter.MyTableModel tableModel;
    private String[] titles = {"选择", "表格名称", "注释"};
    
    private SQLConfig sqlConfig = new SQLConfig();
    
    
    private static final String ipStr = "rm-uf6rg3ju6lyk8gm4wlo.mysql.rds.aliyuncs.com";
    private static final String dataBase = "hosting_edu_dev";
    private static final String user = "root";
    private static final String pwd = "Root1234";
    private static final String businessName = "";
    
    
    public static void main(String[] args) {
        // 创建对象
        GeneratorStarter dtb = new GeneratorStarter();
        // 设置可见
        dtb.setVisible(true);
        // 点击X关闭窗口
        dtb.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 调用设置居中显示
        dtb.setSizeAndCentralizeMe(1000, 440);
    }
    
    /**
     * 初始化面板
     */
    public GeneratorStarter() {
        
        setTitle("数据库生成");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 544, 374);
        
        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(null);
        
        JLabel lblIp = new JLabel("IP:");
        lblIp.setBounds(110, 13, 30, 15);
        panel.add(lblIp);
        
        ipFiled = new JTextArea();
        ipFiled.setText(ipStr);
        ipFiled.setBounds(146, 10, 147, 50);
        panel.add(ipFiled);
        ipFiled.setColumns(10);
        ipFiled.setLineWrap(true);
        ipFiled.setWrapStyleWord(true);
        
        JLabel dbNamelabel = new JLabel("DATABASE:");
        dbNamelabel.setBounds(50, 67, 100, 20);
        panel.add(dbNamelabel);
        
        dbNameFiled = new JTextField();
        dbNameFiled.setBounds(146, 68, 147, 21);
        dbNameFiled.setText(dataBase);
        panel.add(dbNameFiled);
        dbNameFiled.setColumns(10);
        
        JLabel userLabel = new JLabel("USER:");
        userLabel.setBounds(80, 98, 54, 15);
        panel.add(userLabel);
        
        userField = new JTextField();
        userField.setText(user);
        userField.setBounds(145, 97, 148, 21);
        panel.add(userField);
        userField.setColumns(10);
        
        JLabel pwdLabel = new JLabel("PWD:");
        pwdLabel.setBounds(95, 129, 54, 15);
        panel.add(pwdLabel);
        
        pwdField = new JTextField();
        pwdField.setText(pwd);
        pwdField.setBounds(145, 126, 147, 21);
        panel.add(pwdField);
        pwdField.setColumns(10);
        
        JLabel packLabel = new JLabel("*BUSINESS包名:");
        packLabel.setBounds(50, 160, 100, 15);
        panel.add(packLabel);
        
        businessField = new JTextField();
        businessField.setText(businessName);
        businessField.setBounds(146, 155, 147, 21);
        panel.add(businessField);
        businessField.setColumns(10);
        
        
        JLabel catlogLabel2 = new JLabel("AUTHOR：");
        catlogLabel2.setBounds(57, 193, 95, 21);
        panel.add(catlogLabel2);
        
        authorField = new JTextField();
        authorField.setText("");
        authorField.setBounds(146, 193, 147, 21);
        panel.add(authorField);
        authorField.setColumns(10);
        
        
        JLabel mustIPLabel = new JLabel("*ADDRESS(3306)");
        mustIPLabel.setForeground(Color.RED);
        mustIPLabel.setBounds(303, 13, 176, 15);
        panel.add(mustIPLabel);
        
        
        /*按钮和动作*/
        
        JButton button = new JButton("TABLE LIST");
        // 按钮增加动作执行
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                initSQLConfig();
                queryList();
            }
        });
        
        
        button.setBounds(145, 267, 93, 23);
        panel.add(button);
        
        JButton crButton = new JButton("COME ON BABY!");
        // 按钮增加动作执行create()方法
        crButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                List<String> selectedTables = getSelectedTables();
                
                generateCode(selectedTables);
            }
        });
        crButton.setBounds(280, 267, 130, 23);
        panel.add(crButton);
        
        
        // 增加关闭事件监听，关闭相关操作
        this.addWindowListener(new WindowAdapter() {
            
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                close();
                System.exit(0);
            }
            
        });
        //设置表格
        
        Object[][] tableData = {};
        tableModel = new GeneratorStarter.MyTableModel(tableData, titles);
        jtable = new JTable(this.tableModel);
        JScrollPane scr = new JScrollPane(this.jtable);
        scr.setBounds(430, 10, 500, 290);
        panel.add(scr);
        //添加标格监听事件
        jtable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = jtable.getSelectedRow();
                if (jtable.getSelectedColumn() == 0)//如果是第一列的单元格，则返回，不响应点击
                    return;
                //列响应操作
            }
        });
        
        //显示操作信息label
        labelInfo = new JLabel("");
        labelInfo.setForeground(Color.RED);
        labelInfo.setBounds(20, 317, 600, 60);
        panel.add(labelInfo);
        
        
    }
    
    /*---------------------------private method ----------------------------- */
    
    /**
     * 获取选中的列表
     *
     * @return 数据库表名
     */
    private List<String> getSelectedTables() {
        
        List<String> tableNameList = new ArrayList<>();
        
        int rowCount = this.jtable.getRowCount();
        
        for (int i = 0; i < rowCount; i++) {
            if (this.jtable.getValueAt(i, 0).toString().equals("true")) {
                String tabName = this.jtable.getValueAt(i, 1).toString();
                tableNameList.add(tabName);
            }
        }
        return tableNameList;
    }
    
    /**
     * 代码生成
     *
     * @param selectedTables
     */
    private void generateCode(List<String> selectedTables) {
        String businessName = this.getBusinessField().getText();
        if (StringUtils.isBlank(businessName)) {
            this.getLabelInfo().setText("必须输入业务模块名!");
            return;
        }
        if (selectedTables == null || selectedTables.size() == 0) {
            this.getLabelInfo().setText("请选择同一模块的表!");
            return;
        }
        String authorName = this.getAuthorField().getText();
        TableDao tableDao = new TableDaoImpl(sqlConfig);
        this.getLabelInfo().setText("生产中……");
        for (String tableName : selectedTables) {
            TableEntity tableEntity = tableDao.queryTable(tableName);
            List<ColumnEntity> columnEntityList = tableDao.queryColumns(tableName);
            GenUtils.generatorCode(tableEntity, columnEntityList, businessName, authorName);
        }
        this.getLabelInfo().setText("代码生成成功 :" + selectedTables.toString());
    }
    
  
    
    /**
     * 初始化数据库参数
     */
    private void initSQLConfig() {
        
        String catName = this.getAuthorField().getText();
        sqlConfig.setIp(this.ipFiled.getText());
        sqlConfig.setDataBase(this.getDbNameFiled().getText());
        sqlConfig.setUser(this.getUserField().getText());
        sqlConfig.setPwd(this.pwdField.getText());
        sqlConfig.setBusinessName(this.getBusinessField().getText());
    }
    
    
    /**
     * 查询数据库 表格 列表
     */
    private void queryList() {
        initSQLConfig();
        TableDao tableDao = new TableDaoImpl(sqlConfig);
        List<TableEntity> tableEntityList = tableDao.queryList();
        setTableModel(tableEntityList);
        
    }
    
    
    /**
     * 渲染数据表格
     */
    private void setTableModel(List<TableEntity> tableList) {
        
        if (tableList == null || tableList.size() == 0) {
            int rowCount = this.getTableModel().getRowCount();
            int delInd = 0;
            while (delInd < rowCount) {
                this.getTableModel().removeRow(0);
                delInd++;
            }
            this.getLabelInfo().setText("数据库连接异常");
        } else {
            this.getLabelInfo().setText("连接成功");
            int rowCount = this.getTableModel().getRowCount();
            int delInd = 0;
            while (delInd < rowCount) {
                this.getTableModel().removeRow(0);
                delInd++;
            }
            for (TableEntity table : tableList) {
                Object[] rowData = {table.isChecked(), table.getTableName(), table.getComments()};
                this.getTableModel().addRow(rowData);
            }
            
            
        }
        
        
    }
    
    
    /**
     * 设置居中
     */
    private void setSizeAndCentralizeMe(int width, int height) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(width, height);
        this.setLocation(screenSize.width / 2 - width / 2, screenSize.height
                / 2 - height / 2);
    }
    
    /**
     * 关闭事件
     */
    private void close() {
        System.out.println("关闭事件");
    }
    
    
    public JTextArea getIpFiled() {
        return ipFiled;
    }
    
    public void setIpFiled(JTextArea ipFiled) {
        this.ipFiled = ipFiled;
    }
    
    public JTextField getDbFiled() {
        return dbFiled;
    }
    
    public void setDbFiled(JTextField dbFiled) {
        this.dbFiled = dbFiled;
    }
    
    public JTextField getDbNameFiled() {
        return dbNameFiled;
    }
    
    public void setDbNameFiled(JTextField dbNameFiled) {
        this.dbNameFiled = dbNameFiled;
    }
    
    public JTextField getTabField() {
        return tabField;
    }
    
    public void setTabField(JTextField tabField) {
        this.tabField = tabField;
    }
    
    public JTextField getBusinessField() {
        return businessField;
    }
    
    public void setBusinessField(JTextField businessField) {
        this.businessField = businessField;
    }
    
    public JTextField getAuthorField() {
        return authorField;
    }
    
    public void setAuthorField(JTextField authorField) {
        this.authorField = authorField;
    }
    
    public JCheckBox getCheckBox() {
        return checkBox;
    }
    
    public void setCheckBox(JCheckBox checkBox) {
        this.checkBox = checkBox;
    }
    
    public JTextField getUserField() {
        return userField;
    }
    
    public void setUserField(JTextField userField) {
        this.userField = userField;
    }
    
    public JTextField getPwdField() {
        return pwdField;
    }
    
    public void setPwdField(JTextField pwdField) {
        this.pwdField = pwdField;
    }
    
    public JComboBox getDbBox() {
        return dbBox;
    }
    
    public void setDbBox(JComboBox dbBox) {
        this.dbBox = dbBox;
    }
    
    public JLabel getLabelInfo() {
        return labelInfo;
    }
    
    public void setLabelInfo(JLabel labelInfo) {
        this.labelInfo = labelInfo;
    }
    
    public JTable getJtable() {
        return jtable;
    }
    
    public void setJtable(JTable jtable) {
        this.jtable = jtable;
    }
    
    public String[] getTitles() {
        return titles;
    }
    
    public void setTitles(String[] titles) {
        this.titles = titles;
    }
    
    public static String getIpStr() {
        return ipStr;
    }
    
    
    public static String getUser() {
        return user;
    }
    
    public static String getPwd() {
        return pwd;
    }
    
    public static String getBusinessName() {
        return businessName;
    }
    
    public MyTableModel getTableModel() {
        return tableModel;
    }
    
    public void setTableModel(MyTableModel tableModel) {
        this.tableModel = tableModel;
    }
    
    /**
     * 内部类
     */
    class MyTableModel extends DefaultTableModel {
        public MyTableModel(Object[][] data, String[] columns) {
            super(data, columns);
        }
        
        public boolean isCellEditable(int row, int column) { //设置Table单元格是否可编辑
            if (column == 0) return true;
            return false;
        }
        
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0) {
                return Boolean.class;
            }
            return Object.class;
        }
        
    }
}
