package minijava.Temp;


/**
 * A TempMap in which each Temp is mapped to its original name.
 */

public class DefaultMap implements TempMap {

  public String tempMap(Temp t) {
    return t.toString();
  }

}
