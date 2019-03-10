package models;

import java.io.Serializable;

public class GameRecords implements Serializable{
	private int beginnerTime;
	private int beginnerWpm;
	private int beginnerLastTime;
	private int beginnerLastWpm;

	private int intermediateTime;
	private int intermediateWpm;
	private int intermediateLastTime;
	private int intermediateLastWpm;

	private int expertTime;
	private int expertWpm;
	private int expertLastTime;
	private int expertLastWpm;


	public GameRecords(int beginnerTime, int beginnerWpm, int intermediateTime, int intermediateWpm, int expertTime, int  expertWpm) {
		this.beginnerTime = beginnerTime;
		this.beginnerWpm = beginnerWpm;
		this.intermediateTime = intermediateTime;
		this.intermediateWpm = intermediateWpm;
		this.expertTime = expertTime;
		this.expertWpm = expertWpm;
		
	}

	public void setBeginnerTime(int beginnerTime) {
		this.beginnerTime = beginnerTime;
	}
	public int getBeginnerTime(){
		return beginnerTime;
	}
	public void setIntermediateTime(int intermediateTime) {
		this.intermediateTime = intermediateTime;
	}
	public int getIntermediateTime(){
		return intermediateTime;
	}
	public void setExpertTime(int expertTime) {
		this.expertTime = expertTime;
	}
	public int getExpertTime(){
		return expertTime;
	}

	public void setBeginnerWpm(int beginnerWpm) {
		this.beginnerWpm = beginnerWpm;
	}
	public int getBeginnerWpm(){
		return beginnerWpm;
	}
	public void setIntermediateWpm(int intermediateWpm) {
		this.intermediateWpm = intermediateWpm;
	}
	public int getIntermediateWpm(){
		return intermediateWpm;
	}
	public void setExpertWpm(int expertWpm) {
		this.expertWpm = expertWpm;
	}
	public int getExpertWpm(){
		return expertWpm;
	}

	public void setBeginnerLastTime(int beginnerLastTime) {
		this.beginnerLastTime = beginnerLastTime;
	}
	public int getBeginnerLastTime(){
		return beginnerLastTime;
	}
	public void setIntermediateLastTime(int intermediateLastTime) {
		this.intermediateLastTime = intermediateLastTime;
	}
	public int getIntermediateLastTime(){
		return intermediateLastTime;
	}
	public void setExpertLastTime(int expertLastTime) {
		this.expertLastTime = expertLastTime;
	}
	public int getExpertLastTime(){
		return expertLastTime;
	}

	public void setBeginnerLastWpm(int beginnerLastWpm) {
		this.beginnerLastWpm = beginnerLastWpm;
	}
	public int getBeginnerLastWpm(){
		return beginnerLastWpm;
	}
	public void setIntermediateLastWpm(int intermediateLastWpm) {
		this.intermediateLastWpm = intermediateLastWpm;
	}
	public int getIntermediateLastWpm(){
		return intermediateLastWpm;
	}
	public void setExpertLastWpm(int expertLastWpm) {
		this.expertLastWpm = expertLastWpm;
	}
	public int getExpertLastWpm(){
		return expertLastWpm;
	}

	@Override public String toString() {
		return String.format("//d //d //d //d //d //d", beginnerTime, beginnerWpm, intermediateTime, intermediateWpm, expertTime, expertWpm);
	}
}
