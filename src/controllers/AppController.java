package controllers;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.security.SecureRandom;
import java.util.*;
import javafx.scene.media.*;
import javafx.animation.*;
import javafx.animation.Animation.Status;
import javafx.event.Event;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.*;


public class AppController implements Initializable{
	@FXML protected Button btnEnter, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0, btnA, btnB, btnC,btnD, btnE, btnF, btnG, btnH, btnI,
	btnJ, btnK ,btnL ,btnM, btnN, btnO, btnP, btnQ, btnR ,btnS, btnT, btnU ,btnV, btnW, btnX, btnY, btnZ, btnDash, btnEq, btnBs, btnTab, btnOsquare
	,btnCsquare, btnBSlash, btnCaps, btnSc, btnApos, btnShift, btnCom,btnDot, btnFslash, btnCtrl, btnFn, btnAlt, btnAltg, btnUp, btnDown,
	btnLeft, btnRight, btnSpace;
	
	@FXML private TextArea textArea;
	@FXML private FlowPane flowPane;
	@FXML private TextField textField;
	@FXML private RadioMenuItem rdBeginner, rdIntermediate, rdExpert,rdStop, rdPlay, rdPause,volAdjust, volMute;
	@FXML private Menu dLevel, gOption, gSound;
	@FXML private MenuItem exitBtn, bigBtn, intBtn;
	@FXML private Button btnSwitch;
	@FXML private Slider volSlider;
	@FXML private HBox hBoxTimer, hBoxHit;
	@FXML private Label timerLabel, hitLabel;
	@FXML private Text castText;
	
	private final String color = "purple";
	private String keyRColor = "radial-gradient( focus-angle 0.0deg, focus-distance 0.0%, center 50.0% 50.0%, radius 100.0%, rgb(51,0,51) 0.0, rgb(0,0,0) 100.0 )";
	
	private ArrayList<Text> text = new ArrayList<>();
	private ArrayList<String> beginner = new ArrayList<>(Arrays.asList("e","g","a"));
	protected int[] best_wpm = {0, 0, 0};
	protected int[] best_time = {60, 60, 60};
	protected int[] lastWpm = {0, 0, 0};
	protected int[] lastTime = {60, 60, 60};
	
	ArrayList<String> intermediateList = new ArrayList<>();
	ArrayList<String> beginnerList = new ArrayList<>();
	ArrayList<String> expertList = new 	ArrayList<>();
	
	private final int  Y_FROM= -350, Y_TO = -785;
	private int DURATION, startTime = 60, countDown = startTime, hitNum, textSize, wpm;
	private int gameLevel = -1, endTime, time = startTime-endTime, counter, textCount;
	
	private boolean isBeginner_LevelLoaded, isIntermediate_LevelLoaded, isExpert_LevelLoaded;
	
	private SecureRandom random = new SecureRandom();
	private Scanner input;
	private Timeline timeLine;
	
	private final String path = "resources/audio/snipersound.mp3";
	private File file = new File(path);
	private AudioClip hitClip = new AudioClip(file.toURI().toString());;
	private LinkedList<TranslateTransition> trans = new LinkedList<>();
	protected ObjectOutputStream outputRecord;
	private ObjectInputStream inputRecords;
	
	GameRecords gameRecords;
	
	public void exitGame() {
		Stage stage = (Stage) btn1.getScene().getWindow();
		boolean exit = DialogBox.confirmation("Confirm Exit", null, "Exit TouchTypist?", "Exit", "No, Thanks");
		if(exit) {
			openGame_Records();
			saveGame_Records();
			stage.close();
		}
	}
	
	@FXML public void keyPressed(KeyEvent ke) {
		if((ke.getCode().equals(KeyCode.ENTER)) && (textField.isFocused()) && (textField.getText()!=null)) {
			btnEnter.setStyle("-fx-background-color:"+color);
				for(int x =0;x<text.size(); x++) {
					if(textField.getText().matches(text.get(x).getText())) {
						hitClip.play();
						text.get(x).setVisible(false); 
						text.remove(x);
						
						hitNum++;
						
						hitLabel.setText(String.valueOf(hitNum).concat("/").concat(String.valueOf(textSize)));
					}
					
				}
			wpm = hitNum;
			textField.clear();
			if((hitNum==textSize) && (wpm>=best_wpm[gameLevel]) && (time<best_time[gameLevel])) {
				timeLine.stop();
				trans.forEach(text -> text.stop());
				
				
				DialogBox.information("NEW RECORD", null, "You reached "+wpm+" words in "+time+" seconds"
						+ "\nLast Record: "+lastWpm[gameLevel]+" words in "+lastTime[gameLevel]+" seconds\n\nBest Record: "+best_wpm[gameLevel]+" "
								+ "words in "+best_time[gameLevel]+" seconds");
				
				boolean replay = DialogBox.confirmation("PLAY AGAIN", null, "Would You Like To Play Again", "Play Again", "No, Thanks");
				if(replay) {
					
					timerLabel.setTextFill(Color.GREEN);
					lastWpm[gameLevel] = wpm;
					lastTime[gameLevel] = time;
					
					switch(gameLevel){
					case 0:
						if((wpm>=best_wpm[0]) && (time<=best_time[0])) {
							best_wpm[0] = wpm;
							best_time[0] = time;
							}
						
						beginner.clear();
						beginnerList.clear();
						try {
							input = new Scanner(Paths.get("resources/files/beginnerList.txt"));
							while(input.hasNext()) {
								String word = input.next();
								beginnerList.add(word);
								
								//input.close();
							}
						}catch(Exception fileNotFound){
							DialogBox.error("File not found", null, "The file does not exist");
						}
						
						beginner.addAll(beginnerList);
						addText();
						break;
						
					case 1:
						if((wpm>=best_wpm[1]) && (time<=best_time[1])) {
							best_wpm[1] = wpm;
							best_time[1] = time;
						}
						beginner.clear();
						intermediateList.clear();
						try {
							input = new Scanner(Paths.get("resources/files/intermediateList.txt"));
							while(input.hasNext()) {
								String word = input.next();
								intermediateList.add(word);
								
								//input.close();
							}
						}catch(Exception fileNotFound){
							DialogBox.error("File not found", null, "The file does not exist");
						}
						beginner.addAll(intermediateList);
						addText();
						break;
					case 2:
						if((wpm>=best_wpm[2]) && (time<=best_time[2])) {
							best_wpm[2] = wpm;
							best_time[2] = time;
						}
						beginner.clear();
						expertList.clear();
						try {
							input = new Scanner(Paths.get("resources/files/expertList.txt"));
							while(input.hasNext()) {
								String word = input.next();
								expertList.add(word);
								
							//	input.close();
							}
						}catch(Exception fileNotFound){
							DialogBox.error("File not found", null, "The file does not exist");
						}
						beginner.addAll(expertList);
						addText();
						break;
							
					}
					hitNum = 0;
					countDown = startTime;
					
					trans.forEach(text -> text.playFromStart());
					timerLabel.setText(String.valueOf(startTime));
					
					timeLine.play();
				}

			}
			
		}
		
		else if(ke.getCode().equals(KeyCode.ENTER)) {
			btnEnter.setStyle("-fx-background-color:"+color);
		}
		else if(ke.getCode().equals(KeyCode.DIGIT1)) {
				btn1.setTextFill(Color.BLACK);
				btn1.setStyle("-fx-background-color:"+color);
				
			}
		else if(ke.getCode().equals(KeyCode.DIGIT2)) {
				btn2.setTextFill(Color.BLACK);
				btn2.setStyle("-fx-background-color:"+color);
			}
		else if(ke.getCode().equals(KeyCode.DIGIT3)) {
				btn3.setTextFill(Color.BLACK);
				btn3.setStyle("-fx-background-color:"+color);
			}
		else if(ke.getCode().equals(KeyCode.DIGIT4)) {
				btn4.setTextFill(Color.BLACK);
				btn4.setStyle("-fx-background-color:"+color);
			}
		else if(ke.getCode().equals(KeyCode.DIGIT5)) {
				btn5.setTextFill(Color.BLACK);
				btn5.setStyle("-fx-background-color:"+color);
			}
		else if(ke.getCode().equals(KeyCode.DIGIT6)) {
				btn6.setTextFill(Color.BLACK);
				btn6.setStyle("-fx-background-color:"+color);
			}
		else if(ke.getCode().equals(KeyCode.DIGIT7)) {
				btn7.setTextFill(Color.BLACK);
				btn7.setStyle("-fx-background-color:"+color);
			}
		else	if(ke.getCode().equals(KeyCode.DIGIT8)) {
				btn8.setTextFill(Color.BLACK);
				btn8.setStyle("-fx-background-color:"+color);
			}
		else if(ke.getCode().equals(KeyCode.DIGIT9)) {
				btn9.setTextFill(Color.BLACK);
				btn9.setStyle("-fx-background-color:"+color);
			}
		else if(ke.getCode().equals(KeyCode.DIGIT0)) {
				btn0.setTextFill(Color.BLACK);
				btn0.setStyle("-fx-background-color:"+color);
			}
		else if(ke.getCode().equals(KeyCode.A)) {
				btnA.setTextFill(Color.BLACK);
				btnA.setStyle("-fx-background-color:"+color);
			}
		else if(ke.getCode().equals(KeyCode.B)) {
				btnB.setTextFill(Color.BLACK);
				btnB.setStyle("-fx-background-color:"+color);
			}
		else if(ke.getCode().equals(KeyCode.C)) {
				btnC.setTextFill(Color.BLACK);
				btnC.setStyle("-fx-background-color:"+color);
			}
		else if(ke.getCode().equals(KeyCode.D)) {
			btnD.setTextFill(Color.BLACK);
			btnD.setStyle("-fx-background-color:"+color);
		}
		else if(ke.getCode().equals(KeyCode.E)) {
				btnE.setTextFill(Color.BLACK);
				btnE.setStyle("-fx-background-color:"+color);
			}
		else if(ke.getCode().equals(KeyCode.F)) {
				btnF.setTextFill(Color.BLACK);
				btnF.setStyle("-fx-background-color:"+color);
			}
		else	if(ke.getCode().equals(KeyCode.G)) {
				btnG.setTextFill(Color.BLACK);
				btnG.setStyle("-fx-background-color:"+color);
			}
		else	if(ke.getCode().equals(KeyCode.H)) {
				btnH.setTextFill(Color.BLACK);	
				btnH.setStyle("-fx-background-color:"+color);
			}
		else	if(ke.getCode().equals(KeyCode.I)) {
				btnI.setTextFill(Color.BLACK);
				btnI.setStyle("-fx-background-color:"+color);
			}
		else	if(ke.getCode().equals(KeyCode.J)) {
				btnJ.setTextFill(Color.BLACK);
				btnJ.setStyle("-fx-background-color:"+color);
			}
		else	if(ke.getCode().equals(KeyCode.K)) {
				btnK.setTextFill(Color.BLACK);
				btnK.setStyle("-fx-background-color:"+color);
			}
		else	if(ke.getCode().equals(KeyCode.L)) {
			btnL.setTextFill(Color.BLACK);
				btnL.setStyle("-fx-background-color:"+color);
			}
		else	if(ke.getCode().equals(KeyCode.M)) {
			btnM.setTextFill(Color.BLACK);
				btnM.setStyle("-fx-background-color:"+color);
			}
		else	if(ke.getCode().equals(KeyCode.N)) {
				btnN.setTextFill(Color.BLACK);
				btnN.setStyle("-fx-background-color:"+color);
			}
		else	if(ke.getCode().equals(KeyCode.O)) {
				btnO.setTextFill(Color.BLACK);
				btnO.setStyle("-fx-background-color:"+color);
			}
		else	if(ke.getCode().equals(KeyCode.P)) {
			btnP.setTextFill(Color.BLACK);
				btnP.setStyle("-fx-background-color:"+color);
			}
		else	if(ke.getCode().equals(KeyCode.Q)) {
				btnQ.setTextFill(Color.BLACK);
				btnQ.setStyle("-fx-background-color:"+color);
			}
		else	if(ke.getCode().equals(KeyCode.R)) {
				btnR.setTextFill(Color.BLACK);
				btnR.setStyle("-fx-background-color:"+color);
			}
		else	if(ke.getCode().equals(KeyCode.S)) {
				btnS.setTextFill(Color.BLACK);
				btnS.setStyle("-fx-background-color:"+color);
			}
		else	if(ke.getCode().equals(KeyCode.T)) {
				btnT.setTextFill(Color.BLACK);
				btnT.setStyle("-fx-background-color:"+color);
			}
		else	if(ke.getCode().equals(KeyCode.U)) {
				btnU.setTextFill(Color.BLACK);
				btnU.setStyle("-fx-background-color:"+color);
			}
		else	if(ke.getCode().equals(KeyCode.V)) {
				btnV.setTextFill(Color.BLACK);
				btnV.setStyle("-fx-background-color:"+color);
			}
		else	if(ke.getCode().equals(KeyCode.W)) {
				btnW.setTextFill(Color.BLACK);
				btnW.setStyle("-fx-background-color:"+color);
			}
		else	if(ke.getCode().equals(KeyCode.X)) {
				btnX.setTextFill(Color.BLACK);
				btnX.setStyle("-fx-background-color:"+color);
			}
		else	if(ke.getCode().equals(KeyCode.Y)) {
				btnY.setTextFill(Color.BLACK);
				btnY.setStyle("-fx-background-color:"+color);
			}
		else	if(ke.getCode().equals(KeyCode.Z)) {
				btnZ.setTextFill(Color.BLACK);
				btnZ.setStyle("-fx-background-color:"+color);
			}
		else	if(ke.getCode().equals(KeyCode.BACK_SPACE)) {
				btnBs.setTextFill(Color.BLACK);
				btnBs.setStyle("-fx-background-color:"+color);
			}
		else    if(ke.getCode().equals(KeyCode.BACK_SLASH)) {
			btnBSlash.setTextFill(Color.BLACK);
				btnBSlash.setStyle("-fx-background-color:"+color);
			}
		else	if(ke.getCode().equals(KeyCode.CAPS)) {
			btnCaps.setTextFill(Color.BLACK);
				btnCaps.setStyle("-fx-background-color:"+color);
			}
		else	if(ke.getCode().equals(KeyCode.COLON)) {
				btnSc.setTextFill(Color.BLACK);
				btnSc.setStyle("-fx-background-color:"+color);
				System.out.println(ke.getCode());
			}
		else	if(ke.getCode().equals(KeyCode.TAB)) {
			btnTab.setTextFill(Color.BLACK);
			btnTab.setStyle("-fx-background-color:"+color);
		}
		else	if(ke.getCode().equals(KeyCode.BRACELEFT)) {
				btnApos.setTextFill(Color.BLACK);
				btnApos.setStyle("-fx-background-color:"+color);
				System.out.println(ke.getCode());
			}
		else	if(ke.getCode().equals(KeyCode.SHIFT)) {
				btnShift.setTextFill(Color.BLACK);
				btnShift.setStyle("-fx-background-color:"+color);
			}
		else	if(ke.getCode().equals(KeyCode.COMMA)) {
				btnCom.setTextFill(Color.BLACK);
				btnCom.setStyle("-fx-background-color:"+color);
			}
		else	if(ke.getCode().equals(KeyCode.PERIOD)) {
				btnDot.setTextFill(Color.BLACK);
				btnDot.setStyle("-fx-background-color:"+color);
			}
		else	if(ke.getCode().equals(KeyCode.SLASH)) {
			btnFslash.setTextFill(Color.BLACK);
				btnFslash.setStyle("-fx-background-color:"+color);
			}
		else	if(ke.getCode().equals(KeyCode.CONTROL)) {
				btnCtrl.setTextFill(Color.BLACK);
				btnCtrl.setStyle("-fx-background-color:"+color);
			}
		else	if(ke.getCode().equals(KeyCode.ALT)) {
				btnAlt.setTextFill(Color.BLACK);
				btnAlt.setStyle("-fx-background-color:"+color);
			}
		else	if(ke.getCode().equals(KeyCode.SPACE)) {
				btnSpace.setTextFill(Color.BLACK);
				btnSpace.setStyle("-fx-background-color:"+color);
			}
		else	if(ke.getCode().equals(KeyCode.ALT_GRAPH)) {
				btnAltg.setTextFill(Color.BLACK);
				btnAltg.setStyle("-fx-background-color:"+color);
			}
		else	if(ke.getCode().equals(KeyCode.DOWN)) {
				btnDown.setTextFill(Color.BLACK);
				btnDown.setStyle("-fx-background-color:"+color);
			}
		else	if(ke.getCode().equals(KeyCode.UP)) {
				btnUp.setTextFill(Color.BLACK);
				btnUp.setStyle("-fx-background-color:"+color);
			}
		else	if(ke.getCode().equals(KeyCode.MINUS)) {
			btnDash.setTextFill(Color.BLACK);
			btnDash.setStyle("-fx-background-color:"+color);
		}
		else	if(ke.getCode().equals(KeyCode.EQUALS)) {
			btnEq.setTextFill(Color.BLACK);
			btnEq.setStyle("-fx-background-color:"+color);
		}
		else	if(ke.getCode().equals(KeyCode.LEFT)) {
			btnLeft.setTextFill(Color.BLACK);
				btnLeft.setStyle("-fx-background-color:"+color);
			}
		else	if(ke.getCode().equals(KeyCode.RIGHT)) {
			btnRight.setTextFill(Color.BLACK);
				btnRight.setStyle("-fx-background-color:"+color);
			}
		else	if(ke.getCode().equals(KeyCode.BACK_QUOTE)) {
			btnOsquare.setTextFill(Color.BLACK);
				System.out.println(ke.getCode());
				btnOsquare.setStyle("-fx-background-color:"+color);
			}
		if((ke.getCode()==KeyCode.P) && (ke.isControlDown()) && (textField.isFocused())) {
			try {
			trans.forEach(text -> text.pause());
			timeLine.pause();
			
			textField.setEditable(false);
			textField.setText("Game Paused");
			}catch(Exception e) {
				DialogBox.error("TYPIST", null, "No game is currently in progress");
			}
		}
		if(ke.getCode().equals(KeyCode.A) && (ke.isControlDown())) {
			try{
			trans.forEach(text -> text.play());
			timeLine.play();
			
			textField.setEditable(true);
			textField.requestFocus();
			textField.setFocusTraversable(true);
			textField.clear();
			
			hitClip.setVolume(volSlider.getValue());
			volSlider.setVisible(false);
			}catch(Exception exc) {
				DialogBox.error("Game not loaded", null, "Game is currently unloaded, navigate to the game menu and choose a difficulty level to load game");
			}
			
		}
		if(ke.getCode().equals(KeyCode.X) && (ke.isControlDown()) && (textField).isFocused()) {
			try {
			trans.forEach(text -> text.stop());
			timeLine.stop();
			
			textField.setEditable(false);
			textField.setText("Game Stopped");
			}catch(Exception e) {
				DialogBox.error("TYPIST", null, "No game is currently in progress");
			}
		
		}
			
		}
	
		@FXML public void keyReleased(KeyEvent ke) {
				if(ke.getCode().equals(KeyCode.DIGIT1)) {
					btn1.setStyle("-fx-background-color:"+keyRColor);
				}
				else if(ke.getCode().equals(KeyCode.DIGIT2)) {
					btn2.setStyle("-fx-background-color:"+keyRColor);
				}
				else if(ke.getCode().equals(KeyCode.DIGIT3)) {
					btn3.setStyle("-fx-background-color:"+keyRColor);
				}
				else if(ke.getCode().equals(KeyCode.DIGIT4)) {
					btn4.setStyle("-fx-background-color:"+keyRColor);
				}
				else if(ke.getCode().equals(KeyCode.DIGIT5)) {
					btn5.setStyle("-fx-background-color:"+keyRColor);
				}
				else if(ke.getCode().equals(KeyCode.DIGIT6)) {
					btn6.setStyle("-fx-background-color:"+keyRColor);
				}
				else if(ke.getCode().equals(KeyCode.DIGIT7)) {
					btn7.setStyle("-fx-background-color:"+keyRColor);
				}
				else	if(ke.getCode().equals(KeyCode.DIGIT8)) {
					btn8.setStyle("-fx-background-color:"+keyRColor);
				}
				else if(ke.getCode().equals(KeyCode.DIGIT9)) {
					btn9.setStyle("-fx-background-color:"+keyRColor);
				}
				else if(ke.getCode().equals(KeyCode.DIGIT0)) {
					btn0.setStyle("-fx-background-color:"+keyRColor);
				}
				else if(ke.getCode().equals(KeyCode.A)) {
					btnA.setStyle("-fx-background-color:"+keyRColor);
				}
				else if(ke.getCode().equals(KeyCode.B)) {
					btnB.setStyle("-fx-background-color:"+keyRColor);
				}
				else if(ke.getCode().equals(KeyCode.C)) {
					btnC.setStyle("-fx-background-color:"+keyRColor);
				}
				else if(ke.getCode().equals(KeyCode.D)) {
					btnD.setStyle("-fx-background-color:"+keyRColor);
				}
				else if(ke.getCode().equals(KeyCode.E)) {
					btnE.setStyle("-fx-background-color:"+keyRColor);
				}
				else if(ke.getCode().equals(KeyCode.F)) {
					btnF.setStyle("-fx-background-color:"+keyRColor);
				}
				else	if(ke.getCode().equals(KeyCode.G)) {
					btnG.setStyle("-fx-background-color:"+keyRColor);
				}
				else	if(ke.getCode().equals(KeyCode.H)) {
					btnH.setStyle("-fx-background-color:"+keyRColor);
				}
				else	if(ke.getCode().equals(KeyCode.I)) {
					btnI.setStyle("-fx-background-color:"+keyRColor);
				}
				else	if(ke.getCode().equals(KeyCode.J)) {
					btnJ.setStyle("-fx-background-color:"+keyRColor);
				}
				else	if(ke.getCode().equals(KeyCode.K)) {
					btnK.setStyle("-fx-background-color:"+keyRColor);
				}
				else	if(ke.getCode().equals(KeyCode.L)) {
					btnL.setStyle("-fx-background-color:"+keyRColor);
				}
				else	if(ke.getCode().equals(KeyCode.M)) {
					btnM.setStyle("-fx-background-color:"+keyRColor);
				}
				else	if(ke.getCode().equals(KeyCode.N)) {
					btnN.setStyle("-fx-background-color:"+keyRColor);
				}
				else	if(ke.getCode().equals(KeyCode.O)) {
					btnO.setStyle("-fx-background-color:"+keyRColor);
				}
				else	if(ke.getCode().equals(KeyCode.P)) {
					btnP.setStyle("-fx-background-color:"+keyRColor);
				}
				else	if(ke.getCode().equals(KeyCode.Q)) {
					btnQ.setStyle("-fx-background-color:"+keyRColor);
				}
				else	if(ke.getCode().equals(KeyCode.R)) {
					btnR.setStyle("-fx-background-color:"+keyRColor);
				}
				else	if(ke.getCode().equals(KeyCode.S)) {
					btnS.setStyle("-fx-background-color:"+keyRColor);
				}
				else	if(ke.getCode().equals(KeyCode.T)) {
					btnT.setStyle("-fx-background-color:"+keyRColor);
				}
				else	if(ke.getCode().equals(KeyCode.U)) {
					btnU.setStyle("-fx-background-color:"+keyRColor);
				}
				else	if(ke.getCode().equals(KeyCode.V)) {
					btnV.setStyle("-fx-background-color:"+keyRColor);
				}
				else	if(ke.getCode().equals(KeyCode.W)) {
					btnW.setStyle("-fx-background-color:"+keyRColor);
				}
				else	if(ke.getCode().equals(KeyCode.X)) {
					btnX.setStyle("-fx-background-color:"+keyRColor);
				}
				else	if(ke.getCode().equals(KeyCode.Y)) {
					btnY.setStyle("-fx-background-color:"+keyRColor);
				}
				else	if(ke.getCode().equals(KeyCode.Z)) {
					btnZ.setStyle("-fx-background-color:"+keyRColor);
				}
				else	if(ke.getCode().equals(KeyCode.BACK_SPACE)) {
					btnBs.setStyle("-fx-background-color:"+keyRColor);
				}
				else    if(ke.getCode().equals(KeyCode.BACK_SLASH)) {
					btnBSlash.setStyle("-fx-background-color:"+keyRColor);
				}
				else	if(ke.getCode().equals(KeyCode.CAPS)) {
					btnCaps.setStyle("-fx-background-color:"+keyRColor);
				}
				else	if(ke.getCode().equals(KeyCode.MINUS)) {
					btnDash.setStyle("-fx-background-color:"+keyRColor);
				}
				else	if(ke.getCode().equals(KeyCode.EQUALS)) {
					btnEq.setStyle("-fx-background-color:"+keyRColor);
				}
				else	if(ke.getCode().equals(KeyCode.TAB)) {
					btnTab.setStyle("-fx-background-color:"+keyRColor);
				}
				else	if(ke.getCode().equals(KeyCode.COLON)) {
					btnSc.setStyle("-fx-background-color:"+keyRColor);
					System.out.println(ke.getCharacter());
				}
				else	if(ke.getCode().equals(KeyCode.QUOTE)) {
					btnApos.setStyle("-fx-background-color:"+keyRColor);
					System.out.println(ke.getCode());
				}
				else	if(ke.getCode().equals(KeyCode.SHIFT)) {
					btnShift.setStyle("-fx-background-color:"+keyRColor);
				}
				else	if(ke.getCode().equals(KeyCode.COMMA)) {
					btnCom.setStyle("-fx-background-color:"+keyRColor);
				}
				else	if(ke.getCode().equals(KeyCode.PERIOD)) {
					btnDot.setStyle("-fx-background-color:"+keyRColor);
				}
				else	if(ke.getCode().equals(KeyCode.SLASH)) {
					btnFslash.setStyle("-fx-background-color:"+keyRColor);
				}
				else	if(ke.getCode().equals(KeyCode.CONTROL)) {
					btnCtrl.setStyle("-fx-background-color:"+keyRColor);
				}
				else	if(ke.getCode().equals(KeyCode.ALT)) {
					btnAlt.setStyle("-fx-background-color:"+keyRColor);
				}
				else	if(ke.getCode().equals(KeyCode.SPACE)) {
					btnSpace.setStyle("-fx-background-color:"+keyRColor);
				}
				else	if(ke.getCode().equals(KeyCode.ALT_GRAPH)) {
					btnAltg.setStyle("-fx-background-color:"+keyRColor);
				}
				else	if(ke.getCode().equals(KeyCode.DOWN)) {
					btnDown.setStyle("-fx-background-color:"+keyRColor);
				}
				else	if(ke.getCode().equals(KeyCode.UP)) {
					btnUp.setStyle("-fx-background-color:"+keyRColor);
				}
				else	if(ke.getCode().equals(KeyCode.LEFT)) {
					btnLeft.setStyle("-fx-background-color:"+keyRColor);
				}
				else	if(ke.getCode().equals(KeyCode.RIGHT)) {
					btnRight.setStyle("-fx-background-color:"+keyRColor);
				}
				else	if(ke.getCode().equals(KeyCode.BRACELEFT)) {
					btnOsquare.setStyle("-fx-background-color:"+keyRColor);
				}
				else if(ke.getCode().equals(KeyCode.ENTER)) {
					btnEnter.setStyle("-fx-background-color:"+keyRColor);
				}
		}
			
		
			
		public void addText() {
			text.clear();
			flowPane.getChildren().clear();
			trans.clear();
			
			for(int k=0; k<beginner.size();k++) {
				text.add(new Text(beginner.get(k)));
				text.get(k).setFill(Color.DEEPPINK);
				text.get(k).setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR,20));
				text.get(k).setStrokeWidth(1);
				text.get(k).setStroke(Color.BLUE);
				
				flowPane.getChildren().add(text.get(k));
				flowPane.setHgap(5);
				trans.add(new TranslateTransition(Duration.millis(DURATION),text.get(k)));
					
			}
			
			for(int x=0; x<beginner.size(); x++)
			{
				int delayValue = random.nextInt(40);
				
				trans.get(x).setFromY(Y_FROM);
				trans.get(x).setToY(Y_TO);
				trans.get(x).setCycleCount(Animation.INDEFINITE);
				trans.get(x).setDelay(Duration.seconds(delayValue));
			}
			
		}
		public void gameOptions(Event e) {
			if(e.getSource()==rdPlay) {
				try {
				textField.setEditable(true);
				textField.requestFocus();
				textField.setFocusTraversable(true);
				textField.clear();
				
				hitClip.setVolume(volSlider.getValue());
				volSlider.setVisible(false);
				
				trans.forEach(text -> text.play());
				
				timeLine.playFromStart();
				}catch(Exception exception) {
					DialogBox.error("Game not loaded", null, "Game is currently unloaded, navigate to the game menu and choose a difficulty level to load game");
				}
				
			}else if(e.getSource()==rdPause) {
				if(gameLevel<0) {
					DialogBox.error("Game not loaded", null, "No game in progress");
				}else {
				trans.forEach(text -> text.pause());
				timeLine.pause();
				textField.setEditable(false);
				textField.setText("Game Paused");
				}
			}else {
				if(gameLevel<0) {
					DialogBox.error("Game not loaded", null, "No game in progress");
				}else {
				trans.forEach(text -> text.stop());
				timeLine.stop();
				textField.setEditable(false);
				textField.setText("Game Stopped");
				}
			}
		}
		public void gameLevel(Event e) {
			if(e.getSource()==rdBeginner) {
				if(!(isBeginner_LevelLoaded)) {
					beginner.clear();
				try {
					input = new Scanner(Paths.get("resources/files/beginnerList.txt"));
					while(input.hasNext()) {
						String word = input.next();
						beginnerList.add(word);
						
						//input.close();
					}
				}catch(Exception fileNotFound){
					DialogBox.error("File not found", null, "The file does not exist");
				}
				
				gameLevel = 0;
				textField.setEditable(true);
				beginner.addAll(beginnerList);
				beginnerList.clear();
				
				textSize=beginner.size();
				hitNum=0;
				DURATION = 12000;
				startTime = 60;
				timerLabel.setText(String.valueOf(startTime));
				timerLabel.setTextFill(Color.GREEN);
				hitLabel.setTextFill(Color.GREEN);
				
				hBoxTimer.setVisible(true);
				hBoxHit.setVisible(true);
				
				
				addText();
				timer();
				isBeginner_LevelLoaded = true;
				isIntermediate_LevelLoaded = false;
				isExpert_LevelLoaded = false;
				}else
					DialogBox.information("Level loaded", null, "The level is already loaded");
				
			}else if(e.getSource()==rdIntermediate) {
				if(!isIntermediate_LevelLoaded) {
				beginner.clear();
				try {
					input = new Scanner(Paths.get("resources/files/intermediateList.txt"));
					while(input.hasNext()) {
						String word = input.next();
						intermediateList.add(word);
						
						//input.close();
					}
				}catch(Exception fileNotFound){
					DialogBox.error("File not found", null, "The file does not exist");
				}
				gameLevel = 1;
				textField.setEditable(true);
				beginner.addAll(intermediateList);
				intermediateList.clear();
				
				textSize=beginner.size();
				hitNum=0;
				DURATION = 12000;
				startTime = 60;
				timerLabel.setText(String.valueOf(startTime));
				timerLabel.setTextFill(Color.GREEN);
				hitLabel.setTextFill(Color.GREEN);
				
				hBoxTimer.setVisible(true);
				hBoxHit.setVisible(true);
				addText();
				timer();
				
				isBeginner_LevelLoaded = false;
				isIntermediate_LevelLoaded = true;
				isExpert_LevelLoaded = false;
				}else
					DialogBox.information("Level loaded", null, "The level is already loaded");
				
			}else if(e.getSource()==rdExpert) {
				if(!isExpert_LevelLoaded) {
				beginner.clear();
				try {
					input = new Scanner(Paths.get("resources/files/expertList.txt"));
					while(input.hasNext()) {
						String word = input.next();
						expertList.add(word);
						
						//input.close();
					}
				}catch(Exception fileNotFound){
					DialogBox.error("File not found", null, "The file does not exist");
				}
				gameLevel = 2;
				textField.setEditable(true);
				beginner.addAll(expertList);
				expertList.clear();
				
				textSize = beginner.size();
				hitNum=0;
				DURATION = 12000;
				startTime = 60;
				timerLabel.setText(String.valueOf(startTime));
				timerLabel.setTextFill(Color.GREEN);
				hitLabel.setTextFill(Color.GREEN);
				
				hBoxTimer.setVisible(true);
				hBoxHit.setVisible(true);
				
				addText();
				timer();
				isBeginner_LevelLoaded = false;
				isIntermediate_LevelLoaded = false;
				isExpert_LevelLoaded = true;
				}else
					DialogBox.information("Level loaded", null, "The level is already loaded");
			}
		}
		public void onBtnSwitch_Clicked() {
			if(btnSwitch.getText().equalsIgnoreCase("Practice Mode")){
				try {
				if(timeLine.getStatus() == Status.RUNNING || timeLine.getStatus() == Status.PAUSED) {
					trans.forEach(text -> text.pause());
					timeLine.pause();
					textField.setPromptText("Game Paused");
					DialogBox.information1("TYPIST", null, "Game is currently in progress, stop game before proceeding to Practice Mode");
				
				
				
				}else {
					
				dLevel.setDisable(true);
				gOption.setDisable(true);
				gSound.setDisable(true);
				
				textField.clear();
				textField.setEditable(false);
				hBoxTimer.setVisible(false); 
				hBoxHit.setVisible(false);
				text.forEach(value -> value.setVisible(false));
				text.clear();
				trans.clear();
				gameLevel=0;
				
				textArea.requestFocus();
				textArea.setEditable(true);
				textArea.setPromptText("Click HERE to start typing");
				
				castText.setVisible(true);
				beginner.clear();
				btnSwitch.setText("Game Mode");
				}
			}catch(Exception e) {
				DialogBox.error("", null, "select a game level before exiting to the practice mode");
			}
				}
			else if(btnSwitch.getText().equalsIgnoreCase("Game Mode"))
			{
				dLevel.setDisable(false);
				gOption.setDisable(false);
				gSound.setDisable(false);
				
				textField.setEditable(true);
				textField.requestFocus();
				textField.setFocusTraversable(true);
				
				textArea.setEditable(false);
				textArea.setPromptText(null);
				textArea.clear();
				castText.setVisible(false);
				
				btnSwitch.setText("Practice Mode");
			}
		}
		
		public void volumeControl(Event e)
		{
			if(e.getSource()==volMute) {
				hitClip.setVolume(0.0);
			}else if(e.getSource()==volAdjust) {
				trans.forEach(text -> text.pause());
				volSlider.setVisible(true);
			}
		
		}
		public void timer() {
			timeLine = new Timeline();
			timeLine.setCycleCount(Timeline.INDEFINITE);
			countDown=startTime;
			
			timeLine.getKeyFrames().add(new KeyFrame(Duration.seconds(1),e ->  {
				countDown--;
				endTime= countDown;
				time = startTime-endTime;
				timerLabel.setText(String.valueOf(countDown));
				
				if(countDown==40 && countDown>=21) {
					timerLabel.setTextFill(Color.YELLOW);
				}else if(countDown<=20) {
					timerLabel.setTextFill(Color.RED);
				}
				
				if((countDown==0) || (hitNum==textSize && time>=best_time[gameLevel] && wpm<=best_wpm[gameLevel])) {
					timeLine.stop();
					trans.forEach(text -> text.stop());	
				
					
					if(endTime<=0) {
						DialogBox.information1("GAME OVER", null, "You reached "+wpm+" words in"
								+ " "+startTime+" seconds\nLast Record: "+lastWpm[gameLevel]+" words in "+lastTime[gameLevel]+" seconds\n\nBest Record: "+best_wpm[gameLevel]+" words in "+best_time[gameLevel]+" seconds");
					}else{
						DialogBox.information1("GAME OVER", null, "You reached "+wpm+" words in"
								+ " "+time+" seconds\nLast Record: "+lastWpm[gameLevel]+" words in "+lastTime[gameLevel]+" seconds\n\nBest Record: "+best_wpm[gameLevel]+" words in "+best_time[gameLevel]+" seconds");
					}
				
					lastWpm[gameLevel] = wpm;
					lastTime[gameLevel] = time;
					timerLabel.setTextFill(Color.RED);
					
					switch(gameLevel){
					case 0:
						if(wpm>best_wpm[0] && time<=best_time[0] ) {
							best_wpm[0] = wpm;
							best_time[0] = time;
						}
						beginner.clear();
						beginnerList.clear();
						try {
							input = new Scanner(Paths.get("resources/files/beginnerList.txt"));
							while(input.hasNext()) {
								String word = input.next();
								beginnerList.add(word);
								
								//input.close();
							}
						}catch(Exception fileNotFound){
							DialogBox.error("File not found", null, "The file does not exist");
						}
						
						beginner.addAll(beginnerList);
						addText();
						break;
						
					case 1:
						if((wpm>best_wpm[1]) && (time<=best_time[1])) {
							best_wpm[1] = wpm;
							best_time[1] = time;
						}
						beginner.clear();
						intermediateList.clear();
						try {
							input = new Scanner(Paths.get("resources/files/intermediateList.txt"));
							while(input.hasNext()) {
								String word = input.next();
								intermediateList.add(word);
								
								//input.close();
							}
						}catch(Exception fileNotFound){
							DialogBox.error("File not found", null, "The file does not exist");
						}
						beginner.addAll(intermediateList);
						addText();
						break;
						
					case 2:
						if((wpm>best_wpm[2]) && (endTime<=best_time[2])) {
							best_wpm[2] = wpm;
							best_time[2] = time;
						}
						beginner.clear();
						expertList.clear();
						try {
							input = new Scanner(Paths.get("resources/files/expertList.txt"));
							while(input.hasNext()) {
								String word = input.next();
								expertList.add(word);
								
							//	input.close();
							}
						}catch(Exception fileNotFound){
							DialogBox.error("File not found", null, "The file does not exist");
						}
						beginner.addAll(expertList);
						addText();
						break;
							
					}
					countDown = startTime;
					hitNum=0;
					
				}
			}));
			
		}
		
		public void openGame_Records() {
			try {
				outputRecord = new ObjectOutputStream(
						Files.newOutputStream(Paths.get("gameRecords.ser")));
			}catch(Exception e) {
				DialogBox.error("Error", null, "can not find file");
			}
		}
		
		public void saveGame_Records() {
			gameRecords = new GameRecords(best_time[0], best_wpm[0], best_time[1], best_wpm[1], best_time[2], best_wpm[2]);
			gameRecords.setBeginnerLastTime(lastTime[0]);
			gameRecords.setBeginnerLastWpm(lastWpm[0]);
			
			gameRecords.setIntermediateLastTime(lastTime[1]);
			gameRecords.setIntermediateLastWpm(lastWpm[1]);
			
			gameRecords.setExpertLastTime(lastTime[2]);
			gameRecords.setExpertLastWpm(lastWpm[2]);
			try {
				outputRecord.writeObject(gameRecords);
				
				outputRecord.close();
			}catch(Exception e) {
				System.out.println("error saving records");
			}
		}
		
		public void loadGame_Records() {
			try {
				inputRecords = new ObjectInputStream(Files.newInputStream(Paths.get("gameRecords.ser"))); //opens file
				gameRecords = (GameRecords)inputRecords.readObject(); //reads saved records from file
				
				best_time[0] = gameRecords.getBeginnerTime();
				best_wpm[0] = gameRecords.getBeginnerWpm();
				
				best_time[1] = gameRecords.getIntermediateTime();
				best_wpm[1] = gameRecords.getIntermediateWpm();
				
				best_time[2] = gameRecords.getExpertTime();
				best_wpm[2] = gameRecords.getExpertWpm();
				
				lastTime[0] = gameRecords.getBeginnerLastTime();
				lastWpm[0] = gameRecords.getBeginnerLastWpm();
				
				lastTime[1] = gameRecords.getIntermediateLastTime();
				lastWpm[1] = gameRecords.getIntermediateLastWpm();
				
				lastTime[2] = gameRecords.getExpertLastTime();
				lastWpm[2] = gameRecords.getExpertLastWpm();
				
				inputRecords.close(); //closes file
			}catch(Exception e) {
				DialogBox.error("Error", null, "error reading from file");
			}
		}

		@Override
		public void initialize(URL arg0, ResourceBundle arg1) {
			textCast();
			loadGame_Records();
			btnSwitch.setStyle("-fx-background-color:rgb(128,64,0)");
			
		}
		
		public void aboutApp() {
			StringBuilder messageBuilder = new StringBuilder();
			String message;
			try {
			input = new Scanner(Paths.get("resources/files/aboutTouchTypist.txt"));
			while(input.hasNext()) {
				messageBuilder.append(input.nextLine()+"\n");
			}
			message = messageBuilder.toString();
			DialogBox.information("Help", null, message);
			}catch(Exception e) {
				System.out.println(e);
			}
			
		}
		public void privacyPolicy() {
			String policy = "Version 1.0\nCopyright (C) 2017\nDennis Ogbonnaya\n\nDevDenky@gmail.com\nAll Rights Reserved.";
			DialogBox.information("TouchTypist", null, policy);
		}
		
		public void typingTips() {
			try {
				StringBuilder messageBuilder = new StringBuilder();
				String message;
				input= new Scanner(Paths.get("resources/files/keyTechnique.txt"));
				while(input.hasNext()) {
				messageBuilder.append(input.nextLine()+"\n");
				
			}
			message = messageBuilder.toString();
			DialogBox.messageBox("Proper keybording techniques", message);
			
			}catch(Exception e) {
				System.out.println(e);
			}
		}
		
		public void textCast() {
			TranslateTransition textTrans= new TranslateTransition(Duration.seconds(30), castText);
			
			ArrayList<String> textList = new ArrayList<>();
			castText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR,12));
			castText.setFill(Color.WHITE);
			Timeline timeLineCast = new Timeline();
			
			try {
			input = new Scanner(Paths.get("resources/files/keyTechnique.txt"));
			while(input.hasNext()) {
				String text = input.nextLine();
				textList.add(text);
			}
			
			castText.setText(textList.get(textCount));
			textCount=1;
			textTrans.setFromX(80);
			textTrans.setToX(-140);
			
			timeLineCast.getKeyFrames().add(new KeyFrame(Duration.seconds(1),e ->  {
				counter++;
				
				if(counter==30) {
					castText.setText(textList.get(textCount));
					textCount++;
				}
				if(counter==60) {
					castText.setText(textList.get(textCount));
					textCount++;
				}
				if(counter==90) {
					castText.setText(textList.get(textCount));
					textCount++;
				}
				if(counter==120) {
					castText.setText(textList.get(textCount));
					textCount++;
				}
				if(counter==150) {
					castText.setText(textList.get(textCount));
					textCount++;
				}
				if(counter==180) {
					castText.setText(textList.get(textCount));
					textCount++;
				}
				if(counter==210) {
					castText.setText(textList.get(textCount));
					textCount++;
				}
				if(counter==240) {
					castText.setText(textList.get(textCount));
					textCount++;
				}
				if(counter==270) {
					castText.setText(textList.get(textCount));
					textCount++;
				}
				if(counter==300) {
					castText.setText(textList.get(textCount));
					textCount++;
				}
				if(counter==330) {
					textCount=0;
					counter=0;
				}
				
			}));
				
			textTrans.setCycleCount(Animation.INDEFINITE);
			timeLineCast.setCycleCount(Animation.INDEFINITE);
			
			textTrans.play();
			timeLineCast.play();
			
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		public void resetRecords(Event event) {
			if(event.getSource()==bigBtn) {
				best_time[0] =60; best_wpm[0] = 0; lastTime[0] = 60; lastWpm[0] = 0;
			}else if(event.getSource()==intBtn) {
				best_time[1] =60; best_wpm[1] = 0; lastTime[1] = 60; lastWpm[1] = 0;
			}else
				best_time[2] =60; best_wpm[2] = 0; lastTime[2] = 60; lastWpm[2] = 0;
		}
}
