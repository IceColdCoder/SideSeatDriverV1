package smath;

public class SMath {
	
	private SMath(){}
	
	/**
	 * Scale input value with the input scalar value.
	 * @param value
	 * @param scalar
	 * @return
	 */
	public static int scale(int value, float scalar){
		return (int)((float)value * scalar);
	}
	
	/**
	 * Calculate the scalar value with the given input and output scale.
	 * @param output
	 * @param input
	 * @return
	 */
	public static float getScalar(int output, int input){
		return (float)((float)output / (float)input);
	}
	
	/**
	 * Return the absolute value of the input.
	 * @param input
	 * @return
	 */
	public static float abs(float input){
		if(input < 0){
			return -1 * input;
		}
		return input;
	}

}
