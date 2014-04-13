package scripts.BDK.Main;

public class BDKGUI extends javax.swing.JFrame {

	    /**
	     * Creates new form BDKGUI
	     */
	    public BDKGUI() {
	        initComponents();
	    }

	    /**
	     * This method is called from within the constructor to initialize the form.
	     * WARNING: Do NOT modify this code. The content of this method is always
	     * regenerated by the Form Editor.
	     */
	    @SuppressWarnings("unchecked")
	    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
	    private void initComponents() {

	        jPanel1 = new javax.swing.JPanel();
	        jLabel1 = new javax.swing.JLabel();
	        jLabel2 = new javax.swing.JLabel();
	        foodamount = new javax.swing.JTextField();
	        jLabel3 = new javax.swing.JLabel();
	        foodusing = new javax.swing.JComboBox();
	        jButton1 = new javax.swing.JButton();
	        jLabel4 = new javax.swing.JLabel();
	        telemethod = new javax.swing.JComboBox();

	        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

	        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
	        jLabel1.setText("Yaw hide's BDK");

	        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
	        jLabel2.setText("Food Amount");

	        foodamount.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                foodamountActionPerformed(evt);
	            }
	        });

	        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
	        jLabel3.setText("Food Using");

	        foodusing.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "lobster", "salmon", "trout", "shark" }));
	        foodusing.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                foodusingActionPerformed(evt);
	            }
	        });

	        jButton1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
	        jButton1.setText("Start");
	        jButton1.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                jButton1ActionPerformed(evt);
	            }
	        });

	        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
	        jLabel4.setText("Tele method");

	        telemethod.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "falador" }));
	        telemethod.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                telemethodActionPerformed(evt);
	            }
	        });

	        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
	        jPanel1.setLayout(jPanel1Layout);
	        jPanel1Layout.setHorizontalGroup(
	            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(jPanel1Layout.createSequentialGroup()
	                .addGap(105, 105, 105)
	                .addComponent(jButton1)
	                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
	            .addGroup(jPanel1Layout.createSequentialGroup()
	                .addContainerGap(40, Short.MAX_VALUE)
	                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                    .addComponent(jLabel1)
	                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
	                        .addGroup(jPanel1Layout.createSequentialGroup()
	                            .addComponent(jLabel4)
	                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                            .addComponent(telemethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
	                        .addGroup(jPanel1Layout.createSequentialGroup()
	                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                                .addComponent(jLabel2)
	                                .addComponent(jLabel3))
	                            .addGap(63, 63, 63)
	                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                                .addComponent(foodamount, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
	                                .addComponent(foodusing, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
	                .addContainerGap(52, Short.MAX_VALUE))
	        );
	        jPanel1Layout.setVerticalGroup(
	            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(jPanel1Layout.createSequentialGroup()
	                .addContainerGap()
	                .addComponent(jLabel1)
	                .addGap(33, 33, 33)
	                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(jLabel2)
	                    .addComponent(foodamount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
	                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(jLabel3)
	                    .addComponent(foodusing, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
	                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(jLabel4)
	                    .addComponent(telemethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
	                .addComponent(jButton1))
	        );

	        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
	        getContentPane().setLayout(layout);
	        layout.setHorizontalGroup(
	            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
	                .addContainerGap()
	                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                .addContainerGap())
	        );
	        layout.setVerticalGroup(
	            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(layout.createSequentialGroup()
	                .addContainerGap()
	                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
	                .addContainerGap())
	        );

	        pack();
	    }// </editor-fold>                        

	    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
	        // TODO add your handling code here:
	    	String food = foodusing.getSelectedItem().toString();
	    	switch(food){
	    	case "lobster":
	    		BDK.FOOD_IDS = new int[] {379}; break;
	    	case "trout":
	    		BDK.FOOD_IDS = new int[] {333}; break;
	    	case "salmon":
	    		BDK.FOOD_IDS = new int[] {329}; break;
	    	case "shark":
	    		BDK.FOOD_IDS = new int[] {385}; break;
	    	}
	    	BDK.FOOD_NUM = Integer.parseInt(foodamount.getText());
	    	//useHouse = telemethod.getSelectedItem().toString().equals("house") ? true : false;
	    	BDK.WAIT_GUI = false;
	    }                                      

	    private void foodamountActionPerformed(java.awt.event.ActionEvent evt) {                                           
	        // TODO add your handling code here:
	    }                                          

	    private void foodusingActionPerformed(java.awt.event.ActionEvent evt) {                                          
	        // TODO add your handling code here:
	    }                                         

	    private void telemethodActionPerformed(java.awt.event.ActionEvent evt) {                                           
	        // TODO add your handling code here:
	    }

	    // Variables declaration - do not modify                     
	    private javax.swing.JTextField foodamount;
	    private javax.swing.JComboBox foodusing;
	    private javax.swing.JButton jButton1;
	    private javax.swing.JLabel jLabel1;
	    private javax.swing.JLabel jLabel2;
	    private javax.swing.JLabel jLabel3;
	    private javax.swing.JLabel jLabel4;
	    private javax.swing.JPanel jPanel1;
	    private javax.swing.JComboBox telemethod;
	    // End of variables declaration                   
	}