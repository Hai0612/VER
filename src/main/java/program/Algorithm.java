package program;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Algorithm {
    private List<TimeStamp> timeStamps ;
    private List<Constraint> constraints;
    static  String logPath  = "";
    static  String constraintPath  = "";
    static String resultInfo = "";
    static boolean result = true;
    static boolean checkValidData = true;
    public static void main(String[] args) {
        logPath = "src/logs/convertedLog.txt";
        constraintPath = "src/logs/eventConstraints.txt";
        Algorithm algorithm = new Algorithm();
        algorithm.timeStamps = new ArrayList<>();
        algorithm.constraints = new ArrayList<>();
        algorithm.readEventConstraint(constraintPath);
        algorithm.readLogFile(logPath);
        if (!checkValidData){
            algorithm.showResult(resultInfo,true, true);
            return;
        }
        for (int i = 0; i < algorithm.constraints.size(); i++){
            if (!algorithm.constraints.get(i).getConstraint().isEmpty()){
                String constraint = algorithm.constraints.get(i).getConstraint();
                String eventA = algorithm.constraints.get(i).getEventA();
                String eventB = algorithm.constraints.get(i).getEventB();
                switch (constraint){
                    case "before":{
                        algorithm.timeStamps = algorithm.checkEventIsHappening(algorithm.timeStamps, algorithm.constraints.get(i).getEventA());
                        if (!algorithm.checkBeforeConstraint(algorithm.timeStamps, eventA, eventB)){
                            result = false;
                        }
                        break;
                    }
                    case "after":{
                        algorithm.timeStamps = algorithm.checkEventIsHappening(algorithm.timeStamps, algorithm.constraints.get(i).getEventB());
                        if (!algorithm.checkAfterConstraint(algorithm.timeStamps, eventA, eventB)){
                            result = false;
                        }
                        break;
                    }
                    case "equal":{
                        algorithm.timeStamps = algorithm.checkEventIsHappening(algorithm.timeStamps, algorithm.constraints.get(i).getEventA());
                        algorithm.timeStamps = algorithm.checkEventIsHappening(algorithm.timeStamps, algorithm.constraints.get(i).getEventB());
                        if (!algorithm.checkEqualConstraint(algorithm.timeStamps, eventA, eventB)){
                            result = false;
                        }
                        break;
                    }
                    case "meet":{
                        algorithm.timeStamps = algorithm.checkEventIsHappening(algorithm.timeStamps, algorithm.constraints.get(i).getEventA());
                        if (!algorithm.checkMeetConstraint(algorithm.timeStamps, eventA, eventB)){
                            result = false;
                        }
                        break;
                    }
                    case "met-by":{
                        algorithm.timeStamps = algorithm.checkEventIsHappening(algorithm.timeStamps, algorithm.constraints.get(i).getEventB());
                        if (!algorithm.checkMetByConstraint(algorithm.timeStamps, eventA, eventB)){
                            result = false;
                        }
                        break;
                    }
                    case "start":{
                        algorithm.timeStamps = algorithm.checkEventIsHappening(algorithm.timeStamps, algorithm.constraints.get(i).getEventA());
                        if (!algorithm.checkStartConstraint(algorithm.timeStamps, eventA, eventB)){
                            result = false;
                        }
                        break;
                    }
                    case "started-by":{
                        algorithm.timeStamps = algorithm.checkEventIsHappening(algorithm.timeStamps, algorithm.constraints.get(i).getEventB());
                        if (!algorithm.checkStartedByConstraint(algorithm.timeStamps, eventA, eventB)){
                            result = false;
                        }
                        break;
                    }
                    case "finish":{
                        algorithm.timeStamps = algorithm.checkEventIsHappening(algorithm.timeStamps, algorithm.constraints.get(i).getEventB());
                        algorithm.timeStamps = algorithm.checkEventIsHappening(algorithm.timeStamps, algorithm.constraints.get(i).getEventA());
                        if (!algorithm.checkFinishConstraint(algorithm.timeStamps, eventA, eventB)){
                            result = false;
                        }
                        break;
                    }
                    case "finished-by":{
                        algorithm.timeStamps = algorithm.checkEventIsHappening(algorithm.timeStamps, algorithm.constraints.get(i).getEventB());
                        algorithm.timeStamps = algorithm.checkEventIsHappening(algorithm.timeStamps, algorithm.constraints.get(i).getEventA());
                        if (!algorithm.checkFinishedByConstraint(algorithm.timeStamps, eventA, eventB)){
                            result = false;
                        }
                        break;
                    }
                    case "during":{
                        algorithm.timeStamps = algorithm.checkEventIsHappening(algorithm.timeStamps, algorithm.constraints.get(i).getEventA());
                        if (!algorithm.checkDuringConstraint(algorithm.timeStamps, eventA, eventB)){
                            result = false;
                        }
                        break;
                    }
                    case "contain":{
                        algorithm.timeStamps = algorithm.checkEventIsHappening(algorithm.timeStamps, algorithm.constraints.get(i).getEventB());
                        if (!algorithm.checkContainConstraint(algorithm.timeStamps, eventA, eventB)){
                            result = false;
                        }
                        break;
                    }
                    case "overlap":{
                        algorithm.timeStamps = algorithm.checkEventIsHappening(algorithm.timeStamps, algorithm.constraints.get(i).getEventA());
                        if (!algorithm.checkOverlapConstraint(algorithm.timeStamps, eventA, eventB)){
                            result = false;
                        }
                        break;
                    }
                    case "overlapped-by":{
                        algorithm.timeStamps = algorithm.checkEventIsHappening(algorithm.timeStamps, algorithm.constraints.get(i).getEventA());
                        if (!algorithm.checkOverlappedByConstraint(algorithm.timeStamps, eventA, eventB)){
                            result = false;
                        }
                        break;
                    }
                    default:break;
                }

            }
        }
        algorithm.showResult(resultInfo,result, false);
    }

    /**
     * Function to show result of tool
     * @param resultInfo
     * @param result
     */
    private void showResult(String resultInfo, Boolean result, Boolean checkValidData) {

        JFrame frame = new JFrame("VER: Verification of events at runtime");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(930,(resultInfo.split("--").length > 1 ? resultInfo.split("--").length * 52 + 200 : 2 * 160));
        JPanel panel = new JPanel(new FlowLayout());

        JTextField resultField = new JTextField(50);
        JTextArea causeField = new JTextArea((resultInfo.split("--").length > 1 ? (resultInfo.split("--").length * 3 + 3) : 11), 65);
        Insets insets = new Insets(10, 10, 10, 10);
        causeField.setMargin(insets);
        resultField.setFont(new Font("Arial", Font.BOLD, 18));
        causeField.setFont(new Font("Arial", Font.PLAIN, 16));
        resultField.setHorizontalAlignment(JTextField.CENTER);
        if (result) {
            resultField.setText("The system does not violate constraints");
            resultField.setDisabledTextColor(Color.GREEN);
        } else {
            resultField.setText("The system violates constraints");
            causeField.setText(resultInfo);
            resultField.setDisabledTextColor(Color.RED);
        }
        if (checkValidData){
            resultField.setText("");
            causeField.setText(resultInfo);
        }
        causeField.setEnabled(false);
        resultField.setEnabled(false);
        causeField.setDisabledTextColor(Color.BLACK);
        panel.add(resultField);
        panel.add(causeField);
        panel.setBackground(Color.DARK_GRAY);
        panel.setBackground(Color.LIGHT_GRAY);
        frame.getContentPane().add(panel);
        frame.setBackground(Color.DARK_GRAY);
        frame.getContentPane().setBackground(Color.RED);
        frame.setVisible(true);
    }

    /**
     * Function to check if event is happening or not
     * @param timeStamps
     * @param eventName
     * @return
     */
    private List<TimeStamp> checkEventIsHappening(List<TimeStamp> timeStamps, String eventName){
        for (int i = 0; i < timeStamps.size(); i++){
            if (timeStamps.get(i).getEvent().equals(eventName) && (timeStamps.get(i).getEndTime() == null || timeStamps.get(i).getStartTime() == null)){
                timeStamps.remove(i);
            }
        }
        return timeStamps;
    }
    /**
     * Function that checks if the 'before' constraint between event A and B is satisfied or not
     * @param timeStamps
     * @param eventE1
     * @param eventE2
     * @return
     */
    private boolean checkBeforeConstraint(List<TimeStamp> timeStamps, String eventE1, String eventE2){
        List<TimeStamp> timeStampsSE1 = new ArrayList<>();
        List<TimeStamp> timeStampsSE2 = new ArrayList<>();
        for (int i = 0; i < timeStamps.size() ; i++) {
            if (timeStamps.get(i).getShortEvent().equals(eventE1)) {
                timeStampsSE1.add(timeStamps.get(i));
            }
            if (timeStamps.get(i).getShortEvent().equals(eventE2)) {
                timeStampsSE2.add(timeStamps.get(i));
            }
        }
        for (int i = 0; i < timeStampsSE2.size(); i++){
            TimeStamp timeStampE2 = timeStampsSE2.get(i);
            TimeStamp timeStampE1 = causality(timeStampsSE1, timeStampE2);
            if (timeStampE1 != null){
                if (!checkBeforeRelation(timeStampE1, timeStampE2)){
                    return false;
                }
            }else{
                resultInfo +=  "\n-- Violate 'Before' constraint between two events " + eventE1 + " and " + eventE2+  "\n";
                resultInfo += "Cause: There is no occurrence of " +  eventE1 +" corresponding to  " + eventE2+ " : " + timeStampE2.getShortInfo() + "\n";
                return false;
            }
        }
        return true;
    }

    /**
     * Function to inform missed data
     * @param countE2Occurrence
     * @param event
     * @param missedEvent
     */
    private void notifyMissData(int countE2Occurrence , TimeStamp event, String missedEvent){
        if (countE2Occurrence == 0){
            resultInfo += "Cause: There is an occurrence of " + event.getInfo() + ", but no data event " + missedEvent + '\n';
        }
        if (countE2Occurrence > 1){
            resultInfo += "Cause: There is an occurrence of " + event.getInfo() + ", but there is more than one occurrence of the event " + missedEvent + '\n';
        }
    }
    /**
     * Function to check 'before' relation
     * @param timeStampE1
     * @param timeStampE2
     * @return
     */
    private boolean checkBeforeRelation(TimeStamp timeStampE1, TimeStamp timeStampE2){
        if (timeStampE1.getEndTime() != -1 && timeStampE1.getEndTime() < timeStampE2.getStartTime()){
            return true;
        }else{
            resultInfo +=  "\n-- Violate 'Before' constraint between two events " + timeStampE1.getShortEvent() + " and " + timeStampE2.getShortEvent()+  "\n";
            if (timeStampE1.getEndTime() == -1){
                resultInfo += "Cause: End time of event " +  timeStampE1.getShortEvent() + " is undefined at time that start time = " + timeStampE1.getStartTime() + "\n";
            }else{
                resultInfo += "Cause: End time of event " +  timeStampE1.getShortEvent() + " : " + timeStampE1.getEndTime() + "  is not less than start time of event " + timeStampE2.getShortEvent()+ " : " + timeStampE2.getStartTime() + "\n";
            }
            return false;
        }
    }


    /**
     * Function that checks if the 'equal' constraint between event A and B is satisfied or not
     * @param timeStamps
     * @param eventE1
     * @param eventE2
     * @return
     */
    private boolean checkEqualConstraint(List<TimeStamp> timeStamps, String eventE1, String eventE2){
        List<TimeStamp> timeStampsSE1 = new ArrayList<>();
        List<TimeStamp> timeStampsSE2 = new ArrayList<>();
        for (int i = 0; i < timeStamps.size() ; i++) {
            if (timeStamps.get(i).getShortEvent().equals(eventE1)) {
                timeStampsSE1.add(timeStamps.get(i));
            }
            if (timeStamps.get(i).getShortEvent().equals(eventE2)) {
                timeStampsSE2.add(timeStamps.get(i));
            }
        }
        for (int i = 0; i < timeStampsSE2.size(); i++){
            TimeStamp timeStampE2 = timeStampsSE2.get(i);
            TimeStamp timeStampE1 = causality(timeStampsSE1, timeStampE2);
            if (timeStampE1 != null){
                if (!checkEqualRelation(timeStampE1, timeStampE2)){
                    return false;
                }
            }else{
                resultInfo +=  "\n-- Violate 'Equal' constraint between two events " + eventE1 + " and " + eventE2+  "\n";
                resultInfo += "Cause: There is no occurrence of " +  eventE1 +" corresponding to  " + eventE2+ " : " + timeStampE2.getShortInfo() + "\n";
                return false;
            }
        }
        return true;
    }

    /**
     * Function to check 'equal' relation
     * @param timeStampE1
     * @param timeStampE2
     * @return
     */
    private boolean checkEqualRelation(TimeStamp timeStampE1, TimeStamp timeStampE2){
        if (timeStampE1.getEndTime().equals(timeStampE2.getEndTime()) && timeStampE1.getStartTime().equals(timeStampE2.getStartTime())){
            return true;
        }else{
            resultInfo +=  "\n-- Violate 'Equal' constraint between two events " + timeStampE1.getShortEvent() + " and " + timeStampE2.getShortEvent()+  "\n";

            if (!timeStampE1.getEndTime().equals(timeStampE2.getEndTime())){
                resultInfo += "Cause: End time of event " +  timeStampE1.getShortEvent() + " : " + timeStampE1.getEndTime() + "  is not equal to end time of event " + timeStampE2.getShortEvent()+ " : " + timeStampE2.getEndTime() + "\n";
            }
            if(!timeStampE1.getStartTime().equals(timeStampE2.getStartTime())){
                resultInfo += "Cause: Start time of event " +  timeStampE1.getShortEvent() + " : " + timeStampE1.getStartTime() + "  is not equal to start time of event " + timeStampE2.getShortEvent()+ " : " + timeStampE2.getStartTime() + "\n";

            }
            return false;
        }
    }

    /**
     * Function that checks if the 'meet' constraint between event A and B is satisfied or not
     * @param timeStamps
     * @param eventE1
     * @param eventE2
     * @return
     */
    private boolean checkMeetConstraint(List<TimeStamp> timeStamps, String eventE1, String eventE2){
        List<TimeStamp> timeStampsSE1 = new ArrayList<>();
        List<TimeStamp> timeStampsSE2 = new ArrayList<>();
        for (int i = 0; i < timeStamps.size() ; i++) {
            if (timeStamps.get(i).getShortEvent().equals(eventE1)) {
                timeStampsSE1.add(timeStamps.get(i));
            }
            if (timeStamps.get(i).getShortEvent().equals(eventE2)) {
                timeStampsSE2.add(timeStamps.get(i));
            }
        }
        for (int i = 0; i < timeStampsSE2.size(); i++){
            TimeStamp timeStampE2 = timeStampsSE2.get(i);
            TimeStamp timeStampE1 = causality(timeStampsSE1, timeStampE2);
            if (timeStampE1 != null){
                if (!checkMeetRelation(timeStampE1, timeStampE2)){
                    return false;
                }
            }else{
                resultInfo +=  "\n-- Violate 'Meet' constraint between two events " + eventE1 + " and " + eventE2+  "\n";
                resultInfo += "Cause: There is no occurrence of " +  eventE1 +" corresponding to  " + eventE2+ " : " + timeStampE2.getShortInfo() + "\n";
                return false;
            }
        }
        return true;
    }

    /**
     * Function to check 'meet' relation
     * @param timeStampE1
     * @param timeStampE2
     * @return
     */
    private boolean checkMeetRelation(TimeStamp timeStampE1, TimeStamp timeStampE2){
        if(timeStampE1.getEndTime() != -1 && timeStampE1.getEndTime().equals(timeStampE2.getStartTime())){
            return true;
        }else{
            resultInfo +=  "\n-- Violate 'Meet' constraint between two events " + timeStampE1.getShortEvent() + " and " + timeStampE2.getShortEvent()+  "\n";
            if (timeStampE1.getEndTime() == -1){
                resultInfo += "Cause: End time of event " +  timeStampE1.getShortEvent() + " is undefined at time that start time = " + timeStampE1.getStartTime() + "\n";
            }else{
                resultInfo += "Cause: End time of event " +  timeStampE1.getShortEvent() + " : " + timeStampE1.getEndTime() + "  is not equal to start time of event " + timeStampE2.getShortEvent()+ " : " + timeStampE2.getStartTime() + "\n";
            }
            return false;
        }
    }


    /**
     * Function to check 'overlap' relation
     * @param timeStampE1
     * @param timeStampE2
     * @return
     */
    private boolean checkOverlapRelation(TimeStamp timeStampE1, TimeStamp timeStampE2){
        if (timeStampE1.getEndTime() == -1 && timeStampE2.getEndTime() != -1){
            resultInfo +=  "\n-- Violate 'Overlap' constraint between two events " + timeStampE1.getShortEvent() + " and " + timeStampE2.getShortEvent()+  "\n";
            resultInfo += "Cause: End time of event " +  timeStampE1.getShortEvent() + " is undefined at time that" + timeStampE2.getShortEvent() + " has end time = " + timeStampE2.getEndTime() + "\n";
            return false;
        }
        if ((timeStampE1.getStartTime() >= timeStampE2.getStartTime()) || (timeStampE1.getEndTime() <= timeStampE2.getStartTime()) || (timeStampE1.getEndTime() >= timeStampE2.getEndTime() && timeStampE1.getEndTime() != -1)) {
            resultInfo += "\n-- Violate 'Overlap' constraint between two events " + timeStampE1.getShortEvent() + " and " + timeStampE2.getShortEvent() + "\n";
            if (timeStampE1.getStartTime() >= timeStampE2.getStartTime()) {
                resultInfo += "Cause: Start time of event " + timeStampE1.getShortEvent() + " : " + timeStampE1.getStartTime() + "  is not less than start time of event " + timeStampE2.getShortEvent() + " : " + timeStampE2.getStartTime() + "\n";
            }
            if (timeStampE1.getEndTime() <= timeStampE2.getStartTime()) {
                resultInfo += "Cause: End time of event " + timeStampE1.getShortEvent() + " : " + timeStampE1.getEndTime() + "  is not greater than start time of event " + timeStampE2.getShortEvent() + " : " + timeStampE2.getStartTime() + "\n";
            }
            if (timeStampE1.getEndTime() >= timeStampE2.getEndTime() && timeStampE1.getEndTime() != -1) {
                resultInfo += "Cause: End time of event " + timeStampE1.getShortEvent() + " is undefined at time that " + timeStampE2.getShortEvent()+" has end time = " + timeStampE1.getEndTime() + "\n";
            }
            return true;
        }
        return  true;
    }
    /**
     * Function that checks if the 'overlap' constraint between event A and B is satisfied or not
     * @param timeStamps
     * @param eventE1
     * @param eventE2
     * @return
     */
    private boolean checkOverlapConstraint(List<TimeStamp> timeStamps, String eventE1, String eventE2){
        List<TimeStamp> timeStampsSE1 = new ArrayList<>();
        List<TimeStamp> timeStampsSE2 = new ArrayList<>();
        for (int i = 0; i < timeStamps.size() ; i++) {
            if (timeStamps.get(i).getShortEvent().equals(eventE1)) {
                timeStampsSE1.add(timeStamps.get(i));
            }
            if (timeStamps.get(i).getShortEvent().equals(eventE2)) {
                timeStampsSE2.add(timeStamps.get(i));
            }
        }
        for (int i = 0; i < timeStampsSE2.size(); i++){
            TimeStamp timeStampE2 = timeStampsSE2.get(i);
            TimeStamp timeStampE1 = causality(timeStampsSE1, timeStampE2);
            if (timeStampE1 != null){
                if (!checkOverlapRelation(timeStampE1, timeStampE2)){
                    return false;
                }
            }else{
                resultInfo +=  "\n-- Violate 'Overlap' constraint between two events " + eventE1 + " and " + eventE2+  "\n";
                resultInfo += "Cause: There is no occurrence of " +  eventE1 +" corresponding to  " + eventE2+ " : " + timeStampE2.getShortInfo() + "\n";
                return false;
            }
        }
        return true;
    }

    /**
     * Function check 'overlapped-by' relation
     * @param timeStampE1
     * @param timeStampE2
     * @return
     */
    private boolean checkOverlappedByRelation(TimeStamp timeStampE1, TimeStamp timeStampE2) {
        if (timeStampE1.getEndTime() != -1 && timeStampE2.getEndTime() == -1) {
            resultInfo += "\n-- Violate 'Overlapped-by' constraint between two events " + timeStampE1.getShortEvent() + " and " + timeStampE2.getShortEvent() + "\n";
            resultInfo += "Cause: End time of event " + timeStampE2.getShortEvent() + " is undefined at time that" + timeStampE1.getShortEvent() + " has end time = " + timeStampE1.getEndTime() + "\n";
            return false;
        }
        if ((timeStampE1.getStartTime() <= timeStampE2.getStartTime()) || (timeStampE1.getEndTime() >= timeStampE2.getStartTime()) || (timeStampE1.getEndTime() <= timeStampE2.getEndTime() && timeStampE2.getEndTime() != -1)) {
            resultInfo += "\n-- Violate 'Overlapped-by' constraint between two events " + timeStampE1.getShortEvent() + " and " + timeStampE2.getShortEvent() + "\n";
            if (timeStampE1.getStartTime() <= timeStampE2.getStartTime()) {
                resultInfo += "Cause: Start time of event " + timeStampE1.getShortEvent() + " : " + timeStampE1.getStartTime() + "  is not greater than start time of event " + timeStampE2.getShortEvent() + " : " + timeStampE2.getStartTime() + "\n";
            }
            if (timeStampE1.getEndTime() >= timeStampE2.getStartTime()) {
                resultInfo += "Cause: End time of event " + timeStampE1.getShortEvent() + " : " + timeStampE1.getEndTime() + "  is not less than start time of event " + timeStampE2.getShortEvent() + " : " + timeStampE2.getStartTime() + "\n";
            }
            if (timeStampE1.getEndTime() <= timeStampE2.getEndTime() && timeStampE1.getEndTime() != -1) {
                resultInfo += "Cause: End time of event " + timeStampE1.getShortEvent() + " : " + timeStampE1.getEndTime() + "  is not greater than end time of event " + timeStampE2.getShortEvent() + " : " + timeStampE2.getEndTime() + "\n";
            }
            return true;
        }
        return true;
    }
    /**
     * Function that checks if the 'overlapped-by' constraint between event A and B is satisfied or not
     * @param timeStamps
     * @param eventE1
     * @param eventE2
     * @return
     */
    private boolean checkOverlappedByConstraint(List<TimeStamp> timeStamps, String eventE1, String eventE2){
        List<TimeStamp> timeStampsSE1 = new ArrayList<>();
        List<TimeStamp> timeStampsSE2 = new ArrayList<>();
        for (int i = 0; i < timeStamps.size() ; i++) {
            if (timeStamps.get(i).getShortEvent().equals(eventE1)) {
                timeStampsSE1.add(timeStamps.get(i));
            }
            if (timeStamps.get(i).getShortEvent().equals(eventE2)) {
                timeStampsSE2.add(timeStamps.get(i));
            }
        }
        for (int i = 0; i < timeStampsSE2.size(); i++){
            TimeStamp timeStampE2 = timeStampsSE2.get(i);
            TimeStamp timeStampE1 = causality(timeStampsSE1, timeStampE2);
            if (timeStampE1 != null){
                if (!checkOverlappedByRelation(timeStampE1, timeStampE2)){
                    return false;
                }
            }else{
                resultInfo +=  "\n-- Violate 'Overlapped-by' constraint between two events " + eventE1 + " and " + eventE2+  "\n";
                resultInfo += "Cause: There is no occurrence of " +  eventE1 +" corresponding to  " + eventE2+ " : " + timeStampE2.getShortInfo() + "\n";
                return false;
            }
        }
        return true;
    }
    /**
     * Function that checks if the 'start' constraint between event A and B is satisfied or not
     * @param timeStamps
     * @param eventE1
     * @param eventE2
     * @return
     */
    private boolean checkStartConstraint(List<TimeStamp> timeStamps, String eventE1, String eventE2){
        List<TimeStamp> timeStampsSE1 = new ArrayList<>();
        List<TimeStamp> timeStampsSE2 = new ArrayList<>();
        for (int i = 0; i < timeStamps.size() ; i++) {
            if (timeStamps.get(i).getShortEvent().equals(eventE1)) {
                timeStampsSE1.add(timeStamps.get(i));
            }
            if (timeStamps.get(i).getShortEvent().equals(eventE2)) {
                timeStampsSE2.add(timeStamps.get(i));
            }
        }
        for (int i = 0; i < timeStampsSE2.size(); i++){
            TimeStamp timeStampE2 = timeStampsSE2.get(i);
            TimeStamp timeStampE1 = causality(timeStampsSE1, timeStampE2);
            if (timeStampE1 != null){
                if (!checkStartRelation(timeStampE1, timeStampE2)){
                    return false;
                }
            }else{
                resultInfo +=  "\n-- Violate 'Start' constraint between two events " + eventE1 + " and " + eventE2+  "\n";
                resultInfo += "Cause: There is no occurrence of " +  eventE1 +" corresponding to  " + eventE2+ " : " + timeStampE2.getShortInfo() + "\n";
                return false;
            }
        }
        return true;
    }
    /**
     * HFunction that checks if the 'started-by' constraint between event A and B is satisfied or not
     * @param timeStamps
     * @param eventE1
     * @param eventE2
     * @return
     */
    private boolean checkStartedByConstraint(List<TimeStamp> timeStamps, String eventE1, String eventE2){
        List<TimeStamp> timeStampsSE1 = new ArrayList<>();
        List<TimeStamp> timeStampsSE2 = new ArrayList<>();
        for (int i = 0; i < timeStamps.size() ; i++) {
            if (timeStamps.get(i).getShortEvent().equals(eventE1)) {
                timeStampsSE1.add(timeStamps.get(i));
            }
            if (timeStamps.get(i).getShortEvent().equals(eventE2)) {
                timeStampsSE2.add(timeStamps.get(i));
            }
        }
        for (int i = 0; i < timeStampsSE2.size(); i++){
            TimeStamp timeStampE2 = timeStampsSE2.get(i);
            TimeStamp timeStampE1 = causality(timeStampsSE1, timeStampE2);
            if (timeStampE1 != null){
                if (!checkStartedByRelation(timeStampE1, timeStampE2)){
                    return false;
                }
            }else{
                resultInfo +=  "\n-- Violate 'Started-by' constraint between two events " + eventE1 + " and " + eventE2+  "\n";
                resultInfo += "Cause: There is no occurrence of " +  eventE1 +" corresponding to  " + eventE2+ " : " + timeStampE2.getShortInfo() + "\n";
                return false;
            }
        }
        return true;
    }
    /**
     * Function to check 'started-by' relation
     * @param timeStampE1
     * @param timeStampE2
     * @return
     */
    private boolean checkStartedByRelation(TimeStamp timeStampE1, TimeStamp timeStampE2){
        if (timeStampE1.getStartTime().equals(timeStampE2.getStartTime()) && timeStampE1.getEndTime() > timeStampE2.getEndTime()){
            return true;
        }else{
            resultInfo +=  "\n-- Violate 'Started-by' constraint between two events " + timeStampE1.getShortEvent() + " and " + timeStampE2.getShortEvent()+  "\n";
            if (!timeStampE1.getStartTime().equals(timeStampE2.getStartTime())){
                resultInfo += "Cause: Start time of event " +  timeStampE1.getShortEvent() + " : " + timeStampE1.getStartTime() + "  is not equal to start time of event " + timeStampE2.getShortEvent()+ " : " + timeStampE2.getStartTime() + "\n";
            }
            if(timeStampE1.getEndTime() <= timeStampE2.getEndTime()){
                resultInfo += "Cause: End time of event " +  timeStampE1.getShortEvent() + " : " + timeStampE1.getEndTime() + "  is not greater than end time of event " + timeStampE2.getShortEvent()+ " : " + timeStampE2.getEndTime() + "\n";
            }
            return false;
        }
    }
    /**
     * Function to check 'finished-by' relation
     * @param timeStampE1
     * @param timeStampE2
     * @return
     */
    private boolean checkFinishedByRelation(TimeStamp timeStampE1, TimeStamp timeStampE2){
        if (timeStampE1.getEndTime().equals(timeStampE2.getEndTime()) && timeStampE1.getStartTime() < timeStampE2.getStartTime()){
            return true;
        }else{
            resultInfo +=  "\n-- Violate 'Finished-by' constraint between two events " + timeStampE1.getShortEvent() + " and " + timeStampE2.getShortEvent()+  "\n";
            if (!timeStampE1.getEndTime().equals(timeStampE2.getEndTime())){
                resultInfo += "Cause: End time of event " +  timeStampE1.getShortEvent() + " : " + timeStampE1.getEndTime() + "  is not equal to end time of event " + timeStampE2.getShortEvent()+ " : " + timeStampE2.getEndTime() + "\n";
            }
            if(timeStampE1.getStartTime() >= timeStampE2.getStartTime()){
                resultInfo += "Cause: Start time of event " +  timeStampE1.getShortEvent() + " : " + timeStampE1.getStartTime() + "  is not less than start time of event " + timeStampE2.getShortEvent()+ " : " + timeStampE2.getStartTime() + "\n";
            }
            return false;
        }
    }
    /**
     * Function that checks if the 'finished-by' constraint between event A and B is satisfied or not
     * @param timeStamps
     * @param eventE1
     * @param eventE2
     * @return
     */
    private boolean checkFinishedByConstraint(List<TimeStamp> timeStamps, String eventE1, String eventE2){
        List<TimeStamp> timeStampsSE1 = new ArrayList<>();
        List<TimeStamp> timeStampsSE2 = new ArrayList<>();
        for (int i = 0; i < timeStamps.size() ; i++) {
            if (timeStamps.get(i).getShortEvent().equals(eventE1)) {
                timeStampsSE1.add(timeStamps.get(i));
            }
            if (timeStamps.get(i).getShortEvent().equals(eventE2)) {
                timeStampsSE2.add(timeStamps.get(i));
            }
        }
        for (int i = 0; i < timeStampsSE2.size(); i++){
            TimeStamp timeStampE2 = timeStampsSE2.get(i);
            TimeStamp timeStampE1 = causality(timeStampsSE1, timeStampE2);
            if (timeStampE1 != null){
                if (!checkFinishedByRelation(timeStampE1, timeStampE2)){
                    return false;
                }
            }else{
                resultInfo +=  "\n-- Violate 'Finished-by' constraint between two events " + eventE1 + " and " + eventE2+  "\n";
                resultInfo += "Cause: There is no occurrence of " +  eventE1 +" corresponding to  " + eventE2+ " : " + timeStampE2.getShortInfo() + "\n";
                return false;
            }
        }
        return true;
    }

    /**
     * Function to check 'contain' relation
     * @param timeStampE1
     * @param timeStampE2
     * @return
     */
    private boolean checkContainRelation(TimeStamp timeStampE1, TimeStamp timeStampE2){
        if (timeStampE1.getEndTime() != -1 && timeStampE2.getEndTime() == -1){
            resultInfo +=  "\n-- Violate 'During' constraint between two events " + timeStampE1.getShortEvent() + " and " + timeStampE2.getShortEvent()+  "\n";
            resultInfo += "Cause: End time of event " +  timeStampE2.getShortEvent() + " is undefined at time that" + timeStampE1.getShortEvent() + " has end time = " + timeStampE1.getEndTime() + "\n";
            return false;
        }
        if ((timeStampE1.getStartTime() >= timeStampE2.getStartTime()) || ((timeStampE1.getEndTime() <= timeStampE2.getEndTime()) && (timeStampE2.getEndTime() != -1))){
            resultInfo +=  "\n-- Violate 'During' constraint between two events " + timeStampE1.getShortEvent() + " and " + timeStampE2.getShortEvent()+  "\n";
            if (timeStampE1.getStartTime() >= timeStampE2.getStartTime()){
                resultInfo += "Cause: Start time of event " +  timeStampE1.getShortEvent() + " : " + timeStampE1.getStartTime() + "  is not less then start time of event " + timeStampE2.getShortEvent()+ " : " + timeStampE2.getStartTime() + "\n";
            }
            if(timeStampE1.getEndTime() <= timeStampE2.getEndTime()){
                resultInfo += "Cause: End time of event " +  timeStampE1.getShortEvent() + " : " + timeStampE1.getEndTime() + "  is not greater than end time of event " + timeStampE2.getShortEvent()+ " : " + timeStampE2.getEndTime() + "\n";
            }
            return false;
        }
        return true;
    }

    /**
     * Function that checks if the 'contain' constraint between event A and B is satisfied or not
     * @param timeStamps
     * @param eventE1
     * @param eventE2
     * @return
     */
    private boolean checkContainConstraint(List<TimeStamp> timeStamps, String eventE1, String eventE2){
        List<TimeStamp> timeStampsSE1 = new ArrayList<>();
        List<TimeStamp> timeStampsSE2 = new ArrayList<>();
        for (int i = 0; i < timeStamps.size() ; i++) {
            if (timeStamps.get(i).getShortEvent().equals(eventE1)) {
                timeStampsSE1.add(timeStamps.get(i));
            }
            if (timeStamps.get(i).getShortEvent().equals(eventE2)) {
                timeStampsSE2.add(timeStamps.get(i));
            }
        }
        for (int i = 0; i < timeStampsSE2.size(); i++){
            TimeStamp timeStampE2 = timeStampsSE2.get(i);
            TimeStamp timeStampE1 = causality(timeStampsSE1, timeStampE2);
            if (timeStampE1 != null){
                if (!checkContainRelation(timeStampE1, timeStampE2)){
                    return false;
                }
            }else{
                resultInfo +=  "\n-- Violate 'Contain' constraint between two events " + eventE1 + " and " + eventE2+  "\n";
                resultInfo += "Cause: There is no occurrence of " +  eventE1 +" corresponding to  " + eventE2+ " : " + timeStampE2.getShortInfo() + "\n";
                return false;
            }
        }
        return true;
    }
    /**
     * Function to check 'start' relation
     * @param timeStampE1
     * @param timeStampE2
     * @return
     */
    private boolean checkStartRelation(TimeStamp timeStampE1, TimeStamp timeStampE2){
        if (timeStampE1.getStartTime().equals(timeStampE2.getStartTime()) && timeStampE1.getEndTime() < timeStampE2.getEndTime()){
            return true;
        }else{
            resultInfo +=  "\n-- Violate 'Start' constraint between two events " + timeStampE1.getShortEvent() + " and " + timeStampE2.getShortEvent()+  "\n";
            if (!timeStampE1.getStartTime().equals(timeStampE2.getStartTime())){

                resultInfo += "Cause: Start time of event " +  timeStampE1.getShortEvent() + " : " + timeStampE1.getStartTime() + "  is not equal to start time of event " + timeStampE2.getShortEvent()+ " : " + timeStampE2.getStartTime() + "\n";
            }
            if(timeStampE1.getEndTime() >= timeStampE2.getEndTime()){
                resultInfo += "Cause: End time of event " +  timeStampE1.getShortEvent() + " : " + timeStampE1.getEndTime() + "  is not less than end time of event " + timeStampE2.getShortEvent()+ " : " + timeStampE2.getEndTime() + "\n";
            }
            return false;
        }
    }

    /**
     * Function that checks if the 'after' constraint between event A and B is satisfied or not
     * @param timeStamps
     * @param eventE1
     * @param eventE2
     * @return
     */
    private boolean checkAfterConstraint(List<TimeStamp> timeStamps, String eventE1, String eventE2){
        List<TimeStamp> timeStampsSE1 = new ArrayList<>();
        List<TimeStamp> timeStampsSE2 = new ArrayList<>();
        for (int i = 0; i < timeStamps.size() ; i++) {
            if (timeStamps.get(i).getShortEvent().equals(eventE1)) {
                timeStampsSE1.add(timeStamps.get(i));
            }
            if (timeStamps.get(i).getShortEvent().equals(eventE2)) {
                timeStampsSE2.add(timeStamps.get(i));
            }
        }
        for (int i = 0; i < timeStampsSE2.size(); i++){
            TimeStamp timeStampE2 = timeStampsSE2.get(i);
            TimeStamp timeStampE1 = causality(timeStampsSE1, timeStampE2);
            if (timeStampE1 != null){
                if (!checkAfterRelation(timeStampE1, timeStampE2)){
                    return false;
                }
            }else{
                resultInfo +=  "\n-- Violate 'After' constraint between two events " + eventE1 + " and " + eventE2+  "\n";
                resultInfo += "Cause: There is no occurrence of " +  eventE1 +" corresponding to  " + eventE2+ " : " + timeStampE2.getShortInfo() + "\n";
                return false;
            }
        }
        return true;
    }

    /**
     * Function to check 'after' relation
     * @param timeStampE1
     * @param timeStampE2
     * @return
     */
    private boolean checkAfterRelation(TimeStamp timeStampE1, TimeStamp timeStampE2){
        if (timeStampE2.getEndTime() != -1 && timeStampE1.getStartTime() > timeStampE2.getEndTime()){
            return true;
        }else{
            resultInfo +=  "\n-- Violate 'After' constraint between two events " + timeStampE1.getShortEvent() + " and " + timeStampE2.getShortEvent()+  "\n";
            if (timeStampE2.getEndTime() == -1){
                resultInfo += "Cause: End time of event " +  timeStampE1.getShortEvent() + " is undefined at time that start time = " + timeStampE1.getStartTime() + "\n";
            }else{
                resultInfo += "Cause: Start time of event " +  timeStampE1.getShortEvent() + " : " + timeStampE1.getStartTime() + "  is not greater than end time of event " + timeStampE2.getShortEvent()+ " : " + timeStampE2.getEndTime() + "\n";
            }
            return false;
        }
    }
    /**
     * Function that checks if the 'after' constraint between event A and B is satisfied or not
     * @param timeStamps
     * @param eventE1
     * @param eventE2
     * @return
     */
    private boolean checkMetByConstraint(List<TimeStamp> timeStamps, String eventE1, String eventE2){
        List<TimeStamp> timeStampsSE1 = new ArrayList<>();
        List<TimeStamp> timeStampsSE2 = new ArrayList<>();
        for (int i = 0; i < timeStamps.size() ; i++) {
            if (timeStamps.get(i).getShortEvent().equals(eventE1)) {
                timeStampsSE1.add(timeStamps.get(i));
            }
            if (timeStamps.get(i).getShortEvent().equals(eventE2)) {
                timeStampsSE2.add(timeStamps.get(i));
            }
        }
        for (int i = 0; i < timeStampsSE2.size(); i++){
            TimeStamp timeStampE2 = timeStampsSE2.get(i);
            TimeStamp timeStampE1 = causality(timeStampsSE1, timeStampE2);
            if (timeStampE1 != null){
                if (!checkMetByRelation(timeStampE1, timeStampE2)){
                    return false;
                }
            }else{
                resultInfo +=  "\n-- Violate 'Met-by' constraint between two events " + eventE1 + " and " + eventE2+  "\n";
                resultInfo += "Cause: There is no occurrence of " +  eventE1 +" corresponding to  " + eventE2+ " : " + timeStampE2.getShortInfo() + "\n";
                return false;
            }
        }
        return true;
    }

    /**
     * Function to check 'met-by' relation
     * @param timeStampE1
     * @param timeStampE2
     * @return
     */
    private boolean checkMetByRelation(TimeStamp timeStampE1, TimeStamp timeStampE2){
        if(timeStampE2.getEndTime() != -1 && timeStampE1.getStartTime().equals(timeStampE2.getEndTime())){
            return true;
        }else{
            resultInfo +=  "\n-- Violate 'Met-by' constraint between two events " + timeStampE1.getShortEvent() + " and " + timeStampE2.getShortEvent()+  "\n";

            if (timeStampE2.getEndTime() == -1){
                resultInfo += "Cause: End time of event " +  timeStampE2.getShortEvent() + " is undefined at time that start time = " + timeStampE2.getStartTime() + "\n";
            }else{
                resultInfo += "Cause: Start time of event " +  timeStampE1.getShortEvent() + " : " + timeStampE1.getStartTime() + "  is not greater than end time of event " + timeStampE2.getShortEvent()+ " : " + timeStampE2.getEndTime() + "\n";
            }
            return false;
        }
    }
    /**
     * Function that checks if the 'finish' constraint between event A and B is satisfied or not
     * @param timeStamps
     * @param eventE1
     * @param eventE2
     * @return
     */
    private boolean checkFinishConstraint(List<TimeStamp> timeStamps, String eventE1, String eventE2){
        List<TimeStamp> timeStampsSE1 = new ArrayList<>();
        List<TimeStamp> timeStampsSE2 = new ArrayList<>();
        for (int i = 0; i < timeStamps.size() ; i++) {
            if (timeStamps.get(i).getShortEvent().equals(eventE1)) {
                timeStampsSE1.add(timeStamps.get(i));
            }
            if (timeStamps.get(i).getShortEvent().equals(eventE2)) {
                timeStampsSE2.add(timeStamps.get(i));
            }
        }
        for (int i = 0; i < timeStampsSE2.size(); i++){
            TimeStamp timeStampE2 = timeStampsSE2.get(i);
            TimeStamp timeStampE1 = causality(timeStampsSE1, timeStampE2);
            if (timeStampE1 != null){
                if (!checkFinishRelation(timeStampE1, timeStampE2)){
                    return false;
                }
            }else{
                resultInfo +=  "\n-- Violate 'Finish' constraint between two events " + eventE1 + " and " + eventE2+  "\n";
                resultInfo += "Cause: There is no occurrence of " +  eventE1 +" corresponding to  " + eventE2+ " : " + timeStampE2.getShortInfo() + "\n";
                return false;
            }
        }
        return true;
    }

    /**
     * Function to check 'finish' relation
     * @param timeStampE1
     * @param timeStampE2
     * @return
     */
    private boolean checkFinishRelation(TimeStamp timeStampE1, TimeStamp timeStampE2){
        if (timeStampE1.getEndTime().equals(timeStampE2.getEndTime()) && timeStampE1.getStartTime() > timeStampE2.getStartTime()){
            return true;
        }else{
            resultInfo +=  "\n-- Violate 'Finish' constraint between two events " + timeStampE1.getShortEvent() + " and " + timeStampE2.getShortEvent()+  "\n";
            if (!timeStampE1.getEndTime().equals(timeStampE2.getEndTime())){
                resultInfo += "Cause: End time of event " +  timeStampE1.getShortEvent() + " : " + timeStampE1.getEndTime() + "  is not equal to end time of event " + timeStampE2.getShortEvent()+ " : " + timeStampE2.getEndTime() + "\n";
            }
            if(timeStampE1.getStartTime() <= timeStampE2.getStartTime()){
                resultInfo += "Cause: Start time of event " +  timeStampE1.getShortEvent() + " : " + timeStampE1.getStartTime() + "  is not greater than start time of event " + timeStampE2.getShortEvent()+ " : " + timeStampE2.getStartTime() + "\n";
            }
            return false;
        }
    }
    /**
     * Function that checks if the 'during' constraint between event A and B is satisfied or not
     * @param timeStamps
     * @param eventE1
     * @param eventE2
     * @return
     */
    private boolean checkDuringConstraint(List<TimeStamp> timeStamps, String eventE1, String eventE2) {
        List<TimeStamp> timeStampsSE1 = new ArrayList<>();
        List<TimeStamp> timeStampsSE2 = new ArrayList<>();
        for (int i = 0; i < timeStamps.size() ; i++) {
            if (timeStamps.get(i).getShortEvent().equals(eventE1)) {
                timeStampsSE1.add(timeStamps.get(i));
            }
            if (timeStamps.get(i).getShortEvent().equals(eventE2)) {
                timeStampsSE2.add(timeStamps.get(i));
            }
        }
        for (int i = 0; i < timeStampsSE2.size(); i++){
            TimeStamp timeStampE2 = timeStampsSE2.get(i);
            TimeStamp timeStampE1 = causality(timeStampsSE1, timeStampE2);
            if (timeStampE1 != null){
                if (!checkDuringRelation(timeStampE1, timeStampE2)){
                    return false;
                }
            }else{
                resultInfo +=  "\n-- Violate 'During' constraint between two events " + eventE1 + " and " + eventE2+  "\n";
                resultInfo += "Cause: There is no occurrence of " +  eventE1 +" corresponding to  " + eventE2+ " : " + timeStampE2.getShortInfo() + "\n";
                return false;
            }
        }
        return true;
    }

    /**
     * Function that check if an event is the cause of other event or not
     * @param timeStampsSE1
     * @param timeStampE2
     * @return
     */
    private TimeStamp causality(List<TimeStamp> timeStampsSE1, TimeStamp timeStampE2){
        for(int i = 0; i < timeStampsSE1.size() ; i++){
            if (timeStampsSE1.get(i).getId() != null && timeStampE2.getId() != null  && timeStampsSE1.get(i).getId().equals(timeStampE2.getId())){
                return timeStampsSE1.get(i);
            }
        }
        return null;
    }

    /**
     * Function to check 'during' relation
     * @param timeStampE1
     * @param timeStampE2
     * @return
     */
    private boolean checkDuringRelation(TimeStamp timeStampE1, TimeStamp timeStampE2){
        if (timeStampE1.getEndTime() == -1 && timeStampE2.getEndTime() != -1){
            resultInfo +=  "\n-- Violate 'During' constraint between two events " + timeStampE1.getShortEvent() + " and " + timeStampE2.getShortEvent()+  "\n";
            resultInfo += "Cause: End time of event " +  timeStampE1.getShortEvent() + " is undefined at time that " + timeStampE2.getShortEvent() + " has end time = " + timeStampE2.getEndTime() + "\n";
            return false;
        }
        if ((timeStampE1.getStartTime() <= timeStampE2.getStartTime()) || ((timeStampE1.getEndTime() >= timeStampE2.getEndTime()) && (timeStampE1.getEndTime() != -1))){
            resultInfo +=  "\n-- Violate 'During' constraint between two events " + timeStampE1.getShortEvent() + " and " + timeStampE2.getShortEvent()+  "\n";
            if (timeStampE1.getStartTime() <= timeStampE2.getStartTime()){
                resultInfo += "Cause: Start time of event " +  timeStampE1.getShortEvent() + " : " + timeStampE1.getStartTime() + "  is not greater than start time of event " + timeStampE2.getShortEvent()+ " : " + timeStampE2.getStartTime() + "\n";
            }
            if(timeStampE1.getEndTime() >= timeStampE2.getEndTime()){
                resultInfo += "Cause: End time of event " +  timeStampE1.getShortEvent() + " : " + timeStampE1.getEndTime() + "  is not less than end time of event " + timeStampE2.getShortEvent()+ " : " + timeStampE2.getEndTime() + "\n";
            }
            return true;
        }
        return true;
    }

    /**
     * Function that reads time log file of events in system
     * @param logPath
     */
    protected void readLogFile(String logPath){
        try {
            File myObj = new File(logPath);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine().trim();
                if (!data.equals("") && data != null){
                    String[] result = data.split(",");
                    if (Long.parseLong(result[1]) == -1 || (Long.parseLong(result[1]) > Long.parseLong(result[2]) && Long.parseLong(result[2]) > 0)){
                        checkValidData = false;
                        resultInfo +=  "\n Incorrect data format \n";
                        return;
                    }
                    this.timeStamps.add(new TimeStamp(result[0],Long.parseLong(result[1]),Long.parseLong(result[2])));
                }

            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Function that reads constraints between events in system
     * @param constraintPath
     */
    protected void readEventConstraint(String constraintPath){
        try {
            File file = new File(constraintPath);
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine().trim();
                if (!data.equals("") && data != null){
                    String[] result = data.split(",");
                    Constraint constraint = new Constraint(result[0],result[1],result[2]) ;
                    this.constraints.add(new Constraint(result[0],result[1],result[2]));
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}