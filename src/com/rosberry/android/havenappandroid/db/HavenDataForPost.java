/** 
 * Created by abrysov at Mar 23, 2012
 */
package com.rosberry.android.havenappandroid.db;

/**
 * @author abrysov
 *
 */
public class HavenDataForPost {
	

	private static Double	dLat 		= null;
	private static Double	dLong 		= null;

	private static String	userID 		= null;
	
	private static String	adres 		= null;
	private static String	deelgebied	= null;
	private static String	opmerkingen	= null;
	private static String	bestekspost	= null;
	private static String	bestekspostName	= null;
	private static String	niveau		= null;
	private static String	photoPath	= null;
	
	/**
	 *  
	 */
	public HavenDataForPost() {
//		dLat 		= 0.0;
//		dLong 		= 0.0;
		               
//		userID 		= "";
		               
//		adres 		= "";
//		deelgebied	= "";
//		opmerkingen	= "";
//		bestekspost	= "";
//		niveau		= "";
//		photoPath	= "";
	}

	/**
	 * @return the naam
	 */
	public String getUserDI() {
		return userID;
	}

	/**
	 * @param naam the naam to set
	 */
	public void setUserID(String _userID) {
		HavenDataForPost.userID = _userID;
	}

	/**
	 * @return the lLat
	 */
	public Double getLat() {
		return dLat;
	}

	/**
	 * @param lLat the lLat to set
	 */
	public void setLat(Double dLat) {
		HavenDataForPost.dLat = dLat;
	}

	/**
	 * @return the lLong
	 */
	public Double getLong() {
		return dLong;
	}

	/**
	 * @param lLong the lLong to set
	 */
	public void setLong(Double dLong) {
		HavenDataForPost.dLong = dLong;
	}

	/**
	 * @return the adres
	 */
	public String getAdres() {
		return adres;
	}

	/**
	 * @param adres the adres to set
	 */
	public void setAdres(String adres) {
		HavenDataForPost.adres = adres;
	}

	/**
	 * @return the deelgebied
	 */
	public String getDeelgebied() {
		return deelgebied;
	}

	/**
	 * @param deelgebied the deelgebied to set
	 */
	public void setDeelgebied(String deelgebied) {
		HavenDataForPost.deelgebied = deelgebied;
	}

	/**
	 * @return the opmerkingen
	 */
	public String getOpmerkingen() {
		return opmerkingen;
	}

	/**
	 * @param opmerkingen the opmerkingen(comments) to set
	 */
	public void setOpmerkingen(String opmerkingen) {
		HavenDataForPost.opmerkingen = opmerkingen;
	}

	/**
	 * @return the bestekspost
	 */
	public String getBestekspost() {
		return bestekspost;
	}

	/**
	 * @param bestekspost the bestekspost to set
	 */
	public void setBestekspost(String bestekspost) {
		HavenDataForPost.bestekspost = bestekspost;
	}

	/**
	 * @return the niveau
	 */
	public String getNiveau() {
		return niveau;
	}

	/**
	 * @param niveau the niveau to set
	 */
	public void setNiveau(String niveau) {
		HavenDataForPost.niveau = niveau;
	}

	/**
	 * @return the userPhoto
	 */
	public String getPhotoPath() {
		return photoPath;
	}

	/**
	 * @param userPhoto the userPhoto to set
	 */
	public void setPhotoPath(String _photoPath) {
		HavenDataForPost.photoPath = _photoPath;
	}
	
	/**
	 * 
	 * Check for a status fields
	 * if all complete (not null) return true
	 * @return
	 */
	
	public boolean allDataComplete () {
		boolean res = true;
		
		if (dLat == null){
			res = false;
		}else if(dLong == null) {
			res = false;
		}else if(adres == null) {
			res = false;
		}else if(bestekspost == null) {
			res = false;
		}else if(deelgebied == null) {
			res = false;
		}else if(userID == null) {
			res = false;
		}else if(niveau == null) {
			res = false;
		}else if(opmerkingen == null) {
			res = false;
		}else if(photoPath == null) {
			res = false;
		}
		
		
		return res;
	}
	
	public String viewAllDataString(){
		
		String res = "\nadres: " + adres + "\n";
		res += "bestekspost: " + bestekspost + "\n";
		res += "deelgebied: " + deelgebied + "\n";
		res += "userID: " + userID + "\n";
		res += "niveau: " + niveau + "\n";
		res += "opmerkingen: " + opmerkingen + "\n";
		res += "lat: " + dLat + "\n";
		res += "long: " + dLong + "\n";
		res += "photoPath: " + photoPath + "\n";
		
		return res;
	}

	/**
	 * @param name
	 */
	public void setBestekspostName(String _nameBestek) {

		HavenDataForPost.bestekspostName = _nameBestek;
	}
	
	/**
	 * @param name
	 */
	public String getBestekspostName() {

		return bestekspostName;
	}

}
