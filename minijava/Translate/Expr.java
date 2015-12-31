package minijava.Translate;

import minijava.Tree.*;
import minijava.Temp.*;

public abstract class Expr {

    abstract public Exp unEx();

    abstract public Stm unCx(Label t, Label f);

    abstract public Stm unNx();
}