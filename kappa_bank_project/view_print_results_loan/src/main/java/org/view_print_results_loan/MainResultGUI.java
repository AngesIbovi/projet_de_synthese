package org.view_print_results_loan;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.Logger;
 

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import model.SessionInformation;
import model.query.ClientQuery;
import model.query.GetAllSimsQuery;
import model.query.GetSimQuery;
import model.query.GetSimsQuery; 
import model.response.AuthenticationServerResponse;
import model.response.GetAccountsServerResponse;
import model.response.GetAllSimsServerResponse;
import model.response.GetAllSimsServerResponse.SimulationIdentifier;
import model.response.GetSimServerResponse;
import model.response.GetSimServerResponse.AmortizationType;
import model.response.GetSimServerResponse.Repayment;
import model.response.GetSimsServerResponse;
import model.response.GetAccountsServerResponse.Account;
import util.JsonImpl;
import util.KappaProperties;
import view.Tab;  
 
/**
 * A Main Jframe used for the displaying results.
 * @version R3 Sprint 3 - 06/05/2016
 * @Author Kappa-V 
 */
@SuppressWarnings("serial") // Is not going to be serialized
public class MainResultGUI extends Tab { 
	private JTable tblRepay;
	private JButton sendQueryButton;
	private Socket socket;
	
	
	public MainResultGUI() { 
		super("Résultat des scenarios",2);

	} 
	/**
	 * calls the launch method on a set of demo Tabs.
	 * @param args : not used
	 */
	public void setSessionInformation(final SessionInformation sessionInformation) { 
		this.socket = sessionInformation.getSocket();
		 
		final MainResultGUI thisObject = this;
	
		// Initializing tools
				try {
					KappaProperties.init();
					JsonImpl.init();
				} catch(IOException e) {
					System.out.print("Exiting Client.");
					try {
						throw e;
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
		/* Network connection */
		// Socket initialization
		Properties prop = KappaProperties.getInstance();
		//System.out.print(prop);
		Socket connection = null;
		try {
			connection = new Socket(prop.getProperty("SERVER_IP"), Integer.parseInt(prop.getProperty("SERVER_PORT")));
		} catch (NumberFormatException | IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} 

		//this.setSessionInformation(sessionInformation);
		PrintWriter out = null;
		try {
			//out = new PrintWriter(connection.getOutputStream(), true);
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		BufferedReader in = null;
		try {
			//in = new BufferedReader(new InputStreamReader(connection.getInputStream())); 
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		

		// Cleanup planning   
 
				
				 
		this.setLayout(null);
		

		/* Swing components */
		
		// JPanel dimensions and position
		final JPanel panel = new JPanel();
		panel.setBounds(0, 0, 742, 31);
		this.add(panel);
		
		JLabel lblChoixDuScenario = new JLabel("Sélectionner le scenario :");
		lblChoixDuScenario.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel.add(lblChoixDuScenario);

		//final JComboBox<SimulationIdentifier> cbScenChoice = new JComboBox<SimulationIdentifier>();
		final JComboBox<SimulationIdentifier> cbScenChoice = new JComboBox<SimulationIdentifier>();
		cbScenChoice.setToolTipText("Veuillez choisir la simulation");
		cbScenChoice.setEditable(true);
		cbScenChoice.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel.add(cbScenChoice);
		
		final JLabel lblParamtres = new JLabel("Paramètres");
		lblParamtres.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblParamtres.setBounds(10, 74, 87, 22);
		this.add(lblParamtres);
		
		final JLabel lblTypeDePrt = new JLabel("Type de prêt :");
		lblTypeDePrt.setBounds(27, 124, 87, 14);
		this.add(lblTypeDePrt);
		
		final JLabel lblTypeLoan = new JLabel("");
		lblTypeLoan.setHorizontalAlignment(SwingConstants.CENTER);
		lblTypeLoan.setBackground(SystemColor.activeCaption);
		lblTypeLoan.setBounds(118, 124, 155, 18);
		this.add(lblTypeLoan); 
		lblTypeLoan.setOpaque(true);
		
		final JLabel lblge = new JLabel("Âge :");
		lblge.setBounds(244, 96, 38, 14);
		this.add(lblge);
		
		final JLabel lblAge = new JLabel("");
		lblAge.setHorizontalAlignment(SwingConstants.CENTER);
		lblAge.setOpaque(true);
		lblAge.setBackground(SystemColor.activeCaption);
		lblAge.setBounds(276, 96, 55, 18);
		this.add(lblAge);
		
		final JLabel lblMensualits = new JLabel("Montant du prêt :");
		lblMensualits.setBounds(10, 149, 97, 14);
		this.add(lblMensualits);
		
		final JLabel lblCapital = new JLabel("");
		lblCapital.setHorizontalAlignment(SwingConstants.CENTER);
		lblCapital.setOpaque(true);
		lblCapital.setBackground(SystemColor.activeCaption);
		lblCapital.setBounds(117, 149, 55, 18);
		this.add(lblCapital);
		
		final JLabel lblhorsMisAssurance = new JLabel("(hors mis assurance)");
		lblhorsMisAssurance.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblhorsMisAssurance.setBounds(179, 149, 136, 14);
		this.add(lblhorsMisAssurance);
		
		final JLabel lblNumCompte = new JLabel("Num. Compte :");
		lblNumCompte.setBounds(341, 96, 87, 14);
		this.add(lblNumCompte);
		
		final JLabel lblNumAccount = new JLabel("");
		lblNumAccount.setHorizontalAlignment(SwingConstants.CENTER);
		lblNumAccount.setOpaque(true);
		lblNumAccount.setBackground(SystemColor.activeCaption);
		lblNumAccount.setBounds(426, 96, 114, 18);
		this.add(lblNumAccount);
		
		final JLabel lblClient = new JLabel("Client :");
		lblClient.setBounds(37, 96, 38, 14);
		this.add(lblClient);
		
		final JLabel lblCustomer = new JLabel("");
		lblCustomer.setHorizontalAlignment(SwingConstants.CENTER);
		lblCustomer.setOpaque(true);
		lblCustomer.setBackground(SystemColor.activeCaption);
		lblCustomer.setBounds(79, 96, 155, 18);
		this.add(lblCustomer);
		
		final JLabel lblTypeDeRemboursement = new JLabel("Type de remboursement :");
		lblTypeDeRemboursement.setBounds(293, 124, 152, 14);
		this.add(lblTypeDeRemboursement);
		
		final JLabel lblRepaymentConstant = new JLabel("");
		lblRepaymentConstant.setHorizontalAlignment(SwingConstants.CENTER);
		lblRepaymentConstant.setOpaque(true);
		lblRepaymentConstant.setBackground(SystemColor.activeCaption);
		lblRepaymentConstant.setBounds(455, 124, 85, 18);
		this.add(lblRepaymentConstant);
		
		final JLabel lblRsultat = new JLabel("Résultats");
		lblRsultat.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblRsultat.setBounds(10, 208, 87, 22);
		this.add(lblRsultat);
		
		final JLabel lblTauxDintrt = new JLabel("Taux d'intérêt :");
		lblTauxDintrt.setBounds(20, 234, 94, 14);
		this.add(lblTauxDintrt);
		
		final JLabel lblLoanRate = new JLabel("");
		lblLoanRate.setHorizontalAlignment(SwingConstants.CENTER);
		lblLoanRate.setOpaque(true);
		lblLoanRate.setBackground(SystemColor.activeCaption);
		lblLoanRate.setBounds(118, 234, 74, 18);
		this.add(lblLoanRate);
		
		final JLabel label = new JLabel("(hors mis assurance)");
		label.setFont(new Font("Tahoma", Font.ITALIC, 11));
		label.setBounds(198, 234, 136, 14);
		this.add(label);
		
		final JLabel lblMontantDesMensualits = new JLabel("Mensualités :");
		lblMontantDesMensualits.setBounds(30, 261, 84, 14);
		this.add(lblMontantDesMensualits);
		
		final JLabel lblRepaymentAmount = new JLabel("");
		lblRepaymentAmount.setHorizontalAlignment(SwingConstants.CENTER);
		lblRepaymentAmount.setOpaque(true);
		lblRepaymentAmount.setBackground(SystemColor.activeCaption);
		lblRepaymentAmount.setBounds(118, 259, 74, 18);
		this.add(lblRepaymentAmount);
		
		final JLabel lblTauxDassurance = new JLabel("Taux d'assurance :");
		lblTauxDassurance.setBounds(222, 259, 114, 14);
		this.add(lblTauxDassurance);
		
		final JLabel lblInsuranceRate = new JLabel("");
		lblInsuranceRate.setHorizontalAlignment(SwingConstants.CENTER);
		lblInsuranceRate.setOpaque(true);
		lblInsuranceRate.setBackground(SystemColor.activeCaption);
		lblInsuranceRate.setBounds(338, 259, 150, 18);
		this.add(lblInsuranceRate);
		
		final JLabel lblCotTotalDu = new JLabel("Coût total du crédit *:");
		lblCotTotalDu.setBounds(10, 284, 136, 14);
		this.add(lblCotTotalDu);
		
		final JLabel lblTotalCreditCost = new JLabel("");
		lblTotalCreditCost.setHorizontalAlignment(SwingConstants.CENTER);
		lblTotalCreditCost.setOpaque(true);
		lblTotalCreditCost.setBackground(SystemColor.activeCaption);
		lblTotalCreditCost.setBounds(138, 284, 74, 18);
		this.add(lblTotalCreditCost);
		
		final JLabel lblIntrt = new JLabel("Intérêts :");
		lblIntrt.setBounds(224, 284, 63, 14);
		this.add(lblIntrt);
		
		final JLabel lblInterest = new JLabel("");
		lblInterest.setHorizontalAlignment(SwingConstants.CENTER);
		lblInterest.setOpaque(true);
		lblInterest.setBackground(SystemColor.activeCaption);
		lblInterest.setBounds(283, 284, 74, 18);
		this.add(lblInterest);
		
		final JLabel lblAssurance = new JLabel("Assurance :");
		lblAssurance.setBounds(374, 284, 74, 14);
		this.add(lblAssurance);
		
		final JLabel lblInsurance = new JLabel("");
		lblInsurance.setHorizontalAlignment(SwingConstants.CENTER);
		lblInsurance.setOpaque(true);
		lblInsurance.setBackground(SystemColor.activeCaption);
		lblInsurance.setBounds(447, 284, 74, 18);
		this.add(lblInsurance);
		
		final JLabel lblFraisDeDossier = new JLabel("Frais de dossier :");
		lblFraisDeDossier.setBounds(315, 234, 97, 14);
		this.add(lblFraisDeDossier);
		
		final JLabel lblApplicationFee = new JLabel("");
		lblApplicationFee.setHorizontalAlignment(SwingConstants.CENTER);
		lblApplicationFee.setOpaque(true);
		lblApplicationFee.setBackground(SystemColor.activeCaption);
		lblApplicationFee.setBounds(414, 234, 74, 18);
		this.add(lblApplicationFee);
		
		//We define the home choice of ComboBox
		String lblChoix = "- Choisir -";
		cbScenChoice.setSelectedItem(lblChoix);  
		
		final JLabel lblTitle = new JLabel("");
		lblTitle.setForeground(Color.BLUE);
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTitle.setBounds(167, 42, 473, 14);
		this.add(lblTitle);
		
		JLabel lblNomDuScenario = new JLabel("Nom du scenario :");
		lblNomDuScenario.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNomDuScenario.setBounds(10, 42, 147, 22);
		this.add(lblNomDuScenario);
		
		tblRepay = new JTable();
		tblRepay.setBounds(27, 272, 700, 422);
		this.add(tblRepay);
		
		JScrollPane scrollPane = new JScrollPane(tblRepay);
		scrollPane.setBounds(550, 96, 802, 422);
		this.add(scrollPane);
		
		JLabel lblTableauDamortissement = new JLabel("TABLEAU D'AMORTISSEMENT");
		lblTableauDamortissement.setForeground(Color.BLUE);
		lblTableauDamortissement.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblTableauDamortissement.setBounds(841, 74, 370, 14);
		this.add(lblTableauDamortissement);
		
		JLabel lblTotalDesMensualites = new JLabel("TOTAL DES MENSUALITES : ");
		lblTotalDesMensualites.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTotalDesMensualites.setForeground(Color.BLUE);
		lblTotalDesMensualites.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTotalDesMensualites.setBounds(596, 529, 458, 14);
		this.add(lblTotalDesMensualites);
		
		JLabel lblTotalDesInterets = new JLabel("TOTAL DES INTERETS :");
		lblTotalDesInterets.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTotalDesInterets.setForeground(Color.BLUE);
		lblTotalDesInterets.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTotalDesInterets.setBounds(684, 565, 370, 14);
		this.add(lblTotalDesInterets);
		
		JLabel lblTotalDeL = new JLabel("TOTAL DE L ASSURANCE :");
		lblTotalDeL.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTotalDeL.setForeground(Color.BLUE);
		lblTotalDeL.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTotalDeL.setBounds(684, 601, 370, 14);
		this.add(lblTotalDeL);
		
		JLabel label_1 = new JLabel("(hors mis assurance)");
		label_1.setFont(new Font("Tahoma", Font.ITALIC, 11));
		label_1.setBounds(1238, 531, 114, 14);
		this.add(label_1);
		
		final JLabel lblTotalCapital = new JLabel("");
		lblTotalCapital.setBackground(Color.WHITE);
		lblTotalCapital.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTotalCapital.setForeground(new Color(0, 100, 0));
		lblTotalCapital.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblTotalCapital.setBounds(1056, 529, 155, 14);
		this.add(lblTotalCapital);
		
		final JLabel lblTotalInterest = new JLabel("");
		lblTotalInterest.setBackground(SystemColor.inactiveCaptionBorder);
		lblTotalInterest.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTotalInterest.setForeground(new Color(0, 0, 128));
		lblTotalInterest.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblTotalInterest.setBounds(1056, 567, 155, 14);
		this.add(lblTotalInterest);
		
		final JLabel lblTotalInsurance = new JLabel("");
		lblTotalInsurance.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTotalInsurance.setForeground(new Color(255, 69, 0));
		lblTotalInsurance.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblTotalInsurance.setBounds(1056, 603, 155, 14);
		this.add(lblTotalInsurance);
		
		JLabel label_2 = new JLabel("€");
		label_2.setFont(new Font("Tahoma", Font.BOLD, 12));
		label_2.setBounds(1221, 562, 87, 22);
		this.add(label_2);
		
		JLabel label_3 = new JLabel("€");
		label_3.setFont(new Font("Tahoma", Font.BOLD, 12));
		label_3.setBounds(1221, 598, 87, 22);
		this.add(label_3);
		
		JLabel label_4 = new JLabel("€");
		label_4.setFont(new Font("Tahoma", Font.BOLD, 12));
		label_4.setBounds(1221, 529, 17, 22);
		this.add(label_4);
		
		final JButton btnNewButton = new JButton("GRAPHE DES RESULTATS");
		btnNewButton.setEnabled(false);

		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnNewButton.setBounds(10, 356, 246, 67);
		this.add(btnNewButton);
		
		final JButton btnDashboard = new JButton("DASHBOARD");
		btnDashboard.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnDashboard.setEnabled(false);
		btnDashboard.setBounds(293, 356, 246, 67);
		this.add(btnDashboard);
		
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
            	ChartResult frame = null;
				try { 
					System.out.print(((SimulationIdentifier) cbScenChoice.getSelectedItem()).getId());
					frame = new ChartResult(socket,((SimulationIdentifier) cbScenChoice.getSelectedItem()).getId());
				} catch (NumberFormatException | ClassNotFoundException | SQLException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                frame.setVisible(true);
            	//jButton4ActionPerformed(evt);
			}
		});
		btnDashboard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				BarChartResult frame = null;
				try { 
					System.out.print(((SimulationIdentifier) cbScenChoice.getSelectedItem()).getId());
					frame = new BarChartResult(socket,((SimulationIdentifier) cbScenChoice.getSelectedItem()).getId(),"");
				} catch (NumberFormatException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                frame.setVisible(true);
            	//jButton4ActionPerformed(evt);
			}
		});
		
 

		String message = null;
		// Sending the account_id over to the server
		GetAllSimsQuery query = new GetAllSimsQuery("-1");
		out.println(query.toString());
		// Receiving the server's response
		
		try {
			message = in.readLine();
			System.out.print(message);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		//System.out.print(query.toString());
		//Treating the server's response
		try {
			// Prefix and content detection
			int prefixEnd = message.indexOf(' ');
			
			if(prefixEnd == -1) {
				throw new Exception("No prefix");
			}
			
			String prefix = message.substring(0, prefixEnd);
			String content = message.substring(prefixEnd + 1);
			
			//System.out.print(message);
			// Prefix identification
			switch(prefix) {
			case "ERR":
				JOptionPane.showMessageDialog(thisObject, "Format error. Try downloading the newest version.");
				break;
			
			case "OK":
				// De-serialization
				GetAllSimsServerResponse response = JsonImpl.fromJson(content, GetAllSimsServerResponse.class);     
				//System.out.println(response.getSimulations()); 
				List<SimulationIdentifier> listSims=  response.getSimulations();  
				for(int i=0; i<listSims.toArray().length;i++){
				//System.out.println(listSims.toArray()[i]);
				cbScenChoice.addItem((SimulationIdentifier) listSims.toArray()[i]); 
			    } 
				//System.out.println(listSims.toArray().length); 
				 
 				//System.out.println(theList);
				//for(int i=0; i<=theList.length;i++){
					//System.out.println(theList[i]);
				//}
				//cbScenChoice.setModel(response);
				break;
			case "UNAUTHORIZED":
				// De-serialization
				GetAllSimsServerResponse responses = JsonImpl.fromJson(content, GetAllSimsServerResponse.class);     
				//System.out.println(responses); 
				List<SimulationIdentifier> listSimss=  responses.getSimulations();  
				for(int i=0; i<listSimss.toArray().length;i++){
				//System.out.println(listSimss.toArray()[i]);
				cbScenChoice.addItem((SimulationIdentifier) listSimss.toArray()[i]); 
			    } 
				//System.out.println(listSims.toArray().length); 
				 
 				//System.out.println(theList);
				//for(int i=0; i<=theList.length;i++){
					//System.out.println(theList[i]);
				//}
				//cbScenChoice.setModel(response);
				break;
			
			default:
				throw new Exception("Unknown prefix");
			}
		} catch (Exception e1) {
			//System.out.print(e1); 
			JOptionPane.showMessageDialog(thisObject, "Unknown response format. Please try again later or download the newest version.");
		} 
		
		//Action on select item in combox list of scenario
		cbScenChoice.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				EventQueue.invokeLater(new Runnable() { // Starting a thread is long, so we need to clear the eventqueue first
					public void run() {
						new Thread(new Runnable() { // We launch a new thread for this treatment, so that the GUI can still update. This new thread will be the host for the onSuccessfulLogin callable 
							public void run() {
								PrintWriter out;
								BufferedReader in; 
								Socket connections = null;
								Properties prop = KappaProperties.getInstance();
								try {
									connections = new Socket(prop.getProperty("SERVER_IP"), Integer.parseInt(prop.getProperty("SERVER_PORT")));
								} catch (NumberFormatException | IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								try {
									new PrintWriter(connections.getOutputStream(), true);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								try { 
									
									//out = new PrintWriter(connections.getOutputStream(), true); 
									out = new PrintWriter(socket.getOutputStream(), true);
									//in = new BufferedReader(new InputStreamReader(connections.getInputStream()));
									in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
								} catch (Exception e1) { // Reached if an IO exception occurs, or if the socket is not connected anymore
									JOptionPane.showMessageDialog(thisObject, "Erreur: connection au serveur interrompue. V�rifiez votre connection Internet, puis essayez de vous re-connecter.");
									return;
								}
								try {
									// Sending the loan_id over to the server
									GetSimQuery query = new GetSimQuery(((SimulationIdentifier) cbScenChoice.getSelectedItem()).getId());
									out.println(query.toString());
									// Receiving the server's response
									String message = in.readLine(); 

									//Treating the server's response
									try {
										// Prefix and content detection
										int prefixEnd = message.indexOf(' ');
										
										if(prefixEnd == -1) {
											throw new Exception("No prefix");
										}
										
										String prefix = message.substring(0, prefixEnd);
										String content = message.substring(prefixEnd + 1);
										
										// Prefix identification
										switch(prefix) {
										case "ERR":
											JOptionPane.showMessageDialog(thisObject, "Format error. Try downloading the newest version.");
											 
											break;
										
										case "OK":
											// De-serialization
											GetSimServerResponse response = JsonImpl.fromJson(content, GetSimServerResponse.class);
											AmortizationType amortization = response.getAmortizationType(); 
											List<Repayment> listrepay=  response.getRepayments();   
											System.out.print(listrepay.toString());
											String amort = amortization.toString();
											float total_insurance=0;
											float total_capital=0; 
											float total_interest=0;


											String[][] datas = (String[][]) new String[listrepay.size()][6];

											for(int i=0 ; i < listrepay.size() ; i++) {
												//We get the total amount of each part of the credit
												total_insurance =  total_insurance+listrepay.get(i).getInsurance();
												total_capital=total_capital+listrepay.get(i).getCapital();
												total_interest=total_interest+listrepay.get(i).getInterest();
												datas[i][0] =  Integer.toString(i+1);
												datas[i][1] = listrepay.get(i).getDate().toString();
												datas[i][2] = Float.toString(listrepay.get(i).getCapital());
												datas[i][3] =  Float.toString(listrepay.get(i).getInterest());
												datas[i][4] = Float.toString(listrepay.get(i).getInsurance());
												datas[i][5] = Float.toString(listrepay.get(i).getInsurance()+listrepay.get(i).getCapital() + listrepay.get(i).getInterest());

											}
											//String titretable[] = new String [] {
											//		"ECHEANCE", "DATE", 
											//};
											//tblRepay.set; 

													String col[] = {"ECHEANCE","DATE","MENSUALITE","DONT INTERETS","ASSURANCE","MENSUALITE TOTAL"}; 
													DefaultTableModel model = new DefaultTableModel(datas,col);
													tblRepay.setModel(model);
													tblRepay.setFillsViewportHeight(true);
													// Create the scroll pane and add the table to it. 
													// Add the scroll pane to this panel. 
											if (amort=="steady"){
												amort="CONSTANT";
											}else if(amort=="degressive") {
												amort="DEGRESSIF";
											}
											System.out.println(response);
											lblTitle.setText(response.getName());
											lblCustomer.setText(response.getAccountId());
											lblAge.setText(response.getAge());
											lblCapital.setText(Float.toString(response.getCapital()));
											lblTypeLoan.setText(response.getTypeSim());
											lblNumAccount.setText(response.getAcountNum());
											lblRepaymentConstant.setText(amort);
											lblRepaymentAmount.setText(Float.toString(response.getRepaymentConstant()));
											lblLoanRate.setText("0.5");
											lblInsuranceRate.setText(Float.toString(total_insurance/total_capital)); 
											lblTotalCreditCost.setText(Float.toString(total_capital+total_interest+total_insurance));
											lblInterest.setText(Float.toString(total_interest));
											//lblInsurance.setText(Float.toString(response.getInsurance()));
											lblInsurance.setText(Float.toString(total_insurance));
											lblApplicationFee.setText(Float.toString(response.getProcessing_fee()));
											//textPane.setText(listrepay.toString()); 
											lblTotalCapital.setText(Float.toString(total_capital));
											lblTotalInterest.setText(Float.toString(total_interest));
											lblTotalInsurance.setText(Float.toString(total_insurance));
											btnNewButton.setEnabled(true);
											btnDashboard.setEnabled(true);
											
											break;
										
										default:
											throw new Exception("Unknown prefix");
										}
									} catch (Exception e1) {
										JOptionPane.showMessageDialog(thisObject, "Unknown response format. Please try again later or download the newest version.");
									}
								} catch (IOException e1) {
									JOptionPane.showMessageDialog(thisObject, "Unable to connect to the server. Please try again later.");
								}
							}
						}).start();
					}
				});
			}
			
		});
	}

	public static void main(String[] args) {
		/* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
            	MainResultGUI mr = null;
				try {
					mr = new MainResultGUI();
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                mr.setVisible(true);
                
            }
        });
	}	
	
}
