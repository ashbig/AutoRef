/**
 * Id: ProcessObject.java $
 *
 * File     : ProcessObject.java 
 * Date     : 04162001
 * Author	: Dongmei Zuo
 */

package flex.process;

/**
 * This class represents an object that gets executed during a process.
 */
public class ProcessObject {
	private Object o;
	private String type;
	private String iotype;

	/**
	 * Constructor.
	 *
	 * @param o A processed object.
	 * @param type The type of the processed object.
	 * @param iotype The input/output type of the  processed object.
	 *
	 * @return A ProcessObject object.
	 */	
	public ProcessObject(Object o, String type, String iotype) {
		this.o = o;
		this.type = type;
		this.iotype = iotype;
	}

	/**
	 * Return the processed object.
	 *
	 * @return The processed object.
	 */
	public Object getObject() {
		return o;
	}

	/**
	 * Return the processed object type.
	 *
	 * @return The processed object type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * Return the input/output type.
	 *
	 * @return The input/output type.
	 */
	public String getIotype() {
		return iotype;
	}
}