package program;

public class TimeStamp {
    private String event;
    private Long startTime;

    public String getEvent() {
        return event;
    }
    public String getShortEvent(){
        if (event.indexOf("_") < 0){
            return this.getEvent();
        }
        return event.substring(0,event.indexOf("_"));
    }
    public String getId(){
        if (event.indexOf("_") < 0){
            return null;
        }
        return event.substring(event.indexOf("_"),event.length());
    }
    public TimeStamp(String event, Long startTime, Long endTime){
        this.event = event;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    public void setEvent(String event) {
        this.event = event;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    private Long endTime;
    public String getInfo (){
        return "event: " + this.event + ", start = " + this.startTime + ", end = " + this.endTime;
    }
    public String getShortInfo (){
        String endTime = (this.endTime == - 1) ? "UNDEFINED" : Long.toString(this.endTime);
        return "event =  " + this.getShortEvent() + ", start = " + this.startTime + ", end = " + endTime;
    }
}
