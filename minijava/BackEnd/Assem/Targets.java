package minijava.BackEnd.Assem;

import minijava.Temp.Label;
import java.util.*;

public class Targets {
    public List<Label> labels = new LinkedList<Label>();

    public Targets (Label... args) {
	for (Label l : args)
	    labels.add(l);
    }
}
