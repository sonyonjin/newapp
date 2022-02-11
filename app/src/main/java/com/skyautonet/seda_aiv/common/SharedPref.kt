package com.skyautonet.seda_aiv.common

import android.content.Context
import android.content.SharedPreferences

class SharedPref(mContext:Context) {
	companion object {
		const val PREF_NAME ="SeDA_AIV"
	}

	internal var mContext: Context
	internal var mSettingPrefs: SharedPreferences
	internal lateinit var mSettingPrefEditor:SharedPreferences.Editor
	init{
		this.mContext = mContext
		mSettingPrefs = mContext.getSharedPreferences(PREF_NAME, 0)// 0:PRIVATE
	}
	/**
	 * Used to store String data
	 *
	 * @param mKey
	 * @param mItem
	 */
	fun setDataInPref(mKey:String, mItem:String) {
		mSettingPrefEditor = mSettingPrefs.edit()
		mSettingPrefEditor.putString(mKey, mItem)
		mSettingPrefEditor.commit()
	}
	/**
	 * Used to get String data
	 *
	 * @param mKey
	 * @return String
	 */
	fun getDataFromPref(mKey:String):String {
		val mSplashData = mSettingPrefs.getString(mKey, "")
		return mSplashData.toString()
	}
	/**
	 * Used to store int data
	 *
	 * @param mKey
	 * @param mItem
	 */
	fun setInt(mKey:String, mItem:Int) {
		mSettingPrefEditor = mSettingPrefs.edit()
		mSettingPrefEditor.putInt(mKey, mItem)
		mSettingPrefEditor.commit()
	}
	/**
	 * Used to get int data stored in preference
	 *
	 * @param mKey
	 * @return int
	 */
	fun getInt(mKey:String):Int {
		val mPos = mSettingPrefs.getInt(mKey, 0)
		return mPos
	}
	// /**
	// * Used to clear Shared Preference
	// */
	fun clearAllPref() {
		mSettingPrefEditor = mSettingPrefs.edit()
		mSettingPrefEditor.clear()
		mSettingPrefEditor.commit()
	}
	/**
	 * Used to store boolean data
	 *
	 * @param mKey
	 * @param mItem
	 */
	fun setBoolean(mKey:String, mItem:Boolean) {
		mSettingPrefEditor = mSettingPrefs.edit()
		mSettingPrefEditor.putBoolean(mKey, mItem)
		mSettingPrefEditor.commit()
	}
	/**
	 * Used to get boolean data stored ibn shared preferences
	 *
	 * @param mKeys
	 * @return boolean
	 */
	fun getBoolean(mKeys:String):Boolean {
		val mPos = mSettingPrefs.getBoolean(mKeys, false)
		return mPos
	}
}

