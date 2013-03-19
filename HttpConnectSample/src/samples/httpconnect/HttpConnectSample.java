package samples.httpconnect;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.sai.samples.httpconnect.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class HttpConnectSample extends Activity {
    
	private Button xmlButton;
	private ProgressDialog progressDialog;	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        xmlButton = (Button)findViewById(R.id.Button03);
        
        xmlButton.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick(View v) {
				connectXML("http://www.ffrm.es/ffrm/conf.xml");

			}
        });
        

    }
    
	private void connectXML(String urlStr) {
		// A dialog showing a progress indicator and an optional text message or view.
		progressDialog = ProgressDialog.show(this, "", "Connecting...");
		final String url = urlStr; // Variable de tipo "final" se puede acceder desde el nuevo hilo
		
		new Thread() { // Es mejor hacer un nuevo hilo dedicado a la conexion, en lugar de usar el principal
			public void run() {
				InputStream in = null;
				int BUFFER_SIZE = 2000; // Cogemos caracteres de 2000 en 2000
				// Existe una cola de mensajes, por eso hay que crear uno nuevo mediante obtain
				Message msg = Message.obtain(); // message is an object for communication between threads
				msg.what = 1;
				try {
//////////////////////////////////////////////////////////////
//					DefaultHttpClient hc = new DefaultHttpClient();
//					ResponseHandler <String> res = new BasicResponseHandler();
//					HttpPost postMethod = new HttpPost(url);
//					String response=hc.execute(postMethod,res);
//					XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
//		            FirstConnHandler fcHandler = new FirstConnHandler();
//		            xmlReader.setContentHandler(fcHandler);
//		            InputSource inputSource = new InputSource();
//		            inputSource.setEncoding("UTF-8");
//		            inputSource.setCharacterStream(new StringReader(response));
//		            try{
//		            xmlReader.parse(inputSource);
//					} catch (IOException e1) {
//					    e1.printStackTrace();
//					} catch (Exception ex) {
//			            Log.d("XML", "FirstConnParser: parse() failed");
//			        }
//
//					
//////////////////////////////////////////////////////////////					
				    in = openHttpConnection(url);					    
//				    LAS SIGUIENTES LINEAS LEEN LA RESPUESTA XML COMO TEXTO (ver downloadText)
		            InputStreamReader isr = new InputStreamReader(in); // Lee caracteres del input stream
		            // OJO: no lee un string sino un array de caracteres
		            int charRead;
		            String text = ""; // String que contendra el texto final
		            char[] inputBuffer = new char[BUFFER_SIZE]; // Array de caracteres

		                // Reads characters from isr and stores them in the character
		                // array inputBuffer starting at offset 0.
		              	// Returns the number of characters actually read or -1
		                // if the end of the reader has been reached.
		            while ((charRead = isr.read(inputBuffer))>0)
		                  {                    
		                      //---convert the chars to a String---  (data, start, length)
		                      String readString = String.copyValueOf(inputBuffer, 0, charRead);                    
		                      text += readString; // Vamos componiendo el texto
		                      inputBuffer = new char[BUFFER_SIZE]; // Limpiamos el buffer
		                  }
		            Bundle b = new Bundle();
					b.putString("conf_xml", text); // Empaquetamos el texto y lo enviamos al hilo principal
					msg.setData(b);
					msg.what = 2;

//					XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
//		            FirstConnHandler fcHandler = new FirstConnHandler();
//		            // store handler in XMLReader
//		            xmlReader.setContentHandler(fcHandler);
//		            // the process starts
//		            xmlReader.parse(new InputSource(in));
		            
				    in.close(); // Hay que cerrar el flujo de entrada o input-stream
				} catch (IOException e1) {
				    e1.printStackTrace();
		        }

				// I notify the main / UI thread through this method and also pass on the Message object
				messageHandler.sendMessage(msg); // Enviamos el mensaje al hilo principal
				
			}
 		}.start(); // Starts the new Thread of execution

	}
	

	
	// Hence is the code for opening and making an HTTP Connection:
	private InputStream openHttpConnection(String urlStr) {
		InputStream in = null;
		int resCode = -1;
		
		try {
			URL url = new URL(urlStr);// Pasamos el string a url
			URLConnection urlConn = url.openConnection();// Abrimos una conexion
			// Instances of URLConnection are not reusable
			
			if (!(urlConn instanceof HttpURLConnection)) {// Aseguramos que es una conexion HTTP
				throw new IOException ("URL is not an Http URL");
			}
			
			HttpURLConnection httpConn = (HttpURLConnection)urlConn;// Transformamos la conexion en conexion HTTP
			httpConn.setAllowUserInteraction(true);// antes en false. Esto habra que cambiarlo ?? !!
            httpConn.setInstanceFollowRedirects(true);// true if this connection will follows redirects
            httpConn.setRequestProperty("User-Agent", "Android");
            httpConn.setRequestProperty("Content-Language", "es-ES");
            httpConn.setRequestMethod("GET");// Sets the request command which will be sent to the remote HTTP server
            httpConn.connect(); // Opens a connection to the resource

            resCode = httpConn.getResponseCode();// Returns the response code returned by the remote HTTP server.                
            if (resCode == HttpURLConnection.HTTP_OK) {
            	// Devuelve el objeto solicitado mediante GET
                in = httpConn.getInputStream(); // Returns an InputStream for reading data from the resource                            
            }         
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return in;
	}

// You can create your own threads, and communicate back with the main application thread through a Handler.
	private Handler messageHandler = new Handler() { // Recibimos el mensaje en el hilo principal
		
		public void handleMessage(Message msg) { // Subclasses must implement this to receive messages
			super.handleMessage(msg);
				TextView text1 = (TextView) findViewById(R.id.textView1);
				progressDialog.dismiss();
				switch (msg.what) {
				case 1:
					text1.setText("Failed!");
					break;
					
				case 2:
					List<String> urlStringList = confXmlGetUrls(msg.getData().getString("conf_xml"));
//					String urlsStr = "HEY!";
					String urlsStr = "url base: " + urlStringList.get(0) + "\n url Final: " + urlStringList.get(1);
					text1.setText(urlsStr);
					EditText editTextUser = (EditText) findViewById(R.id.editText3);
					EditText editTextPass = (EditText) findViewById(R.id.editText2);
					loginFunction(editTextUser.getText().toString() , editTextPass.getText().toString(), urlStringList.get(0));
					break;
				case 3:
					text1.setText(msg.getData().getString("validationAnswer"));
					break;
				}
		}
	};
	
	private List<String> confXmlGetUrls(String text) {
		
		String startUrlBase = "<urb>";
		String endUrlBase = "</urb>";
		String startUrlFinal = "<urf>";
		String endUrlFinal = "</urf>";
		String urlBaseStr, urlFinalStr = new String();
		List<String> urlStringList = new ArrayList<String>();
		
		urlBaseStr = text.substring(text.indexOf(startUrlBase) + startUrlBase.length(), text.indexOf(endUrlBase));
		urlFinalStr = text.substring(text.indexOf(startUrlFinal) + startUrlFinal.length(), text.indexOf(endUrlFinal));

		urlStringList.add(0, urlBaseStr);
		urlStringList.add(1, urlFinalStr);
		
		return urlStringList;
		
	}
	
	
	private void loginFunction(String username, String password, String urlBaseStr) {
		
		progressDialog = ProgressDialog.show(this, "", "Validating...");
		final String urlLogin = urlBaseStr + "validar?dcorreo=" + username + "&pw=" + encodePass(password) + "&version=1.1";
		new Thread() { // Es mejor hacer un nuevo hilo dedicado a la conexion, en lugar de usar el principal
			public void run() {
				InputStream in = null;
				int BUFFER_SIZE = 2000; // Cogemos caracteres de 2000 en 2000
				// Existe una cola de mensajes, por eso hay que crear uno nuevo mediante obtain
				Message msg = Message.obtain(); // message is an object for communication between threads
				msg.what = 1;
				try {
//////////////////////////////////////////////////////////////
//					DefaultHttpClient hc = new DefaultHttpClient();
//					ResponseHandler <String> res = new BasicResponseHandler();
//					HttpPost postMethod = new HttpPost(url);
//					String response=hc.execute(postMethod,res);
//					XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
//		            FirstConnHandler fcHandler = new FirstConnHandler();
//		            xmlReader.setContentHandler(fcHandler);
//		            InputSource inputSource = new InputSource();
//		            inputSource.setEncoding("UTF-8");
//		            inputSource.setCharacterStream(new StringReader(response));
//		            try{
//		            xmlReader.parse(inputSource);
//					} catch (IOException e1) {
//					    e1.printStackTrace();
//					} catch (Exception ex) {
//			            Log.d("XML", "FirstConnParser: parse() failed");
//			        }
//
//					
//////////////////////////////////////////////////////////////					
				    in = openHttpConnection(urlLogin);					    
//				    LAS SIGUIENTES LINEAS LEEN LA RESPUESTA XML COMO TEXTO (ver downloadText)
		            InputStreamReader isr = new InputStreamReader(in); // Lee caracteres del input stream
		            // OJO: no lee un string sino un array de caracteres
		            int charRead;
		            String text = ""; // String que contendra el texto final
		            char[] inputBuffer = new char[BUFFER_SIZE]; // Array de caracteres

		                // Reads characters from isr and stores them in the character
		                // array inputBuffer starting at offset 0.
		              	// Returns the number of characters actually read or -1
		                // if the end of the reader has been reached.
		            while ((charRead = isr.read(inputBuffer))>0)
		                  {                    
		                      //---convert the chars to a String---  (data, start, length)
		                      String readString = String.copyValueOf(inputBuffer, 0, charRead);                    
		                      text += readString; // Vamos componiendo el texto
		                      inputBuffer = new char[BUFFER_SIZE]; // Limpiamos el buffer
		                  }
		            Bundle b = new Bundle();
					b.putString("validationAnswer", text); // Empaquetamos el texto y lo enviamos al hilo principal
					msg.setData(b);
					msg.what = 3;

//					XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
//		            FirstConnHandler fcHandler = new FirstConnHandler();
//		            // store handler in XMLReader
//		            xmlReader.setContentHandler(fcHandler);
//		            // the process starts
//		            xmlReader.parse(new InputSource(in));
		            
				    in.close(); // Hay que cerrar el flujo de entrada o input-stream
				} catch (IOException e1) {
				    e1.printStackTrace();
		        }

				// I notify the main / UI thread through this method and also pass on the Message object
				messageHandler.sendMessage(msg); // Enviamos el mensaje al hilo principal
				
			}
 		}.start(); // Starts the new Thread of execution


	}
	
	private String encodePass(String password){
		
		String encPass = "f";
		int cont = 0;
		int auxInt = 0;
		char auxChar = (char) 0;
		
		for (cont = 0; cont < password.length(); cont++) {
			auxChar = password.charAt(password.length() - cont - 1);
			auxInt = (int) auxChar + 2;
			auxChar = (char) auxInt;
			encPass = encPass + auxChar;		
		}
		
		encPass = encPass + "8";
		return encPass;
		
	}
	
}