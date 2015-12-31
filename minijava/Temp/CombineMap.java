package minijava.Temp;

/**
 * A TempMap that combines two other TempMaps.
 */

public class CombineMap implements TempMap {

    private TempMap tmap1, tmap2;

    /**
     * Class constructor
     * @param t1 a TempMap
     * @param t2 a second TempMap.  Every Temp should have an entry in
     * the second map.
     */

    public CombineMap(TempMap t1, TempMap t2) {
	tmap1=t1; tmap2=t2;
    }

    /**
     * Returns the string to which a Temp is mapped.  If there is a
     * string for the Temp in the first map, it is returned.
     * Otherwise the second map is used.
     *
     * @param t the Temp
     * @return a String.  Assuming that the map was properly
     * constructed, this value will never be null.
     */

    public String tempMap(Temp t) {
	String s = tmap1.tempMap(t);
	if (s!=null) return s;
	return tmap2.tempMap(t);
    }
}
