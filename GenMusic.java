import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.MidiChannel;
import java.util.Random;
import java.util.Vector;
import java.lang.Math;

public class GenMusic {

	public static void main( String[] args ) {

		int channel = 0; // 0 is a piano, 9 is percussion, other channels are for other instruments

		int volume = 80; // between 0 and 127
		int duration = 200;//400; // in milliseconds
		Random rand = new Random();
		int totalNumNotes = 30;
		int pitchOffset = 50;
		int maxNotes = 6;
		int minNotes = 4;
		int chancePause = 3;
		int currentNumNotes = 0;
		int apart = 3;
		int numRepeat = 4;
		Vector currentNotesVec = new Vector();
		Vector lastNotesVec = new Vector();

		try {
			//init everything
			Synthesizer synth = MidiSystem.getSynthesizer();
			synth.open();
			MidiChannel[] channels = synth.getChannels();

			// --------------------------------------
			// Play a few notes.
			// The two arguments to the noteOn() method are:
			// "MIDI note number" (pitch of the note),
			// and "velocity" (i.e., volume, or intensity).
			// Each of these arguments is between 0 and 127.
			
			//-----notes-----
			//60 C
			//62 D
			//64 E
			//67 G
			/*
			channels[channel].noteOn( 60, volume ); // C note
			Thread.sleep( duration );
			channels[channel].noteOff( 60 );
			channels[channel].noteOn( 62, volume ); // D note
			Thread.sleep( duration );
			channels[channel].noteOff( 62 );
			channels[channel].noteOn( 64, volume ); // E note
			Thread.sleep( duration );
			channels[channel].noteOff( 64 );

			Thread.sleep( 500 );
	//		*/

			for(int i=0; i<1000; i++)
			{
				//determine number of notes to play
				currentNumNotes = rand.nextInt(maxNotes - minNotes);
				currentNumNotes += minNotes - currentNotesVec.size();
				//int pause = rand.nextInt(chancePause);
				//if(pause == 0)
				//	currentNumNotes = 0;
				//currentNumNotes = 4;
				for(int j=0; j<currentNumNotes; j++)
				{
					channels[channel].allNotesOff();
					//pick note
					int choice = rand.nextInt(totalNumNotes);
					if(!currentNotesVec.contains(choice))//no repeats
					{
						boolean good = true;
						for(int k=0;good && k<currentNotesVec.size();k++)
						{
							//limit which notes play together
							int vecNoteVal = (int)currentNotesVec.elementAt(k);
							vecNoteVal = vecNoteVal % 12;
							int choiceVal = choice % 12;
							int ans = Math.abs(vecNoteVal - choiceVal);
							if(ans == 0)
							{
								//notes are the same but diff octive
								good = true;
							}
							else if(ans < apart || ans > 11 - apart)
							{
								good = false;
							}
						}
						if(good)
						{
							currentNotesVec.add(choice);
						}
					}
				}

				for(int j=0;j<currentNotesVec.size();j++)
				{
					channels[channel].noteOn( (int)currentNotesVec.elementAt(j) + pitchOffset, volume ); 
				}
				for(int j=0;j<totalNumNotes;j++)
				{
					if(currentNotesVec.contains(j))
						System.out.print("" + j % 12 + " ");
					else
						System.out.print("  ");
				}
				System.out.println();
				lastNotesVec = currentNotesVec;
				//keep a random note
				Vector keepNotesVec = new Vector();
				for(int j=0;j<numRepeat;j++)
				{
					int keepIndex = rand.nextInt(currentNotesVec.size());
					int keepValue = (int) currentNotesVec.elementAt(keepIndex);
					if(!keepNotesVec.contains(keepValue))//no repeats
					{
						keepNotesVec.add(keepValue);
					}
				}
				currentNotesVec.clear();
				for(int j=0;j<keepNotesVec.size();j++)
				{
					currentNotesVec.add((int) keepNotesVec.elementAt(j));
				}
				keepNotesVec.clear();
				Thread.sleep( duration );
				//channels[channel].allNotesOff();
			}
			Thread.sleep( 500 );

			//clean up
			synth.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}

