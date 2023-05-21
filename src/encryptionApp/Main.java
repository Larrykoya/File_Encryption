package encryptionApp;


import java.awt.Color;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class Main extends JFrame implements ActionListener {
	
	//creating jframe components
    private JButton uploadButton,decryptButton;
    private JTextArea textArea;
    private JPanel p1;
    private JLabel label1,label2;
    private JScrollPane scroll;
    
  
    private String secretKey = "1qd4m6j8A12T45Zz";
    //constructor
    public Main() {
    	//setting jframe attributes
        setTitle("File Encrypter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 400);
        
        //initializing jframe components
        p1 = new JPanel();
        label1 = new JLabel("Select file to encrypt:   ");
        label2 = new JLabel("Decrypt encrypted file: ");
        uploadButton = new JButton("Encrypt File");
        decryptButton = new JButton("Decrypt File");
        textArea = new JTextArea(15,20);;  
        scroll = new JScrollPane(textArea);  
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        //setting attributs of the jframe components
        uploadButton.setFocusable(false);
        uploadButton.addActionListener(this);
        decryptButton.setFocusable(false);
        decryptButton.addActionListener(this);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        p1.setBackground(Color.decode("#A0C1D1"));
        uploadButton.setBackground(Color.decode("#232C33"));
        uploadButton.setForeground(Color.decode("#FFFFFF"));
        decryptButton.setBackground(Color.decode("#232C33"));
        decryptButton.setForeground(Color.decode("#FFFFFF"));
        
        //adding jpanel to jframe
        this.add(p1);
        
        //adding other components to jpanel
        p1.add(label1);
        p1.add(uploadButton);
        p1.add(label2);
        p1.add(decryptButton);
        p1.add(scroll);
        
        //setting jframe visible
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e) {
    	//on click of encrypt button
        if (e.getSource() == uploadButton) {
        	//create file chooser component
            JFileChooser fileChooser = new JFileChooser();
            //prompt user to select file to encrypt
            fileChooser.showOpenDialog(this);
            //get selected file
                File selectedFile = fileChooser.getSelectedFile();
                try {
                	//read content of the selected file
                    BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
                    String originalFileName = selectedFile.getName();
                    String line;
                    StringBuilder sb = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        sb.append("\n" + line);
                    }
                    reader.close();
                    //encrypt the data from the selected file and save in a variable cypherText
                    String cypherText = encrypt(sb.toString(), secretKey);
                    System.out.println(sb.toString());
                    //display cypher text in the text area
                    textArea.setText(cypherText);
        			long temp=System.currentTimeMillis()/1000;
        			String suffix = String.valueOf(temp);
        			//write the cypher text into a new file
                    FileWriter writer = new FileWriter("Encrypted_"+originalFileName+"_"+suffix+".txt");
        			writer.write(cypherText);
        			writer.close();
                } catch (IOException err) {
                    JOptionPane.showMessageDialog(this, "Error reading file: " + err.getMessage());
                }
            }
      //on click of decrypt button
        if (e.getSource() == decryptButton) {
        	//create file chooser component
            JFileChooser fileChooser = new JFileChooser();
          //prompt user to select file to decrypt
             fileChooser.showOpenDialog(this);
           //get selected file
                File selectedFile = fileChooser.getSelectedFile();
                try {
                	//read cypher text from selected file
                    BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
                    String line;
                    StringBuilder sb = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    reader.close();
                    //decrypt cypher text and save in a variable originalText
                    String originalText = decrypt(sb.toString(), secretKey);
                  //display original text in the text area
                    textArea.setText(originalText);
                } catch (IOException err) {
                    JOptionPane.showMessageDialog(this, "Error reading file: " + err.getMessage());
                }
            }
        }
    public static void main(String[] args) {
    	//creating an instance of the Main class
        new Main();
    }
    
    //Encryption method
    private String encrypt(String plainText, String secretKey) {
    	 try {
    		 //use the secret key to get a secret key spec
    		 SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");
    		 //create an instance of the AES algorithm cipher
             Cipher cipher = Cipher.getInstance("AES");
             //use the key spec to initialize the cipher for encryption
             cipher.init(Cipher.ENCRYPT_MODE, keySpec);
             //Encrypt the plain text with the cipher to generate an array of encrypted bytes
             byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
             //encode array of encrypted bytes to base64 string(cipher-text) and return
             return Base64.getEncoder().encodeToString(encryptedBytes);
    	 }catch(Exception e) {
    		 return  e.getMessage();
    	 }
    	 
    }
    //Decryption method
    private String decrypt(String encryptedText, String secretKey) {
        try {
   		 	//use the secret key to get a secret key spec
        	SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");
   		 	//create an instance of the AES algorithm cipher
        	Cipher cipher = Cipher.getInstance("AES");
            //use the key spec to initialize the cipher for decryption
        	cipher.init(Cipher.DECRYPT_MODE, keySpec);
            //decode base64 string(cipher-text) into array of encrypted bytes 
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
            //decode array of encrypted bytes to array of decrypted bytes with the initialized cipher
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            //convert the array of decrypted bytes to a UTF_8 standard string and return
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        }catch(Exception e) {
        	return e.getMessage();
        }
        }
    
}



    


