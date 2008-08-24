package com;

import java.util.ArrayList;

public class Example2
{
    static Example2 sequencer;

    ArrayList<String> sequences;

    public Example2()
    {
        this.sequences = (ArrayList<String>)((ArrayList)sequencer.sequences).clone();
        //this.sequences = (ArrayList)((ArrayList)sequencer.sequences).clone();
    }
}
