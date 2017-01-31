package sampleclients;

import java.io.*;
import java.util.*;

public class RandomWalkClient {
	private static Random rand = new Random();

	public class Agent {
		public Agent( char id, String color ) {
			System.err.println("Found " + color + " agent " + id );
		}

		public String act() {
			return Command.every[rand.nextInt( Command.every.length )].toString();
		}
	}

	private BufferedReader in = new BufferedReader( new InputStreamReader( System.in ) );
	private List< Agent > agents = new ArrayList< Agent >();

	public RandomWalkClient() throws IOException {
		readMap();
	}

	private void readMap() throws IOException {
		Map< Character, String > colors = new HashMap< Character, String >();
		String line, color;

		// Read lines specifying colors
		while ( ( line = in.readLine() ).matches( "^[a-z]+:\\s*[0-9A-Z](,\\s*[0-9A-Z])*\\s*$" ) ) {
			line = line.replaceAll( "\\s", "" );
			color = line.split( ":" )[0];

			for ( String id : line.split( ":" )[1].split( "," ) )
				colors.put( id.charAt( 0 ), color );
		}

		// Read lines specifying level layout
		while ( !line.equals( "" ) ) {
			for ( int i = 0; i < line.length(); i++ ) {
				char id = line.charAt( i );
				if ( '0' <= id && id <= '9' )
					agents.add( new Agent( id, colors.get( id ) ) );
			}

			line = in.readLine();

		}
	}

	public boolean update() throws IOException {
		String jointAction = "[";

		for ( int i = 0; i < agents.size() - 1; i++ )
			jointAction += agents.get( i ).act() + ",";
		
		jointAction += agents.get( agents.size() - 1 ).act() + "]";

		// Place message in buffer
		System.out.println( jointAction );
		
		// Flush buffer
		System.out.flush();

		// Disregard these for now, but read or the server stalls when its output buffer gets filled!
		String percepts = in.readLine();
		if ( percepts == null )
			return false;

		return true;
	}

	public static void main( String[] args ) {

		// Use stderr to print to console
		System.err.println( "Hello from RandomWalkClient. I am sending this using the error outputstream" );
		try {
			RandomWalkClient client = new RandomWalkClient();
			while ( client.update() )
				;

		} catch ( IOException e ) {
			// Got nowhere to write to probably
		}
	}
}
