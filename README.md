# MuseScore Helper Program
### Function:
This is a program that can speed up museScore's note-editing process with an interactive piano keyboard that 
can accept computer keyboard's input. The accuracy of the tempo is set to a sixteenth note in order to minimize
the noise. Current supporting functions includes:
- Setting tempo of metronome
- Setting what each tick represent (eg: sixteenth note[1], eighth note[2] or quarter note[4])
- Setting the default tonality of the score (how many flat and sharp)
- Setting the type of score we're typing (Treble or Bass)

### Instruction:
1. Run OuterClass
2. Set up basic settings:
   1. Type in the tempo (the first blank) in rpm. `Default: 120`
   2. Choose what each tick's representation (1/2/4 as described above). `Default: 1`
   3. Choose how many flat and sharp (number). `Default: 0`
   4. Choose if it's sharp or flat (if it's C major either will work). `Default: #`
   5. Choose the type of score (Treble / Bass). `Default: Treble`
3. Click Start. Click the piano windows after the metronome start working. 
4. Start typing whenever ready. The score won't start listening until the first note is hit.
5. Click pause will output the current typed score (excluding the last note typed)
6. Click start after pausing will continue the typing process.
7. Click finish when you want to terminate the program.
8. **After clicking pause/finish, make sure to click the museScore app within 5 second. Then the note will be typed automatically**

### Future edition:
- edit the visualization rectangle for the rest
- if working with 3/4, the rest part must be rewritten. Currently only support 4/4
- Support alto clef for viola/ tenor clef for some cello pieces