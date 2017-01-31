package sampleclients;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class GuiClient extends JFrame {
	private static ActionListener listener = new ActionListener() {
		@Override
		public void actionPerformed( ActionEvent e ) {
			CommandButton c = ( (CommandButton) e.getSource() );
			buttonSend( c.t, c.cmd );
		}

	};

	// Receiver and Transmitter are not needed for most planners, but may lead to (negligible) speed up as you do not synchronize each
	// action with the server
	private class ServerReceiver extends Thread {
		private GuiClient gui;

		public ServerReceiver( GuiClient g ) {
			gui = g;
		}

		public void run() {
			try {
				BufferedReader reader = new BufferedReader( new InputStreamReader( System.in ) );
				while ( true ) {
					String msg = reader.readLine();
					if ( msg == null )
						throw new IOException( "End of server messages" );

					gui.AddCommunication( "<IN>  " + msg );
				}
			} catch ( IOException e ) {
				gui.AddInformation( e.toString() );
			}
		}
	}

	private class ServerTransmitter extends Thread {
		private GuiClient gui;
		private LinkedBlockingQueue< String > outbound = new LinkedBlockingQueue< String >();

		public ServerTransmitter( GuiClient g ) {
			gui = g;
		}

		public void run() {
			try {
				while ( true ) {
					String msg = outbound.take();
					System.out.println( msg );
					gui.AddCommunication( "<OUT> " + msg );
				}

			} catch ( InterruptedException e ) {
				gui.AddInformation( e.toString() );
			}
		}
	}

	private class CommandButton extends JButton {
		public final String cmd;
		public final ServerTransmitter t;

		public CommandButton( ServerTransmitter t, String label, String cmd ) {
			super( label );
			this.t = t;
			this.cmd = cmd;
			this.addActionListener( listener );
		}
	}

	public static void buttonSend( ServerTransmitter st, String cmd ) {
		st.outbound.add( cmd );
	}

	private final String nl = System.getProperty( "line.separator" );

	private JTextArea communication = new JTextArea();
	private JTextArea information = new JTextArea();

	private JPanel fixed = new JPanel();
	private JPanel custom = new JPanel();
	private JPanel comm = new JPanel();
	private JPanel info = new JPanel();

	private int msgNo;
	private int agents;

	private ServerReceiver receiver;

	private ServerTransmitter transmitter;

	private class GBC extends GridBagConstraints {
		public GBC( int x, int y ) {
			this( x, y, 1 );
		}

		public GBC( int x, int y, int spanx ) {
			this.insets = new Insets( 0, 3, 0, 3 );
			this.gridx = x;
			this.gridy = y;
			this.gridwidth = spanx;
			this.fill = GridBagConstraints.NONE;
		}

		public GBC( int x, int y, int spanx, int sep ) {
			this.insets = new Insets( sep, 3, sep, 3 );
			this.gridx = x;
			this.gridy = y;
			this.gridwidth = spanx;
			this.fill = GridBagConstraints.HORIZONTAL;
			this.weightx = 0;
		}
	}

	public GuiClient( String[] customs ) throws IOException {
		super( "02285 Toy Client" );
		System.err.println("Hello from GuiClient");
		readMap(); // Get agent count
		receiver = new ServerReceiver( this );
		transmitter = new ServerTransmitter( this );

		communication.setEditable( false );
		communication.setFont( new Font( "Monospaced", Font.PLAIN, 11 ) );
		information.setEditable( false );
		information.setFont( new Font( "Monospaced", Font.PLAIN, 11 ) );

		// Fixed Buttons panel
		JSeparator sep1 = new JSeparator( JSeparator.HORIZONTAL );
		JSeparator sep2 = new JSeparator( JSeparator.HORIZONTAL );
		HashMap< String, GBC > buts = new HashMap< String, GBC >();
		fixed.setLayout( new GridBagLayout() );

		buts.put( "Move(N)", new GBC( 3, 0 ) );
		buts.put( "Move(W)", new GBC( 2, 1 ) );
		buts.put( "Move(E)", new GBC( 4, 1 ) );
		buts.put( "Move(S)", new GBC( 3, 2 ) );
		fixed.add( new JLabel( "Navigation" ), new GBC( 2, 1, 3 ) );
		fixed.add( sep1, new GBC( 0, 3, 7, 10 ) );
		int yoff = 4;
		buts.put( "Push(N,N)", new GBC( 3, yoff + 1 ) );
		buts.put( "Push(N,W)", new GBC( 2, yoff + 1 ) );
		buts.put( "Push(W,N)", new GBC( 1, yoff + 2 ) );
		buts.put( "Push(W,W)", new GBC( 1, yoff + 3 ) );
		buts.put( "Push(W,S)", new GBC( 1, yoff + 4 ) );
		buts.put( "Push(S,W)", new GBC( 2, yoff + 5 ) );
		buts.put( "Push(N,E)", new GBC( 4, yoff + 1 ) );
		buts.put( "Push(E,N)", new GBC( 5, yoff + 2 ) );
		buts.put( "Push(E,E)", new GBC( 5, yoff + 3 ) );
		buts.put( "Push(E,S)", new GBC( 5, yoff + 4 ) );
		buts.put( "Push(S,E)", new GBC( 4, yoff + 5 ) );
		buts.put( "Push(S,S)", new GBC( 3, yoff + 5 ) );
		fixed.add( new JLabel( "Push" ), new GBC( 2, yoff + 3, 3 ) );
		fixed.add( sep2, new GBC( 0, yoff + 7, 7, 10 ) );
		yoff = 12;
		buts.put( "Pull(N,S)", new GBC( 3, yoff + 1 ) );
		buts.put( "Pull(N,W)", new GBC( 2, yoff + 1 ) );
		buts.put( "Pull(W,N)", new GBC( 1, yoff + 2 ) );
		buts.put( "Pull(W,E)", new GBC( 1, yoff + 3 ) );
		buts.put( "Pull(W,S)", new GBC( 1, yoff + 4 ) );
		buts.put( "Pull(S,W)", new GBC( 2, yoff + 5 ) );
		buts.put( "Pull(S,N)", new GBC( 3, yoff + 5 ) );
		buts.put( "Pull(N,E)", new GBC( 4, yoff + 1 ) );
		buts.put( "Pull(E,N)", new GBC( 5, yoff + 2 ) );
		buts.put( "Pull(E,W)", new GBC( 5, yoff + 3 ) );
		buts.put( "Pull(E,S)", new GBC( 5, yoff + 4 ) );
		buts.put( "Pull(S,E)", new GBC( 4, yoff + 5 ) );
		fixed.add( new JLabel( "Pull" ), new GBC( 2, yoff + 3, 3 ) );

		for ( Entry< String, GBC > e : buts.entrySet() ) {
			fixed.add( new CommandButton( transmitter, e.getKey(), "[" + Multify( e.getKey() ) + "]" ), e.getValue() );
		}

		// Custom Panel
		GridBagConstraints c = new GridBagConstraints();
		c.gridy++;
		custom.setLayout( new GridBagLayout() );
		if ( customs.length == 0 )
			customs = new String[] { "", "" };

		for ( int i = 0; i < customs.length; i++ ) {
			JButton but = new JButton( "Command " + i );
			final JTextField input = new JTextField( customs[i] );
			but.addActionListener( new ActionListener() {
				@Override
				public void actionPerformed( ActionEvent e ) {
					buttonSend( transmitter, input.getText() );
				}
			} );

			c = new GridBagConstraints();
			c.gridy = i;
			c.fill = GridBagConstraints.HORIZONTAL;
			custom.add( but, c );
			c.weightx = 0.80;
			c.gridx = 1;
			custom.add( input, c );
		}

		// Communication panel
		comm.setLayout( new GridBagLayout() );
		comm.setMinimumSize(new Dimension(200, 250));
		c = new GridBagConstraints();
		c.ipadx = 5;
		comm.add( new JLabel( "Communication Done" ), c );
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		comm.add( new JScrollPane( communication, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED ), c );

		// Information panel
		info.setLayout( new GridBagLayout() );
		comm.setMinimumSize(new Dimension(200, 100));
		c = new GridBagConstraints();
		c.ipadx = 5;
		info.add( new JLabel( "Client Information (e.g. Exceptions)" ), c );
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		info.add( new JScrollPane( information, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED ), c );

		// Add components to main frame
		setLayout( new GridBagLayout() );
		c = new GridBagConstraints();
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets( 3, 3, 3, 3 );
		add( fixed, c );

		c.gridy = 1;
		add( custom, c );

		c.gridy = 2;
		c.weighty = 0.8;
		c.fill = GridBagConstraints.BOTH;
		add( comm, c );

		c.gridy = 3;
		c.weighty = 0.2;
		add( info, c );

		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setMinimumSize( new Dimension( 525, 820 ) );
		setLocation( 800, 120 );
		receiver.start();
		transmitter.start();
		this.pack();
		setVisible( true );
	}

	public void AddCommunication( String m ) {
		// Append is thread safe..
		communication.append( msgNo + ":\t" + m + nl );
		synchronized ( this ) {
			communication.setCaretPosition( communication.getText().length() );
			msgNo++;
		}
	}

	public void AddInformation( String m ) {
		// Append is thread safe..
		information.append( m + nl );
		synchronized ( this ) {
			information.setCaretPosition( information.getText().length() );
		}
	}

	/**
	 * Turns Cmd into Cmd,Cmd,Cmd (..) based on number of agents
	 * 
	 * @param cmd
	 * @return Multified cmd
	 */
	private String Multify( String cmd ) {
		String s = "";
		for ( int i = 0; i < agents - 1; i++ ) {
			s += cmd + ",";
		}
		return s + cmd;
	}

	private void readMap() throws IOException {
		BufferedReader in = new BufferedReader( new InputStreamReader( System.in ) );
		agents = 0;
		String line;

		// Read lines specifying colors
		while ( ( line = in.readLine() ).matches( "^[a-z]+:\\s*[0-9A-Z](,\\s*[0-9A-Z])*\\s*$" ) ) {
			// Skip
		}
		// Read lines specifying level layout
		// By convention levels are ended with an empty newline
		while ( !line.equals( "" ) ) {
			for ( int i = 0; i < line.length(); i++ ) {
				char id = line.charAt( i );
				if ( '0' <= id && id <= '9' )
					agents++;
			}
			line = in.readLine();
			if ( line == null )
				break;

		}
	}

	public static void main( String[] args ) {
		try {
			UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
			new GuiClient( args );
		} catch ( Exception e ) {
			e.printStackTrace();
		}

	}

}
