/** @author James Taylor 
 * Com S 227 Sec B Sec 8
 * HW 1 --- 1/27/2017
 */

package hw1;

public class Television {
/** Used when changing the Volume 
 * 
*/
	public static final double VOLUME_INCREMENT = 0.07;
/**
 * channel and volume simply track the current values
 * maxC is the maximum channel
 * prevChannel keeps track of what channel the TV was on
 */
	private int channel;
	private int prevChannel;
	private int maxC;
	private double volume;
/** Constructs the TV 
* @param
* the max channel based on givenChannelMax 
* Initial volume is set at half
* channel is set at 0
**/
	 public Television(int givenChannelMax){
		volume = 0.5;
		channel = 0;
		maxC = givenChannelMax-1;	
	}
/**
 * Saves current channel as prevChannel
 * moves the channel down 1 as long as the channel is not 0
 * if channel is at 0 it loops it to the top (maxC)
 */
	public void channelDown(){
		prevChannel = channel;
		if (channel > 0) 
			channel = channel - 1;
		else channel = maxC;		
	}
/**
 * Saves current channel as prevChannel
 * moves the channel up 1 as long as the channel is not already the max channel
 * if it is the maxChannel then it loops it to 0
 */
	public void channelUp(){
		prevChannel = channel;
		if (channel < maxC)
			channel = channel + 1;
		else channel = 0;
	}
/**
 * @return Channel X Volume XX%
 * Returns a string including Channel and Volume as a # between 0 - 100)
 * Example: "Channel 4 Volume 45%"
 */
	public String display(){
		
		return "Channel " + channel + " Volume " + Math.round(volume*100) + "%";
	}
/**
 * returns whatever Volume has been set as or changed to	
 * @return volume
 */
	public double getVolume(){
		
		return volume;
	}
/**
 * @return channel
 * returns whatever channel has been set as or changed to	
 */
	public int getChannel(){
		
		return channel;
	}
/**
 * uses the prevChannel variable used elsewhere and 
 * sets the current channel to be that value	
 */
	public void goToPreviousChannel(){
		channel = prevChannel;
	}
/**	
 * @param givenMax is taken in and a new maxChannel is set (maxC)
 * if the previous Channel or current Channel are greater than that value
 * they are brought down to that value (maxC)
 */
	public void resetChannelMax(int givenMax){
		maxC = givenMax -1;
		if (prevChannel > maxC) 
			prevChannel = maxC;
		if (channel > maxC)
			channel = maxC;		
		
	}
/**	
 * @param channelNumber is taken in and if that number is between 0 and maxC
 * the channel is set as that number
 * if it is above maxC it is brought down to maxC
 * and if it is below 0 it is raised to 0.
 * previous channel is also set.
 */
	public void setChannel(int channelNumber){
		prevChannel = channel;
		if(channelNumber >= 0 && channelNumber <= maxC)
			channel = channelNumber;
		else if (channelNumber < 0)
			channel = 0;
		else if (channelNumber > maxC)
			channel = maxC;	
		}
/** 
 * volume is subtracted by VOLUME_INCREMENT (0.07)
 * if that is below 0, it is set at 0.	
 */
	public void volumeDown(){
			volume =  Math.max(volume-VOLUME_INCREMENT, 0);
	}
/** 
 * volume is raised by VOLUME_INCREMENT (0.07)
 * if that is above 1.0, it is set at 1.0.	
 */	
	public void volumeUp(){
			volume =  Math.min(volume+VOLUME_INCREMENT, 100); 
			if (volume > 1.0) 
				volume = 1.0;
	}
}

