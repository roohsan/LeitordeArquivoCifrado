package leitordearquivo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class Menu extends JFrame {

	//declaração das variaveis globais
	private JTextArea areaTexto;
	private JButton botaoCriar;
	private JButton botaoAbrir;
	private JButton botaoSalvar;
	private String caminho;
	private int senhaAux;

	Menu() {

		JFrame frame = new JFrame();
		
		JOptionPane.showMessageDialog(null, "Ola, tudo bem?\nUtilize a caixa de texto para criar sua mensagem!!");
				

		//criaçãoo da areaTexto
		areaTexto = new JTextArea();//criando a area de texto

		areaTexto.setBounds(0, 0, 490, 430);//dimensão e posicionamento 
		add(areaTexto); // adicionando a area de texto na tela.

		//criação do botao criarArquivo
		botaoCriar = new JButton("Criar Arquivo");//criando o botao
		botaoCriar.setBounds(0, 431, 165, 30);//dimensão e posicionamento  
		add(botaoCriar);//adicionando botaoCriar na tela

		//criação do botao abrir arquivo
		botaoAbrir = new JButton("Abrir/Editar");//criando
		botaoAbrir.setBounds(165, 431, 165, 30);
		add(botaoAbrir);//adicionando botaoArir na tela

		botaoSalvar = new JButton("Salvar");
		botaoSalvar.setBounds(330, 431, 165, 30);
		add(botaoSalvar);

		botaoCriar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {

				//evento do botaoCriar
				int senha = Integer.parseInt(JOptionPane.showInputDialog("Digite uma senha de 1 a 25:"));
				senhaAux = senha;

				//validado senha em branco

				if ((senha<=0)||(senha>25)) {

					JOptionPane.showMessageDialog(null, "Forneça uma Senha dentro do escopo Informado!",
							"alerta", JOptionPane.INFORMATION_MESSAGE);

				}else {

					CriptografiaCaesar c = new CriptografiaCaesar();

					JFileChooser criarCho = new JFileChooser();//criando um objeto JFileChooser
					criarCho.setFileSelectionMode(JFileChooser.FILES_ONLY);//restringindo a criação para somente arquivos
					int i = criarCho.showSaveDialog(null);//metodo salvar do JFileChooser

					if (i == 1) {
						JOptionPane.showMessageDialog(null, "Operação Cancelada!",
								"alerta", JOptionPane.ERROR_MESSAGE);

					} else {

						File arquivo = criarCho.getSelectedFile();

						FileWriter arq;

						try {
							arq = new FileWriter(arquivo.getAbsolutePath());
							PrintWriter gravarArq = new PrintWriter(arq);

							gravarArq.printf(areaTexto.getText() + "%n");
							arq.close();

							c.criptografarCaesar(areaTexto.getText(), senha);//enviando dados para cripitografia

							//salvando texto cripitografado no arquivo.
							arq = new FileWriter(arquivo.getPath());
							PrintWriter gravarCifra = new PrintWriter(arq);
							//JOptionPane.showMessageDialog(null, c.criptografarCaesar(areaTexto.getText(), senha));
							//textocifrado = c.criptografarCaesar(areaTexto.getText(), senha);//armazenado o texto cifrado
							gravarCifra.printf(c.criptografarCaesar(areaTexto.getText(), senha));
							arq.close();
							areaTexto.setText(" ");

							JOptionPane.showMessageDialog(null, "Arquivo Salvo!",
									"alerta", JOptionPane.INFORMATION_MESSAGE);
						} catch (IOException ex) {

						}
					}
				}		
			}

		});

		botaoAbrir.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				CriptografiaCaesar c = new CriptografiaCaesar();
				//evento do botaoAbrir
				int senha = Integer.parseInt(JOptionPane.showInputDialog("Digite a senha do arquivo:"));
				senhaAux = senha;   //salva a senha em caso de fechamento de programa, para poder descriptografar de forma correta o arquivo(encerramento perdi o valor inical)
				JFileChooser abrirCho = new JFileChooser();//instancia objeto
				abrirCho.showOpenDialog(null);//executa a interface JFchooser
				File arquivo = abrirCho.getSelectedFile();//obtem o arquivo
				String arqCaminho = arquivo.getPath();//pega o caminho do arquivo
				caminho = arqCaminho;//atribuindo a varivel global

				try {
					//leitura do conteudo
					BufferedReader leitor = new BufferedReader(new FileReader(arqCaminho));
					String texto1 = "", texto2 = "";
					while ((texto1 = leitor.readLine()) != null) { //capturando caracteres
						texto2 += texto1 + "\n";
					}
					String textoDecifrado = c.criptografarCaesar(texto2, 26-senha);//descripitografia da senha para exibição
					areaTexto.setText(textoDecifrado);//inserindo na area de texto
					leitor.close();

				} catch (FileNotFoundException ex) {

				} catch (IOException ex) {

				}


			}

		});

		botaoSalvar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {

				CriptografiaCaesar c = new CriptografiaCaesar();

				if (caminho != null) {
					FileWriter escrita;

					try {
						escrita = new FileWriter(caminho); //atribui o caminho do arquivo
						PrintWriter gravarArq = new PrintWriter(escrita);//cria um novo objeto para escrita



						gravarArq.printf(c.criptografarCaesar(areaTexto.getText(), senhaAux));//utiliza a senha auxilar, sendo assim conservando a senha incial do arquivo.
						//gravarArq.printf(areaTexto.getText() + "%n");//grava no caminho indicado

						escrita.close();
						JOptionPane.showMessageDialog(null, "Arquivo Salvo com Sucesso!!");
						caminho = null;
						areaTexto.setText("");
					} catch (IOException ex) {
					}
				} else {

					JOptionPane.showMessageDialog(null, "Arquivo Não Selecionado!!",
							"alerta", JOptionPane.ERROR_MESSAGE);

				}

			}

		});

		setSize(500, 500);
		setResizable(false);
		setLocationRelativeTo(null);
		setLayout(null);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}
